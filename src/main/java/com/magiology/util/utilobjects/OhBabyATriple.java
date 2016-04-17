package com.magiology.util.utilobjects;

public class OhBabyATriple <Obj1,Obj2,Obj3>{
	public Obj1 obj1;
	public Obj2 obj2;
	public Obj3 obj3;
	public OhBabyATriple(){}
	public OhBabyATriple(Obj1 obj1, Obj2 obj2, Obj3 obj3){
		this.obj1=obj1;
		this.obj2=obj2;
		this.obj3=obj3;
	}
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof OhBabyATriple))return false;
		return ((OhBabyATriple)obj).obj1.equals(obj1)&&((OhBabyATriple)obj).obj2.equals(obj2)&&((OhBabyATriple)obj).obj3.equals(obj3);
	}
	@Override
	public String toString(){
		return "DoubleObject{"+obj1+", "+obj2+", "+obj3+"}";
	}
}
