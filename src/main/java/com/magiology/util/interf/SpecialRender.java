package com.magiology.util.interf;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Solution for side only classes
 *
 * @author LapisSea
 */
public interface SpecialRender{

	@SideOnly(Side.CLIENT)
	Renderable getRenderer();
}
