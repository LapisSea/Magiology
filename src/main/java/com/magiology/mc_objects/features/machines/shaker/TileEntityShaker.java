package com.magiology.mc_objects.features.machines.shaker;

import com.magiology.util.m_extensions.TileEntityMTickable;
import com.magiology.util.statics.UtilM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityShaker extends TileEntityMTickable implements IInventory{
	
	private ItemStack[] stacks=new ItemStack[1];
	
	@Override
	public void onLoad(){
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		stacks=UtilM.readStacksFromNBT(compound,"Inv");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		UtilM.writeStacksToNBT(stacks,compound,"Inv");
		return compound;
	}
	
	@Override
	public void update(){
		
	}
	
	@Override
	public String getName(){
		return "Shaker";
	}
	
	@Override
	public boolean hasCustomName(){
		return false;
	}
	
	@Override
	public int getSizeInventory(){
		return stacks.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int index){
		return stacks[index];
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count){
		ItemStack itemstack=ItemStackHelper.getAndSplit(stacks, index, count);
		if(itemstack!=null)this.markDirty();
		return itemstack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index){
        return ItemStackHelper.getAndRemove(stacks, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		stacks[index]=stack;
		if(stack!=null&&stack.stackSize>this.getInventoryStackLimit()){
			stack.stackSize=this.getInventoryStackLimit();
		}
		this.markDirty();
	}
	
	@Override
	public int getInventoryStackLimit(){
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return isValid()&&player.getDistanceSq(x()+0.5, y()+0.5, z()+0.5)<=64;
	}
	
	@Override
	public void openInventory(EntityPlayer player){}
	
	@Override
	public void closeInventory(EntityPlayer player){}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return true;
	}
	
	@Override
	public int getField(int id){return 0;}
	
	@Override
	public void setField(int id, int value){}
	
	@Override
	public int getFieldCount(){return 0;}
	
	@Override
	public void clear(){}
}
