package com.magiology.util.objs;

import com.magiology.util.interf.Calculable;

public class CalculableFloat implements Calculable<CalculableFloat>{
	
	public float value;
	
	public CalculableFloat(float value){
		this.value=value;
	}
	
	@Override
	public CalculableFloat add(float var){
		return new CalculableFloat(value+var);
	}
	
	@Override
	public CalculableFloat add(CalculableFloat var){
		return add(var.value);
	}
	
	@Override
	public CalculableFloat div(float var){
		return new CalculableFloat(value/var);
	}
	
	@Override
	public CalculableFloat div(CalculableFloat var){
		return div(var.value);
	}
	
	@Override
	public CalculableFloat mul(float var){
		return new CalculableFloat(value*var);
	}
	
	@Override
	public CalculableFloat mul(CalculableFloat var){
		return mul(var.value);
	}
	
	@Override
	public CalculableFloat sub(float var){
		return new CalculableFloat(value-var);
	}
	
	@Override
	public CalculableFloat sub(CalculableFloat var){
		return sub(var.value);
	}
	
	@Override
	public CalculableFloat copy(){
		return new CalculableFloat(value);
	}
	
	@Override
	public String toString(){
		return String.valueOf(value);
	}
}
