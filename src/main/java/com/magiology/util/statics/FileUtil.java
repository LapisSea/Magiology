package com.magiology.util.statics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;


public class FileUtil{
	
	public static Object getFileObj(File file){
		try{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(file));
			Object result=null;
			try{
				result=in.readObject();
			}catch(Exception e){
				e.printStackTrace();
			}
			in.close();
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static StringBuilder getFileTxt(File file){
		if(!file.exists()||file.isDirectory())return null;
		StringBuilder result=new StringBuilder();
		BufferedReader br = null;
		try{
			br=new BufferedReader(new FileReader(file));
			String line=null;
			while((line=br.readLine())!=null)result.append(line).append("\n");
			br.close();
		}catch(Exception e){
			e.printStackTrace();
			if(br!=null)try{
				br.close();
			}catch(Exception e1){}
		}
		return result;
	}
	public static void setFileObj(File file,Serializable content){
		try{
			file.createNewFile();
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(file));
			try{
				out.writeObject(content);
			}catch(Exception e){
				LogUtil.println(file);
				e.printStackTrace();
			}
			out.close();
		}catch(Exception e){
			LogUtil.println(file);
			e.printStackTrace();
		}
	}
	
	public static void setFileTxt(File file,String content){
		try{
			file.createNewFile();
			PrintWriter out=new PrintWriter(file);
			out.print(content);
			out.flush();
			out.close();
		}catch(Exception e){
			LogUtil.println(file);
			e.printStackTrace();
		}
	}
}
