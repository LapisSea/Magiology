package com.magiology.forge.networking;

import com.magiology.util.statics.UtilC;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class PacketM implements IMessage, IMessageHandler<PacketM,IMessage>{
	
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
	public final IMessage onMessage(PacketM message, MessageContext ctx){
		IThreadListener listener;
		EntityPlayer player;
		boolean isRemote=ctx.side==Side.CLIENT;
		
		if(isRemote){
			listener=UtilC.getMC();
			player=UtilC.getThePlayer();
		}else{
			listener=((WorldServer)ctx.getServerHandler().player.world);
			player=ctx.getServerHandler().player;
		}
		
		listener.addScheduledTask(()->message.onMessage(player.world, player, isRemote));
		return null;
	}
	
}
