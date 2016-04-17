package com.magiology.api.connection;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IConnectionProvider{
	public IConnection[] getConnections();
	public TileEntity getHost();
	public boolean isStrate(EnumFacing facing);
}
