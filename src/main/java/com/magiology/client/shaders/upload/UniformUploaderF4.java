package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;

public class UniformUploaderF4 extends UniformUploaderBase<float[]>{
	
	public UniformUploaderF4(ShaderProgram parent,String name){
		super(parent, name);
	}

	@Override
	public void upload(float... value){
		if(value.length!=4)throw new IllegalArgumentException("Invalid array lenght! Required: 4, Given: "+value.length);
		if(isSame(value))return;
		prev=value;
		parent.upload(pointer,value[0],value[1],value[2],value[3]);
	}
}