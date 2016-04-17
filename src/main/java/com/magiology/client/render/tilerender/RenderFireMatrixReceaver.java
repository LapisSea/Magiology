package com.magiology.client.render.tilerender;

import com.magiology.client.render.Textures;
import com.magiology.client.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.client.render.aftereffect.TwoDotsLineRender;
import com.magiology.forgepowered.events.client.RenderEvents;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;
import com.magiology.util.utilobjects.vectors.TwoDots;

import net.minecraft.tileentity.TileEntity;

public class RenderFireMatrixReceaver extends TileEntitySpecialRendererM{
	
	VertexRenderer buf=Render.NVB();
	
	public void drawCube(double minx,double miny,double minz,double maxx,double maxy,double maxz){
			
			buf.addVertexWithUV(minx, maxy, minz, 0, 0);
			buf.addVertexWithUV(minx, miny, minz, 0, 1);
			buf.addVertexWithUV(minx, miny, maxz, 1, 1);
			buf.addVertexWithUV(minx, maxy, maxz, 1, 0);
			
			buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
			buf.addVertexWithUV(maxx, miny,  maxz, 1, 0);
			buf.addVertexWithUV(maxx, miny,  minz, 0, 0);
			buf.addVertexWithUV(maxx, maxy, minz,  0, 1);
			
			buf.addVertexWithUV(minx, maxy, maxz,  0, 1);
			buf.addVertexWithUV(minx, miny , maxz, 0, 0);
			buf.addVertexWithUV(maxx, miny, maxz,  1, 0);
			buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
			
			buf.addVertexWithUV(maxx, maxy, minz,  1, 1);
			buf.addVertexWithUV(maxx, miny, minz,  1, 0);
			buf.addVertexWithUV(minx, miny , minz, 0, 0);
			buf.addVertexWithUV(minx, maxy, minz,  0, 1);
			
			buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
			buf.addVertexWithUV(maxx, maxy, minz, 1, 0);
			buf.addVertexWithUV(minx, maxy, minz, 0, 0);
			buf.addVertexWithUV(minx, maxy, maxz, 0, 1);
			
			buf.addVertexWithUV(minx, miny, maxz, 0, 1);
			buf.addVertexWithUV(minx, miny, minz, 0, 0);
			buf.addVertexWithUV(maxx, miny, minz, 1, 0);
			buf.addVertexWithUV(maxx, miny, maxz, 1, 1);
			buf.draw();
	}

	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float partialTicks) {
		TileEntityFireMatrixReceaver tile=(TileEntityFireMatrixReceaver) tileEntity;
		
		boolean var1=true;
		for(int a=0;a<RenderEvents.universalLongRender.size();a++){
			LongAfterRenderRenderer ab=RenderEvents.universalLongRender.get(a);
			if(ab instanceof TwoDotsLineRender&&!((TwoDotsLineRender)ab).isDead())if(((TwoDotsLineRender)ab).tile==tile){
				if(tile.hasTransferPoint){
					var1=false;
				}else{
					ab.kill();
				}
			}
		}
		if(var1)RenderEvents.spawnLARR(new TwoDotsLineRender(new TwoDots(tile.x()+0.5, tile.y()+0.5, tile.z()+0.5, tile.transferp.getX()+0.5, tile.transferp.getY()+0.5, tile.transferp.getZ()+0.5),tile));
		
		float rotation=PartialTicksUtil.calculatePos(tile.prevRotation,tile.rotation);
		OpenGLM.pushMatrix();
		OpenGLM.translate(posX,posY,posZ);
		OpenGLM.enableCull();
		
		this.bindTexture(Textures.FireMatrixReceaverBase);
		

		OpenGLM.enableLighting();
		OpenGLM.disableTexture2D();
		GL11U.setUpOpaqueRendering(1);
		OpenGLM.color(1, 0.01, 0.01, 0.2);
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(0.5F, 0.5F, 0.5F);
		OpenGLM.rotate(rotation, 0, 1, 0);
		OpenGLM.rotate(rotation+20, 0, 0, 1);
		
		float noise=PowerUtil.getPowerPrecentage(tile)/50.0F;
		this.drawCube(p*-0.75+RandUtil.CRF(noise), p*-1.25+RandUtil.CRF(noise), p*-0.75+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise), p*0.25+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise));
		
		OpenGLM.rotate(rotation+90, 0, -2, 0);
		this.drawCube(p*-0.75+RandUtil.CRF(noise), p*-1.25+RandUtil.CRF(noise), p*-0.75+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise), p*0.25+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise));
		OpenGLM.rotate(-rotation-90, 0, -2, 0);
		
		OpenGLM.rotate(rotation+72, 1, 1, 0);
		this.drawCube(p*-0.75+RandUtil.CRF(noise), p*-1.25+RandUtil.CRF(noise), p*-0.75+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise), p*0.25+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise));
		OpenGLM.rotate(-rotation-72, 1, 1, 0);
		
		OpenGLM.rotate(rotation+64, 0, 1, 1);
		this.drawCube(p*-0.75+RandUtil.CRF(noise), p*-1.25+RandUtil.CRF(noise), p*-0.75+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise), p*0.25+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise));
		OpenGLM.rotate(-rotation-64, 0, 1, 1);
		
		OpenGLM.rotate(rotation+170, 1, 0, 1);
		this.drawCube(p*-0.75+RandUtil.CRF(noise), p*-1.25+RandUtil.CRF(noise), p*-0.75+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise), p*0.25+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise));
		OpenGLM.rotate(-rotation-170, 1, 0, 1);
		

		OpenGLM.rotate(-rotation-231, 1, 1, 0);
		this.drawCube(p*-0.75+RandUtil.CRF(noise), p*-1.25+RandUtil.CRF(noise), p*-0.75+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise), p*0.25+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise));
		OpenGLM.rotate(rotation+231, 1, 1, 0);
		
		OpenGLM.rotate(-rotation-267, 0, 1, 1);
		this.drawCube(p*-0.75+RandUtil.CRF(noise), p*-1.25+RandUtil.CRF(noise), p*-0.75+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise), p*0.25+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise));
		OpenGLM.rotate(rotation+267, 0, 1, 1);
		
		OpenGLM.rotate(-rotation-192, 1, 0, 1);
		this.drawCube(p*-0.75+RandUtil.CRF(noise), p*-1.25+RandUtil.CRF(noise), p*-0.75+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise), p*0.25+RandUtil.CRF(noise), p*0.75+RandUtil.CRF(noise));
		OpenGLM.rotate(rotation+192, 1, 0, 1);
		
		OpenGLM.popMatrix();
		
		
		OpenGLM.enableTexture2D();
		GL11U.endOpaqueRendering();
		
		this.drawCube(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
		
		
		
		
		OpenGLM.enableLighting();
		OpenGLM.popMatrix();
	}
	
}
