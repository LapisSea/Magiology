package com.magiology.api.lang.program;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.ObjectHolder;


public class ProgramCommon{
	
	public static Object[] compileArgs(List<DoubleObject<String,Object>> standardVars, String source, ObjectHolder<Integer>... errorPos){
		if(errorPos.length==1)errorPos[0].setVar(-1);
		else if(errorPos.length!=0)throw new IllegalStateException("You can't use more than 1 ObjectHolder's!");
		if(source==null||source.isEmpty())return new Object[0];
		List<Object> result=new ArrayList<Object>();
		
		for(DoubleObject<String,Object> i:standardVars)source=source.replaceAll(i.obj1, i.obj2.toString());
		
		String newline=System.getProperty("line.separator");
		
		source=source.replaceAll(newline, "");
		while(source.contains("  "))source=source.replaceAll("  ", " ");
		source=removeSpacesFrom(source,'{','}','(',')','=',';','*','/','+','-',newline.charAt(0),'%','!','>','<','@','#');
		
		String[] args=source.split(";");
		
		for(int i=0;i<args.length;i++)try{
			String arg=args[i];
			String[] words=arg.split(" ");
			if(words.length==2){
				String type=words[0],value=words[1];
				if(words[0].equals("boolean")){
					if(U.isBoolean(value))result.add(Boolean.parseBoolean(value));
				}
				else if(type.equals("int"))result.add(Integer.parseInt(value));
				else if(type.equals("long"))result.add(Long.parseLong(value));
				else if(type.equals("float"))result.add(Float.parseFloat(value));
				else if(type.equals("double"))result.add(Double.parseDouble(value));
				else if(type.equals("String"))result.add(value);
			}
		}catch(Exception e){
			if(errorPos.length==1)errorPos[0].setVar(i);
		}
		return result.toArray(new Object[result.size()]);
	}
	public static String JSNull(){
		return "undefined";
	}
	
	public static String JSNull(String msg){
		if(msg==null||msg.isEmpty())return JSNull();
		return new StringBuilder("undefined--err<").append(msg).append(">").toString();
	}
	public static String removeSpacesFrom(String raw,char...cs){
		String result=new String(raw);
		for(char c:cs){
			while(result.contains(c+" "))result=result.replace(c+" ", c+"");
			while(result.contains(" "+c))result=result.replace(" "+c, c+"");
		}
		return result;
	}
}
