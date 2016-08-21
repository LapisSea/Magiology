package com.magiology.util.m_extensions;

import com.magiology.util.statics.UtilM;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityM extends TileEntity{
	public static final float p=1F/16F;
	
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(pos, 69/*no I am not 12*/, getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag(){
		return this.writeToNBT(new NBTTagCompound());
	}
    
	@Override
	public BlockPosM getPos(){
		return new BlockPosM(super.getPos());
	}
	
	public long getTime(){
		return worldObj.getTotalWorldTime();
	}
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet){
		readFromNBT(packet.getNbtCompound());
	}
	public int[] read3I(NBTTagCompound nbt,String name){
		return new int[]{
			nbt.getInteger(name+"X"),
			nbt.getInteger(name+"Y"),
			nbt.getInteger(name+"Z")
		};
	}
	public BlockPos readPos(NBTTagCompound nbt,String name){
		return new BlockPos(nbt.getInteger(name+"X"), nbt.getInteger(name+"Y"), nbt.getInteger(name+"Z"));
	}
	public void sync(){
		if(worldObj==null||worldObj.isRemote)return;
		markDirty();
	}
	public void write3I(NBTTagCompound nbt,int x,int y,int z,String name){
		nbt.setInteger(name+"X", pos.getX());
		nbt.setInteger(name+"Y", pos.getY());
		nbt.setInteger(name+"Z", pos.getZ());
	}
	public void writePos(NBTTagCompound nbt,BlockPos pos,String name){
		nbt.setInteger(name+"X", pos.getX());
		nbt.setInteger(name+"Y", pos.getY());
		nbt.setInteger(name+"Z", pos.getZ());
	}
	public int x(){
		return pos.getX();
	}
	public int y(){
		return pos.getY();
	}
	public int z(){
		return pos.getZ();
	}

	public boolean isRemote(){
		return UtilM.isRemote(this);
	}
	public long worldTime(){
		return UtilM.worldTime(this);
	}
}
