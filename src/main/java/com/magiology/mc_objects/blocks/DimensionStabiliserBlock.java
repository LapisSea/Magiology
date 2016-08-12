package com.magiology.mc_objects.blocks;

import com.magiology.mc_objects.tileentitys.DummyTileEntity;
import com.magiology.util.m_extensions.BlockContainerM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
<<<<<<< HEAD
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimensionStabiliserBlock extends BlockContainerM{

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
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer(){
		return BlockRenderLayer.CUTOUT;
=======
import net.minecraft.world.World;

public class DummyBlock extends BlockContainerM{

	public DummyBlock(){
		super(Material.iron);
		setCreativeTab(CreativeTabs.tabAllSearch);
>>>>>>> 1.9
	}
	@Override
	public TileEntity createTileEntity(World world, IBlockState state){
		return new DummyTileEntity();
	}
}
