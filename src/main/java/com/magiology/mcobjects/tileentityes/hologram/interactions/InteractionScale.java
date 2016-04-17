package com.magiology.mcobjects.tileentityes.hologram.interactions;

import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.util.utilobjects.ObjectHolder;

public class InteractionScale<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionScale(){
		super("scale","0.69");
	}

	@Override
	public Object get(Host host){
		return host.scale;
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return Float.parseFloat(wordsIn[2]);
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		host.scale=(float)setter;
		changed.setVar(true);
	}

}
