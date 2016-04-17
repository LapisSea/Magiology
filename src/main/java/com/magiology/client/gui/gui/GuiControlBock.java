package com.magiology.client.gui.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.container.ControlBockContainer;
import com.magiology.client.gui.guiutil.gui.DrawThatSexyDotHelper;
import com.magiology.client.gui.guiutil.gui.buttons.CustomButton;
import com.magiology.core.MReference;
import com.magiology.forgepowered.packets.packets.TileRedstone;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiControlBock extends GuiContainer implements Updateable{
	
	CustomButton CustomButton;
	DrawThatSexyDotHelper dot1,dot2,dot3;
	
	double dotS1=0,dotS2=0,dotS3=0;
	ResourceLocation EnergyBar=new ResourceLocation(MReference.MODID,"/textures/models/PowerCounter/EnergyBar.png");
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/GuiControlBock.png");
	
	private TileEntityControlBlock tileCB;
	
	public GuiControlBock(InventoryPlayer pInventory,TileEntityControlBlock tileCB){
		super(new ControlBockContainer(pInventory,tileCB));
		this.tileCB=tileCB;
		this.xSize=176;
		this.ySize=166;
		
	}
	@Override
	 protected void actionPerformed(GuiButton b){
		 
		 switch (b.id){
		 case 1:{
			 tileCB.redstoneC++;
			 if(tileCB.redstoneC>2)tileCB.redstoneC=0;
			 if(CustomButton!=null){
				 CustomButton.rGoal=RandUtil.RD();
				 CustomButton.gGoal=RandUtil.RD();
				 CustomButton.bGoal=RandUtil.RD();
			 }
			 if(dot1!=null){
				 dot1.glow+=20;
				 dot2.glow+=20;
				 dot3.glow+=20;
				 dot1.scale*=1.6;
				 dot2.scale*=1.6;
				 dot3.scale*=1.6;
			 }
		 }break;
		 case 2:{
			 
		 }break;
		 
		 }
		 UtilM.sendMessage(new TileRedstone(tileCB));
		 tileCB.getWorld().markBlockForUpdate(tileCB.getPos());
		 
	 }
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int v2, int v3){
		
		
		if(dot1==null){
			dot1=new DrawThatSexyDotHelper(guiLeft+9-6, guiTop+8-6,  214-6, 67-6, 16, 16, 1.5);
			dot2=new DrawThatSexyDotHelper(guiLeft+9-6, guiTop+22-6, 214-6, 67-6, 16, 16,1.5);
			dot3=new DrawThatSexyDotHelper(guiLeft+11-6,guiTop+35-6, 214-6, 67-6, 16, 16, 1.5);
		}
		if(CustomButton!=null){
			CustomButton.update(guiLeft+40, guiTop+40);
		}
		TessUtil.bindTexture(main);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		OpenGLM.alphaFunc(GL11.GL_GREATER, 0.9F);
		OpenGLM.translate(0, 0, 1);
		drawSmartShit(false);
		OpenGLM.translate(0, 0, -1);
		if(tileCB.redstoneC==2){
		this.drawTexturedModalRect(guiLeft+11, guiTop+10, 206, 51, 19, 10);
		this.drawTexturedModalRect(guiLeft+11, guiTop+24, 206, 51, 19, 10);
		}
		GL11U.setUpOpaqueRendering(2);
		drawSmartShit(true);
		GL11U.endOpaqueRendering();
		
		dot1.render(guiLeft+9-6, guiTop+8-6);
		dot2.render(guiLeft+9-6, guiTop+22-6);
		dot3.render(guiLeft+11-6,guiTop+35-6);

		TessUtil.bindTexture(EnergyBar);
		double scale=((float)(tileCB.tank)/(float)(tileCB.maxT))*100.0;
		if(scale>1)scale=1;
		
		OpenGLM.translate(guiLeft+142-6, guiTop+43-12.5, 0);
		OpenGLM.scale(0.1, 0.1, 1);
		this.drawRect(0,(float)(256-(256*scale)), 0, (float)(256-(256*scale)), 128, (float)(256*scale));
		OpenGLM.scale(10, 10, 1);
		OpenGLM.translate(-guiLeft-142+6, -guiTop-43+12.5, 0);
		
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int a,int b){
		
		String text1="null",text2="Redstone ctrl";
		switch (tileCB.redstoneC){
		case 0:text1="Turn OFF";break;
		case 1:text1="Ignore";break;
		case 2:text1="Turn ON";break;
		}
		int colorGray=UtilM.colorToCode(Color.GRAY);
		int first=-1,second=-1;
		if(tileCB.redstoneC==0)second=colorGray;
		if(tileCB.redstoneC==1)first=colorGray;
		if(tileCB.redstoneC==2){
			second=first=colorGray;
			OpenGLM.enableBlend();
			GL11U.allOpacityIs(true);
			GL11U.blendFunc(2);
		}
		this.drawString(fontRendererObj,"ON", 14, 11, first);
		this.drawString(fontRendererObj,"OFF",12, 25, second);
		if(tileCB.redstoneC==2){
			OpenGLM.color(1, 1, 1, 1);
			GL11U.allOpacityIs(true);
			OpenGLM.disableBlend();
		}
		
		this.drawString(fontRendererObj, text1, 5, 64, -1);
		this.drawString(fontRendererObj, text2, 5, 74, -1);
	}
	
	
	
	 protected void drawRect(float x, float y,float tx, float yt, float xp, float yp){
		 float f = 0.00390625F;
		 float f1 = 0.00390625F;
		 Renderer.POS_UV.beginQuads();
		 Renderer.POS_UV.addVertex(x + 0, y + yp, this.zLevel, (tx + 0) * f, (yt + yp) * f1);
		 Renderer.POS_UV.addVertex(x + xp, y + yp, this.zLevel, (tx+ xp) * f, (yt + yp) * f1);
		 Renderer.POS_UV.addVertex(x + xp, y + 0, this.zLevel, (tx + xp) * f, (yt + 0) * f1);
		 Renderer.POS_UV.addVertex(x + 0, y + 0, this.zLevel, (tx + 0) * f, (yt + 0) * f1);
		 Renderer.POS_UV.draw();
	}
	 
	 public void drawSmartShit(boolean type){
		double angle=PartialTicksUtil.calculatePos(tileCB.prevAngle,tileCB.angle);
		switch (tileCB.redstoneC){
		case 0:{
			this.drawTexturedModalRect(guiLeft+12, guiTop+35, 176, 21, 16, 16);
			this.drawTexturedModalRect(guiLeft-1, guiTop+12, 176, 106, 17, 36);
		}break;
		case 1:{
			this.drawTexturedModalRect(guiLeft+12, guiTop+35, 176, 72, 16, 16);
			this.drawTexturedModalRect(guiLeft+2, guiTop+27, 176, 143, 17, 19);
			
		}break;
		case 2:{
			this.drawTexturedModalRect(guiLeft+12, guiTop+35, 176, 38, 16, 16);
			this.drawTexturedModalRect(guiLeft+23, guiTop+39, 176, 89, 19, 16);
			
		}break;
		}
		this.drawTexturedModalRect(guiLeft+48, guiTop+60, 176, 38+17, 16, 16);
		OpenGLM.translate(guiLeft+31, guiTop+18, 0);
		OpenGLM.translate(20.5, 4.5, 0);
		OpenGLM.rotate(angle, 0, 0, 1);
		OpenGLM.translate(-20.5, -4.5, 0);
		int glowSize=6;
		this.drawTexturedModalRect(-glowSize+1, -glowSize, 202-glowSize, 30-glowSize, 25+glowSize*2, 9+glowSize*2);
		if(type)this.drawTexturedModalRect(-glowSize+1, -glowSize, 202-glowSize, 30-glowSize, 25+glowSize*2, 9+glowSize*2);
		OpenGLM.translate(20.5, 4.5, 0);
		OpenGLM.rotate(-angle, 0, 0, 1);
		OpenGLM.translate(-20.5, -4.5, 0);
		OpenGLM.translate(-(guiLeft+31), -(guiTop+18), 0);
		double thingyPos=PartialTicksUtil.calculatePos(tileCB.prevThingyPos, tileCB.thingyPos);
		{
			OpenGLM.translate(guiLeft+83, guiTop+79, 0);
			OpenGLM.translate(-thingyPos*5, 0, 0);
			this.drawTexturedModalRect(0, 0, 204, 0, 19, 10);
			
			OpenGLM.translate(2*thingyPos*5, 0, 0);
			OpenGLM.translate(10, 0, 0);
			OpenGLM.rotate(180, 0, 1, 0);
			OpenGLM.disableCull();
			this.drawTexturedModalRect(0, 0, 204, 0, 19, 10);
			OpenGLM.enableCull();
			OpenGLM.rotate(180, 0,-1, 0);
			OpenGLM.translate(-10, 0, 0);
			OpenGLM.translate(-(guiLeft+83), -(guiTop+79), 0);
		}
		OpenGLM.translate(-thingyPos*5, 0, 0);
		
	}
	 
	 @Override
	 public void initGui(){
		 super.initGui();
		 if(CustomButton==null)CustomButton=new CustomButton(1, guiLeft+40, guiTop+40, 20, 20, null,"textures/gui/widgetsAdd.png");
		 this.buttonList.add(CustomButton);
	 }
	 
	 @Override
	public void update(){
		if(dot1==null){
			dot1=new DrawThatSexyDotHelper(guiLeft+9-6, guiTop+8-6,  214-6, 67-6, 16, 16, 1.5);
			dot2=new DrawThatSexyDotHelper(guiLeft+9-6, guiTop+22-6, 214-6, 67-6, 16, 16,1.5);
			dot3=new DrawThatSexyDotHelper(guiLeft+11-6,guiTop+35-6, 214-6, 67-6, 16, 16, 1.5);
		}
		dotS1+=RandUtil.CRD(5);
		dotS2+=RandUtil.CRD(5);
		dotS3+=RandUtil.CRD(5);
		dotS1*=0.99;
		dotS2*=0.99;
		dotS3*=0.99;
		dot1.update(tileCB.angle*10+dotS2/5, 3,false);
		dot2.update(tileCB.angle*10+dotS1/5, 3,false);
		dot3.update(tileCB.angle*10+dotS3/5, 3,false);
		dot1.finishTheLoop();
		dot2.finishTheLoop();
		dot3.finishTheLoop();
	 }
}