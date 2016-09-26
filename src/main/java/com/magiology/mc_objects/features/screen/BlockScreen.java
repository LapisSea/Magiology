package com.magiology.mc_objects.features.screen;

import com.magiology.mc_objects.BlockStates;
import com.magiology.mc_objects.BlockStates.PropertyDirectionM;
import com.magiology.util.interf.IBlockBreakListener;
import com.magiology.util.m_extensions.BlockContainerM;
import com.magiology.util.objs.Vec3M;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class BlockScreen extends BlockContainerM implements IBlockBreakListener{
	
	public static final PropertyDirectionM ROT=BlockStates.ROTATION_FULL_3BIT;
	
	public BlockScreen(){
		super(Material.IRON);
		setPossibleBlockBounds(new AxisAlignedBB(0,p*5,0,1,1-p*5,1),new AxisAlignedBB(0,0,p*5,1,1,1-p*5),new AxisAlignedBB(p*5,0,0,1-p*5,1,1));
	}
	
	@Override
	protected int chooseBox(IBlockState state){
		return state.getValue(ROT).getIndex()/2;
	}
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, new IProperty[]{ROT});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return ROT.set(getDefaultState(),EnumFacing.getFront(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return ROT.get(state).getIndex();
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn,BlockPos pos,EnumFacing facing,float hitX,float hitY,float hitZ,int meta,EntityLivingBase placer){
		return ROT.set(getDefaultState(),facing);
	}
	
	public EnumFacing getRotation(IBlockState state){
		return ROT.get(state);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world,int metadata){
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
	public boolean onBlockActivated(World worldIn,BlockPos pos,IBlockState state,EntityPlayer playerIn,EnumHand hand,ItemStack heldItem,EnumFacing side,float hitX,float hitY,float hitZ){
		if(hand==EnumHand.OFF_HAND)return false;
		TileEntityScreen tile=(TileEntityScreen)worldIn.getTileEntity(pos);
		tile.structurize(new Vec3M(pos).add(hitX,hitY,hitZ));
		return false;
	}
	
	@Override
	public void onBroken(World world,BlockPos pos,IBlockState state){
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityScreen)
			((TileEntityScreen)tile).onBroken(world,pos,state);
	}
}
