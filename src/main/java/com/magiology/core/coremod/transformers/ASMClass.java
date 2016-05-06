package com.magiology.core.coremod.transformers;

public class ASMClass extends ASMBase{

	public ASMClass(String obfuscated, String normal){
		super("L"+obfuscated, (normal.length()==1?"":"L")+normal);
	}
	@Override
	public String get(){
		return ClassTransformerBase.isObfuscated?obfuscated:normal;
	}
}