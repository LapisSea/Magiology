package com.magiology.mcobjects.tileentityes.hologram.interactions;

import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.ObjectHolder;

public class InteractionColor<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionColor(){
		super("color","0.69, 0.420, 360Noscope, m8");
	}

	@Override
	public Object get(Host host){
		return host.setColor;
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return new ColorF(Float.parseFloat(wordsIn[2].replaceAll(",", "")), Float.parseFloat(wordsIn[3].replaceAll(",", "")), Float.parseFloat(wordsIn[4].replaceAll(",", "")), Float.parseFloat(wordsIn[5].replaceAll(",", "")));
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		host.setColor=(ColorF)setter;
		changed.setVar(true);
	}

}
