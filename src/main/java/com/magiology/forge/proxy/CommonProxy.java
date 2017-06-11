package com.magiology.forge.proxy;

import com.magiology.core.Magiology;
import com.magiology.core.registry.init.BlocksM;
import com.magiology.core.registry.init.ItemsM;
import com.magiology.core.registry.init.PacketRegistry;
import com.magiology.core.registry.init.TileEntityRegistry;
import com.magiology.cross_mod.ModChecker;
import com.magiology.io.IOManager;
import com.magiology.mc_objects.entitys.EntityPenguin;
import com.magiology.util.m_extensions.ResourceLocationM;
import com.magiology.util.statics.UtilM;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.awt.*;

public class CommonProxy{
	
	private int entityID=0;
	
	public void loadModFiles(){
		IOManager manager=Magiology.EXTRA_FILES;
		
		manager.addFile("READ_ME.txt");
		
		manager.addFolder("animations/user");
		manager.addFile("animations/penguin/swimming.la");
		
		manager.addFolder("shaders/user");
		manager.addFolder("shaders/modules");
		manager.addFile("shaders/MatterJumper.fs");
		manager.addFile("shaders/MatterJumper.vs");
		manager.addFile("shaders/MultiTransform.fs");
		manager.addFile("shaders/MultiTransform.vs");
		manager.addFile("shaders/vanilla.fs");
		manager.addFile("shaders/vanilla.vs");
		manager.addFile("shaders/modules/FogFX.sm");
		manager.addFile("shaders/modules/InverseMat3.sm");
		manager.addFile("shaders/modules/PixelWobbleFX.sm");
		
		manager.checkAndExtract();
	}
	
	public void preInit(){
		
		BlocksM.get().register();
		ItemsM.get().register();
		PacketRegistry.get().register();
		TileEntityRegistry.get().register();
	}
	
	public void init(){
		registerModEntityWithEgg(EntityPenguin.class, new Color(20, 20, 30), new Color(230, 230, 230));
	}
	
	public void postInit(){
		ModChecker.instance().detectMods();
		ModChecker.instance().init();
	}
	
	public void onExit(){
	}
	
	private void registerModEntityWithEgg(Class parEntityClass, Color col1, Color col2){
		registerModEntityWithEgg(parEntityClass, col1.hashCode(), col2.hashCode());
	}
	
	private void registerModEntityWithEgg(Class parEntityClass, int col1, int col2){
		EntityRegistry.registerModEntity(new ResourceLocationM(UtilM.classNameToMcName(parEntityClass)), parEntityClass, UtilM.classNameToMcName(parEntityClass.getSimpleName()), ++entityID, Magiology.getMagiology(), 80, 1, true, col1, col2);
	}
}
