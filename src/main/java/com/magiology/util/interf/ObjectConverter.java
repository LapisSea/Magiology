package com.magiology.util.interf;

public interface ObjectConverter<in,out>{
	public out convert(in in);
}
