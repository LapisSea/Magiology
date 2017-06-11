package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;

public abstract class UniformUploaderArray<T> extends UniformUploaderBase<T>{
	
	public UniformUploaderArray(ShaderProgram parent, String name){
		super(parent, name);
	}
	
	@Override
	protected boolean isSame(T value){
		if(prev==null) return false;
		float[] array1=(float[])value, array2=(float[])prev;
		if(array1.length!=array2.length) return false;
		
		for(int i=0; i<array1.length; i++){
			if(array1[i]!=array2[i]){
				return false;
			}
		}
		return true;
	}
}
