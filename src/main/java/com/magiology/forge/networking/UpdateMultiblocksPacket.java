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
				if(tiles==null)toSend.put(player,tiles=new ArrayList<>());
				tiles.add(tile);
			});
		});
		dirty.clear();
		toSend.forEach((player,tiles)->tiles.forEach(t->Packets.sendTo(new UpdateMultiblocksPacket(t),(EntityPlayerMP)player)));
	}
	
	public static void markForSync(TileEntityOneBlockStructure tile){
		dirty.add(tile);
	}
	
	public UpdateMultiblocksPacket(){
	}
	
	public UpdateMultiblocksPacket(TileEntityOneBlockStructure tile){
		nbt=new NBTTagCompound();
		tile.writeToNBT(nbt);
	}
	
	
	@Override
	public IMessage onMessage(World world,EntityPlayer player,boolean isRemote){
		
		BlockPosM pos=new BlockPosM(nbt.getInteger("x"),nbt.getInteger("y"),nbt.getInteger("z"));
		if(!world.isBlockLoaded(pos))return null;
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityOneBlockStructure){
			tile.readFromNBT(nbt);
		}
		return null;
	}
}
