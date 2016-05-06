package com.magiology.mcobjects.blocks.fire;

import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.util.utilobjects.m_extension.BlockContainerM;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class FireLamp extends BlockContainerM{
	
	public FireLamp(){
		super(Material.glass);
		this.setLightLevel(1F).setHardness(0.2F).setHarvestLevel("pickaxe", 1);
		setBlockBounds(0.25F, 0.0005F, 0.25F, 0.75F, 0.7F, 0.75F);
		setStepSound(SoundType.GLASS);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2){
		return new TileEntityFireLamp();
	}
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){return false;}
	
}
