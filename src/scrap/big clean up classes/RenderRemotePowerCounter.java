package com.magiology.client.render.tilerender;

import java.text.DecimalFormat;

import com.magiology.client.render.Textures;
import com.magiology.mcobjects.tileentityes.TileEntityRemotePowerCounter;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RenderRemotePowerCounter extends TileEntitySpecialRendererM {
	
	World world;
	int metadata=0;
	Block block;
	
	double powerBar;
	int maxPB,currentP;
	float p=1F/16F;
	VertexRenderer buf=Render.NVB();
	FontRenderer fr=TessUtil.getFontRenderer();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentit, double x1, double y1, double z1, float f) {
		TileEntityRemotePowerCounter tileentity=(TileEntityRemotePowerCounter)tileentit;
		world=tileentity.getWorld();
		metadata=U.getBlockMetadata(tileentity.getWorld(), tileentity.getPos());
		
		float x=0,y=0,z=0,xr=0,yr=0,zr=0;
		switch (metadata){
		case 0:{
			zr=-90;
			y=p*6;
		}break;
		case 1:{
			zr=90;
			y=p*-6;
		}break;
		case 2:{
			yr=90;
			z=p*6;
		}break;
		case 3:{
			yr=-90;
			z=p*-6;
		}break;
		case 4:{
			x=p*6;
		}break;
		case 5:{
			yr=180;
			x=-p*6;
		}break;
		default:{
		}break;
			
		}
		
		block=U.getBlock(world,tileentity.getPos());
		
		
		powerBar=tileentity.powerBar;
		maxPB=tileentity.maxPB;
		currentP=tileentity.currentP;
		
		OpenGLM.translate(x1,y1,z1);
		OpenGLM.translate(x,y,z);
		OpenGLM.translate(0.5, 0.5, 0.5);
		OpenGLM.rotate(-zr, 0, 0, 1);
		OpenGLM.rotate(-yr, 0, 1, 0);
		OpenGLM.rotate(-xr, 1, 0, 0);
		OpenGLM.translate(-0.5, -0.5+p*1, -0.5);
		
		OpenGLM.enableCull();
		
		drawCore(tileentity.getPos().getX(),tileentity.getPos().getY(),tileentity.getPos().getZ());
		OpenGLM.disableLighting();
		OpenGLM.color(1, 1, 1, 1);
		drawText();
		
		OpenGLM.enableLighting();
		
		OpenGLM.translate(0.5, 0.5-p*1, 0.5);
		OpenGLM.rotate(xr, 1, 0, 0);
		OpenGLM.rotate(yr, 0, 1, 0);
		OpenGLM.rotate(zr, 0, 0, 1);
		OpenGLM.translate(-0.5, -0.5, -0.5);
		OpenGLM.translate(-x, -y, -z);
		OpenGLM.translate(-x1, -y1, -z1);
		
		
		
		
	}
	
	public void drawText(){
		float x=p*8-0.001F;
		float y=p*9;
		float z=p*3;
		float xr=90;
		float yr=90;
		float zr=90;
		float scale=0.0049F;
		
		OpenGLM.translate(x,y,z);
		OpenGLM.rotate(-zr, 0, 0, 1);OpenGLM.rotate(-yr, 0, 1, 0);OpenGLM.rotate(-xr, 1, 0, 0);
		OpenGLM.scale(scale, scale, scale);
		
		OpenGLM.enableTexture2D();
		String pauwa=Integer.toString(maxPB)+"/"+Integer.toString(currentP);
		double Precent=currentP!=maxPB?(powerBar*100>0?powerBar*100:0):100;
		DecimalFormat df = new DecimalFormat("###.##");
		String PrecentS=Precent<=0?(currentP>0?"Almost empty":"Empty"):(df.format(Precent)+"%");
		
		fr.drawString(pauwa, 0, 0, 11111);
		fr.drawString(PrecentS, 0, 10, 11111);
		fr.drawString(block.getLocalizedName(), 0, 20, 11111);
		
		OpenGLM.scale(1F/scale, 1F/scale, 1F/scale);
		OpenGLM.rotate(xr, 1, 0, 0);OpenGLM.rotate(yr, 0, 1, 0);OpenGLM.rotate(zr, 0, 0, 1);
		OpenGLM.translate(-x, -y, -z);
	}

	public void drawCore(int x, int y, int z){
			
			TessUtil.bindTexture(Textures.PowerCounterEnergyBar);
			double var1=powerBar;
			double var2=p*5+p*4*var1;
			double var3=1-var1;
			
			{
			buf.addVertexWithUV(p*8-0.001, var2, p*11, 0, var3);
			buf.addVertexWithUV(p*8-0.001, p*5,  p*11, 0, 1);
			buf.addVertexWithUV(p*8-0.001, p*5,  p*13, 1, 1);
			buf.addVertexWithUV(p*8-0.001, var2, p*13, 1, var3);
			buf.draw();
			}
			
			
				double minx=p*8;double miny=p*4;double minz=p*2;
				double maxx=p*10;double maxy=p*10;double maxz=p*14;
				

				TessUtil.bindTexture(Textures.vanillaBrick);
				
				
				{
				buf.addVertexWithUV(minx, maxy, minz, minz, miny);
				buf.addVertexWithUV(minx, miny, minz, minz, maxy);
				buf.addVertexWithUV(minx, miny, maxz, maxz, maxy);
				buf.addVertexWithUV(minx, maxy, maxz, maxz, miny);
				}
				
				
				{
				buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
				buf.addVertexWithUV(maxx, miny,  maxz, 1, 0);
				buf.addVertexWithUV(maxx, miny,  minz, 0, 0);
				buf.addVertexWithUV(maxx, maxy, minz,  0, 1);
				}
				
				
				{
				buf.addVertexWithUV(minx, maxy, maxz,  0, 1);
				buf.addVertexWithUV(minx, miny , maxz, 0, 0);
				buf.addVertexWithUV(maxx, miny, maxz,  1, 0);
				buf.addVertexWithUV(maxx, maxy, maxz,  1, 1);
				}
				
				
				{
				buf.addVertexWithUV(maxx, maxy, minz,  1, 1);
				buf.addVertexWithUV(maxx, miny, minz,  1, 0);
				buf.addVertexWithUV(minx, miny , minz, 0, 0);
				buf.addVertexWithUV(minx, maxy, minz,  0, 1);
				}
				
				
				{
				buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
				buf.addVertexWithUV(maxx, maxy, minz, 1, 0);
				buf.addVertexWithUV(minx, maxy, minz, 0, 0);
				buf.addVertexWithUV(minx, maxy, maxz, 0, 1);
				}
				
				
				{
				buf.addVertexWithUV(minx, miny, maxz, 0, 1);
				buf.addVertexWithUV(minx, miny, minz, 0, 0);
				buf.addVertexWithUV(maxx, miny, minz, 1, 0);
				buf.addVertexWithUV(maxx, miny, maxz, 1, 1);
			}
			buf.draw();
			
		
	}
	
	

}
