package com.magiology.mcobjects.blocks.network;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkBaseComponent.NetworkBaseComponentHandler;
import com.magiology.api.network.NetworkInterface;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.blocks.MultiColisionProviderBlock;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NetworkRouter extends MultiColisionProviderBlock{
	
	public NetworkRouter(){
		super(Material.iron);
		this.setHardness(10F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*5, p*5, p*5, p*11, p*11, p*11);
		this.useNeighborBrightness=true;
	}
	
	
	@Override 
	protected BlockState createBlockState(){
		return new BlockState(this,new IProperty[]{U.META});
	}
	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return NetworkBaseComponentHandler.createComponent(new TileEntityNetworkRouter());
	}
	@Override
	public int getMetaFromState(IBlockState state){
		return state.getValue(U.META).intValue();
	}
	@Override
	public ItemStack getPickBlock(RayTraceResult target, World world, BlockPosM pos, EntityPlayer player){
		TileEntityNetworkRouter tile=pos.getTile(world, TileEntityNetworkRouter.class);
		if(tile==null)return super.getPickBlock(target, world, pos, player);
		
		int id=tile.getPointedBoxID()-7;
		if(id<0)return super.getPickBlock(target, world, pos, player);
		
		ItemStack stack=tile.getStackInSlot(id);
		return stack!=null?stack:super.getPickBlock(target, world, pos, player);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return getDefaultState().withProperty(U.META, Integer.valueOf(meta));
	}
	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		TileEntityNetworkRouter tile=(TileEntityNetworkRouter) world.getTileEntity(pos);
		int id=tile.getPointedBoxID()-7;
		if(id<0)return false;
		tile.extractionActivated[id]=!tile.extractionActivated[id];
		if(tile.getStackInSlot(id)==null){
			if(!UtilM.isItemInStack(MItems.networkPointer,player.getHeldItemMainhand()))return false;
			tile.setInventorySlotContents(id, player.getHeldItemMainhand());
			player.inventory.mainInventory[player.inventory.currentItem]=null;
			return true;
		}else{
			return true;
		}
	}
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		int side=SideUtil.getOppositeSide(facing.getIndex());
		TileEntity[] sides=SideUtil.getTilesOnSides(world,pos);
		if(!(sides[side] instanceof NetworkInterface)){
			for(int i=0;i<6;i++){
				if(sides[i] instanceof NetworkInterface){
					side=i;
					break;
				}
			}
		}
		side=SideUtil.getOppositeSide(side);
		return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(U.META, side);
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