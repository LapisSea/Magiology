package com.magiology.core;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.util.Arrays;

import net.minecraftforge.fml.common.ModMetadata;

public class MReference{
	public static final String
		NAME="Magiology",
		MODID="magiology",
		VERSION="0.1052",
		CHANNEL_NAME="magicpackets",
		MOD_START_FOLDER="com.",
		PROXY_LOCATION=MOD_START_FOLDER+MODID+".forge.proxy",
		CLIENT_PROXY_LOCATION=PROXY_LOCATION+".ClientProxy",
		SERVER_PROXY_LOCATION=PROXY_LOCATION+".ServerProxy",
		MC_VERSION= "1.11.2",
		ACCEPTED_MC_VERSION= "["+MC_VERSION+"]",
		AUTHORS[]={"LapisSea"},
		BASE_PATH=MOD_START_FOLDER+MODID+".",
		SOURCE_FILE,
		MD5_ID,
		CREDITS="Thanks to all the great people in the MC modding community for literary changing my life!";
	static{
		
		String dummy=null;
		try{
			String path=MReference.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			dummy=path.endsWith(".jar")||path.endsWith(".zip")?path:null;
		}catch(URISyntaxException e){
			e.printStackTrace();
		}
		SOURCE_FILE=dummy;
		dummy=MODID+MC_VERSION+VERSION;
		try{
			dummy=new String(MessageDigest.getInstance("MD5").digest(dummy.getBytes("UTF-8")),"UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		MD5_ID=dummy;
	}
	public static void overrideMetadata(ModMetadata meta){
		meta.autogenerated=false;
		
		meta.authorList=Arrays.asList(AUTHORS);
		meta.credits=CREDITS;
		meta.description="";//TODO
		meta.logoFile="/magiology_logo.png";
		meta.modId=MODID;
		meta.name=NAME;
		meta.updateJSON=null;//TODO
		meta.url="http://lapissea.byethost8.com";
		meta.version=VERSION;
		meta.screenshots=null;//TODO
	}
	
}
