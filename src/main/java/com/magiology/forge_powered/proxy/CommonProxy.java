package com.magiology.forge_powered.proxy;

import java.awt.Color;

import com.magiology.core.Magiology;
import com.magiology.forge_powered.events.EntityEvents;
import com.magiology.forge_powered.events.TickEvents;
import com.magiology.forge_powered.events.WorldEvents;
import com.magiology.io.IOManager;
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
		IOManager manager=Magiology.extraFiles;
		manager.addFolder("animations/user");
		manager.addFile("animations/penguin/swimming.la");
		manager.addFile("animations/user/READ_ME.txt");
		manager.checkAndExtract();
	}
	
	public void preInit(){
		
		MBlocks.get().register();
		MItems.get().register();
		ClassList.getImplementations(TileEntityM.class).forEach((clazz)->GameRegistry.registerTileEntity(clazz, "te_"+UtilM.classNameToMcName(clazz.getSimpleName())));
	}
	
	public void init(){
		registerModEntityWithEgg(EntityPenguin.class,new Color(20, 20, 30),new Color(230, 230, 230));
		MinecraftForge.EVENT_BUS.register(TickEvents.instance);
		MinecraftForge.EVENT_BUS.register(EntityEvents.instance);
		MinecraftForge.EVENT_BUS.register(WorldEvents.instance);
	}
	
	private void registerModEntityWithEgg(Class parEntityClass,Color col1,Color col2){
		registerModEntityWithEgg(parEntityClass, col1.hashCode(), col2.hashCode());
	}
	private void registerModEntityWithEgg(Class parEntityClass,int col1,int col2){
		EntityRegistry.registerModEntity(parEntityClass, UtilM.classNameToMcName(parEntityClass.getSimpleName()), ++entityID, Magiology.getMagiology(), 80, 1, true,col1,col2);
	}
	
	public void postInit(){
		
	}
	
	public void onExit(){
		
	}
}
