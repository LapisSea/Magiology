package com.magiology.forgepowered.packets.packets.toserver;

import java.io.IOException;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class UploadPlayerDataPacket extends AbstractToServerMessage{
	private int energy,maxEnergy;
	
	public UploadPlayerDataPacket(){}
	public UploadPlayerDataPacket(EntityPlayer player){
		energy=ExtendedPlayerData.getEnergy(player);
		maxEnergy=ExtendedPlayerData.getMaxEnergy(player);
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(energy);
		buffer.writeInt(maxEnergy);
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		energy=buffer.readInt();
		maxEnergy=buffer.readInt();
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		ExtendedPlayerData.setEnergy(player, energy);
		ExtendedPlayerData.setMaxEnergy(player, maxEnergy);
		return null;
	}

}
