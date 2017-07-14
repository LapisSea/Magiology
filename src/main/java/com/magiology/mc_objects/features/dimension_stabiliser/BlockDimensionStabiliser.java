package com.magiology.mc_objects.features.dimension_stabiliser;

import com.magiology.util.m_extensions.BlockContainerM;
import com.magiology.util.objs.block_bounds.BasicBlockBounds;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDimensionStabiliser extends BlockContainerM{
	
	private static BlockDimensionStabiliser instance;
	
	public static BlockDimensionStabiliser get(){
		return instance;
	}
	
	public BlockDimensionStabiliser(){
		super(Material.IRON, ()->new TileEntityDimensionStabiliser());
		
		setBlockBounds(new BasicBlockBounds(0, 0, 0, 1, 8/16F, 1));
		instance=this;
	}
	
	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return side==EnumFacing.DOWN;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos){
		return state;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer){
		return layer==BlockRenderLayer.CUTOUT;
	}
	
}
