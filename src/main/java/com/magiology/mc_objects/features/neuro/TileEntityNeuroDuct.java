package com.magiology.mc_objects.features.neuro;

import com.magiology.forge.events.TickEvents;
import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.util.m_extensions.BlockPosM;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityNeuroDuct extends TileEntityNeuroBase{
	
	private TileEntityNeuroController controller;
	protected static final String NBT_CONTROLLER_TAG="brain";
	
	
	public TileEntityNeuroDuct(){
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		Runnable r=()->{
			if(compound.hasKey(NBT_CONTROLLER_TAG)){
				setController(BlockPosM.fromLong(compound.getLong(NBT_CONTROLLER_TAG)).getTile(worldObj, TileEntityNeuroController.class));
			}
		};
		if(worldObj!=null)r.run();
		else TickEvents.nextTick(r);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		if(getController()!=null)compound.setLong(NBT_CONTROLLER_TAG, getController().getPos().toLong());
		return super.writeToNBT(compound);
	}
	
	@Override
	public TileEntityNeuroController getController(){
		return controller;
	}
	
	@Override
	public void setController(TileEntityNeuroController controller){
		if(this.controller!=controller)UpdateTileNBTPacket.markForSync(this);
		this.controller=controller;
	}


	@Override
	public void update(){
		
	}
	
	
}
