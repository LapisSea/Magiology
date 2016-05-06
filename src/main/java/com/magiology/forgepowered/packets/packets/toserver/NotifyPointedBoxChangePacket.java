package com.magiology.forgepowered.packets.packets.toserver;

import java.io.IOException;
import java.util.List;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class NotifyPointedBoxChangePacket extends AbstractToServerMessage{
	private List<Integer> ghostHits;
	private int id;
	BlockPos pos;
	public NotifyPointedBoxChangePacket(){}
	public<T extends TileEntity&MultiColisionProvider> NotifyPointedBoxChangePacket(T tile){
		pos=tile.getPos();
		id=tile.getPointedBoxID();
		ghostHits=tile.getGhostHits();
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		TileEntity tile=player.worldObj.getTileEntity(pos);
		if(tile instanceof MultiColisionProvider){
			((MultiColisionProvider)tile).setPointedBoxID(id);
			((MultiColisionProvider)tile).setGhostHits(ghostHits);
		}
		return null;
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		pos=readPos(buffer);
		id=buffer.readInt();
		ghostHits=readIntList(buffer);
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		writePos(buffer, pos);
		buffer.writeInt(id);
		writeIntList(buffer, ghostHits);
	}

}
