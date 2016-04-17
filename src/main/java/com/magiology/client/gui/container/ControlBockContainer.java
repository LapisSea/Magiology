package com.magiology.client.gui.container;

import com.magiology.client.gui.guiutil.container.ControlBockContainerSlot;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ControlBockContainer extends Container{
	
	public ControlBockContainer(InventoryPlayer pInventory,TileEntityControlBlock tileCB){
		for(int i=0;i<4;i++)this.addSlotToContainer(new ControlBockContainerSlot(tileCB,i,80,8+i*18));
		
		for(int i=0;i<3;i++){for(int j=0;j<9;j++){
				this.addSlotToContainer(new Slot(pInventory,9+j+9*i,8+j*18,84+18*i));
		}}
		for(int i=0;i<9;i++)this.addSlotToContainer(new Slot(pInventory,i,8+i*18,142));
	}
	
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber){
		ItemStack itemstack=null;
		{
			Slot slot = this.inventorySlots.get(slotNumber);
			if(slot!=null&&slot.getHasStack()){
				ItemStack itemstack1=slot.getStack();
				itemstack=itemstack1.copy();
				if(slotNumber<=3){
					if(!mergeItemStack(itemstack1, 31, 40, false))if(!mergeItemStack(itemstack1, 4, 31, false))return null;
					slot.onSlotChange(itemstack1, itemstack);
				}else if(slotNumber!=0&&slotNumber!=1&&slotNumber!=2&&slotNumber!=3){
					if(!mergeItemStack(itemstack1, 0, 4, true)){
						if(slotNumber>=31&&slotNumber<=39){if(!mergeItemStack(itemstack1, 4, 31, false))return null;}
						else if(!mergeItemStack(itemstack1, 31, 40, false))return null;
						if(itemstack1.stackSize==0)slot.putStack((ItemStack)null);
						else slot.onSlotChanged();
						return null;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}
				if(itemstack1.stackSize==0)slot.putStack((ItemStack)null);
				else					   slot.onSlotChanged();
				if(itemstack1.stackSize==itemstack.stackSize)return null;
				slot.onPickupFromSlot(player, itemstack1);
			}
		}
		
		return itemstack;
		
	}
}
