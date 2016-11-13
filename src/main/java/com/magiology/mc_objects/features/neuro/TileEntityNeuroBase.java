package com.magiology.mc_objects.features.neuro;

import java.util.Collection;

import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityMTickable;
import com.magiology.util.statics.UtilM;


public abstract class TileEntityNeuroBase extends TileEntityMTickable implements NeuroPart{
	
	@Override
	public Collection<NeuroPart> getConnected(){
		return UtilM.getTileSides(getWorld(), new BlockPosM(getPos()), NeuroPart.class);
	}
	
	@Override
	public void onConnect(){}

	@Override
	public void onDisconnect(){}
	
}
