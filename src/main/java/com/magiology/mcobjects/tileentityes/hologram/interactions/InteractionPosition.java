package com.magiology.mcobjects.tileentityes.hologram.interactions;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.util.utilobjects.ObjectHolder;

public class InteractionPosition<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionPosition(){
		super("pos","0.69, 0.420");
	}

	@Override
	public Object get(Host host){
		return host.position;
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return new Vector2f(Float.parseFloat(wordsIn[4]), Float.parseFloat(wordsIn[5]));
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		host.position=(Vector2f)setter;
	}

}
