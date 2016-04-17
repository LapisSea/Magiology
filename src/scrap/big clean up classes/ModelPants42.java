
package com.magiology.client.render.models;

import com.magiology.client.render.Textures;
import com.magiology.core.init.MItems;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModelPants42 extends ModelBiped{
	ModelRenderer legFront1,legFront2,legFront3,legFront4,Base,legSideAdd11,legSideAdd12,legSideAdd21,legSideAdd22,Stick1,Stick2,Stick3,Stick4,FrontStickB1,FrontStickAdd1,FrontStickB2,FrontStickAdd2;
	double leftLegRx,rightLegRx;
  
  public ModelPants42(){
	textureWidth = 32;
	textureHeight = 18;
	
	  legFront1 = new ModelRenderer(this, 8, 11);
	  legFront1.addBox(-2F, 1F, -1.7F, 4, 2, 1);
	  legFront1.setRotationPoint(2F, 12F, 0F);
	  legFront1.setTextureSize(64, 32);
	  legFront1.mirror = true;
	  setRotation(legFront1, -0.6108652F, 0F, 0F);
	  legFront2 = new ModelRenderer(this, 8, 11);
	  legFront2.addBox(-2F, 2.4F, -0.7F, 4, 2, 1);
	  legFront2.setRotationPoint(2F, 12F, 0F);
	  legFront2.setTextureSize(64, 32);
	  legFront2.mirror = true;
	  setRotation(legFront2, -0.6108652F, 0F, 0F);
	  legFront3 = new ModelRenderer(this, 8, 11);
	  legFront3.addBox(-2F, 4F, 0.3F, 4, 2, 1);
	  legFront3.setRotationPoint(2F, 12F, 0F);
	  legFront3.setTextureSize(64, 32);
	  legFront3.mirror = true;
	  setRotation(legFront3, -0.6108652F, 0F, 0F);
	  legFront4 = new ModelRenderer(this, 8, 11);
	  legFront4.addBox(-2F, 5.3F, 1.3F, 4, 2, 1);
	  legFront4.setRotationPoint(2F, 12F, 0F);
	  legFront4.setTextureSize(64, 32);
	  legFront4.mirror = true;
	  setRotation(legFront4, -0.6108652F, 0F, 0F);
	  Base = new ModelRenderer(this, 0, 0);
	  Base.addBox(-4.5F, 9.3F, -2.5F, 9, 3, 5);
	  Base.setRotationPoint(0F, 0F, 0F);
	  Base.setTextureSize(64, 32);
	  Base.mirror = true;
	  setRotation(Base, 0F, 0F, 0F);
	  
	  legSideAdd11 = new ModelRenderer(this, 28, 9);
	  legSideAdd11.addBox(1.8F, 0F, 0F, 1, 8, 1);
	  legSideAdd11.setRotationPoint(2F, 12F, 0F);
	  legSideAdd11.setTextureSize(64, 32);
	  legSideAdd11.mirror = true;
	  setRotation(legSideAdd11, 0F, 0F, 0.0174533F);
	  legSideAdd12 = new ModelRenderer(this, 28, 9);
	  legSideAdd12.addBox(1.8F, 0F, -1F, 1, 8, 1);
	  legSideAdd12.setRotationPoint(2F, 12F, 0F);
	  legSideAdd12.setTextureSize(64, 32);
	  legSideAdd12.mirror = true;
	  setRotation(legSideAdd12, 0F, 0F, 0.0174533F);
	  
	  legSideAdd21 = new ModelRenderer(this, 28, 0);
	  legSideAdd21.addBox(-6.8F, 0F, 0F, 1, 8, 1);
	  legSideAdd21.setRotationPoint(2F, 12F, 0F);
	  legSideAdd21.setTextureSize(64, 32);
	  legSideAdd21.mirror = true;
	  setRotation(legSideAdd21, 0F, 0F, 0.0174533F);
	  legSideAdd22 = new ModelRenderer(this, 28, 0);
	  legSideAdd22.addBox(-6.8F, 0F, -1F, 1, 8, 1);
	  legSideAdd22.setRotationPoint(2F, 12F, 0F);
	  legSideAdd22.setTextureSize(64, 32);
	  legSideAdd22.mirror = true;
	  setRotation(legSideAdd22, 0F, 0F, 0.0174533F);
	  
	  Stick1 = new ModelRenderer(this, 24, 11);
	  Stick1.addBox(-0.5F, 2.9F, 7.5F, 1, 5, 1);
	  Stick1.setRotationPoint(0F, 0F, 0F);
	  Stick1.setTextureSize(64, 32);
	  Stick1.mirror = true;
	  setRotation(Stick1, -0.6108652F, 0F, 0F);
	  Stick2 = new ModelRenderer(this, 20, 8);
	  Stick2.addBox(-0.5F, 2.5F, 6.1F, 1, 3, 1);
	  Stick2.setRotationPoint(0F, 0F, 0F);
	  Stick2.setTextureSize(64, 32);
	  Stick2.mirror = true;
	  setRotation(Stick2, -0.2792527F, 0F, 0F);
	  Stick3 = new ModelRenderer(this, 24, 8);
	  Stick3.addBox(-0.5F, 4.2F, 3.3F, 1, 2, 1);
	  Stick3.setRotationPoint(0F, 0F, 0F);
	  Stick3.setTextureSize(64, 32);
	  Stick3.mirror = true;
	  setRotation(Stick3, 0.3490659F, 0F, 0F);
	  Stick4 = new ModelRenderer(this, 0, 8);
	  Stick4.addBox(-0.5F, -2.2F, 0.6F, 1, 8, 1);
	  Stick4.setRotationPoint(0F, 0F, 0F);
	  Stick4.setTextureSize(64, 32);
	  Stick4.mirror = true;
	  setRotation(Stick4, 0.8726646F, 0F, 0F);
	  FrontStickB1 = new ModelRenderer(this, 4, 8);
	  FrontStickB1.addBox(1.5F, 5.4F, -1.2F, 1, 5, 1);
	  FrontStickB1.setRotationPoint(0F, 0F, 0F);
	  FrontStickB1.setTextureSize(64, 32);
	  FrontStickB1.mirror = true;
	  setRotation(FrontStickB1, -0.122173F, -0.122173F, 0.4363323F);
	  FrontStickAdd1 = new ModelRenderer(this, 8, 8);
	  FrontStickAdd1.addBox(0.6F, 10.3F, 0.4F, 2, 1, 2);
	  FrontStickAdd1.setRotationPoint(0F, 0F, 0F);
	  FrontStickAdd1.setTextureSize(64, 32);
	  FrontStickAdd1.mirror = true;
	  setRotation(FrontStickAdd1, -0.122173F, -0.7853982F, 0.4363323F);
	  FrontStickB2 = new ModelRenderer(this, 4, 8);
	  FrontStickB2.addBox(-2.5F, 5.4F, -1.1F, 1, 5, 1);
	  FrontStickB2.setRotationPoint(0F, 0F, 0F);
	  FrontStickB2.setTextureSize(64, 32);
	  FrontStickB2.mirror = true;
	  setRotation(FrontStickB2, -0.1396263F, 0.1396263F, -0.4363323F);
	  FrontStickAdd2 = new ModelRenderer(this, 8, 8);
	  FrontStickAdd2.addBox(-2.6F, 10.4F, 0.5F, 2, 1, 2);
	  FrontStickAdd2.setRotationPoint(0F, 0F, 0F);
	  FrontStickAdd2.setTextureSize(64, 32);
	  FrontStickAdd2.mirror = true;
	  setRotation(FrontStickAdd2, -0.1396263F, 0.7853982F, -0.4363323F);
  }
  
  @Override
public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
	  if(entity!=null)if(entity.isInvisible())return;
	  float color=0.9F;
	  if(entity!=null&&entity instanceof EntityPlayer){
	  		EntityPlayer player=(EntityPlayer)entity;
	  		ItemStack arInv=player.inventory.armorInventory[1];
	  		if(arInv!=null&&arInv.stackSize>0){
	  			float max=arInv.getMaxDamage(),now=arInv.getItemDamage();
	  			if(arInv.getMaxDamage()>0)color=(max-now)/max;
	  		}
	  	}
	  U.getMC().renderEngine.bindTexture(Textures.Pants42Model);
	  setRotationAngles(f, f1, f2, f3, f4, f5,entity);
	  
	  OpenGLM.color(color, color, color, 1);
	  legSideAdd11.render(f5);
	  legSideAdd12.render(f5);
	  legSideAdd21.render(f5);
	  legSideAdd22.render(f5);
	  
	  OpenGLM.translate(0, 0, 0.005);
	  legFront1.render(f5);
	  legFront2.render(f5);
	  legFront3.render(f5);
	  legFront4.render(f5);
	  if(entity!=null){
		  legFront1.rotateAngleX-=(float)leftLegRx;
		  legFront2.rotateAngleX-=(float)leftLegRx;
		  legFront3.rotateAngleX-=(float)leftLegRx;
		  legFront4.rotateAngleX-=(float)leftLegRx;
		  legFront1.rotateAngleX+=(float)rightLegRx;
		  legFront2.rotateAngleX+=(float)rightLegRx;
		  legFront3.rotateAngleX+=(float)rightLegRx;
		  legFront4.rotateAngleX+=(float)rightLegRx;
	  }
	  
	  OpenGLM.translate(-(1.0/16.0)*4, 0, 0);
	  legFront1.render(f5);
	  legFront2.render(f5);
	  legFront3.render(f5);
	  legFront4.render(f5);
	  OpenGLM.translate((1.0/16.0)*4, 0, 0);
	  OpenGLM.translate(0, 0,  -0.005);
	  
	  
	  
 	  Base.render(f5);
 	  Stick1.render(f5);
 	  Stick2.render(f5);
 	  Stick3.render(f5);
 	  Stick4.render(f5);
	  OpenGLM.color(1, 1, 1, 1);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z){
	model.rotateAngleX = x;
	model.rotateAngleY = y;
	model.rotateAngleZ = z;
  }
  
  	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5,Entity e){
  		if(e==null)return;
  		ItemStack item=null;
  		if(e!=null)if(e instanceof EntityPlayer){
  			EntityPlayer pl=(EntityPlayer)e;
  			ItemStack[] invArmour=pl.inventory.armorInventory;
  			if(invArmour!=null){
  				for(int a=0;a<invArmour.length;a++)if(invArmour[a]!=null&&invArmour[a].getItem()==MItems.pants_42I)item=invArmour[a];
  			}
  			
  		}
  		if(item!=null){
  			NBTTagCompound stackTC=item.getTagCompound();
  			float[] r=new float[6];
  			
  			for(int b=0;b<r.length;b++)r[b]=stackTC.getFloat("r"+b);

//			UtilM.println(r[0]+"\n");
  			legFront1.rotateAngleX+=r[0];
  			legFront2.rotateAngleX+=r[1];
  			legFront3.rotateAngleX+=r[2];
  			legFront4.rotateAngleX+=r[3];
  			legSideAdd11.rotateAngleZ+=r[4];
  			legSideAdd12.rotateAngleZ+=r[5];
  			legSideAdd21.rotateAngleZ-=r[4];
  			legSideAdd22.rotateAngleZ-=r[5];
  			
  		}
  		if(e!=null)isSneak=e.isSneaking();
  		super.setRotationAngles(f, f1, f2, f3, f4, f5,e);
  		leftLegRx=bipedLeftLeg.rotateAngleX;
  		rightLegRx=bipedRightLeg.rotateAngleX;
  		legFront1.rotateAngleX+=(float)leftLegRx;
  		legFront2.rotateAngleX+=(float)leftLegRx;
  		legFront3.rotateAngleX+=(float)leftLegRx;
  		legFront4.rotateAngleX+=(float)leftLegRx;
  		legSideAdd11.rotateAngleX+=(float)leftLegRx;
  		legSideAdd12.rotateAngleX+=(float)leftLegRx;
  		legSideAdd21.rotateAngleX+=(float)rightLegRx;
  		legSideAdd22.rotateAngleX+=(float)rightLegRx;
  		
  		syncBoxes(Base,bipedBody);
  		syncBoxes(Stick1,bipedBody);
  		syncBoxes(Stick2,bipedBody);
  		syncBoxes(Stick3,bipedBody);
  		syncBoxes(Stick4,bipedBody);
  		if(isSneak){
  			Base.rotationPointY+=-1/1.5;
  			Base.rotationPointZ+=-0.8/1.5;
  		}
  		ModelRenderer leg=bipedLeftLeg;leg.rotateAngleX=0;leg.rotateAngleY=0;leg.rotateAngleZ=0;leg.rotationPointY=this.isSneak?-(float)(1.0/16.0)*40:0;leg.rotationPointX=0;
  		syncBoxes(legFront1,leg);
  		syncBoxes(legFront2,leg);
  		syncBoxes(legFront3,leg);
  		syncBoxes(legFront4,leg);
  		syncBoxes(legSideAdd11,leg);
  		syncBoxes(legSideAdd12,leg);
  		syncBoxes(legSideAdd21,leg);
  		syncBoxes(legSideAdd22,leg);
  		
  	}
  	
  	public void addStuff(ModelRenderer MR,float f, float f1, float f2, float f3, float f4, float f5){
  		MR.rotateAngleX+=f;
  		MR.rotateAngleY+=f1;
  		MR.rotateAngleZ+=f2;
  		MR.rotationPointX+=f3;
  		MR.rotationPointY+=f4;
  		MR.rotationPointZ+=f5;
  	}
  	public void syncBoxes(ModelRenderer MR1,ModelRenderer MR2){
  		MR1.rotateAngleX+=MR2.rotateAngleX;
  		MR1.rotateAngleY+=MR2.rotateAngleY;
  		MR1.rotateAngleZ+=MR2.rotateAngleZ;
  		MR1.rotationPointX+=MR2.rotationPointX;
  		MR1.rotationPointY+=MR2.rotationPointY;
  		MR1.rotationPointZ+=MR2.rotationPointZ;
  	}
  	

}