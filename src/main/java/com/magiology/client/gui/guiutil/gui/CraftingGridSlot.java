package com.magiology.client.gui.guiutil.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class CraftingGridSlot extends Slot{
	
	public CraftingGridSlot(IInventory inv, int x,int y, int index){
		super(inv, x, y, index);
	}
	@Override
	public boolean canTakeStack(EntityPlayer p_82869_1_){
		return false;
	}
}
