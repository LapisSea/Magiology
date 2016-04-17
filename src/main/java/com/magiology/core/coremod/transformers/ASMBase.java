package com.magiology.core.coremod.transformers;

public class ASMBase implements ValueMaker{
	
	public String obfuscated,normal;
	
	public ASMBase(String obfuscated, String normal){
		this.obfuscated=obfuscated;
		this.normal=normal;
	}
	
	@Override
	public String get(){
		return ClassTransformerBase.isObfuscated?obfuscated:normal;
	}
	@Override
	public String toString(){
		return get();
	}
}