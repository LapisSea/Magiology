package com.magiology.core.coremod.transformers;

import org.objectweb.asm.tree.AbstractInsnNode;

public class LineWalker{
		private AbstractInsnNode curentLine;
		public LineWalker(AbstractInsnNode curentLine){
			goTo(curentLine);
		}

		public AbstractInsnNode get(){
			return curentLine;
		}
		
		public LineWalker goTo(AbstractInsnNode line){
			curentLine=line;
			return this;
		}
		public LineWalker next(){
			return next(1);
		}
		public LineWalker next(int count){
			for(int i=0;i<count;i++)curentLine=nextR();
			return this;
		}
		public AbstractInsnNode nextR(){
			return curentLine=curentLine.getNext();
		}
		public LineWalker previous(){
			return previous(1);
		}
		public LineWalker previous(int count){
			for(int i=0;i<count;i++)curentLine=previousR();
			return this;
		}

		public AbstractInsnNode previousR(){
			return curentLine=curentLine.getPrevious();
		}
	}