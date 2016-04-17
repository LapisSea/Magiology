package com.magiology.mcobjects.tileentityes.corecomponents.powertiles;

import java.util.List;

import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.PowerCore;
import com.magiology.api.power.PowerUpgrades;
import com.magiology.mcobjects.tileentityes.TileEntityBateryGeneric;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.mcobjects.tileentityes.corecomponents.TileEntityConnectionProvider;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.PowerUtil.PowerItemUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.NBTUtil;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityPow extends TileEntityConnectionProvider implements ISidedPower,PowerUpgrades,ITickable{
	public ItemStack[] containerItems=null;
	public int currentEnergy=0,maxTSpeed=0,middleTSpeed=0,minTSpeed=1,maxEnergyBuffer=0;
	public int FirstSide=-1;
	
	public boolean
		keepsPropertiesOnPickup=false,
		hasPriorityUpg=false;
	public int NumberOfContainerSlots=0;
	
	public TileEntityPow(boolean[] changeableByUser,boolean[] sidedPower,int minTSpeed, int middleTSpeed, int maxTSpeed, int maxEnergyBuffer){
		if(changeableByUser!=null)for(int i=0;i<6;i++){
			connections[i].setInEnabled(changeableByUser[i]);
			connections[i].setOutEnabled(changeableByUser[i+6]);
		}
		if(sidedPower!=null)for(int i=0;i<6;i++){
			connections[i].setIn(sidedPower[i]);
			connections[i].setOut(sidedPower[i+6]);
		}
		this.minTSpeed=minTSpeed;
		this.middleTSpeed=middleTSpeed;
		this.maxTSpeed=maxTSpeed;
		this.maxEnergyBuffer=maxEnergyBuffer;
		
		NumberOfContainerSlots=5;
		setUpgradeSlots();
	}
	
	@Override
	public void addEnergy(int amount){currentEnergy+=amount;}
	
	public boolean canAcceptThatMuchEnergy(int amout){
		return (maxEnergyBuffer-amout>=currentEnergy?true:false);
	}
	
	
	/**
	 * @param side : transfer energy to specific side
	 * @param type : send to something is 1 and send out itself is 0
	 */
	public void doCustomSidedPowerTransfer(int side,int type){
		worldObj.getTileEntity(SideUtil.offsetNew(side,pos));
		TransferEnergyToPosition(SideUtil.offsetNew(side,pos), type,side);
	}
	
	@Override
	public boolean getAllowedReceaver(int id){
		return connections[id].getInEnabled();
	}
	

	@Override
	public boolean getAllowedSender(int id){
		return connections[id].getOutEnabled();
	}
	
	@Override
	public boolean getBannedSide(int id){
		return connections[id].isBanned();
	}
	
	@Override
	public ItemStack[] getcontainerItems(){
		return containerItems;
	}
	@Override
	public int getEnergy(){
		return currentEnergy;
	}
	
	@Override
	public <T extends TileEntity> boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return false;
	}
	@Override
	public boolean getIn(int direction){
		boolean in=connections[direction].getIn(),out=connections[direction].getOut(),inOut=connections[direction].getMain();
		if(inOut&&!in&&!out)return true;
		if(in)return true;
		return false;
	}
	@Override
	public int getMaxEnergy(){
		return maxEnergyBuffer;
	}
	@Override
	public int getMaxTSpeed(){
		return maxTSpeed;
	}
	@Override
	public int getMiddleTSpeed(){
		return middleTSpeed;
	}
	@Override
	public int getMinTSpeed(){
		return minTSpeed;
	}
	@Override
	public int getNumberOfContainerSlots(){
		return NumberOfContainerSlots;
	}
	@Override
	public boolean getOut(int direction){
		boolean in=connections[direction].getIn(),out=connections[direction].getOut(),inOut=connections[direction].getMain();
		if(inOut&&!in&&!out)return true;
		if(out)return true;
		return false;
	}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		
	}
	/**
	 * EPICLY POWERFUL AND IMPORTANT MLG FUNCTION! GET DA CAMERA!!!!
	 */
	public void handleStandardPowerTransmission(boolean isRepeatable){
		//Get a random order of sides that are going to be done
		int[] randSides=SideUtil.randomizeSides();
		
		//Set the first loop of sending power to a specific side if there is one
		if(hasPriorityUpg){
			int pos=-1;
			for(int a=0;a<randSides.length;a++)if(randSides[a]==FirstSide){
				pos=a;
				continue;
			}if(pos!=0){
				int first=randSides[0];
				int oneThatShouldBeFirst=randSides[pos];
				randSides[0]=oneThatShouldBeFirst;
				randSides[pos]=first;
			}
		}
		//try to send/receive power from all sides
		for (int i=0;i<randSides.length;i++){
			int side=randSides[i];
			TileEntity ab=worldObj.getTileEntity(SideUtil.offsetNew(side,pos));
			//if there is nothing to interact with than skip the process (only for optimization)
			boolean var1=ab instanceof PowerCore&&ab instanceof ISidedPower;
			//Is next to a special pipe
			boolean var2=false;
			if(var1&&this instanceof TileEntityFirePipe){
				//special interaction for pipes that contains Priority upgrade
				if(hasPriorityUpg){
					TileEntity a=worldObj.getTileEntity(SideUtil.offsetNew(randSides[0],pos));
					TileEntityPow tile=a instanceof TileEntityPow?(TileEntityPow)a:null;
					if(tile!=null){
						if(i!=0)var1=false;
						if((float)tile.currentEnergy/(float)tile.maxEnergyBuffer>0.9)var1=true;
					}
				}
				//special interaction for pipes that are next to a pipe that contains Priority upgrade
				else{
					TileEntityFirePipe tile=ab instanceof TileEntityFirePipe?(TileEntityFirePipe)ab:null;
					if(tile!=null&&tile.hasPriorityUpg){
						if(SideUtil.offsetNew(tile.FirstSide,tile.pos).equals(pos)){
							var1=false;
							var2=true;
						}
					}
				}
			}
			if(var1&&connections[side].hasForce()){
				if(connections[side].getMain()&&!connections[side].getIn()&&!connections[side].getOut()){
					doCustomSidedPowerTransfer(side,ab instanceof TileEntityFirePipe?(var2?-1:(hasPriorityUpg&&FirstSide==side?1:-1)):0);
					if(isRepeatable){
						TileEntity tile=worldObj.getTileEntity(SideUtil.offsetNew(side,pos));
						if(tile instanceof TileEntityFirePipe)((TileEntityFirePipe)tile).power(false);
					}
				}else{
					if(connections[side].getIn())doCustomSidedPowerTransfer(side,0);
					else if(connections[side].getOut())doCustomSidedPowerTransfer(side,1);
				}
			}
		}
	}
	public boolean isAnyBatery(BlockPos pos){
		boolean return1=false;
		TileEntity tile=worldObj.getTileEntity(pos);
		
		if(tile instanceof TileEntityBateryGeneric){
			return1=true;
		}
		
		return return1;
	}
	@Override
	public boolean isPowerKeptOnWrench(){
		return true;
	}
	@Override
	public boolean isSavingFullNBT(){
		return true;
	}
	public boolean isTPipe(BlockPos pos){
		return worldObj.getTileEntity(pos)instanceof TileEntityFirePipe;
	}
	@Override
	public boolean isUpgradeInInv(Item item){
		for(int a=0;a<NumberOfContainerSlots;a++){
			if(UtilM.isItemInStack(item, containerItems[a]))return true;
		}
		return false;
	}
	@Override
	public void readFromItemOnPlace(ItemStack stack){
		setEnergy(PowerItemUtil.getPower(stack));
	}
	@Override
	public void readFromNBT(NBTTagCompound NBTTC){
		super.readFromNBT(NBTTC);
		currentEnergy = NBTTC.getInteger("energy");
		
		containerItems=new ItemStack[NumberOfContainerSlots];
		containerItems=NBTUtil.loadItemsFromNBT(NBTTC, "TEMT", containerItems);
	}
	@Override
	public void setAllowedReceaver(boolean Boolean, int id){
		connections[id].setInEnabled(Boolean);
	}
	@Override
	public void setAllowedSender(boolean Boolean, int id){
		connections[id].setOutEnabled(Boolean);
	}
	@Override
	public void setBannedSide(boolean Boolean, int id){
		connections[id].setBanned(Boolean);
	}
	@Override
	public void setcontainerItems(ItemStack[] containerItems){
		this.containerItems=containerItems;
	}
	@Override
	public void setEnergy(int Int){
		currentEnergy=Int;
	}
	@Override
	public void setMaxEnergyBuffer(int Int){
		maxEnergyBuffer=Int;
	}
	
	@Override
	public void setMaxTSpeed(int Int){
		maxTSpeed=Int;
	}
	@Override
	public void setMiddleTSpeed(int Int){
		middleTSpeed=Int;
	}
	@Override
	public void setMinTSpeed(int Int){
		minTSpeed=Int;
	}
	@Override
	public void setNumberOfContainerSlots(int Int){
		NumberOfContainerSlots=Int;
	}
	@Override
	public void setReceaveOnSide(int direction, boolean bolean){
		connections[direction].setIn(bolean);
	}
	@Override
	public void setSendOnSide(int direction, boolean bolean){
		connections[direction].setOut(bolean);
	}
	
	public void setUpgradeSlots(){
		if(NumberOfContainerSlots>0)containerItems=new ItemStack[NumberOfContainerSlots];
	}
	
	@Override
	public void subtractEnergy(int amount){currentEnergy-=amount;}
	
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param type : send to something is 1 and send out itself is 0
	 * @param side
	 */
	public void TransferEnergyToPosition(BlockPos pos1,int type, int side){
		TileEntity tileEn=worldObj.getTileEntity(pos1);
		getEnergy();
		if(type==-1&&tileEn instanceof TileEntityFirePipe){
			for(int l=0;l<20;l++){
				PowerUtil.tryToEquateEnergy(this, tileEn, PowerUtil.getMaxSpeed(tileEn, this),side);
				PowerUtil.tryToEquateEnergy(this, tileEn, PowerUtil.getMiddleSpeed(tileEn, this),side);
				PowerUtil.tryToEquateEnergy(this, tileEn, PowerUtil.getMinSpeed(tileEn, this),side);
			}
		}
		else if(tileEn instanceof TileEntityPow){
			switch(type){
			case 0:{
				side=SideUtil.getOppositeSide(side);
				for(int l=0;l<20;l++){
					PowerUtil.tryToDrainFromTo(tileEn, this, PowerUtil.getMaxSpeed(tileEn, this),side);
					PowerUtil.tryToDrainFromTo(tileEn, this, PowerUtil.getMiddleSpeed(tileEn, this),side);
					PowerUtil.tryToDrainFromTo(tileEn, this, PowerUtil.getMinSpeed(tileEn, this),side);
				}
			}break;
			case 1:{
				for(int l=0;l<20;l++){
					PowerUtil.tryToDrainFromTo(this, tileEn, PowerUtil.getMaxSpeed(tileEn, this),side);
					PowerUtil.tryToDrainFromTo(this, tileEn, PowerUtil.getMiddleSpeed(tileEn, this),side);
					PowerUtil.tryToDrainFromTo(this, tileEn, PowerUtil.getMinSpeed(tileEn, this),side);
				}
			}break;
			}
		}
		/**Power visual debug (shows path-finding in particles)*/
//		if(Math.abs(i-getEnergy())>0&&Helper.RInt(25)==0){
//			EntityMovingParticleFX p=new EntityMovingParticleFX(getWorld(), pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+1.5,tileEn.getPos().getX()+0.5, tileEn.getPos().getY()+0.5, tileEn.getPos().getZ()+1.5, 100+Math.abs(i-getEnergy()), type==1?1:0.2, type==0?1:0.2, type==-1?1:0.2,0.4);
//			p.noClip=true;
//			Helper.spawnEntityFX(p);
//		}
	}
	@Override
	public void writeToItemOnWrenched(ItemStack stack){
		PowerItemUtil.setEssencialPow(stack, this);
	}
	@Override
	public void writeToNBT(NBTTagCompound NBTTC){
		super.writeToNBT(NBTTC);
		NBTTC.setInteger("energy", currentEnergy);
		if(containerItems!=null){
			NBTUtil.saveItemsToNBT(NBTTC, "TEMT", containerItems);
		}
	}
}
