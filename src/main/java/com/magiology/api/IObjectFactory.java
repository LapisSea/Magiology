package com.magiology.api;

public interface IObjectFactory<T>{
	public T generate(Object...args);
}
