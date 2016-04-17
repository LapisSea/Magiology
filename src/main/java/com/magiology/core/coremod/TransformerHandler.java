package com.magiology.core.coremod;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.magiology.core.coremod.transformers.ClassTransformerBase;
import com.magiology.core.init.classload.ClassList;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.DoubleObject;

import net.minecraft.launchwrapper.IClassTransformer;

public class TransformerHandler implements IClassTransformer{
	
	//big thanks to https://www.youtube.com/watch?v=75_rJYLj5AU ClassTransformerBase.class
	private List<ClassTransformerBase> transformers;
	
	private void initTransformers(){
		transformers=new ArrayList<>();
		ClassList.getImplementations().get(ClassTransformerBase.class).forEach(clazz->{
			if(clazz!=ClassTransformerBase.class){
				try{
					transformers.add((ClassTransformerBase)clazz.newInstance());
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public byte[] transform(String name, String deobfuscatedName, byte[] classBytecode){
		ClassTransformerBase.isObfuscated=!name.equals(deobfuscatedName);
		if(transformers==null)initTransformers();
		
		ClassNode node=new ClassNode();
		ClassReader classReader=new ClassReader(classBytecode);
		classReader.accept(node, 0);
		
		for(ClassTransformerBase transformer:transformers){
			String[] clazzes=transformer.getTransformingClasses();
			List<DoubleObject<ClassNode, Integer>> toTransform=new ArrayList<>();
			
			for(int i=0;i<clazzes.length;i++){
				if(clazzes[i].equals(deobfuscatedName)){
					toTransform.add(new DoubleObject<ClassNode, Integer>(node, i));
				}
			}
			
			if(!toTransform.isEmpty()){
				String debuginfo=transformer.getDebudInfo();
				if(debuginfo==null||debuginfo.isEmpty())debuginfo="Class transformer "+transformer.getClass().getSimpleName()+" has no description!";
				PrintUtil.println(debuginfo);
				
				for(DoubleObject<ClassNode, Integer> data:toTransform){
					transformer.transform(data.obj1, data.obj2);
				}
			}
		}
		
		ClassWriter writer=new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		return writer.toByteArray();
	}
}
