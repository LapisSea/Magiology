package com.magiology.util.statics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrintUtil{
	
	public static void println(){
		LogUtil.info("");
	}
	public static void println(Object obj){
		LogUtil.info(UtilM.toString(obj));
	}
	public static void println(Object... objs){
		LogUtil.info(UtilM.toString(objs));
	}
	
	public static<T> T printlnAndReturn(T obj){
		println(obj);
		return obj;
	}
	public static void printlnEr(){
		LogUtil.error("");
	}
	public static void printlnEr(Object obj){
		LogUtil.error(UtilM.toString(obj));
	}
	
	public static void printlnEr(Object... objs){
		LogUtil.error(UtilM.toString(objs));
	}
	public static void printlnInf(){
		LogUtil.info("");
	}
	public static void printlnInf(Object obj){
		LogUtil.info(UtilM.toString(obj));
	}
	
	public static void printlnInf(Object... objs){
		LogUtil.info(UtilM.toString(objs));
	}

	public static void printStackTrace(){
		StringBuilder result=new StringBuilder();
		
		StackTraceElement[] a1=Thread.currentThread().getStackTrace();
		int length=0;
		DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal=Calendar.getInstance();
		result.append("Invoke time: ").append(dateFormat.format(cal.getTime())).append("\n");
		for(int i=2;i<a1.length;i++){
			StackTraceElement a=a1[i];
			String s=a.toString();
			result.append(s).append("\n");
			length=Math.max(s.length(),length);
		}
		for(int b=0;b<length/4;b++)result.append("_/\\_");
		
		println(result);
	}
}
