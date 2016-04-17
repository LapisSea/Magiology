package com.magiology.client.gui.gui;

import java.util.ArrayList;
import java.util.List;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.mcobjects.effect.GuiParticle;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
public abstract class GuiContainerAndGuiParticles extends GuiContainer implements Updateable{
	
	public static List<GuiParticle> guiParticles=new ArrayList<GuiParticle>();
	
	public static void spawnGuiParticle(GuiParticle particles){
		boolean isNotFound=true;
		for(int c=0;c<guiParticles.size();c++)if(guiParticles.get(c).isDead){
			isNotFound=false;
			guiParticles.set(c, particles);
			c=guiParticles.size();
		}
		if(isNotFound)guiParticles.add(particles);
	}

	public GuiContainerAndGuiParticles(Container objects){
		super(objects);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_,int p_146976_2_, int p_146976_3_){}
	
	public boolean isMouseOverObj(Object obj,int x,int y){
		if(obj instanceof Slot){
			return this.isPointInRegion(((Slot)obj).xDisplayPosition, ((Slot)obj).yDisplayPosition, 16, 16, x,y);
		}else if(obj instanceof GuiButton){
			return this.isPointInRegion(((GuiButton)obj).xPosition, ((GuiButton)obj).yPosition, ((GuiButton)obj).width, ((GuiButton)obj).height, x,y);
		}else return false;
	}
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
		guiParticles.clear();
	}
	
	public void renderParticles(float partialTicks){
		for(int c=0;c<guiParticles.size();c++){
			GuiParticle ab=guiParticles.get(c);
			if(ab!=null&&!ab.isDead)ab.renderParticle(partialTicks);
		}
	}
	@Override
	public void update(){
		updateParticles();
	}
	public void updateParticles(){
		for(int c=0;c<guiParticles.size();c++){
			GuiParticle ab=guiParticles.get(c);
			if(ab!=null&&!ab.isDead){
				ab.UpdateScreenRes(width,height,guiLeft,guiTop,xSize,ySize);
				ab.UpdateParticle();
			}
		}
	}
}
