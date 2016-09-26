package com.magiology.util.interf;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockBreakListener{
	public void onBroken(World world, BlockPos pos, IBlockState state);
}
