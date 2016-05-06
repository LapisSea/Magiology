package com.magiology.util.utilobjects;

public class SimpleCounter{
	private int var=0;
	
	public SimpleCounter(int start){
		var=start;
	}
	public SimpleCounter(){
		this(-1);
	}
	
	public void add(){
		var++;
	}
	public int get(){
		return var;
	}
	public int getAndAdd(){
		add();
		return var;
	}
}
