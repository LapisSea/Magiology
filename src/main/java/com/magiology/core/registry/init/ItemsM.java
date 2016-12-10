package com.magiology.mc_objects;

import com.magiology.core.class_manager.ClassList;
import com.magiology.util.m_extensions.ResourceLocationM;
import com.magiology.util.objs.data.RegistrableDatabaseStorageArray;
import com.magiology.util.statics.CollectionConverter;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilM;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry extends RegistrableDatabaseStorageArray<Item>{

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
		obj.setRegistryName(new ResourceLocationM(name));
		obj.setUnlocalizedName(name);
		GameRegistry.register(obj);
	}
	

	private final Item[] items=CollectionConverter.convAr(ClassList.getImplementations(Item.class), Item.class, (i)->i.newInstance());
	@Override
	public Item[] getDatabase(){
		return items;
	}
	
}
