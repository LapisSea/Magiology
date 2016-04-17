package com.magiology.handlers.animationhandlers.thehand.animation;

import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.util.utilclasses.UtilM;

public abstract class HandAnimationBase{
	
	public final String name;
	protected final HandPosition start,end;
	
	public HandAnimationBase(HandPosition start, HandPosition end, String name){
		this.start=start;
		this.end=end;
		this.name=name;
	}
	
	public boolean canStart(){
		return TheHandHandler.getActivePosition(UtilM.getThePlayer()).name.equals(start.name);
	}
	
	public abstract HandData getWantedPos();
}
