package com.magiology.util.objs.animation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import com.magiology.core.Magiology;
import com.magiology.util.statics.LogUtil;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimationBank{
	
	private static final List<Anim> animations=new ArrayList<>();
	
	private static class Anim{
		private final AnimationM instance;
		private final String srcName,srcNameLower;
		private final int accessId;
		
		public Anim(AnimationM instance, String srcName, int accessId){
			this.instance=instance;
			this.srcName=srcName;
			this.srcNameLower=srcName.toLowerCase();
			this.accessId=accessId;
		}
	}
	
	public static AnimationMReference getExact(String name){
		String name0=name.toLowerCase();
		return new AnimationMReference(stream(ent->ent.srcNameLower.equals(name0)).findFirst().get().accessId);
	}
	public static AnimationMReference getByName(String name){
		String name0=name.toLowerCase();
		return new AnimationMReference(stream(ent->ent.srcNameLower.contains(name0)).findFirst().get().accessId);
	}
	public static Map<String, AnimationMReference> getAllWith(String name){
		String name0=name.toLowerCase();
		Map<String, AnimationMReference> result=new HashMap<>();
		stream(ent->ent.srcNameLower.contains(name0)).forEach(ent->result.put(ent.srcName, new AnimationMReference(ent.accessId)));
		return result;
	}
	
	private static Stream<Anim> stream(Predicate<? super Anim> predicate){
		return animations.stream().filter(predicate);
	}
	
	static AnimationM get(int id){
		return animations.get(id).instance;
	}
	
	public static void load(){
		animations.clear();
		File root=new File(Magiology.extraFiles.getRoot()+"animations");
		List<File> files=new ArrayList<File>();
		load0(root, files);
		files.stream().filter((f)->f.getPath().endsWith(".la")).forEach(f->{
			String name=f.getPath();
			name=name.substring(name.indexOf("animations\\")+11,name.length()-3).replace('\\', '/');
			try{
				animations.add(new Anim(new AnimationM(FileUtils.readFileToString(f)), name, animations.size()));
			}catch(Exception e){
				e.printStackTrace();
			}
		});
	}
	private static void load0(File f,List<File> files){
		if(f.isDirectory()){
			LogUtil.println(f);
			for(String name:f.list()){
				load0(new File(f.getPath()+"/"+name), files);
			}
		}else files.add(f);
	}
}
