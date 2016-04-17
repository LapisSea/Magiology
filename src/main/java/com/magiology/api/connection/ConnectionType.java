package com.magiology.api.connection;

public enum ConnectionType{
	Energy(2),Fluid(1),Item(0),Space(3);
	public int id;
	private ConnectionType(int id){
	}
}