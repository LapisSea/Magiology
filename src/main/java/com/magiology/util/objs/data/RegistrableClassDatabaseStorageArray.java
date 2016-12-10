package com.magiology.util.objs.data;


public abstract class RegistrableClassDatabaseStorageArray<T> extends DatabaseStorageArray<Class<T>>{
	
	private boolean registered=false;

	public RegistrableClassDatabaseStorageArray(Class<T> base){
		super(base);
	}

	public void register(){
		if(registered)return;
		registered=true;
		
		for(Class<T> t:getDatabase()){
			registerObj(t);
		}
	}
	
	public abstract void registerObj(Class<T> obj);
}