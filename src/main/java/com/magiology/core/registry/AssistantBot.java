package com.magiology.core.registry;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.magiology.core.MReference;
import com.magiology.util.objs.PairM;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilM;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/*{
  "last update": 1485974032322
}*/

/**
 *
 * This is a class that makes development a bit faster and easier as it abstracts and automates the
 * process of registration with a clean code generation. The code is the same in terms of performance/load
 * time when compared to how you would manually register something.<br>
 * "Life improvement class"
 *
 * @author LapisSea
 */
public class AssistantBot{
	
	static{
		LogUtil.println(AssistantBot.class.getSimpleName(), "class loaded!");
	}
	
	public interface AutomatableCode{
		
		PairM<String,String>[] getMarkers();
		
		void generate(String src, final StringBuilder generated, List<Class> allClasses, int id);
		
	}
	
	private static final List<String> fails=Lists.newArrayList(MReference.SERVER_PROXY_LOCATION, MReference.CLIENT_PROXY_LOCATION);
	
	private static final String	ROOT		="../src/main/java/";
	private static final File	thisClass	=getFileFromClass(AssistantBot.class);
	
	private static Map<String,Object>	BOT_DATA;
	private static String				THIS_CLASS_SRC;
	private static int					START,END;
	private static boolean				DIRTY	=false;
	
	static void run(){
		UtilM.startTime();
		detectAndUpdate();
		end();
	}
	
	private static void end(){
		LogUtil.println("Assistant bot finished in:", UtilM.endTime());
	}
	
	private static void detectAndUpdate(){
		LogUtil.println("Searching for new files...");
		long lastFileCreation=getJavaFiles().stream().mapToLong(AssistantBot::detectCreationTime).max().orElse(0);
		LogUtil.println("Reading bot data...");
		readData();
		long lastUpdate=((Number)BOT_DATA.get("last update")).longValue();
		
		if(lastFileCreation>lastUpdate){
			LogUtil.println("Change detected!");
			
			build();
			
			BOT_DATA.put("last update", lastFileCreation);
			
			LogUtil.println("Writing bot data...");
			writeData();
			if(DIRTY){
				LogUtil.println("Source file/s were changed. MC will need to restart!");
				end();
				UtilM.exit(0);
			}
		}
		LogUtil.println("No changes were made. Continuing launching...");
	}
	
	private static void build(){
		ClassLoader loader=AssistantBot.class.getClassLoader();
		List<Class> allClasses=new ArrayList<>();
		getJavaFiles().forEach(file->{
			String classPath=file.getPath();
			classPath=classPath.substring(ROOT.length(), classPath.length()-5).replaceAll("(/|\\\\)", ".");
			try{
				exploreAndLoadClass(allClasses, loader.loadClass(classPath));
			}catch(Exception e){
				if(!fails.contains(classPath)) throw new RuntimeException(e);
			}
		});
		allClasses.stream().filter(clas->clas.getName().startsWith("com.magiology.core.registry.init.")&&UtilM.instanceOf(clas, AutomatableCode.class)).forEach(clas->{
			try{
				Path classPath=getFileFromClass(clas).toPath();
				
				Constructor constructor=clas.getDeclaredConstructor();
				constructor.setAccessible(true);
				AutomatableCode autoCode=(AutomatableCode)constructor.newInstance();
				
				PairM<String,String>[] markers=autoCode.getMarkers();
				String src=new String(Files.readAllBytes(classPath));
				
				String newSrc=src;
				
				for(int i=0;i<markers.length;i++){
					PairM<String,String> marker=markers[i];
					int startPos=src.indexOf(marker.obj1),endPos=src.indexOf(marker.obj2);
					if(startPos==-1) throw new IllegalStateException("Can't find "+marker.obj1+" in "+clas.getName());
					if(endPos==-1) throw new IllegalStateException("Can't find "+marker.obj2+" in "+clas.getName());
					startPos+=marker.obj1.length();
					
					StringBuilder generated=new StringBuilder();
					autoCode.generate(src, generated, allClasses, i);
					
					newSrc=newSrc.substring(0, startPos)+generated.toString()+newSrc.substring(endPos, newSrc.length());
				}
				
				if(!newSrc.equals(src)){
					Files.write(classPath, newSrc.getBytes());
					DIRTY=true;
				}
				
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		});
	}
	
	private static void exploreAndLoadClass(List<Class> classes, Class<?> clazs){
		classes.add(clazs);
		for(Class c:clazs.getDeclaredClasses()){
			exploreAndLoadClass(classes, c);
		}
	}
	
	private static void readData(){
		
		try{
			THIS_CLASS_SRC=new String(Files.readAllBytes(thisClass.toPath()));
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		
		boolean started=false;
		char lastChar=' ';
		
		for(int i=0;i<THIS_CLASS_SRC.length();i++){
			char ch=THIS_CLASS_SRC.charAt(i);
			if(lastChar=='*'&&ch=='/'){
				END=i-1;
				break;
			}
			
			if(lastChar=='/'&&ch=='*'){
				START=i+1;
				started=true;
			}
			lastChar=ch;
		}
		
		BOT_DATA=new Gson().fromJson(THIS_CLASS_SRC.substring(START, END), new TypeToken<Map<String,Object>>(){}.getType());
	}
	
	private static void writeData(){
		String newClassSrc=THIS_CLASS_SRC.substring(0, START)+new GsonBuilder().setPrettyPrinting().create().toJson(BOT_DATA)+
				THIS_CLASS_SRC.substring(END, THIS_CLASS_SRC.length());
		try{
			Files.write(thisClass.toPath(), newClassSrc.getBytes());
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	private static long detectCreationTime(File path){
		try{
			return Files.readAttributes(path.toPath(), BasicFileAttributes.class).creationTime().toMillis();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	private static final Predicate<File> IS_FOLDER=file->file.isDirectory(),IS_JAVA_FILE=file->{
		if(file.isDirectory()) return false;
		String path1=file.toString();
		return path1.endsWith(".java")&&!path1.endsWith("package-info.java");
	};
	
	private static List<File> getJavaFiles(){
		return getJavaFiles(new ArrayList<>(), new File(ROOT));
	}
	
	private static List<File> getJavaFiles(List<File> fileList, File dir){
		
		List<File> files=Lists.newArrayList(dir.listFiles());
		
		files.stream().filter(IS_FOLDER).forEach(file->getJavaFiles(fileList, file));
		files.stream().filter(IS_JAVA_FILE).forEach(fileList::add);
		return fileList;
	}
	
	private static File getFileFromClass(Class clazz){
		return new File(ROOT+clazz.getName().replace(".", "/")+".java");
	}
}
