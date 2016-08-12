package com.magiology.mc_objects;

import com.magiology.mc_objects.blocks.DimensionStabiliserBlock;
import com.magiology.util.objs.DatabaseStorage;

import net.minecraft.block.Block;

public final class MBlocks extends DatabaseStorage<Block>{
	
	public static final MBlocks instance=new MBlocks();
	private MBlocks(){
		super(Block.class);
	}
	@Override
	public Block[] getDatabase(){
		return allBlocks();
	}
	
	//------------------------------------------------------------------
	//BLOCKS AND SHIT---------------------------------------------------
	//------------------------------------------------------------------
	
	public static final DimensionStabiliserBlock DIMENSION_STABILISER=new DimensionStabiliserBlock();
	
	
	public static Block[] allBlocks(){
		return new Block[]{
			DIMENSION_STABILISER,
			//....
		};
	}
}