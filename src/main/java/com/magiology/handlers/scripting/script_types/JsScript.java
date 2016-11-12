package com.magiology.handlers.scripting.script_types;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import com.magiology.handlers.scripting.Script;
import com.magiology.handlers.scripting.ScriptResult;
import com.magiology.util.objs.LogBuffer;
import com.magiology.util.statics.UtilM;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class JsScript extends Script{
	
	private static JsScript ACTIVE_SCRIPT;
	private static final String SCRIPT_LINE_NAME="__SRCLIN__";
	
	protected List<String> requiredFunctions=new ArrayList<>();
	
	protected ScriptObjectMirror scriptObject;
	protected NashornScriptEngine scriptCore;
	private String lastClassPath="";
	private boolean lastClassPathFailed=false,useLineGuessing=true;

	private LogBuffer out=new LogBuffer(s->{}),err=new LogBuffer(s->{});
	
	public JsScript(String source){
		super(source);
		requiredFunctions.add("main");
	}
	
	public JsScript(String sourcePath, boolean loadNow){
		super(sourcePath, loadNow);
	}
	
	
	private void applyLogging(ScriptContext ctx){
		ctx.setErrorWriter(new Writer(){
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException{
				StringBuilder data=new StringBuilder();
				for(int i=off,j=i+len;i<j;i++)
					data.append(cbuf[i]);
				String dat=data.toString();
				activeWrap.getErr().log(dat);
			}
			
			@Override
			public void flush() throws IOException{}
			
			@Override
			public void close() throws IOException{}
		});
		ctx.setWriter(new Writer(){
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException{
				StringBuilder data=new StringBuilder();
				for(int i=off,j=i+len;i<j;i++)
					data.append(cbuf[i]);
				
				activeWrap.getOut().log(data.toString());
			}
			
			@Override
			public void flush() throws IOException{}
			
			@Override
			public void close() throws IOException{}
		});
		
	}
	
	@Override
	protected ScriptResult compile(){
		
		ScriptEngine engine=new NashornScriptEngineFactory().getScriptEngine(classPath->approveClass(classPath));
		activeWrap.wrapp((BiConsumer<String,Object>)engine::put);
		long exec;
		String errorMsg;
		try{
			String src=getSource();
			if(useLineGuessing){
				
				String n="\n",lines[]=src.split(UtilM.LINE_REG);
				StringBuilder newSrc=new StringBuilder("var "+SCRIPT_LINE_NAME+"=0;").append(n);
				for(int i=0;i<lines.length;i++){
					String line=lines[i];
					newSrc.append(SCRIPT_LINE_NAME).append("=").append(i).append(';').append(n);
					newSrc.append(line).append(n);
				}
				src=newSrc.toString();
				
			}
			UtilM.startTime();
			scriptCore=(NashornScriptEngine)engine;
			scriptCore.eval(src);
			SimpleScriptContext i=(SimpleScriptContext)scriptCore.getContext();
			scriptObject=(ScriptObjectMirror)i.getBindings(ScriptContext.ENGINE_SCOPE);
			
			for(String func:requiredFunctions){
				ScriptObjectMirror o=(ScriptObjectMirror)scriptObject.get(func);
				Exception e=new IllegalStateException("Missing "+func+" function!");
				if(o==null)throw e;
				
				String fun=o.toString().trim();
				
				if(!fun.startsWith("function"))throw e;
				
				fun=fun.substring(8);
				if(!Character.isWhitespace(fun.charAt(0)))throw e;
				fun=fun.trim();
				if(!fun.startsWith(func))throw e;
			}

			return ScriptResult.newComp(UtilM.endTime());
		}catch(Exception e){
			exec=UtilM.endTime();
			errorMsg=errorToMsg(e);
			scriptCore=null;
			scriptObject=null;
			e.printStackTrace();
		}
		return ScriptResult.newComp(exec, -1, errorMsg);
	}
	
	@Override
	protected ScriptResult runScript(){
		ACTIVE_SCRIPT=this;
		synchronized(ACTIVE_SCRIPT){
			long exec;
			String errorMsg;
			
			if(scriptCore!=null){
				try{
					activeWrap.wrapp((BiConsumer<String,Object>)scriptObject::put);
					
					UtilM.startTime();
					
					Object returnObj=scriptCore.invokeFunction("main");
					ACTIVE_SCRIPT=null;
					return ScriptResult.newExec(UtilM.endTime());
				}catch(Exception e){
					exec=UtilM.endTime();
					errorMsg=errorToMsg(e.getCause()==null?e:e.getCause());
					activeWrap.getLog().log(true, errorMsg);
				}
				ACTIVE_SCRIPT=null;
				return ScriptResult.newExec(exec, -10, errorMsg);
			}
		}
		return ScriptResult.newExec(0, -2, "Script not compiled!");
	}
	
	
	protected boolean approveClass(String classPath){
		
		boolean newPath=true;
		if(lastClassPath.length()==classPath.length()){
			newPath=false;
			for(int i=0;i<classPath.length();i++){
				char c1=classPath.charAt(i),c2=lastClassPath.charAt(i);
				//are you different?
				if(c1!=c2){
					//Okay try to fix
					if(c1=='$')c1='.';
					else if(c2=='$')c2='.';
					//Still different?
					if(c1!=c2){
						//k... terminate
						newPath=true;
						break;
					}
				}
			}
		}
		
		if(newPath){
			lastClassPath=classPath;
			activeWrap.getLog().log(false, "Loading class: "+lastClassPath);
		}
		
		lastClassPathFailed=false;
		if(classPath.startsWith("com.magiology.handlers.scripting.bridge"))return true;
		
		lastClassPathFailed=true;
		return false;
	}
	
	protected String errorToMsg(Throwable e){
		String msg="LINE:";
		
		if(e instanceof ScriptException)msg+=((ScriptException)e).getLineNumber();
		else if(useLineGuessing)msg+=scriptCore.get(SCRIPT_LINE_NAME);
		else msg+="unknown";
		
		msg+=" ERROR: ";
		if(lastClassPathFailed&&e instanceof ClassNotFoundException)msg+="Class forbidden: "+lastClassPath;
		else if(e instanceof IllegalStateException){
			
		}
		else{
			e.printStackTrace();
			msg+=e.toString();
		}
		
		return msg;
	}
	
	@Override
	protected String getScriptExtension(){
		return "js";
	}

	public boolean isUsingLineGuessing(){
		return useLineGuessing;
	}
	
	public void setUsingLineGuessing(boolean useLineGuessing){
		compiled=false;
		this.useLineGuessing=useLineGuessing;
	}
}
