package com.magiology.forge_powered.events;

import java.util.Queue;

import com.google.common.collect.Queues;
import com.magiology.client.shaders.ShaderHandler;
import com.magiology.client.shaders.effects.PositionAwareEffect;
import com.magiology.client.shaders.programs.InvisibleEffect;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
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
					renderManager.doRenderEntity(entity, pos.x, pos.y, pos.z, 0, PartialTicksUtil.partialTicks, false);
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
	private boolean flag;
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderEntityPre(RenderLivingEvent.Pre e){
		EntityLivingBase en=e.getEntity();
		if(en.isInvisibleToPlayer(UtilC.getThePlayer()))invisibleEntitys.add(en);
		Entity en1=e.getEntity();
		
		if(en1 instanceof EntityPlayer){
			EntityPlayer player=(EntityPlayer)en1;
			
			en1=player.getRidingEntity();
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
