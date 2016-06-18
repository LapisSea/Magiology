package com.magiology.util.interf;

public interface Calculable<T extends Calculable>{
	public T add(float var);
	public T add(T var);
	public T div(float var);
	public T div(T var);
	
	public T mul(float var);
	public T mul(T var);
	public T sub(float var);
	public T sub(T var);
	
	public T copy();
}
