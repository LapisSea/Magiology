package com.magiology.mc_objects;

import com.magiology.core.MReference;
import com.magiology.util.m_extensions.BlockContainerM;
import com.magiology.util.m_extensions.BlockM;
import com.magiology.util.objs.RegistrableDatabaseStorage;
import com.magiology.util.statics.CollectionConverter;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.class_manager.ClassList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class MBlocks extends RegistrableDatabaseStorage<Block>{
	
	private static final MBlocks instance=new MBlocks();
	
	public static MBlocks get(){
		return instance;
	}
	
	private MBlocks(){
		super(Block.class);
	}
	
	private final Block[] blocks=CollectionConverter.convAr(ClassList.getImplementations(BlockM.class,BlockContainerM.class), Block.class, (i)->i.newInstance());
	
	@Override
	public Block[] getDatabase(){
		return blocks;
	}
	
	@Override
	public void registerObj(Block block){
		String name=UtilM.standardizeName(UtilM.removeMcObjectEnd(block.getClass().getSimpleName()));
		block.setRegistryName(MReference.MODID, name);
		block.setUnlocalizedName(name);
		ItemBlock ib=new ItemBlock(block);
		GameRegistry.register(block);
		GameRegistry.register(ib.setRegistryName(MReference.MODID, name));
	}
}
