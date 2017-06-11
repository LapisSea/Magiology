package com.magiology.util.interf;

public interface IObjectFactory<T>{
	
	public T generate(Object... args);
}
