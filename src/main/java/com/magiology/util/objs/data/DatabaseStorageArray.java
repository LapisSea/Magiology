package com.magiology.util.objs.data;

import com.magiology.util.statics.UtilM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DatabaseStorageArray<T> implements IDatabaseStorage<T>{
	
	protected Map<Class,List<T>>  extensionHistory=new HashMap<>();
	protected Map<String,List<T>> nameHistory     =new HashMap<>();
	protected final Class<? extends T> base;
	
	public DatabaseStorageArray(T base1){
		base=(Class<? extends T>)base1.getClass();
	}
	
	public DatabaseStorageArray(Class<? extends T> base1){
		base=base1;
	}
	
	@Override
	public <C> List<C> getByExtension(Class<C> c){
		List<C> prev=(List<C>)extensionHistory.get(c);
		if(prev!=null) return prev;
		
		List<C> newResult=new ArrayList();
		for(T t : getDatabase()){
			if(UtilM.instanceOf(t, c)) newResult.add((C)t);
		}
		extensionHistory.put(c, (List<T>)newResult);
		return newResult;
	}
	
	@Override
	public List<T> getByName(String name){
		List<T> prev=extensionHistory.get(name);
		if(prev!=null) return prev;
		
		List<T> newResult=new ArrayList();
		for(T t : getDatabase()){
			if(t.getClass().getSimpleName().contains(name)) newResult.add(t);
		}
		
		nameHistory.put(name, newResult);
		return newResult;
	}
	
	public abstract T[] getDatabase();
	
}
