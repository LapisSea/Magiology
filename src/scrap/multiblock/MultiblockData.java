package com.magiology.mc_objects.tile.multiblock;

import java.util.List;

import com.magiology.util.objs.ArrayList_ModifyHook;

public class MultiblockData<T>{
	
	public final List<T> parts=new ArrayList_ModifyHook<>(this::update);
	
	protected void update(){}
}
