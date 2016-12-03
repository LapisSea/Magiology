package com.magiology.mc_objects.features.neuro;

import java.util.Collection;

public interface NeuroInterface<T extends NeuroInterface> extends NeuroPart{
	
	T getInterfaceCore();
	
	Collection<T> getWholeInterface();
	
	@Override
	default NeuroPart getSelf(){
		return getInterfaceCore();
	}
}
