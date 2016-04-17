package com.magiology.client.render.models;

import org.lwjgl.opengl.GL11;

import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ModelHelmet42 extends ModelBiped{
	ModelRenderer baseTop1,baseTop2,baseTop3,baseTop4,baseSide1,baseSide2,baseSide3,baseSide4,baseBack1,baseBack2,baseBack3,baseBack4;
	public boolean shouldFollowThePlayer=false,shouldFollowThePlayerMaster=false,shouldFollowThePlayerHasAMaster=false;
	public ModelHelmet42(){
		textureWidth = 38;
		textureHeight = 12;
		
		baseTop1 = new ModelRenderer(this, 0, 0);
		baseTop1.addBox(-1.5F, -8.5F, -4.5F, 3, 1, 4);
		baseTop1.setRotationPoint(0F, 0F, 0F);
		baseTop1.setTextureSize(64, 32);
		baseTop1.mirror = true;
		setRotation(baseTop1, 0F, 0F, 0F);
		baseTop2 = new ModelRenderer(this, 16, 6);
		baseTop2.addBox(-4.5F, -8.5F, -4.5F, 3, 1, 1);
		baseTop2.setRotationPoint(0F, 0F, 0F);
		baseTop2.setTextureSize(64, 32);
		baseTop2.mirror = true;
		setRotation(baseTop2, 0F, 0F, 0F);
		baseTop3 = new ModelRenderer(this, 16, 6);
		baseTop3.addBox(1.5F, -8.5F, -4.5F, 3, 1, 1);
		baseTop3.setRotationPoint(0F, 0F, 0F);
		baseTop3.setTextureSize(64, 32);
		baseTop3.mirror = true;
		setRotation(baseTop3, 0F, 0F, 0F);
		baseTop4 = new ModelRenderer(this, 14, 0);
		baseTop4.addBox(-0.5F, -8.4F, -1.5F, 1, 1, 4);
		baseTop4.setRotationPoint(0F, 0F, 0F);
		baseTop4.setTextureSize(64, 32);
		baseTop4.mirror = true;
		setRotation(baseTop4, -0.122173F, 0F, 0F);
		baseSide1 = new ModelRenderer(this, 14, 0);
		baseSide1.addBox(-4.5F, -3.5F, -8.366667F, 1, 1, 11);
		baseSide1.setRotationPoint(0F, 0F, 0F);
		baseSide1.setTextureSize(64, 32);
		baseSide1.mirror = true;
		setRotation(baseSide1, -0.7853982F, 0F, 0F);
		baseSide2 = new ModelRenderer(this, 14, 0);
		baseSide2.addBox(3.5F, -3.5F, -8.366667F, 1, 1, 11);
		baseSide2.setRotationPoint(0F, 0F, 0F);
		baseSide2.setTextureSize(64, 32);
		baseSide2.mirror = true;
		setRotation(baseSide2, -0.7853982F, 0F, 0F);
		baseSide3 = new ModelRenderer(this, 1, 6);
		baseSide3.addBox(-4.4F, -3.4F, 3.4F, 1, 1, 5);
		baseSide3.setRotationPoint(0F, 0F, 0F);
		baseSide3.setTextureSize(64, 32);
		baseSide3.mirror = true;
		setRotation(baseSide3, 0.7853982F, 0F, 0F);
		baseSide4 = new ModelRenderer(this, 1, 6);
		baseSide4.addBox(3.4F, -3.4F, 3.4F, 1, 1, 5);
		baseSide4.setRotationPoint(0F, 0F, 0F);
		baseSide4.setTextureSize(64, 32);
		baseSide4.mirror = true;
		setRotation(baseSide4, 0.7853982F, 0F, 0F);
		baseBack1 = new ModelRenderer(this, 9, 7);
		baseBack1.addBox(2.6F, -1F, 3.5F, 2, 1, 1);
		baseBack1.setRotationPoint(0F, 0F, 0F);
		baseBack1.setTextureSize(64, 32);
		baseBack1.mirror = true;
		setRotation(baseBack1, 0F, 0F, 0F);
		baseBack2 = new ModelRenderer(this, 9, 7);
		baseBack2.addBox(-4.6F, -1F, 3.5F, 2, 1, 1);
		baseBack2.setRotationPoint(0F, 0F, 0F);
		baseBack2.setTextureSize(64, 32);
		baseBack2.mirror = true;
		setRotation(baseBack2, 0F, 0F, 0F);
		baseBack3 = new ModelRenderer(this, 28, 0);
		baseBack3.addBox(2.4F, -6.7F, 3.5F, 1, 8, 1);
		baseBack3.setRotationPoint(0F, 0F, 0F);
		baseBack3.setTextureSize(64, 32);
		baseBack3.mirror = false;
		setRotation(baseBack3, 0.0698132F, 0F, -0.4014257F);
		baseBack4 = new ModelRenderer(this, 28, 0);
		baseBack4.addBox(-3.4F, -6.7F, 3.5F, 1, 8, 1);
		baseBack4.setRotationPoint(0F, 0F, 0F);
		baseBack4.setTextureSize(64, 32);
		baseBack4.mirror = false;
		setRotation(baseBack4, 0.0698132F, 0F, 0.4014257F);
  }
  @Override
public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
	  if(entity!=null){
		  if(entity.isInvisible())return;
		  isSneak=entity.isSneaking();
	  }
	  U.getMC().renderEngine.bindTexture(Textures.Helmet42Model);
	  setRotationAngles(f, f1, f2, f3, f4, f5,entity);
	  float color=1;
	  if(entity!=null)if(entity instanceof EntityPlayer){
		  EntityPlayer player=(EntityPlayer)entity;
		  ItemStack arInv=player.inventory.armorInventory[3];
		  if(arInv!=null&&arInv.stackSize>0){
			  float max=arInv.getMaxDamage(),now=arInv.getItemDamage();
			  if(arInv.getMaxDamage()>0)color=(max-now)/max;
			  if(color<0.3)color=0.3F;
		  }
	  }
	  
//	  / (180F / (float)Math.PI)
	  if(shouldFollowThePlayerHasAMaster)shouldFollowThePlayer=shouldFollowThePlayerMaster;
	  
	  double yt=0,
			  rotationX=entity!=null?bipedHead.rotateAngleX:shouldFollowThePlayer?-(U.getMC().thePlayer.rotationPitch/ (180F / (float)Math.PI)):0,
			  rotationY=entity!=null?bipedHead.rotateAngleY:shouldFollowThePlayer?(U.getMC().thePlayer.rotationYawHead/(180F / (float)Math.PI)):0,
			  rotationZ=bipedHead.rotateAngleZ;
	  
	  rotationX*=(180F / (float)Math.PI);
	  rotationY*=(180F / (float)Math.PI);
	  rotationZ*=(180F / (float)Math.PI);
	  if(entity==null&&shouldFollowThePlayer){
		  rotationY+=180;
	  }
	  
	  if(isSneak)yt = 1.0/16.0;
	  OpenGLM.translate(0, yt, 0);
	  OpenGLM.rotate(rotationZ, 0.0F, 0.0F, 1.0F);
	  OpenGLM.rotate(rotationY, 0.0F, 1.0F, 0.0F);
	  OpenGLM.rotate(rotationX, 1.0F, 0.0F, 0.0F);
	  {
		  float p=1F/16F;
		  OpenGLM.color(color, color, color, 1);
		  baseTop1.render(f5);
		  baseTop2.render(f5);
		  baseTop3.render(f5);
		  baseTop4.render(f5);
		  baseSide1.render(f5);
		  baseSide2.render(f5);
		  baseSide3.render(f5);
		  baseSide4.render(f5);
		  baseBack1.render(f5);
		  baseBack2.render(f5);
		  baseBack3.render(f5);
		  baseBack4.render(f5);
		  OpenGLM.color(1, 1, 1, 1);
		  OpenGLM.enableBlend();
		  OpenGLM.disableTexture2D();
		  OpenGLM.disableLighting();
		  OpenGLM.depthMask(false);
		  GL11U.blendFunc(2);
		  Renderer.POS.beginQuads();
		  OpenGLM.color((float)(0.8+UtilM.CRandF(0.4)), (float)(0.2+UtilM.CRandF(0.2)), (float)(0.1+UtilM.CRandF(0.2)), (float)(0.7+UtilM.RF()*0.3));
		  for(int a=0;a<2;a++){
			  Renderer.POS.addVertex(-p*3+UtilM.CRandF(0.01), -p*4+UtilM.CRandF(0.01), -p*4.005);
			  Renderer.POS.addVertex(-p*3+UtilM.CRandF(0.01), -p*3+UtilM.CRandF(0.01), -p*4.005);
			  Renderer.POS.addVertex(-p  +UtilM.CRandF(0.01), -p*3+UtilM.CRandF(0.01), -p*4.005);
			  Renderer.POS.addVertex(-p  +UtilM.CRandF(0.01), -p*4+UtilM.CRandF(0.01), -p*4.005);
			  
			  Renderer.POS.addVertex( p  +UtilM.CRandF(0.01), -p*4+UtilM.CRandF(0.01), -p*4.005);
			  Renderer.POS.addVertex( p  +UtilM.CRandF(0.01), -p*3+UtilM.CRandF(0.01), -p*4.005);
			  Renderer.POS.addVertex( p*3+UtilM.CRandF(0.01), -p*3+UtilM.CRandF(0.01), -p*4.005);
			  Renderer.POS.addVertex( p*3+UtilM.CRandF(0.01), -p*4+UtilM.CRandF(0.01), -p*4.005);
		  }
		  Renderer.POS.draw();
		  Renderer.POS.begin(GL11.GL_TRIANGLES);
		  OpenGLM.color((float)(0.8+UtilM.CRandF(0.4)), (float)(0.2+UtilM.CRandF(0.2)), (float)(0.1+UtilM.CRandF(0.2)), 1);
		  for(int l=0;l<2+(isSneak?4:0);l++){
			  double[] criclexyz=UtilM.createBallXYZ(0.5+UtilM.RF(), false);
			  Renderer.POS.addVertex(criclexyz[0]+UtilM.CRandF(0.3), criclexyz[1]+UtilM.CRandF(0.3), criclexyz[2]+UtilM.CRandF(0.3));
			  Renderer.POS.addVertex(criclexyz[0]+UtilM.CRandF(0.3), criclexyz[1]+UtilM.CRandF(0.3), criclexyz[2]+UtilM.CRandF(0.3));
			  Renderer.POS.addVertex(criclexyz[0]+UtilM.CRandF(0.3), criclexyz[1]+UtilM.CRandF(0.3), criclexyz[2]+UtilM.CRandF(0.3));
		  }
		  
		  Renderer.POS.draw();
		  OpenGLM.enableTexture2D();
		  OpenGLM.enableLighting();
		  OpenGLM.disableBlend();
		  OpenGLM.depthMask(true);
	  }
	  OpenGLM.rotate(-rotationX, 1.0F, 0.0F, 0.0F);
	  OpenGLM.rotate(-rotationY, 0.0F, 1.0F, 0.0F);
	  OpenGLM.rotate(-rotationZ, 0.0F, 0.0F, 1.0F);
	  OpenGLM.translate(0, -yt, 0);
  }	
  
  private void setRotation(ModelRenderer model, float x, float y, float z){
	  model.rotateAngleX = x;
	  model.rotateAngleY = y;
	  model.rotateAngleZ = z;
  }
  
  @Override
public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5,Entity entity){
	  super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
