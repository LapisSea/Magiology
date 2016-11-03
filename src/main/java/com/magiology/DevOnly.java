package com.magiology;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InnerClassNode;

import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilM;

import net.minecraft.launchwrapper.IClassTransformer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD,ElementType.CONSTRUCTOR})
public @interface DevOnly{
	
	public boolean shouldCrash() default true;
	
	public String crashMessage() default "<CLASS_NAME> is forbidden from using outside the dev environment!";
	
	public static class DevOnlyBlockCutter implements IClassTransformer{
		
		@Override
		public byte[] transform(String name, String transformedName, byte[] basicClass){
			if(!name.startsWith("com.magiology"))return basicClass;
			ClassNode node=new ClassNode();
			ClassReader classReader=new ClassReader(basicClass);
			classReader.accept(node, 0);
			
			if(removeDevOnly(node))clearClass(node);
			
			
			ClassWriter writer=new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
			node.accept(writer);
			byte[] b=writer.toByteArray();
			
			if(!new String(basicClass).equals(new String(b))){
				System.out.print(new String(basicClass)+"\n");
				System.out.print(new String(b)+"\n");
//				UtilM.exit(404);
			}
			return b;
		}
		private static boolean removeDevOnly(ClassNode node){
			if(combine(
					node.invisibleAnnotations,
					node.invisibleTypeAnnotations,
					node.visibleAnnotations,
					node.visibleTypeAnnotations
			).stream().anyMatch(t->t.desc.endsWith(DevOnly.class.getSimpleName()+";")))return true;
			
			List remover=new ArrayList<>();
			
			if(node.fields!=null){
				node.fields.forEach(f->{
					if(combine(
							f.invisibleAnnotations,
							f.invisibleTypeAnnotations,
							f.visibleAnnotations,
							f.visibleTypeAnnotations
					).stream().anyMatch(t->t.desc.endsWith("DevOnlyBlock;"))){
						remover.add(f);
					}
				});
				node.fields.removeAll(remover);
				remover.clear();
			}
			if(node.methods!=null){
				node.methods.forEach(f->{
					if(combine(
							f.invisibleAnnotations,
							f.invisibleTypeAnnotations,
							f.visibleAnnotations,
							f.visibleTypeAnnotations
					).stream().anyMatch(t->t.desc.endsWith(DevOnly.class.getSimpleName()+";"))){
						remover.add(f);
					}
				});
				node.methods.removeAll(remover);
				remover.clear();
			}
			
			return false;
		}
		private static void clearClass(ClassNode node){
			node.attrs=null;
			node.fields=null;
			node.innerClasses=null;
			node.interfaces=null;
			node.invisibleAnnotations=null;
			node.invisibleTypeAnnotations=null;
			node.methods=null;
			node.visibleAnnotations=null;
			node.visibleTypeAnnotations=null;
		}
		
		private	static <T>List<T> combine(List<?extends T>...lists){
			List<T> l=new ArrayList<>();
			for(int i=0;i<lists.length;i++){
				if(lists[i]!=null)l.addAll(lists[i]);
			}
			return l;
		}
		private	static boolean flag1(List<?> l,Object o){
			return l!=null&&l.contains(o);
		}
	}
}
