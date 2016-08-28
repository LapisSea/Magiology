package com.magiology.util.statics.class_manager;

import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.google.common.base.Joiner;
import com.magiology.core.MReference;
import com.magiology.util.statics.PrintUtil;
import com.magiology.util.statics.UtilM;

import org.apache.commons.lang3.ArrayUtils;


class ClassFinder{
	
	private static String okToCrashList[]={
			"com.magiology.forge_powered.proxy.ServerProxy",
			"com.magiology.util.statics.classload.ListCompiler"
		};
	
	
	static void load(){
		populate();
		
		Queue<String> sorted=new ArrayDeque<>();
		ClassList.getClassesToSort().forEach((type)->{
			ClassList.implementations.put(type, new ArrayList<>());
			ClassList.directImplementations.put(type, new ArrayList<>());
			exploreType(type);
			
			sorted.add(type.getSimpleName());
		});
		ClassList.getClassesToSort().clear();
		
		if(!sorted.isEmpty())PrintUtil.println(Joiner.on(", ").appendTo(new StringBuilder(sorted.size()==1?"Sorted class: ":"Sorted classes: "), sorted));
	}
	private static void exploreType(Class targetType){
		List<Class<?>> impl=ClassList.implementations.get(targetType);
		List<Class<?>> dirImpl=ClassList.directImplementations.get(targetType);
		
		for(Class<?> type:ClassList.allClasses){
			if(targetType!=type&&UtilM.instanceOf(type,targetType)&&!Modifier.isAbstract(type.getModifiers())&&!Modifier.isInterface(type.getModifiers()))impl.add(type);
			if(type.getSuperclass()==targetType)dirImpl.add(type);
		}
	}
	
	private static void populate(){
		if(ClassList.allClasses.isEmpty()){
			List<String> failed=new ArrayList<>();
			ClassLoader loader=ClassFinder.class.getClassLoader();
			PrintUtil.println("Starting to load all classes from "+MReference.NAME+"!");
			UtilM.startTime();
			
			for(String clazz:ClassList.classes){
				exploreAndLoadClass(loader,failed,clazz,null);
			}
			//set array to null so garbage collection can free more RAM
			ClassList.classes=null;
			
			PrintUtil.println("Loading of all classes is done in "+UtilM.endTime()+"ms.");
			if(failed.isEmpty())PrintUtil.println("Generated class list is ok! ^_^");
			else{
				PrintUtil.println("Something changed! Class list or black list needs to be updated!");
				PrintUtil.println("Failed class list:");
				for(String string:failed)PrintUtil.println(string);

				PrintUtil.println("\nUpdating list...");
				ListCompiler.generateAndInject();
				PrintUtil.println("List updated!");
				
				UtilM.exit(36042069);//https://goo.gl/B4FxBe
			}
		}
	}
	private static void exploreAndLoadClass(ClassLoader loader,List<String> failed,String clazz,Class clazs){
		try{
			if(clazz==null){
				ClassList.allClasses.add(clazs);
				for(Class c:clazs.getDeclaredClasses()){
					exploreAndLoadClass(loader,failed,null,c);
				}
			}else{
				exploreAndLoadClass(loader,failed,null,loader.loadClass(clazz));
			}
			
		}catch(Exception e){
			if(!ArrayUtils.contains(okToCrashList, clazz))failed.add(e.getClass().getSimpleName()+": "+clazz);
		}
	}
	
	
}
