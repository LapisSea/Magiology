package com.magiology.forge_powered.proxy;

import com.magiology.core.MReference;
import com.magiology.forge_powered.events.TickEvents;
import com.magiology.mc_objects.MBlocks;
import com.magiology.mc_objects.tileentitys.DummyTileEntity;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy{
	
	public void loadModFiles(){
		
	}
	
	public void preInit(){
		loadBlocks();
		loadTileEntitys();
	}
	
	public void init(){
		MinecraftForge.EVENT_BUS.register(TickEvents.instance);
	}
	
	public void postInit(){
		
	}
	
	public void onExit(){
		
	}
	
	private void loadBlocks(){
		for(Block block:MBlocks.allBlocks())registerBlock(block);
	}
	private ItemBlock registerBlock(Block block){
		return registerBlock(block,block.getClass().getSimpleName());
	}
	private ItemBlock registerBlock(Block block,String name){
		name=UtilM.standardizeName(UtilM.removeMcObjectEnd(name));
		block.setRegistryName(MReference.MODID, name);
		block.setUnlocalizedName(name);
		ItemBlock ib=new ItemBlock(block);
		GameRegistry.register(block);
		GameRegistry.register(ib.setRegistryName(MReference.MODID, name));
		return ib;
	}
	
	
	private void loadTileEntitys(){
		register(DummyTileEntity.class);
	}
	private static void register(Class<? extends TileEntityM> clazz){
		String name=clazz.getSimpleName().substring("TileEntity".length());
		GameRegistry.registerTileEntity(clazz, "TE"+name);
	}
	
}


