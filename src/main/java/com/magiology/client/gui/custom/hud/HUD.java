package com.magiology.client.gui.custom.hud;

import com.magiology.util.renderers.Renderer;

import net.minecraft.entity.player.EntityPlayer;

public abstract class HUD{
	public static void drawRect(float xPngSize,float yPngSize,double xPos, double yPos, double xTextureOffset, double yTextureOffset, double xSize, double ySize){
		Renderer.POS_UV.beginQuads();
		Renderer.POS_UV.addVertex(xPos + 0, yPos + ySize, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize);
		Renderer.POS_UV.addVertex(xPos + xSize, yPos + ySize, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + ySize) * yPngSize);
		Renderer.POS_UV.addVertex(xPos + xSize, yPos + 0, 0, (float)(xTextureOffset + xSize) * xPngSize, (float)(yTextureOffset + 0) * yPngSize);
		Renderer.POS_UV.addVertex(xPos + 0, yPos + 0, 0, (float)(xTextureOffset + 0) * xPngSize, (float)(yTextureOffset + 0) * yPngSize);
		Renderer.POS_UV.draw();
	}
	public EntityPlayer player;
	public abstract void render(int xScreen,int yScreen,float partialTicks);
	public void update(){}
}
