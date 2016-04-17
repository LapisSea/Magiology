package com.magiology.client.gui.gui;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.magiology.client.gui.custom.guiparticels.GuiStandardFX;
import com.magiology.client.gui.guiutil.gui.buttons.InvisivleGuiButton;
import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.forgepowered.events.TickEvents;
import com.magiology.forgepowered.events.client.RenderEvents;
import com.magiology.forgepowered.packets.packets.RightClickBlockPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket;
import com.magiology.mcobjects.effect.GuiParticle;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSC extends GuiContainerAndGuiParticles{
	
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/SCGui.png");
	InvisivleGuiButton addButton;
	int mouseX,mouseY,prevMouseX,prevMouseY,mouseleftTime;
	public int listOffset;
	public final TileEntitySmartCrafter tileCB;
	EntityPlayer player;
	GuiTextField txt1,txt2;
	int side=0;
	public GuiSC(EntityPlayer player,TileEntitySmartCrafter tileCB,int side){
		super(new SmartCrafterContainer(player,tileCB,side));
		this.player=player;
		this.side=side;
		this.tileCB=tileCB;
		this.xSize=176;
		this.ySize=213;
	}
	@Override
	public void drawGuiContainerForegroundLayer(int x,int y){
		mouseX=x;
		mouseY=y;
		if(isShiftKeyDown()){
			ResourceLocation txt=new ResourceLocation(MReference.MODID+":/textures/particle/SmoothBuble1.png");
			int xSpeed=mouseX-prevMouseX,ySpeed=mouseY-prevMouseY;
			double num=Math.sqrt(xSpeed*xSpeed+ySpeed*ySpeed)/10;
			GuiParticle part=null;
			if(prevMouseX!=mouseX&&prevMouseY!=mouseY)for(int gg=0;gg<num;gg++){
				double a=gg/num;
				int posX=(int)(prevMouseX+(mouseX-prevMouseX)*a),posY=(int)(prevMouseY+(mouseY-prevMouseY)*a);
				part=new GuiParticle(posX,  posY, 6, 0, 0, 1, 0.15, 0.15, txt, GuiStandardFX.InpactFX);
				GuiContainerAndGuiParticles.spawnGuiParticle(part);
				part.scale=2;
				part.opacity=0.03;
			}else if(U.getMC().theWorld.getTotalWorldTime()%2==0){
				part=new GuiParticle(mouseX,  mouseY, 6, 0, 0, 1, 0.15, 0.15, txt, GuiStandardFX.InpactFX);
				GuiContainerAndGuiParticles.spawnGuiParticle(part);
				part.scale=2;
				part.opacity=0.03;
			}
			if(part!=null)part.noClip=false;
		}
		prevMouseX=mouseX;
		prevMouseY=mouseY;
		FontRenderer fr=TessUtil.getFontRenderer();
		OpenGLM.translate(-guiLeft, -guiTop, 0);
		TessUtil.bindTexture(main);
		this.drawTexturedModalRect(addButton.xPosition, addButton.yPosition, 93, 167, 9, 9);
		{
			OpenGLM.pushMatrix();
			GuiButton button=(GuiButton)buttonList.get(5);
			int l=14737632;
			if(button.mousePressed(mc,x,y))l=16777120;
			OpenGLM.translate(button.xPosition+button.width/2, button.yPosition+(button.height-8)/2, 0);
			GL11U.glRotate(0, 0, 90);
			OpenGLM.translate(3, -4, 1);
			drawCenteredStringShoadowless(fr, "<-", 0,0, l);
			OpenGLM.translate(1, -1, -1);
			drawCenteredStringShoadowless(fr, "<-", 0,0, Color.DARK_GRAY.hashCode());
			OpenGLM.popMatrix();
		}{
			OpenGLM.pushMatrix();
			GuiButton button=(GuiButton)buttonList.get(6);
			int l=14737632;
			if(button.mousePressed(mc,x,y))l=16777120;
			OpenGLM.translate(button.xPosition+button.width/2, button.yPosition+(button.height-8)/2, 0);
			GL11U.glRotate(0, 0, -90);
			OpenGLM.translate(-3, -4, 1);
			drawCenteredStringShoadowless(fr, "<-", 0,0, l);
			OpenGLM.translate(-1, 1, -1);
			drawCenteredStringShoadowless(fr, "<-", 0,0, Color.DARK_GRAY.hashCode());
			OpenGLM.popMatrix();
		}
		this.renderParticles(RenderEvents.partialTicks);
		OpenGLM.translate(guiLeft, guiTop, 0);
		
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int x, int y){
		TessUtil.getWR();
		TessUtil.bindTexture(main);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
//		this.drawTexturedModalRect(guiLeft, guiTop-66, 0, -32, xSize, 100);
		OpenGLM.pushMatrix();
		OpenGLM.translate(guiLeft, guiTop, 0);
		OpenGLM.translate(40, 10, 0);
		tileCB.wantedProducts[listOffset].render();
		if(!txt1.isFocused())fontRendererObj.drawStringWithShadow(tileCB.wantedProducts[listOffset].ammountWanted+"", -15, 8, Color.WHITE.hashCode());
		OpenGLM.color(1, 1, 1, 1);
		OpenGLM.translate(0, 57, 0);
		tileCB.wantedProducts[listOffset+1].render();
		if(!txt2.isFocused())fontRendererObj.drawStringWithShadow(tileCB.wantedProducts[listOffset+1].ammountWanted+"", -15, 8, Color.WHITE.hashCode());
		OpenGLM.color(1, 1, 1, 1);
		OpenGLM.popMatrix();
		txt1.drawTextBox();
		txt2.drawTextBox();
	}
	
	@Override
	public void initGui(){
		super.initGui();
		buttonList.add(new GuiButton(0, guiLeft+4, guiTop+4, 20, 20, "<-"));
		buttonList.add(new GuiButton(1, guiLeft+152, guiTop+4, 20, 20, "->"));
		addButton=new InvisivleGuiButton(2, guiLeft+5, guiTop+70+46, 9, 9, "");
		buttonList.add(addButton);
		buttonList.add(new InvisivleGuiButton(3, guiLeft+40, guiTop+18+40, 7, 7, "X"));
		buttonList.add(new InvisivleGuiButton(4, guiLeft+40, guiTop+75+40, 7, 7, "x"));
		buttonList.add(new GuiButton(5, guiLeft+152, guiTop+56, 20, 20, ""));
		buttonList.add(new GuiButton(6, guiLeft+152, guiTop+81, 20, 20, ""));
		buttonList.add(new GuiButton(7, guiLeft+152, guiTop+56-10, 20, 10, ""));
		buttonList.add(new GuiButton(8, guiLeft+152, guiTop+81+20, 20, 10, ""));
		
		Keyboard.enableRepeatEvents(true);
		txt1 = new GuiTextField(0,fontRendererObj,  guiLeft+25,  guiTop+18, 56, 10);
		txt1.setFocused(false);
		txt1.setMaxStringLength(8);
		txt1.setTextColor(Color.WHITE.hashCode());
		txt1.setEnableBackgroundDrawing(false);
		txt2 = new GuiTextField(0,fontRendererObj,  guiLeft+25,  guiTop+75, 56, 10);
		txt2.setFocused(false);
		txt2.setMaxStringLength(8);
		txt2.setTextColor(Color.WHITE.hashCode());
		txt2.setEnableBackgroundDrawing(false);
	}
	
	@Override
	protected void keyTyped(char c, int i) throws IOException{
		if(Integer.valueOf(c)==13){
			mouseClicked(-10000, -10000, 0);
		}
		if(txt1.textboxKeyTyped(c, i));
		else if(txt2.textboxKeyTyped(c, i));
		else super.keyTyped(c, i);
		{	
			String text=txt1.getText();
			if(!text.isEmpty()){
				if(!UtilM.isInteger(text)){
					String clearedText="";
					char[] chars=text.toCharArray();
					boolean[] valid=new boolean[chars.length];
					for(int a=0;a<chars.length;a++){valid[a]=UtilM.isInteger(chars[a]+"");}
					for(int a=0;a<chars.length;a++)if(valid[a])clearedText+=chars[a]+"";
					txt1.setText(clearedText);
				}
			}
		}{
			String text=txt2.getText();
			if(!text.isEmpty()){
				if(!UtilM.isInteger(text)){
					String clearedText="";
					char[] chars=text.toCharArray();
					boolean[] valid=new boolean[chars.length];
					for(int a=0;a<chars.length;a++){valid[a]=UtilM.isInteger(chars[a]+"");}
					for(int a=0;a<chars.length;a++)if(valid[a])clearedText+=chars[a]+"";
					txt2.setText(clearedText);
				}
			}
		}
		
	}
	@Override
	protected void mouseClicked(int x, int y, int id) throws IOException{
		boolean prevTxt1State=txt1.isFocused(),prevTxt2State=txt2.isFocused();
		super.mouseClicked(x, y, id);
		txt1.mouseClicked(x, y, id);
		txt2.mouseClicked(x, y, id);
		boolean txt1State=txt1.isFocused(),txt2State=txt2.isFocused();
		if(prevTxt1State&&!txt1State){
			if(!txt1.getText().isEmpty()){
				tileCB.wantedProducts[listOffset].ammountWanted=Integer.parseInt(txt1.getText());
				txt1.setText("");
			}else tileCB.wantedProducts[listOffset].ammountWanted=0;
			UtilM.sendMessage(new GenericServerIntPacket(2, tileCB.wantedProducts[listOffset].ammountWanted));
		}
		if(prevTxt2State&&!txt2State){
			if(!txt2.getText().isEmpty()){
				tileCB.wantedProducts[listOffset+1].ammountWanted=Integer.parseInt(txt2.getText());
				txt2.setText("");
			}else tileCB.wantedProducts[listOffset+1].ammountWanted=0;
			UtilM.sendMessage(new GenericServerIntPacket(3, tileCB.wantedProducts[listOffset+1].ammountWanted));
		}
		if(!prevTxt1State&&txt1State)if(tileCB.wantedProducts[listOffset  ].ammountWanted!=0)txt1.setText(tileCB.wantedProducts[listOffset  ].ammountWanted+"");
		if(!prevTxt2State&&txt2State)if(tileCB.wantedProducts[listOffset+1].ammountWanted!=0)txt2.setText(tileCB.wantedProducts[listOffset+1].ammountWanted+"");
	}
	
	@Override
	public void update(){
		((GuiButton)buttonList.get(0)).enabled=((GuiButton)buttonList.get(1)).enabled=tileCB.isActive;
		if(Mouse.isButtonDown(Mouse.getButtonIndex("BUTTON0"))){
			mouseleftTime++;
			if(mouseleftTime>10&&tileCB.getWorld().getTotalWorldTime()%2==0){
				if(((GuiButton)buttonList.get(5)).mousePressed(mc,mouseX,mouseY)){
					actionPerformed(((GuiButton)buttonList.get(5)));
				}
				if(((GuiButton)buttonList.get(6)).mousePressed(mc,mouseX,mouseY)){
					actionPerformed(((GuiButton)buttonList.get(6)));
				}
			}
		}else mouseleftTime=0;
		
		listOffset=((SmartCrafterContainer)player.openContainer).listOffset;
		if(!isCtrlKeyDown())super.update();
		else{
			if(!guiParticles.isEmpty())for(int c=0;c<guiParticles.size();c++){
				GuiParticle ab=guiParticles.get(c);
				if(ab!=null&&!ab.isDead){
					if(ab.age<2){
						ab.UpdateScreenRes(width,height,guiLeft,guiTop,xSize,ySize);
						ab.UpdateParticle();
					}else ab.maxAge=10+UtilM.RInt(40);
					
				}
			}
		}
	}
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	@Override
	protected void actionPerformed(GuiButton b){
		boolean sideSwitcher=false;
		int rot=(int)(U.getMC().thePlayer.rotationYawHead%360);
		while(rot<0)rot+=360; 
		if(rot<155&&rot>25)sideSwitcher=true;
		switch (b.id){
		case 0:{
			Magiology.NETWORK_CHANNEL.sendToServer(new RightClickBlockPacket(tileCB.getPos().add(0, 0, (sideSwitcher?1:-1)), (byte)side));
			TickEvents.bufferedGui=TickEvents.instance.new CientPlayerBufferedGui(tileCB.getPos());
		}break;
		case 1:{
			Magiology.NETWORK_CHANNEL.sendToServer(new RightClickBlockPacket(tileCB.getPos().add(0,0,(sideSwitcher?-1:1)), (byte)side));
			TickEvents.bufferedGui=TickEvents.instance.new CientPlayerBufferedGui(tileCB.getPos());
		}break;
		case 2:{
			
		}break;
		case 3:{
			UtilM.sendMessage(new GenericServerIntPacket(1, listOffset));
		}break;
		case 4:{
			UtilM.sendMessage(new GenericServerIntPacket(1, listOffset+1));
		}break;
		case 5:{
			UtilM.sendMessage(new GenericServerIntPacket(0, -1));
		}break;
		case 6:{
			UtilM.sendMessage(new GenericServerIntPacket(0, 1));
		}break;
		case 7:{
			UtilM.sendMessage(new GenericServerIntPacket(0, -listOffset));
		}break;
		case 8:{
			UtilM.sendMessage(new GenericServerIntPacket(0, 48-listOffset));
		}break;
		}
	}
	public void drawCenteredStringShoadowless(FontRenderer p_73732_1_, String p_73732_2_, int p_73732_3_, int p_73732_4_, int p_73732_5_)
	{
		p_73732_1_.drawString(p_73732_2_, p_73732_3_ - p_73732_1_.getStringWidth(p_73732_2_) / 2, p_73732_4_, p_73732_5_);
	}
}
