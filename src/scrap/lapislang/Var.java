package com.magiology.api.lapislang;


public class Var implements Operator{
	private static final char mul='*',div='/',add='+',sub='-',Int='i',flo='f',dou='d',lon='l';
	public String name;
	public char type;
	public Object value;
	public Var(String name, char type, Object value){
		this.name=name;
		this.type=type;
		this.value=value;
	}
	public Class getType(){
		if(type==Int)return Integer.class;
		if(type==flo)return Float.class;
		if(type==dou)return Double.class;
		if(type==lon)return Long.class;
		if(type=='b')return Boolean.class;
		if(type=='s')return "".getClass();
		throw new IllegalStateException(getClass().getSimpleName()+" has an invalid type. 'Type: "+type);
	}
	public Object getTypeObject(){
		if(type==Int)return new Integer(0);
		if(type==flo)return new Float(0);
		if(type==dou)return new Double(0);
		if(type==lon)return new Long(0);
		if(type=='b')return new Boolean(false);
		if(type=='s')return "";
		throw new IllegalStateException(getClass().getSimpleName()+" has an invalid type. 'Type: "+type);
	}
	@Override
	public String toString(){
		return "Variable{name: "+name+", type: "+type+", value: "+value+"}";
	}
	@Override
	public Object run(){
		return value;
	}
}