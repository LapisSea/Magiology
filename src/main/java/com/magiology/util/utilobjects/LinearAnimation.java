package com.magiology.util.utilobjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.magiology.api.Calculable;

public class LinearAnimation<T extends Calculable>{
	
	private DoubleObject<T[], Float>[] animationData;
	
	public LinearAnimation(DoubleObject<T[], Float>...data){
		List<DoubleObject<T[], Float>> dataSorted=new ArrayList<DoubleObject<T[], Float>>();
		
		for(DoubleObject<T[], Float> pos:data){
			boolean added=false;
			
			for(int i=0;i<dataSorted.size();i++){
				if(dataSorted.get(i).obj2>pos.obj2){
					dataSorted.add(i, pos);
					i=dataSorted.size();
					added=true;
				}
			}
			if(!added)dataSorted.add(pos);
		}
		animationData=dataSorted.toArray(new DoubleObject[0]);
	}
	
	public T[] get(float animationPos){
		int pos=0;
		for(int i=0;i<animationData.length-1;i++){
			if(animationData[pos+1].obj2<animationPos)pos=i;
			else i=animationData.length;
		}
		return get(pos, animationPos);
	}
	public T[] get(int partId,float animationPos){
		DoubleObject<T[], Float> before=animationData[partId],after=animationData[partId+1];
		T[] result=(T[])Array.newInstance(before.obj1.getClass().getComponentType(), before.obj1.length);
		if(animationPos>=after.obj2)return after.obj1;
		if(animationPos<=before.obj2)return before.obj1;
		float 
		precentageDifference=after.obj2-before.obj2,
		precentageStart=animationPos-before.obj2,
		precentage=precentageStart/precentageDifference;
		
		
		
		for(int i=0;i<result.length;i++){
			T pos1=before.obj1[i], pos2=after.obj1[i];
			result[i]=(T)pos2.sub(pos1).mul(precentage).add(pos1);
		}
		
		return result;
	}
}
