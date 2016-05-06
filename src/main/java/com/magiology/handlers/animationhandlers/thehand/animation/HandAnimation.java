package com.magiology.handlers.animationhandlers.thehand.animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.LinearAnimation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandAnimation extends HandAnimationBase{
	
	public AnimationPart[] animationData;
	private boolean isRunning;
	
	private long timeStarted;
	public HandData wantedPos,speed;
	
	public HandAnimation(HandPosition startEnd, AnimationPart[] animationData, String name){
		this(startEnd, startEnd, animationData, name);
	}
	public HandAnimation(HandPosition start, HandPosition end, AnimationPart[] animationData, String name){
		super(start, end, name);
		wantedPos=(HandData)start.data.clone();
		this.animationData=animationData;
	}
	
	
	
	@Override
	public boolean canStart(){
		return super.canStart()&&!isRunning;
	}
	private void end(){
		TheHandHandler.setActivePositionId(UtilC.getThePlayer(), end.posInRegistry());
	}
	@Override
	public HandData getWantedPos(){
		return wantedPos;
	}
	public boolean isDone(){
		return !isRunning;
	}
	
	public void start(){
		if(!canStart())return;
		wantedPos.set(start.data);
		timeStarted=UtilC.getWorldTime();
		isRunning=true;
	}
	
	public LinearAnimation toLinearAnimation(int quality){
		int tickCount=0;
		List<HandData> data=new ArrayList<HandData>();
		
		wantedPos.set(start.data);
		timeStarted=0;
		isRunning=true;
		
		data.add((HandData)wantedPos.clone());
		int count=0;
		while(isRunning){
			update(count);
			tickCount++;
			if(tickCount>quality){
				data.add((HandData)wantedPos.clone());
				tickCount=0;
			}
			count++;
		}
		data.add((HandData)wantedPos.clone());
		PrintUtil.println(data.size());
		DoubleObject<HandData[], Float>[] data1=new DoubleObject[data.size()];
		Iterator<HandData> data2=data.iterator();
		for(int i=0;i<data1.length;i++)data1[i]=new DoubleObject<HandData[], Float>(new HandData[]{data2.next()}, (i+0F)/data1.length);
		return new LinearAnimation<HandData>(data1);
	}
	public void update(long time){
		if(isDone()){
			end();
			return;
		}
		
		speed=new HandData();
		int timeRunning=(int)(time-timeStarted);
		boolean noInvoke=true;
		for(AnimationPart animationPart:animationData)if(animationPart.isActive(timeRunning)){
			noInvoke=false;
			animationPart.runner.pocess(speed, animationPart);
		}
		if(noInvoke)isRunning=false;
		wantedPos.set(wantedPos.add(speed));
	}
}
