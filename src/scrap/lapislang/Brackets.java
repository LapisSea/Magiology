package com.magiology.api.lapislang;

import java.util.List;


public class Brackets implements Operator{
	
	public List<Operator> operators;
	
	
	public Brackets(List<Operator> operators){
		this.operators=operators;
	}
	
	@Override
	public Object run(){
		for(int i=0;i<operators.size()-1;i++)operators.get(i).run();
		return operators.get(operators.size()-1).run();
	}
	public int size(){
		return operators.size();
	}
	public Operator get(int index){
		return operators.get(index);
	}
	@Override
	public String toString(){
		return "Brackets{operators: "+operators+"}";
	}
}
