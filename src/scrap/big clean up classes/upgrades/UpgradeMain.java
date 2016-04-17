package com.magiology.mcobjects.items.upgrades;

public final class UpgradeMain{
	
	public static int ItemTypeID=0,arrayIndex=0;
	static boolean[]first={true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
	
	
	public static int nextItemTypeID(){
		if(first[0]){first[0]=false;return 0;}
		else ItemTypeID++;
		return ItemTypeID;
	}
	public static int nextarrayIndex(){
		if(first[1]){first[1]=false;return 0;}
		else arrayIndex++;
		return arrayIndex;
	}
	
}
