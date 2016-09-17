package com.magiology.util.statics.class_manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.magiology.core.Magiology;
import com.magiology.util.statics.FileUtil;

public class ListCompiler{
	static{if(!Magiology.isDev())throw new IllegalStateException("It is forbidden to compile class list in user environment! This is a bug!");}
	
	private static String startPath="..\\src\\main\\java\\";
	private static int startPathLength=startPath.length();
	private static File thisClass=new File(startPath+getPathToClass(ClassList.class));
	
	static void generateAndInject(){
		try{
			
			List<String> fileNames=getFileNames(new ArrayList<>(), new File(startPath+"com\\magiology").toPath());
			
			List<String> beforeList=new ArrayList<>(),afterList=new ArrayList<>();
			String tabs="\t";
			BufferedReader br=new BufferedReader(new FileReader(thisClass));
			
			StringBuilder originalFile1=new StringBuilder();
			
			boolean listStarted=false,listEnded=false;
			{
				String line=null;
				while((line=br.readLine())!=null){
					originalFile1.append(line).append("\n");
					if(!listStarted){
						beforeList.add(line);
						if(line.endsWith("classes={")){
							listStarted=true;
							int tabCount=0;
							while(line.length()>=tabCount&&line.charAt(tabCount)=='\t'){
								tabCount++;
								tabs+="\t";
							}
						}
					}
					if(!listEnded){
						if(line.endsWith("};"))listEnded=true;
					}
					if(listEnded){
						afterList.add(line);
					}
					
				}
			}
			br.close();
			
			if(afterList.get(afterList.size()-1).isEmpty())afterList.remove(afterList.size()-1);
			
			final StringBuilder newFile=new StringBuilder();
			
			beforeList.forEach(line->newFile.append(line).append("\n"));
			
			for(int i=0;i<fileNames.size();i++){
				String line=fileNames.get(i);
				if(i!=fileNames.size()-1)line+=",";
				char[] line1=line.toCharArray();
				
				newFile.append(tabs);
				for(char element:line1){
					if(element=='\\')newFile.append('.');
					else newFile.append(element);
				}
				newFile.append("\n");
			}
			
			afterList.forEach(line->newFile.append(line).append("\n"));
			
			FileUtil.setFileTxt(thisClass, newFile.toString());
			
		}catch(IOException e){
		}
	}
	
	private static List<String> getFileNames(List<String> fileNames, Path dir){
		try(DirectoryStream<Path> stream=Files.newDirectoryStream(dir)){
			for(Path path:stream){
				if(path.toFile().isDirectory()){
					getFileNames(fileNames, path);
				}else{
					String path1=path.toString();
					if(path1.endsWith(".java"))fileNames.add('"'+path1.substring(startPathLength, path1.length()-5)+"\"");
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return fileNames;
	}
	private static String getPathToClass(Class clazz){
	    return clazz.getName().replace(".", "/") + ".java";
	}
}