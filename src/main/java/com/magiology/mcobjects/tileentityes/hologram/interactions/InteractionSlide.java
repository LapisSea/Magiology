package com.magiology.mcobjects.tileentityes.hologram.interactions;

import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.mcobjects.tileentityes.hologram.Slider;
import com.magiology.util.utilobjects.ObjectHolder;

public class InteractionSlide<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionSlide(){
		super("sliderPos","0.69");
	}

	@Override
	public Object get(Host host){
		return ((Slider)host).getSliderPrecentqage();
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return Float.parseFloat(wordsIn[2]);
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		((Slider)host).sliderPos=((Float)setter)*0.75F;
		changed.setVar(true);
	}

}
