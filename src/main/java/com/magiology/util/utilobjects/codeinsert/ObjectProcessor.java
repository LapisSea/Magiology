package com.magiology.util.utilobjects.codeinsert;

public interface ObjectProcessor<T>{
	public abstract T process(T object, Object...objects);
}