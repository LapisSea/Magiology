package com.magiology.forge.proxy;

import static com.magiology.core.MReference.*;

import java.awt.Color;

import com.magiology.DevOnly;
import com.magiology.core.Magiology;
import com.magiology.cross_mod.ModChecker;
import com.magiology.forge.events.*;
import com.magiology.forge.networking.*;
import com.magiology.io.IOManager;
import com.magiology.mc_objects.*;
import com.magiology.mc_objects.entitys.EntityPenguin;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.class_manager.ClassList;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.*;

public class CommonProxy{
	
	private int entityID=0;
	
	public void loadModFiles(){
		IOManager manager=Magiology.extraFiles;
		
		manager.addFile("READ_ME.txt");
		
		manager.addFolder("animations/user");
		manager.addFile  ("animations/penguin/swimming.la");
		
		manager.addFolder("shaders/user");
		manager.addFile  ("shaders/invisible.fs");
		
		manager.checkAndExtract();
	}
	
	public void preInit(){
		
		Magiology.getMagiology().NETWORK_CHANNEL=new SimpleNetworkWrapperM(CHANNEL_NAME);
		Packets.register();
		BlockRegistry.get().register();
		ItemRegistry.get().register();
		ClassList.getImplementations(TileEntityM.class).forEach((clazz)->GameRegistry.registerTileEntity(clazz, "te_"+UtilM.classNameToMcName(clazz.getSimpleName())));
		
	}
	
	public void init(){
		registerModEntityWithEgg(EntityPenguin.class,new Color(20, 20, 30),new Color(230, 230, 230));
		MinecraftForge.EVENT_BUS.register(TickEvents.instance);
		MinecraftForge.EVENT_BUS.register(EntityEvents.instance);
		MinecraftForge.EVENT_BUS.register(WorldEvents.instance);
	}
	
	public void postInit(){
		ModChecker.instance().detectMods();
		ModChecker.instance().init();
	}
	
	public void onExit(){
		
	}
	
	private void registerModEntityWithEgg(Class parEntityClass,Color col1,Color col2){
		registerModEntityWithEgg(parEntityClass, col1.hashCode(), col2.hashCode());
	}
	private void registerModEntityWithEgg(Class parEntityClass,int col1,int col2){
		EntityRegistry.registerModEntity(parEntityClass, UtilM.classNameToMcName(parEntityClass.getSimpleName()), ++entityID, Magiology.getMagiology(), 80, 1, true,col1,col2);
	}
}
