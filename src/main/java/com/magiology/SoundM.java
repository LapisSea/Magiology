package com.magiology;

import com.magiology.core.MReference;
import com.magiology.util.utilclasses.RandUtil;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public enum SoundM{
	PopFX("poop"),
	WeirdFX("weird"),
	WindFX("wind"),
	WingSwingFX("swing1","swing2");
	
	public final String[] soundNames;
	public final SoundCategory category;
	private SoundM(SoundCategory category, String... names){
		soundNames=names;
	}
	@Override
	public String toString(){
		String result=null;
		if(soundNames.length==1)result=soundNames[0];
		else result=soundNames[RandUtil.RI(soundNames.length)];
		return result;
	}
	public SoundEvent toSoundEvent(){
		return new SoundEvent(new ResourceLocation(MReference.MODID+":"+toString()));
	}
	
	public static void register(){
		for(SoundM sound:values()){
			for(String name:sound.soundNames){
				registerSound(name);
			}
		}
	}
	
	private static void registerSound(String soundNameIn){
		ResourceLocation resourcelocation=new ResourceLocation(soundNameIn);
		SoundEvent.soundEventRegistry.register(SoundEvent.soundEventRegistry.getKeys().size(), resourcelocation, new SoundEvent(resourcelocation));
	}
}
