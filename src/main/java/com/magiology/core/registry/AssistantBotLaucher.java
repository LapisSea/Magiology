package com.magiology.core.registry;

import com.magiology.core.Magiology;
import com.magiology.util.statics.LogUtil;

public class AssistantBotLaucher{
	private static boolean RAN;
	
	
	public static void run(){
		if(RAN)return;
		RAN=true;
		
		if(!Magiology.IS_DEV){
			LogUtil.println("Development mode state: OFF\nSkipping assistant bot. (this is good if you are not a developer)");
			return;
		}
		
		LogUtil.println("Development mode state: ON\nActivating assistant bot...");
		AssistantBot.run();
	}
}
