package com.magiology.util.m_extensions;

import com.magiology.forge.events.TickEvents;
import com.magiology.util.interf.Locateable.LocateableBlockM;
import com.magiology.util.interf.Worldabale;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileEntityM extends TileEntity implements Worldabale,LocateableBlockM{
	
	public static final float p=1F/16F;
	
	private boolean		nbtLoaded	=false;
	private Runnable worldSetHook;
	
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
		return world.getTotalWorldTime();
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet){
		readFromNBT(packet.getNbtCompound());
	}
	
	public void sync(){
		if(world==null||client())return;
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
		return hasWorld()&&world.getTileEntity(getPos())==this;
	}

	public IBlockState getState(){
		return getWorld().getBlockState(getPos());
	}
	public void setState(IBlockState newState){
		getWorld().setBlockState(getPos(), newState);
	}
	
	@Override
	public void setWorld(World worldIn){
		super.setWorld(worldIn);
		if(worldSetHook!=null){
			TickEvents.nextTick(worldIn.isRemote, worldSetHook);
			worldSetHook=null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		nbtLoaded=true;
		super.readFromNBT(compound);
		
		if(nbtUsingWorld(compound)){
			if(hasWorld())readFromNbtWithWorld(compound);
			else{
				nbtLoaded=false;
				worldSetHook=()->{
					nbtLoaded=true;
					readFromNbtWithWorld(compound);
				};
			}
		}
	}

	protected void readFromNbtWithWorld(NBTTagCompound compound){}
	protected boolean nbtUsingWorld(NBTTagCompound compound){
		return false;
	}
	
	public boolean isNbtLoaded(){
		return nbtLoaded;
	}
	@Deprecated
	public void markNbtAsLoaded(){
		nbtLoaded=true;
	}
	
	@Override
	public BlockPosM getPos(){
		BlockPosM p=BlockPosM.conv(pos);
		pos=p;
		return p;
	}
}
