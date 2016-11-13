package com.magiology.mc_objects.features.neuro;

import com.magiology.mc_objects.BlockStates;
import com.magiology.mc_objects.BlockStates.IPropertyM;
import com.magiology.mc_objects.BlockStates.PropertyBoolM;
import com.magiology.mc_objects.BlockStates.PropertyIntegerM;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;

public class BlockNeuroController extends BlockNeuroBase{

	private static final PropertyBoolM[] CONNECTIONS;
	public static final PropertyIntegerM LEVEL=BlockStates.saveableIntProp("level", 3);
	
	static{
		CONNECTIONS=new PropertyBoolM[6];
		for(int i=0;i<6;i++)CONNECTIONS[i]=BlockStates.booleanProp(EnumFacing.getFront(i).toString());
	}
	
	public BlockNeuroController(){
		super(Material.IRON,()->new TileEntityNeuroController(),UtilM.mixedToArray(IPropertyM.class,LEVEL,CONNECTIONS));
	}
	
}
