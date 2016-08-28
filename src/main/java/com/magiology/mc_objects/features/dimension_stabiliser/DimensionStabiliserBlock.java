package com.magiology.mc_objects.features.dimension_stabiliser;

import com.magiology.util.m_extensions.BlockContainerM;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DimensionStabiliserBlock extends BlockContainerM{

    public static final PropertyBool FACING = PropertyBool.create("main");
    
	public DimensionStabiliserBlock(){
		super(Material.GOURD);
		setCreativeTab(CreativeTabs.FOOD);
		setBlockBounds(0, 0, 0, 1, 8/16F, 1);
		
	}
	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side){
		return false;
	}
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side){
		return side==EnumFacing.DOWN;
	}
	@Override
	public boolean isFullBlock(IBlockState state){
		return false;
	}
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
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
    public int getMetaFromState(IBlockState state){
    	return super.getMetaFromState(state);
    }
    @Override
    public IBlockState getStateFromMeta(int meta){
    	return super.getStateFromMeta(meta);
    }
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer){
        return layer==BlockRenderLayer.CUTOUT||layer==BlockRenderLayer.TRANSLUCENT;
    }
	@Override
	public TileEntity createNewTileEntity(World world, int metadata){
		return new TileEntityDimensionStabiliser();
	}
}
