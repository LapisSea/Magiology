package com.magiology.util.utilobjects.codeinsert;

public interface ObjectConverter<in,out>{
	public out convert(in in);
}
