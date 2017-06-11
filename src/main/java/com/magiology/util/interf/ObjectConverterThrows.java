package com.magiology.util.interf;

@FunctionalInterface
public interface ObjectConverterThrows<T, result>{
	
	public abstract result convert(T object) throws Exception;
}
