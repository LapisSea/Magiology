package com.magiology.handlers.animationhandlers.thehand;

import java.util.ArrayList;
import java.util.List;

public class HandPosition{
	

	private static boolean isCompiled=false;
	private static final List<HandPosition> registry=new ArrayList<>();
	private static HandPosition[] values;
	
	public static void compile(){
		isCompiled=true;
		values=registry.toArray(new HandPosition[0]);
	}
	public static void registerPosition(HandPosition pos){
		assert !isCompiled:"It is to late to register!";
		assert !registry.contains(pos):"This position is already registered!";
		boolean nameMatch=false;
		for(HandPosition handPosition:registry){
			if(pos.name.equals(handPosition.name)){
				nameMatch=true;
				break;
			}
		}
		assert !nameMatch:"Position with is already registered with \""+pos.name+"\"!";
		
		pos.posInRegistry=registry.size();
		registry.add(pos);
	}
	public static HandPosition[] values(){
		return values;
	}
	
	
	
	public final HandData data;
	public final String name;
	private int posInRegistry;
	
	public HandPosition(HandData data, String name){
		this.data=data;
		this.name=name;
	}
	public int posInRegistry(){
		return posInRegistry;
	}
}