package com.magiology.handlers.scripting.script_types;

import com.magiology.handlers.scripting.ScriptResult;

public class RenderScript extends JsScript{
	
	public RenderScript(String source){
		super(source);
		requiredFunctions.add("render");
	}
	
	public RenderScript(String sourcePath, boolean loadNow){
		super(sourcePath, loadNow);
		requiredFunctions.add("render");
	}
	
	public ScriptResult render(){
		return standardFunctionCall("render");
	}
	
}
