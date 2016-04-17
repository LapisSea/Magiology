package com.magiology.api.lang.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.magiology.api.lang.program.ProgramDataBase;
import com.magiology.api.lang.program.ProgramUsable;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class NetworkProgramHolderWrapper{
	private static TileEntityNetworkProgramHolder instance;
	private static List<CharSequence> log;
	public static void print(Object value){
		log.set(log.size()-1, log.get(log.size()-1)+value.toString());
	}
	
	public static void println(Object value){
		ProgramUsable.log(value.toString(), log);
	}
	public static Object run(BlockPosM pos,String name,Object args1){
		try{
			if(instance==null)return "There is no program container! This is an error! Report it to the developer!";
			TileEntityNetworkController brain=instance.getBrain();
			if(brain==null)return "The program container is not connected to a controller!";
			DoubleObject<ProgramUsable,TileEntityNetworkProgramHolder> program=brain.getProgram(name);
			if(program==null)return "Can't find function at: "+pos+"with name: "+name;
			
			List<Object> argsList=new ArrayList<Object>();
			for(String i:((Map<String,Object>)args1).keySet())argsList.add(((Map<String,Object>)args1).get(i));
			
			TileEntityNetworkProgramHolder instance2=instance;
			List<CharSequence> log2=log;
			
			log=ProgramDataBase.getProgram(program.obj1.getProgramId()).getLog();
			
			
			Object result=program.obj1.run(argsList.toArray(),program.obj2);
			
			
			log=log2;
			instance=instance2;
			
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "undefined";
	}
	
	public static void setInstance(TileEntityNetworkProgramHolder instance1){
		instance=instance1;
	}
	public static void setLog(List<CharSequence> log0){
		log=log0;
	}
}