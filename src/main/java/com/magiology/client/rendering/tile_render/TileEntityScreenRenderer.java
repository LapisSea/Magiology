package com.magiology.client.rendering.tile_render;

import com.magiology.client.renderers.Renderer;
import com.magiology.handlers.frame_buff.InWorldFrame;
import com.magiology.mc_objects.BlockStates;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
import com.magiology.util.m_extensions.TileEntitySpecialRendererM;
import com.magiology.util.objs.*;
import com.magiology.util.statics.*;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class TileEntityScreenRenderer extends TileEntitySpecialRendererM<TileEntityScreen>{
	
	
	@Override
	public void renderTileEntityAt(TileEntityScreen tile, Vec3M renderPos, float partialTicks){
		double minX=0,minY=0,maxX=1,maxY=1; 
		if(tile.hasBrain()){
			TileEntityScreen brain=tile.getBrain();
			InWorldFrame screen=brain.screenTexture;
//			PrintUtil.println(obj);
			if(screen==null){
				screen=brain.screenTexture=new InWorldFrame(brain.screen.xSize*64,brain.screen.ySize*64,false);
				screen.setRednerHook(f->renderScreen(tile.getBrain(),f.getWidth(),f.getHeight()));
			}else{
				screen.setSize(Math.max(brain.screen.xSize, 1)*64,Math.max(brain.screen.ySize, 1)*64);
			}
			
			if(tile.screenDirty)screen.requestRender();
			screen.bindTexture();
			double xs=brain.screen.xSize,ys=brain.screen.ySize;
			minX=(tile.xScreenOff)/xs;
			minY=(tile.yScreenOff)/ys;
			maxX=(tile.xScreenOff+1)/xs;
			maxY=(tile.yScreenOff+1)/ys;
		}else OpenGLM.disableTexture2D();
		
		EnumFacing rot=BlockStates.SAVE_ROTATION_FULL_3BIT.get(tile.getWorld().getBlockState(tile.getPos()));
		Vec3i rotVec=rot.getDirectionVec();
		Vec3M rot3d=new Vec3M();
		
		if(rotVec.getY()==1)rot3d.x=-90;
		else if(rotVec.getY()==-1)rot3d.x=90;
		if(rotVec.getX()!=0||rotVec.getZ()!=0)rot3d.y=Math.toDegrees(Math.atan2(rotVec.getX(),rotVec.getZ()));
		
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(renderPos);
		OpenGLM.translate(0.5F,0.5F,0.5F);
		OpenGLM.rotate(rot3d);
		OpenGLM.translate(-0.5F,-0.5F,-0.5F);
		Renderer.POS_UV.beginQuads();
		Renderer.POS_UV.addVertex(0, 1, 0.5,  minX,maxY);
		Renderer.POS_UV.addVertex(0, 0, 0.5,  minX,minY);
		Renderer.POS_UV.addVertex(1, 0, 0.5,  maxX,minY);
		Renderer.POS_UV.addVertex(1, 1, 0.5,  maxX,maxY);
		Renderer.POS_UV.draw();
		
		if(!tile.hasBrain())OpenGLM.enableTexture2D();
		OpenGLM.popMatrix();
	}
	
	private void renderScreen(TileEntityScreen tile, int width, int height){
		
		OpenGLM.disableTexture2D();
		ColorF.WHITE.bind();
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex(1,       1,        0);
		Renderer.POS.addVertex(1,       height-1, 0);
		Renderer.POS.addVertex(width-1, height-1, 0);
		Renderer.POS.addVertex(width-1, 1,        0);
		Renderer.POS.draw();

		ColorF.ORANGE.bind();
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(1,1,0);
		Renderer.LINES.addVertex(width-1,height-1,0);
		Renderer.LINES.addVertex(1,height-1,0);
		Renderer.LINES.addVertex(width-1,1,0);
		Renderer.LINES.draw();
		ColorF.GREEN.bind();
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex( 10,  10, 0);
		Renderer.POS.addVertex( 10, -10, 0);
		Renderer.POS.addVertex(-10, -10, 0);
		Renderer.POS.addVertex(-10,  10, 0);
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(width/2F, height/2F, 0);
		OpenGLM.rotateZ(Math.sin(((((double)UtilM.worldTime(tile))+PartialTicksUtil.partialTicks)/20+tile.getPos().toLong())%(Math.PI*2))*360);
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
