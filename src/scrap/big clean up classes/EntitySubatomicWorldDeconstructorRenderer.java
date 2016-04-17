package com.magiology.client.render.entityrender;

import com.magiology.mcobjects.entitys.EntitySubatomicWorldDeconstructor;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

public class EntitySubatomicWorldDeconstructorRenderer extends Render{
	public EntitySubatomicWorldDeconstructorRenderer(float scale){
		super(TessUtil.getRM());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return null;
	}

	@Override
	public void doRender(Entity en, double var1, double var2, double var3, float var4,float partialTicks){
		try{
			EntitySubatomicWorldDeconstructor entity=(EntitySubatomicWorldDeconstructor)en;
			EntityPlayer player=U.getMC().thePlayer;
			OpenGLM.pushMatrix();
			OpenGLM.translate(-(float)(player.lastTickPosX+(player.posX-player.lastTickPosX)*partialTicks),
					-(float)(player.lastTickPosY+(player.posY-player.lastTickPosY)*partialTicks), 
					-(float)(player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*partialTicks));
//			OpenGLM.translate(entity.posX, entity.posY, entity.posZ);
			OpenGLM.translate((float)(entity.lastTickPosX+(entity.posX-entity.lastTickPosX)*partialTicks),
					(float)(entity.lastTickPosY+(entity.posY-entity.lastTickPosY)*partialTicks), 
					(float)(entity.lastTickPosZ+(entity.posZ-entity.lastTickPosZ)*partialTicks));
			float size=0.1F;
			AxisAlignedBB cube=new AxisAlignedBB(-size,-size,-size,size,size,size+1);
//			GL11H.SetUpOpaqueRendering(1);
			OpenGLM.disableTexture2D();
			OpenGLM.color(0.5, 0.5,1,1);
			
//			if(entity.targetHit){
//				for(int a=0;a<15;a++){
//					double[] a1=Helper.createBallXYZ(10, false);
//					TessHelper.drawLine(0, 0, 0, a1[0], a1[1], a1[2], 1, true, null, 0, 1);
//				}
//			}
			
//			GL11H.scaled(scale);
			GL11U.glRotate(Math.toDegrees(Math.atan2(entity.motionX+entity.motionZ, entity.motionY)), Math.toDegrees(Math.atan2(entity.motionX,entity.motionZ)), 0);
			TessUtil.drawCube(cube);
			
//			GL11H.EndOpaqueRendering();
			OpenGLM.enableTexture2D();
			
			OpenGLM.popMatrix();
		}catch(Exception e){
			
		}
		
	
	
	}
}