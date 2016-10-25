package com.magiology.forge.networking;

import java.io.IOException;

import com.magiology.util.statics.UtilM;

import net.minecraft.nbt.NBTTagCompound;

public abstract class NBTPacket extends PacketM{
	
	protected NBTTagCompound nbt;
	
	public NBTPacket(){}
	public NBTPacket(NBTTagCompound nbt){
		this.nbt=nbt;
	}
	
	@Override
	public void fromBytes(PacketBufferM buf){
		try{
			nbt=buf.readNBTTagCompoundFromBuffer();
		}catch(IOException e){
			e.printStackTrace();
			UtilM.exit(420);
		}
	}

	@Override
	public void toBytes(PacketBufferM buf){
		buf.writeNBTTagCompoundToBuffer(nbt);
	}
	
}
