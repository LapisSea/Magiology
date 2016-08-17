package com.magiology.forge_powered.proxy;

import com.magiology.core.MReference;
import com.magiology.features.MRegistery;
import com.magiology.forge_powered.events.TickEvents;
import com.magiology.util.interf.ObjectProcessor;
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
		MRegistery.registerBlocks(((ObjectProcessor<Block>)(obj,a)->{
			registerBlock(obj);
			return obj;
		}));
		MRegistery.registerTileEntitys(((ObjectProcessor<Class<? extends TileEntityM>>)(clazz,a)->{
			GameRegistry.registerTileEntity(clazz, "te_"+UtilM.standardizeName(UtilM.removeMcObjectEnd(clazz.getSimpleName())));
			return clazz;
		}));
	}
	
	public void init(){
		MinecraftForge.EVENT_BUS.register(TickEvents.instance);
	}
	
	public void postInit(){
		
	}
	
	public void onExit(){
		
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
	
	
}


