package com.magiology.client.gui.container;

import com.magiology.client.gui.guiutil.container.CustomSlot;
import com.magiology.client.gui.guiutil.container.FakeContainer;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class UpgradeContainer extends Container{
	
	private final TileEntityPow tileCB;
	IInventory tileStacks;
	
	public UpgradeContainer(InventoryPlayer pInventory,TileEntityPow tileCB2){
		tileCB=tileCB2;
		tileStacks=new FakeContainer(tileCB.containerItems,64);
		
		for(int i=0;i<3;i++){for(int j=0;j<9;j++){this.addSlotToContainer(new Slot(pInventory,9+j+9*i,8+j*18,49+18*i));}}
		for(int i=0;i<9;i++)this.addSlotToContainer(new Slot(pInventory,i,8+i*18,107));
		
		int number=tileCB.containerItems.length,offsetx=176/2+1-number*9,offsety=7;
		for(int a=0;a<number;a++){
			this.addSlotToContainer(new CustomSlot(tileStacks,a, offsetx, offsety,tileCB));
			offsetx+=18;
		}
	}
	
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_){
		 super.onContainerClosed(p_75134_1_);
		}
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber){
		ItemStack itemstack=null;
		{
			Slot slot = this.inventorySlots.get(slotNumber);
			if(slot!=null&&slot.getHasStack()){
				ItemStack itemstack1=slot.getStack();
				itemstack=itemstack1.copy();
				if(slotNumber>35){
					if(!mergeItemStack(itemstack1, 27, 36, false))
						if(!mergeItemStack(itemstack1, 0, 27, false))return null;
					slot.onSlotChange(itemstack1, itemstack);
					
				}else if(true){//if(RegisterItemUpgrades.isItemUpgrade(slot.getStack().getItem())&&RegisterItemUpgrades.isUpgradeValid(RegisterItemUpgrades.getItemUpgradeType(RegisterItemUpgrades.getItemUpgradeID(slot.getStack().getItem())),tileCB.container)){
					
					if(!mergeItemStack(itemstack1, 36, 36+tileCB.containerItems.length, false)){
						if(slotNumber>=36&&slotNumber<=36+tileCB.containerItems.length){
							if(!mergeItemStack(itemstack1, 0, 27, false))return null;
						}
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
