package com.magiology.forge.networking;

import com.magiology.util.statics.UtilC;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public abstract class PacketM<T extends IMessage> implements IMessage, IMessageHandler<T, IMessage>{
	
	public abstract void toBytes(PacketBufferM buf);
	
	public abstract void fromBytes(PacketBufferM buf);
	
	public abstract IMessage onMessage(World world, EntityPlayer player, boolean isRemote);
	
	@Override
	public final void fromBytes(ByteBuf buf){
		fromBytes(new PacketBufferM(buf));
	}
	
	@Override
	public final void toBytes(ByteBuf buf){
		toBytes(new PacketBufferM(buf));
	}
	
	@Override
	public final IMessage onMessage(T message, MessageContext ctx){
		
		if(ctx.side==Side.CLIENT){
			EntityPlayer player=UtilC.getThePlayer();
			return ((PacketM)message).onMessage(player.worldObj, player, true);
		}
		
		EntityPlayerMP player=ctx.getServerHandler().playerEntity;
		((WorldServer)player.worldObj).addScheduledTask(()->Packets.sendTo(((PacketM)message).onMessage(ctx.getServerHandler().playerEntity.worldObj,ctx.getServerHandler().playerEntity, false), player));
		
		return null;
	}
}
