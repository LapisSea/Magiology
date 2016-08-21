package com.magiology.mc_objects;

import com.magiology.core.MReference;
import com.magiology.util.objs.RegistrableDatabaseStorage;
import com.magiology.util.statics.CollectionConverter;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.class_manager.ClassList;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MItems extends RegistrableDatabaseStorage<Item>{

	private static final MItems instance=new MItems();
	
	public static MItems get(){
		return instance;
	}
	
	private MItems(){
		super(Item.class);
	}

	@Override
	public void registerObj(Item obj){
		String name=UtilM.standardizeName(UtilM.removeMcObjectEnd(obj.getClass().getSimpleName()));
		GameRegistry.register(obj.setRegistryName(MReference.MODID, name).setUnlocalizedName(name));
	}
	
	private final Item[] items=CollectionConverter.convAr(ClassList.getImplementations(Item.class), Item.class, (i)->i.newInstance());
	
	@Override
	public Item[] getDatabase(){
		return items;
	}
	
}
