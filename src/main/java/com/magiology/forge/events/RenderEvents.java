package com.magiology.forge.events;

import com.magiology.client.post.PostProcessFX;
import com.magiology.client.renderers.Renderer;
import com.magiology.client.shaders.effects.PositionAwareEffect;
import com.magiology.core.registry.init.ShadersM;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
import com.magiology.util.m_extensions.BlockM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.shader.Framebuffer;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class RenderEvents{
	
	public RenderEvents(){throw new UnsupportedOperationException();}
	
	public static final TemporaryFrame		MAIN_FRAME_COPY	=new TemporaryFrame(1, 1, true);
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
			Renderer.POS_UV.addVertex(f, 0, 500, 1, 1);
			Renderer.POS_UV.addVertex(0, 0, 500, 0, 1);
			
			Renderer.POS_UV.draw();
			
		});
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void render2Dscreem(RenderGameOverlayEvent e){
		TickEvents.worldRendering=false;
		//if(e.getType()==ElementType.TEXT)Development.rendnerRandomAnimation();
		
		PostProcessFX.processAll();
	}
	
	@SubscribeEvent()
	public static void renderDrawBlockHighlight(DrawBlockHighlightEvent e){
		RayTraceResult hit=e.getTarget();
		
		TileEntityScreen highlighted=null;
		BlockPos pos=hit.getBlockPos();
		
		if(hit.typeOfHit==Type.BLOCK){
			TileEntity tile=UtilC.getTheWorld().getTileEntity(pos);
			if(tile instanceof TileEntityScreen) highlighted=(TileEntityScreen)tile;
		}
		if(pos==null) TileEntityScreen.setHighlighted(highlighted, 0, 0, 0);
		else TileEntityScreen.setHighlighted(highlighted, (float)hit.hitVec.xCoord-pos.getX(), (float)hit.hitVec.yCoord-pos.getY(), (float)hit.hitVec.zCoord-pos.getZ());
		PostProcessFX.processAll();
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void renderWorldFirst(RenderWorldLastEvent e){
		PositionAwareEffect.updateViewTransformation();
		ParticleHandler.get().renderParticles();
		ShadersM.INVISIBLE.render();
		LogUtil.println("agfaf");
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void renderWorldLast(RenderWorldLastEvent e){
		PostProcessFX.processAll();
		Vec3M pos=new Vec3M(UtilC.getThePlayer().getPositionEyes(0));
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void renderEntityPre(RenderLivingEvent.Pre e){
		
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void renderPlayerPost(RenderPlayerEvent.Post e){
		
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void drawBlockHighlight(DrawBlockHighlightEvent e){
		RayTraceResult hit=e.getTarget();
		if(hit.typeOfHit==Type.MISS) return;
		
		EntityPlayer player=e.getPlayer();
		World world=player.worldObj;
		BlockPos pos=hit.getBlockPos();
		IBlockState state=world.getBlockState(pos);
		Block block=state.getBlock();
		
		if(block instanceof BlockM){
			BlockM blockM=(BlockM)block;
			OpenGLM.pushMatrix();
			
			OpenGLM.enableBlend();
			OpenGLM.BlendFunc.NORMAL.bind();
			OpenGLM.glLineWidth(2);
			OpenGLM.disableTexture2D();
			OpenGLM.depthMask(false);
			OpenGLM.color(0, 0, 0, 0.4F);
			
			OpenGLM.translate(PartialTicksUtil.calculate(player).mul(-1).add(pos));
			
			blockM.getBlockBounds().getHighlightRenderer().drawBounds(state, world, pos, hit);
			
			OpenGLM.enableTexture2D();
			ColorM.WHITE.bind();
			
			OpenGLM.popMatrix();
			e.setCanceled(true);
		}
	}
}
