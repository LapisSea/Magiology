package com.magiology.handlers.scripting.script_types;

import com.magiology.handlers.scripting.ScriptResult;

public class RenderNUpdateScript extends RenderScript{

	public RenderNUpdateScript(String source){
		super(source);
		requiredFunctions.add("update");
	}
	
	public RenderNUpdateScript(String sourcePath, boolean loadNow){
		super(sourcePath, loadNow);
		requiredFunctions.add("update");
	}
	
	public ScriptResult update(){
		return standardFunctionCall("update");
	}
	
}
