package com.magiology.util.m_extensions;

import com.magiology.util.statics.*;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public abstract class BlockContainerM extends BlockContainer{
	public static final float p=1F/16F;
	
	protected boolean isNullTileEntityOk=false,hasBoxArray=false;
	private AxisAlignedBB boundingBox=new AxisAlignedBB(0, 0, 0, 1, 1, 1),boxes[];
	
	protected BlockContainerM(Material material){
		super(material);
	}
	
	
	public void setBlockBounds(double x1, double y1, double z1, double x2, double y2, double z2){
		setBlockBounds(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
	}
	public void setBlockBounds(AxisAlignedBB box){
		this.boundingBox=box;
	}
	public void setPossibleBlockBounds(AxisAlignedBB...boxes){
		if(boxes==null)throw new IllegalArgumentException("Boxes canot be null!");
		if(boxes.length==0)throw new IllegalArgumentException("Boxes canot be empty!");
		if(this.boxes!=null)throw new IllegalArgumentException("Boxes are already set!");
		
		this.boxes=boxes;
		hasBoxArray=true;
	}
	
	protected int chooseBox(IBlockState state){
		throw new IllegalStateException("Function chooseBox is not overriden in: "+this.getClass().getName()+" and boxes are set!");
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return hasBoxArray?boxes[chooseBox(state)]:boundingBox;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata){
		if(!isNullTileEntityOk){
			PrintUtil.println(
					getUnlocalizedName()+" block is a BlockContainer and it is not providing a TileEntity!",
					"Are you sure that this is ok?",
					"If so please add "+'"'+"isNullTileEntityOk=false;"+'"'+" for disabling this message",
					"If not use createNewTileEntity(World world, int metadata) function to provide one!","",
					UtilM.getStackTrace());
		}
		return null;
	}
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world,BlockPos pos, EntityPlayer player){
		return getPickBlock(state, target, world, new BlockPosM(pos), player);
	}
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world,BlockPosM pos, EntityPlayer player){
		return super.getPickBlock(state,target, world, pos, player);
	}
	
}
