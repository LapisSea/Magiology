package com.magiology.api.lang.bridge;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;
import com.magiology.api.lang.ICommandInteract;
import com.magiology.api.lang.program.ProgramCommon;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.WorldNetworkInterface;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;


public class InterfaceWrapper{
	
	public static Object New(Object src){
		if(src!=null&&src instanceof NetworkInterface&&src instanceof ISidedNetworkComponent)return new InterfaceWrapper((NetworkInterface&ISidedNetworkComponent)src);
		return ProgramCommon.JSNull("No interface");
	}
	private ISidedNetworkComponent comp;
	private NetworkInterface interf;
	private <T extends NetworkInterface&ISidedNetworkComponent>InterfaceWrapper(T src){
		interf=src;
		comp=src;
	}public Block getBlock(){
		return getFacedPos().getBlock(getWorld());
	}
	public Object getBlockState(String stateName){
		ImmutableSet set=getFacedPos().getState(getWorld()).getProperties().entrySet();
		Iterator iterator=set.iterator();
		if(set.size()==0)return ProgramCommon.JSNull("No states");
		Entry entry;
		while(iterator.hasNext()){
			entry=(Entry)iterator.next();
			if(((IProperty)entry.getKey()).getName().equals(stateName))return entry.getValue();
		}
		return ProgramCommon.JSNull("State \""+stateName+"\" does not exist!");
	}
	
	
	
	public Object getCommandInteractors(){
		WorldNetworkInterface Interface=interf.getInterfaceProvider();
		if(Interface==null)return ProgramCommon.JSNull("Block is not able to contain command interactors");
		List<ICommandInteract> list=Interface.getCommandInteractors();
		if(list==null)return ProgramCommon.JSNull("No command interactors found");
		return new CommandInteractorList(list);
	}
	public BlockPosM getFacedPos(){
		return getPos().offset(EnumFacing.getFront(comp.getOrientation()));
	}
	public Block getInterfaceBlock(){
		return getPos().getBlock(getWorld());
	}
	public Object getInterfaceBlockState(String stateName){
		ImmutableSet set=getPos().getState(getWorld()).getProperties().entrySet();
		Iterator iterator=set.iterator();
		if(set.size()==0)return ProgramCommon.JSNull("No states");
		Entry entry;
		while(iterator.hasNext()){
			entry=(Entry)iterator.next();
			if(((IProperty)entry.getKey()).getName().equals(stateName))return entry.getValue();
		}
		return "undefined";
	}
	
	public int getInterfaceRedstone(EnumFacing side){
		return getWorld().getRedstonePower(getPos(), side);
	}
	
	public int getInterfaceRedstone(int side){
		return getWorld().getRedstonePower(getPos(), EnumFacing.getFront(side));
	}
	private BlockPosM getPos(){
		return BlockPosM.get(interf.getHost().getPos());
	}
	public int getRedstone(EnumFacing side){
		return getFacedPos().getRedstonePower(getWorld(), side);
	}
	public int getRedstone(int side){
		return getFacedPos().getRedstonePower(getWorld(), EnumFacing.getFront(side));
	}
	private World getWorld(){
		return interf.getHost().getWorld();
	}
}
