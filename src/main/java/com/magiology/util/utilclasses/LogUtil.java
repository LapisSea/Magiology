package com.magiology.util.utilclasses;

import static org.apache.logging.log4j.Level.*;

import org.apache.logging.log4j.Level;

import com.magiology.core.MReference;

import net.minecraftforge.fml.common.FMLLog;

public class LogUtil{
	
	public static void all(Object object){
		log(ALL, object);
	}
	
	public static void debug(Object object){
		log(DEBUG, object);
	}
	
	public static void error(Object object){
		log(ERROR, object);
	}
	
	public static void fatal(Object object){
		log(FATAL, object);
	}
	
	public static void info(Object object){
		log(INFO, object);
	}
	
	private static void log(Level logLevel, Object object){
		String[] lines=object.toString().split("\n");
		for(String line:lines)FMLLog.log(MReference.NAME, logLevel, line);
	}
	
	public static void off(Object object){
		log(OFF, object);
	}
	
	public static void trace(Object object){
		log(TRACE, object);
	}
	
	public static void warn(Object object){
		log(WARN, object);
	}
}