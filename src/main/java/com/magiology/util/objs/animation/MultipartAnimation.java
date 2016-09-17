package com.magiology.util.objs.animation;

public class MultipartAnimation{
	
	public AnimationM source;
	
	public MultipartAnimation(AnimationM source){
		this.source=source;
	}
	
	public float get(int id, float pos){
		int id2=fixIds(id);
		return id2==-1?0:source.get(id2, pos);
	}
	
	public int fixIds(int id){
		return id;
	}
	
}
