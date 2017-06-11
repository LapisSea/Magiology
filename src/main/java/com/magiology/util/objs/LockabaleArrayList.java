package com.magiology.util.objs;

import java.util.Collection;

public class LockabaleArrayList<E> extends ArrayList_ModifyHook<E>{
	
	private boolean locked=false;
	
	public LockabaleArrayList(){
		super();
		setHook(this::lockList);
	}
	
	public LockabaleArrayList(Collection<? extends E> c){
		super(c);
		setHook(this::lockList);
	}
	
	public void lock(){
		locked=true;
	}
	
	public boolean isLocked(){
		return locked;
	}
	
	protected void lockList(){
		if(locked) throw new IllegalAccessError("This list is locked");
	}
}
