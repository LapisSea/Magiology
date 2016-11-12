package com.magiology.handlers.scripting;

import java.util.List;

import com.magiology.util.objs.LockabaleArrayList;

public class ScriptLog{
		
		private boolean locked=false;
		
		private LockabaleArrayList<ScriptLogLine> backDoorLog=new LockabaleArrayList<>(),
				directLog=new LockabaleArrayList<>(),
				allLog=new LockabaleArrayList<>();
		
		public void log(boolean isError, String msg){
			if(locked) return;
			for(String line:msg.split("\n")){
				log(new ScriptLogLine(line, null, 0,isError));
			}
		}
		
		public void log(ScriptLogLine line){
			if(locked) return;
			allLog.add(line);
			(line.type==0?directLog:backDoorLog).add(line);
		}
		
		public void lock(){
			locked=true;
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
			
			public final String		msg,hoverMsg;
			public final int		type;
			public final boolean	isError;
			
			public ScriptLogLine(String msg, String hoverMsg, int type, boolean isError){
				this.msg=msg;
				this.hoverMsg=hoverMsg;
				this.type=type;
				this.isError=isError;
			}
			
		}
		
	}