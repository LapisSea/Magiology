package com.magiology.client.gui.guiutil.gui.buttons;

import org.lwjgl.input.Mouse;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class CleanButton extends GuiButton implements Updateable{
	
	public ColorF color,prevColor;
	public boolean[] enabledOutline={true,true,true,true};
	public PhysicsFloat highlight=new PhysicsFloat(0, 0.1F,true);
	
	public CleanButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ColorF color){
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.color=prevColor=color;
	}
	
	public CleanButton(int buttonId, int x, int y, String buttonText, ColorF color){
		super(buttonId, x, y, buttonText);
		this.color=prevColor=color;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY){
		if(!this.visible)return;
		this.mouseDragged(mc, mouseX, mouseY);
		this.hovered=mouseX>=this.xPosition&&mouseY>=this.yPosition&&mouseX<this.xPosition+this.width&&mouseY<this.yPosition+this.height;
		this.getHoverState(this.hovered);
		GL11U.setUpOpaqueRendering(1);
		ColorF color=PartialTicksUtil.calculate(prevColor, this.color).mul(highlight.getPoint()+1);
		if(!enabled)color=color.mix(color.blackNWhite(),1,2);
		GL11U.glColor(color);
		GL11U.texture(false);
		this.drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
		GL11U.glColor(color.mix(new ColorF(0,0,0,color.a), 1,1.2F));
		Renderer.LINES.begin();
		
		if(enabledOutline[0]){
			Renderer.LINES.addVertex(xPosition,	   yPosition, 0);
			Renderer.LINES.addVertex(xPosition+width, yPosition, 0);
		}
		if(enabledOutline[1]){
			Renderer.LINES.addVertex(xPosition, yPosition,		0);
			Renderer.LINES.addVertex(xPosition, yPosition+height, 0);
		}
		if(enabledOutline[2]){
			Renderer.LINES.addVertex(xPosition+width, yPosition,		0);
			Renderer.LINES.addVertex(xPosition+width, yPosition+height, 0);
		}
		if(enabledOutline[3]){
			Renderer.LINES.addVertex(xPosition,	   yPosition+height, 0);
			Renderer.LINES.addVertex(xPosition+width, yPosition+height, 0);
		}
		
		Renderer.LINES.draw();
		
		GL11U.texture(true);
		
		this.drawCenteredString(Font.FR(), this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color.mix(ColorF.WHITE,1F,1.5F).toCode());
		GL11U.endOpaqueRendering();
	}
	
	public CleanButton setOutline(boolean[] enabled){
		enabledOutline=enabled;
		return this;
	}
	
	public CleanButton setOutline(int side, boolean enabled){
		enabledOutline[side]=enabled;
		return this;
	}
	@Override
	public void update(){
		if(enabled){
			if(hovered){
				if(Mouse.isButtonDown(0))highlight.wantedPoint=0.5F;
				else highlight.wantedPoint=0.2F;
			}else highlight.wantedPoint=0F;
		}else highlight.wantedPoint=0F;
		highlight.update();
	}
}
