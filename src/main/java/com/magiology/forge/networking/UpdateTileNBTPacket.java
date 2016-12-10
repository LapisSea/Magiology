package com.magiology.forge.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateTileNBTPacket extends NBTPacket{
	
	public static final Side SIDE=Side.CLIENT;
	
	private static Set<TileEntityM> dirty=new HashSet<>();
	
	
	public static void upload(){
		if(dirty.isEmpty())return;
		
		Map<EntityPlayer, List<TileEntityM>> toSend=new HashMap<>();
		dirty.forEach((tile)->{
			tile.getWorld().playerEntities.forEach(player->{
				List<TileEntityM> tiles=toSend.get(player);
				if(tiles==null) toSend.put(player, tiles=new ArrayList<>());
				tiles.add(tile);
			});
		});
		dirty.clear();
		toSend.forEach((player, tiles)->tiles.forEach(t->Packets.sendTo(new UpdateTileNBTPacket(t), (EntityPlayerMP)player)));
	}
	
	public static void markForSync(TileEntityM tile){
		if(!tile.isRemote())dirty.add(tile);
	}
	
	public UpdateTileNBTPacket(){}
	
	public UpdateTileNBTPacket(TileEntityM tile){
		nbt=new NBTTagCompound();
		tile.writeToNBT(nbt);
	}
	
	@Override
	public IMessage onMessage(World world, EntityPlayer player, boolean isRemote){
		
		BlockPosM pos=new BlockPosM(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
		if(!world.isBlockLoaded(pos)) return null;
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityM){
			tile.readFromNBT(nbt);
		}
		return null;
	}

}
