package com.magiology.util.utilobjects.codeinsert;

public interface ObjectProcessor<T>{
	public abstract T pocess(T object, Object...objects);
}