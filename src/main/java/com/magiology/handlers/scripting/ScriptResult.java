package com.magiology.handlers.scripting;

public class ScriptResult{
	
	public static enum Type{
		COMPILE(0),
		EXECUTE(1);
		
		public final int id;
		private Type(int id){
			this.id=id;
		}
	}
	
	public static ScriptResult newComp(long execTime){
		return new ScriptResult(Type.COMPILE, execTime, 0);
	}
	public static ScriptResult newComp(long execTime, int returnCode, String errorMsg){
		return new ScriptResult(Type.COMPILE, execTime, returnCode,errorMsg);
	}
	public static ScriptResult newExec(long execTime){
		return new ScriptResult(Type.EXECUTE, execTime, 0);
	}
	public static ScriptResult newExec(long execTime, int returnCode, String errorMsg){
		return new ScriptResult(Type.EXECUTE, execTime, returnCode,errorMsg);
	}

	public final int		returnCode;
	public final long		execTime;
	public final boolean	isError;
	public final String		errorMsg;
	public final Type		type;
	
	public ScriptResult(Type type, long execTime, int returnCode){
		this(type, execTime, returnCode,null);
	}
	public ScriptResult(Type type, long execTime, int returnCode, String errorMsg){
		this(type, execTime, returnCode, errorMsg!=null, errorMsg);
	}
	private ScriptResult(Type type, long execTime, int returnCode, boolean isError, String errorMsg){
		this.returnCode=returnCode;
		this.isError=isError;
		this.errorMsg=errorMsg;
		this.type=type;
		this.execTime=execTime;
	}
	
	@Override
	public String toString(){
		if(isError)return "ScriptResult done in "+execTime+" as "+type+" with result of "+returnCode+": "+errorMsg;
		return "ScriptResult successfully done in "+execTime+" as "+type+" with result of "+returnCode;
	}
	
}
