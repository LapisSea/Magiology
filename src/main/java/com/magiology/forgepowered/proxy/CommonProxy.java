package com.magiology.forgepowered.proxy;

import com.magiology.api.lang.program.ProgramDataBase;
import com.magiology.core.MUpdater;
import com.magiology.core.init.MBlocks;
import com.magiology.core.init.MCreativeTabs;
import com.magiology.core.init.MEntitys;
import com.magiology.core.init.MEvents;
import com.magiology.core.init.MInterfaces;
import com.magiology.core.init.MItems;
import com.magiology.core.init.MPackets;
import com.magiology.core.init.MRecepies;
import com.magiology.core.init.MTileEntitys;

public class CommonProxy{
	
	public void loadModFiles(){
		
	}
	
	public void preInit(){
		MPackets.preInit();
		
		MUpdater.init();
		
		MCreativeTabs.preInit();
		MBlocks.preInit();
		MItems.preInit();
	}
	
	public void init(){
		MRecepies.init();
		MTileEntitys.init();
		MEntitys.init();
		MEvents.init();
		MInterfaces.init();
	}
	
	public void postInit(){
		ProgramDataBase.loadClass();
	}
	
	public void onExit(){
		
	}
	
}


