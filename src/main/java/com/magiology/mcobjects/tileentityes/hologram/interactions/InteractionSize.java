package com.magiology.mcobjects.tileentityes.hologram.interactions;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.mcobjects.tileentityes.hologram.Button;
import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ObjectHolder;

public class InteractionSize<Host extends HoloObject> extends AbstractInteraction<Host>{
	
	public InteractionSize(){
		super("size","0.69, 0.420");
	}

	@Override
	public Object get(Host host){
		return host.originalSize.scale(1);
	}

	@Override
	public Object parseWords(String[] wordsIn)throws Exception{
		return new Vector2f(Float.parseFloat(wordsIn[2]), Float.parseFloat(wordsIn[3]));
	}

	@Override
	public void set(Host host, ObjectHolder<Boolean> changed, Object setter){
		host.originalSize=(Vector2f)setter;
		host.size=new Vector2f(host.originalSize.x*host.scale, host.originalSize.y*host.scale);
		if(host instanceof Button)((Button)host).body=new CubeModel(0, 0, -UtilM.p/2, -host.size.x, -host.size.y, UtilM.p/2);
		changed.setVar(true);
	}

}
