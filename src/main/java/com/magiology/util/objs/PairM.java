package com.magiology.util.objs;

import com.magiology.util.statics.UtilM;

public class PairM<Obj1,Obj2>{
	public Obj1 obj1;
	public Obj2 obj2;
	public PairM(){}
	public PairM(Obj1 obj1, Obj2 obj2){
		this.obj1=obj1;
		this.obj2=obj2;
	}
	public Object get(boolean firstObj){
		return firstObj?obj1:obj2;
	}
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof PairM))return false;
		return ((PairM)obj).obj1.equals(obj1)&&((PairM)obj).obj2.equals(obj2);
	}
	@Override
	public String toString(){
		return "DoubleObject["+UtilM.toString(obj1)+", "+UtilM.toString(obj2)+"}";
	}
	@Override
	public int hashCode(){
		int hash=1;
		if(obj1!=null)hash=hash*31+obj1.hashCode();
		if(obj2!=null)hash=hash*31+obj2.hashCode();
		return hash;
	}
}