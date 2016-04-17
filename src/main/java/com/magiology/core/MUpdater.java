package com.magiology.core;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

import com.magiology.handlers.web.DownloadingHandler;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM.U;

public class MUpdater{
	private static float currentVersion=Float.parseFloat(MReference.VERSION),newestVersion=-1;
	public static boolean debug=false;
	private static String extraData;
	
	public static float getCurrentVersion(){return currentVersion;}
	public static String getExtraData(){return extraData;}
	public static boolean getFoundNew(){return newestVersion>currentVersion||debug;}
	public static float getNewestVersion(){return newestVersion;}
	public static String getUpdateStatus(){
		if(!getFoundNew())return U.signature(AQUA,ITALIC)+"Found a newer version! "+RESET+GOLD+"["+BLUE+"More info"+GOLD+"]";
		else return U.signature(AQUA,ITALIC)+"I am up to date! "+BLUE+";)";
	}
	public static void init(){
		
//		debug=true;
		try{
			newestVersion=Float.parseFloat(DownloadingHandler.findValue("VERSION"));
		}catch(Exception e){
			e.printStackTrace();
		}
		if(newestVersion==-1)PrintUtil.printlnEr(MReference.NAME+" has failed to check for updates");
		else extraData=U.signature(AQUA)+"Latest version is: "+GOLD+newestVersion+AQUA+", and you are using version: "+GOLD+currentVersion;
	}
}