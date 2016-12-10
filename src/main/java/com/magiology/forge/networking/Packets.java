package com.magiology.forge.networking;

import com.magiology.core.Magiology;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class Packets{
	
	public static void sendTo(IMessage packet, EntityPlayerMP player){
		Magiology.NETWORK_CHANNEL.sendTo(packet, player);
	}
	
	public static void sendToAll(IMessage packet){
		Magiology.NETWORK_CHANNEL.sendToAll(packet);
	}
	
	public static void sendToAllAround(IMessage packet, int dimension, double x, double y, double z, double range){
		sendToAllAround(packet, new TargetPoint(dimension, x, y, z, range));
	}
	
	public static void sendToAllAround(IMessage packet, TargetPoint point){
		Magiology.NETWORK_CHANNEL.sendToAllAround(packet, point);
	}
	
	public static void sendToDimension(IMessage packet, World world){
		sendToDimension(packet, world.provider.getDimension());
	}
	
	public static void sendToDimension(IMessage packet, int dimensionId){
		Magiology.NETWORK_CHANNEL.sendToDimension(packet, dimensionId);
	}
	
	public static void sendToServer(IMessage packet){
		Magiology.NETWORK_CHANNEL.sendToServer(packet);
	}
	
}
