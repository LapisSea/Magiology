package com.magiology.mcobjects.blocks.network;

import java.util.List;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandler;
import com.magiology.api.network.Redstone;
import com.magiology.api.network.skeleton.TileEntityNetworkInteract;
import com.magiology.mcobjects.blocks.MultiColisionProviderBlock;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkInterface;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NetworkInterface extends MultiColisionProviderBlock{

	private static final AxisAlignedBB box=new AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10);
	
	public NetworkInterface(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.useNeighborBrightness=true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return box;
	}
	
	@Override 
	public boolean canProvidePower(IBlockState state){
		return true;
	}
	
	@Override 
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this,new IProperty[]{U.META});
	}
	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return NetworkBaseComponentHandler.createComponent(new TileEntityNetworkInterface());
	}


	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(U.META).intValue();
	}
	@Override
	public void getProvidingPower(World world, TileEntity t, BlockPos pos, int metadata,Redstone Return, EnumFacing side){
		TileEntityNetworkInterface tile=(TileEntityNetworkInterface)t;
		if(tile.getData().get("redstone")instanceof Redstone){
			Redstone data=(Redstone)tile.getData().get("redstone");
			
			Return.isStrong=data.isStrong;
			Return.on=data.on;
			Return.strenght=0;
			if(data.on){
				boolean enabled=true;
				if(enabled&&side.getIndex()==tile.getOrientation())Return.strenght=data.strenght;
			}
		}
	}
	
	@Override
	public void getRedstoneConnectableSides(IBlockAccess world, TileEntity tile, BlockPos pos, List<Integer> sides){
		if(((TileEntityNetworkInteract)tile).getData().get("redstone")instanceof Redstone)switch(((TileEntityNetworkInteract)tile).getOrientation()){
		case 3:sides.add(0);break;
		case 4:sides.add(1);break;
		case 2:sides.add(2);break;
		case 5:sides.add(3);break;
		}
	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(U.META, Integer.valueOf(meta));
	}
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		super.onBlockAdded(world, pos, state);
		TileEntityNetworkInterface tile=(TileEntityNetworkInterface)world.getTileEntity(pos);
		int side=tile.getOrientation();
		TileEntity test=world.getTileEntity(SideUtil.offset(side, pos));
		
		if(!(test instanceof ISidedNetworkComponent)){
			for(int i=0;i<tile.connections.length;i++){
				if(
						tile.connections[i]!=null&&
						(test=world.getTileEntity(SideUtil.offset(side, pos)))instanceof ISidedNetworkComponent&&
						((ISidedNetworkComponent)test).getBrain()!=null
				   ){
					side=i;
					i=tile.connections.length;
				}
			}
		}
		TileEntity test2=world.getTileEntity(SideUtil.offset(side, pos));
		if(side!=-1&&test2 instanceof ISidedNetworkComponent){
			ISidedNetworkComponent component=(ISidedNetworkComponent) test2;
			if(component!=null)tile.setBrain(component.getBrain());
			tile.canPathFindTheBrain=true;
		}
		if(tile.getBrain()!=null)tile.getBrain().restartNetwork();
		super.onBlockAdded(world, pos, state);
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