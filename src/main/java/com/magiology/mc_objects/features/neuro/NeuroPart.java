package com.magiology.mc_objects.features.neuro;

import java.util.Collection;

public interface NeuroPart{

	TileEntityNeuroController getController();
	void setController(TileEntityNeuroController controller);
	
	Collection<NeuroPart> getConnected();
	
	void onConnect();
	void onDisconnect();
}
