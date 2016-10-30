package com.magiology.util.m_extensions;

import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityM extends TileEntity{
	public static final float p=1F/16F;
	
	private boolean nbtLoaded=false;
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(pos, 100, getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag(){
		return this.writeToNBT(new NBTTagCompound());
	}
    
	
	public long getTime(){
		return worldObj.getTotalWorldTime();
	}
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet){
		readFromNBT(packet.getNbtCompound());
	}
	public void sync(){
		if(worldObj==null||worldObj.isRemote)return;
		markDirty();
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
	public boolean isValid(){
		return worldObj!=null&&worldObj.getTileEntity(getPos())==this;
	}
	
	public IBlockState getState(){
		return getWorld().getBlockState(getPos());
	}
	@Override
	public void readFromNBT(NBTTagCompound compound){
		nbtLoaded=true;
		super.readFromNBT(compound);
	}
	public boolean isNbtLoaded(){
		return nbtLoaded;
	}
}
