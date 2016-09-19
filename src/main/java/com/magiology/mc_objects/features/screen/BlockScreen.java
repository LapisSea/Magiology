package com.magiology.mc_objects.features.screen;

import com.magiology.util.m_extensions.BlockContainerM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
		if(hand==EnumHand.OFF_HAND)return false;
		if(worldIn.isRemote){
			TileEntityScreen tile=(TileEntityScreen)worldIn.getTileEntity(pos);
			tile.structurize();
		}
		return false;
	}
}
