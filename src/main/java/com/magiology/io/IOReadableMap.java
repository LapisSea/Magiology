package com.magiology.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;

import com.magiology.util.utilclasses.UtilM;

public class IOReadableMap{
	
	public Map<String, String> data=new HashMap<String, String>();
	public String fileDir;
	
	public IOReadableMap(String filePath){
		fileDir=FilenameUtils.removeExtension(filePath)+".rhm";//rhm stands for "readable hash map"
	}
	
	private void clear(File file)throws IOException{
		if(file.exists()){
			file.delete();
			file.createNewFile();
		}
	}
	public boolean getB(String string, boolean... onNull){
		String a=data.get(string);
		if(UtilM.isBoolean(a))return Boolean.parseBoolean(a);
		return onNull.length==1?onNull[0]:false;
	}
	
	public int getI(String string, int... onNull){
		String a=data.get(string);
		if(UtilM.isInteger(a))return Integer.parseInt(a);
		return onNull.length==1?onNull[0]:0;
	}
	public String getS(String string, String... onNull){
		String s=data.get(string);
		if(s!=null)return s;
		return onNull.length==1?onNull[0]:"";
	}
	public void readFromFile(){
		try{
			if(!new File(fileDir).exists())new File(fileDir).createNewFile();
			
			BufferedReader br=new BufferedReader(new FileReader(new File(fileDir)));
			List<String> lines=new ArrayList<String>();
			if(br!=null){
				String line=br.readLine();
				while (line!=null){
					lines.add(line);
					line=br.readLine();
				}
			}
			br.close();
			
			for(String string:lines){
				string=string.substring(1, string.length()-1);
				String[] line=string.split('"'+", "+'"');
				data.put(line[0], line[1]);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void set(String tag, boolean Boolean){
		data.put(tag, Boolean+"");
	}
	public void set(String tag, int Int){
		data.put(tag, Int+"");
	}
	public void set(String tag, String string){
		data.put(tag, string);
	}
	
	public void writeToFile(){
		File file=new File(fileDir);
		try{
			clear(file);
			PrintWriter out=new PrintWriter(fileDir);
			try{
				for(Entry<String, String> line:data.entrySet()){
					out.println('"'+line.getKey()+'"'+", "+'"'+line.getValue()+'"');
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

