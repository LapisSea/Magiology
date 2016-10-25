package com.magiology.forge.networking;

import java.util.*;

import com.magiology.handlers.TileEntityOneBlockStructure;
import com.magiology.util.m_extensions.BlockPosM;

import net.minecraft.entity.player.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateMultiblocksPacket extends NBTPacket{
	
	private static Set<TileEntityOneBlockStructure> dirty=new HashSet<>();
	
	public static void upload(){
		if(dirty.isEmpty())return;
		
		Map<EntityPlayer,List<TileEntityOneBlockStructure>> toSend=new HashMap<>();
		dirty.forEach((tile)->{
			tile.getWorld().playerEntities.forEach(player->{
				List<TileEntityOneBlockStructure> tiles=toSend.get(player);
				if(tiles==null)
					toSend.put(player,tiles=new ArrayList<>());
				tiles.add(tile);
			});
		});
		dirty.clear();
		toSend.forEach((player,tiles)->Packets.sendTo(new UpdateMultiblocksPacket(tiles),(EntityPlayerMP)player));
	}
	
	public static void markForSync(TileEntityOneBlockStructure tile){
//		tile.sync();
		dirty.add(tile);
	}
	
//	private List<BlockPos>		positions	=new ArrayList<>(),brains=new ArrayList<>();
//	private static final long	NULL_POS	=Long.MAX_VALUE;
	
	public UpdateMultiblocksPacket(){
	}
	
	public UpdateMultiblocksPacket(Collection<? extends TileEntityOneBlockStructure> blocks){
		nbt=new NBTTagCompound();
		nbt.setInteger("count",blocks.size());
		int id=0;
		for(TileEntityOneBlockStructure tile:blocks){
			NBTTagCompound nbt=new NBTTagCompound();
			tile.writeToNBT(nbt);
			this.nbt.setTag(String.valueOf(id++),nbt);
		}
	}
	
//	@Override
//	public void toBytes(PacketBufferM buf){
//		long[] data=new long[positions.size()*2];
//		buf.writeInt(data.length);
//		for(int i=0;i<positions.size();i++){
//			data[i*2]=positions.get(i).toLong();
//			BlockPos brain=brains.get(i);
//			data[i*2+1]=brain==null?NULL_POS:brain.toLong();
//		}
//		buf.writeLongArray(data);
//	}
//	
//	@Override
//	public void fromBytes(PacketBufferM buf){
//		long[] data=buf.readLongArray(new long[buf.readInt()*2]);
//		
//		for(int i=0;i<data.length;i++){
//			(i%2==0?positions:brains).add(data[i]==NULL_POS?null:BlockPos.fromLong(data[i]));
//		}
//	}
	
	@Override
	public IMessage onMessage(World world,EntityPlayer player,boolean isRemote){
//		for(int i=0;i<positions.size();i++){
//			BlockPos tilePos=positions.get(i),brainPos=brains.get(i);
//			boolean brainNull=brainPos==null;
//			if(!world.isBlockLoaded(tilePos)||(!brainNull&&!world.isBlockLoaded(brainPos)))continue;
//			
//			TileEntity tile=world.getTileEntity(tilePos);
//			if(tile instanceof TileEntityOneBlockStructure){
//				TileEntityOneBlockStructure t=((TileEntityOneBlockStructure)tile);
//				if(brainNull)t.setBrain(null);
//				else{
//					TileEntity brain=world.getTileEntity(brainPos);
//					if(brain instanceof TileEntityOneBlockStructure)t.setBrain((TileEntityOneBlockStructure)brain);
//				}
//			}
//		}
		for(int i=0,j=nbt.getInteger("count");i<j;i++){
			NBTTagCompound nbt=this.nbt.getCompoundTag(String.valueOf(i));
			BlockPosM pos=new BlockPosM(nbt.getInteger("x"),nbt.getInteger("y"),nbt.getInteger("z"));
//			ParticleBubbleFactory.get().spawn(new Vec3M(pos).add(0.5),new Vec3M(),0.2F,20,0,ColorF.RED);
			if(!world.isBlockLoaded(pos))continue;
			TileEntity tile=world.getTileEntity(pos);//.getTile(world,TileEntityOneBlockStructure.class);
			if(tile instanceof TileEntityOneBlockStructure){
				tile.readFromNBT(nbt);
			}
		}
		return null;
	}
}
