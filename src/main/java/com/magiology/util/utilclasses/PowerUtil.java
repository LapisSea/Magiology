package com.magiology.util.utilclasses;

import static com.magiology.api.power.PowerCore.*;

import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.PowerCore;
import com.magiology.api.power.PowerProducer;
import com.magiology.api.power.PowerUpgrades;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile.UpdateablePipeHandler;
import com.magiology.util.utilobjects.NBTUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;


public class PowerUtil{
	
	public static class PowerItemUtil{
		public static boolean getDataTypeB(ItemStack stack, String key){
			return hasData(stack)?stack.getTagCompound().getBoolean(SAVE_TO_ITEM_PREFIX+key):false;
		}
		//-------------------------------------------------------------------------------------------
		//helpers------------------------------------------------------------------------------------
		public static int getDataTypeI(ItemStack stack, String key){
			return hasData(stack)?stack.getTagCompound().getInteger(SAVE_TO_ITEM_PREFIX+key):0;
		}
		public static int getFuel(ItemStack stack){
			return getDataTypeI(stack, "fuel");
		}
		public static int getMaxEnergy(ItemStack stack){
			return getDataTypeI(stack, "energyMax");
		}
		public static int getMaxFuel(ItemStack stack){
			return getDataTypeI(stack, "fuelMax");
		}
		public static int getMaxTransfer(ItemStack stack){
			return getDataTypeI(stack, "transferMax");
		}
		public static int getMiddleTransfer(ItemStack stack){
			return getDataTypeI(stack, "transferMiddle");
		}
		public static int getMinTransfer(ItemStack stack){
			return getDataTypeI(stack, "transferMin");
		}
		public static int getPower(ItemStack stack){
			return getDataTypeI(stack, "energy");
		}
		public static boolean getPowerKeptOnWrench(ItemStack stack){
			return getDataTypeB(stack, "transferMin");
		}
		//core needed in any situation---------------------------------------------------------------
		public static boolean hasData(ItemStack stack){
			return stack.hasTagCompound()&&stack.getTagCompound().hasKey(SAVE_TO_ITEM_PREFIX);
		}
		public static boolean hasDataType(ItemStack stack, String key){
			return hasData(stack)&&stack.getTagCompound().hasKey(SAVE_TO_ITEM_PREFIX+key);
		}
		//-------------------------------------------------------------------------------------------
		//convenient methods that you will actually use----------------------------------------------
		public static void markWithData(ItemStack stack){
			if(!hasData(stack)){
				NBTUtil.createNBT(stack);
				stack.getTagCompound().setBoolean(SAVE_TO_ITEM_PREFIX, true);
			}
		}
		public static void setDataType(ItemStack stack, String key, boolean data){
			if(hasData(stack))stack.getTagCompound().setBoolean(SAVE_TO_ITEM_PREFIX+key, data);
		}
		public static void setDataType(ItemStack stack, String key, int data){
			if(hasData(stack))stack.getTagCompound().setInteger(SAVE_TO_ITEM_PREFIX+key, data);
		}
		//-------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------
		public static void setEssencialPow(ItemStack stack, PowerCore value){
			setPower(stack, value.getEnergy());
			setMaxTransfer(stack, value.getMaxTSpeed());
			setMiddleTransfer(stack, value.getMiddleTSpeed());
			setMinTransfer(stack, value.getMinTSpeed());
			setMaxEnergy(stack, value.getMaxEnergy());
			setPowerKeptOnWrench(stack, value.isPowerKeptOnWrench());
		}
		public static void setEssencialPowGen(ItemStack stack, PowerProducer value){
			setFuel(stack, value.getFuel());
			setMaxFuel(stack, value.getMaxFuel());
		}
		//-------------------------------------------------------------------------------------------
		public static void setFuel(ItemStack stack, int value){
			setDataType(stack, "fuel", value);
		}
		public static void setMaxEnergy(ItemStack stack, int value){
			setDataType(stack, "energyMax", value);
		}
		public static void setMaxFuel(ItemStack stack, int value){
			setDataType(stack, "fuelMax", value);
		}
		public static void setMaxTransfer(ItemStack stack, int value){
			setDataType(stack, "transferMax", value);
		}
		public static void setMiddleTransfer(ItemStack stack, int value){
			setDataType(stack, "transferMiddle", value);
		}
		public static void setMinTransfer(ItemStack stack, int value){
			setDataType(stack, "transferMin", value);
		}
		public static void setPower(ItemStack stack, int value){
			setDataType(stack, "energy", value);
		}
		public static void setPowerKeptOnWrench(ItemStack stack, boolean value){
			setDataType(stack, "transferMin", value);
		}
	}
	//	adds power
	public static void add(int amount, PowerCore tile){
		if(tile!=null)tile.addEnergy(amount);
		else msg();
	}
	public static boolean canISidedPowerSendFromTo(ISidedPower fromTile,ISidedPower toTile,int sideOfSending){
		if(sideOfSending>=0&&sideOfSending<=6);else{PrintUtil.println("THE GIVEN SIDE IS INVALID!\nPLEASE ENTER A SIDE FROM 0-6!\n----------**********----------");return false;}
		return fromTile.getOut(sideOfSending)&&toTile.getIn(SideUtil.getOppositeSide(sideOfSending));
	}
	public static void cricleSideInteraction(ISidedPower iSidedPower,int side){
		boolean[] data=getNextCricleSideInteraction(iSidedPower, side);
		iSidedPower.setReceaveOnSide(side,data[0]);
		iSidedPower.setSendOnSide(side,data[1]);
		if(iSidedPower instanceof TileEntity)UpdateablePipeHandler.updatein3by3(((TileEntity)iSidedPower).getWorld(), ((TileEntity)iSidedPower).getPos());
	}
	public static float getFuelPrecentage(Object object){
		if(object instanceof PowerProducer);else return 0;
		PowerProducer obj=(PowerProducer)object;
		return (float)obj.getFuel()/(float)obj.getMaxFuel();
	}
	public static int getHowMuchToSendFromToForDrain(Object fromTile,Object toTile){
		int result=-1;
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			PowerCore framTile=(PowerCore) fromTile,taTile=(PowerCore) toTile;
			int sender=-1,target=-1;
			
			sender=framTile.getEnergy();//hey I want to send everything to you
			target=taTile.getMaxEnergy()-taTile.getEnergy();//OK :) but here is how much I can get if I can do it
			
			int abc=Math.min(sender, target);
			result=Math.min(getMaxSpeed(fromTile, toTile), abc);
		}else msg();
		if(result<0)result=0;
		return result;
	}
	public static int getHowMuchToSendFromToForEquate(Object fromTile,Object toTile){
		int result=-1;
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			PowerCore framTile=(PowerCore) fromTile,taTile=(PowerCore) toTile;
			int sender=-1,target=-1;
			
			sender=(int)((framTile.getEnergy()-taTile.getEnergy())/2.0);//hey I want to be equal with you
			target=taTile.getMaxEnergy()-taTile.getEnergy();//OK :) but here is how much I can get if I can do it
			
			int abc=Math.min(sender, target);
			 result=Math.min(getMaxSpeed(fromTile, toTile), abc);
		}else msg();
		return result;
	}
	public static int getMaxSpeed(Object tile1,Object tile2){
		int result=-1;
		if(tile1 instanceof PowerCore&&tile2 instanceof PowerCore){
			result=Math.max(((PowerCore)tile1).getMaxTSpeed(), ((PowerCore)tile2).getMaxTSpeed());
		}else msg();
		return result;
	}
	public static int getMiddleSpeed(Object tile1,Object tile2){
		int result=-1;
		if(tile1 instanceof PowerCore&&tile2 instanceof PowerCore){
			result=Math.max(((PowerCore)tile1).getMiddleTSpeed(), ((PowerCore)tile2).getMiddleTSpeed());
		}else msg();
		return result;
	}
	public static int getMinSpeed(Object tile1,Object tile2){
		int result=-1;
		if(tile1 instanceof PowerCore&&tile2 instanceof PowerCore){
			result=Math.max(((PowerCore)tile1).getMinTSpeed(), ((PowerCore)tile2).getMinTSpeed());
		}else msg();
		return result;
	}
	public static boolean[] getNextCricleSideInteraction(ISidedPower iSidedPower,int side){
		boolean[] result=new boolean[2];
		boolean
		allowedRec=iSidedPower.getAllowedReceaver(side),
		allowedSend=iSidedPower.getAllowedSender(side);
		
		if(!allowedRec&&!allowedSend)return new boolean[]{false,false};
		
		boolean[] inOut=new boolean[]{iSidedPower.getIn(side),iSidedPower.getOut(side)};
		
		
		if(allowedRec&&!allowedSend){
			if(inOut[0]==false){
				result[0]=true;
				result[1]=false;
			}else{
				result[0]=false;
				result[1]=false;
			}
			return result;
		}
		if(!allowedRec&&allowedSend){
			if(inOut[1]==false){
				result[0]=false;
				result[1]=true;
			}else{
				result[0]=false;
				result[1]=false;
			}
			return result;
		}
		
		
		if(inOut[0]==true&&inOut[1]==true){
			result[0]=false;
			result[1]=true;
		}else if(inOut[0]==false&&inOut[1]==true){
			result[0]=true;
			result[1]=false;
		}else if(inOut[0]==true&&inOut[1]==false){
			result[0]=false;
			result[1]=false;
		}else if(inOut[0]==false&&inOut[1]==false){
			result[0]=true;
			result[1]=true;
		}
		
		//result[0]=ReceaveOnSide
		//result[1]=SendOnSide
		return result;
	}
	public static float getPowerPrecentage(Object object){
		if(object instanceof PowerCore){
			PowerCore obj=(PowerCore)object;
			return (float)obj.getEnergy()/(float)obj.getMaxEnergy();
		}
		if(object instanceof ItemStack){
			ItemStack stack=(ItemStack)object;
			NBTTagCompound NBT=stack.getTagCompound();
			if(stack.hasTagCompound()&&NBT.hasKey(PowerCore.SAVE_TO_ITEM_PREFIX)){
				try{
					return (float)NBT.getInteger(PowerCore.SAVE_TO_ITEM_PREFIX+"energy")/(float)NBT.getInteger(PowerCore.SAVE_TO_ITEM_PREFIX+"energyMax");
				}catch(Exception e){}
			}
		}
		return 0;
	}
public static boolean isObjectPowerd(Object object){return object instanceof PowerCore;}
public static boolean isObjectPowerd3DBlock(Object object){return isObjectPowerd(object)&&object instanceof ISidedPower;}
public static boolean isObjectPowerdGenerator(Object object){return object instanceof PowerProducer;}
public static boolean isObjectUpgradeablePower(Object object){return object instanceof PowerUpgrades;}
//	raw move from tile to tile
	public static boolean moveFromTo(PowerCore fromTile,PowerCore toTile,int amount,int sideOfSender){
		//if the object's are iSided than check if they can interact on side if not do the transfer
		boolean var1=false;
		if(fromTile instanceof ISidedPower&&toTile instanceof ISidedPower)var1=canISidedPowerSendFromTo((ISidedPower)fromTile, (ISidedPower)toTile, sideOfSender);
		else var1=true;
		if(!var1){
//			if(fromTile instanceof TileEntity&&Helper.RInt(200)==0){
//				boolean fromTileB=((TileEntityPow)fromTile).getOut(sideOfSender),toTileB=((TileEntityPow)toTile).getIn(SideHelper.getOppositeSide(sideOfSender));
////				Helper.printInln(fromTileB,toTileB);
//				if(fromTileB)Helper.spawnEntityFX(new EntityFlameFXM(((TileEntityPow)fromTile).getWorld(), 0.5+((TileEntityPow)fromTile).x(), 0.5+((TileEntityPow)fromTile).y(), 0.5+((TileEntityPow)fromTile).z(), 0, 0.1, 0));
//				if(toTileB)Helper.spawnEntityFX(new EntityFlameFXM(((TileEntityPow)toTile).getWorld(), 0.5+((TileEntityPow)toTile).x(), 0.5+((TileEntityPow)toTile).y(), 0.5+((TileEntityPow)toTile).z(), 0, 0.1, 0));
//			}
			return false;
		}
		//-----------------------------------------------------------------------------------------
		if(fromTile!=null&&toTile!=null){
			subtract(amount,fromTile);
				 add(amount,toTile);
			return true;
		}
		else msg();
		return false;
	}
//	if target or sender is not PowerCore
	private static void msg(){PrintUtil.println("YOU HAVE TO ADD 'Object' THAT IMPLEMENTS THE 'PowerCore' INTERFACE!");}
	
	public static void sortSides(ISidedPower iSidedPower){
		for(int a=0;a<6;a++){
			if(iSidedPower.getIn(a)&&!iSidedPower.getAllowedReceaver(a))iSidedPower.setAllowedReceaver(false, a);
			if(iSidedPower.getOut(a)  &&!iSidedPower.getAllowedSender(a))iSidedPower.setAllowedSender  (false, a);
		}
	}
	//	subtract power
	public static void subtract(int amount, PowerCore tile){
		if(tile!=null)tile.subtractEnergy(amount);
		else msg();
	}
	//	function to use
	public static boolean tryToDrainFromTo(Object fromTile,Object toTile,int amount, int side){
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			PowerCore tileFrom=(PowerCore)fromTile;
			PowerCore tileTo=(PowerCore)toTile;
			if(tileFrom.getEnergy()>=amount&&tileTo.getEnergy()+amount<=tileTo.getMaxEnergy()){
				boolean var1=moveFromTo(tileFrom, tileTo, amount,side);
				return amount>0&&var1;
			}
		}
		else msg();
		return false;
	}
	
	//	function to use
	public static boolean tryToEquateEnergy(Object fromTile,Object toTile,int amount, int sideOfSender){
		if(fromTile instanceof PowerCore&&toTile instanceof PowerCore){
			PowerCore tileFrom=(PowerCore)fromTile,tileTo=(PowerCore)toTile;
			if( tileTo.getEnergy()+amount<=tileTo.getMaxEnergy()&&// so target can't get more than it can store
				tileFrom.getEnergy()>=tileTo.getEnergy()+amount&&// so sender wont send if they have equal energy
				tileFrom.getEnergy()>=amount//---------------------------- so sender can't send more than it has
				){
				return moveFromTo(tileFrom, tileTo, amount,sideOfSender);
			}else{
//				switches sender and target
				PowerCore a=tileFrom;
				tileFrom=tileTo;
				tileTo=a;
				
				if(
					tileTo.getEnergy()+amount<=tileTo.getMaxEnergy()&&// so target can't get more than it can store
					tileFrom.getEnergy()>=tileTo.getEnergy()+amount&&// so sender wont send if they have equal energy
					tileFrom.getEnergy()>=amount//---------------------------- so sender can't send more than it has
					){
					return moveFromTo(tileFrom, tileTo, amount,sideOfSender);
				}
			}
		}
		else msg();
		return false;
	}
}
