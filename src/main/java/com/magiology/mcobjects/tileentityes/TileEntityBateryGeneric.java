package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.SideUtil;

import net.minecraft.util.math.BlockPos;

public class TileEntityBateryGeneric extends TileEntityPow{
	
	
	
	public TileEntityBateryGeneric(boolean[] allowedSidedPower,boolean[] sidedPower, int minTSpeed, int middleTSpeed,int maxTSpeed, int maxEnergyBuffer) {
		super(allowedSidedPower, sidedPower, minTSpeed, middleTSpeed, maxTSpeed,maxEnergyBuffer);
	}
	
	public void fromBateryToBatery(){
		int[] s=SideUtil.randomizeSides();
		for(int a=0;a<6;a++){
			int side=s[a];
			BlockPos pos1=SideUtil.offsetNew(side,pos);
			if(this.isAnyBatery(pos1)){
				TileEntityBateryGeneric tile= (TileEntityBateryGeneric) worldObj.getTileEntity(pos1);
				
				PowerUtil.tryToEquateEnergy(this, tile, PowerUtil.getMaxSpeed(this, tile),side);
				PowerUtil.tryToEquateEnergy(this, tile, PowerUtil.getMiddleSpeed(this, tile),side);
				PowerUtil.tryToEquateEnergy(this, tile, PowerUtil.getMinSpeed(this, tile),side);
			}
			
		}
	}

	public void power(){
		fromBateryToBatery();
	}
	
	@Override
	public void update(){
		this.power();
		PowerUtil.sortSides(this);
	}

	@Override
	public void updateConnections(){
		UpdateablePipeHandler.onConnectionUpdate(this);
	}
}
