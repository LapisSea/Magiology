package com.magiology.forge.events;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.magiology.client.post.PostProcessFX;
import com.magiology.client.renderers.FastNormalRenderer;
import com.magiology.client.renderers.Renderer;
import com.magiology.client.rendering.ShaderMultiTransformModel;
import com.magiology.client.shaders.ShaderHandler;
import com.magiology.client.shaders.effects.PositionAwareEffect;
import com.magiology.client.shaders.programs.InvisibleEffect;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
import com.magiology.util.m_extensions.BlockM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.MathUtil;
import com.magiology.util.statics.math.MatrixUtil;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderEvents{

	public static List<EntityLivingBase> invisibleEntitys=new ArrayList<>();
	public static final TemporaryFrame MAIN_FRAME_COPY=new TemporaryFrame(1, 1, true);
	static{
		MAIN_FRAME_COPY.setRednerHook(frame->{
			Framebuffer src=UtilC.getMC().getFramebuffer();
			frame.setSize(src.framebufferTextureWidth, src.framebufferTextureHeight).setUsingDepth(true);
			frame.frameBuffer.framebufferColor=src.framebufferColor;
			src.bindFramebufferTexture();
			double f=src.framebufferTextureWidth;
			double f1=src.framebufferTextureHeight;
			
            
			Renderer.POS_UV.beginQuads();

			Renderer.POS_UV.addVertex(0, f1, 500, 0, 0);
			Renderer.POS_UV.addVertex(f, f1, 500, 1, 0);
			Renderer.POS_UV.addVertex(f, 0 , 500, 1, 1);
			Renderer.POS_UV.addVertex(0, 0 , 500, 0, 1);
			
			Renderer.POS_UV.draw();
			
			
		});
	}
	public static ShaderMultiTransformModel model=new ShaderMultiTransformModel(){
		int[] ids=new int[]{0,0,0,0};
		@Override
		public int[] getMatrixIds(){
			return new int[]{
					0,0,0,0,
					0,0,0,0,
					0,0,0,0,
					0,0,0,0,
					0,0,0,0,
					
					1,1,0,0,
					0,0,1,1,
					0,0,1,1,
					1,1,0,0,
					
					2,2,1,1,
					1,1,2,2,
					1,1,2,2,
					2,2,1,1,
					2,2,2,2
			};
		}
		
		@Override
		public Matrix4f[] getMatrices(){
			float tim=(float)((UtilC.getWorldTime()+(double)PartialTicksUtil.partialTicks)%3600)*2;
			Matrix4f mat1=MatrixUtil.createMatrix(new Vec3M()).rotateAt(new Vec3M(0.5,0.5,0.5),new Vec3M(MathUtil.sin(tim)*15,0,0)).finish();
			Matrix4f mat2=Matrix4f.mul(mat1, MatrixUtil.createMatrix(new Vec3M()).rotateAt(new Vec3M(0.5,0.5,0.5),new Vec3M(MathUtil.sin(tim)*15,0,0)).finish(), null);
			Matrix4f mat3=Matrix4f.mul(mat2, MatrixUtil.createMatrix(new Vec3M()).rotateAt(new Vec3M(0.5,0.5,0.5),new Vec3M(MathUtil.sin(tim)*15,0,0)).finish(), null);
			return new Matrix4f[]{mat1,mat2,mat3};
		}
		
		@Override
		public int generateModel(){
			int id=GLAllocation.generateDisplayLists(1);
			GlStateManager.glNewList(id, 4864);

			float p=5/16F;
			FastNormalRenderer buf=new FastNormalRenderer();
			buf.begin(true, buf.POS_UV);
			buf.add(0, p, 0, 0,0);
			buf.add(p, p, 0, 1,0);
			buf.add(p, 0, 0, 1,1);
			buf.add(0, 0, 0, 0,1);

			buf.add(0, 0, p, 0,1);
			buf.add(p, 0, p, 1,1);
			buf.add(p, p, p, 1,0);
			buf.add(0, p, p, 0,0);

			buf.add(0, 0, 0, 0,1);
			buf.add(0, 0, p, 1,1);
			buf.add(0, p, p, 1,0);
			buf.add(0, p, 0, 0,0);
			
			buf.add(p, p, 0, 0,0);
			buf.add(p, p, p, 1,0);
			buf.add(p, 0, p, 1,1);
			buf.add(p, 0, 0, 0,1);
			
			buf.add(0, 0, 0, 0,0);
			buf.add(p, 0, 0, 1,0);
			buf.add(p, 0, p, 1,1);
			buf.add(0, 0, p, 0,1);
			
			
			buf.add(0, p+p, 0, 0,0);
			buf.add(p, p+p, 0, 1,0);
			buf.add(p, 0+p, 0, 1,1);
			buf.add(0, 0+p, 0, 0,1);

			buf.add(0, 0+p, p, 0,1);
			buf.add(p, 0+p, p, 1,1);
			buf.add(p, p+p, p, 1,0);
			buf.add(0, p+p, p, 0,0);

			buf.add(0, 0+p, 0, 0,1);
			buf.add(0, 0+p, p, 1,1);
			buf.add(0, p+p, p, 1,0);
			buf.add(0, p+p, 0, 0,0);
			
			buf.add(p, p+p, 0, 0,0);
			buf.add(p, p+p, p, 1,0);
			buf.add(p, 0+p, p, 1,1);
			buf.add(p, 0+p, 0, 0,1);

			buf.add(0, p+p*2, 0, 0,0);
			buf.add(p, p+p*2, 0, 1,0);
			buf.add(p, 0+p*2, 0, 1,1);
			buf.add(0, 0+p*2, 0, 0,1);

			buf.add(0, 0+p*2, p, 0,1);
			buf.add(p, 0+p*2, p, 1,1);
			buf.add(p, p+p*2, p, 1,0);
			buf.add(0, p+p*2, p, 0,0);

			buf.add(0, 0+p*2, 0, 0,1);
			buf.add(0, 0+p*2, p, 1,1);
			buf.add(0, p+p*2, p, 1,0);
			buf.add(0, p+p*2, 0, 0,0);
			
			buf.add(p, p+p*2, 0, 0,0);
			buf.add(p, p+p*2, p, 1,0);
			buf.add(p, 0+p*2, p, 1,1);
			buf.add(p, 0+p*2, 0, 0,1);

			buf.add(0, p*3, p, 0,1);
			buf.add(p, p*3, p, 1,1);
			buf.add(p, p*3, 0, 1,0);
			buf.add(0, p*3, 0, 0,0);
			buf.draw();
			
			GlStateManager.glEndList();
			return id;
		}
		@Override
		public int getModelListId(){
			if(modelId==-1)modelId=generateModel();
			return modelId;
		}
	};
	
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void render2Dscreem(RenderGameOverlayEvent e){
		TickEvents.worldRendering=false;
//		if(e.getType()==ElementType.TEXT)RandomAnimation.rendner();
		
		PostProcessFX.processAll();
	}

	@SubscribeEvent()
	public void renderDrawBlockHighlight(DrawBlockHighlightEvent e){
		RayTraceResult hit=e.getTarget();
		
		TileEntityScreen highlighted=null;
		BlockPos pos=hit.getBlockPos();
		
		if(hit.typeOfHit==Type.BLOCK){
			TileEntity tile=UtilC.getTheWorld().getTileEntity(pos);
			if(tile instanceof TileEntityScreen) highlighted=(TileEntityScreen)tile;
		}
		if(pos==null)TileEntityScreen.setHighlighted(highlighted,0,0,0);
		else TileEntityScreen.setHighlighted(highlighted,(float)hit.hitVec.xCoord-pos.getX(),(float)hit.hitVec.yCoord-pos.getY(),(float)hit.hitVec.zCoord-pos.getZ());
		PostProcessFX.processAll();
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderWorldFirst(RenderWorldLastEvent e){
		PositionAwareEffect.updateViewTransformation();
		ParticleHandler.get().renderParticles();
		if(!invisibleEntitys.isEmpty()){
			InvisibleEffect shader=ShaderHandler.getShader(InvisibleEffect.class);
			if(shader.shouldRender()){
				RenderManager renderManager=UtilC.getMC().getRenderManager();
				GlStateManager.pushMatrix();
				shader.activate();
				OpenGLM.translate(PartialTicksUtil.calculate(UtilC.getViewEntity()).mul(-1));
//				OpenGLM.disableLightmap();
				for(EntityLivingBase entity:invisibleEntitys){
					Vec3M pos=PartialTicksUtil.calculate(entity);
					GlStateManager.enableBlend();
					GL11.glAlphaFunc(GL11.GL_GREATER, 0);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
					entity.setInvisible(false);
					renderManager.doRenderEntity(entity, pos.x(), pos.y(), pos.z(), 0, PartialTicksUtil.partialTicks, false);
					entity.setInvisible(true);
				}
				GlStateManager.disableBlend();
//				OpenGLM.enableLightmap();
				invisibleEntitys.clear();
				shader.deactivate();
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.popMatrix();
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void renderWorldLast(RenderWorldLastEvent e){
		PostProcessFX.processAll();
		Vec3M pos=new Vec3M(UtilC.getThePlayer().getPositionEyes(0));
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderEntityPre(RenderLivingEvent.Pre e){
		
		
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderPlayerPost(RenderPlayerEvent.Post e){
		
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void drawBlockHighlight(DrawBlockHighlightEvent e){
		RayTraceResult hit=e.getTarget();
		if(hit.typeOfHit==Type.MISS)return;
		
		EntityPlayer player=e.getPlayer();
		World world=player.worldObj;
		BlockPos pos=hit.getBlockPos();
		IBlockState state=world.getBlockState(pos);
		Block block=state.getBlock();
		
		if(block instanceof BlockM){
			BlockM blockM=(BlockM)block;
			OpenGLM.pushMatrix();
			
			GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
			OpenGLM.color(0, 0, 0, 0.4F);
            
			OpenGLM.translate(PartialTicksUtil.calculate(player).mul(-1).add(pos));
			blockM.getBoundingBox().drawBoundsOutline(state, world, pos);
			
			OpenGLM.enableTexture2D();
			ColorM.WHITE.bind();
			
			OpenGLM.popMatrix();
			e.setCanceled(true);
//			LogUtil.printStackTrace();
		}
	}
}

