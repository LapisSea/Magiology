package com.magiology.core.coremod.transformers;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMMethod extends ASMBase{
		public ASMFuncDesc desc;
		public ASMMethod(String obfuscated, String normal,ASMFuncDesc desc){
			super(obfuscated, normal);
			this.desc=desc;
		}
		@Override
		public boolean equals(Object obj){
			if(obj instanceof MethodNode){
				MethodNode meth/*not Breaking Bad one*/=(MethodNode)obj;
				if(meth.name.equals(get())){
//					PrintUtil.println(meth.name,meth.desc,(desc.get()));//debug for wrong descriptions
					if(meth.desc.equals(desc.get()))return true;
				}
			}
			if(obj instanceof MethodInsnNode){
				MethodInsnNode meth/*not Breaking Bad one! I think said this already...*/=(MethodInsnNode)obj;
				if(meth.name.equals(get())&&meth.desc.equals(desc.get()))return true;
			}
			return super.equals(obj);
		}
		@Override
		public String toString(){
			return get()+" - "+desc.toString();
		}
	}