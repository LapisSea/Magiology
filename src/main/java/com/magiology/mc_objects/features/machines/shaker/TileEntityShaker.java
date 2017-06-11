package com.magiology.mc_objects.features.machines.shaker;

import com.magiology.util.m_extensions.TileEntityMTickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class TileEntityShaker extends TileEntityMTickable implements IInventory{
	
	private final List<ItemStack> stacks=new ArrayList<>();
	
	@Override
	public void onLoad(){
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		//		stacks=UtilM.readStacksFromNBT(compound, "Inv");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		
		//		UtilM.writeStacksToNBT(stacks, compound, "Inv");
		return super.writeToNBT(compound);
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
		return stacks.size();
	}
	
	@Override
	public ItemStack getStackInSlot(int index){
		return stacks.get(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count){
		ItemStack itemstack=ItemStackHelper.getAndSplit(stacks, index, count);
		if(itemstack!=null) this.markDirty();
		return itemstack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index){
		return ItemStackHelper.getAndRemove(stacks, index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		//		stacks[index]=stack;
		if(stack!=null&&stack.getCount()>this.getInventoryStackLimit()){
			//			stack.stackSize=this.getInventoryStackLimit();
		}
		this.markDirty();
	}
	
	@Override
	public int getInventoryStackLimit(){
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player){
		return isInWorld()&&player.getDistanceSq(x()+0.5, y()+0.5, z()+0.5)<=64;
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
	public int getField(int id){
		return 0;
	}
	
	@Override
	public void setField(int id, int value){}
	
	@Override
	public int getFieldCount(){
		return 0;
	}
	
	@Override
	public void clear(){}
	
	@Override
	public boolean isEmpty(){
		return false;
	}
}
