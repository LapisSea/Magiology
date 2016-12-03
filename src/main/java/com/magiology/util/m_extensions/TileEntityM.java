package com.magiology.util.m_extensions;

import com.magiology.forge.events.TickEvents;
import com.magiology.util.interf.Worldabale;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityM extends TileEntity implements Worldabale{
	
	public static final float p=1F/16F;
	
	private boolean		nbtLoaded	=false;
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound nbt=getUpdateTag();
		return nbt.getSize()<=4?null:new SPacketUpdateTileEntity(pos, 100, nbt);
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
		if(worldObj==null||worldObj.isRemote) return;
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
	
	
	public boolean isInWorld(){
		return worldObj!=null&&worldObj.getTileEntity(getPos())==this;
	}

	public IBlockState getState(){
		return getWorld().getBlockState(getPos());
	}
	public void setState(IBlockState newState){
		getWorld().setBlockState(getPos(), newState);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		nbtLoaded=true;
		super.readFromNBT(compound);
		
		if(getWorld()!=null) readFromNbtWithWorld(compound);
		else TickEvents.nextTick(()->readFromNbtWithWorld(compound));
	}
	
	protected void readFromNbtWithWorld(NBTTagCompound compound){}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		return super.writeToNBT(compound);
	}
	
	public boolean isNbtLoaded(){
		return nbtLoaded;
	}
}
