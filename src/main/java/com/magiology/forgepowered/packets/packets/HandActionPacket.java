package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class HandActionPacket extends AbstractToServerMessage{
	
	private int id;
	private Vec3M pos;
	
	public HandActionPacket(){}
	public HandActionPacket(int id, Vec3M pos){
		this.id=id;
		this.pos=pos;
	}

	@Override
	public IMessage process(EntityPlayer player,Side side){
		switch(id){
		case 0:TheHandHandler.shoot(player,pos);break;
		}
		return null;
	}

	@Override
	public void read(PacketBuffer buffer)throws IOException{
		id=buffer.readInt();
		pos=readVec3M(buffer);
	}

	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(id);
		writeVec3M(buffer,pos);
	}

}
