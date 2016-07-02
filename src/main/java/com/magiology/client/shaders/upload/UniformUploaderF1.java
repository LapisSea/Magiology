package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;

public class UniformUploaderF1 extends UniformUploaderBase<Float>{
	
	public UniformUploaderF1(ShaderProgram parent,String name){
		super(parent, name);
	}

	@Override
	public void upload(Float value){
		if(isSame(value))return;
		prev=value;
		parent.upload(pointer,value);
	}
}