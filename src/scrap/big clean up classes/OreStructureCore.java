package com.magiology.mcobjects.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OreStructureCore extends BlockContainer {
	
//	@SideOnly(Side.CLIENT) private IIcon top;
//	@SideOnly(Side.CLIENT) private IIcon front;	
//	@Override
//	@SideOnly(Side.CLIENT)
//	public IIcon getIcon(int side, int p_149691_2_) {
//	return side == 1 || side == 0 ? this.top : (side == 2 ? this.front : this.blockIcon);
//	}
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister p_149651_1_) {
//	this.blockIcon = p_149651_1_.registerIcon(MReference.MODID + ":" + "OreStructureCore");
//	this.top = p_149651_1_.registerIcon("bedrock");
//	this.front = p_149651_1_.registerIcon(MReference.MODID + ":" + "OreStructureCore");
//	}
	public OreStructureCore() {
		super(Material.iron);
		this.setLightLevel(1F).setHardness(10F).setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityOreStructureCore();
	}
	
}