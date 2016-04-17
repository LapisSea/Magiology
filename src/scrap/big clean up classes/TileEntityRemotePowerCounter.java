package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;

public class TileEntityRemotePowerCounter extends TileEntityM implements ITickable{
	
	public Block block;
	public TileEntity tile1;
	public double powerBar;
	public int maxPB,currentP;
	BlockPos pos1;
	
	@Override
	public void update(){
		boolean okBlock=true;
		int metadata=U.getBlockMetadata(worldObj, pos);
		pos1=SideUtil.offsetNew(metadata, pos);
		
		block=U.getBlock(worldObj, pos);
		tile1=worldObj.getTileEntity(pos);
		
		
		{
			if(tile1 instanceof TileEntityPow){
				powerBar=(float)((TileEntityPow)tile1).currentEnergy/(float)((TileEntityPow)tile1).maxEnergyBuffer;
				currentP=((TileEntityPow)tile1).currentEnergy;
				maxPB=((TileEntityPow)tile1).maxEnergyBuffer;
			}else if(tile1 instanceof TileEntityBigFurnaceCore){
				powerBar=(float)((TileEntityBigFurnaceCore)tile1).currentEnergy/(float)((TileEntityBigFurnaceCore)tile1).maxEnergyBuffer;
				
				currentP=((TileEntityBigFurnaceCore)tile1).currentEnergy;
				maxPB=((TileEntityBigFurnaceCore)tile1).maxEnergyBuffer;
			}else if(tile1 instanceof TileEntityBFCPowerOut){
				BlockPos pos1=pos.add(0, 10000, 0);
					
					 if(((TileEntityBFCPowerOut)tile1).CallDir[0]!=null)pos1.add(-2,-1, 0);
				else if(((TileEntityBFCPowerOut)tile1).CallDir[1]!=null)pos1.add( 2,-1, 0);
				else if(((TileEntityBFCPowerOut)tile1).CallDir[2]!=null)pos1.add( 0,-1,-2);
				else if(((TileEntityBFCPowerOut)tile1).CallDir[3]!=null)pos1.add( 0,-1, 2);
				
				if(pos1.getY()>1000){
					tile1= worldObj.getTileEntity(pos1);
					powerBar=(float)((TileEntityBigFurnaceCore)tile1).currentEnergy/(float)((TileEntityBigFurnaceCore)tile1).maxEnergyBuffer;
					currentP=((TileEntityBigFurnaceCore)tile1).currentEnergy;
					maxPB=((TileEntityBigFurnaceCore)tile1).maxEnergyBuffer;
				}
			}else if(tile1 instanceof TileEntityBateryGeneric){
				powerBar=(float)((TileEntityBateryGeneric)tile1).currentEnergy/(float)((TileEntityBateryGeneric)tile1).maxEnergyBuffer;
				currentP=((TileEntityBateryGeneric)tile1).currentEnergy;
				maxPB=((TileEntityBateryGeneric)tile1).maxEnergyBuffer;
			}else if(tile1 instanceof TileEntityFireLamp){
				powerBar=(float)((TileEntityFireLamp)tile1).currentEnergy/(float)((TileEntityFireLamp)tile1).maxEnergyBuffer;
				currentP=((TileEntityFireLamp)tile1).currentEnergy;
				maxPB=((TileEntityFireLamp)tile1).maxEnergyBuffer;
			}else if(tile1 instanceof TileEntityRemotePowerCounter){
				powerBar=(float)((TileEntityRemotePowerCounter)tile1).powerBar;
				currentP=((TileEntityRemotePowerCounter)tile1).currentP;
				maxPB=((TileEntityRemotePowerCounter)tile1).maxPB;
			}
			else okBlock=false;
			
			if(okBlock==false){
				currentP=0;
				maxPB=0;
			}
			
			if(powerBar>0)powerBar+=-0.01;
			
		}
		
	}
	
	
}
