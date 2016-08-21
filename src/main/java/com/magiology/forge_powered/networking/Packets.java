package com.magiology.forge_powered.networking;

import com.magiology.core.Magiology;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Packets{

	@SideOnly(Side.SERVER)
	public static void sendTo(IMessage packet, EntityPlayerMP player){
		Magiology.NETWORK_CHANNEL.sendTo(packet, player);
	}
	
	@SideOnly(Side.SERVER)
	public static void sendToAll(IMessage packet){
		Magiology.NETWORK_CHANNEL.sendToAll(packet);
	}
	
	@SideOnly(Side.SERVER)
	public static void sendToAllAround(IMessage packet, int dimension, double x, double y, double z, double range){
		sendToAllAround(packet, new TargetPoint(dimension, x, y, z, range));
	}
	
	@SideOnly(Side.SERVER)
	public static void sendToAllAround(IMessage packet, TargetPoint point){
		Magiology.NETWORK_CHANNEL.sendToAllAround(packet, point);
	}
	
	@SideOnly(Side.SERVER)
	public static void sendToDimension(IMessage packet, int dimensionId){
		Magiology.NETWORK_CHANNEL.sendToDimension(packet, dimensionId);
	}
	
	@SideOnly(Side.CLIENT)
	public static void sendToServer(IMessage packet){
		Magiology.NETWORK_CHANNEL.sendToServer(packet);
	}
	
}
