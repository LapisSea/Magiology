package com.magiology.client.gui;

import static com.magiology.util.utilclasses.UtilM.*;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.updateable.Updater;
import com.magiology.util.utilclasses.UtilC;

import net.minecraft.entity.player.EntityPlayer;

public class GuiUpdater{
	
	public static interface Updateable{
		public void update();
	}
	private static GuiUpdater instance;
	public static GuiUpdater GetInstace(){return instance;}
	
	public static void tryToUpdate(EntityPlayer player){
		if(player==null)return;
		List objects=new ArrayList();
		objects.add(player.openContainer);
		if(isRemote(player))objects.add(UtilC.getMC().currentScreen);
		Updater.update(objects);
	}
	public GuiUpdater(){instance=this;}
}
