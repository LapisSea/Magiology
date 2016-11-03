package com.magiology.util.objs.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilM;

public abstract class DatabaseStorageArray<T> implements IDatabaseStorage<T>{

	protected Map<Class, List<T>> extensionHistory=new HashMap<>();
	protected Map<String, List<T>> nameHistory=new HashMap<>();
	protected final Class<?extends T> base;
	
	
	public DatabaseStorageArray(Class<?extends T> base1){
		base=base1;
	}
	
	public <C> List<C> getByExtension(Class<C> c){
		List<C> prev=(List<C>)extensionHistory.get(c);
		if(prev!=null)return prev;
		
		List<C> newResult=new ArrayList();
		for(T t:getDatabase()){
			if(UtilM.instanceOf(t, c))newResult.add((C)t);
		}
		extensionHistory.put(c, (List<T>)newResult);
		return newResult;
	}
	public List<T> getByName(String name){
		List<T> prev=extensionHistory.get(name);
		if(prev!=null)return prev;
		
		List<T> newResult=new ArrayList();
		for(T t:getDatabase()){
			if(t.getClass().getSimpleName().contains(name))newResult.add(t);
		}
		
		nameHistory.put(name, newResult);
		return newResult;
	}
	
	public abstract T[] getDatabase();
	
}
