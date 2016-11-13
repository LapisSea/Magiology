package com.magiology.mc_objects.features.neuro;

import com.magiology.mc_objects.BlockStates;
import com.magiology.mc_objects.BlockStates.PropertyBoolM;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;

public class BlockNeuroDuct extends BlockNeuroBase{

	private static final PropertyBoolM[] CONNECTIONS;
	
	static{
		CONNECTIONS=new PropertyBoolM[6];
		for(int i=0;i<6;i++)CONNECTIONS[i]=BlockStates.booleanProp(EnumFacing.getFront(i).toString());
	}
	
	public BlockNeuroDuct(){
		super(Material.IRON,()->new TileEntityNeuroDuct(),CONNECTIONS);
	}
	
}
