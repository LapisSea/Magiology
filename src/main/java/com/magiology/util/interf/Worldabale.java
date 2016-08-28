package com.magiology.util.interf;

import net.minecraft.world.World;

/**
 * 
 * @author LapisSea
 * 
 * @description Yep... I could not make up a better name... What? You need actual description? Naaaaah this is me!
 *
 */
public interface Worldabale{
	public World getWorld();
	public boolean isRemote();
	public boolean server();
	public boolean client();
}
