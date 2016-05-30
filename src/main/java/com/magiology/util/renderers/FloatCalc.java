package com.magiology.util.renderers;

import com.magiology.api.Calculable;

public class FloatCalc implements Calculable<FloatCalc>{
	
	public float value;
	
	public FloatCalc(){
		
	}
	public FloatCalc(float value){
		this.value=value;
	}
	
	@Override
	public FloatCalc add(float var){
		return null;
	}

	@Override
	public FloatCalc add(FloatCalc var){
		return null;
	}

	@Override
	public FloatCalc div(float var){
		return null;
	}

	@Override
	public FloatCalc div(FloatCalc var){
		return null;
	}

	@Override
	public FloatCalc mul(float var){
		return null;
	}

	@Override
	public FloatCalc mul(FloatCalc var){
		return null;
	}

	@Override
	public FloatCalc sub(float var){
		return null;
	}

	@Override
	public FloatCalc sub(FloatCalc var){
		return null;
	}
	@Override
	public FloatCalc copy(){
		return new FloatCalc(value);
	}

}
