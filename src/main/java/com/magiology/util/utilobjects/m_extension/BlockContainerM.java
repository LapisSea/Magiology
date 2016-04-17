package com.magiology.util.utilobjects.m_extension;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.network.Redstone;
import com.magiology.core.MReference;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockContainerM extends BlockContainer{
	public static final float p=1F/16F;
	protected boolean isNullTileEntityOk=false;
	protected BlockContainerM(Material material){
		super(material);
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
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side){
		if(side==null)return false;
		List<Integer> sides=new ArrayList<Integer>();
		getRedstoneConnectableSides(world, world.getTileEntity(pos), pos,sides);
		
		return sides.contains(side.getIndex());
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
	
	/**
	 * Used instead of isProvidingStrongPower and isProvidingWeakPower. Note: do not use Return=smth; use Return.smth=smth_else;
	 * @param side 
	 */
	public void getProvidingPower(World world, TileEntity tile, BlockPos pos, int metadata,Redstone Return, EnumFacing side){}
	public void getRedstoneConnectableSides(IBlockAccess world, TileEntity tile, BlockPos pos, List<Integer> sides){}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side){
		Redstone data=new Redstone();
		getProvidingPower((World)worldIn, worldIn.getTileEntity(pos), pos, UtilM.getBlockMetadata((World)worldIn, pos), data,side);
		if(!data.isStrong)return 0;
		return data.strenght;
	}
	@Override
	public int getWeakPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side){
		Redstone data=new Redstone();
		getProvidingPower((World)worldIn, worldIn.getTileEntity(pos), pos, UtilM.getBlockMetadata((World)worldIn, pos), data,side);
		return data.strenght;
	}
	
	@SideOnly(value=Side.CLIENT)
	public Block setBlockTextureName(){
		return setBlockTextureName(MReference.MODID, getUnlocalizedName().substring(5));
	}
	@SideOnly(value=Side.CLIENT)
	public Block setBlockTextureName(String name){
		BlockM.modelsInit.put(this, new ModelResourceLocation(name));
		return this;
	}
	//#fucka youuu json models!
	@SideOnly(value=Side.CLIENT)
	public Block setBlockTextureName(String modPath,String name){
		BlockM.modelsInit.put(this, new ModelResourceLocation(modPath,name));
		return this;
	}
}
