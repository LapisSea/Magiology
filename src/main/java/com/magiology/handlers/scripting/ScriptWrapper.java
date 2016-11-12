package com.magiology.handlers.scripting;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.magiology.util.objs.LogBuffer;
import com.magiology.util.statics.UtilM;

import jdk.nashorn.api.scripting.AbstractJSObject;

public class ScriptWrapper{
	
	private ScriptLog	log	=new ScriptLog();
	private LogBuffer	out	=new LogBuffer(s->log.log(false, s)), err=new LogBuffer(s->log.log(true, s));
	
	public LogBuffer getOut(){
		return out;
	}
	
	public LogBuffer getErr(){
		return err;
	}
	
	public void wrapp(BiConsumer<String, Object> put){
		put.accept("load", (Consumer<String>)this::load);
		put.accept("print", (Consumer<?>)this::print);
	}
	
	public void finish(){
		out.flush();
		err.flush();
		out=err=null;
		log.lock();
	}

	public void print(Object data){
		String print;
		
		if(data instanceof AbstractJSObject)print=jsToString((AbstractJSObject)data);
		else print=UtilM.toString(data);
		
		log.log(false, print);
	}
	
	protected String jsToString(AbstractJSObject obj){
		StringBuilder s=new StringBuilder();
		Iterator<Object> iter=obj.values().iterator();
		
		if(iter.hasNext())while(true){
			Object o=iter.next();
			
			if(o instanceof AbstractJSObject){
				AbstractJSObject jso=(AbstractJSObject)o;
				s.append('[').append(jsToString(jso)).append(']');
			}
			else s.append(UtilM.toString(o));
			
			if(iter.hasNext())s.append(", ");
			else break;
		}
		return s.toString();
	}
	
	public void load(String path){
		throw new UnsupportedOperationException("function \"load\" is not supported!");
	}
	
	public ScriptLog getLog(){
		return log;
	}
}
