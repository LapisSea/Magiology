package com.magiology.client.rendering.tile_render;

import com.magiology.client.renderers.Renderer;
import com.magiology.handlers.frame_buff.InWorldFrame;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
import com.magiology.util.m_extensions.TileEntitySpecialRendererM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;

public class TileEntityScreenRenderer extends TileEntitySpecialRendererM<TileEntityScreen>{
	
	
	@Override
	public void renderTileEntityAt(TileEntityScreen screenTile, Vec3M renderPos, float partialTicks){

		if(screenTile.screenTexture==null){
			screenTile.screenTexture=new InWorldFrame(64,64,false);
			screenTile.screenTexture.setRednerHook(f->renderScreen(screenTile,f.getWidth(),f.getHeight()));
		}
		InWorldFrame screen=screenTile.screenTexture;
		
		if(screenTile.screenDirty)screen.requestRender();
		OpenGLM.pushMatrix();
		screen.bindTexture();
		OpenGLM.translate(renderPos);
		OpenGLM.disableLighting();
		
		Renderer.POS_UV.beginQuads();
		Renderer.POS_UV.addVertex(1, 1, 0,  1,1);
		Renderer.POS_UV.addVertex(1, 0, 0,  1,0);
		Renderer.POS_UV.addVertex(0, 0, 0,  0,0);
		Renderer.POS_UV.addVertex(0, 1, 0,  0,1);
		Renderer.POS_UV.draw();
		OpenGLM.enableLighting();
		
		OpenGLM.popMatrix();
	}
	
	private void renderScreen(TileEntityScreen screenTile, int width, int height){
		
		OpenGLM.disableTexture2D();
		ColorF.WHITE.bind();
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex(1,       1,        0);
		Renderer.POS.addVertex(1,       height-1, 0);
		Renderer.POS.addVertex(width-1, height-1, 0);
		Renderer.POS.addVertex(width-1, 1,        0);
		Renderer.POS.draw();
		
		ColorF.GREEN.bind();
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex( 10,  10, 0);
		Renderer.POS.addVertex( 10, -10, 0);
		Renderer.POS.addVertex(-10, -10, 0);
		Renderer.POS.addVertex(-10,  10, 0);
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(30, 30, 0);
		OpenGLM.rotateZ(Math.sin((System.currentTimeMillis()/1000D)%(Math.PI*2))*360);
		Renderer.POS.draw();
		OpenGLM.popMatrix();
		
		
		
		ColorF.BLACK.bind();
		Renderer.POS.beginQuads();
		
		Renderer.POS.addVertex(0,     0, 0);
		Renderer.POS.addVertex(0,     1, 0);
		Renderer.POS.addVertex(width, 1, 0);
		Renderer.POS.addVertex(width, 0, 0);
		
		Renderer.POS.addVertex(0,     height-1, 0);
		Renderer.POS.addVertex(0,     height,   0);
		Renderer.POS.addVertex(width, height,   0);
		Renderer.POS.addVertex(width, height-1, 0);

		Renderer.POS.addVertex(0, 0,      0);
		Renderer.POS.addVertex(0, height, 0);
		Renderer.POS.addVertex(1, height, 0);
		Renderer.POS.addVertex(1, 0,      0);

		Renderer.POS.addVertex(width-1, 0,      0);
		Renderer.POS.addVertex(width-1, height, 0);
		Renderer.POS.addVertex(width,   height, 0);
		Renderer.POS.addVertex(width,   0,      0);
		
		Renderer.POS.draw();
		ColorF.WHITE.bind();
		OpenGLM.enableTexture2D();
	}
	
	
}
