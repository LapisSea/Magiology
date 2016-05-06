package com.magiology.client.gui.guiutil.gui.buttons;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomButton extends GuiButton{

	public ResourceLocation buttonTexture = new ResourceLocation("textures/gui/widgets.png");
	public double[] one2={1,0},one2Goal={1,0};
	public double r=1,g=1,b=1,rGoal=0.9,gGoal=0.9,bGoal=0.9;
	public int state=0;
	VertexBuffer tess=TessUtil.getWB();
	
	
	public CustomButton(int id, int x, int y,int width, int height, String text,String resouce){
		super(id, x, y, width, height, text);
		rGoal=RandUtil.RD()/1.3;
		gGoal=RandUtil.RD()/1.3;
		bGoal=RandUtil.RD()/1.3;
		if(resouce!=null)buttonTexture=new ResourceLocation(resouce);
	}
	
	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_){
		if (this.visible){
			FontRenderer fontrenderer = TessUtil.getFontRenderer();
			p_146112_1_.getTextureManager().bindTexture(buttonTexture);
			OpenGLM.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
			this.getHoverState(this.hovered);
			OpenGLM.enableBlend();
			GL11U.blendFunc(1);
			OpenGLM.translate(0, 0, 1);
			
				this.drawTexturedModalRect(xPosition,yPosition,0,46,width/2,height);
				this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46,width/2,height);
				
				OpenGLM.color(one2[0], one2[0], one2[0], 0.3);
				OpenGLM.translate(0, 0, 1);
					this.drawTexturedModalRect(xPosition,yPosition,0,46+20,width/2,height);
					this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46+20,width/2,height);
					
					OpenGLM.color(one2[1], one2[1], one2[1], 0.3);
					this.drawTexturedModalRect(xPosition,yPosition,0,46+40,width/2,height);
					this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46+40,width/2,height);
			OpenGLM.translate(0, 0, -2);
			
			OpenGLM.depthMask(false);
			
			OpenGLM.color(0, 0, 0, 0.2);
			OpenGLM.translate(0, 0, -1);
			this.drawTexturedModalRect(xPosition,yPosition,0,46+60,width/2,height);
			this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46+60,width/2,height);
			OpenGLM.translate(0, 0, 1);
			
			GL11U.allOpacityIs(true);
			GL11U.blendFunc(2);
			OpenGLM.color(r, g, b, 0.3);
			
			this.drawTexturedModalRect(xPosition,yPosition,0,46+60,width/2,height);
			this.drawTexturedModalRect(xPosition+width/2,yPosition,200-width/2,46+60,width/2,height);
			
			OpenGLM.depthMask(true);
			OpenGLM.disableBlend();
			OpenGLM.color(1, 1, 1, 1);
			
			this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
			int l = 14737632;
			if (packedFGColour != 0)l = packedFGColour;
			else if (!this.enabled)l = 10526880;
			else if (this.hovered)l = 16777120;
			this.drawCenteredString(fontrenderer,this.displayString,this.xPosition+this.width/2,this.yPosition+(this.height-8)/2,l);
		}
	}
	
	public void setC(int R,int G,int B){
		rGoal=R;gGoal=G;bGoal=B;
		r=rGoal;g=gGoal;b=bGoal;
	}
	
	public void update(int x, int y){
		state=this.getHoverState(this.hovered);
		if(state==1){
			one2Goal[0]=1;
			one2Goal[1]=0;
		}else if(state==2){
			one2Goal[0]=0;
			one2Goal[1]=1;
		}
		xPosition=x;
		yPosition=y;
		if(r>1)r=1;
		else if(r<0)r=0;
		if(g>1)g=1;
		else if(g<0)g=0;
		if(b>1)b=1;
		else if(b<0)b=0;
		double Cspeed=0.04;
		r=UtilM.graduallyEqualize(r, rGoal, Cspeed+RandUtil.CRD(Cspeed/2));
		g=UtilM.graduallyEqualize(g, gGoal, Cspeed+RandUtil.CRD(Cspeed/2));
		b=UtilM.graduallyEqualize(b, bGoal, Cspeed+RandUtil.CRD(Cspeed/2));
		for(int a=0;a<one2.length;a++)if(Math.abs(one2[a]-one2Goal[a])>0.1)one2[a]=UtilM.graduallyEqualize(one2[a], one2Goal[a], Cspeed*3+RandUtil.CRD(Cspeed));
	}
}
