package com.magiology.util.objs;


public abstract class RegistrableDatabaseStorage<T> extends DatabaseStorage<T>{
	
	private boolean registered=false;
	
	public RegistrableDatabaseStorage(Class<?extends T> base){
		super(base);
	}

	public void register(){
		if(registered)return;
		registered=true;
		
		for(T t:getDatabase()){
			registerObj(t);
		}
	}
	
	public abstract void registerObj(T obj);
}
