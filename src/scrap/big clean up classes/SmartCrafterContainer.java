package com.magiology.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.guiutil.container.FakeContainer;
import com.magiology.client.gui.guiutil.gui.CraftingGridSlot;
import com.magiology.mcobjects.tileentityes.TileEntitySmartCrafter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SmartCrafterContainer extends Container implements Updateable{
	
	public final TileEntitySmartCrafter tileSC;
	public int side,listOffset,lastListOffset;
	List<Slot> constantSlots=new ArrayList(),addedSlots=new ArrayList();
	EntityPlayer player;
	public SmartCrafterContainer(EntityPlayer player,TileEntitySmartCrafter tileSC2, int side){
		tileSC=tileSC2;
		this.player=player;
		InventoryPlayer pInventory=player.inventory;
		for(int i=0;i<3;i++){for(int j=0;j<9;j++){this.addSlotToList(constantSlots,new Slot(pInventory,9+j+9*i,8+j*18,85+18*i+46));}}
		for(int i=0;i<9;i++)this.addSlotToList(constantSlots,new Slot(pInventory,i,8+i*18,143+46));
		setCraftingSlots();
		updateSlots();
	}
	public void setCraftingSlots(){
		addedSlots.clear();
		for(int i=0;i<2;i++){
			FakeContainer inv=new FakeContainer(tileSC.wantedProducts[i+listOffset].grid, 64);
			FakeContainer output=new FakeContainer(tileSC.wantedProducts[i+listOffset].product, 64);
			for(int x=0;x<3;x++)for(int y=0;y<3;y++)
			addSlotToList(addedSlots,new CraftingGridSlot(inv, x+3*y, x*18+79, y*18-28+i*57+40));
			addSlotToList(addedSlots,new CraftingGridSlot(output, 0, 41, i*57-10+40));
		}
	}
	public Slot addSlotToList(List list,Slot slot){
		slot.slotNumber=constantSlots.size()+addedSlots.size();
		list.add(slot);
		return slot;
	}
	public void updateSlots(){
		inventorySlots.clear();
		for(int a=0;a<constantSlots.size();a++)inventorySlots.add(constantSlots.get(a));
		for(int a=0;a<addedSlots.size();a++)inventorySlots.add(addedSlots.get(a));
		inventoryItemStacks.clear();
		for(int a=0;a<inventorySlots.size();a++)inventoryItemStacks.add(inventorySlots.get(a)!=null?((ItemStack)((Slot)inventorySlots.get(a)).getStack()):(ItemStack)null);
		detectAndSendChanges();
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
		
		return itemstack;
	}
	@Override
	public ItemStack slotClick(int slotId, int var1, int var2, EntityPlayer player){
		ItemStack result=null;
		boolean noMod=true;
		Slot slot=null;try{slot=getSlot(slotId);}catch(Exception e){return null;}
		
		if(slot instanceof CraftingGridSlot)noMod=false;
		if(noMod)super.slotClick(slotId, var1, var2, player);
		else{
			if(player.inventory.getItemStack()!=null){
				ItemStack itemStack=player.inventory.getItemStack().copy();
				itemStack.stackSize=1;
				slot.putStack(itemStack);
			}else{
				slot.putStack((ItemStack)null);
			}
		}
//		Helper.println("no mod:"+noMod+"\t"+result);
		return result;
	}
	@Override
	public void update(){
		if(listOffset!=lastListOffset){
			lastListOffset=listOffset;
			setCraftingSlots();
			updateSlots();
		}
	}
}
