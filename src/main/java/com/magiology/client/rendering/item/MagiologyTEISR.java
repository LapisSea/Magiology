package com.magiology.client.rendering.item;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class MagiologyTEISR extends TileEntityItemStackRenderer{
	
	private static boolean isWrapped=false;
	
	public static void wrapp(){
		if(isWrapped)return;
		isWrapped=true;
		TileEntityItemStackRenderer.instance=new MagiologyTEISR(TileEntityItemStackRenderer.instance);
	}
	
	private final TileEntityItemStackRenderer parent;
	
	private MagiologyTEISR(TileEntityItemStackRenderer parent){
		this.parent=parent;
	}
	
	@Override
	public void renderByItem(ItemStack stack){
		
		if(SIRRegistry.shouldRender(stack)){
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
			SIRRegistry.renderStack(stack);
			GL11.glPopAttrib();
			
			
		}else parent.renderByItem(stack);
	}
}
