package com.magiology.mcobjects.tileentityes.network.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.api.lang.ICommandInteract;
import com.magiology.api.network.InterfaceTileEntitySaver;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.SideUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileHologramProjectorInterface implements WorldNetworkInterface,InterfaceTileEntitySaver{
	private static Map<TileEntity,Long> cardList=new HashMap<TileEntity,Long>();
	public static long getCard(TileEntity tileEntity){
		Long long1=cardList.get(tileEntity);
		if(long1!=null)return long1;
		Long id;
		do{
			id=RandUtil.RL();
		}while(cardList.containsValue(id)&&id!=-1&&id!=0&&id!=-2);
		cardList.put(tileEntity, id);
		return id;
	}
	public TileEntity tile;
	
	
	@Override public TileEntity getBoundTile(){return tile;}
	@Override
	public TileEntityNetworkController getBrain(){
		NetworkInterface interface1=getConnectedInterface();
		return interface1!=null?interface1.getBrain():null;
	}

	@Override
	public long getCard(){
		return getCard(tile);
	}
	
	@Override
	public List<ICommandInteract> getCommandInteractors(){
		List<ICommandInteract> result=new ArrayList<ICommandInteract>();
		for(HoloObject i:((TileEntityHologramProjector)tile).holoObjects)if(i!=null)result.add(i);
		return result;
	}
	
	@Override
	public NetworkInterface getConnectedInterface(){
		TileEntity[] tiles=SideUtil.getTilesOnSides(tile);
		for(int i=0;i<tiles.length;i++){
			if(tiles[i] instanceof NetworkInterface){
				WorldNetworkInterface Interface=((NetworkInterface)tiles[i]).getInterfaceProvider();
				if(Interface instanceof InterfaceTileEntitySaver){
					return (NetworkInterface)tiles[i];
				}
			}
		}
		return null;
	}
	

	@Override
	public void onNetworkActionInvoked(NetworkInterface Interface, String action, Object... data){
		PrintUtil.println("hi");
	}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		cardList.put(tile, NBT.getLong("card"));
	}
	@Override public void setBoundTile(TileEntity tile){this.tile=tile;}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		NBT.setLong("card", getCard());
	}
}
