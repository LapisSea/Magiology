package com.magiology.util.objs.data;


public abstract class RegistrableDatabaseStorageCollection<T> extends DatabaseStorageCollection<T>{
	
	private boolean registered=false;
	
	public RegistrableDatabaseStorageCollection(Class<?extends T> base){
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
