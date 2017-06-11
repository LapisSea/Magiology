package com.magiology.util.interf;

import com.magiology.util.statics.UtilM;
import net.minecraft.world.World;

/**
 *
 * Yep... I could not make up a better name... What? You need actual description? Naaaaah this is me!
 *
 * @author LapisSea
 *
 */
public interface Worldabale{
	
	World getWorld();
	
	default boolean hasWorld(){
		return getWorld()!=null;
	}
	
	default boolean isRemote(){
		return UtilM.isRemote(this);
	}
	
	default boolean server(){
		return !isRemote();
	}
	
	default boolean client(){
		return isRemote();
	}
	
	default long worldTime(){
		return UtilM.worldTime(this);
	}
	
	default boolean peridOf(int period){
		return UtilM.peridOf(getWorld(), period);
	}
}
