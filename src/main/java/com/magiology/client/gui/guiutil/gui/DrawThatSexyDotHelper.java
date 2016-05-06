package com.magiology.client.gui.guiutil.gui;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.client.gui.Gui;

public class DrawThatSexyDotHelper extends Gui{
	
	boolean isUpdated=false;
	public double scale,prevScale,scalingSpeed,trueScale,rotation,prevRotation,rotatingSpeed,transparency=1;
	public int x,y,xTexture,yTexture,xSize,ySize,glow;
	
	public DrawThatSexyDotHelper(int x,int y, int xTexture, int yTexture, int xSize, int ySize, double scale){
		this.x=x;
		this.y=y;
		this.xTexture=xTexture;
		this.yTexture=yTexture;
		this.xSize=xSize;
		this.ySize=ySize;
		this.trueScale=scale;
		rotation=RandUtil.CRD(5);
	}
	
	public void finishTheLoop(){
		transparency-=0.05;
	}
	
	public void render(int x3,int y3){
		isUpdated=false;
		x=x3;y=y3;
		if(!isUpdated){
			double scale=PartialTicksUtil.calculate(prevScale, this.scale);
			double rotation=PartialTicksUtil.calculate(prevRotation, this.rotation);
			
			double scal=scale-1.2;
			double xR=x-scal*7.5,yR=y-scal*7.5;
			double xof=9+scal*7.5,yof=9+scal*7.5;
			OpenGLM.translate(xR, yR, 40);
			//......................................
			OpenGLM.translate(xof, yof, 0);
			OpenGLM.rotate(rotation, 0, 0, 1);
			OpenGLM.translate(-xof, -yof, 0);
			OpenGLM.scale(scale, scale, scale);
			GL11U.allOpacityIs(false);
			OpenGLM.translate(0, 0, 1);
			
			drawTexturedModalRect(0,0,xTexture,yTexture,xSize,ySize);
			OpenGLM.depthMask(false);
			OpenGLM.translate(0, 0, -1);
			//---------------------------------------------
			GL11U.allOpacityIs(true);
			OpenGLM.enableBlend();
			GL11U.blendFunc(2);
			
			for(int a=0;a<this.glow/11;a++)drawTexturedModalRect(0,0,xTexture,yTexture,xSize,ySize);
			
			OpenGLM.disableBlend();
			GL11U.allOpacityIs(true);
			OpenGLM.scale(1/scale, 1/scale, 1/scale);
			OpenGLM.translate(xof, yof, 0);
			OpenGLM.rotate(-rotation, 0, 0, 1);
			OpenGLM.translate(-xof, -yof, 0);
			//......................................
			OpenGLM.translate(-xR, -yR, -40);
			OpenGLM.depthMask(true);
			
		}
	}
	
	public void update(double rot,int glow,boolean var1){
		prevRotation=rotation;
		prevScale=scale;
		if(!isUpdated){
			if(scale<0){
				scale=0;
				scalingSpeed*=-0.3;
			}
			isUpdated=true;
			if(var1)transparency+=0.05;
			rotatingSpeed*=0.9;
			rotatingSpeed=rot;
			if(this.glow>glow*10)this.glow--;
			else if(this.glow<glow*10)this.glow++;
			if(this.scale>trueScale)this.scalingSpeed-=0.005;
			else if(this.scale<trueScale)this.scalingSpeed+=0.005;
			this.scale*=0.98;
			this.scale+=scalingSpeed;
			rotatingSpeed*=0.2;
			this.rotation+=this.rotatingSpeed;
		}
	}
	
	
	public void updateAndRender(double rotation,int x3,int y3,int glow,boolean var1){
		render(x3,y3);
		this.update(rotation,glow,var1);
	}
	
}
