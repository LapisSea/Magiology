package com.magiology.client.render.tilerender;

import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;

public class RenderEnergizedLapisOre extends TileEntitySpecialRendererM {

	VertexRenderer buf=Render.NVB();
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f){
		OpenGLM.pushMatrix();
		this.bindTexture(Textures.EnergizedLapisOre);
		OpenGLM.enableLighting();
		GL11U.setUpOpaqueRendering(2);
		OpenGLM.translate(x,y,z);
		for(int a=0;a<6;a++){
			int b=0;int c=0;
			
			if(a==1)b=90;
			else if(a==2)b=-90;
			else if(a==3)c=-90;
			else if(a==4)c=90;
			else if(a==5)c=180;
			
			
			OpenGLM.translate(0.5, 0.5, 0.5);
			OpenGLM.rotate(b, 1,0,0);OpenGLM.rotate(c, 0,0,1);
			OpenGLM.translate(-0.5, -0.5, -0.5);
			
				buf.addVertexWithUV(0,	 1.001, 0,	 0,1);
				buf.addVertexWithUV(0,	 1.001, 1.001, 0,0);
				buf.addVertexWithUV(1.001, 1.001, 1.001, 1,0);
				buf.addVertexWithUV(1.001, 1.001, 0,	 1,1);
				buf.draw();
				
			OpenGLM.translate(0.5, 0.5, 0.5);
			OpenGLM.rotate(-b, 1,0,0);OpenGLM.rotate(-c, 0,0,1);
			OpenGLM.translate(-0.5, -0.5, -0.5);
		}

		GL11U.endOpaqueRendering();
		
//		OpenGLM.disableTexture2D();
//		OpenGLM.translate(0.5, 1.5, 0.5);
//		new PlateModel(1, 1F, 1, true);
//		OpenGLM.enableTexture2D();
		OpenGLM.popMatrix();
	}
	
	
}