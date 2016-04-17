package com.magiology.api.network;

import net.minecraft.tileentity.TileEntity;

public interface ISidedNetworkComponent extends NetworkBaseComponent{
	public class ISidedNetworkComponentHandler extends NetworkBaseComponentHandler{
		
	}
	public boolean getAccessibleOnSide(int side);
	public TileEntity getHost();
	public int  getOrientation();
	public boolean isSideEnabled(int side);
	public void setAccessibleOnSide(int side,boolean accessible);
	
	public void setOrientation(int orientation);

}
