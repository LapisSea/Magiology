package com.magiology.core.registry.imp;

import com.magiology.core.registry.AssistantBot.AutomatableCode;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.data.RegistrableDatabaseStorageArray;
import com.magiology.util.statics.UtilM;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public abstract class AutoReferencedRegistry<T> extends RegistrableDatabaseStorageArray<T> implements AutomatableCode{
	
	private final T[]     items;
	private       List<T> registryBuilder;
	private boolean useRawClasses=false;
	
	protected AutoReferencedRegistry(Class<T> t){
		super(t);
		registryBuilder=new ArrayList<>();
		init();
		items=registryBuilder.toArray(UtilM.newArray(t, registryBuilder.size()));
		registryBuilder=null;
	}
	
	protected abstract void init();
	
	@Override
	public T[] getDatabase(){
		return items;
	}
	
	public void add(T t){
		registryBuilder.add(t);
	}
	
	@Override
	public PairM<String,String>[] getMarkers(){
		return new PairM[]{
			new PairM<>("//<GEN:\tINIT START>", "//<GEN:\tINIT END>"),
			new PairM<>("//<GEN:\tREFERENCE START>", "//<GEN:\tREFERENCE END>"),
			new PairM<>("//<GEN:\tIMPORTS START>", "//<GEN:\tIMPORTS END>")
		};
	}
	
	@Override
	public void generate(String src, StringBuilder generated, List<Class> allClasses, int id){
		generated.append("\n");
		Stream<Class> filtered=allClasses.stream().filter(
			clas->!Modifier.isAbstract(clas.getModifiers())&&!Modifier.isInterface(clas.getModifiers())&&UtilM.instanceOf(clas, base));
		
		switch(id){
		case 0:{
			
			List<String> names=new ArrayList<>(), staticNames;
			filtered.forEach(clazz->names.add(clazz.getSimpleName()));
			if(names.isEmpty()) return;
			staticNames=leveledStaticNames(names, true);
			
			for(int i=0; i<names.size(); i++){
				generated.append("\t\tadd(").append(staticNames.get(i)).append("=new ").append(names.get(i)).append("());\n");
			}
			
			generated.append("\t\t");
		}
		break;
		case 1:{
			List<String> names=new ArrayList<>(), staticNames;
			filtered.forEach(clazz->names.add(clazz.getSimpleName()));
			if(names.isEmpty()) return;
			staticNames=leveledNames(names, true);
			
			for(int i=0; i<names.size(); i++){
				generated.append("\tpublic static ").append(staticNames.get(i)).append(' ').append(classNameToCutName(names.get(i)).toUpperCase())
						 .append(";\n");
			}
			
			generated.append("\t");
		}
		break;
		case 2:{
			filtered.forEach(clazz->{
				String importLine="import "+clazz.getName().replace('$', '.')+";\n";
				if(!src.contains(importLine))generated.append(importLine);
			});
		}
		break;
		}
	}
	
	protected List<String> leveledStaticNames(Iterable<String> classes, boolean front){
		List<String> names=new ArrayList<>();
		classes.forEach(name->names.add(classNameToCutName(name).toUpperCase()));
		if(names.isEmpty()) return names;
		
		int maxNameSize=names.stream().mapToInt(String::length).max().orElse(1);
		for(int i=0; i<names.size(); i++){
			String name=names.get(i);
			char[] chars=new char[maxNameSize-name.length()];
			Arrays.fill(chars, ' ');
			names.set(i, front?name+new String(chars):new String(chars)+name);
		}
		return names;
	}
	
	protected List<String> leveledNames(Iterable<String> classes, boolean front){
		List<String> names=new ArrayList<>();
		classes.forEach(names::add);
		if(names.isEmpty()) return names;
		
		int maxNameSize=names.stream().mapToInt(String::length).max().orElse(1);
		for(int i=0; i<names.size(); i++){
			String name=names.get(i);
			char[] chars=new char[maxNameSize-name.length()];
			Arrays.fill(chars, ' ');
			names.set(i, front?name+new String(chars):new String(chars)+name);
		}
		return names;
	}
	
	protected String classNameToCutName(String className){
		return UtilM.classNameToMcName(className);
	}
}
