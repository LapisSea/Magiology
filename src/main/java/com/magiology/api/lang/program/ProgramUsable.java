package com.magiology.api.lang.program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.magiology.api.lang.bridge.NetworkProgramHolderWrapper;
import com.magiology.api.lang.bridge.WorldWrapper;
import com.magiology.io.WorldData.FileContent;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ProgramUsable{
	
	public static final String jsJavaBridge=new Object(){@Override
	public String toString(){
		StringBuilder txt=new StringBuilder();
		//functions
		add(txt,"function run_funct(pos, name, args){");
		add(txt,"return Java.type('"+NetworkProgramHolderWrapper.class.getName()+"').run(pos, name, args);");
		add(txt,"}");
		
		add(txt,"function print(value){");
		add(txt,"return Java.type('"+NetworkProgramHolderWrapper.class.getName()+"').print(value);");
		add(txt,"}");
		add(txt,"function println(value){");
		add(txt,"return Java.type('"+NetworkProgramHolderWrapper.class.getName()+"').println(value);");
		add(txt,"}");
		
		//classes
		add(txt,"World=Java.type('"+WorldWrapper.class.getName()+"');");
		add(txt,"BlockPos=Java.type('"+BlockPosM.class.getName()+"');");
		add(txt,"EnumFacing=Java.type('"+EnumFacing.class.getName()+"');");
		//vars
		add(txt,"runPos=\"undefined\";");
		
//		add(txt,"");
		
		return txt.toString();
	}}.toString();
	public static final int jsJavaBridgeLines=jsJavaBridge.split("\n").length;
	private static void add(StringBuilder txt, String line){
		txt.append(line).append("\n");
	}
	
	public static Invocable compile(CharSequence src) throws ScriptException{
		ScriptEngine engine=new ScriptEngineManager(null).getEngineByName("nashorn");
		engine.eval(jsJavaBridge+src.toString());
		return (Invocable)engine;
		
	}
	
	public static void log(String newLog, List<CharSequence> log){
		String side=FMLCommonHandler.instance().getEffectiveSide().toString();
		String[] Log=newLog.split("\n");
		for(String line:Log){
			log.add("["+side+"]: "+line);
		}
		while(log.size()>100){
			log.remove(0);
		}
	}
	
	
	public static Object run(ProgramUsable program, String mainFuncName, Object[] args, TileEntityNetworkProgramHolder holder) throws NoSuchMethodException, ScriptException{
		Map<String, Object> map=new HashMap<String, Object>();
		
		map.put("runPos", holder.getPos());
		((ScriptEngine)program.getCompiled()).put("runPos", holder.getPos());
		WorldWrapper.setBlockAccess(holder.getWorld());
		NetworkProgramHolderWrapper.setInstance(holder);
		
		try{
			program.getCompiled().invokeFunction("init", map);
		}catch(Exception e){
			log(ProgramDataBase.err+e.getMessage(),program.getLog());
		}
		
		return runProgram(program, mainFuncName, args);
	}
	private static Object runProgram(ProgramUsable program, String mainFuncName, Object[] args) throws NoSuchMethodException, ScriptException{
		try{
			Object o=program.lastResult=program.compiled.invokeFunction(mainFuncName, args);
			program.lastResult=o;
			return o;
		} catch (NoSuchMethodException|ScriptException e){
			program.lastResult=e;
			throw e;
		}
	}
	
	
	private Invocable compiled;
	
	public Object lastResult;
	private List<CharSequence> log=new ArrayList<CharSequence>();
	private int programId;
	
	private ProgramSerializable saveableData;
	
	public Invocable getCompiled(){return compiled;}
	public List<CharSequence> getLog(){if(log==null)log=new ArrayList<CharSequence>();return log;}

	public int getProgramId(){
		return programId;
	}
	public ProgramSerializable getSaveableData(){
		if(saveableData==null){
			int id=UtilM.getMapKey(ProgramDataBase.useablePrograms, this);
			FileContent i=ProgramDataBase.programs.getFileContent(id+"");
			saveableData=i==null?new ProgramSerializable("", ""):(ProgramSerializable)i.content;
		}
		return saveableData;
	}
	
	
	
	public void init(CharSequence src){
		try{
			compiled=compile(src);
		}catch(ScriptException e){
			lastResult=e;
		}
	}
	public void log(String newLog){
		log(newLog, getLog());
	}
	
	public Object run(Object[] args, TileEntityNetworkProgramHolder holder){
		Object result=run("main", args, holder);
		return result;
	}
	
	public Object run(String mainFuncName, Object[] args, TileEntityNetworkProgramHolder holder){
		try{
			return run(this, mainFuncName, args, holder);
		}catch(Exception e){
			return e;
		}
	}
	public void setLog(List<CharSequence> log){this.log=log;}
}
