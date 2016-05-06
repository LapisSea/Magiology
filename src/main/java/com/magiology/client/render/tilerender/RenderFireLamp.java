package com.magiology.client.render.tilerender;

import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;

public class RenderFireLamp extends TileEntitySpecialRendererM{

	private final float p= 1F/16F;
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f){
		TileEntityFireLamp tileFL=(TileEntityFireLamp) tile;
		boolean var1=true;
		float FP=PowerUtil.getFuelPrecentage(tileFL),a=FP*10-1;
		if(a>1)a=1;
		else if(a<0)a=0;
		OpenGLM.pushMatrix();
		OpenGLM.translate(x,y,z);
		OpenGLM.disableTexture2D();
		GL11U.setUpOpaqueRendering(1);
		
		TessUtil.drawBlurredCube((int)x, (int)y, (int)z, p*4.5,0.01,p*4.5,p*11.5,p*11*FP,p*11.5, 15, 0.03, 1,0.1,0.1, 0.5*a);
		OpenGLM.popMatrix();
		OpenGLM.depthMask(true);
		OpenGLM.enableTexture2D();
		GL11U.endOpaqueRendering();
		OpenGLM.color(1,1,1,1);
		OpenGLM.enableLighting();
		}
	
	
}