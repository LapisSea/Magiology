package com.magiology.util.statics.class_manager;

import java.io.*;
import java.nio.file.*;
import java.util.*;

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
						if(line.endsWith("classes=new String[]{")){
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
//			PrintUtil.println("Old size:",thisClass.length());
			FileUtil.setFileTxt(thisClass, newFile.toString());
//			PrintUtil.println("New size:",thisClass.length());
//			
//			//reload class at running time
//			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//			PrintUtil.println(compiler.run(null, null, null, thisClass.getPath()));
//			PrintUtil.println(ListCompiler.class.getResource("com"));
//			// Load and instantiate compiled class.
//			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("src/main/java").toURI().toURL() });
//			try{
//				Class<?> cls = Class.forName(ClassList.class.getName(), true, classLoader);
//				cls.newInstance();
//			}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
//				e.printStackTrace();
//			}
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
					if(path1.endsWith(".java")&&!path1.endsWith("package-info.java"))fileNames.add('"'+path1.substring(startPathLength, path1.length()-5)+"\"");
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