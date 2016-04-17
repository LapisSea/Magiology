package com.magiology.api.connection;

import net.minecraft.util.EnumFacing;

public final class IConnectionFactory{
	public static IConnection New(IConnectionProvider host,ConnectionType type,EnumFacing side){
		return new Connection(host, type, side);
	}
	
	public static IConnection New(IConnectionProvider host,ConnectionType type,int side){
		return new Connection(host, type, side);
	}
	public static IConnection[] NewArray(IConnectionProvider host,ConnectionType type){
		IConnection[] result=new IConnection[6];
		for(int i=0;i<6;i++)result[i]=New(host, type, i);
		return result;
	}
	private IConnectionFactory(){}
}