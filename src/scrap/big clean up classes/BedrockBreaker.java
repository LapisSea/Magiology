package com.magiology.mcobjects.blocks;

import com.magiology.mcobjects.tileentityes.TileEntityBedrockBreaker;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BedrockBreaker extends BlockContainer {

	float p=1F/16F;
	
	@Override
	public boolean isOpaqueCube() {return false;}
	@Override
	public int getRenderType(){
		return -1;
	}
	public BedrockBreaker() {
		super(Material.glass);
		this.setHardness(20F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos){
		setBlockBounds(p*3, 0F, p*3, p*13, p*16, p*13);}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityBedrockBreaker();
	}
}
