package com.magiology.mc_objects;

import com.magiology.core.MReference;
import com.magiology.util.objs.RegistrableDatabaseStorage;
import com.magiology.util.statics.*;
import com.magiology.util.statics.class_manager.ClassList;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry extends RegistrableDatabaseStorage<Item>{

	private static final ItemRegistry instance=new ItemRegistry();
	
	public static ItemRegistry get(){
		return instance;
	}
	
	private ItemRegistry(){
		super(Item.class);
	}

	@Override
	public void registerObj(Item obj){
		String name=UtilM.classNameToMcName(obj.getClass());
		LogUtil.println("(UAHSDUABSUIDN1",obj,name);
		obj.setRegistryName(MReference.MODID, name);
		obj.setUnlocalizedName(name);
		GameRegistry.register(obj);
		LogUtil.println("(UAHSDUABSUIDN2",obj.getRegistryName(),name);
	}
	

	private final Item[] items=CollectionConverter.convAr(ClassList.getImplementations(Item.class), Item.class, (i)->i.newInstance());
	@Override
	public Item[] getDatabase(){
		return items;
	}
	
}
