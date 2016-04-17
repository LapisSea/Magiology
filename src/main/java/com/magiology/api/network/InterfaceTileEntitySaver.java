package com.magiology.api.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public interface InterfaceTileEntitySaver{
	public TileEntity getBoundTile();
	public void readFromNBT(NBTTagCompound NBT);
	public void setBoundTile(TileEntity tile);
	public void writeToNBT(NBTTagCompound NBT);
}
