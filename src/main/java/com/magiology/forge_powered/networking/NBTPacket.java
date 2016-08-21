package com.magiology.forge_powered.networking;

import java.io.IOException;

import com.magiology.util.statics.UtilM;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public abstract class NBTPacket extends PacketM{
	
	protected NBTTagCompound nbt;
	
	public NBTPacket(){}
	public NBTPacket(NBTTagCompound nbt){
		this.nbt=nbt;
	}
	
	@Override
	public void fromBytes(PacketBuffer buf){
		try{
			nbt=buf.readNBTTagCompoundFromBuffer();
		}catch(IOException e){
			e.printStackTrace();
			UtilM.exit(42);
		}
	}

	@Override
	public void toBytes(PacketBuffer buf){
		buf.writeNBTTagCompoundToBuffer(nbt);
	}
	
}
