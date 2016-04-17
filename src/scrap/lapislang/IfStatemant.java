package com.magiology.api.lapislang;


public class IfStatemant implements Operator{
	public Operator trueActivation,isTrue;
	@Override
	public Object run(){
		return (Boolean)isTrue.run()?trueActivation.run():null;
	}
}
