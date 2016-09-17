package com.magiology.forge_powered.networking;

import com.magiology.util.statics.UtilC;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class PacketM<T extends IMessage> implements IMessage, IMessageHandler<T, IMessage>{
	
	public abstract void toBytes(PacketBufferM buf);
	
	public abstract void fromBytes(PacketBufferM buf);
	
	public abstract IMessage onMessage(EntityPlayer player, boolean isRemote);
	
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
			return onMessage(UtilC.getThePlayer(), true);
		}
		
		EntityPlayerMP player=ctx.getServerHandler().playerEntity;
		((WorldServer)player.worldObj).addScheduledTask(()->Packets.sendTo(onMessage(ctx.getServerHandler().playerEntity, false), player));
		
		return null;
	}
}
