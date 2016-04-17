package com.magiology.api;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.lapislang.Brackets;
import com.magiology.api.lapislang.Calculation;
import com.magiology.api.lapislang.LapisLangCompiler;
import com.magiology.api.lapislang.LapisProgram;
import com.magiology.api.lapislang.Operator;

public class Function implements Operator{
	public Brackets brackets;
	public Function(LapisProgram lp, String functionContent){
		// result=strength/15
		// return result
		List<Operator> operations=new ArrayList<Operator>();
		String[] lines=functionContent.split(";");
		for(int i=0;i<lines.length;i++){
			String line=lines[i];
			Calculation calc=LapisLangCompiler.getcalculationFromLine(lp, line).obj1;
			if(calc!=null)operations.add(calc);
		}
		brackets=new Brackets(operations);
	}
	@Override
	public Object run(){
		for(int i=0;i<brackets.size()-1;i++)brackets.get(i).run();
		return brackets.get(brackets.size()-1).run();
	}
	@Override
	public String toString(){
		return "Function{content: "+brackets+", size: "+(brackets!=null?brackets.size():0)+"}";
	}
}
