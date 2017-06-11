package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;

public class UniformUploaderF3 extends UniformUploaderBase<float[]>{
	
	public UniformUploaderF3(ShaderProgram parent, String name){
		super(parent, name);
	}
	
	@Override
	public void upload(float... value){
		if(value.length!=3) throw new IllegalArgumentException("Invalid array length! Required: 3, Given: "+value.length);
		if(isSame(value)) return;
		prev=value;
		parent.upload(pointer, value[0], value[1], value[2]);
	}
}
