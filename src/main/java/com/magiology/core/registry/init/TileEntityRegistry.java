package com.magiology.core.registry.init;

import java.util.HashMap;
import java.util.Map;

import com.magiology.core.registry.imp.AutoRegistry;
//<GEN:	IMPORTS START>
import com.magiology.mc_objects.features.dimension_stabiliser.TileEntityDimensionStabiliser;
import com.magiology.mc_objects.features.machines.shaker.TileEntityShaker;
import com.magiology.mc_objects.features.neuro.TileEntityNeuroController;
import com.magiology.mc_objects.features.neuro.TileEntityNeuroDuct;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
//<GEN:	IMPORTS END>
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.statics.UtilM;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityRegistry extends AutoRegistry<TileEntityM>{

	private static final TileEntityRegistry instance=new TileEntityRegistry();
	public static TileEntityRegistry get(){return instance;}
	private final Map<String, Class<? extends TileEntityM>> nameMap=new HashMap<>();
	
	private TileEntityRegistry(){
		super(TileEntityM.class);
	}
	
	public static Class<? extends TileEntityM> getFromId(String id){
		return instance.nameMap.get(id);
	}
	
	@Override
	public void registerObj(Class<TileEntityM> clazz){
		String name="te_"+UtilM.classNameToMcName(clazz);
		nameMap.put(name,clazz);
		GameRegistry.registerTileEntity(clazz, name);
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
