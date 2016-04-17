package com.magiology.util.utilobjects;

import java.util.HashMap;
import java.util.Map;

public class Tracker{
		private Map<String, DoubleObject<Float, Integer>> data=new HashMap<String, DoubleObject<Float, Integer>>();
		private Map<String, Long> runningData=new HashMap<String, Long>();
		public void end(String tag){
			if(!runningData.containsKey(tag))return;
			long value=System.currentTimeMillis()-runningData.remove(tag);
			DoubleObject<Float, Integer> dataValue=data.getOrDefault(tag, new DoubleObject<Float, Integer>(-1F, 0));
			if(dataValue.obj1==-1){
				data.put(tag, new DoubleObject<Float, Integer>((float)value, 1));
				return;
			}
			data.put(tag, new DoubleObject<Float, Integer>((dataValue.obj1*dataValue.obj2+value)/(dataValue.obj2+1), dataValue.obj2+1));
		}
		public float get(String tag){
			return data.containsKey(tag)?data.get(tag).obj1:-1L;
		}
		public Map<String, DoubleObject<Float, Integer>> getData(){
			return data;
		}
		public void start(String tag){
			runningData.put(tag, System.currentTimeMillis());
		}
	}