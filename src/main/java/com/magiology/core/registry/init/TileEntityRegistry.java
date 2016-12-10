package com.magiology.core.registry.init;

import com.magiology.core.registry.imp.AutoRegistry;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.statics.UtilM;

import net.minecraftforge.fml.common.registry.GameRegistry;
//<GEN:	IMPORTS START>
import com.magiology.mc_objects.features.dimension_stabiliser.TileEntityDimensionStabiliser;
import com.magiology.mc_objects.features.machines.shaker.TileEntityShaker;
import com.magiology.mc_objects.features.neuro.TileEntityNeuroController;
import com.magiology.mc_objects.features.neuro.TileEntityNeuroDuct;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
//<GEN:	IMPORTS END>

public class TileEntityRegistry extends AutoRegistry<TileEntityM>{

	private static final TileEntityRegistry instance=new TileEntityRegistry();
	public static TileEntityRegistry get(){return instance;}
	
	
	private TileEntityRegistry(){
		super(TileEntityM.class);
	}
	


	@Override
	public void registerObj(Class<TileEntityM> clazz){
		GameRegistry.registerTileEntity(clazz, "te_"+UtilM.classNameToMcName(clazz));
	}
	
	@Override
	protected void init(){
		//<GEN:	INIT START>
		add(TileEntityDimensionStabiliser.class);
		add(TileEntityShaker.class);
		add(TileEntityNeuroController.class);
		add(TileEntityNeuroDuct.class);
		add(TileEntityScreen.class);
		//<GEN:	INIT END>
	}
	
}
