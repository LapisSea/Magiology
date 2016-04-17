package com.magiology.api.network;

import java.util.List;

import com.magiology.api.lang.ICommandInteract;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;

public interface WorldNetworkInterface extends BasicWorldNetworkInterface{
	public TileEntityNetworkController getBrain();
	public long getCard();
	public List<ICommandInteract> getCommandInteractors();
	public NetworkInterface getConnectedInterface();
	public void onNetworkActionInvoked(NetworkInterface Interface,String action,Object... data);
}
