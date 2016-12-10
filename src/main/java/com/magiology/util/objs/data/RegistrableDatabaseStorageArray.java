package com.magiology.util.objs.data;


public abstract class RegistrableDatabaseStorageArray<T> extends DatabaseStorageArray<T>{
	
	private boolean registered=false;
	
	public RegistrableDatabaseStorageArray(Class<T> base){
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
