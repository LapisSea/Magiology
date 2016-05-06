package com.magiology.forgepowered.packets.packets.toclient;

import java.io.IOException;

import com.magiology.forgepowered.packets.core.AbstractToClientMessage;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateClientPlayerDataPacket extends AbstractToClientMessage{
	private int now,max;
	public UpdateClientPlayerDataPacket(){}
	public UpdateClientPlayerDataPacket(EntityPlayer target){
		super(new SendingTarget(target.worldObj, target));
		now=ExtendedPlayerData.getEnergy(target);
		max=ExtendedPlayerData.getMaxEnergy(target);
	}

	@Override
	public void read(PacketBuffer buffer)throws IOException{
		now=buffer.readInt();
		max=buffer.readInt();
	}

	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(now);
		buffer.writeInt(max);
	}
	
	@Override
	public IMessage process(EntityPlayer player, Side side){
		ExtendedPlayerData.setEnergy(player, now);
		ExtendedPlayerData.setMaxEnergy(player, max);
		return null;
		
	}
}
