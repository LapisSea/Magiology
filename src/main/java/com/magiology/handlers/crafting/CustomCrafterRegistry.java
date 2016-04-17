package com.magiology.handlers.crafting;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.magiology.core.MReference;
import com.magiology.io.IOReadableMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CustomCrafterRegistry{
	
	private static Map<Item,Item> data=new HashMap<Item,Item>();
	private static boolean initDone=false;
	private static IOReadableMap loader;
	
	public static ItemStack getProduct(ItemStack stack){
		return new ItemStack(data.get(stack.getItem()));
	}
	public static void init(String filePath){
		if(initDone)throw new IllegalStateException("This handler has already been initialized!");
		initDone=true;
		
		loader=new IOReadableMap(filePath);
		loader.readFromFile();
		Map<String,String> fileData=loader.data;
		
		for(Entry<String,String> i:fileData.entrySet()){
			Item processingItem=GameRegistry.findItem(MReference.MODID, i.getKey());
			Item productItem=GameRegistry.findItem(MReference.MODID, i.getValue());
			if(processingItem!=null&&productItem!=null)data.put(processingItem, productItem);
			else throw new IllegalArgumentException((processingItem==null?"processingItem: "+i.getKey()+" does not exist!":"")+(productItem==null?(processingItem==null?",":"")+" productItem: "+i.getValue()+" does not exist!":""));
		}
	}
	public static void setupFile(Map<Item,Item> data){
		loader.data.clear();
		for(Entry<Item,Item> i:data.entrySet()){
			loader.data.put(i.getKey().getUnlocalizedName(), i.getValue().getUnlocalizedName());
		}
	}
}