package com.magiology.client.gui.custom;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OnOffGuiButton extends GuiButton implements Updateable{
	
	public static class GuiButtonClickEvent{
		private static GuiButtonClickEvent inst;
		public static GuiButtonClickEvent get(){
			if(inst==null)inst=new GuiButtonClickEvent();
			return inst;
		}
		private GuiButtonClickEvent(){}
		@SubscribeEvent
		public void onClickPre(ActionPerformedEvent.Pre e){
			if(e.button.visible&&e.button instanceof OnOffGuiButton)e.setCanceled(!((OnOffGuiButton)e.button).onClicked());
		}
	}
	protected boolean isOn;
	public PhysicsFloat pos=new PhysicsFloat(0, 0.1F);
	
	public ColorF setColor,color,prevColor,selectionColor,prevSelectionColor;
	public OnOffGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, ColorF color){
		super(buttonId, x, y, widthIn, heightIn, "");
		this.color=color.copy();
		prevColor=color.copy();
		setColor=color.copy();
		selectionColor=color.copy();
		prevSelectionColor=color.copy();
		pos.addWall("0", 0, false);
		pos.addWall("1", 1, true);
	}
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY){
		if(this.visible){
			VertexRenderer buff=TessUtil.getVB();
			buff.setInstantNormalCalculation(false);
			GL11U.texture(false);
			OpenGLM.enableBlend();
			OpenGLM.tryBlendFuncSeparate(770, 771, 1, 0);
			OpenGLM.blendFunc(770, 771);
			
			buff.addVertexWithUV(xPosition + 0, yPosition + height, zLevel, 0,0);
			buff.addVertexWithUV(xPosition + width, yPosition + height, zLevel, 0,0);
			buff.addVertexWithUV(xPosition + width, yPosition + 0, zLevel,0,0);
			buff.addVertexWithUV(xPosition + 0, yPosition + 0, zLevel,0,0);
			ColorF color=UtilM.calculateRenderColor(prevColor, this.color);
			OpenGLM.lineWidth(1);
			GL11U.glColor(color.mix(ColorF.BLACK,1,0.3F));
			buff.setClearing(false);
			buff.draw();
			buff.setClearing(true);
			GL11U.glColor(color);
			buff.setDrawAsWire(true);
			buff.draw();
			buff.setDrawAsWire(false);
			
			int width=this.width/2;
			float scale=UtilM.getGuiScaleRaw();
			buff.addVertexWithUV(xPosition + 1.1/scale, yPosition + height-1/scale, zLevel, 0,0);
			buff.addVertexWithUV(xPosition + width-1/scale, yPosition + height-1/scale, zLevel, 0,0);
			buff.addVertexWithUV(xPosition + width-1/scale, yPosition + 1.1/scale, zLevel,0,0);
			buff.addVertexWithUV(xPosition + 1.1/scale, yPosition + 1.1/scale, zLevel,0,0);
			buff.pushMatrix();
			buff.translate(width*pos.getPoint(), 0, 0);
			ColorF selectionColor=UtilM.calculateRenderColor(prevSelectionColor, this.selectionColor);
			OpenGLM.lineWidth(1);
			GL11U.glColor(selectionColor.mix(ColorF.WHITE,1,0.6F));
			buff.setClearing(false);
			buff.draw();
			buff.setClearing(true);
			GL11U.glColor(selectionColor.mix(ColorF.BLACK,1,0.3F));
			buff.setDrawAsWire(true);
			buff.draw();
			buff.setDrawAsWire(false);
			buff.popMatrix();
			GL11U.texture(true);
			OpenGLM.color(1.0F, 1.0F, 1.0F, 1.0F);
			buff.setInstantNormalCalculation(true);
		}
	}
	public void forceIsOn(boolean isOn){
		setOn(isOn);
		pos.speed=0;
		pos.point=pos.prevPoint=isOn?1:0;
	}
	public boolean isOn(){
		return pos.wantedPoint==1;
	}
	public boolean onClicked(){
		int mouseX=Mouse.getX()/UtilM.getGuiScaleRaw(),mouseY=(-Mouse.getY()+Display.getHeight())/UtilM.getGuiScaleRaw(),xPosition=(int) (this.xPosition+(width/2)*pos.getPoint());
		boolean selected=mouseX >= xPosition&& mouseY >= this.yPosition && mouseX < xPosition + this.width/2 && mouseY < this.yPosition + this.height;
		if(!selected)return false;
		pos.wantedPoint=pos.wantedPoint==0?1:0;
		return true;
	}
	public void setOn(boolean isOn){
		pos.wantedPoint=isOn?1:0;
	}
	@Override
	public void update(){
		pos.update();
		pos.bounciness=Math.abs(pos.speed*1.5F);
		prevSelectionColor=selectionColor.copy();
		prevColor=color.copy();
		int mouseX=Mouse.getX()/UtilM.getGuiScaleRaw(),mouseY=(-Mouse.getY()+Display.getHeight())/UtilM.getGuiScaleRaw(),xPosition=(int)(this.xPosition+(width/2)*pos.getPoint());
		this.hovered=mouseX>=this.xPosition&&mouseY>=this.yPosition&&mouseX<this.xPosition+this.width&&mouseY<this.yPosition+this.height;
		color=UtilM.slowlyEqalizeColor(color, setColor.mul(hovered?1:0.9), 0.05F);
		if(mouseX>=xPosition&&mouseY>=this.yPosition&&mouseX<xPosition+this.width/2&&mouseY<this.yPosition+this.height)selectionColor=UtilM.slowlyEqalizeColor(selectionColor, color.mix(ColorF.WHITE), 0.05F);
		else selectionColor=UtilM.slowlyEqalizeColor(selectionColor, color, 0.05F);
	}
}
