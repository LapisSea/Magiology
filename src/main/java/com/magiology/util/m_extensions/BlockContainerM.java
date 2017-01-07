package com.magiology.util.m_extensions;

import com.magiology.forge.proxy.ClientProxy;
import com.magiology.util.interf.ObjectReturn;
import com.magiology.util.objs.BlockStates.IPropertyM;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockContainerM<T extends TileEntity> extends BlockM implements ITileEntityProvider{
	
	public static interface MixedRender<T extends BlockContainerM>{
		default void registerModel(T block){
			ClientProxy.autoJsonModel(block);
			ClientProxy.autoTESR(block);
		}
	}
	
	public static final float p=1F/16F;
	private final ObjectReturn<T> tileFactory;
	
	protected BlockContainerM(Material material, ObjectReturn<T> tileFactory,IPropertyM...properties){
		super(material,properties);
		isBlockContainer=true;
		
		if(tileFactory==null)this.tileFactory=()->null;
		else this.tileFactory=tileFactory;
	}
	
	@Override
	public void breakBlock(World world,BlockPos pos,IBlockState state){
		removeTile(world,pos);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer(){
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean eventReceived(IBlockState state,World worldIn,BlockPos pos,int id,int param){
		TileEntity tileentity=worldIn.getTileEntity(pos);
		return tileentity==null?false:tileentity.receiveClientEvent(id,param);
	}
	
	public void createTile(World world,BlockPos pos){
		world.setTileEntity(pos, tileFactory.process());
	}
	public void removeTile(World world,BlockPos pos){
		world.removeTileEntity(pos);
	}
	public T getTile(World world,BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		try{
			return (T)tile;
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public final TileEntity createNewTileEntity(World world, int metadata){
		return tileFactory.process();
	}
	
}
