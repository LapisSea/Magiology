package com.magiology.client.gui.guiutil.container;

import com.magiology.util.utilclasses.PrintUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class UpgItemContainer implements IInventory{
	
	public final Item Citem;
	public final ItemStack container;
	private int invSS=64;
	public final NBTTagCompound NBT;
	
	
	public UpgItemContainer(ItemStack container){
		this.container=container;
		if(container.getItem()!=null){
			invSS=64;
			NBT=container.getTagCompound();
			Citem=container.getItem();
		}
		else{
			NBT=null;
			Citem=null;
		}
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void closeInventory(EntityPlayer player){
		this.markDirty();
	}

	@Override
	public ItemStack decrStackSize(int v1, int v2){
		ItemStack[] stacks1=this.slots();
		if(stacks1[v1]!=null){
			ItemStack ItemS=null;
			
			if(stacks1[v1].stackSize<=v2){
				ItemS=stacks1[v1];
				stacks1[v1]=null;
//				((UpgItem)Citem).setStacks(container, stacks1);
				return ItemS;
			}else{
				ItemS=stacks1[v1].splitStack(v2);
				if(stacks1[v1].stackSize==0){
					stacks1[v1]=null;
//					((UpgItem)Citem).setStacks(container, stacks1);
				}
			}
//			((UpgItem)Citem).setStacks(container, stacks1);
			return ItemS;
		}
		return null;
	}

	@Override
	public IChatComponent getDisplayName(){
		return new ChatComponentText(this.getName());
	}

	@Override
	public int getField(int id) {
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
		return container.getDisplayName()+" inventory";
	}

	@Override
	public int getSizeInventory(){
//		return ((UpgItem)Citem).getInventorySize();
		return 5;
	}
	
	@Override
	public ItemStack getStackInSlot(int v1){
//		UtilM.println("getStackInSlot was called from: "+container.getDisplayName()+"\n");
		return this.slots()[v1];
	}

	@Override
	public boolean hasCustomName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int v1, ItemStack stack) {
		boolean reurn1=false;
//		if(RegisterItemUpgrades.isItemUpgrade(stack.getItem()))reurn1=true;
		return reurn1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return true;
	}
	@Override
	public void markDirty(){
//		((UpgItem)Citem).setStacks(container, ((UpgItem)Citem).getStacks(container));
	}

	@Override
	public void openInventory(EntityPlayer player){
		
	}

	@Override
	public ItemStack removeStackFromSlot(int v1){
		ItemStack[] a=slots();
		if(a[v1]!=null){
			ItemStack ItemS=a[v1];	
			a[v1]=null;
			//((UpgItem)Citem).setStacks(container, a);
			return ItemS;
		}
		return null;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	public void setInventoryName(String string){
		
	}

	@Override
	public void setInventorySlotContents(int v1, ItemStack stack){
		int v2=v1;
		if(v2>this.getInventoryStackLimit()){
			PrintUtil.printlnEr("ERROR--> "+container.getDisplayName()+" HAS FAILED TO SET INVENTORY SLOT AT ID: "+v2+"\n");
			return;
		}
		
		
//		UtilM.println(v2+"/"+(slots.length-1)+"\n");
//		this.slots[0]=stack;
		ItemStack[] stacks1=slots();
		stacks1[v2]=stack;
		//((UpgItem)Citem).setStacks(container, stacks1);
	}

	public ItemStack[] slots(){
//		return ((UpgItem)Citem).getStacks(container);
		return new ItemStack[0]; 
	}
	
}
