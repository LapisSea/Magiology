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
		StringBuilder s=new StringBuilder("ScriptResult ");
		if(!isError)s.append("successfully ");
		s.append("done in ");
		s.append(execTime);
		s.append("ms as ");
		s.append(type);
		s.append(" with result of ");
		s.append(returnCode);
		if(isError){
			s.append(": ");
			s.append(errorMsg);
		}
		return s.toString();
	}
	
}
