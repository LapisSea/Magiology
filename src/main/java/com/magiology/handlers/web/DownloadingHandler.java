package com.magiology.handlers.web;

import static com.magiology.client.gui.custom.hud.MainMenuUpdateNotificationHUD.*;
import static com.magiology.core.MReference.*;
import static com.magiology.handlers.web.QuickMediaFireDownlader.*;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

import com.magiology.util.utilclasses.UtilM;


public class DownloadingHandler{
	public static void downladAssets(){
		new File(MODS_SUBFOLDER_WIN_GUI).mkdir();
		downladAndName(MODS_SUBFOLDER_WIN_GUI,	"http://www.mediafire.com/listen/5psc9ipik0uk96y/Close.wav");
		downladAndName(MODS_SUBFOLDER_WIN_GUI,   "http://www.mediafire.com/listen/1u2c4h3sc5s9258/Loaded.wav");
		downladAndName(MODS_SUBFOLDER_WIN_GUI,"http://www.mediafire.com/download/1cyu902d1kqb517/MagiZip.zip");
		downladAndName(MODS_SUBFOLDER_WIN_GUI,   "http://www.mediafire.com/listen/azulbuu0x71gadg/OpenUp.wav");
	}
	public static void downloadUpdater(){
		new Thread(new Runnable(){@Override public void run(){
			instance.isDownloading=instance.downloadingInvoked=true;
			downlad(UPDATER_DIR, "http://www.mediafire.com/download/bps59al3r24tmuo/MagiologyUpdater.jar");
			UtilM.sleep(100);
			instance.isDownloading=false;
		}}).start();
	}
	
	public static String findValue(String tag){
		try{
			URL url=new URL("https://raw.githubusercontent.com/LapisSea/Magiology_1.8/master/src/main/java/com/magiology/core/MReference.java");
			Scanner s=new Scanner(url.openStream());
			String value=null;
			try{
				s.findWithinHorizon(tag+"=", 99999);
				value=s.next();
				value=value.substring(1, value.length()-2);
			}catch(Exception e){}
			s.close();
			return value;
		}catch(Exception ex){}
		return null;
	}
}
