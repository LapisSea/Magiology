package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.util.objs.vec.Vec3M;

public class UniformUploaderVec<T extends Vec3M> extends UniformUploaderBase<T>{
	
	public UniformUploaderVec(ShaderProgram parent, String name){
		super(parent, name);
	}
	
	@Override
	public void upload(T value){
		if(isSame(value)) return;
		prev=value;
		parent.upload(pointer, value);
	}
}
