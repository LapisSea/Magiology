package com.magiology.client.gui.gui;

import com.magiology.client.gui.container.ArmorContainer;
import com.magiology.client.gui.custom.guiparticels.GuiStandardFX;
import com.magiology.client.gui.guiutil.gui.buttons.InvisivleGuiButton;
import com.magiology.core.MReference;
import com.magiology.mcobjects.effect.GuiParticle;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiArmor extends GuiContainerAndGuiParticles{
	
protected static ItemRenderer itemRenderer = new ItemRenderer(U.getMC());
	//	private ItemRendererHelmet42 IRH42 = new ItemRendererHelmet42();
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/GuiArmorEditor.png");
	int numberOfSlots=0,xMouse=0,yMouse=0,startRandom;
	double o1=0,o2=0,speed=0,playerXPos=-1,playerYPos=-1,prevPlayerYPos,sliderPos,prevSliderPos;
	ItemStack[] p42;
	EntityPlayer player;
	InvisivleGuiButton playerButton=new InvisivleGuiButton(5, (int)playerXPos, (int)playerYPos, 18, 32, 5+"");
	ResourceLocation texture1=new ResourceLocation(MReference.MODID+":/textures/particle/SmoothBuble1.png");
	
	public GuiArmor(EntityPlayer player,ItemStack[] armorInventory){
		super(new ArmorContainer(player, armorInventory));
		this.p42=armorInventory;
		this.player=player;
		this.xSize=176;
		this.ySize=166;
	}
	@Override
	 protected void actionPerformed(GuiButton b){
		 switch (b.id){
		 case 5:{
			speed+=RandUtil.CRD(5);
		 }break;
		 
		 }
	 }
	
	public void drawBIGItemStackAt(ItemStack stack,int x,int y,double scale){
		scale*=15;
		if(stack==null)return;
		OpenGLM.pushMatrix();
		OpenGLM.translate( x, y, 0);
		OpenGLM.scale(scale,scale,scale);
//		IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(stack, EQUIPPED);
//		if(customRenderer!=null){
//			OpenGLM.pushMatrix();
//			OpenGLM.rotate(180,0,0,0);
//			OpenGLM.translate(-0.5, -0.5, 0);
//			OpenGLM.depthMask(true);
//			if(customRenderer instanceof ItemRendererHelmet42){
//				IRH42.model.shouldFollowThePlayerMaster=false;
//				IRH42.model.shouldFollowThePlayerHasAMaster=true;
//				IRH42.renderItem(EQUIPPED, stack);
//			}
//			else customRenderer.renderItem(EQUIPPED, stack);
//			OpenGLM.popMatrix();
//		}else{
////			TessHelper.bindTexture(TextureMap.locationItemsTexture);
////			IIcon iicon = stack.getItem().getIcon(stack, 0);
////			OpenGLM.disableLighting();
////			ItemRenderer.renderItemIn2D(Tessellator.instance, iicon.getMaxU(), iicon.getMaxV(), iicon.getMinU(), iicon.getMinV(), iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
////			OpenGLM.enableLighting();
//			Render.RI().renderItemModel(stack);
//		}
		OpenGLM.popMatrix();
	}
	
	public void drawBIGRotatingItemStack(ItemStack stack){
		long WT=U.getMC().theWorld.getTotalWorldTime();
		int angleX=(int)(WT%360),rotatePosX=37+8*3;
		double angleY2=500,angleY1=(int)(WT%angleY2),angleY3=(angleY1>angleY2/2?angleY2-angleY1:angleY1),
				angleY=(angleY3-angleY2/4)/(angleY2/25),rotateYOffset=36;
		OpenGLM.pushMatrix();
		OpenGLM.translate(rotatePosX, 0, 0);
		OpenGLM.rotate(angleX, 0, 1, 0);
		OpenGLM.translate(0,  rotateYOffset, 0);
		OpenGLM.rotate(angleY, 1, 0, 0);
		OpenGLM.translate(0, -rotateYOffset, 0);
		double offset=(1F/16F)*15*1.5;
		OpenGLM.translate(-8*3+offset, 0, offset);
		drawBIGItemStackAt(stack, 0, 15, 3);
		OpenGLM.popMatrix();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int v2, int v3){
		TessUtil.bindTexture(main);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		this.renderParticles(v1);
		TessUtil.bindTexture(main);
		OpenGLM.translate(guiLeft, guiTop, 0);
		GL11U.setUpOpaqueRendering(1);
		OpenGLM.color(1, 1, 1, o1);
		this.drawTexturedModalRect(60, 65, 0, 170, 100, 12);
		this.drawTexturedModalRect(94, 61, 34, 166, 74, 4);
//		if(p42[pos]!=null&&p42[pos].getItem() instanceof UpgItem){
//			numberOfSlots=((UpgItem)p42[pos].getItem()).getInventorySize();
//		}
		for(int a=0;a<numberOfSlots;a++){
			int overrideNumberOfSlots=numberOfSlots;
			if(overrideNumberOfSlots>4)overrideNumberOfSlots=4;
			if(a<4)this.drawTexturedModalRect(131+a*18-(overrideNumberOfSlots*9), 20-(numberOfSlots>overrideNumberOfSlots?9:0), 7, 83, 18, 18);
			if(a>3&&numberOfSlots>overrideNumberOfSlots){
				this.drawTexturedModalRect(122, 29, 7, 83, 18, 18);
			}
		}
		OpenGLM.color(1, 1, 1, 1);
		GL11U.endOpaqueRendering();
		OpenGLM.translate(-guiLeft, -guiTop, 0);
		
		o2=0;
//		if(p42[pos]!=null&&p42[pos].getItem() instanceof UpgItem)o2=1;
		if(playerXPos!=guiLeft-5){
			playerXPos=guiLeft-5;
			playerYPos=-guiTop-100-startRandom;
		}
		playerButton.xPosition=(int)playerXPos-10;
		playerButton.yPosition=(int)-playerYPos-26;
		OpenGLM.pushMatrix();
		float posY=(float)(prevPlayerYPos+(playerYPos-prevPlayerYPos)*PartialTicksUtil.partialTicks);
//		posY=(float)playerYPos;
		OpenGLM.translate(playerXPos,-posY, 0);
		TessUtil.drawPlayerIntoGUI(0, 0, 11, (float)playerXPos-v2 , (-posY)-v3, this.mc.thePlayer);
		TessUtil.drawSlotLightMapWcustomSizes(this, -10, -26, 18, 32,false,true);
		TessUtil.drawSlotLightMapWcustomSizes(this, -8, -24, 17, 28,false,false);
		this.drawTexturedModalRect(6, 4, 4, 100, 3, 1);
		this.drawTexturedModalRect(6, 5, 7, 2, 2, 1);
		this.drawTexturedModalRect(6, -25, 4, 100, 3, 1);
		this.drawTexturedModalRect(6, -26, 7, 2, 2, 1);
		OpenGLM.popMatrix();
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int i,int e){
		sliderPos=(player.openContainer instanceof ArmorContainer)?((ArmorContainer)player.openContainer).sliderPos:0;
		int pos=(int)Math.round(Math.abs(sliderPos/16));
		for(int a=0;a<5;a++){
			if(o1<o2)o1+=0.035;
			if(o1>o2)o1-=0.035;
		}
		long WT=U.getMC().theWorld.getTotalWorldTime();
		OpenGLM.translate(0, 0, 250);
		drawBIGRotatingItemStack(p42[pos]);
		OpenGLM.translate(0, 0,-250);
		
		TessUtil.bindTexture(main);
		GL11U.setUpOpaqueRendering(2);
		double scale=0.5;
		drawSmartShit();
		GL11U.endOpaqueRendering();
		OpenGLM.disableLighting();
		GL11U.allOpacityIs(false);
		OpenGLM.translate(0, 0, 1);
		drawSmartShit();
		OpenGLM.translate(0, 0, -1);
		GL11U.setUpOpaqueRendering(2);
		double r1=(WT%20.0)/10.0,g1=(WT%65.0)/32.5,b1=(WT%50.0)/25.0;
		double r=r1>1?2-r1:r1,g=g1>1?2-g1:g1,b=b1>1?2-b1:b1;
		for(int spot=0;spot<4;spot++)if(isMouseOverFakeSlot(11, 8+spot*16, i, e)){
			int x=11,y=7+spot*16;
			OpenGLM.color(r, g, b, 0.3);
			OpenGLM.pushMatrix();
			OpenGLM.translate(x, y, 0);
			OpenGLM.scale(scale,scale,scale);
			OpenGLM.disableLighting();
			OpenGLM.disableDepth();
			OpenGLM.colorMask(true, true, true, false);
			this.drawTexturedModalRect(0, 0, 180, 40, 32, 32);
			OpenGLM.colorMask(true, true, true, true);
			OpenGLM.enableLighting();
			OpenGLM.enableDepth();
			OpenGLM.popMatrix();
		}
		
		GL11U.endOpaqueRendering();
	}
	public void drawSmartShit(){
		OpenGLM.pushMatrix();
		OpenGLM.translate(0, prevSliderPos+(sliderPos-prevSliderPos)*PartialTicksUtil.partialTicks, 0);
		double scale=0.5;
		int x=6,y=6+48;
		OpenGLM.translate(x, y, 0);
		OpenGLM.scale(scale,scale,scale);
		this.drawTexturedModalRect(0, 0, 180, 3, 10, 32);
		OpenGLM.popMatrix();
	}
	
	
	@Override
	 public void initGui(){
		 super.initGui();
		 for(int a=0;a<p42.length;a++){
			 InvisivleGuiButton button=new InvisivleGuiButton(a, 11+guiLeft, 55-16*a+guiTop, 16, 16, a+"");
			 this.buttonList.add(button); 
		 }
		 playerXPos=guiLeft-5;
		 playerYPos=-guiTop-100-startRandom;
		 this.buttonList.add(playerButton);
		 startRandom=RandUtil.RI((int)(ySize/2.9F));
	 }
	
	 private boolean isMouseOverFakeSlot(int xPos,int yPos,int x, int y){
		 return this.isPointInRegion(xPos, yPos, 15, 14, x, y);
	 }
	 
	 @Override
	public void onGuiClosed(){
		super.onGuiClosed();
	}
	 @Override
	public void update(){
		prevPlayerYPos=playerYPos;
		prevSliderPos=sliderPos;
		this.updateParticles();
		for(int a=0;a<4;a++){
			GuiParticle part=new GuiParticle(100+RandUtil.RI(68)+guiLeft, 60+guiTop, 30, 0,-0.5+RandUtil.CRF(0.5), RandUtil.RF()*2+2, o1/20, 1, RandUtil.RF(),RandUtil.RF()*0.5,RandUtil.RF()*0.2, texture1, GuiStandardFX.CloudFX);
			part.hasMovementNoise=false;
			GuiContainerAndGuiParticles.spawnGuiParticle(part);
		}
		speed+=RandUtil.CRD(0.15);
		double pos1=-guiTop-100-0.2*ySize/2.9F;
		double pos2=-guiTop-100-0.8*ySize/2.9F;
		if(playerYPos>pos1)speed-=0.015;
		if(playerYPos<pos2)speed+=0.015;
		speed*=0.99;
		this.playerYPos+=(float)speed;
		if(playerYPos>-guiTop-100){playerYPos=-guiTop-100;speed*=-1;}
		if(playerYPos<-guiTop-100-ySize/2.9F){playerYPos=-guiTop-100-ySize/2.9F;speed*=-1;}
	}
	 
}
