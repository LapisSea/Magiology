package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;

public abstract class UniformUploaderBase<T>{
	protected T prev;
	public abstract void upload(T value);
	protected final int pointer;
	protected ShaderProgram parent;
	
	public UniformUploaderBase(ShaderProgram parent,int pointer){
		this.pointer=pointer;
		this.parent=parent;
	}
	public UniformUploaderBase(ShaderProgram parent,String name){
		this(parent, parent.getUniformLocation(name));
	}
	
	protected boolean isSame(T value){
		if(prev==null)return false;
		return prev.equals(value);
	}
}