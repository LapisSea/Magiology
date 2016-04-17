package com.magiology.client.gui.guiutil.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class FakeContainer implements IInventory{
	

	private int invSS;
	public ItemStack[] slots;
	
	
	public FakeContainer(ItemStack[] stacks,int invStackSize){
		slots=stacks;
		invSS=invStackSize;
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ItemStack decrStackSize(int v1, int v2){
		
		if(this.slots[v1]!=null){
			ItemStack ItemS=null;
			
			if(this.slots[v1].stackSize<=v2){
				ItemS=this.slots[v1];
				this.slots[v1]=null;
				return ItemS;
			}else{
				ItemS=this.slots[v1].splitStack(v2);
				if(this.slots[v1].stackSize==0){
					this.slots[v1]=null;
				}
			}
			return ItemS;
		}
		return null;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField(int id){
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInventoryStackLimit(){
		return this.invSS;
	}

	@Override
	public String getName(){
		return "UniversalInventory";
	}
	
	@Override
	public int getSizeInventory(){
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int v1){
		return this.slots[v1<slots.length?v1:slots.length-1];
	}
	@Override
	public boolean hasCustomName(){
		return true;
	}
	
	@Override
	public boolean isItemValidForSlot(int v1, ItemStack stack) {
		boolean reurn1=false;
		//if(RegisterItemUpgrades.isItemUpgrade(stack.getItem()))reurn1=true;
		return reurn1;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return true;
	}

	@Override
	public void markDirty(){
		
	}

	@Override
	public void openInventory(EntityPlayer player){
		// TODO Auto-generated method stub
		
	}

	@Override
	public ItemStack removeStackFromSlot(int v1){
		if(this.slots[v1]!=null){
			ItemStack ItemS=this.slots[v1];	
			this.slots[v1]=null;
			return ItemS;
		}
		return null;
	}

	@Override
	public void setField(int id, int value){
		// TODO Auto-generated method stub
		
	}

	public void setInventoryName(String string){
		
	}

	@Override
	public void setInventorySlotContents(int v1, ItemStack stack){
		int v2=v1;
		if(v2>=slots.length)return;
		
		this.slots[v2]=stack;
		if(stack!=null&&stack.stackSize>this.getInventoryStackLimit()){
			stack.stackSize=this.getInventoryStackLimit();
		}
		
	}
	
	
}
