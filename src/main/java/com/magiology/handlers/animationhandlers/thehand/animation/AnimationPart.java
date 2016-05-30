package com.magiology.handlers.animationhandlers.thehand.animation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;

public class AnimationPart{
	protected static final AnimationPart[] blank=new AnimationPart[]{new AnimationPart(0, 0, 0, 0)};
	private static ObjectProcessor[] processors;
	//	public static AnimationPart[] gen(int pos, float...startLenghtSpeed){
//		assert startLenghtSpeed.length%2!=0:"invalid data!";
//		AnimationPart[] result=new AnimationPart[startLenghtSpeed.length/2];
//		int start=0;
//		for(int i=0;i<result.length;i++){
//			result[i]=new AnimationPart(pos, start, (int)startLenghtSpeed[i*2], startLenghtSpeed[i*2+1]);
//			start+=(int)startLenghtSpeed[i*2];
//		}
//		return result;
//	}
	
	ObjectProcessor<HandData> runner;
	
	private float speed;
	
	private int start,lenght,posID;
	
	public AnimationPart(int pos, int start, int lenght, float speed){
		if(processors==null){
			processors=new ObjectProcessor[]{
					new ObjectProcessor<HandData>(){@Override public HandData process(HandData object, Object...objects){
						AnimationPart instance=(AnimationPart)objects[0];
						object.base[instance.posID]+=instance.speed;
						return object;
					}},
					new ObjectProcessor<HandData>(){@Override public HandData process(HandData object, Object...objects){
						AnimationPart instance=(AnimationPart)objects[0];
						object.thumb[instance.posID]+=instance.speed;
						return object;
					}},
					new ObjectProcessor<HandData>(){@Override public HandData process(HandData object, Object...objects){
						AnimationPart instance=(AnimationPart)objects[0];
						object.fingers[0][instance.posID]+=instance.speed;
						return object;
					}},
					new ObjectProcessor<HandData>(){@Override public HandData process(HandData object, Object...objects){
						AnimationPart instance=(AnimationPart)objects[0];
						object.fingers[1][instance.posID]+=instance.speed;
						return object;
					}},
					new ObjectProcessor<HandData>(){@Override public HandData process(HandData object, Object...objects){
						AnimationPart instance=(AnimationPart)objects[0];
						object.fingers[2][instance.posID]+=instance.speed;
						return object;
					}},
					new ObjectProcessor<HandData>(){@Override public HandData process(HandData object, Object...objects){
						AnimationPart instance=(AnimationPart)objects[0];
						object.fingers[3][instance.posID]+=instance.speed;
						return object;
					}}
				};
		}
		this.start=start;
		this.lenght=lenght;
		this.speed=speed;
		
		int processID=-1;
		
		if(pos>=0&&pos<6){
			processID=0;
			posID=pos;
		}
		else if(pos<11){
			processID=1;
			posID=pos-6;
		}
		else if(pos<15){
			processID=2;
			posID=pos-11;
		}
		else if(pos<19){
			processID=3;
			posID=pos-15;
		}
		else if(pos<23){
			processID=4;
			posID=pos-19;
		}
		else if(pos<27){
			processID=5;
			posID=pos-23;
		}
		
		if(!UtilM.isIdInArray(processors, processID)){
			throw new IllegalArgumentException(""+pos);
		}
		runner=processors[processID];
		
	}
	boolean isActive(int time){
		if(speed==0)return false;
		return time>=start&&time<=start+lenght;
	}
	public static class AnimationPartEmpty extends AnimationPart{

		public AnimationPartEmpty(int pos, int start, int lenght, float speed){
			super(pos, start, lenght, speed);
		}
		@Override
		boolean isActive(int time){
			return false;
		}
	}
	
	
	@Override
	public String toString(){
		return new StringBuilder("AnimationPart[")
			.append("start=").append(start)
			.append(", lenght=").append(lenght)
			.append(", posID=").append(posID)
			.append(", speed=").append(speed)
			.append(", runnerID=").append(ArrayUtils.indexOf(processors, runner))
		.append(']').toString();
	}
	public static class AnimationFactory{
//		0:0-5 base=new float[]{0,0,0, 0,0,0};
//		1:6-10 thumb=new float[]{0,0,0, 0, 0};
//		fingers=new float[][]{ 2:11-14 {0,0, 0, 0}, 3 {0,0, 0, 0}, 4 {0,0, 0, 0}, 5 {0,0, 0, 0}};
		private static final int[] groupStarts={0,6,11,15,19,23};
		public List<AnimationPart> data=new ArrayList<>();
		private int group=0,delay=0;
		/**
		 * 0 base, 1-5 fingers, 0=0, 1=6, 2=11,3=15,4=19,5=23
		 * @param groupId
		 */
		public AnimationFactory setGroup(int groupId){
			group=groupId;
			return this;
		}
		
		public AnimationFactory gen(int pos, float...startLenghtSpeed){
			if(startLenghtSpeed.length%2!=0)throw new IllegalArgumentException("Invalid data!");
			
			pos+=groupStarts[group];
			
			
			int start=Math.max(0, delay),length=startLenghtSpeed.length/2;
			for(int i=0;i<length;i++){
				float speed=startLenghtSpeed[i*2+1];
				data.add(new AnimationPart(pos, start, (int)startLenghtSpeed[i*2], speed));
				start+=(int)startLenghtSpeed[i*2];
			}
			return this;
		}
		public AnimationPart[] compile(){
			AnimationPart[] data1=data.toArray(new AnimationPart[data.size()]);
			data.clear();
			return data1; 
		}

		public AnimationFactory setDelay(int delay){
			this.delay=delay;
			return this;
		}
	}
	
}