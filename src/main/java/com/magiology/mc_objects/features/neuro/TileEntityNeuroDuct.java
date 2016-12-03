package com.magiology.mc_objects.features.neuro;

import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.util.m_extensions.TileEntityM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityNeuroDuct extends TileEntityM implements NeuroPart{
	
	private TileEntityNeuroController controller;
	
	public TileEntityNeuroDuct(){
		
	}
	
	@Override
	protected void readFromNbtWithWorld(NBTTagCompound compound){
		readNeuroPartFromNbt(compound);
		BlockNeuroDuct.get().updateBlockState(getState(), getWorld(), pos);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		writeNeuroPartToNbt(compound);
		return super.writeToNBT(compound);
	}
	
	@Override
	public TileEntityNeuroController getController(){
		return controller;
	}
	
	@Override
	public void setController(TileEntityNeuroController controller){
		if(this.controller!=controller){
			this.controller=controller;
			if(controller!=null)controller.requestConnectedRefresh();
			UpdateTileNBTPacket.markForSync(this);
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
		return oldState.getBlock()!=newSate.getBlock();
	}
	
	@Override
	public void onConnect(){
		setState(BlockNeuroBase.HAS_CONTROLLER.set(getState(), true));
	}
	
	@Override
	public void onDisconnect(){
		IBlockState state=getState();
		if(state.getBlock() instanceof BlockNeuroDuct)setState(BlockNeuroBase.HAS_CONTROLLER.set(state, false));
	}
	
}
