package com.magiology.util.m_extensions;

import com.magiology.mc_objects.BlockStates.IPropertyM;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockContainerM extends BlockM implements ITileEntityProvider{
	public static final float p=1F/16F;
	
	protected boolean isNullTileEntityOk=false;
	
	protected BlockContainerM(Material material,IPropertyM...properties){
		super(material,properties);
		this.isBlockContainer=true;
	}
	
	@Override
	public void breakBlock(World worldIn,BlockPos pos,IBlockState state){
		super.breakBlock(worldIn,pos,state);
		worldIn.removeTileEntity(pos);
	}
	
	@Override
	public BlockRenderLayer getBlockLayer(){
		return BlockRenderLayer.CUTOUT;
	}
	
	/**
	 * Called on both Client and Server when World#addBlockEvent is called. On
	 * the Server, this may perform additional changes to the world, like
	 * pistons replacing the block with an extended base. On the client, the
	 * update may involve replacing tile entities, playing sounds, or performing
	 * other visual actions to reflect the server side changes.
	 * 
	 * @param state
	 *            The block state retrieved from the block position prior to
	 *            this method being invoked
	 * @param pos
	 *            The position of the block event. Can be used to retrieve tile
	 *            entities.
	 */
	@Override
	public boolean eventReceived(IBlockState state,World worldIn,BlockPos pos,int id,int param){
		super.eventReceived(state,worldIn,pos,id,param);
		TileEntity tileentity=worldIn.getTileEntity(pos);
		return tileentity==null?false:tileentity.receiveClientEvent(id,param);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata){
		if(!isNullTileEntityOk){
			throw new IllegalStateException(
					getUnlocalizedName()+"\nblock is a BlockContainer and it is not providing a TileEntity!"+
					"\nAre you sure that this is ok?"+
					"\nIf so please add \"isNullTileEntityOk=false;\" for disabling this message"+
					"\nIf not use createNewTileEntity(World world, int metadata) function to provide one!\n"+
					UtilM.getStackTrace());
		}
		return null;
	}
	
}
