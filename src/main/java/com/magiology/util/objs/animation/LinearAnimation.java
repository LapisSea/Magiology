package com.magiology.util.objs.animation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.magiology.util.interf.Calculable;
import com.magiology.util.interf.ObjectReturn;

public class LinearAnimation<T extends Calculable<T>>{
	
	private Point<T>[] points;
	
	static class Point<T extends Calculable<T>>{
		public T value;
		public float pos;
		
		public Point(T point, float pos){
			this.value=point;
			this.pos=pos;
		}
		
		@Override
		public String toString(){
			return "Point[pos="+pos+", value="+value+"]";
		}
	}
	
	public LinearAnimation(Collection<T> data){
		this(((ObjectReturn<Map<Float, T>>)()->{
			Map<Float, T> map=new HashMap<>();
			int i=0;
			for(T t:data){
				float j=i/(data.size()-1F);
				i++;
				map.put(j, t);
			}
			return map;
		}).process());
	}
	
	public LinearAnimation(Map<Float, T> data){
		if(data.isEmpty())
			throw new IllegalStateException("Linear animation can't be empty!");
		
		List<Point> dataSorted=new ArrayList<Point>();
		
		for(Entry<Float, T> pos:data.entrySet()){
			boolean added=false;
			
			for(int i=0; i<dataSorted.size(); i++){
				if(dataSorted.get(i).pos>pos.getKey()){
					dataSorted.add(i, new Point(pos.getValue(), pos.getKey()));
					i=dataSorted.size();
					added=true;
				}
			}
			if(!added)dataSorted.add(new Point(pos.getValue(), pos.getKey()));
		}
		points=dataSorted.toArray(new Point[dataSorted.size()]);
	}
	
	public T get(float pos){
		Point<T> start=null, end=null;
		
		pos%=1;
		
		for(int i=1; i<points.length; i++){
			Point<T> child=points[i];
			if(child.pos>=pos){
				start=points[i-1];
				end=child;
				break;
			}
		}
		
		if(start==null)return points[0].value;
		
		float startPos=start.pos, endPos=end.pos;
		
		if(pos<startPos)return start.value;
		if(pos>endPos)return end.value;
		
		float precentageDifference=endPos-startPos, precentageStart=pos-startPos, precentage=precentageStart/precentageDifference;
		T startValue=start.value;
		
		return end.value.sub(startValue).mul(precentage).add(startValue);
	}
	
	Point<T>[] getPoints(){
		return points;
	}
	
}
