package com.magiology.core.coremod.transformers;

public class ASMFuncDesc implements ValueMaker{
	
	public String obfuscatedS,normalS,returnType;
	public ValueMaker[] param;
	
	public ASMFuncDesc(String returnType,ValueMaker...param){
		this.returnType=returnType;
		this.param=param;
	}
	
	private void compile(){
		boolean isObfuscatedSave=ClassTransformerBase.isObfuscated;
		ClassTransformerBase.isObfuscated=true;
		obfuscatedS=ValueMaker.compileBase(param, returnType);
		ClassTransformerBase.isObfuscated=false;
		normalS=ValueMaker.compileBase(param, returnType);
		ClassTransformerBase.isObfuscated=isObfuscatedSave;
	}
	@Override
	public String get(){
		if(obfuscatedS==null)compile();
		return ClassTransformerBase.isObfuscated?obfuscatedS:normalS;
	}
	@Override
	public String toString(){
		return get();
	}
}