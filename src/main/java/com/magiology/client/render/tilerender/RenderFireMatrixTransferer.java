package com.magiology.client.render.tilerender;

import com.magiology.client.render.Textures;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixTransferer;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;

public class RenderFireMatrixTransferer extends TileEntitySpecialRendererM {

	VertexRenderer buf=Render.NVB();
	private final float p=1F/16F;
	
	float pos=0,ballRotation=0;
	
	public void drawBall(){
		{
			double minx=p*-0.75;double miny=p*-1.25;double minz=p*-0.75;
			double maxx=p*0.75;double maxy=p*0.25;double maxz=p*0.75;
			
			this.bindTexture(Textures.FireMatrixTransfererBase);
			

			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
		}
	}
	
	public void drawBase() {
		{
			double minx=p*4;double miny=p*0;double minz=p*4;
			double maxx=p*12;double maxy=p*3.5;double maxz=p*12;

			this.bindTexture(Textures.FireMatrixTransfererBase);

			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
			
			this.drawCube(p*6,p*3.5,p*6,p*10,p*5,p*10);
		}
	}
	public void drawCube(double minx,double miny,double minz,double maxx,double maxy,double maxz){
		buf.cleanUp();
		buf.addVertexWithUV(minx, miny, maxz, 0, 1);
		buf.addVertexWithUV(minx, miny, minz, 0, 0);
		buf.addVertexWithUV(maxx, miny, minz, 1, 0);
		buf.addVertexWithUV(maxx, miny, maxz, 1, 1);
		buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
		buf.addVertexWithUV(maxx, maxy, minz, 1, 0);
		buf.addVertexWithUV(minx, maxy, minz, 0, 0);
		buf.addVertexWithUV(minx, maxy, maxz, 0, 1);
		buf.addVertexWithUV(maxx, maxy, minz,  1, 1);
		buf.addVertexWithUV(maxx, miny, minz,  1, 0);
		buf.addVertexWithUV(minx, miny , minz, 0, 0);
		buf.addVertexWithUV(minx, maxy, minz,  0, 1);
		buf.addVertexWithUV(minx, maxy, maxz,  0, 1);
		buf.addVertexWithUV(minx, miny , maxz, 0, 0);
		buf.addVertexWithUV(maxx, miny, maxz,  1, 0);
		buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
		buf.addVertexWithUV(minx, maxy, minz, minz, miny);
		buf.addVertexWithUV(minx, miny, minz, minz, maxy);
		buf.addVertexWithUV(minx, miny, maxz, maxz, maxy);
		buf.addVertexWithUV(minx, maxy, maxz, maxz, miny);
		buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
		buf.addVertexWithUV(maxx, miny,  maxz, 1, 0);
		buf.addVertexWithUV(maxx, miny,  minz, 0, 0);
		buf.addVertexWithUV(maxx, maxy, minz,  0, 1);
		buf.draw();
	}
	public void drawWhaterThatIs(){
		{
			double minx=p*2;double miny=p*4;double minz=p*2;
			double maxx=p*4;double maxy=p*9;double maxz=p*4;
			
			this.bindTexture(Textures.FireMatrixTransfererBase);
			
			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
		}
		
		
	}
	public void drawWhaterThatIs2(){
		{
			double minx=p*2.25;double miny=p*9;double minz=p*2.25;
			double maxx=p*3.75;double maxy=p*15;double maxz=p*3.75;
			
			this.bindTexture(Textures.FireMatrixTransfererBase);
			

			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
		}
	}
	public void drawWhaterThatIs3(){
		{
			double minx=p*2.5;double miny=p*15;double minz=p*2.5;
			double maxx=p*3.5;double maxy=p*18;double maxz=p*3.5;
			
			this.bindTexture(Textures.FireMatrixTransfererBase);
			

			this.drawCube(minx, miny, minz, maxx, maxy, maxz);
		}
	}
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		
		if(ballRotation>360)ballRotation-=360;
		ballRotation++;
		
		if(tileentity instanceof TileEntityFireMatrixTransferer){
			pos=((TileEntityFireMatrixTransferer)tileentity).Pos;
			this.ballRotation=((TileEntityFireMatrixTransferer)tileentity).ballRotation;
		}
		this.bindTexture(Textures.FireMatrixTransfererBase);
		OpenGLM.translate(x,y,z);
		OpenGLM.enableCull();
		OpenGLM.enableLighting();
		
		for(int i=0;i<4;i++){
			OpenGLM.translate(0.5F, 0.5F, 0.5F);
			OpenGLM.rotate(-i*90, 0, 1, 0);
			OpenGLM.translate(-0.5F, -0.5F, -0.5F);
			
			OpenGLM.translate(p*5.5F, -p*3, p*1.5F);

			OpenGLM.rotate(-45, 0, 1, 0);
			OpenGLM.rotate(30, 0, 0, 1);
			drawWhaterThatIs();
			OpenGLM.rotate(-30, 0, 0, 1);
			OpenGLM.rotate(45, 0, 1, 0);
			
			OpenGLM.translate(-p*5.5F, p*3, -p*1.5F);
			

			OpenGLM.translate(p*1F, -p*2, -p*3F);
			OpenGLM.rotate(-45, 0, 1, 0);
			OpenGLM.rotate(-15, 0, 0, 1);
			drawWhaterThatIs2();
			OpenGLM.rotate(15, 0, 0, 1);
			OpenGLM.rotate(45, 0, 1, 0);
			OpenGLM.translate(-p*1F, p*2, p*3F);
			
			OpenGLM.translate(-p*5, p*10.5F,-p*9);
			OpenGLM.rotate(-45, 0, 1, 0);
			OpenGLM.rotate(-75, 0, 0, 1);
			drawWhaterThatIs3();
			OpenGLM.rotate(75, 0, 0, 1);
			OpenGLM.rotate(45, 0, 1, 0);
			OpenGLM.translate(p*5, -p*10.5F, p*9);
			
			OpenGLM.translate(0.5F, 0.5F, 0.5F);
			OpenGLM.rotate(i*90, 0, 1, 0);
			OpenGLM.translate(-0.5F, -0.5F, -0.5F);
			
		}
		OpenGLM.enableLighting();
		OpenGLM.disableTexture2D();
		GL11U.setUpOpaqueRendering(1);
		OpenGLM.color(1, 0.01, 0.01, 0.2);
		
		OpenGLM.translate(0.5F, p*9+pos,0.5F);

		OpenGLM.rotate(ballRotation, 0, 1, 0);
		OpenGLM.rotate(ballRotation+20, 0, 0, 1);
		
		
		drawBall();
		
		OpenGLM.rotate(ballRotation+90, 0, -2, 0);
		drawBall();
		OpenGLM.rotate(-ballRotation-90, 0, -2, 0);
		
		OpenGLM.rotate(ballRotation+72, 1, 1, 0);
		drawBall();
		OpenGLM.rotate(-ballRotation-72, 1, 1, 0);
		
		OpenGLM.rotate(ballRotation+64, 0, 1, 1);
		drawBall();
		OpenGLM.rotate(-ballRotation-64, 0, 1, 1);
		
		OpenGLM.rotate(ballRotation+170, 1, 0, 1);
		drawBall();
		OpenGLM.rotate(-ballRotation-170, 1, 0, 1);
		

		OpenGLM.rotate(-ballRotation-231, 1, 1, 0);
		drawBall();
		OpenGLM.rotate(ballRotation+231, 1, 1, 0);
		
		OpenGLM.rotate(-ballRotation-267, 0, 1, 1);
		drawBall();
		OpenGLM.rotate(ballRotation+267, 0, 1, 1);
		
		OpenGLM.rotate(-ballRotation-192, 1, 0, 1);
		drawBall();
		OpenGLM.rotate(ballRotation+192, 1, 0, 1);
		
		
		OpenGLM.rotate(-ballRotation-20, 0, 0, 1);
		OpenGLM.rotate(-ballRotation, 0, 1, 0);
		OpenGLM.translate(-0.5F, -p*9-pos, -0.5F);
		OpenGLM.enableTexture2D();
		GL11U.endOpaqueRendering();
		 
		drawBase();
		
		OpenGLM.enableLighting();
		OpenGLM.translate(-x, -y, -z);
	}
	
	
}
