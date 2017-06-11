package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;

public class UniformUploaderF2 extends UniformUploaderArray<float[]>{
	
	public UniformUploaderF2(ShaderProgram parent, String name){
		super(parent, name);
	}
	
	@Override
	public void upload(float... value){
		if(value.length!=2) throw new IllegalArgumentException("Invalid array length! Required: 2, Given: "+value.length);
		if(isSame(value)) return;
		prev=value;
		parent.upload(pointer, value[0], value[1]);
	}
}
