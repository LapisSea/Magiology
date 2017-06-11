package com.magiology.mc_objects.features.machines.shaker;

import com.magiology.util.m_extensions.BlockContainerM;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

public class BlockShaker extends BlockContainerM<TileEntityShaker>{
	
	public BlockShaker(){
		super(Material.IRON, ()->new TileEntityShaker());
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}
