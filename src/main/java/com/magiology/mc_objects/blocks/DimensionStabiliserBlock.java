package com.magiology.mc_objects.blocks;

import com.magiology.mc_objects.tileentitys.DummyTileEntity;
import com.magiology.util.m_extensions.BlockContainerM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class DummyBlock extends BlockContainerM{

	public DummyBlock(){
		super(Material.iron);
		setCreativeTab(CreativeTabs.tabAllSearch);
	}
	@Override
	public TileEntity createTileEntity(World world, IBlockState state){
		return new DummyTileEntity();
	}
}
