package com.magiology.forge_powered.proxy;

import com.magiology.forge_powered.events.TickEvents;
import com.magiology.mc_objects.blocks.DummyBlock;
import com.magiology.mc_objects.tileentitys.DummyTileEntity;
import com.magiology.util.m_extensions.TileEntityM;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	
	public void loadModFiles(){
		
	}
	
	public void preInit(){
		loadBlocks();
		loadTileEntitys();
	}
	
	public void init(){
		FMLCommonHandler.instance().bus().register(TickEvents.instance);
	}
	
	public void postInit(){
		
	}
	
	public void onExit(){
		
	}
	
	private void loadBlocks(){
		GameRegistry.registerBlock(new DummyBlock());
	}
	private void loadTileEntitys(){
		register(DummyTileEntity.class);
	}
	private static<T extends TileEntityM> void register(Class<T> clazz){
		String name=clazz.getSimpleName().substring("TileEntity".length());
		GameRegistry.registerTileEntity(clazz, "TE"+name);
	}
	
}


