package com.magiology.util.renderers;

import com.magiology.api.Calculable;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ValueWithPrev<T extends Calculable<T>>{
	
	public T value,prevValue;
	
	public ValueWithPrev(){}
	public ValueWithPrev(T value){
		this.value=prevValue=value;
	}
	
	public T get(){
		return PartialTicksUtil.calculate(prevValue, value);
	}
	
	public void update(T newValue){
		prevValue=value;
		value=newValue;
	}
	public void update(){
		prevValue=value.add(0);
	}
}
