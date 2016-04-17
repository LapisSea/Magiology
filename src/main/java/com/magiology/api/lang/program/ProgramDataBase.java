package com.magiology.api.lang.program;

import static com.magiology.io.WorldData.WorkingProtocol.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.magiology.io.WorldData;
import com.magiology.io.WorldData.FileContent;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;


public class ProgramDataBase{
	
	public static final String err="<ERROR> (DO NOT RETURN SOMETHING THAT STARTS WITH THIS!): ";
	
	protected static WorldData<String,ProgramSerializable> programs=new WorldData<String,ProgramSerializable>("jsPrograms","js","jsProg",SYNC,FROM_SERVER,SYNC_ON_LOAD,SYNC_ON_CHANGE);
	protected static Map<Integer, ProgramUsable> useablePrograms=new HashMap<Integer, ProgramUsable>();
	
	public static int code_aviableId(){
		int result=1;
		for(Entry<String, FileContent<ProgramSerializable>>i:programs.entrySet()){
			if(i.getKey().equals(result+""))result=Integer.parseInt(i.getKey().toString())+1;
		}
		return result;
	}
	
	public static String code_get(int programId){
		FileContent<ProgramSerializable> code=programs.getFileContent(programId+"");
		
		String codeS=code!=null?code.content!=null?code.content.src.toString():"":"";
		if(codeS.isEmpty())codeS="function main(value){\n	\n	 \n	\n	return \"sucess\";\n}\nfunction init(map){\n	\n}";
		
		return codeS;
	}
	
	public static FileContent code_remove(int programId){
		useablePrograms.remove(programId);
		return programs.removeFile(programId+"");
	}

	//TODO: code------------------------------------------------------------------------------------------
	public static void code_set(int programId, CharSequence programName,CharSequence src){
		if(src==null||src.length()<0)return;
		programs.addFile(programId+"", new ProgramSerializable(programName, src));
		getProgram(programId).init(ProgramDataBase.code_get(programId));
	}
	
	public static ProgramUsable getProgram(int programId){
		ProgramUsable result=useablePrograms.get(programId);
		if(result==null){
			result=new ProgramUsable();
			result.init(ProgramDataBase.code_get(programId));
			useablePrograms.put(programId, result);
		}
		return result;
	}
	
	public static void loadClass(){programs.getFileContent("");}
	
	public static void log(int programId, String string){
		if(string.isEmpty())return;
		getProgram(programId).log(string);
	}
	
	public static void log_clear(int programId){
		getProgram(programId).setLog(new ArrayList<CharSequence>());
	}
	public static List<CharSequence> log_get(int programId){
		List<CharSequence> result=getProgram(programId).getLog();
		if(result==null)result=new ArrayList<CharSequence>();
		if(result.isEmpty())result.add(new StringBuilder());
		return result;
	}
	//TODO: run-------------------------------------------------------------------------------------------
	public static Object run(int programId,Object[] args, TileEntityNetworkProgramHolder holder){
		Object result=null;
		try{
			ProgramUsable program=getProgram(programId);
			result=program.run("main", args, holder);
		}catch(Exception e){
			result=err+e.getMessage();
		}
		if(result!=null)log(programId, result+"");
		return result;
	}
	
}
