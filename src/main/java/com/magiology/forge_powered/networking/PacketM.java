package com.magiology.forge_powered.networking;

import com.magiology.util.statics.UtilC;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class PacketM<T extends IMessage> implements IMessage,IMessageHandler<T, IMessage>{
	
	public abstract IMessage onMessage(boolean isRemote, EntityPlayer player);
	
	public abstract void fromBytes(PacketBuffer buf);
	public abstract void toBytes(PacketBuffer buf);
	
	
	@Override
	public final void fromBytes(ByteBuf buf){
		fromBytes(new PacketBufferM(buf));
	}
	
	@Override
	public final void toBytes(ByteBuf buf){
		toBytes(new PacketBufferM(buf));
	}
	
	@Override
	public final IMessage onMessage(IMessage message, MessageContext ctx){
		
		if(ctx.side==Side.CLIENT)return onMessage(true, UtilC.getThePlayer());
		
		EntityPlayerMP player=ctx.getServerHandler().playerEntity;
		((WorldServer)player.worldObj).addScheduledTask(()->Packets.sendTo(onMessage(false, ctx.getServerHandler().playerEntity), player));
		
		return null;
	}
}
