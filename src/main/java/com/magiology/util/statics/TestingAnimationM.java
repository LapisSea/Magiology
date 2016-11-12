package com.magiology.util.statics;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.magiology.DevOnly;
import com.magiology.util.objs.animation.AnimationM;

@DevOnly
public class TestingAnimationM{
	
	private static AnimationM instance;
	private static long lastRead=0;
	
	public static AnimationM get(){
		if(souldRefresh())read();
		return instance;
	}
	
	private static boolean souldRefresh(){
		if(instance==null)return true;
		long tim=UtilC.getWorldTime();
		boolean isTime=tim-lastRead>20;
		if(isTime)lastRead=tim;
		return isTime;
	}
	
	private static void read(){
		try{
			instance=new AnimationM(FileUtils.readFileToString(new File("LiveAnimation.la")));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
}
