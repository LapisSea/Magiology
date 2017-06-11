package com.magiology.forge.networking;

import com.magiology.util.statics.UtilM;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public abstract class NBTPacket extends PacketM{
	
	protected NBTTagCompound nbt;
	
	public NBTPacket(){}
	
	public NBTPacket(NBTTagCompound nbt){
		this.nbt=nbt;
	}
	
	@Override
	public void fromBytes(PacketBufferM buf){
		try{
			nbt=buf.readCompoundTag();
		}catch(IOException e){
			e.printStackTrace();
			UtilM.exit(420);
		}
	}
	
	@Override
	public void toBytes(PacketBufferM buf){
		buf.writeCompoundTag(nbt);
	}
	
}
