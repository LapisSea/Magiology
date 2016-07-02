package com.magiology.client.shaders.upload;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.util.interf.ObjectProcessor;

public class UniformUploaderCustom<T> extends UniformUploaderBase<T>{
		protected ObjectProcessor<T> run;
		public UniformUploaderCustom(ShaderProgram parent,String name,ObjectProcessor<T> run){
			super(parent,name);
			this.run=run;
		}
		
		@Override
		public void upload(T value){
			if(isSame(value))return;
			run.process(value);
		}
	}