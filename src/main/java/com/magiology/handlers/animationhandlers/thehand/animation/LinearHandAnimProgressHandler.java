package com.magiology.handlers.animationhandlers.thehand.animation;

import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.util.utilclasses.math.MathUtil;

public abstract class LinearHandAnimProgressHandler{
	public static abstract class LinearHandAnimProgressHandlerClassic extends LinearHandAnimProgressHandler{
		protected int timeMul=-1;
		@Override
		public float getProgress(){
			return progress;
		}
		
		
		public abstract int getTimeForAnimation();
		@Override
		public boolean isInactive(){
			return timeHeld==0;
		}
		
		@Override
		public void onHoldingEnd(){
			timeMul=-1;
		}

		@Override
		public void onHoldingStart(){
			timeMul=1;
		}

		@Override
		public void update(){
			timeHeld=MathUtil.snap(timeHeld+timeMul, 0, getTimeForAnimation());
			progress=timeHeld/(float)getTimeForAnimation();
		}
		@Override
		public boolean willRestrictItemSwitching(){
			return true;
		}
	}
	private boolean holding,held;
	protected float progress;
	
	protected int timeHeld;
	public abstract float getProgress();
	
	public abstract boolean isInactive();
	public abstract void onHoldingEnd();
	public abstract void onHoldingStart();
	protected void passOnAnimation(HandAnimationBase animation){
		timeHeld=2;
		TheHandHandler.activeAnimation=animation;
	}
	public final void setHolding(boolean holding){
		held=this.holding;
		this.holding=holding;
		if(held!=this.holding){
			if(holding)onHoldingStart();
			else onHoldingEnd();
		}
	}
	public abstract void update();
	
	public abstract boolean willRestrictItemSwitching();
}