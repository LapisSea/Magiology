package com.magiology.handlers.scripting;

import com.magiology.core.Magiology;
import com.magiology.io.IOManager.IODirectory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Script{
	
	protected final        IODirectory         dir      =Magiology.EXTRA_FILES.getDirectoryManager("scripts");
	protected static final String              NULL_PATH="no_path_script";
	protected              List<ScriptWrapper> logs     =new ArrayList<>();
	protected boolean compiled;
	private   String  sourcePath, source;
	
	public Script(String sourcePath, boolean loadNow){
		this.sourcePath=sourcePath;
		if(loadNow) load();
	}
	
	public Script(String source){
		this.source=source;
	}
	
	public String getBasePath(){
		return sourcePath==null?NULL_PATH:sourcePath;
	}
	
	protected void load(){
		compiled=false;
		
		source=dir.readOr(getBasePath()+"."+getScriptExtension(), "");
	}
	
	protected abstract ScriptResult compile(ScriptWrapper activeWrap);
	
	protected abstract ScriptResult runMain(ScriptWrapper activeWrap);
	
	protected abstract String getScriptExtension();
	
	protected ScriptWrapper newScriptWrapper(){
		return new ScriptWrapper();
	}
	
	public ScriptResult callMain(){
		ScriptResult compile=preRun();
		if(compile!=null) return compile;
		
		ScriptWrapper activeWrap=newScriptWrapper();
		logs.add(activeWrap);
		ScriptResult result=runMain(activeWrap);
		return result;
	}
	
	protected ScriptResult preRun(){
		
		while(logs.size()>20) logs.remove(20);
		
		if(!compiled){
			compiled=true;
			ScriptWrapper activeWrap=newScriptWrapper();
			logs.add(activeWrap);
			ScriptResult result=compile(activeWrap);
			activeWrap.getLog().log(result.isError, result.toString());
			if(result.isError) return result;
		}
		return null;
	}
	
	public List<ScriptWrapper> getLogs(){
		return Collections.unmodifiableList(logs);
	}
	
	public String getSource(){
		return source;
	}
	
	public void setSource(String source){
		this.source=source;
	}
}
