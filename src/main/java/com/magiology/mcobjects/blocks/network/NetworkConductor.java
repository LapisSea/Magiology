package com.magiology.mcobjects.blocks.network;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandler;
import com.magiology.mcobjects.blocks.MultiColisionProviderBlock;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkConductor;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NetworkConductor extends MultiColisionProviderBlock{
	
	public NetworkConductor(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*6, p*6, p*6, p*10, p*10, p*10);
		this.useNeighborBrightness=true;
	}
	
	
	@Override 
	protected BlockState createBlockState(){
		return new BlockState(this,new IProperty[]{U.META});
	}
	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return NetworkBaseComponentHandler.createComponent(new TileEntityNetworkConductor());
	}
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(U.META).intValue();
	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(U.META, Integer.valueOf(meta));
	}
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(U.META, facing.getIndex());
	}
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		super.onNeighborChange(world, pos, neighbor);
		TileEntity test=world.getTileEntity(pos);
		if(!(test instanceof ISidedNetworkComponent))return;
		ISidedNetworkComponent tile=(ISidedNetworkComponent)test;
		if(tile.getBrain()!=null)tile.getBrain().restartNetwork();
	}
}