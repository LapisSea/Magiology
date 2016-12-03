package com.magiology.util.statics;

import static org.apache.logging.log4j.Level.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.Level;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.magiology.core.MReference;

import net.minecraftforge.fml.common.FMLLog;

public class LogUtil{
	
	public static void printFunctionTrace(int count, CharSequence splitter){
		println(getFunctionTrace(count, splitter));
	}
	
	public static String getFunctionTrace(int count, CharSequence splitter){
		StringBuilder line=new StringBuilder();
		
		StackTraceElement[] trace=Thread.currentThread().getStackTrace();
		if(count>=trace.length) count=trace.length-1;
		//		for(StackTraceElement stack:trace){
		//			stack.getMethodName()
		//		}
		for(int i=count+1;i>=2;i--){
			line.append(trace[i].getMethodName()).append('(').append(trace[i].getLineNumber()).append(')');
			if(i!=2) line.append(splitter);
		}
		return line.toString();
	}
	
	/**
	 * print fancy stuff and things
	 */
	public static void printWrapped(Object obj){
		String[] data=UtilM.toString(obj).split("\n");
		StringBuilder line=new StringBuilder();
		
		int length=0;
		for(int i=0;i<data.length;i++){
			String lin=(data[i]=data[i].replaceFirst("\\s+$", ""));
			length=Math.max(length, lin.length());
		}
		
		if(length>6){
			line.append("<<");
			for(int i=0, j=length-4;i<j;i++)
				line.append('=');
			line.append(">>");
		}else for(int i=0;i<length;i++)
			line.append('=');
		
		String lineS=line.toString();
		
		info(lineS);
		for(String lin:data)
			info(lin);
		info(lineS);
	}
	
	public static void println(){
		info("");
	}
	
	public static void println(Object obj){
		info(UtilM.toString(obj));
	}
	
	public static void println(Object...objs){
		info(UtilM.toString(objs));
	}
	
	public static void printPrettyJsonObj(Object objs){
		GsonBuilder gsonBuilder=new GsonBuilder().setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Class.class, (JsonSerializer<Class>)(clazz, a, b)->new JsonPrimitive(clazz.getSimpleName()));
		println(gsonBuilder.create().toJson(objs));
	}
	
	public static <T> T printlnAndReturn(T obj){
		println(obj);
		return obj;
	}
	
	public static void printlnEr(){
		error("");
	}
	
	public static void printlnEr(Object obj){
		error(UtilM.toString(obj));
	}
	
	public static void printlnEr(Object...objs){
		error(UtilM.toString(objs));
	}
	
	public static void printlnInf(){
		info("");
	}
	
	public static void printlnInf(Object obj){
		info(UtilM.toString(obj));
	}
	
	public static void printlnInf(Object...objs){
		info(UtilM.toString(objs));
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
			length=Math.max(s.length(), length);
		}
		for(int b=0;b<length/4;b++)
			result.append("_/\\_");
		
		println(result);
	}
	
	private static void all(String object){
		log(ALL, object);
	}
	
	private static void debug(String object){
		log(DEBUG, object);
	}
	
	private static void error(String object){
		log(ERROR, object);
	}
	
	private static void fatal(String object){
		log(FATAL, object);
	}
	
	private static void info(String object){
		log(INFO, object);
	}
	
	private static void log(Level logLevel, String object){
		String[] lines=object.split(UtilM.LINE_REG);
		for(String line:lines){
			FMLLog.log(MReference.NAME, logLevel, line);
		}
	}
	
	private static void off(String object){
		log(OFF, object);
	}
	
	private static void trace(String object){
		log(TRACE, object);
	}
	
	private static void warn(String object){
		log(WARN, object);
	}
	
}
