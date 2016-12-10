package com.magiology.util.objs.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.util.objs.CalculableFloat;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.animation.LinearAnimation.Point;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.statics.math.MathUtil;

import net.minecraft.util.math.MathHelper;

public class AnimationM{

	private final String[] names;
	private final LinearAnimation<CalculableFloat>[] data;
	public AnimationM(String file){
		PairM<LinearAnimation<CalculableFloat>[], String[]>dat=parse(file);
		names=dat.obj2;
		data=dat.obj1;
	}

	public AnimationM(LinearAnimation<CalculableFloat>[] data, String[] names){
		this.data=data;
		this.names=names;
	}
	
	private PairM<LinearAnimation<CalculableFloat>[], String[]> parse(String string){
		
		String[] lines=string.split("\n");
		List<LinearAnimation<CalculableFloat>> data=new ArrayList<>();
		List<String> names=new ArrayList<>();
		for(String line:lines){
			line=line.trim();
			if(line.length()==0)continue;
			int nameEnd=line.lastIndexOf("\" ");
			String name=line.substring(0, nameEnd);
			name=name.substring(1);
			if(names.contains(name)){
				int id=0;
				for(;names.contains(name+id);id++);
				name+=id;
			}
			names.add(name);
			
			line=line.substring(nameEnd+1).trim();
			String[] muldata=line.split(" ");
			line=muldata[1];
			
			float valueMul=-Float.parseFloat(muldata[0]);
			
			Map<Float,CalculableFloat> points=new HashMap<>();
			
			String[] parts=line.split(",");
			for(String part:parts){
				String[] pt=part.split("/");
				points.put(MathUtil.snap(Float.parseFloat(pt[0]), 0, 1), new CalculableFloat(valueMul*Float.parseFloat(pt[1])));
			}
			LinearAnimation<CalculableFloat> part=new LinearAnimation(points);
			

			Point<CalculableFloat> start=part.getPoints()[0],end=part.getPoints()[part.getPoints().length-1];
			
			Vec2FM 
				l1p1=new Vec2FM(start.pos, start.value.value),
				l2p1=new Vec2FM(end.pos, end.value.value);
			
			float 
				diffx=l1p1.x+1-l2p1.x,
				diffy=l1p1.y-l2p1.y,
				angle=(float)Math.atan2(diffx,diffy),
				sin=MathHelper.sin(angle),
				cos=MathHelper.cos(angle);

			points.put(0F, new CalculableFloat(l1p1.y-cos*(l1p1.x/sin)));
			points.put(1F, new CalculableFloat(l2p1.y+cos*((1-l2p1.x)/sin)));
			
			data.add(new LinearAnimation(points));
		}
		
		return new PairM(data.toArray(new LinearAnimation[data.size()]),names.toArray(new String[names.size()]));
	}
	
	public float get(int id, float pos){
		return data[id].get(pos).value;
	}
	
	public float[] getAll(float pos){
		float[] all=new float[data.length];
		for(int i=0; i<all.length; i++){
			all[i]=get(i, pos);
		}
		return all;
	}
	
	public int getIdName(String name){
		return ArrayUtils.indexOf(names, name);
	}
}
