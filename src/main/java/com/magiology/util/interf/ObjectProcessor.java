package com.magiology.util.interf;

public interface ObjectProcessor<T>{
	public abstract T process(T object, Object...objects);
}