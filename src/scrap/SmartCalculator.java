package com.magiology.util.utilclasses.math;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.magiology.util.utilclasses.math.ObjectCalculator.Calculator;
import com.magiology.util.utilobjects.vectors.Vec3M;


public class SmartCalculator{
	
	private static final char mul='*',div='/',add='+',sub='-',Int='i',flo='f',dou='d',lon='l',vec='v';
	private static final int normalLenght=2,arrayLenght=4;
	
	public static Object[] calc(CalculationFormat fromat,Object... data){
		return null;
	}
	private static class CalcObj{
		public char type;
		public boolean isArray=false;
		public Class getType(){
			if(isArray){
				if(type==Int)return new int[0].getClass();
				if(type==flo)return new float[0].getClass();
				if(type==dou)return new double[0].getClass();
				if(type==lon)return new long[0].getClass();
				if(type==vec)return new Vec3M[0].getClass();
			}else{
				if(type==Int)return Integer.class;
				if(type==flo)return Float.class;
				if(type==dou)return Double.class;
				if(type==lon)return Long.class;
				if(type==vec)return Vec3M.class;
			}
			throw new IllegalStateException(getClass().getSimpleName()+" has an invalid type. 'Type: "+type+" isArray: "+isArray+"'");
		}
	}
	public static class CalculationFormat extends CalcObj{
		
		public List<CalcObj> formatData=new ArrayList<CalcObj>();
		public String source;
		private boolean isCompiled=false;
		
		private CalculationFormat(){}
		public static CalculationFormat format(String format){
			CalculationFormat result=new CalculationFormat();
			result.source=format;
			return result;
		}
		
		public Object calc(Object... data){
			if(!isCompiled)compile();
			Object result=null;
			if(size()!=data.length)throw new IllegalStateException("Formating and given objects do not mach.");
			List<CalcObj> filterd=new ArrayList<CalcObj>();
			for(CalcObj i:formatData){
				if(!(i instanceof Operator)){
					filterd.add(i);
				}
			}
//			for(int i=0;i<data.length;i++){
//				CalcObj obj=filterd.get(i);
////				Helper.printInln(obj instanceof CalculationFormat);
////				if(obj instanceof CalculationFormat){
////					int size=((CalculationFormat)obj).size();
////					Object[] subData=new Object[size];
////					for(int j=0;j<size;j++)subData[i]=data[i+j];
////					Object subResult=((CalculationFormat)obj).calc(subData);
////					
////					Object[] newData=new Object[data.length-size+1];
////					
////					for(int j=0;j<i;j++){
////						newData[j]=data[j];
////					}
////					newData[i]=subResult;
////					for(int j=i+1;j<newData.length;j++){
////						newData[j]=data[j+size];
////					}
////					Helper.printInln(data);
////					Helper.printInln(newData);
////					Helper.printInln("me it's me");
////					data=newData;
////					i+=-size+1;
////				}else{
//					if(obj.getType()!=data[i].getClass()){
//						Helper.printInln(obj,data[i]);
//						throw new IllegalStateException("Format obj and given object are not compatabile "+obj.getType()+", "+data[i].getClass()+", id: "+i);
//					}
////				}
//			}
			int skippedValues=0;
			for(int i=0;i<formatData.size();i++){
				CalcObj obj=formatData.get(i);
				if(i==0)result=data[0];
				else{
					if(obj instanceof Operator){
//						Helper.printInln(result,"before");
						result=((Operator)obj).calc(result, data[i-skippedValues]);
//						Helper.printInln(result,"after");
						skippedValues++;
					}
				}
			}
			return result;
		}
		public int size(){
			int size=0;
			for(Object i:formatData){
				if(!(i instanceof Operator)){
					if(i instanceof CalculationFormat)size+=((CalculationFormat)i).size();
					else size++;
				}
			}
			return size;
		}
		public void compile(){
			if(isCompiled)return;
			String format=""+source;
			format.replaceAll("\\s","");
//			if(StringUtils.countMatches(format, "(")!=StringUtils.countMatches(format, ")"))throw new IllegalStateException("Invalid brackets! "+StringUtils.countMatches(format, "(")+" of '(' and "+StringUtils.countMatches(format, ")")+" of ')'");
			if(StringUtils.countMatches(format, "(")>0||StringUtils.countMatches(format, ")")>0)throw new IllegalStateException("Formater does not support brackets! (for now)");
			
			String bracketContent="";
			int bracketCount=0;
			
			while(format.length()!=0){
				char c=format.charAt(0);
				if(c==mul||c==div||c==add||c==sub){
					if(bracketCount==0)formatData.add(new Operator(c));
					else bracketContent+=c;
					format=format.substring(1);
				}else if(c=='('){
					if(bracketCount>0)bracketContent+="(";
					bracketCount++;
					format=format.substring(1);
				}else if(c==')'){
					bracketCount--;
					if(bracketCount>0)bracketContent+=")";
					if(bracketCount==0){
						formatData.add(format(bracketContent));
						bracketContent="";
					}
					format=format.substring(1);
				}else{
					Variable var=getVar(format);
					format=format.substring(var.source.length());
					if(bracketCount==0)formatData.add(var);
					else bracketContent+=var.source;
				}
			}
			if(formatData.size()==0)throw new IllegalStateException("Format empty!");
			if(formatData.size()==1)throw new IllegalStateException("Format does nothing!");
			isCompiled=true;
		}
		private Variable getVar(String format){
			if(!format.startsWith("%"))throw new IllegalStateException("Invalid variable! "+format);
			
			int varLenght=9999999;
			
			if(format.contains(mul+""))varLenght=min(varLenght, format.indexOf(mul));
			if(format.contains(div+""))varLenght=min(varLenght, format.indexOf(div));
			if(format.contains(add+""))varLenght=min(varLenght, format.indexOf(add));
			if(format.contains(sub+""))varLenght=min(varLenght, format.indexOf(sub));
			if(format.contains(")"))varLenght=min(varLenght, format.indexOf(")"));
			if(varLenght==9999999)varLenght=format.length();
			if(varLenght!=normalLenght&&varLenght!=arrayLenght)throw new IllegalStateException("Invalid variable! "+format+" length "+varLenght);

			return new Variable(format.substring(0,varLenght));
		}
		@Override
		public String toString(){
			return "CalculationFormat{source: "+source+", data: "+formatData+"}";
		}
		
		private static class Variable extends CalcObj{
			public String source;
			public Variable(String source){
				this.source=source;
				if(source.length()==arrayLenght){
					if(!source.endsWith("[]"))throw new IllegalStateException("Invalid variable structure!");
					else isArray=true;
				}
				char type=source.charAt(1);
				if(type!=Int&&type!=flo&&type!=dou&&type!=lon&&type!=vec)throw new IllegalStateException("Invalid variable type! "+type);
				this.type=type;
			}
			@Override
			public String toString(){
				return "Variable{"+"source: "+source+", type: "+type+", isArray: "+isArray+"}";
			}
		}
		private static class Operator extends CalcObj{
			Calculator calculator;
			public Operator(char type){
				this.type=type;
			}
			@Override
			public String toString(){
				return "Operator{type: "+type+"}";
			}
			public Object calc(Object left,Object right){
				if(calculator==null)calculator=ObjectCalculator.getCalculator(left, right, type);
				return calculator.calc(left, right);
			}
		}
	}
}
