package com.magiology.util.interf;

public interface ISidedConnection{
	
	boolean canConnect(ISidedConnection o);
	
	public static boolean handshake(ISidedConnection o1, ISidedConnection o2){
		return o1!=null&&o2!=null&&o1.canConnect(o2)&&o2.canConnect(o1);
	}
	
}
