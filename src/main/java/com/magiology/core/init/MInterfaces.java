package com.magiology.core.init;

import com.magiology.api.network.interfaces.registration.InterfaceRegistration;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.interfaces.TileHologramProjectorInterface;

public class MInterfaces{
	public static void init(){
		InterfaceRegistration.registerInterfaceToTileEntity(new TileHologramProjectorInterface(), TileEntityHologramProjector.class);
	}
}
