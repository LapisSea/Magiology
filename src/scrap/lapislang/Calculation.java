package com.magiology.api.lapislang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.util.utilclasses.Util.U;
import com.magiology.util.utilclasses.math.ObjectCalculator;
import com.magiology.util.utilclasses.math.ObjectCalculator.Calculator;

public class Calculation implements Operator{
	public List<Operator> parts;
	public List<Character> functions;
	public List<Calculator> calculators=new ArrayList<Calculator>();
	public Var saver;
	
	public Calculation(Var saver,List<Operator> parts1, List<Character> functions1){
		this.functions=functions1;
		this.parts=parts1;
		this.saver=saver==null?new Var("subReturn", '!', null):saver;
		
		int min=Integer.MAX_VALUE;
		for(Character character:functions){
			if(!priorities.containsKey(character))throw new IllegalStateException("impossible function: "+character);
			min=Math.min(min, priorities.get(character));
		}
		for(int i=0;i<functions.size();i++){
			if(priorities.get(functions.get(i))>min){
				int start=i,end=-1;
				List<Operator> subParts=new ArrayList<Operator>();
				subParts.add(parts.get(i));
				while(i<functions.size()&&priorities.get(functions.get(i))>min){
					subParts.add(parts.get(end=i+1));
					i++;
				}
				List<Character> subFunctions=new ArrayList<Character>();
				parts.remove(start);
				for(int j=0;j<end-start;j++){
					parts.remove(start);
					subFunctions.add(functions.remove(start));
				}
				parts.add(start, new Calculation(null, subParts, subFunctions));
			}
		}
	}
	@Override
	public Object run(){
		try{
			if(parts.size()>1){
				if(calculators.isEmpty()){
					Object result=null;
					for(int i=0;i<parts.size()-1;i++){
						int j=U.booleanToInt(result!=null);
						Object left=parts.get(j)instanceof Var?((Var)parts.get(j)).getTypeObject():parts.get(j).run(),
							   right=parts.get(i+1)instanceof Var?((Var)parts.get(i+1)).getTypeObject():parts.get(i+1).run();
						Calculator newCalc=ObjectCalculator.getCalculator(result==null?left:result,right, functions.get(i));
						result=newCalc.calc(result==null?left:result,right);
						calculators.add(newCalc);
					}
				}
				Object result=null;
				for(int i=0;i<calculators.size();i++){
					Calculator calc=calculators.get(i);
					result=calc.calc(result==null?parts.get(i).run():result, parts.get(i+1).run());
				}
				return saver.value=result;
			}else return saver.value=parts.get(0).run();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public String toString(){
		return "Calculation{parts: "+parts+"functions: "+functions+"}";
	}
	private static Map<Character,Integer> priorities=new HashMap<Character,Integer>();
	static{
		priorities.put('+', 0);
		priorities.put('-', 0);
		priorities.put('*', 1);
		priorities.put('/', 1);
		priorities.put('%', 1);
	}
}