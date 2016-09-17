package com.magiology.util.objs;

import java.util.ArrayList;

import com.magiology.util.interf.Calculable;

public class CalculableList<T extends Calculable<T>>extends ArrayList<T> implements Calculable<CalculableList<T>>{
	
	public CalculableList(T[] data){
		for(int i=0; i<data.length; i++){
			add(data[i]);
		}
	}
	
	public CalculableList(){}
	
	public CalculableList(int initialCapacity){
		super(initialCapacity);
	}
	
	@Override
	public CalculableList<T> add(float var){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(get(i).add(var));
		}
		return newObj;
	}
	
	@Override
	public CalculableList<T> add(CalculableList<T> var){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(get(i).add(var.get(i)));
		}
		return newObj;
	}
	
	@Override
	public CalculableList<T> div(float var){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(get(i).div(var));
		}
		return newObj;
	}
	
	@Override
	public CalculableList<T> div(CalculableList<T> var){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(get(i).div(var.get(i)));
		}
		return newObj;
	}
	
	@Override
	public CalculableList<T> mul(float var){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(get(i).mul(var));
		}
		return newObj;
	}
	
	@Override
	public CalculableList<T> mul(CalculableList<T> var){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(get(i).mul(var.get(i)));
		}
		return newObj;
	}
	
	@Override
	public CalculableList<T> sub(float var){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(get(i).sub(var));
		}
		return newObj;
	}
	
	@Override
	public CalculableList<T> sub(CalculableList<T> var){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(i, get(i).sub(var.get(i)));
		}
		return newObj;
	}
	
	@Override
	public CalculableList<T> copy(){
		CalculableList<T> newObj=new CalculableList<>();
		for(int i=0; i<this.size(); i++){
			newObj.add(get(i).copy());
		}
		return newObj;
	}
	
}
