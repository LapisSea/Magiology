package com.magiology.core.coremod.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import com.magiology.util.utilclasses.math.MathUtil;

public class CodeWrapper{

	public static class Ldc implements CodeAction{
		
		private boolean isReverse;
		private float value;
		public Ldc(float value){
			this(value,false);
		}
		public Ldc(float value,boolean isReverse){
			this.value=value;
			this.isReverse=isReverse;
		}
		@Override
		public boolean check(AbstractInsnNode line){
			return ClassTransformerBase.isLdc(line, value);
		}

		@Override
		public void move(LineWalker code){
			if(isReverse)code.previous();
			else code.next();
		}
		
	}
	public static class Areturn implements CodeAction{
		
		private boolean isReverse;
		public Areturn(){
			this(false);
		}
		public Areturn(boolean isReverse){
			this.isReverse=isReverse;
		}
		@Override
		public boolean check(AbstractInsnNode line){
			return ClassTransformerBase.isAreturnValue(line);
		}
		
		@Override
		public void move(LineWalker code){
			if(isReverse)code.previous();
			else code.next();
		}
		
	}
	public static class Aload implements CodeAction{
		
		private boolean isReverse;
		private int value;
		public Aload(int value){
			this(value,false);
		}
		public Aload(int value,boolean isReverse){
			this.value=value;
			this.isReverse=isReverse;
		}
		@Override
		public boolean check(AbstractInsnNode line){
			return ClassTransformerBase.isAloadValue(line, value);
		}

		@Override
		public void move(LineWalker code){
			if(isReverse)code.previous();
			else code.next();
		}
		
	}
	
	public static interface CodeAction{
		boolean check(AbstractInsnNode line);
		void move(LineWalker code);
	}
	public static class InvokeInterface implements CodeAction{
		
		private boolean isReverse;
		private ASMMethod value;
		public InvokeInterface(ASMMethod value){
			this(value,false);
		}
		public InvokeInterface(ASMMethod value,boolean isReverse){
			this.value=value;
			this.isReverse=isReverse;
		}
		@Override
		public boolean check(AbstractInsnNode line){
			return ClassTransformerBase.isInvokeInterface(line, value);
		}
		
		@Override
		public void move(LineWalker code){
			if(isReverse)code.previous();
			else code.next();
		}
		
	}
	public static class InvokeStatic implements CodeAction{
		
		private boolean isReverse;
		private ASMMethod value;
		public InvokeStatic(ASMMethod value){
			this(value,false);
		}
		public InvokeStatic(ASMMethod value,boolean isReverse){
			this.value=value;
			this.isReverse=isReverse;
		}
		@Override
		public boolean check(AbstractInsnNode line){
			return ClassTransformerBase.isInvokeStatic(line, value);
		}
		
		@Override
		public void move(LineWalker code){
			if(isReverse)code.previous();
			else code.next();
		}
		
	}
	public static class InvokeVirtual implements CodeAction{
		
		private boolean isReverse;
		private ASMMethod value;
		public InvokeVirtual(ASMMethod value){
			this(value,false);
		}
		public InvokeVirtual(ASMMethod value,boolean isReverse){
			this.value=value;
			this.isReverse=isReverse;
		}
		@Override
		public boolean check(AbstractInsnNode line){
			return ClassTransformerBase.isInvokeVirtual(line, value);
		}
		
		@Override
		public void move(LineWalker code){
			if(isReverse)code.previous();
			else code.next();
		}
		
	}
	public static AbstractInsnNode find(MethodNode method, CodeAction[] block){
		AbstractInsnNode result=null;
		LineWalker code=new LineWalker(method.instructions.get(0));
		
		AbstractInsnNode beginning;
		
		
		ListIterator<AbstractInsnNode> codeData=method.instructions.iterator();
		while(codeData.hasNext()){
			code.goTo(beginning=codeData.next());
			
			boolean compleate=true;
			innerLoop:for(CodeAction codeAction:block){
				if(!codeAction.check(code.get())){
					code.goTo(beginning);
					compleate=false;
					break innerLoop;
				}
				codeAction.move(code);
			}
			if(compleate){
				result=beginning;
				break;
			}
		}
		
		return result;
	}
	public static boolean ifWrapper(MethodNode method,InsnList ifCondition, CodeAction[] actionsBefore, int offsetStart, CodeAction[] actionsAfter, int offsetEnd){
		LabelNode label=new LabelNode();
		InsnList before=new InsnList(),after=new InsnList();
		
		before.add(ifCondition);
		before.add(new JumpInsnNode(IFEQ, label));
		after.add(label);
		
		return wrappAround(method, before, after, actionsBefore, offsetStart, actionsAfter, offsetEnd);
	}
	public static boolean wrappAround(MethodNode method,InsnList before,InsnList after, CodeAction[] actionsBefore, int offsetStart, CodeAction[] actionsAfter, int offsetEnd){
		
		AbstractInsnNode 
			start=find(method, actionsBefore),
			end=find(method, actionsAfter);
		
		if(start==null||end==null)return false;
		
		int offsetStartMul=MathUtil.getNumPrefix(offsetStart);
		int offsetEndMul=MathUtil.getNumPrefix(offsetEnd);
		offsetStart*=offsetStartMul;
		offsetEnd*=offsetEndMul;
		
		if(offsetStartMul==1)for(int i=0;i<offsetStart;i++)start=start.getNext();
		else for(int i=0;i<offsetStart;i++)start=start.getPrevious();
		
		if(offsetEndMul==1)for(int i=0;i<offsetEnd;i++)end=end.getNext();
		else for(int i=0;i<offsetEnd;i++)end=end.getPrevious();
		
		method.instructions.insertBefore(start, before);
		method.instructions.insert(end, after);
		
		return true;
	}
}