package com.magiology.util.objs.animation;

public class AnimationMReference{

	private final int accessId;
	
	public AnimationMReference(int accessId){
		this.accessId=accessId;
	}
	
	//http://stackoverflow.com/questions/716597/array-or-list-in-java-which-is-faster             ArrayList virtually equal to Array
	//http://stackoverflow.com/questions/23291448/java-independent-variable-vs-array-performance  Array object access virtually equal to normal object access
	//																							  No different from using an object reference and sacrificing possible animation desync on reloading
	public float get(int id, float pos){
		return get().get(id, pos);
	}

	public float[] getAll(float pos){
		return get().getAll(pos);
	}

	public int getIdName(String name){
		return get().getIdName(name);
	}

	private AnimationM get(){
		return AnimationBank.get(accessId);
	}
}
