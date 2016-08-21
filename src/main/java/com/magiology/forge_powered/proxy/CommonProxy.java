package com.magiology.forge_powered.proxy;

import com.magiology.forge_powered.events.EntityEvents;
import com.magiology.forge_powered.events.TickEvents;
import com.magiology.mc_objects.MBlocks;
import com.magiology.mc_objects.MItems;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.class_manager.ClassList;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	
	public void loadModFiles(){
		
	}
	
	public void preInit(){
		
		MBlocks.get().register();
		MItems.get().register();
		ClassList.getImplementations(TileEntityM.class).forEach((clazz)->GameRegistry.registerTileEntity(clazz, "te_"+UtilM.standardizeName(UtilM.removeMcObjectEnd(clazz.getSimpleName()))));
	}
	
	public void init(){
		MinecraftForge.EVENT_BUS.register(TickEvents.instance);
		MinecraftForge.EVENT_BUS.register(EntityEvents.instance);
	}
	
	public void postInit(){
		
	}
	
	public void onExit(){
		
	}
	
	
	
}


