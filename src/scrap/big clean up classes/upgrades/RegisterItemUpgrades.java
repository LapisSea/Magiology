package com.magiology.mcobjects.items.upgrades;

import java.util.ArrayList;

import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class RegisterItemUpgrades{
	
	public static enum Container{
		FirePipe(UpgradeType.Control,UpgradeType.Speed,UpgradeType.Priority,UpgradeType.Capacity),
		FireGen(UpgradeType.Speed,UpgradeType.Range,UpgradeType.Capacity),
		Machine(UpgradeType.Speed,UpgradeType.Range,UpgradeType.Capacity),
		ControlB(UpgradeType.Capacity),
		TeleTransfer(UpgradeType.Capacity,UpgradeType.Speed,UpgradeType.Range),
		Pants42(UpgradeType.Control,UpgradeType.Speed,UpgradeType.Flight,UpgradeType.Capacity,UpgradeType.ThermalVision),
		Helmet42(UpgradeType.Control,UpgradeType.Capacity),
		FireBarrel(UpgradeType.Capacity);
		
		/**Valid upgrade items (1 upgrade x,2 upgrade y,3 upgrade (trlollollollollollol ho ho ho... ho ho ho...))**/
		private final UpgradeType[] ValidType;
		Container(UpgradeType... ValidTypes){ValidType=ValidTypes;}
		public int[] GetValidTypeID(){
			int[] result=new int[ValidType.length];
			for(int a=0;a<result.length;a++)result[a]=ValidType[a].GetTypeID();
			return result;
		}
		public int getNumberOfTypes(){return ValidType.length;}
	}
	
	static int nextUpgradeTypeID=0;
	/**get next upgrade type ID*/
	static int GNUTID(){return nextUpgradeTypeID++;}
	
	public static enum UpgradeType{
		Range(GNUTID()),Speed(GNUTID()),Control(GNUTID()),Flight(GNUTID()),Priority(GNUTID())
		,Capacity(GNUTID()),ThermalVision(GNUTID())
		;
		private final int TypeID;
		UpgradeType(int id){TypeID=id;}
		public int GetTypeID(){return TypeID;}
	}
	
	//items
	/**saves Item for easy data referencing (go to getItemUpgradeID())*/
	private static ArrayList<Item>	regItems=new ArrayList<Item>();
	/**Item type id (type speed,type power...)*/
	private static ArrayList<Integer> itemUpgradeTypeID=new ArrayList<Integer>();
	/**Item Upgrade Level (1 slow, 2 faster, 3 more speed, 4 ULTIMATE PAUWA, 5 I's over 9000!,...)*/
	private static ArrayList<Integer> ItemUpgradeLevel=new ArrayList<Integer>();
	/**Valid upgrade slots (1 pipe upgrade slots,2 some block upgrade slots,3 an item upgrade slots)*/
	private static ArrayList<UpgradeType>itemSlotTypeID=new ArrayList<UpgradeType>();
	
	
	public static void registerItemUpgrades(Item item,int level,UpgradeType ut){
		int arrayIndex=UpgradeMain.nextarrayIndex();
		regItems.add(arrayIndex, item);
		itemUpgradeTypeID.add(arrayIndex, UpgradeMain.nextItemTypeID());
		itemSlotTypeID.add(arrayIndex, ut);
		ItemUpgradeLevel.add(arrayIndex, level);
	}

	/**saves Item for easy data referencing (go to getItemUpgradeID())*/
	public static int getItemUpgradeID(Item item){
		return regItems.indexOf(item);
	}

	/**Item type id (type speed,type power...)*/
	public static int getItemTypeID(int ID){
		return itemUpgradeTypeID.get(ID);
	}
	/**Item Upgrade Level (1 slow, 2 faster, 3 more speed, 4 ULTIMATE PAUWA, 5 I's over 9000!,...)*/
	public static int getItemUpgradeLevel(int ID){
		return ItemUpgradeLevel.get(ID);
	}
	/**Item Upgrade Level (1 slow, 2 faster, 3 more speed, 4 ULTIMATE PAUWA, 5 I's over 9000!,...)*/
	public static int getItemUpgradeLevel(Item item){
		return ItemUpgradeLevel.get(getItemUpgradeID(item));
	}
	/**Item Upgrade Level (Range, Speed,...)*/
	public static UpgradeType getItemUpgradeType(int ID){
		return itemSlotTypeID.get(ID);
	}
	public static int getItemTypesID(int ID){
		return itemSlotTypeID.get(ID).GetTypeID();
	}
	
	public static boolean isUpgradeValid(UpgradeType ut,Container c){
		int[] list=c.GetValidTypeID();
		for(int a=0;a<list.length;a++)if(ut.GetTypeID()==list[a])return true;
		return false;
	}
	public static boolean isItemUpgrade(Item item){
		if(getItemUpgradeID(item)!=-1)return true;
		return false;
	}
	
	public static boolean isTileUpgradeable(TileEntity tile){
		
		if(tile instanceof TileEntityPow){
			TileEntityPow tileMT=(TileEntityPow)tile;
			if(tileMT.containerItems!=null&&tileMT.containerItems.length>0){
				ItemStack[] USlots=tileMT.containerItems;
				int USlotsSize=USlots.length,EmptyUSlots=USlotsSize;
				for(int a=0;a<USlotsSize;a++)if(USlots[a]!=null&&USlots[a].stackSize>0&&isItemUpgrade(USlots[a].getItem()))EmptyUSlots--;
				
				if(EmptyUSlots>0)return true;
			}
		}
		
		
		return false;
	}
}



