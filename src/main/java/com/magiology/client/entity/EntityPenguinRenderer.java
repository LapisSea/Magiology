package com.magiology.client.entity;

import com.magiology.mc_objects.entitys.EntityPenguin;
import com.magiology.util.m_extensions.ResourceLocationM;
import com.magiology.util.objs.ModelRendererDummy;
import com.magiology.util.objs.animation.AbstractAnimation;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

public class EntityPenguinRenderer extends RenderLiving<EntityPenguin>{
	
	private static ResourceLocation peng=new ResourceLocationM("textures/entity/Penguin.png");
	private static EntityPenguin penguin;
	
	private static AbstractAnimation swimmming=new AbstractAnimation("penguin/swimming", "RightArmY-Pt1", "RightArmY-Pt2");
	
	public EntityPenguinRenderer(RenderManager renderManager){
		super(renderManager, new MainModel(), 5/16F);
		shadowOpaque=0.5F;
	}
	
	private static class MainModel extends ModelBase{
		
		private ModelRenderer Helmet, MHead, MBeak, MBody, JBody, MLeft_leg_1, MLeft_leg_2, MLeft_arm_2, MLeft_arm_1, JLeft_stick_1, JLeft_stick_2, JLeft_jet, JLeft_tank, MRight_leg_1, MRight_leg_2, MRight_arm_2, MRight_arm_1, JRight_stick_1, JRight_stick_2, JRight_jet, JRight_tank;
		
		private ModelRendererDummy rightHandStack, leftHandStack;
		
		public MainModel(){
			textureWidth=64;
			textureHeight=32;
			
			Helmet=newBox(26, 0, -2.5F, -4.5F, -2.5F, 5, 5, 5, 0, 0, 0, 0, 0, 0);
			MHead=newBox(0, 10, -2, -4, -2, 4, 4, 4, 0, 0, 0, 0, 0, 0);
			MBeak=newBox(0, 21, 0, 0, 0, 1, 1, 1, -0.5F, -2, -3, 0, 0, 0);
			MRight_leg_2=newBox(10, 10, -0, 1, -0F, 2, 0, 2, -1F, 0F, -1.5F, 0, 0, 0);
			MLeft_leg_2=newBox(10, 12, -0, 1, -0F, 2, 0, 2, -1F, 0F, -1.5F, 0, 0, 0);
			MLeft_leg_1=newBox(0, 12, -0.5F, 0, -0.5F, 1, 1, 1, -1.5F, 9F, 2F, 0, 0, 0);
			MRight_leg_1=newBox(0, 10, -0.5F, 0, -0.5F, 1, 1, 1, 1.5F, 9F, 2F, 0, 0, 0);
			
			MLeft_arm_1=newBox(16, 9, -1, 0, -1.5F, 1, 4, 3, -3, 0, 1, 0, 0, 0);
			MLeft_arm_2=newBox(0, 4, -0.5F, 0, -1.5F, 1, 3, 3, -0.5F, 4, 0, 0, 0, 0);
			
			MRight_arm_1=newBox(17, 16, 0, 0, -1.5F, 1, 4, 3, 3, 0, 1, 0, 0, 0);
			MRight_arm_2=newBox(8, 4, -0.5F, 0, -2.5F, 1, 3, 3, 0.5F, 4, 1, 0, 0, 0);
			
			MBody=newBox(0, 18, -3, 0, -1, 6, 9, 5, 0, 0, 0, 0, 0, 0);
			JBody=newBox(16, 5, 0, 0, 0, 4, 3, 1, -2, 0, 4, 0, 0, 0);
			JRight_stick_1=newBox(-1, 0, 0, 0, -1, 7, 1, 1, -1, 0, 4, 0, -2.96706F, -0.1745329F);
			JRight_jet=newBox(22, 26, 0, -1.5F, 0, 2, 4, 2, -13.2F, 1.5F, 3.5F, 0, -2.70526F, 0);
			JLeft_stick_2=newBox(15, 2, 0, 0, -1, 7, 1, 1, -7F, 1.2F, 5.3F, 0, 2.792527F, 0);
			JLeft_stick_1=newBox(-1, 2, 0, 0, 0, 7, 1, 1, 1F, 0F, 4F, 0, -0.1745329F, 0.1745329F);
			JRight_stick_2=newBox(15, 0, 0, 0, 0, 7, 1, 1, 7.1F, 1.2F, 5.2F, 0, 0.4363323F, 0F);
			JLeft_jet=newBox(22, 26, 0, -1.5F, 0, 2, 4, 2, 13F, 1.5F, 3.1F, 0, 1.22173F, 0F);
			
			JRight_tank=newBox(24, 10, 0, 0, 0, 2, 4, 2, -1.5F, 1.3F, 4.5F, 0, -0.7853982F, 0F);
			JLeft_tank=newBox(24, 10, 0, 0, 0, 2, 4, 2, 1.5F, 1.3F, 4.5F, 0, -0.7853982F, 0F);
			
			MLeft_arm_1.childModels=new ArrayList<>();
			MRight_arm_1.childModels=new ArrayList<>();
			MLeft_arm_2.childModels=new ArrayList<>();
			MRight_arm_2.childModels=new ArrayList<>();
			MLeft_leg_1.childModels=new ArrayList<>();
			MRight_leg_1.childModels=new ArrayList<>();
			
			MLeft_arm_1.childModels.add(MLeft_arm_2);
			MRight_arm_1.childModels.add(MRight_arm_2);
			
			MLeft_leg_1.childModels.add(MLeft_leg_2);
			MRight_leg_1.childModels.add(MRight_leg_2);
			
			MRight_arm_2.childModels.add(rightHandStack=new ModelRendererDummy(this){
				
				@Override
				public void renderHook(){
					OpenGLM.scale(5/16F);
					renderHeldItem(penguin, penguin.getHeldItemMainhand(), TransformType.FIRST_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
					UtilC.getMC().renderEngine.bindTexture(peng);
				}
			});
			MLeft_arm_2.childModels.add(leftHandStack=new ModelRendererDummy(this){
				
				@Override
				public void renderHook(){
					renderHeldItem(penguin, penguin.getHeldItemOffhand(), TransformType.FIRST_PERSON_LEFT_HAND, EnumHandSide.LEFT);
					UtilC.getMC().renderEngine.bindTexture(peng);
				}
			});
			
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
		public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn){
		}
		
		@Override
		public void render(Entity e, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale){
			// head x,y, right arm 1 x,y
			
			OpenGLM.scale(-1, 1, 1);
			penguin=(EntityPenguin)e;
			ItemStack mainItem=penguin.getHeldItemMainhand(), secondItem=penguin.getHeldItemMainhand();
			rightHandStack.isHidden=mainItem==null;
			leftHandStack.isHidden=secondItem==null;
			float bodyRotPrecent=PartialTicksUtil.calculate(penguin.animator.prevSlidingTransition, penguin.animator.slidingTransition), swimming=PartialTicksUtil.calculate(penguin.animator.prevSwimmingMul, penguin.animator.swimmingMul), bodyRot90=bodyRotPrecent*90, swimRot=bodyRotPrecent*PartialTicksUtil.calculate(
				penguin.animator.prevSwimRot, penguin.animator.swimRot), rightArm1X=0, rightArm1Z=0, rightArm2Z=0, leftArm1X=0, leftArm1Z=0, leftArm2Z=0, rightLegX=0, leftLegX=0;
			
			boolean hasRot=bodyRotPrecent!=0;
			
			if(swimming==1) rightLegX=leftLegX=0.5F;
			else{
				leftLegX=MathHelper.cos(limbSwing*(2.3F-bodyRotPrecent))*(2+bodyRotPrecent*0.5F)*limbSwingAmount;
				if(swimming>0) leftLegX=leftLegX*(1-swimming)+0.5F*swimming;
				rightLegX=leftLegX*(bodyRotPrecent*2-1);
			}
			
			if(swimming!=0){
				float pos=((limbSwing+limbSwingAmount)/6)%1, mul=(float)(swimming*Math.PI), arm1=swimmming.get(0, pos)*mul, arm2=swimmming.get(1, pos)*mul;
				
				rightArm1Z+=arm1;
				rightArm2Z+=arm2;
				leftArm1Z+=arm1;
				leftArm2Z+=arm2;
			}
			if(mainItem!=null){
				rightArm1Z+=0.1F;
				rightArm2Z+=(1-swimming)*-0.55F;
				rightArm1X+=-0.7F;
			}
			
			float cos1=MathHelper.cos(ageInTicks*0.09F)*0.03F+0.03F, cos2=MathHelper.cos(ageInTicks*0.015F)*4F, sin1=MathHelper.sin(ageInTicks*0.067F)*0.03F, sin2=MathHelper.sin(ageInTicks*0.04F)*0.03F, sin3=MathHelper.sin(ageInTicks*0.01F)*4F;
			int speed=30;
			float tim=UtilC.getWorldTime()%speed;
			float pos=PartialTicksUtil.calculate(tim, tim+1)/speed;
			
			rightArm1X+=cos1;
			rightArm1Z+=sin1;
			rightArm2Z+=sin2;
			leftArm1X+=cos1;
			leftArm1Z+=sin1;
			leftArm2Z+=sin2;
			
			MRight_arm_2.offsetY=-(Math.abs(MathHelper.sin(rightArm2Z))*0.5F)/16;
			MLeft_arm_2.offsetY=-(Math.abs(MathHelper.sin(leftArm2Z))*0.5F)/16;
			
			MLeft_arm_1.offsetY=Math.abs(MathHelper.sin(leftArm1Z))/16F;
			MLeft_arm_1.offsetX=Math.min(0, MathHelper.cos(leftArm1Z))/16F;
			
			MRight_arm_1.offsetZ=-MathHelper.sin(rightArm1X)/16F;
			MRight_arm_1.offsetY=Math.abs(MathHelper.sin(rightArm1Z))/16F;
			MRight_arm_1.offsetX=-Math.min(0, MathHelper.cos(rightArm1Z))/16F;
			
			MRight_leg_1.rotateAngleX=rightLegX;
			MLeft_leg_1.rotateAngleX=leftLegX;
			
			MRight_arm_1.rotateAngleX=rightArm1X;
			MLeft_arm_1.rotateAngleX=leftArm1X;
			
			MLeft_arm_1.rotateAngleZ=leftArm1Z;
			MLeft_arm_2.rotateAngleZ=leftArm2Z;
			MRight_arm_1.rotateAngleZ=-rightArm1Z;
			MRight_arm_2.rotateAngleZ=-rightArm2Z;
			
			OpenGLM.translate(0, 13.9/16F, -1.5/16F);
			if(hasRot){
				OpenGLM.translate(0, 9/16F, 0);
				OpenGLM.rotateX(bodyRot90);
				OpenGLM.translate(0, 1/16F, 1.5/16F);
				OpenGLM.rotateX(swimRot);
				OpenGLM.translate(0, -1/16F, -1.5/16F);
				OpenGLM.translate(0, -9/16F+7/16F*bodyRotPrecent, 0);
			}
			
			OpenGLM.pushMatrix();
			if(hasRot) OpenGLM.rotateX(-bodyRot90);
			float trans=-1*bodyRotPrecent/16F;
			OpenGLM.translate(0, 0, 1/16F);
			OpenGLM.translate(0, trans, trans);
			if(swimRot!=0) OpenGLM.rotateX(swimRot/-2);
			OpenGLM.rotateYX(-headYaw+cos2, headPitch+sin3);
			OpenGLM.translate(0, -trans, trans);
			MHead.render(scale);
			MBeak.render(scale);
			if(penguin.hasHelemt()) Helmet.render(scale);
			OpenGLM.popMatrix();
			
			OpenGLM.enableCull();
			MRight_leg_1.render(scale);
			MLeft_leg_1.render(scale);
			OpenGLM.disableCull();
			
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
			MLeft_arm_1.render(scale);
			MRight_arm_1.render(scale);
		}
	}
	
	private static void renderHeldItem(EntityLivingBase entity, ItemStack stack, TransformType cam, EnumHandSide handSide){
		if(stack!=null){
			boolean flag=handSide==EnumHandSide.LEFT;
			OpenGLM.translate(6/16F, 6/16F, -11.5F/16F);
			Item item=stack.getItem();
			if(item instanceof ItemBlock){
				//				if(UtilC.getMC().getBlockRendererDispatcher().isEntityBlockAnimated(Block.getBlockFromItem(item))){
				//					OpenGLM.translate(0.0F, 0.0625F, -0.25F);
				//					OpenGLM.rotate(30.0F, 1.0F, 0.0F, 0.0F);
				//					OpenGLM.rotate(-5.0F, 0.0F, 1.0F, 0.0F);
				//					float f4=0.375F;
				//					OpenGLM.scale(0.375F, -0.375F, 0.375F);
				//				}else{
				//					OpenGLM.rotateY(-50);
				//					OpenGLM.rotateZ(95);
				//					OpenGLM.rotateX(-15);
				//					OpenGLM.translate(5/16F,-0/16F,5/16F);
				//					OpenGLM.scale(1.5F);
				//
				//				}
			}else if(item.isFull3D()||item.getItemUseAction(stack)==EnumAction.BOW){
				OpenGLM.rotateZ(-10);
				OpenGLM.rotateX(130);
				OpenGLM.rotateZ(35);
				if(item.shouldRotateAroundWhenRendering()){
					OpenGLM.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					OpenGLM.translate(0.0F, -0.0625F, 0.0F);
				}
				
				OpenGLM.translate(0.0625F-7F/16F, -0.125F+8F/16F, -4F/16F);
				OpenGLM.scale(1.5F, -1.5F, 1.5F);
			}else{
				OpenGLM.rotateXZY(-10, 115, -25);
				
				OpenGLM.translate(0.1875F, 0.1875F, 0.0F);
				OpenGLM.scale(0.875F, 0.875F, 0.875F);
				OpenGLM.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
				OpenGLM.rotate(-60.0F, 1.0F, 0.0F, 0.0F);
				OpenGLM.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
				OpenGLM.scale(1.5F);
			}
			
			OpenGLM.rotate(-15.0F, 1.0F, 0.0F, 0.0F);
			OpenGLM.rotate(40.0F, 0.0F, 0.0F, 1.0F);
			
			UtilC.getIR().renderItemSide(entity, stack, cam, flag);
		}
	}
	
	@Override
	public void doRender(EntityPenguin entity, double x, double y, double z, float entityYaw, float partialTicks){
		BlockPos pos=entity.getAiTarget();
		//		if(pos.getY()!=-1){
		//			Vec3M pos1=PartialTicksUtil.calculate(entity);
		//			OpenGLM.disableTexture2D();
		//			OpenGLM.pushMatrix();
		//			OpenGLM.disableDepth();
		//			OpenGLM.translate(PartialTicksUtil.calculate(UtilC.getViewEntity()).mul(-1).add(pos1));
		//			ColorF.RED.bind();
		//			OpenGLM.lineWidth(5);
		//
		//			Path path=entity.getNavigator().getPathToPos(pos);
		//
		//			Renderer.LINES.begin();
		////			Renderer.LINES.addVertex(0,0,0);
		////			Renderer.LINES.addVertex(pos.getX()-pos1.x+0.5, pos.getY()-pos1.y+0.5, pos.getZ()-pos1.z+0.5);
		//
		//			if(path!=null&&path.getCurrentPathLength()>1)for(int i=0;i<path.getCurrentPathLength()-1;i++){
		//				PathPoint ia=path.getPathPointFromIndex(i);
		//				PathPoint ie=path.getPathPointFromIndex(i+1);
		//				Renderer.LINES.addVertex(ia.xCoord-pos1.x+0.5, ia.yCoord-pos1.y+0.5, ia.zCoord-pos1.z+0.5);
		//				Renderer.LINES.addVertex(ie.xCoord-pos1.x+0.5, ie.yCoord-pos1.y+0.5, ie.zCoord-pos1.z+0.5);
		//			}
		//			Renderer.LINES.draw();
		//
		//			OpenGLM.popMatrix();
		//			ColorF.WHITE.bind();
		//			OpenGLM.enableTexture2D();
		//			OpenGLM.enableDepth();
		//		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityPenguin entity){
		return peng;
	}
}
