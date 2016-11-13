package com.magiology.mc_objects.features.neuro;

import com.magiology.mc_objects.BlockStates.IPropertyM;
import com.magiology.util.interf.ObjectReturn;
import com.magiology.util.m_extensions.BlockContainerM;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockNeuroBase<T extends TileEntityNeuroBase> extends BlockContainerM<T>{
	
	
	protected BlockNeuroBase(Material material, ObjectReturn<T> tileFactory, IPropertyM[] properties){
		super(material, tileFactory, properties);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn){
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof NeuroPart){
			TileEntityNeuroController controller=((NeuroPart)tile).getController();
			if(controller!=null)controller.refreshConnected();
		}
	}
	
}
