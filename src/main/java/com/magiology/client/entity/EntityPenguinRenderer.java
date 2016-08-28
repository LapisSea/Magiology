package com.magiology.client.entity;

import java.util.ArrayList;

import com.magiology.mc_objects.entitys.EntityPenguin;
import com.magiology.util.m_extensions.ResourceLocationM;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class EntityPenguinRenderer extends RenderLiving<EntityPenguin>{
	
	private static ResourceLocation peng=new ResourceLocationM("textures/entity/Penguin.png");
	
	public EntityPenguinRenderer(RenderManager rendermanagerIn){
		super(rendermanagerIn, new MainModel(), 5/16F);
		shadowOpaque=0.5F;
	}
	
	private static class MainModel extends ModelBase{
		
		
		private ModelRenderer 
			Helmet, MHead, MBeak, MBody, JBody, 
			MLeft_leg_1,  MLeft_leg_2,  MLeft_arm_2,  MLeft_arm_1,  JLeft_stick_1,  JLeft_stick_2,  JLeft_jet, JLeft_tank,
			MRight_leg_1, MRight_leg_2, MRight_arm_2, MRight_arm_1, JRight_stick_1, JRight_stick_2, JRight_jet,JRight_tank;
		
		public MainModel(){
			textureWidth=64;
			textureHeight=32;
			

			Helmet=			newBox(26,0,	-2.5F,-4.5F,-2.5F, 5,5,5,	 0,0,0,				0,0,0);
			MHead=			newBox(0,10,	-2,-4,-2, 4,4,4,  			 0,0,0,				0,0,0);
			MBeak=			newBox(0,21,	 0,0,0, 1,1,1,				-0.5F,-2,-3,		0,0,0);
			MRight_leg_2=	newBox(10,10,	-0,1,-0F, 2,0,2,			-1F, 0F, -1.5F,		0,0,0);
			MLeft_leg_2=	newBox(10,12,	-0,1,-0F, 2,0,2,			-1F, 0F, -1.5F,		0,0,0);
			MLeft_leg_1=	newBox(0,12,	-0.5F,0,-0.5F, 1,1,1,		-1.5F,9F,2F,		0,0,0);
			MRight_leg_1=	newBox(0,10,	-0.5F,0,-0.5F, 1,1,1,		 1.5F,9F,2F,		0,0,0);
			MRight_arm_2=	newBox(8,4,		-3.5F,0,-2.5F, 1,3,3,		 3.5F,4,1,			0,0,0);
			MLeft_arm_2=	newBox(0,4,		-0.5F,0,-1.5F, 1,3,3,		-0.5F,4,0,			0,0,0);
			MLeft_arm_1=	newBox(16,9,	-1,0,-1.5F, 1,4,3,			-3,0,1,				0,0,0);
			MRight_arm_1=	newBox(17,16,	 0,0,-1.5F, 1,4,3,			 3,0,1,				0,0,0);
			MBody=			newBox(0,18,	-3,0,-1, 6,9,5,				 0,0,0,				0,0,0);
			JBody=			newBox(16,5,	 0,0,0, 4,3,1,				-2,0,4,				0,0,0);
			JRight_stick_1=	newBox(-1,0,	 0,0,-1, 7,1,1,				-1,0,4,				0,-2.96706F,-0.1745329F);
			JRight_jet=		newBox(22, 26,	 0,-1.5F,0, 2,4,2,			-13.2F, 1.5F, 3.5F,	0, -2.70526F, 0);
			JLeft_stick_2=	newBox(15, 2,	 0,0,-1, 7,1,1,				-7F, 1.2F, 5.3F,	0, 2.792527F, 0);
			JLeft_stick_1=	newBox(-1, 2,	 0,0,0, 7,1,1,				 1F, 0F, 4F,  		0, -0.1745329F, 0.1745329F);
			JRight_stick_2=	newBox(15, 0,	 0,0,0, 7,1,1,				 7.1F, 1.2F, 5.2F,	0, 0.4363323F, 0F);
			JLeft_jet=		newBox(22, 26,	 0,-1.5F,0, 2,4,2,			 13F, 1.5F, 3.1F,	0, 1.22173F, 0F);
			
			JRight_tank=	newBox(24, 10,	 0,0,0, 2,4,2,				-1.5F,1.3F,4.5F,	0, -0.7853982F, 0F);
			JLeft_tank=		newBox(24, 10,	 0,0,0, 2,4,2,				 1.5F,1.3F,4.5F,	0, -0.7853982F, 0F);
			
			MLeft_arm_1.childModels=new ArrayList<>();
			MRight_arm_1.childModels=new ArrayList<>();
			MLeft_arm_1.childModels.add(MLeft_arm_2);
			MRight_arm_1.childModels.add(MRight_arm_2);
			
			MLeft_leg_1.childModels=new ArrayList<>();
			MRight_leg_1.childModels=new ArrayList<>();
			MLeft_leg_1.childModels.add(MLeft_leg_2);
			MRight_leg_1.childModels.add(MRight_leg_2);
		}
		private ModelRenderer newBox(int texOffX, int texOffY, float offX, float offY, float offZ, int width, int height, int depth, float rotationPointX, float rotationPointY, float rotationPointZ, float xRot, float yRot, float zRot){
			ModelRenderer model=new ModelRenderer(this, texOffX, texOffY);
			model.addBox(offX, offY, offZ, width, height, depth);
			model.setRotationPoint(rotationPointX, rotationPointY, rotationPointZ);
			model.setTextureSize(64, 32);
			model.mirror=true;
			model.rotateAngleX=xRot;
			model.rotateAngleY=yRot;
			model.rotateAngleZ=zRot;
			return model;
		}
		
		@Override
		public void render(Entity e, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale){
			EntityPenguin penguin=(EntityPenguin)e;
			setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale, penguin);
			
			float 
				bodyRotPrecent=PartialTicksUtil.calculate(penguin.prevSlidingTransition, penguin.slidingTransition),
				bodyRot90=bodyRotPrecent*90;
			
			boolean hasRot=bodyRotPrecent!=0;
			
		    MLeft_leg_1.rotateAngleX=MathHelper.cos(limbSwing*(2.3F-bodyRotPrecent))*(2+bodyRotPrecent*0.5F)*limbSwingAmount;
			MRight_leg_1.rotateAngleX=MLeft_leg_1.rotateAngleX*(bodyRotPrecent*2-1);
			OpenGLM.translate(0, 13.9/16F, -1.5/16F);
			if(hasRot){
				OpenGLM.translate(0, 9/16F,0);
				OpenGLM.rotateX(bodyRot90);
				OpenGLM.translate(0, -9/16F+7/16F*bodyRotPrecent, 0);
			}
			
			
			OpenGLM.pushMatrix();
			if(hasRot)OpenGLM.rotateX(-bodyRot90);
			float trans=-1*bodyRotPrecent/16F;
			OpenGLM.translate(0,0,1/16F);
			OpenGLM.translate(0,trans,trans);
			OpenGLM.rotateY(headYaw);
			OpenGLM.rotateX(headPitch);
			OpenGLM.translate(0,-trans,trans);
			MHead.render(scale);
			MBeak.render(scale);
			if(penguin.hasHelemt())Helmet.render(scale);
			OpenGLM.popMatrix();
			
			OpenGLM.enableCull();
			MRight_leg_1.render(scale);
			MLeft_leg_1.render(scale);
			OpenGLM.disableCull();
			
			MLeft_arm_1.render(scale);
			MRight_arm_1.render(scale);
			
			MBody.render(scale);
			
			if(penguin.hasJetpack()){
				JBody.render(scale);
				JRight_stick_1.render(scale);
				JRight_jet.render(scale);
				JLeft_stick_2.render(scale);
				JLeft_stick_1.render(scale);
				JRight_stick_2.render(scale);
				JLeft_jet.render(scale);
				JLeft_tank.render(scale);
				JRight_tank.render(scale);
			}
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityPenguin entity){
		return peng;
	}
}
