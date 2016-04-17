package com.magiology.mcobjects.effect;

import com.magiology.client.gui.custom.guiparticels.GuiStandardFX;
import com.magiology.client.gui.custom.guiparticels.GuiStandardFX.GuiFXProp;
import com.magiology.client.gui.gui.GuiContainerAndGuiParticles;
import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.util.ResourceLocation;

public class GuiParticle{
	
	public boolean noClip=false,isDead=false,hasMovementNoise=true;
	public GuiStandardFX standardFX;
	public ResourceLocation texture;
	public double xPos,lastXPos,yPos,lastYPos,r=1,g=1,b=1,scale=1,opacity=1,xSpeed=0,ySpeed=0,xRotation=0,yRotation=0,zRotation=0,boundingBoxXY=0,bounciness=1;
	public int zLayer=-100,screenResX,screenResY,guiLeft,guiTop,age,maxAge,xSize,ySize;
	
	//-//---Constructor cluster start---//-//
	public GuiParticle(double x,double y,int maxAge,double xSpeed,double ySpeed,double scale,double opacity,double bounciness,double r,double g,double b,ResourceLocation texture,GuiStandardFX standardFX){
		this(x,y,maxAge,xSpeed,ySpeed,r,g,b,texture,standardFX);
		this.scale=scale;
		this.opacity=opacity;
		this.bounciness=bounciness;
	}
	public GuiParticle(double x,double y,int maxAge,double xSpeed,double ySpeed,double r,double g,double b,ResourceLocation texture,GuiStandardFX standardFX){
		this(x,y,maxAge, texture);
		this.xSpeed=xSpeed;
		this.ySpeed=ySpeed;
		this.r=r;
		this.g=g;
		this.b=b;
		this.standardFX=standardFX;
	}
	public GuiParticle(double x,double y,int maxAge,ResourceLocation texture){
		this.xPos=x;
		this.yPos=y;
		this.texture=texture;
		this.maxAge=maxAge;
	}
	//-//---Constructor cluster end---//-//
	
	public boolean[] getScreenColision(int scale){
		boolean[] result=new boolean[4];
		if(xPos<boundingBoxXY/2+scale)				result[0]=true;
		else if(xPos>screenResX-boundingBoxXY/2-scale)result[1]=true;
		if(yPos<scale+boundingBoxXY/2)				result[2]=true;
		else if(yPos>screenResY-boundingBoxXY/2-scale)result[3]=true;
		return result;
	}
	
	public void MoveParticle(double xSpeed,double ySpeed){
		xPos+=xSpeed;
		yPos+=ySpeed;
		if(!noClip){
			if(standardFX.IsEnabled(GuiFXProp.HasColision)){
				boolean[] a=getScreenColision(0);
				if(a[0]){
					if(standardFX.HasFX(GuiFXProp.HasColision))for(int i=0;i<6;i++)GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(0, yPos,10, RandUtil.RF()*3+1, RandUtil.CRD(10),1,0.5,0.7,r*0.8+RandUtil.RF()*0.2,g*0.8+RandUtil.RF()*0.2,b*0.8+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.InpactFX));
					this.xSpeed*=-bounciness;
					xPos=boundingBoxXY/2;
				}
				if(a[1]){
					if(standardFX.HasFX(GuiFXProp.HasColision))for(int i=0;i<6;i++)GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(screenResX-boundingBoxXY/2, yPos,10, -RandUtil.RF()*3-1, RandUtil.CRD(10),1,0.5,0.7,r*0.8+RandUtil.RF()*0.2,g*0.8+RandUtil.RF()*0.2,b*0.8+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.InpactFX));
					this.xSpeed*=-bounciness;
					xPos=screenResX-boundingBoxXY/2;
				}
				if(a[2]){
					if(standardFX.HasFX(GuiFXProp.HasColision))for(int i=0;i<6;i++)GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(xPos, 0,10, RandUtil.CRD(10), RandUtil.RF()*3+1,1,0.5,0.7,r*0.8+RandUtil.RF()*0.2,g*0.8+RandUtil.RF()*0.2,b*0.8+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.InpactFX));
					this.ySpeed*=-bounciness;
					yPos=boundingBoxXY/2;
				}
				if(a[3]){
					if(standardFX.HasFX(GuiFXProp.HasColision))for(int i=0;i<6;i++)GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(xPos, screenResY,10, RandUtil.CRD(10), -RandUtil.RF()*3-1,1,0.5,0.7,r*0.8+RandUtil.RF()*0.2,g*0.8+RandUtil.RF()*0.2,b*0.8+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.InpactFX));
					this.ySpeed*=-bounciness;
					yPos=screenResY-boundingBoxXY/2;
				}
			}
			if(standardFX.IsEnabled(GuiFXProp.HasColisionForce)){
				boolean[] a=getScreenColision(RandUtil.RI(25));
				
				if(a[0]){
					this.xSpeed+=0.2;
					if(standardFX.HasFX(GuiFXProp.HasColisionForce))GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(-xPos, yPos+RandUtil.RI(10)-5,13+RandUtil.RI(4), 3, 0,1,0.1,0.7,r*0.8+RandUtil.RF()*0.2,g*0.8+RandUtil.RF()*0.2,b*0.8+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.InpactFX));
				}
				if(a[1]){
					this.xSpeed-=0.2;
					if(standardFX.HasFX(GuiFXProp.HasColisionForce))GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(screenResX, yPos+RandUtil.RI(10)-5,13+RandUtil.RI(4), -3, 0,1,0.1,0.7,r*0.8+RandUtil.RF()*0.2,g*0.8+RandUtil.RF()*0.2,b*0.8+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.InpactFX));
				}
				if(a[2]){
					this.ySpeed+=0.2;
					if(standardFX.HasFX(GuiFXProp.HasColisionForce))GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(xPos+RandUtil.RI(10)-5, -yPos,13+RandUtil.RI(4), 0, 3,1,0.1,0.7,r*0.8+RandUtil.RF()*0.2,g*0.8+RandUtil.RF()*0.2,b*0.8+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.InpactFX));
				}
				if(a[3]){
					this.ySpeed-=0.2;
					if(standardFX.HasFX(GuiFXProp.HasColisionForce))GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(xPos+RandUtil.RI(10)-5, screenResY,13+RandUtil.RI(4), 0, -3,1,0.1,0.7,r*0.8+RandUtil.RF()*0.2,g*0.8+RandUtil.RF()*0.2,b*0.8+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.InpactFX));
				}
			}
		}
	}
	public void renderParticle(float partialTicks){
		OpenGLM.pushMatrix();
		U.getMC().renderEngine.bindTexture(Textures.SmoothBuble1);
		GL11U.setUpOpaqueRendering(2);
		double opacityF=opacity,
				x=PartialTicksUtil.calculatePos(lastXPos,xPos),
				y=PartialTicksUtil.calculatePos(lastYPos,yPos);
		
		
		
		OpenGLM.color(r, g, b, opacityF);
		Renderer.POS_UV.beginQuads();
		Renderer.POS_UV.addVertex(-boundingBoxXY/2+x, -boundingBoxXY/2+y, 0, 0, 0);
		Renderer.POS_UV.addVertex(-boundingBoxXY/2+x,  boundingBoxXY/2+y, 0, 0, 1);
		Renderer.POS_UV.addVertex( boundingBoxXY/2+x,  boundingBoxXY/2+y, 0, 1, 1);
		Renderer.POS_UV.addVertex( boundingBoxXY/2+x, -boundingBoxXY/2+y, 0, 1, 0);
		Renderer.POS_UV.draw();
		GL11U.endOpaqueRendering();
		OpenGLM.color(1, 1, 1, 1);
		OpenGLM.popMatrix();
	}
	
	private void setDead(){this.isDead=true;}
	
	public void UpdateParticle(){
		lastXPos=xPos;
		lastYPos=yPos;
		age++;
		if(standardFX.IsEnabled(GuiFXProp.HasAgeLimit))if(age>maxAge||opacity<=0){
			setDead();
		}
		boundingBoxXY=10.0*scale;
		
		double lesstiplyer=6;
		if(standardFX.IsEnabled(GuiFXProp.HatesToBeUnseen)){
			if(standardFX==GuiStandardFX.StarterFX){
				GuiContainerAndGuiParticles.spawnGuiParticle(new GuiParticle(xPos, yPos,7+RandUtil.RI(5),RandUtil.CRF(1),RandUtil.CRF(1),2.5,0.05,0.7,1,0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2,Textures.SmoothBuble1,GuiStandardFX.CloudFX));
			}
			if((xPos<guiLeft||yPos<guiTop)||(xPos>guiLeft+xSize||yPos>guiTop+ySize));else lesstiplyer/=4.0;
			
		}
		double[] ab=MathUtil.circleXZ(RandUtil.RI(360));
		if(hasMovementNoise){
			xSpeed+=ab[0]/lesstiplyer;
			ySpeed+=ab[1]/lesstiplyer;
			xSpeed*=0.99-(lesstiplyer==6?0.01:0);
			ySpeed*=0.99-(lesstiplyer==6?0.01:0);
			
		}
		
		if(standardFX==GuiStandardFX.InpactFX){
			xSpeed+=ab[0]/4;
			ySpeed+=ab[1]/4;
		}
		
		if(standardFX.IsEnabled(GuiFXProp.HasBlendOut))
		if(age>(float)maxAge/2){
			opacity/=1.3;
		}
		MoveParticle(xSpeed,ySpeed);
	}
	public void UpdateScreenRes(int width,int height, int guiLeft,int guiTop,int xSize,int ySize){
		screenResX=width;
		screenResY=height;
		this.guiLeft=guiLeft;
		this.guiTop=guiTop;
		this.xSize=xSize;
		this.ySize=ySize;
	}
}
