package com.magiology.core.coremod.transformers;

public class ASMClass extends ASMBase{

	public ASMClass(String obfuscated, String normal){
		super("L"+obfuscated, "L"+normal);
	}
	
}