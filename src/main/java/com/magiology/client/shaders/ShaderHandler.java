package com.magiology.client.shaders;

import java.util.HashMap;
import java.util.Map;

import com.magiology.core.class_manager.ClassList;

public class ShaderHandler{
	
	private static final ShaderHandler instance=new ShaderHandler();
	public static ShaderHandler get(){return instance;}
	private ShaderHandler(){}
	
	private Map<Class<ShaderProgram>, ShaderProgram> shaders=new HashMap<>();
	
	public static <T extends ShaderProgram>T getShader(Class<T> clazz){
//		LogUtil.println(ClassList.getImplementations(ShaderProgram.class));
		return (T)get().shaders.get(clazz);
	}
	
	public void load(){
		if(shaders.isEmpty()){
			ClassList.getImplementations(ShaderProgram.class).forEach(clazz->{
				try{
					shaders.put(clazz, clazz.newInstance());
				}catch(Exception e){
					e.printStackTrace();
				}
			});
		}
		shaders.entrySet().forEach(e->{
			e.getValue().compile();
		});
	}
}
