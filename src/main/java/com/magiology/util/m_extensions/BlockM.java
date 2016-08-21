package com.magiology.util.m_extensions;

import com.magiology.core.MReference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockM extends Block{
	public static final float p=1F/16F;
	
	public BlockM(Material material){
		super(material);
		setRegistryName(MReference.MODID, this.getClass().getSimpleName());
	}
	
	protected AxisAlignedBB boundingBox=new AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10);
	
	public void setBlockBounds(double x1, double y1, double z1, double x2, double y2, double z2){
		setBlockBounds(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
	}
	public void setBlockBounds(AxisAlignedBB box){
		this.boundingBox=box;
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return boundingBox;
	}
	
	
	
	@Override
	public BlockM setCreativeTab(CreativeTabs tab){
		return (BlockM)super.setCreativeTab(tab);
	}
}
