package com.magiology.util.utilobjects;


public class ObjectHolder<T>{
	
	private T var;
	
	public ObjectHolder(){}
	public ObjectHolder(T var){
		this.var=var;
	}
	
	public T getVar(){
		return var;
	}
	
	public void setVar(T var){
		this.var=var;
	}
}
