package com.magiology.forge.networking;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * @author LapisSea
 * 
 * @description wrapper for easy debugging + nice place for registration
 *
 */
public class SimpleNetworkWrapperM extends SimpleNetworkWrapper{
	
	private int registrationId=0;
	
	public SimpleNetworkWrapperM(String channelName){
		super(channelName);
		
	}
	
	public <T extends IMessage&IMessageHandler<T, IMessage>> void registerPacket(Class<T> clazz, Side receiverSide){
		registerMessage(clazz, clazz, registrationId, receiverSide);
		registrationId++;
	}
}
