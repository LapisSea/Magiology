package com.magiology.util.utilclasses;

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
		try{
			BufferedReader br=new BufferedReader(new FileReader(file));
			StringBuilder result=new StringBuilder();
			String line=null;
			while((line=br.readLine())!=null)result.append(line).append("\n");
			br.close();
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static void setFileObj(File file,Serializable content){
		try{
			file.createNewFile();
			ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(file));
			try{
				out.writeObject(content);
			}catch(Exception e){
				PrintUtil.println(file);
				e.printStackTrace();
			}
			out.close();
		}catch(Exception e){
			PrintUtil.println(file);
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
			PrintUtil.println(file);
			e.printStackTrace();
		}
	}
}
