package com.magiology.client.render.entityrender;

import com.magiology.mcobjects.entitys.EntityBallOfEnergy;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilC;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

public class BallOfEnergyRenderer extends Render{

	ModelBase model;
	public BallOfEnergyRenderer(ModelBase model, float scale){
		super(TessUtil.getRM());
		this.model=model;
	}

	@Override
	public void doRender(Entity en, double var1, double var2, double var3, float var4,float partialTicks){
		EntityBallOfEnergy entity=(EntityBallOfEnergy)en;
		EntityPlayer player=UtilC.getMC().thePlayer;
		int time360=entity.age%90*4,offs=25,many=1;
		OpenGLM.pushMatrix();
		OpenGLM.translate(-(float)(player.lastTickPosX+(player.posX-player.lastTickPosX)*partialTicks),
				-(float)(player.lastTickPosY+(player.posY-player.lastTickPosY)*partialTicks), 
				-(float)(player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*partialTicks));
//		OpenGLM.translate(entity.posX, entity.posY, entity.posZ);
		OpenGLM.translate((float)(entity.lastTickPosX+(entity.posX-entity.lastTickPosX)*partialTicks),
				(float)(entity.lastTickPosY+(entity.posY-entity.lastTickPosY)*partialTicks), 
				(float)(entity.lastTickPosZ+(entity.posZ-entity.lastTickPosZ)*partialTicks));
		float smoothRotation=(time360-4)+4*partialTicks,size=-(float)(0.1-entity.time/100F);
		AxisAlignedBB cube=new AxisAlignedBB(-size,-size,-size,size,size,size);
		GL11U.setUpOpaqueRendering(1);
		OpenGLM.disableTexture2D();
		OpenGLM.color(1, 0.1, 0.1, 0.2);
		GL11U.glRotate(smoothRotation, 0, 0);
		TessUtil.drawCube(cube);
		GL11U.glRotate(0, -smoothRotation+offs*many++, 0);
		TessUtil.drawCube(cube);
		GL11U.glRotate(0, 0, -smoothRotation+offs*many++);
		TessUtil.drawCube(cube);
		GL11U.glRotate(0, smoothRotation+offs*many++, 0);
		TessUtil.drawCube(cube);
		GL11U.glRotate(smoothRotation+offs*many++, 0, 0);
		TessUtil.drawCube(cube);
		GL11U.glRotate(0, 0, smoothRotation+offs*many++);
		TessUtil.drawCube(cube);
		GL11U.glRotate(-smoothRotation+offs*many++, 0, smoothRotation+offs*many++);
		TessUtil.drawCube(cube);
		GL11U.glRotate(0, -smoothRotation+offs*many++, -smoothRotation+offs*many++);
		TessUtil.drawCube(cube);
		GL11U.glRotate(smoothRotation+offs*many++, -smoothRotation+offs*many++, 0);
		TessUtil.drawCube(cube);
		GL11U.endOpaqueRendering();
		OpenGLM.enableTexture2D();
		
		OpenGLM.popMatrix();
	
	
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity){
		return null;
	}
}
