package com.magiology.mc_objects.tile.multiblock;

import com.magiology.util.objs.ArrayList_ModifyHook;

import java.util.List;

public class MultiblockData<T>{
	
	public final List<T> parts=new ArrayList_ModifyHook<>(this::update);
	
	protected void update(){}
}
