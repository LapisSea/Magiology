package com.magiology.util.m_extensions;

import java.util.HashMap;
import java.util.Map;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.google.common.base.Strings;
import com.magiology.forge.events.TickEvents;
import com.magiology.util.interf.Locateable.LocateableBlockM;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilM;
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
	private Runnable	worldSetHook;
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound nbt=getUpdateTag();
		return nbt.getSize()<=4?null:new SPacketUpdateTileEntity(pos, 100, nbt);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag){
		super.handleUpdateTag(tag);
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
		if(world==null||client()) return;
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
		worldNbt(false);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		worldNbt(true);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		nbtLoaded=true;
		super.readFromNBT(compound);
		
		if(nbtUsingWorld(compound)){
			if(hasWorld()) readFromNbtWithWorld(compound);
			else{
				nbtLoaded=false;
				worldSetHook=()->{
					nbtLoaded=true;
					readFromNbtWithWorld(compound);
				};
			}
		}
	}
	
	private final void worldNbt(boolean now){
		if(worldSetHook==null||!hasWorld()) return;
		
		if(now) worldSetHook.run();
		else TickEvents.nextTick(isRemote(), worldSetHook);
		
		worldSetHook=null;
		
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
	
	protected void addToStringData(Map<String,String> data){
		
		StringBuilder hash=new StringBuilder(Integer.toHexString(hashCode()));
		while(hash.length()<8){
			hash.insert(0, '0');
		}
		
		data.put("at", x()+","+y()+","+z());
		data.put("HS", hash.toString());
		
		if(!isNbtLoaded())data.put("NBT!", null);
		if(!hasWorld())data.put("World!", null);
	}
	
	@Override
	public String toString(){
		StringBuilder result=new StringBuilder(UtilM.removeMcObjectEnd(getClass().getSimpleName())+"[");
		
		Map<String,String> data=new HashMap<>();
		addToStringData(data);
		data.forEach((key,value)->{
			
			if(Strings.isNullOrEmpty(value)){
				if(Strings.isNullOrEmpty(key))return;
				result.append(' ').append(key);
				return;
			}
			
			char c=result.charAt(result.length()-1);
			if(c!='['&&!Character.isWhitespace(c))result.append(' ');
			result.append(key).append(": ");
			
			if(value.contains(" "))result.append('"').append(value).append('"');
			else result.append(value);
			
		});
		
		result.append("]");
		return result.toString();
	}
}
