package com.magiology.handlers.scripting.bridge;


public class JavaHomeGetter{
	public static String get(){
		return java.lang.System.getProperty("java.home");
	}
}
