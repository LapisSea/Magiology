package com.magiology.client.gui.guiutil.gui.buttons;

import java.awt.Color;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ColoredGuiButton extends GuiButton{
	
	public float 
	r=1,g=1,b=1,alpha=1,
	prevR=1,prevG=1,prevB=1,prevAlpha=1,
	wantedR=1,wantedG=1,wantedB=1,wantedAlpha=1;
	
	public ColoredGuiButton(int id, int x, int y,int width, int height, String txt){
		super(id, x, y, width, height, txt);
	}
	
	public void blink(float f){
		r+=f;
		g+=f;
		b+=f;
		r=MathUtil.snap(r, 0, 1);
		g=MathUtil.snap(g, 0, 1);
		b=MathUtil.snap(b, 0, 1);
	}
	@Override
	public void drawButton(Minecraft v1, int v2, int v3){
		if (this.visible)
		{
			float r=PartialTicksUtil.calculate(prevR, this.r),g=PartialTicksUtil.calculate(prevG, this.g),b=PartialTicksUtil.calculate(prevB, this.b),alpha=PartialTicksUtil.calculate(prevAlpha, this.alpha);
			FontRenderer fontrenderer = v1.fontRendererObj;
			v1.getTextureManager().bindTexture(buttonTextures);
			OpenGLM.color(r,g,b,alpha);
			this.hovered = v2 >= this.xPosition && v3 >= this.yPosition && v2 < this.xPosition + this.width && v3 < this.yPosition + this.height;
			int k = this.getHoverState(this.hovered);
			OpenGLM.enableBlend();
			GL11U.blendFunc(1);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
			this.mouseDragged(v1, v2, v3);
			int l = 14737632;

			if (packedFGColour != 0)
			{
				l = packedFGColour;
			}
			else if (!this.enabled)
			{
				l = 10526880;
			}
			else if (this.hovered)
			{
				l = 16777120;
			}
			
			float[] rgb=UtilM.codeToRGBABPercentage(l);
			
			rgb[0]=(rgb[0]+r)/2F;
			rgb[1]=(rgb[1]+g)/2F;
			rgb[2]=(rgb[2]+b)/2F;
			
			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, new Color(r,g,b,alpha).hashCode());
			OpenGLM.color(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
	public void update(){
		r=MathUtil.snap(r, 0, 1);
		g=MathUtil.snap(g, 0, 1);
		b=MathUtil.snap(b, 0, 1);
		prevR=r;
		prevG=g;
		prevB=b;
		prevAlpha=alpha;
		
		r=(float)UtilM.graduallyEqualize(r, wantedR, 0.1);
		g=(float)UtilM.graduallyEqualize(g, wantedG, 0.1);
		b=(float)UtilM.graduallyEqualize(b, wantedB, 0.1);
		alpha=(float)UtilM.graduallyEqualize(prevAlpha, wantedAlpha, 0.2);
	}

}