package com.magiology.mcobjects.tileentityes;

import static com.magiology.api.power.SixSidedBoolean.Modifier.*;
import static com.magiology.util.utilclasses.SideUtil.*;

import com.magiology.api.power.SixSidedBoolean;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPowGen;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilobjects.SlowdownUtil;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFireLamp extends TileEntityPowGen{
	
	EffectRenderer efr=Get.Render.ER();
	public boolean isWorking=false;
	
	
	SlowdownUtil optimizer=new SlowdownUtil(20),optimizer2=new SlowdownUtil(4), optimizer3=new SlowdownUtil(3);

	@Override
	public void writeToNBT(NBTTagCompound NBTTC){
		super.writeToNBT(NBTTC);
		NBTTC.setInteger("FT", fuel);
		NBTTC.setInteger("MFT", maxFuel);
	}
	@Override
	public void readFromNBT(NBTTagCompound NBTTC){
		super.readFromNBT(NBTTC);
		fuel=NBTTC.getInteger("FT");
		maxFuel=NBTTC.getInteger("MFT");
	}
	
	public TileEntityFireLamp(){
		super(SixSidedBoolean.create(First6True,Include,DOWN(),Last6True,Include,UP()).sides,SixSidedBoolean.lastGen.sides.clone(), 1, 2, 5, 115, 100);
	}
	
	@Override
	public boolean canGeneratePowerAddon(){
		return isWorking&&fuel>0;
	}
	@Override
	public void onGenerateEnergy(){
		currentEnergy+=3;
		fuel--;
	}
	
	
	@Override
	public boolean isPowerKeptOnWrench(){
		return true;
	}
	
	
	public void power(){
		
		int i=getEnergy();
		handleStandardPowerTransmission(true);
		if(i!=getEnergy())if(worldObj.isRemote&&optimizer3.progress==0){
		}
		
		if(canGeneratePower(3))onGenerateEnergy();
		
		tryToSendEnergyToObj();
	}
	
	@SideOnly(Side.CLIENT)
	public void spawnPaticle(boolean isforced){
		if(!worldObj.isRemote)return;
		if(optimizer3.isTimeWithAddProgress())return;
		
		if(canGeneratePower(3)||isforced){
		}
		
		
	}
	
	public void tryToSendEnergyToObj(){
		TileEntity aa=worldObj.getTileEntity(pos.add(0,1,0));
		if(aa instanceof TileEntityFirePipe){
			TileEntityFirePipe pipe= (TileEntityFirePipe)aa;
			if(PowerUtil.tryToDrainFromTo(this, pipe, PowerUtil.getHowMuchToSendFromToForDrain(this, pipe),EnumFacing.UP.getIndex()))if(worldObj.isRemote&&optimizer3.progress==0){
			}
		}
	}

	@Override
	public void update(){
		
		
		if(isWorking){
			power();
			spawnPaticle(false);
		}
		
		updateConnections();
		
		PowerUtil.sortSides(this);
	}

	@Override
	public void updateConnections(){
		UpdateablePipeHandler.onConnectionUpdate(this);
		TileEntity tile=worldObj.getTileEntity(pos.add(0, -1, 0));
		if(tile instanceof TileEntityFirePipe)connections[1].setMain(((TileEntityFirePipe)tile).connections[0].getMain());
		else connections[1].setMain(false);
		connections[1].setForce(true);
	}
}