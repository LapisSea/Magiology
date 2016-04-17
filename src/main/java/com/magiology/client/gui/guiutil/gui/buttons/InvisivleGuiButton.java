package com.magiology.client.gui.guiutil.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InvisivleGuiButton extends GuiButton{

	public InvisivleGuiButton(int id, int x, int y,int width, int height, String txt){
		super(id, x, y, width, height, txt);
	}
	
	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_){}

}
