package com.magiology.forgepowered.packets.packets.toserver;

import java.io.IOException;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.NBTUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class SendWingModePacket extends AbstractToServerMessage{
	
	private int modeId;
	public SendWingModePacket(){}
	public SendWingModePacket(int modeId){
		this.modeId=modeId;
	}
	
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		buffer.writeInt(modeId);
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		modeId=buffer.readInt();
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		ItemStack wings=player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if(wings!=null)NBTUtil.setInt(wings, "mode", modeId);
		else PrintUtil.printlnEr("SendWingMode packet could not process data!");
		return null;
	}

}
