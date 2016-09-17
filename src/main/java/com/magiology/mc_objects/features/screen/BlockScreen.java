package com.magiology.mc_objects.features.screen;

import com.magiology.util.m_extensions.BlockContainerM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockScreen extends BlockContainerM{
	
	public BlockScreen(){
		super(Material.IRON);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata){
		return new TileEntityScreen();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}
