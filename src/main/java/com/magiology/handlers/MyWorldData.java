package com.magiology.handlers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class MyWorldData extends WorldSavedData{

	final static String key = "my.unique.string";
	public static MyWorldData forWorld(World world){
				// Retrieves the MyWorldData instance for the given world, creating it if necessary
		MapStorage storage = world.getPerWorldStorage();
		MyWorldData result = (MyWorldData)storage.loadData(MyWorldData.class, key);
		if (result == null) {
			result = new MyWorldData(key);
			storage.setData(key, result);
		}
		return result;
	}

	// Fields containing your data here
	
	public MyWorldData(String name){
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// Get your data from the nbt here
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		// Put your data in the nbt here
	}

}