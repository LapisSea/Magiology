package com.magiology.client.gui.container;

import com.magiology.core.init.MItems;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CommandCenterContainer extends Container{
	
	public static class CommandCenterContainerSlot extends Slot{
		public CommandCenterContainerSlot(IInventory inventoryIn, int index, int xPosition, int yPosition){
			super(inventoryIn, index, xPosition, yPosition);
		}
		@Override
		public boolean isItemValid(ItemStack stack){
			return UtilM.isItemInStack(MItems.commandContainer, stack);
		}
	}
	public int selectedSlotId=-1;
	
	public TileEntityNetworkProgramHolder tile;
	
	public CommandCenterContainer(EntityPlayer player,TileEntityNetworkProgramHolder tile){
		this.tile=tile;
		for(int i=0;i<3;i++)for(int j=0;j<9;j++)addSlotToContainer(new Slot(player.inventory,9+j+9*i,8+j*18,84+18*i));
		for(int i=0;i<9;i++)addSlotToContainer(new Slot(player.inventory,i,8+i*18,142));
		for(int i=0;i<4;i++)for(int j=0;j<4;j++)addSlotToContainer(new CommandCenterContainerSlot(tile,j+4*i,j*18+53,18*i+7));
	}
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	@Override
	public ItemStack func_184996_a(int slotId, int clickedButton, ClickType mode, EntityPlayer player){
		if(slotId<0)return super.func_184996_a(slotId, clickedButton, mode, player);
		Slot clickedSlot=inventorySlots.get(slotId);
		switch (clickedButton){
		case 0:if(selectedSlotId==slotId-36)selectedSlotId=-1;break;
		case 1:if(UtilM.isItemInStack(MItems.commandContainer, clickedSlot.getStack())&&clickedSlot.inventory instanceof TileEntityNetworkProgramHolder){
			selectedSlotId=slotId-36;
			return clickedSlot.getStack();
		}break;
		}
		return super.func_184996_a(slotId, clickedButton, mode, player);
	}
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber){
		try{
			Slot clickedSlot=inventorySlots.get(slotNumber);
			if(clickedSlot==null||!clickedSlot.getHasStack())return null;
			ItemStack itemstack=null;
			ItemStack itemstack1=clickedSlot.getStack();
			itemstack=itemstack1.copy();
			if(clickedSlot.inventory instanceof TileEntityNetworkProgramHolder){
				if(!mergeItemStack(itemstack1, 27, 36, false))if(!mergeItemStack(itemstack1, 0, 27, false))return null;
			}else if(UtilM.isItemInStack(MItems.commandContainer, itemstack1)){
				if(!mergeItemStack(itemstack1, 36, inventorySlots.size(), false))return null;
			}
			if(itemstack1.stackSize==0)clickedSlot.putStack((ItemStack)null);
			else					   clickedSlot.onSlotChanged();
			if(itemstack1.stackSize==itemstack.stackSize)return null;
			clickedSlot.onPickupFromSlot(player, itemstack1);
			return itemstack;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
