package com.magiology.util.interf;

public interface ObjectProcessor<T>{
	
	T process(T object, Object... objects);
}
