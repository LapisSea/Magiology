package com.magiology.mcobjects.blocks.network;

import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandler;
import com.magiology.mcobjects.blocks.MultiColisionProviderBlock;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkController extends MultiColisionProviderBlock{
	
	public NetworkController(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*6, p*6, p*6, p*10, p*10, p*10);
		this.useNeighborBrightness=true;
	}

	
	@Override 
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this,new IProperty[]{U.META});
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata){
		return NetworkBaseComponentHandler.createComponent(new TileEntityNetworkController());
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
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player){
		if(!player.isSneaking())return;
		TileEntityNetworkController tile=(TileEntityNetworkController)world.getTileEntity(pos);
		tile.restartNetwork();
	}
}
