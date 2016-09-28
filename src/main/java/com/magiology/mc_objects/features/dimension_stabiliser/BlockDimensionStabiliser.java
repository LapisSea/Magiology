package com.magiology.mc_objects.features.dimension_stabiliser;

import com.magiology.util.m_extensions.BlockContainerM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockDimensionStabiliser extends BlockContainerM{
	
	public BlockDimensionStabiliser(){
		super(Material.IRON);
		setBlockBounds(0,0,0,1,8/16F,1);
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess worldIn,BlockPos pos,EnumFacing side){
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState state,IBlockAccess world,BlockPos pos,EnumFacing side){
		return side==EnumFacing.DOWN;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state,IBlockAccess worldIn,BlockPos pos){
		return state;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state,BlockRenderLayer layer){
		return layer==BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world,int metadata){
		return new TileEntityDimensionStabiliser();
	}
}
