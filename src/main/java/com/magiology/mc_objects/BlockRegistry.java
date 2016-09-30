package com.magiology.mc_objects;

import com.magiology.core.MReference;
import com.magiology.util.m_extensions.BlockM;
import com.magiology.util.objs.RegistrableDatabaseStorage;
import com.magiology.util.statics.*;
import com.magiology.util.statics.class_manager.ClassList;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BlockRegistry extends RegistrableDatabaseStorage<BlockM>{
	
	private static final BlockRegistry instance=new BlockRegistry();
	
	public static BlockRegistry get(){
		return instance;
	}
	
	private BlockRegistry(){
		super(BlockM.class);
	}
	
	private final BlockM[] blocks=CollectionConverter.convAr(ClassList.getImplementations(BlockM.class), BlockM.class, (i)->i.newInstance());
	
	@Override
	public BlockM[] getDatabase(){
		return blocks;
	}
	
	@Override
	public void registerObj(BlockM block){
		String name=UtilM.standardizeName(UtilM.removeMcObjectEnd(block.getClass().getSimpleName()));
		block.setRegistryName(MReference.MODID, name);
		block.setUnlocalizedName(name);
		ItemBlock ib=new ItemBlock(block);
		GameRegistry.register(block);
		GameRegistry.register(ib.setRegistryName(MReference.MODID, name));
	}
}
