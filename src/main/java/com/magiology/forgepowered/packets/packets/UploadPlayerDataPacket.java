package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class UploadPlayerDataPacket extends AbstractToServerMessage{
	private boolean isFlap;
	private boolean[] keys;
	public UploadPlayerDataPacket(){}
	public UploadPlayerDataPacket(EntityPlayer player){
		ExtendedPlayerData.enshure(player);
		ExtendedPlayerData data=ExtendedPlayerData.get(player);
		this.isFlap=data.isFlappingDown;
		keys=data.keys.clone();
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		ExtendedPlayerData data=ExtendedPlayerData.get(player);
		if(data==null)data=ExtendedPlayerData.register(player);
		if(data!=null){
			data.isFlappingDown=isFlap;
			data.keys=keys.clone();
		}
		return null;
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		isFlap=buffer.readBoolean();
		keys=ArrayUtils.add(keys, buffer.readBoolean());
		keys=ArrayUtils.add(keys, buffer.readBoolean());
		keys=ArrayUtils.add(keys, buffer.readBoolean());
		keys=ArrayUtils.add(keys, buffer.readBoolean());
		keys=ArrayUtils.add(keys, buffer.readBoolean());
		keys=ArrayUtils.add(keys, buffer.readBoolean());
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeBoolean(isFlap);
		buffer.writeBoolean(keys[0]);
		buffer.writeBoolean(keys[1]);
		buffer.writeBoolean(keys[2]);
		buffer.writeBoolean(keys[3]);
		buffer.writeBoolean(keys[4]);
		buffer.writeBoolean(keys[5]);
	}

}
