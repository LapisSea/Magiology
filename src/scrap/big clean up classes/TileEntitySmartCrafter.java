package com.magiology.mcobjects.tileentityes;


import com.magiology.client.gui.gui.CraftingGridWOutput;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

public class TileEntitySmartCrafter extends TileEntityM implements ISidedInventory,ITickable{
	
	public boolean isActive=false;
	public int rotation=-1;
	boolean isSideInverted=false;
	public TileEntity tile1=null,tile2=null;
	public CraftingGridWOutput[] wantedProducts=new CraftingGridWOutput[50];
	@Override
	public void update(){
		
		isActive=isActive();
		if(!isActive){
			tile1=null;
			tile2=null;
		}
//		if(isActive)Helper.spawnEntityFX(new EntityFlameFX(worldObj, xCoord+0.5, yCoord+0.5, zCoord+0.5, 0, 0, 0));
		for(int a=0;a<wantedProducts.length;a++)if(wantedProducts[a]==null)wantedProducts[a]=new CraftingGridWOutput(null, 0, null);
//		Helper.println(wantedProducts[1].product[0]);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		rotation=NBT.getInteger("rotation");
		for(int a=0;a<wantedProducts.length;a++)if(wantedProducts[a]==null)wantedProducts[a]=new CraftingGridWOutput(null, 0, null);
		for(int id=0;id<wantedProducts.length;id++){
			CraftingGridWOutput a=wantedProducts[id];
			if(a!=null){
				a.product=UtilM.loadItemsFromNBT(NBT, "WP"+id,a.product);
				a.grid=UtilM.loadItemsFromNBT(NBT, "grid"+id, a.grid);
				a.ammountWanted=NBT.getInteger("AW"+id);
			}
		}
		
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		NBT.setInteger("rotation", rotation);
		for(int a=0;a<wantedProducts.length;a++)if(wantedProducts[a]==null)wantedProducts[a]=new CraftingGridWOutput(null, 0, null);
		for(int id=0;id<wantedProducts.length;id++){
			CraftingGridWOutput a=wantedProducts[id];
			if(a!=null){
				UtilM.saveItemsToNBT(NBT, "WP"+id, a.product);
				UtilM.saveItemsToNBT(NBT, "grid"+id, a.grid);
				NBT.setInteger("AW"+id, a.ammountWanted);
			}
		}
		
	}
	
	public boolean isActive(){
		switch(rotation){
		case 0:{
			tile1=worldObj.getTileEntity(pos.add(0,0,-1));
			tile2=worldObj.getTileEntity(pos.add(0,0,1));
		}break;
		case 1:{
			tile1=worldObj.getTileEntity(pos.add(-1,0,0));
			tile2=worldObj.getTileEntity(pos.add(1,0,0));
		}break;
		case 2:{
			tile1=worldObj.getTileEntity(pos.add(0,-1,0));
			tile2=worldObj.getTileEntity(pos.add(0,1,0));
		}break;
		}
		if(UtilM.isNull(tile1,tile2))return false;
		if(tile1 instanceof IInventory&&tile2 instanceof IInventory);else return false;
		
		return true;
	}
	

	@Override
	public int getSizeInventory(){
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int v1){
		return null;
	}

	@Override
	public ItemStack decrStackSize(int v1, int v2){
		
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int v1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int v1, ItemStack stack){
	}

	@Override
	public String getName(){
		
		return "SmartCrafterInventory";
	}

	@Override
	public boolean hasCustomName(){
		
		return true;
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}
	
	public void setInventoryName(String string){
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		
	}

	@Override
	public boolean isItemValidForSlot(int v1, ItemStack stack) {
		boolean reurn1=true;
//		reurn1=GameRegistry.getFuelValue(stack)>0;
		
		return reurn1;
	}

	@Override
	public int getField(int id){
		return 0;
	}

	@Override
	public void setField(int id, int value){
		
	}

	@Override
	public int getFieldCount(){
		return 0;
	}

	@Override
	public void clear(){
		
	}

	@Override
	public IChatComponent getDisplayName(){
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side){
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,EnumFacing direction){
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,EnumFacing direction) {
		return false;
	}
}
