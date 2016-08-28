package com.magiology.forge_powered.proxy;

import com.magiology.core.Magiology;
import com.magiology.forge_powered.events.EntityEvents;
import com.magiology.forge_powered.events.TickEvents;
import com.magiology.mc_objects.MBlocks;
import com.magiology.mc_objects.MItems;
import com.magiology.mc_objects.entitys.EntityPenguin;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.class_manager.ClassList;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	
	private int entityID=0;
	
	public void loadModFiles(){
		
	}
	
	public void preInit(){
		
		MBlocks.get().register();
		MItems.get().register();
		ClassList.getImplementations(TileEntityM.class).forEach((clazz)->GameRegistry.registerTileEntity(clazz, "te_"+UtilM.classNameToMcName(clazz.getSimpleName())));
	}
	
	public void init(){
		registerModEntityWithEgg(EntityPenguin.class);
		MinecraftForge.EVENT_BUS.register(TickEvents.instance);
		MinecraftForge.EVENT_BUS.register(EntityEvents.instance);
	}
	
	private void registerModEntityWithEgg(Class parEntityClass){
		EntityRegistry.registerModEntity(parEntityClass, UtilM.classNameToMcName(parEntityClass.getSimpleName()), ++entityID, Magiology.getMagiology(), 80, 1, true,0x00FF00,0x0000FF);
	}
	
	public void postInit(){
		
	}
	
	public void onExit(){
		
	}
}
