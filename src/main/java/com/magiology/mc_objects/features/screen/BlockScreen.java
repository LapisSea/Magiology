package com.magiology.mc_objects.features.screen;

import com.magiology.mc_objects.BlockStates;
import com.magiology.mc_objects.BlockStates.*;
import com.magiology.util.m_extensions.*;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class BlockScreen extends BlockContainerM<TileEntityScreen>{

	public static final PropertyDirectionM ROT=BlockStates.SAVE_ROTATION_FULL_3BIT;
	public static final PropertyBoolM ACTIVE=BlockStates.saveableBooleanProp("active");
	
	public BlockScreen(){
		super(Material.IRON,()->new TileEntityScreen(),ROT,ACTIVE);
		setPossibleBlockBounds(new AxisAlignedBB(0,p*5,0,1,1-p*5,1),new AxisAlignedBB(0,0,p*5,1,1,1-p*5),new AxisAlignedBB(p*5,0,0,1-p*5,1,1));
	}
	
	@Override
	protected int chooseBox(IBlockState state){
		return state.getValue(ROT).getIndex()/2;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn,BlockPos pos,EnumFacing facing,float hitX,float hitY,float hitZ,int meta,EntityLivingBase placer){
		return ROT.set(getDefaultState(),facing);
	}

	public static EnumFacing getRotation(IBlockState state){
		return ROT.get(state);
	}
	public static boolean isActive(IBlockState state){
		return ACTIVE.get(state);
	}
	public static IBlockState setActive(IBlockState state, boolean active){
		return ACTIVE.set(state,active);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean onBlockActivated(World world,BlockPos pos,IBlockState state,EntityPlayer playerIn,EnumHand hand,ItemStack heldItem,EnumFacing side,float hitX,float hitY,float hitZ){
		if(hand==EnumHand.OFF_HAND)return false;
		TileEntityScreen tile=new BlockPosM(pos).getTile(world,TileEntityScreen.class);
		if(tile!=null){
			tile.formMultiblock(pos);
		}
		return tile!=null;
	}
	
}
