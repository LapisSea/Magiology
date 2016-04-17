package com.magiology.handlers.animationhandlers.thehand.animation;

import com.magiology.util.utilobjects.codeinsert.BooleanReturn;

public class AnimationEvent{
	
	public boolean called;
	private Runnable onEvent;
	private BooleanReturn shouldCall;
	
	public AnimationEvent(Runnable onEvent, BooleanReturn shouldCall){
		this.onEvent=onEvent;
		this.shouldCall=shouldCall;
	}
	
	public Runnable getOnEvent(){
		return onEvent;
	}
	
	public BooleanReturn getShouldCall(){
		return shouldCall;
	}
}
