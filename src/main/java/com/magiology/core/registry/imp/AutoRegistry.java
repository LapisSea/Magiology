package com.magiology.core.registry.imp;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.magiology.core.registry.AssistantBot.AutomatableCode;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.data.RegistrableClassDatabaseStorageArray;
import com.magiology.util.statics.UtilM;

public abstract class AutoRegistry<T> extends RegistrableClassDatabaseStorageArray<T> implements AutomatableCode{
	
	private final Class<T>[]	items;
	private List<Class<T>>		registryBuilder;
	protected Class<T> base;
	
	protected AutoRegistry(Class<T> t){
		super(t);
		base=t;
		registryBuilder=new ArrayList<>();
		init();
		items=registryBuilder.toArray(UtilM.newArray(t.getClass(), registryBuilder.size()));
		registryBuilder=null;
	}
	
	protected abstract void init();
	
	
	@Override
	public Class<T>[] getDatabase(){
		return items;
	}
	
	@Override
	public PairM<String, String>[] getMarkers(){
		return new PairM[]{
				new PairM<>("//<GEN:\tINIT START>", "//<GEN:\tINIT END>"),
				new PairM<>("//<GEN:\tIMPORTS START>", "//<GEN:\tIMPORTS END>")
		};
	}
	
	public void add(Class<? extends T> t){
		registryBuilder.add((Class<T>)t);
	}
	
	@Override
	public void generate(StringBuilder generated, List<Class> allClasses, int id){
		generated.append("\n");
		Stream<Class> filtered=allClasses.stream().filter(clas->!Modifier.isAbstract(clas.getModifiers())&&!Modifier.isInterface(clas.getModifiers())&&UtilM.instanceOf(clas, base));
		
		switch(id){
		case 0:{
			filtered.forEach(clazz->generated.append("\t\tadd(").append(clazz.getSimpleName()).append(".class);\n"));
			generated.append("\t\t");
		}break;
		case 1:{
			filtered.forEach(clazz->generated.append("import ").append(clazz.getName().replace('$', '.')).append(";\n"));
		}break;
		}
	}
}
