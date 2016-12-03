package com.magiology.mc_objects.features.neuro;

import com.magiology.util.statics.LogUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNeuroController extends BlockNeuroBase<TileEntityNeuroController>{
	
	public BlockNeuroController(){
		super(Material.IRON,()->new TileEntityNeuroController(),CONNECTIONS);
	}
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn){
		TileEntityNeuroController tile=getTile(world, pos);
		for(int i=0;i<6;i++){
			BlockPos pos1=tile.getPos().offset(EnumFacing.getFront(i));
			TileEntity tile1=world.getTileEntity(pos1);
			state=CONNECTIONS[i].set(state, tile1 instanceof NeuroPart);
		}
		world.setBlockState(pos, state);
		
		super.neighborChanged(state, world, pos, blockIn);
	}
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn){
		LogUtil.println(playerIn);
	}
	
}
