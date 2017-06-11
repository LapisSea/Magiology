package com.magiology.handlers.scripting;

import com.magiology.util.objs.LockabaleArrayList;

import java.util.List;

public class ScriptLog{

	private LockabaleArrayList<ScriptLogLine> backDoorLog=new LockabaleArrayList<>(), directLog=new LockabaleArrayList<>(), allLog=new LockabaleArrayList<>();

	public void log(boolean isError, String msg){
		for(String line : msg.split("\n")) log(new ScriptLogLine(line, null, 0, isError));
	}

	public void log(ScriptLogLine line){
		allLog.add(line);
		(line.type==0?directLog:backDoorLog).add(line);
	}

	public void lock(){
		allLog.lock();
		directLog.lock();
		backDoorLog.lock();
	}

	public List<ScriptLogLine> getDirectLog(){
		return directLog;
	}

	public List<ScriptLogLine> getBackDoorLog(){
		return backDoorLog;
	}

	public List<ScriptLogLine> getAllLog(){
		return allLog;
	}

	public static class ScriptLogLine{

		public final String msg, hoverMsg;
		public final int     type;
		public final boolean isError;

		public ScriptLogLine(String msg, String hoverMsg, int type, boolean isError){
			this.msg=msg;
			this.hoverMsg=hoverMsg;
			this.type=type;
			this.isError=isError;
		}

	}

}
