package com.magiology.forge_powered.events;

import java.util.Queue;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Queues;
import com.magiology.client.shaders.ShaderHandler;
import com.magiology.client.shaders.effects.PositionAwareEffect;
import com.magiology.client.shaders.programs.Template;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderEvents{
	
	
	public static Queue<EntityLivingBase> invisibleEntitys=Queues.newArrayDeque();
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void render2Dscreem(RenderGameOverlayEvent e){
		
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderWorldLast(RenderWorldLastEvent e){
		PositionAwareEffect.updateViewTransformation();
		ParticleHandler.instance.renderParticles();
		if(!invisibleEntitys.isEmpty()){
			Template shader=ShaderHandler.getShader(Template.class);
			if(shader.shouldRender()){
				RenderManager renderManager=UtilC.getMC().getRenderManager();
				OpenGLM.pushMatrix();
				shader.activate();
				OpenGLM.translate(PartialTicksUtil.calculate(UtilC.getViewEntity()).mul(-1));
				//				OpenGLM.disableLightmap();
				for(EntityLivingBase entity:invisibleEntitys){
					Vec3M pos=PartialTicksUtil.calculate(entity);
					OpenGLM.enableBlend();
					GL11.glAlphaFunc(GL11.GL_GREATER, 0);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
					entity.setInvisible(false);
					renderManager.doRenderEntity(entity, pos.x, pos.y, pos.z, 0, PartialTicksUtil.partialTicks, false);
					entity.setInvisible(true);
				}
				OpenGLM.disableBlend();
				//				OpenGLM.enableLightmap();
				invisibleEntitys.clear();
				shader.deactivate();
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				OpenGLM.popMatrix();
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
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderEntityPre(RenderLivingEvent.Pre e){
		EntityLivingBase en=e.getEntity();
		if(en.isInvisibleToPlayer(UtilC.getThePlayer())) invisibleEntitys.add(en);
	}
}
