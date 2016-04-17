package com.magiology.api.updateable;

import java.util.Collection;

import com.magiology.client.gui.GuiUpdater.Updateable;

public class Updater{
	public static void update(Collection objects){
		for(Object object:objects){
			if(object instanceof Updateable)((Updateable)object).update();
		}
	}
	
	public static void update(Object[] objects){
		for(int i=0;i<objects.length;i++){
			if(objects[i]instanceof Updateable)((Updateable)objects[i]).update();
		}
	}
	private Updater(){}
	
}
