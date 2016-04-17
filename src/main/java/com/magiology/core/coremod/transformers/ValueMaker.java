package com.magiology.core.coremod.transformers;
public interface ValueMaker{
		public static String compileBase(ValueMaker[] data, String returnType){
			StringBuilder result=new StringBuilder("(");
			
			for(ValueMaker valueMaker:data){
				if(valueMaker.get().length()==1)result.append(valueMaker.get());
				else result.append(valueMaker.get()).append(";");
			}
			
			result.append(")").append(returnType);
			if(returnType.length()>1)result.append(";");
			return result.toString();
		}
		public abstract String get();
}
