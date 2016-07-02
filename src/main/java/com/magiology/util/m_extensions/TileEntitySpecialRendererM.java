package com.magiology.util.m_extensions;

import com.magiology.util.objs.Vec3M;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntitySpecialRendererM<T extends TileEntity> extends TileEntitySpecialRenderer<T>{
	public static final float p=1F/16F;
	public abstract void renderTileEntityAt(T tileEntity, Vec3M renderPos, float partialTicks);

	@Override
	public final void renderTileEntityAt(TileEntity tile, double posX, double posY, double posZ, float partialTicks, int IDuannoMaybeRenderPass){
		renderTileEntityAt((T)tile, new Vec3M(posX, posY, posZ), partialTicks);
	}

}
