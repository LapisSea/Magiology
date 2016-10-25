package com.magiology.util.m_extensions;

import com.magiology.util.objs.vec.Vec3M;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySpecialRendererM<T extends TileEntity> extends TileEntitySpecialRenderer<T>{
	public static final float p=1F/16F;
	public abstract void renderTileEntityAt(T tileEntity, Vec3M renderPos, float partialTicks);
	
	private static final Vec3M renderPos=new Vec3M();
	
	@Override
	public final void renderTileEntityAt(TileEntity tile, double posX, double posY, double posZ, float partialTicks, int destroyStage){
		renderPos.set(posX, posY, posZ);
		renderTileEntityAt((T)tile, renderPos, partialTicks);
	}

}
