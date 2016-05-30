package com.magiology.core;

import java.util.Arrays;

import net.minecraftforge.fml.common.ModMetadata;

public class MReference{
	public static final String
		MODID= "magiology",
		VERSION= "0.1052",
		NAME= "Magiology",
		CHANNEL_NAME= "magicpackets",
		MOD_START_FOLDER= "com.",
		PROXY_LOCATION=MOD_START_FOLDER+MODID+".core.proxy",
		ClIENT_PROXY_LOCATION=PROXY_LOCATION+".ClientProxy",
		SERVER_PROXY_LOCATION=PROXY_LOCATION+".ServerProxy",
		MC_VERSION= "1.9",
		MODS_SUBFOLDER_DIR= "mods/"+MC_VERSION+"/"+NAME,
		UPDATER_DIR= MODS_SUBFOLDER_DIR+"/updater/"+NAME+"Updater.jar",
		NEWEST_VERSION_URL= "http://www.mediafire.com/download/d41w4px39x74gzt/Magiology.jar",
		MODS_SUBFOLDER_COMMON_ASSETS= MODS_SUBFOLDER_DIR+"/common",
		MODS_SUBFOLDER_WIN_GUI= MODS_SUBFOLDER_DIR+"/externalGui",
		INFO_FILE_NAME= "FileForMod-"+MODID,
		ACCEPTED_MC_VERSION= "["+MC_VERSION+"]",
		JS_PROGRAMS_DIR= MODS_SUBFOLDER_DIR+"/jsPrograms",
		AUTHORS="LapisSea",
		BASE_PATH=MOD_START_FOLDER+MODID+".";
	public static ModMetadata getModMetadata(){
		ModMetadata metadata=new ModMetadata();
		metadata.authorList=Arrays.asList(AUTHORS.split(","));
		metadata.autogenerated=false;
		metadata.credits="All credits reserved by: "+metadata.getAuthorList();
		//TODO fill this up! -> METADATA.logoFile 
		metadata.modId=MODID;
		metadata.name=NAME;
		//TODO fill this up! -> METADATA.screenshots
		//TODO fill this up! -> METADATA.updateJSON
		metadata.version=VERSION;
		return metadata;
	}
}
