package com.magiology.client.render.tilerender;

import com.magiology.client.render.Textures;
import com.magiology.mcobjects.tileentityes.TileEntityOreStructureCore;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;

public class RenderOreStructureCore extends TileEntitySpecialRendererM{
	private final float p= 1F/16F;
	private final float tW=1F/96F;
	private final float tH=1F/80F;
	VertexRenderer buf=Render.NVB();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f){
		TileEntityOreStructureCore core= (TileEntityOreStructureCore) tileentity;
		OpenGLM.translate(x,y,z);
		OpenGLM.translate(0.5, 1, 0.5);
		OpenGLM.enableLighting();
		OpenGLM.enableCull();
		
		if(core.updateStructureHelper==true)renderTop();
		
		OpenGLM.enableLighting();
		OpenGLM.translate(-0.5, -1, -0.5);
		OpenGLM.translate(-x, -y, -z);
	}

	public void renderTop(){
		OpenGLM.enableLighting();
		this.bindTexture(Textures.OreStructureCore);
			buf.addVertexWithUV(-p*10, p*0, -p*10,		tW*88, tH*0);
			buf.addVertexWithUV(-p*10, p*0,  p*10,		tW*88, tH*80);
			buf.addVertexWithUV(-p*10, p*2,  p*10,		tW*96, tH*80);
			buf.addVertexWithUV(-p*10, p*2, -p*10,		tW*96, tH*0);
			
			buf.addVertexWithUV(-p*8, p*2, -p*8,		  tW*80, tH*0);
			buf.addVertexWithUV(-p*8, p*2,  p*8,		  tW*80, tH*72);
			buf.addVertexWithUV(-p*8, p*0,  p*8,		  tW*88, tH*72);
			buf.addVertexWithUV(-p*8, p*0, -p*8,		  tW*88, tH*0);
			

			buf.addVertexWithUV(p*10, p*2, -p*10,		 tW*88, tH*0);
			buf.addVertexWithUV(p*10, p*2,  p*10,		 tW*88, tH*80);
			buf.addVertexWithUV(p*10, p*0,  p*10,		 tW*96, tH*80);
			buf.addVertexWithUV(p*10, p*0, -p*10,		 tW*96, tH*0);

			buf.addVertexWithUV(p*8, p*0, -p*8,		   tW*80, tH*0);
			buf.addVertexWithUV(p*8, p*0,  p*8,		   tW*80, tH*72);
			buf.addVertexWithUV(p*8, p*2,  p*8,		   tW*88, tH*72);
			buf.addVertexWithUV(p*8, p*2, -p*8,		   tW*88, tH*0);
			
			
			buf.addVertexWithUV( p*10, p*2, p*10,		 tW*88, tH*0);
			buf.addVertexWithUV(-p*10, p*2, p*10,		 tW*88, tH*80);
			buf.addVertexWithUV(-p*10, p*0, p*10,		 tW*96, tH*80);
			buf.addVertexWithUV( p*10, p*0, p*10,		 tW*96, tH*0);
			
			buf.addVertexWithUV( p*8, p*0, p*8,		   tW*80, tH*0);
			buf.addVertexWithUV(-p*8, p*0, p*8,		   tW*80, tH*72);
			buf.addVertexWithUV(-p*8, p*2, p*8,		   tW*88, tH*72);
			buf.addVertexWithUV( p*8, p*2, p*8,		   tW*88, tH*0);
			

			buf.addVertexWithUV( p*10, p*0, -p*10,		tW*88, tH*0);
			buf.addVertexWithUV(-p*10, p*0, -p*10,		tW*88, tH*80);
			buf.addVertexWithUV(-p*10, p*2, -p*10,		tW*96, tH*80);
			buf.addVertexWithUV( p*10, p*2, -p*10,		tW*96, tH*0);

			buf.addVertexWithUV( p*8, p*2, -p*8,		  tW*80, tH*0);
			buf.addVertexWithUV(-p*8, p*2, -p*8,		  tW*80, tH*72);
			buf.addVertexWithUV(-p*8, p*0, -p*8,		  tW*88, tH*72);
			buf.addVertexWithUV( p*8, p*0, -p*8,		  tW*88, tH*0);
			
			
			
			buf.addVertexWithUV(-p*10, p*2, -p*10,		tW*0,  tH*0);
			buf.addVertexWithUV(-p*10, p*2,  p*10,		tW*80, tH*0);
			buf.addVertexWithUV(p*10,  p*2,  p*10,		tW*80, tH*80);
			buf.addVertexWithUV(p*10,  p*2, -p*10,		tW*0,  tH*80);
			buf.draw();
		OpenGLM.enableLighting();
	}
}