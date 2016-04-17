package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class RightClickBlockPacket extends AbstractToServerMessage{
	
	private BlockPos pos;
	private byte side=-1;
	
	public RightClickBlockPacket(){}
	public RightClickBlockPacket(BlockPos pos,byte side){
		this.side=side;
		this.pos=pos;
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		IBlockState state=player.worldObj.getBlockState(pos);
		state.getBlock().onBlockActivated(player.worldObj, pos, state, player, EnumFacing.getFront(this.side), 0.5F, 0.5F, 0.5F);
		return null;
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		side=buffer.readByte();
		pos=readPos(buffer);
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		buffer.writeByte(side);
		writePos(buffer, pos);
	}

}
