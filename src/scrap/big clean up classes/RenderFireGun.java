package com.magiology.client.render.tilerender;

import com.magiology.client.render.Textures;
import com.magiology.mcobjects.tileentityes.TileEntityFireGun;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class RenderFireGun extends TileEntitySpecialRendererM {
	
	private final float p= 1F/16F;
	private final float tW=1F/72F;
	private final float tH=1F/64F;
	VertexRenderer buf=Render.NVB();
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		TileEntityFireGun dir= (TileEntityFireGun) tileentity;
		buf.cleanUp();
		OpenGLM.translate(x,y,z);
		OpenGLM.enableLighting();
		OpenGLM.enableCull();
		
		for(int i=0; i<dir.rotation.length; i++){
			if(dir.rotation[i] != null){
				drawGunStand(dir.rotation[i], tileentity,x,y,z,f);
				drawGun(dir.rotation[i], tileentity,x,y,z,f);
			}
		}
		
		if(dir.rotation[0]==null)if(dir.rotation[1]==null&&dir.rotation[2]==null&&dir.rotation[3]==null){
			drawGunStand(EnumFacing.SOUTH, tileentity,x,y,z,f);
		}
		
		OpenGLM.enableLighting();
		OpenGLM.translate(-x, -y, -z);
	}
	
	public void drawGunStand(EnumFacing dir, TileEntity tileentity, double x, double y, double z, float f){
		this.bindTexture(Textures.FireGunGun);

		OpenGLM.translate(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){
			OpenGLM.rotate(180, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.SOUTH)){
			OpenGLM.rotate(-90, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.EAST)){
		}
		else if (dir.equals(EnumFacing.NORTH)){
			OpenGLM.rotate(90, 0, 1, 0);
		}
		OpenGLM.translate(-0.5F, -0.5F, -0.5F);
		{
		buf.addVertexWithUV(p*8, p*4, p*9, tW*0, tH*0);
		buf.addVertexWithUV(p*8, p*0, p*9, tW*0, tH*32);
		buf.addVertexWithUV(p*10,p*0, p*9, tW*16,tH*32);
		buf.addVertexWithUV(p*10,p*4, p*9, tW*16,tH*0);

		buf.addVertexWithUV(p*10,p*4, p*7, tW*16,tH*0);
		buf.addVertexWithUV(p*10,p*0, p*7, tW*16,tH*32);
		buf.addVertexWithUV(p*8, p*0, p*7, tW*0, tH*32);
		buf.addVertexWithUV(p*8, p*4, p*7, tW*0, tH*0);
		
		buf.addVertexWithUV(p*8, p*4, p*7, tW*16,tH*0);
		buf.addVertexWithUV(p*8, p*0, p*7, tW*16,tH*32);
		buf.addVertexWithUV(p*8, p*0, p*9, tW*0, tH*32);
		buf.addVertexWithUV(p*8, p*4, p*9, tW*0, tH*0);

		buf.addVertexWithUV(p*10, p*4, p*9, tW*0, tH*0);
		buf.addVertexWithUV(p*10, p*0, p*9, tW*0, tH*32);
		buf.addVertexWithUV(p*10, p*0, p*7, tW*16,tH*32);
		buf.addVertexWithUV(p*10, p*4, p*7, tW*16,tH*0);
		buf.draw();
		}
		OpenGLM.translate(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){
			OpenGLM.rotate(-180, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.SOUTH)){
			OpenGLM.rotate(90, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.EAST)){
		}
		else if (dir.equals(EnumFacing.NORTH)){
			OpenGLM.rotate(-90, 0, 1, 0);
		}
		OpenGLM.translate(-0.5F, -0.5F, -0.5F);
	}
	public void drawGun(EnumFacing dir, TileEntity tileentity, double x, double y, double z, float f){
		TileEntityFireGun isit= (TileEntityFireGun) tileentity;
		OpenGLM.pushMatrix();
		this.bindTexture(Textures.FireGunGun);

		OpenGLM.translate(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){
			OpenGLM.rotate(180, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.SOUTH)){
			OpenGLM.rotate(-90, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.EAST)){
		}
		else if (dir.equals(EnumFacing.NORTH)){
			OpenGLM.rotate(90, 0, 1, 0);
		}
		OpenGLM.translate(-0.5F, -0.5F, -0.5F);
		{
			{
			OpenGLM.translate(-UtilM.calculatePos(isit.prevAnimation, isit.animation),0,0);
			buf.addVertexWithUV(p*5,  p*7, p*9.5,  tW*16, tH*0);
			buf.addVertexWithUV(p*5,  p*4, p*9.5,  tW*16, tH*24);
			buf.addVertexWithUV(p*12, p*4, p*9.5,  tW*72, tH*24);
			buf.addVertexWithUV(p*12, p*7, p*9.5,  tW*72, tH*0);

			buf.addVertexWithUV(p*12, p*7, p*6.5,  tW*72, tH*0);
			buf.addVertexWithUV(p*12, p*4, p*6.5,  tW*72, tH*24);
			buf.addVertexWithUV(p*5,  p*4, p*6.5,  tW*16, tH*24);
			buf.addVertexWithUV(p*5,  p*7, p*6.5,  tW*16, tH*0);
			
			buf.addVertexWithUV(p*12, p*7, p*9.5,  tW*72, tH*0);
			buf.addVertexWithUV(p*12, p*7, p*6.5,  tW*72, tH*24);
			buf.addVertexWithUV(p*5,  p*7, p*6.5,  tW*16, tH*24);
			buf.addVertexWithUV(p*5,  p*7, p*9.5,  tW*16, tH*0);

			buf.addVertexWithUV(p*12, p*7, p*9.5,  tW*24, tH*64);
			buf.addVertexWithUV(p*12, p*4, p*9.5,  tW*24, tH*40);
			buf.addVertexWithUV(p*12, p*4, p*6.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*12, p*7, p*6.5,  tW*0, tH*64);
			
			buf.addVertexWithUV(p*5, p*7, p*6.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*5, p*4, p*6.5,  tW*0, tH*64);
			buf.addVertexWithUV(p*5, p*4, p*9.5,  tW*24, tH*64);
			buf.addVertexWithUV(p*5, p*7, p*9.5,  tW*24, tH*40);
			}
			
			{
			buf.addVertexWithUV(p*12, p*6.5, p*9,  tW*16, tH*24);
			buf.addVertexWithUV(p*12, p*4.5, p*9,  tW*16, tH*40);
			buf.addVertexWithUV(p*19, p*4.5, p*9,  tW*72,tH*40);
			buf.addVertexWithUV(p*19, p*6.5, p*9,  tW*72,tH*24);

			buf.addVertexWithUV(p*19, p*6.5, p*7,  tW*72,tH*24);
			buf.addVertexWithUV(p*19, p*4.5, p*7,  tW*72,tH*40);
			buf.addVertexWithUV(p*12, p*4.5, p*7,  tW*16, tH*40);
			buf.addVertexWithUV(p*12, p*6.5, p*7,  tW*16, tH*24);
			
			buf.addVertexWithUV(p*19, p*6.5, p*9,  tW*72,tH*24);
			buf.addVertexWithUV(p*19, p*6.5, p*7,  tW*72,tH*40);
			buf.addVertexWithUV(p*12, p*6.5, p*7,  tW*16, tH*40);
			buf.addVertexWithUV(p*12, p*6.5, p*9,  tW*16, tH*24);

			buf.addVertexWithUV(p*19, p*6.5, p*9,  tW*24, tH*56);
			buf.addVertexWithUV(p*19, p*4.5, p*9,  tW*24, tH*40);
			buf.addVertexWithUV(p*19, p*4.5, p*7,  tW*40,  tH*40);
			buf.addVertexWithUV(p*19, p*6.5, p*7,  tW*40,  tH*56);
			}
			
			{
			buf.addVertexWithUV(p*19, p*5.5, p*8.5,  tW*0, tH*32);
			buf.addVertexWithUV(p*19, p*4.5, p*8.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*22, p*4.5, p*8.5,  tW*16,tH*40);
			buf.addVertexWithUV(p*22, p*5.5, p*8.5,  tW*16,tH*32);
				
			buf.addVertexWithUV(p*22, p*5.5, p*7.5,  tW*16,tH*32);
			buf.addVertexWithUV(p*22, p*4.5, p*7.5,  tW*16,tH*40);
			buf.addVertexWithUV(p*19, p*4.5, p*7.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*19, p*5.5, p*7.5,  tW*0, tH*32);
				
			buf.addVertexWithUV(p*22, p*5.5, p*8.5,  tW*16,tH*32);
			buf.addVertexWithUV(p*22, p*5.5, p*7.5,  tW*16,tH*40);
			buf.addVertexWithUV(p*19, p*5.5, p*7.5,  tW*0, tH*40);
			buf.addVertexWithUV(p*19, p*5.5, p*8.5,  tW*0, tH*32);
				
			buf.addVertexWithUV(p*22, p*5.5, p*8.5,  tW*40, tH*40);
			buf.addVertexWithUV(p*22, p*4.5, p*8.5,  tW*40, tH*48);
			buf.addVertexWithUV(p*22, p*4.5, p*7.5,  tW*48, tH*48);
			buf.addVertexWithUV(p*22, p*5.5, p*7.5,  tW*48, tH*40);
			}
			buf.draw();
		}
		OpenGLM.popMatrix();
	}
}
