package com.magiology.forge.events;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.client.post.PostProcessFX;
import com.magiology.client.renderers.FastNormalRenderer;
import com.magiology.client.renderers.Renderer;
import com.magiology.client.rendering.RandomAnimation;
import com.magiology.client.rendering.ShaderMultiTransformModel;
import com.magiology.client.shaders.ShaderHandler;
import com.magiology.client.shaders.effects.PositionAwareEffect;
import com.magiology.client.shaders.programs.InvisibleEffect;
import com.magiology.client.shaders.programs.MultiTransformShader;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
import com.magiology.util.m_extensions.ResourceLocationM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.MathUtil;
import com.magiology.util.statics.math.MatrixUtil;
import com.magiology.util.statics.math.PartialTicksUtil;

import jline.internal.Log;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderEvents{

	public static List<EntityLivingBase> invisibleEntitys=new ArrayList<>();
	public static final TemporaryFrame MAIN_FRAME_COPY=new TemporaryFrame(1, 1, true);
	static{
		MAIN_FRAME_COPY.setRednerHook(frame->{
			Framebuffer src=UtilC.getMC().getFramebuffer();
			frame.setSize(src.framebufferTextureWidth, src.framebufferTextureHeight).setUsingDepth(true);
			frame.frameBuffer.framebufferColor=src.framebufferColor;
			src.bindFramebufferTexture();
			double f=(float)src.framebufferTextureWidth;
			double f1=(float)src.framebufferTextureHeight;
			
            
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
		public int getModelListId(){
			if(modelId==-1)modelId=generateModel();
			return modelId;
		}
	};
	
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void render2Dscreem(RenderGameOverlayEvent e){
		TickEvents.worldRendering=false;
		if(e.getType()==ElementType.TEXT)RandomAnimation.rendner();
		
		PostProcessFX.processAll();
	}

	@SubscribeEvent()
	public void renderDrawBlockHighlight(DrawBlockHighlightEvent e){
		RayTraceResult hit=e.getTarget();
		
		TileEntityScreen highlighted=null;
		BlockPos pos=hit.getBlockPos();
		
		if(hit.typeOfHit==Type.BLOCK){
			TileEntity tile=UtilC.getTheWorld().getTileEntity(pos);
			if(tile instanceof TileEntityScreen){
				highlighted=(TileEntityScreen)tile;
			}
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
		//		EntityPlayer player = UtilC.getThePlayer();
		//		float playerOffsetX=-(float)(player.lastTickPosX+(player.posX-player.lastTickPosX)*e.getPartialTicks()),playerOffsetY=-(float)(player.lastTickPosY+(player.posY-player.lastTickPosY)*e.getPartialTicks()),playerOffsetZ=-(float)(player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*e.getPartialTicks());
		//		OpenGLM.pushMatrix();
		//		PrintUtil.println(UtilC.getMC().currentScreen);
		//		boolean useShaders=false;
		//		if(useShaders){
		//			Template.get().bind();
		//			if(UtilC.getWorldTime()%40==0){
		//				ShaderHandler.get().load();
		//			}
		//			Template.get().initUniforms();
		//		}
		//		OpenGLM.enableLighting();
		//		OpenGLM.disableLightmap();
		//		RenderHelper.disableStandardItemLighting();
		//		
		//		Minecraft.getMinecraft().getRenderManager().renderEntityStatic(player, PartialTicksUtil.partialTicks, false);
		//		Minecraft.getMinecraft().getRenderManager().doRenderEntity(player, 2, 0, 0, 0, PartialTicksUtil.partialTicks, false);
		//		Template.unbind();
		//		OpenGLM.popMatrix();
	}
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void renderWorldLast(RenderWorldLastEvent e){
		PostProcessFX.processAll();
	}
	
	private boolean flag;
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderEntityPre(RenderLivingEvent.Pre e){
		EntityLivingBase en=e.getEntity();
		if(en.isInvisibleToPlayer(UtilC.getThePlayer()))invisibleEntitys.add(en);
		Entity en1=e.getEntity();
		
		if(en1 instanceof EntityPlayer){
			EntityPlayer player=(EntityPlayer)en1;
			if(UtilC.getThePlayer().isSneaking())ShaderHandler.get().load();
			
			en1=player.getRidingEntity();
			
			OpenGLM.pushMatrix();
			
			OpenGLM.translate(0.5, 2, 0);
			OpenGLM.bindTexture(new ResourceLocationM("textures/blocks/CoalLevel2.png"));
			
			MultiTransformShader shader=ShaderHandler.get().getShader(MultiTransformShader.class);
			shader.drawModel(model);
			
			OpenGLM.popMatrix();
			
			if(en1 instanceof EntitySquid){
				EntitySquid squid=(EntitySquid)en1;
				flag=true;
				
				OpenGLM.pushMatrix();
				
				
				OpenGLM.translate(0, 3/16F, 0);
				//PartialTicksUtil.calculate(prevPos, pos) is equal to prevPos+(pos-prevPos)*partialTicks to smooth out transformation 
				//OpenGLM.rotateX,Y,Z https://github.com/LapisSea/Magiology/blob/1.9/src/main/java/com/magiology/util/statics/OpenGLM.java#L129-L146
				OpenGLM.rotateY(-PartialTicksUtil.calculate(squid.prevRenderYawOffset,squid.renderYawOffset));//-\
				OpenGLM.rotateX(-PartialTicksUtil.calculate(squid.prevSquidPitch,squid.squidPitch)+90);//--------===> match squid rotation
				OpenGLM.rotateZ(-PartialTicksUtil.calculate(squid.prevSquidYaw,squid.squidYaw));//---------------/
				//fix gap between riding entity and 
				OpenGLM.translate(0, -3/16F, 0);
				
				
				
			}else flag=false;
		}else flag=false;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderPlayerPost(RenderPlayerEvent.Post e){
		if(flag){
			OpenGLM.popMatrix();
		}
		
//		EntityPlayer player=e.getEntityPlayer();
//		if(player.isInvisibleToPlayer(UtilC.getThePlayer()))return;
//		
//		
//		ModelRenderer box=e.getRenderer().getMainModel().bipedLeftArm;//TODO: get selected box
//		
//		
//		if(!box.isHidden){
//			EnumFacing face=EnumFacing.DOWN;
//			
//			OpenGLM.pushMatrix();
//			OpenGLM.color(ColorF.WHITE);
//			OpenGLM.endOpaqueRendering();
//			OpenGLM.disableTexture2D();
//			OpenGLM.enableRescaleNormal();
//			OpenGLM.rotateY(-PartialTicksUtil.calculate(player.prevRenderYawOffset, player.renderYawOffset));
//			ModelBox lol=box.cubeList.get(0);
//			OpenGLM.translate(0, player.getEyeHeight()-(player.isSneaking()?5:3)/16F, 0);
//			GlStateManager.scale(0.94, 0.94, 0.94);
//			OpenGLM.scale(1, -1, -1);
//			OpenGLM.scale(1/16F);
//			OpenGLM.translate(box.offsetX, box.offsetY, box.offsetZ);
//			OpenGLM.translate(box.rotationPointX, box.rotationPointY, box.rotationPointZ);
//			
//			if(box.rotateAngleZ!=0.0F){
//				GlStateManager.rotate(box.rotateAngleZ*(180F/(float)Math.PI), 0.0F, 0.0F, 1.0F);
//			}
//			if(box.rotateAngleY!=0.0F){
//				GlStateManager.rotate(box.rotateAngleY*(180F/(float)Math.PI), 0.0F, 1.0F, 0.0F);
//			}
//			if(box.rotateAngleX!=0.0F){
//				GlStateManager.rotate(box.rotateAngleX*(180F/(float)Math.PI), 1.0F, 0.0F, 0.0F);
//			}
//			
//			AdvancedRenderer buff=new AdvancedRenderer();
//			switch(face){
//			case DOWN:{
//				buff.addVertexWithUV(lol.posX1, lol.posY2, lol.posZ2, 0, 1);
//				buff.addVertexWithUV(lol.posX2, lol.posY2, lol.posZ2, 1, 1);
//				buff.addVertexWithUV(lol.posX2, lol.posY2, lol.posZ1, 1, 0);
//				buff.addVertexWithUV(lol.posX1, lol.posY2, lol.posZ1, 0, 0);
//			}break;
//			case EAST:{
//				buff.addVertexWithUV(lol.posX1, lol.posY1, lol.posZ2, 0, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY1, lol.posZ2, 1, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY2, lol.posZ2, 1, 1);
//				buff.addVertexWithUV(lol.posX1, lol.posY2, lol.posZ2, 0, 1);
//			}break;
//			case NORTH:{
//				buff.addVertexWithUV(lol.posX1, lol.posY1, lol.posZ2, 0, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY1, lol.posZ2, 1, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY2, lol.posZ2, 1, 1);
//				buff.addVertexWithUV(lol.posX1, lol.posY2, lol.posZ2, 0, 1);
//			}break;
//			case SOUTH:{
//				buff.addVertexWithUV(lol.posX1, lol.posY1, lol.posZ2, 0, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY1, lol.posZ2, 1, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY2, lol.posZ2, 1, 1);
//				buff.addVertexWithUV(lol.posX1, lol.posY2, lol.posZ2, 0, 1);
//			}break;
//			case UP:{
//				buff.addVertexWithUV(lol.posX1, lol.posY1, lol.posZ1, 0, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY1, lol.posZ1, 1, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY1, lol.posZ2, 1, 1);
//				buff.addVertexWithUV(lol.posX1, lol.posY1, lol.posZ2, 0, 1);
//			}break;
//			case WEST:{
//				buff.addVertexWithUV(lol.posX1, lol.posY1, lol.posZ2, 0, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY1, lol.posZ2, 1, 0);
//				buff.addVertexWithUV(lol.posX2, lol.posY2, lol.posZ2, 1, 1);
//				buff.addVertexWithUV(lol.posX1, lol.posY2, lol.posZ2, 0, 1);
//			}break;
//			}
//			buff.draw();
//			OpenGLM.enableDepth();
//			OpenGLM.enableTexture2D();
//			OpenGLM.popMatrix();
//		}
//		
//		
	}
}
