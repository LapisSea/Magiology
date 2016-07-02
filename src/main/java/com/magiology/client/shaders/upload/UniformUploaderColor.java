package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.util.objs.ColorF;

public class UniformUploaderColor<T extends ColorF> extends UniformUploaderBase<T>{
	
	public UniformUploaderColor(ShaderProgram parent,String name){
		super(parent,name);
	}
	
	@Override
	public void upload(T value){
		if(isSame(value))return;
		prev=value;
		parent.upload(pointer,value);
	}
}