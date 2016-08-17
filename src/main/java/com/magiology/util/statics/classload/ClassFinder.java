package com.magiology.util.statics.classload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.util.statics.FileUtil;
import com.magiology.util.statics.PrintUtil;
import com.magiology.util.statics.UtilM;

import org.apache.commons.lang3.ArrayUtils;


class ClassFinder{
	
	private static boolean compiled=false;
	private static String 
		startPath="..\\src\\main\\java\\",
		blacklist[]={
				"com.magiology.forge_powered.proxy.ServerProxy"
		};
	
	private static int startPathLength=startPath.length(),iterationCount=0;
	private static File thisClass=new File(startPath+getPathToClass(ClassList.class));
	
	static void load(){
		if(compiled)return;
		compiled=true;
		if(Magiology.isDev())generateAndInject();
		
		ClassLoader loader=ClassFinder.class.getClassLoader();
		PrintUtil.println("Starting to load all classes from "+MReference.NAME+"!");
		UtilM.startTime();
		List<String> failed=new ArrayList<>();
		
		for(String clazz:ClassList.classes){
			try{
				ClassList.loadedClasses.add(loader.loadClass(clazz));
			}catch(Exception e){
				if(!ArrayUtils.contains(blacklist, clazz))failed.add(e.getClass().getSimpleName()+": "+clazz);
			}
		}
		//set array to null so garbage collection can free more RAM
		ClassList.classes=null;
		
		PrintUtil.println("Loading of all classes is done in "+UtilM.endTime()+"ms.");
		if(failed.isEmpty()){
			PrintUtil.println("No classes have failed to load! ^_^\nStarting to sort classes!");
			
			UtilM.startTime();
			
			for(Class<?> instance:ClassList.getClassesToSort()){
				List<Class<?>> list=new ArrayList<>();
				ClassList.implementations.put(instance, list);
				for(Class<?> clazz:ClassList.loadedClasses){
					if(clazz!=instance&&UtilM.instanceOf(clazz, instance)){
						list.add(clazz);
						iterationCount++;
					}
					iterationCount++;
				}
			}
			ClassList.implementations.keySet().forEach(key->{
				ClassList.directImplementations.put(key, new ArrayList<>());
				iterationCount++;
			});
			
			Map<Class<?>, List<Class<?>>> toRemove1=new HashMap<>();
			ClassList.implementations.forEach((k,v)->v.forEach(clazz1->v.forEach(clazz2->{
				iterationCount++;
				if(clazz1!=clazz2){
					if(UtilM.instanceOf(clazz1, clazz2)){
						List<Class<?>> list=toRemove1.get(k);
						if(list==null)toRemove1.put(k, list=new ArrayList<>());
						list.add(clazz1);
					}
					if(UtilM.instanceOf(clazz2, clazz1)){
						List<Class<?>> list=toRemove1.get(k);
						if(list==null)toRemove1.put(k, list=new ArrayList<>());
						list.add(clazz2);
					}
				}
			})));
			ClassList.implementations.forEach((k,v)->v.forEach(clazz->{
				ClassList.directImplementations.get(k).add(clazz);
				iterationCount++;
			}));
			toRemove1.forEach((k,v)->v.forEach(clazz->ClassList.directImplementations.get(k).remove(clazz)));
			
			List<List<Class<?>>> toRemove2=new ArrayList<>();
			ClassList.directImplementations.values().forEach(list->{
				if(list.isEmpty())toRemove2.add(list);
				iterationCount++;
			});
			toRemove2.forEach(list->{
				ClassList.directImplementations.remove(list);
				iterationCount++;
			});
			
			PrintUtil.println("Classes are sucessfuly sorted in:",UtilM.endTime()+"ms with",iterationCount+" iterations!\nResult:");
			PrintUtil.println("\tDirect extensions:");
			ClassList.directImplementations.forEach((k,v)->PrintUtil.println("\t\tClass:",k.getSimpleName(),"-> count:",v.size()));
			PrintUtil.println("\tExtensions:");
			ClassList.implementations.forEach((k,v)->PrintUtil.println("\t\tClass:",k.getSimpleName(),"-> count:",v.size()));
			
			setError(false);
		}
		else{
			setError(true);
			PrintUtil.println("But some classes have failed to load!");
			PrintUtil.println("This is not fatal or a big deal but it coud cause some problems in rare cases.");
			PrintUtil.println("Failed class list:");
			for(String string:failed)PrintUtil.println(string);
			PrintUtil.println("You may want to refresh the class list!");
			UtilM.exit(360);
		}
	}
	private static void generateAndInject(){
		if(!ClassList.error||!Magiology.isDev())return;
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
			
			String originalFile=originalFile1.toString();
			
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
			
			if(!newFile.equals(originalFile)){
				FileUtil.setFileTxt(thisClass, newFile.toString());
				setError(false);
				UtilM.exit(420);
			}
			
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
	
	private static void setError(boolean b){
		if(!Magiology.isDev())return;
			
		StringBuilder clazzB=FileUtil.getFileTxt(thisClass);
		String clazz=clazzB.toString();
		
		int start=clazz.indexOf("error=")+"error=".length(),end=start;
		while(clazz.charAt(end)!=';')end++;
		
		String newClass=clazzB.replace(start, end, ""+b).toString();
		FileUtil.setFileTxt(thisClass, newClass);
	} 
	
	
}
