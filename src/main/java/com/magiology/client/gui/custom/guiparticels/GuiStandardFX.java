package com.magiology.client.gui.custom.guiparticels;

public enum GuiStandardFX{
	BigCloudFX(new GuiFXHelper(true,GuiFXProp.HasAgeLimit),new GuiFXHelper(true,GuiFXProp.HasBlendOut),new GuiFXHelper(true,GuiFXProp.HasBlendIn)),
	CloudFX(new GuiFXHelper(true,GuiFXProp.HasAgeLimit),new GuiFXHelper(true,GuiFXProp.HasBlendOut)),
	InpactFX(new GuiFXHelper(true,GuiFXProp.HasAgeLimit),new GuiFXHelper(true,GuiFXProp.HasBlendOut)),
	StarterFX(new GuiFXHelper(true,GuiFXProp.HasColision),new GuiFXHelper(true,GuiFXProp.HasColisionForce),new GuiFXHelper(false,GuiFXProp.HatesToBeUnseen)),
	SummonedFX(new GuiFXHelper(true,GuiFXProp.HasColision),new GuiFXHelper(true,GuiFXProp.HasColisionForce),new GuiFXHelper(false,GuiFXProp.HasAgeLimit),new GuiFXHelper(true,GuiFXProp.HasBlendOut));
	
	//--------------------------------------------------------//
	/**Just a helper for saving HasEffects for each property*/
	public static class GuiFXHelper{
		public GuiFXProp fxProp;
		public boolean HasEffects;
		public GuiFXHelper(boolean HasEffects,GuiFXProp fxProp){
			this.HasEffects=HasEffects;
			this.fxProp=fxProp;
		}
	}
	//--------------------------------------------------------//
	//________________________________________________________//
	//________________________________________________________//
	//--------------------------------------------------------//
	public static enum GuiFXProp{
		HasAgeLimit,HasBlendIn,HasBlendOut,HasColision,HasColisionForce,HatesToBeUnseen;
	}
	GuiFXHelper[] guiFXHelpers;
	GuiStandardFX(GuiFXHelper...guiFXHelpers){
		this.guiFXHelpers=guiFXHelpers;
	}
	
	public boolean HasFX(GuiFXProp guiFXProp){
		for(GuiFXHelper guiFXHelper:guiFXHelpers){
			if(guiFXHelper.fxProp==guiFXProp){
				if(guiFXHelper.HasEffects)return true;
			}
		}
		return false;
	}
	public boolean IsEnabled(GuiFXProp guiFXProp){
		for(GuiFXHelper guiFXHelper:guiFXHelpers){
			if(guiFXHelper.fxProp==guiFXProp)return true;
		}
		return false;
	}
}
