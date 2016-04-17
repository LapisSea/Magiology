package com.magiology.api.lapislang;

import static com.magiology.util.utilclasses.Util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.magiology.api.Function;

public class LapisProgram{
	Map<String,String> tags;
	public List<Var> in=new ArrayList<Var>(),vars=new ArrayList<Var>();
	public List<Function> func=new ArrayList<Function>();
	@Override
	public String toString(){
		return "LapisProgram{\n\ttags: "+tags+"\n\tin: "+in+"\n\tvars: "+vars+"\n\tfuncs:"+func+"\n}";
	}
	public Object run(Object...in){
		try{
			if(in.length!=this.in.size())throw new IllegalArgumentException("Wrong amount of arguments is given! Inputed: "+in.length+" ,Required: "+this.in.size());
			for(int i=0;i<this.in.size();i++){
				Var variable=this.in.get(i);
				
				Object inConv=in[i];
				String inS=inConv.toString();
				if(inS.length()>=2){
					//test for numbers
					String maybeASomething=inS.substring(0, inS.length()-1);
					try{
						if(inS.endsWith("I")){
							inConv=Integer.parseInt(maybeASomething);
						}
						if(inS.endsWith("F")){
							inConv=Float.parseFloat(maybeASomething);
						}
						if(inS.endsWith("D")){
							inConv=Double.parseDouble(maybeASomething);
						}
						if(inS.endsWith("B")){
							inConv=Boolean.parseBoolean(maybeASomething);
						}
					}catch(Exception e){}
				}
				Class inc=inConv.getClass();
				if(Instanceof(variable.getType(),inc))variable.value=inConv;
				else if(variable.getType()==String.class){
					variable.value=inConv.toString();
				}else if(variable.getType()==Float.class){
					if(inc==Integer.class)variable.value=new Float((Integer)inConv);
				}else if(variable.getType()==Double.class){
					if(inc==Integer.class)variable.value=new Float((Integer)inConv);
					else if(inc==Float.class)variable.value=new Float((Float)inConv);
				}
				else{
					throw new IllegalArgumentException("var id: "+(i+1)+", "+variable.getType()+" cannot be set to "+inConv.getClass()+" = "+inConv);
				}
			}
			return func.get(0).run();
		}catch(Exception e){
			return "<ERROR> (DO NOT RETURN SOMETHING THAT STARTS WITH THIS!): "+e.getMessage();
		}
	}
}