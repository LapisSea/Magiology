package com.magiology.api.network;

import java.util.List;
import java.util.Map;

import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface NetworkInterface{
	
	public long getActiveCard();
	public TileEntityNetworkController getBrain();
	public Map<String,Object> getData();
	public TileEntity getHost();
	public Object getInteractData(String string);
	public WorldNetworkInterface getInterfaceProvider();
	public List<TileEntityNetworkRouter> getPointerContainers();
	public List<ItemStack> getPointers();
	public  void  setInteractData(String string,Object object);
	
}
