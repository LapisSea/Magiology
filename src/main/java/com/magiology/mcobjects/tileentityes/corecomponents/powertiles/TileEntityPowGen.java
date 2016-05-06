package com.magiology.mcobjects.tileentityes.corecomponents.powertiles;

import com.magiology.api.power.PowerProducer;
import com.magiology.util.utilclasses.PowerUtil.PowerItemUtil;

import net.minecraft.item.ItemStack;

public abstract class TileEntityPowGen extends TileEntityPow implements PowerProducer{
	
	public int fuel,maxFuel;
	public TileEntityPowGen(boolean[] allowedSidedPower, boolean[] sidedPower,int minTSpeed, int middleTSpeed, int maxTSpeed, int maxEnergyBuffer, int maxFuel){
		super(allowedSidedPower, sidedPower, minTSpeed, middleTSpeed, maxTSpeed,maxEnergyBuffer);
		this.maxFuel=maxFuel;
	}
	@Override
	public void addFuel(int Int){
		fuel+=Int;
	}
	@Override
	public boolean canGeneratePower(int Add){
		return canGeneratePowerAddon()&&(getMaxEnergy()>=getEnergy()+Add?true:false);
	}
	@Override public abstract boolean canGeneratePowerAddon();
	@Override public abstract void onGenerateEnergy();
	@Override
	public int getFuel(){
		return fuel;
	}
	@Override
	public int getMaxFuel(){
		return maxFuel;
	}
	@Override
	public void readFromItemOnPlace(ItemStack stack){
		super.readFromItemOnPlace(stack);
		setFuel(PowerItemUtil.getFuel(stack));
	}
	@Override
	public void setFuel(int Int){
		fuel=Int;
	}
	@Override
	public void setMaxFuel(int Int){
		maxFuel=Int;
	}

	@Override
	public void subtractFuel(int Int){
		fuel-=Int;
	}
	@Override
	public void writeToItemOnWrenched(ItemStack stack){
		super.writeToItemOnWrenched(stack);
		PowerItemUtil.setEssencialPowGen(stack, this);
	}
}