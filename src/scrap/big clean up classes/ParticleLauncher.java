package com.magiology.mcobjects.blocks;

import com.magiology.util.utilobjects.m_extension.BlockContainerM;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ParticleLauncher extends BlockContainerM {

	float p=1F/16F;
	
	@Override
	public boolean isOpaqueCube() {return false;}
	
	public ParticleLauncher() {
		super(Material.iron);
		this.setHardness(20F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityPLauncher();
	}
}
