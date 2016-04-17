package com.magiology.mcobjects.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class ItemContainer extends Item implements IInventory{
	

	public static int INV_SIZE = 9;
	public ItemStack[] containerItems=new ItemStack[INV_SIZE];
	private final ItemStack invItem;
	
	
	public ItemContainer(ItemStack stack,int invSize){
		this.invItem = stack;
		INV_SIZE=invSize;
		if (!stack.hasTagCompound())stack.setTagCompound(new NBTTagCompound());
		stack.readFromNBT(stack.getTagCompound());
	}
	
	@Override
	public void clear(){
		
	}

	@Override
	public void closeInventory(EntityPlayer player){}

	@Override
	public ItemStack decrStackSize(int slot,int amount){
		ItemStack stack = getStackInSlot(slot);
		if(stack != null){
			if(stack.stackSize > amount){
				stack = stack.splitStack(amount);
				markDirty();
			}else{
				setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField(int id){
		return 0;
	}

	@Override
	public int getFieldCount(){
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		
		return 2;
	}

	@Override
	public String getName() {
		
		return "InventoryPants_42";
	}

	@Override
	public int getSizeInventory(){return containerItems.length;}

	@Override
	public ItemStack getStackInSlot(int slot){return containerItems[slot];}

	@Override
	public boolean hasCustomName(){
		
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack){
		return true;//!(RegisterItemUpgrades.isItemUpgrade(itemstack.getItem()));
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player){return player.inventory.armorInventory[1]==invItem;}
	
	@Override
	public void markDirty(){
		for (int i=0;i<getSizeInventory();i++){
		if(getStackInSlot(i)!=null&&getStackInSlot(i).stackSize==0)
		containerItems[i]=null;
		}
		invItem.writeToNBT(invItem.getTagCompound());
	}
	
	@Override
	public void openInventory(EntityPlayer player){}

	public void readFromNBT(NBTTagCompound tagcompound){
		NBTTagList items = tagcompound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
		
		for(int i=0;i<items.tagCount();i++){
			NBTTagCompound item = items.getCompoundTagAt(i);
			byte slot = item.getByte("Slot");
			
			if(slot>=0&&slot<getSizeInventory())containerItems[slot] = ItemStack.loadItemStackFromNBT(item);
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int slot){
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)setInventorySlotContents(slot, null);
		return stack;
	}

	@Override
	public void setField(int id, int value){
		
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack){
		this.containerItems[slot]=itemstack;
		if(itemstack!=null&&itemstack.stackSize>this.getInventoryStackLimit())itemstack.stackSize = this.getInventoryStackLimit();
		markDirty();
		}

	public void writeToNBT(NBTTagCompound tagcompound){
		NBTTagList items = new NBTTagList();
		
		for(int i=0;i<getSizeInventory();i++){
			if(getStackInSlot(i)!=null){
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				getStackInSlot(i).writeToNBT(item);
				items.appendTag(item);
			}
		}
		tagcompound.setTag("ItemInventory", items);
	}
	
	
}
