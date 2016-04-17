package com.magiology.util.utilobjects;

public class SimpleCounter {
	private boolean isFirst=true;
	private int var=0;
	public void add(){
		var++;
	}
	public int get(){
		return var;
	}
	public int getAndAdd(){
		if(isFirst)isFirst=false;
		else add();
		return var;
	}
}
