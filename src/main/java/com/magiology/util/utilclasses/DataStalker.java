package com.magiology.util.utilclasses;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

//NOTE: This is not a personal data Stalker! It's just a cool private marked variables or functions reader! (just so someone doesn't get the wrong idea)
public class DataStalker{
	private static boolean colecterMode=false; 
	private static ArrayList<Method> huntedFunctions=new ArrayList<Method>();
	private static ArrayList<Field> huntedVariables=new ArrayList<Field>();
	public static void callAShakedowForAClass(Class clazz){
		PrintUtil.println("Warning! DataStalker has called a shakedow for a class!");
		colecterMode=true;
		huntVariable(clazz, "SomerandomName");
		huntFunction(clazz, "SomerandomName");
		colecterMode=false;
		PrintUtil.println("Info: DataStalker has finished a shakedow for a class!");
	}
	
	public static Method getFunction(Class clazz,String name){
		for(Method a:huntedFunctions)if(a.getName().equals(name))return a;
		Method hunted=huntFunction(clazz, name);
		if(hunted!=null)PrintUtil.println("Info: Function with name: "+hunted.getName()+" in class: "+clazz.getName()+" has been captured.");
		return hunted;
	}
	public static Field getVariable(Class clazz,String name){
		for(Field a:huntedVariables)if(a.getName().equals(name))return a;
		Field hunted=huntVariable(clazz, name);
		if(hunted!=null)PrintUtil.println("Info: Variable with name: "+hunted.getName()+" in class: "+clazz.getName()+" has been captured.");
		return hunted;
	}
	
	public static<T> T getVariable(Class clazz,String name,Object objectForExtractingData){
		Field field=getVariable(clazz, name);
		if(UtilM.isNull(field==null,objectForExtractingData))return null;
		T result=null;
		try{result=(T)field.get(objectForExtractingData);
		}catch(Exception e){e.printStackTrace();}
		return result;
	}
	private static Method huntFunction(Class clazz,String name){
		if(clazz==null||name==null)return null;
		if(name.isEmpty())return null;
		Method[] data=clazz.getMethods();
		if(colecterMode)for(Method a:data)huntedFunctions.add(a);
		Method huntedFunction=null;
		for(Method a:data)if(a.getName().equals(name)){
			huntedFunction=a;
			continue;
		}
		if(huntedFunction!=null){
			huntedFunction.setAccessible(true);
			if(!colecterMode){
				boolean bol=true;
				for(Method a:huntedFunctions)if(a.equals(huntedFunction))bol=false;
				if(bol)huntedFunctions.add(huntedFunction);
			}
		}
		return huntedFunction;
	}
	private static Field huntVariable(Class clazz,String name){
		if(clazz==null||name==null)return null;
		if(name.isEmpty())return null;
		Field[] data=clazz.getDeclaredFields();
		if(colecterMode)for(Field a:data)huntedVariables.add(a);
		Field huntedVariable=null;
		for(Field a:data)if(a.getName().equals(name)){
			huntedVariable=a;
			continue;
		}
		if(huntedVariable!=null){
			huntedVariable.setAccessible(true);
			if(!colecterMode){
				boolean bol=true;
				for(Field a:huntedVariables)if(a.equals(huntedVariable))bol=false;
				if(bol)huntedVariables.add(huntedVariable);
			}
		}
		return huntedVariable;
	}
	
	public static void printResources(){
		if(!huntedFunctions.isEmpty()){
			PrintUtil.println("Captured functions:");
			for(Method fun:huntedFunctions)PrintUtil.println("function:",fun.getName(),"\tclass:"+fun.getClass().getName());
		}else PrintUtil.println("No functions has been captured. Yet...");
		PrintUtil.println(" ");
		if(!huntedVariables.isEmpty()){
			PrintUtil.println("Captured variables:");
			for(Field fun:huntedVariables)PrintUtil.println("function:",fun.getName(),"\tclass:"+fun.getClass().getName());
		}else PrintUtil.println("No variables has been captured. Yet...");
		PrintUtil.println("--------------------------");
	}
	public void setColecterMode(boolean mode){
		colecterMode=mode;
	}
}
