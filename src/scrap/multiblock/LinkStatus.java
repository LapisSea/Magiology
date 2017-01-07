package com.magiology.mc_objects.tile.multiblock;

public enum LinkStatus{
	LOADED(true), UNLOADED(false), WAITING_FOR_RESPONSE(false), UNKNOWN(false);
	
	public final boolean loaded;
	
	private LinkStatus(boolean loaded){
		this.loaded=loaded;
	}
}