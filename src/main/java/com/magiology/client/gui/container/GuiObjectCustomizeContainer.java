package com.magiology.client.gui.container;

import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiObjectCustomizeContainer extends Container{
	
	TileEntityHologramProjector hologramProjector;
	EntityPlayer player;
	
	public GuiObjectCustomizeContainer(EntityPlayer player, TileEntityHologramProjector hologramProjector){
		this.player=player;
		this.hologramProjector=hologramProjector;
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player){return true;}
	
}
