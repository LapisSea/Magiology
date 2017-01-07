package com.magiology.forge.events;

import com.magiology.util.interf.IBlockBreakListener;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@EventBusSubscriber
public class WorldEvents{

	public WorldEvents(){throw new UnsupportedOperationException();}
	
	@SubscribeEvent
	public static void blockBreakEvent(BreakEvent e){
		IBlockState state=e.getState();
		Block block=state.getBlock();
		IBlockBreakListener listener=null;
		if(block instanceof IBlockBreakListener)listener=(IBlockBreakListener)block;
		else{
			TileEntity tile=e.getWorld().getTileEntity(e.getPos());
			if(tile instanceof IBlockBreakListener)listener=(IBlockBreakListener)tile;
		}
		if(listener!=null)listener.onBroken(e.getWorld(), e.getPos(), state);
		
	}
	
}
