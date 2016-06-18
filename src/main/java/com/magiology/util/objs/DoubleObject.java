package com.magiology.util.objs;

import com.magiology.util.statics.UtilM;

public class DoubleObject <Obj1,Obj2>{
	public Obj1 obj1;
	public Obj2 obj2;
	public DoubleObject(){}
	public DoubleObject(Obj1 obj1, Obj2 obj2){
		this.obj1=obj1;
		this.obj2=obj2;
	}
	public Object get(boolean firstObj){
		return firstObj?obj1:obj2;
	}
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof DoubleObject))return false;
		return ((DoubleObject)obj).obj1.equals(obj1)&&((DoubleObject)obj).obj2.equals(obj2);
	}
	@Override
	public String toString(){
		
		return "DoubleObject{"+UtilM.toString(obj1)+", "+UtilM.toString(obj2)+"}";
	}
}