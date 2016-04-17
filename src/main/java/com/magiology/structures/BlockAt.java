package com.magiology.structures;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class BlockAt{
	public Block bl;
	public int metadata=0;
	public BlockPos pos;
	public BlockAt(Block block,BlockPos pos){
		this.bl=block;
		this.pos=pos;
	}
	public BlockAt(Block block,int x,int y,int z){
		this(block, new BlockPos(x,y,z));
	}
}
