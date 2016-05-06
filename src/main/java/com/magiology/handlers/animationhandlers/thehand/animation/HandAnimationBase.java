package com.magiology.handlers.animationhandlers.thehand.animation;

import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.util.utilclasses.UtilC;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class HandAnimationBase{
	
	public final String name;
	protected final HandPosition start,end;
	
	public HandAnimationBase(HandPosition start, HandPosition end, String name){
		this.start=start;
		this.end=end;
		this.name=name;
	}
	
	public boolean canStart(){
		return TheHandHandler.getActivePosition(UtilC.getThePlayer()).name.equals(start.name);
	}
	
	public abstract HandData getWantedPos();
}
