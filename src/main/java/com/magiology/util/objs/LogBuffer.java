package com.magiology.util.objs;

import com.magiology.util.interf.ObjectSimpleCallback;
import com.magiology.util.statics.UtilM;

public class LogBuffer{
	
	private StringBuilder buff=new StringBuilder();
	private ObjectSimpleCallback<String> hook;
	
	public LogBuffer(ObjectSimpleCallback<String> hook){
		this.hook=hook;
	}
	
	public void log(CharSequence log){
		buff.append(log);
		logMlutiLine();
	}
	
	public void flush(){
		if(buff.length()==0) return;
		
		if(logMlutiLine()) return;
		
		hook.process(buff.toString());
		buff=new StringBuilder();
	}
	
	private boolean logMlutiLine(){
		if(buff.indexOf(UtilM.LINE_REG)!=-1){
			for(String line : buff.toString().split(UtilM.LINE_REG)) hook.process(line);
			buff=new StringBuilder();
			return true;
		}
		return false;
	}
}
