package com.magiology.handlers.scripting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.magiology.core.Magiology;
import com.magiology.io.IOManager.IODirectory;

public abstract class Script{
	
	protected final IODirectory		dir			=Magiology.extraFiles.getDirectoryManager("scripts");
	protected static final String	NULL_PATH	="no_path_script";
	protected List<ScriptWrapper>	logs		=new ArrayList<>();
	protected ScriptWrapper			activeWrap;
	private boolean					running;
	protected boolean				compiled;
	private String					sourcePath, source;
	
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
	
	protected abstract ScriptResult compile();
	
	protected abstract ScriptResult runScript();
	
	protected abstract String getScriptExtension();
	
	protected ScriptWrapper newScriptWrapper(){
		return new ScriptWrapper();
	}
	
	public ScriptResult run(){
		if(running) throw new IllegalStateException("This script is already running");
		running=true;
		
		while(logs.size()>20)
			logs.remove(20);
		
		if(!compiled){
			compiled=true;
			activeWrap=newScriptWrapper();
			logs.add(activeWrap);
			ScriptResult result=compile();
			activeWrap.finish();
			if(result.isError) return result;
		}
		activeWrap=newScriptWrapper();
		logs.add(activeWrap);
		ScriptResult result=runScript();
		activeWrap.finish();
		running=false;
		return result;
	}
	
	public List<ScriptWrapper> getLogs(){
		return Collections.unmodifiableList(logs);
	}

	public boolean isRunning(){
		return running;
	}
	
	public String getSource(){
		return source;
	}
	
	public void setSource(String source){
		this.source=source;
	}
}
