package com.magiology.forgepowered.packets.packets.generic;

import java.io.IOException;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.handlers.GenericPacketEventHandler;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class GenericServerIntPacket extends AbstractToServerMessage{
	
	private int data,eventId;
	
	
	public GenericServerIntPacket(){}
	public GenericServerIntPacket(int eventId,int data){
		this.data=data;
		this.eventId=eventId;
		GenericPacketEventHandler.addNewIntegerPacketEvent(eventId, data, U.getMC().thePlayer,Side.CLIENT);
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		GenericPacketEventHandler.addNewIntegerPacketEvent(eventId, data, player,Side.SERVER);
		return null;
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		data=buffer.readInt();
		eventId=buffer.readInt();
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		buffer.writeInt(data);
		buffer.writeInt(eventId);
	}

}
