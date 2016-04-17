package com.magiology.client.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class ISidedPowerInstructorContainer extends Container{
	
	EntityPlayer player;
	public TileEntity tile;
	public ISidedPowerInstructorContainer(EntityPlayer player,TileEntity tile){
		this.tile=tile;
		this.player=player;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_){
		 super.onContainerClosed(p_75134_1_);
	}
}
