package com.magiology.util.objs.animation;

public class AbstractAnimation{
	
	//	private final String name,partNames[];
	
	private int[]               ids;
	private AnimationMReference anim;
	
	public AbstractAnimation(String name, String... partNames){
		//		this.name=name;
		//		this.partNames=partNames;
		anim=AnimationBank.getExact(name);
		ids=new int[partNames.length];
		for(int i=0; i<ids.length; i++){
			ids[i]=anim.getIdName(partNames[i]);
		}
	}
	
	public float get(int id, float pos){
		return anim.get(ids[id], pos);
	}
	
}
