package com.magiology.util.renderers;

import com.magiology.util.utilclasses.math.PartialTicksUtil;

public class PartialTicks1F{
	
	public float value,prevValue;
	
	public PartialTicks1F(){}
	public PartialTicks1F(float value){
		this.value=prevValue=value;
	}
	
	public float get(){
		return PartialTicksUtil.calculatePos(prevValue, value);
	}
	
	public void update(float newValue){
		prevValue=value;
		value=newValue;
	}
}
