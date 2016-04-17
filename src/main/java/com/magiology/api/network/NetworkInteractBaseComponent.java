package com.magiology.api.network;


public interface NetworkInteractBaseComponent extends ISidedNetworkComponent{
	
	public NetworkInterface getInterface();
	public boolean hasAnInterface();
	public boolean isActive();
	public void update();
}
