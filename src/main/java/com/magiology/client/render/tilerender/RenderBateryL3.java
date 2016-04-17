package com.magiology.client.render.tilerender;

import com.magiology.client.render.Textures;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class RenderBateryL3 extends TileEntitySpecialRendererM {

	VertexRenderer buf=Render.NVB();
	public EnumFacing[] connections = new EnumFacing[6];
	private final float p=1F/16F;
	private final float tH=1F/64F;
	private final float tHC=1F/16F;
	
	private final float tW=1F/64F;
	
	private final float tWC=1F/112F;
	
	public void renderConections(EnumFacing dir)
	{
		this.bindTexture(Textures.BateryL3Core);
			OpenGLM.translate(0.5F, 0.5F, 0.5F);
			if(dir.equals(EnumFacing.WEST)){
			}
			else if (dir.equals(EnumFacing.UP)){
				OpenGLM.rotate(-90, 0, 0, 1);
			}
			else if (dir.equals(EnumFacing.DOWN)){
				OpenGLM.rotate(90, 0, 0, 1);
			}
			else if (dir.equals(EnumFacing.SOUTH)){
				OpenGLM.rotate(90, 0, 1, 0);
			}
			else if (dir.equals(EnumFacing.EAST)){
				OpenGLM.rotate(-180, 0, 1, 0);
			}
			else if (dir.equals(EnumFacing.NORTH)){
				OpenGLM.rotate(-90, 0, 1, 0);
			}
			OpenGLM.translate(-0.5F, -0.5F, -0.5F);
			
			buf.addVertexWithUV(p*0,  p*9.5, p*6.5,  tW*48, tH*1);
			buf.addVertexWithUV(p*0,  p*6.5, p*6.5,  tW*48, tH*4);
			buf.addVertexWithUV(p*0,  p*6.5, p*9.5, tW*17, tH*4);
			buf.addVertexWithUV(p*0,  p*9.5, p*9.5, tW*17, tH*1);
			
			buf.addVertexWithUV(p*0, p*9.5, p*9.5, tWC*24.5, tHC*16);
			buf.addVertexWithUV(p*0, p*6.5, p*9.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*9.5, tWC*0,	tHC*0);
			buf.addVertexWithUV(p*1, p*9.5, p*9.5, tWC*0,	tHC*16);
			
			buf.addVertexWithUV(p*1, p*9.5, p*6.5, tWC*0,	tHC*16);
			buf.addVertexWithUV(p*1, p*6.5, p*6.5, tWC*0,	tHC*0);
			buf.addVertexWithUV(p*0, p*6.5, p*6.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*6.5, tWC*24.5, tHC*16);
			
			buf.addVertexWithUV(p*1, p*9.5, p*9.5, tWC*0,	tHC*16);
			buf.addVertexWithUV(p*1, p*9.5, p*6.5, tWC*0,	tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*6.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*0, p*9.5, p*9.5, tWC*24.5, tHC*16);
			
			buf.addVertexWithUV(p*0, p*6.5, p*9.5, tWC*24.5, tHC*16);
			buf.addVertexWithUV(p*0, p*6.5, p*6.5, tWC*24.5, tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*6.5, tWC*0,	tHC*0);
			buf.addVertexWithUV(p*1, p*6.5, p*9.5, tWC*0,	tHC*16);
			buf.draw();
			this.bindTexture(Textures.BateryL3Core);
			OpenGLM.translate(0.5F, 0.5F, 0.5F);
			OpenGLM.rotate(90, 0, 0, 1);
			OpenGLM.translate(-0.5F, -0.5F, -0.5F);
			
			buf.addVertexWithUV(p*0.5, p*15.5, p*0.5,  tW*17, tH*4);
			buf.addVertexWithUV(p*0.5, p*15.5, p*15.5, tW*17, tH*1);
			buf.addVertexWithUV(p*1,   p*15.5, p*15.5, tW*48, tH*1);
			buf.addVertexWithUV(p*1,   p*15.5, p*0.5,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*15,  p*15.5, p*0.5,  tW*17, tH*4);
			buf.addVertexWithUV(p*15,  p*15.5, p*15.5, tW*17, tH*1);
			buf.addVertexWithUV(p*15.5,p*15.5, p*15.5, tW*48, tH*1);
			buf.addVertexWithUV(p*15.5,p*15.5, p*0.5,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,   p*15.5, p*1,   tW*17, tH*4);
			buf.addVertexWithUV(p*15,  p*15.5, p*1,   tW*17, tH*1);
			buf.addVertexWithUV(p*15,  p*15.5, p*0.5, tW*48, tH*1);
			buf.addVertexWithUV(p*1,   p*15.5, p*0.5, tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,   p*15.5, p*15.5, tW*17, tH*4);
			buf.addVertexWithUV(p*15,  p*15.5, p*15.5, tW*17, tH*1);
			buf.addVertexWithUV(p*15,  p*15.5, p*15,   tW*48, tH*1);
			buf.addVertexWithUV(p*1,   p*15.5, p*15,   tW*48, tH*4);
			
			
			buf.addVertexWithUV(p*1,  p*15.5, p*1,  tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*15.5, p*15, tW*17, tH*1);
			buf.addVertexWithUV(p*1,  p*15,   p*15, tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*15,   p*1,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*15, p*15, p*1,	tW*17, tH*4);
			buf.addVertexWithUV(p*15, p*15, p*15,   tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15.5, p*15, tW*48, tH*1);
			buf.addVertexWithUV(p*15, p*15.5, p*1,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,  p*15,   p*1, tW*17, tH*4);
			buf.addVertexWithUV(p*15, p*15,   p*1, tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15.5, p*1, tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*15.5, p*1, tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,  p*15.5, p*15, tW*17, tH*4);
			buf.addVertexWithUV(p*15, p*15.5, p*15, tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15,   p*15, tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*15,   p*15, tW*48, tH*4);
		buf.draw();
		OpenGLM.translate(0.5F, 0.5F, 0.5F);
		OpenGLM.rotate(-90, 0, 0, 1);
		OpenGLM.translate(-0.5F, -0.5F, -0.5F);
		
		
		OpenGLM.translate(0.5F, 0.5F, 0.5F);
		if(dir.equals(EnumFacing.WEST)){
			OpenGLM.rotate(0, 0, 1, 0);
		}
		else if (dir.equals(EnumFacing.SOUTH)){
			OpenGLM.rotate(-90, 0, 1, 0);
		}else if (dir.equals(EnumFacing.EAST)){
			OpenGLM.rotate(180, 0, 1, 0);
		}else if (dir.equals(EnumFacing.UP)){
			OpenGLM.rotate(90, 0, 0, 1);
		}
		else if (dir.equals(EnumFacing.DOWN)){
			OpenGLM.rotate(-90, 0, 0, 1);
		}
		else if (dir.equals(EnumFacing.NORTH)){
			OpenGLM.rotate(90, 0, 1, 0);
		}
		OpenGLM.translate(-0.5F, -0.5F, -0.5F);
	}
	
	public void renderSides() {

		this.bindTexture(Textures.BateryL3Core);
			buf.addVertexWithUV(p*1,  p*15,   p*1,  tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*1,	p*1,  tW*48, tH*4);
			buf.addVertexWithUV(p*1,  p*1,	p*15, tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*15,   p*15, tW*17, tH*1);

			buf.addVertexWithUV(p*15,  p*15,   p*15, tW*17, tH*1);
			buf.addVertexWithUV(p*15,  p*1,	p*15, tW*17, tH*4);
			buf.addVertexWithUV(p*15,  p*1,	p*1,  tW*48, tH*4);
			buf.addVertexWithUV(p*15,  p*15,   p*1,  tW*48, tH*1);
		
			buf.addVertexWithUV(p*15, p*1,	p*15,  tW*48, tH*4);
			buf.addVertexWithUV(p*15, p*15,   p*15,  tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*15,   p*15,  tW*17, tH*1);
			buf.addVertexWithUV(p*1,  p*1,	p*15,  tW*17, tH*4);

			buf.addVertexWithUV(p*1,  p*1,	p*1,  tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*15,   p*1,  tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15,   p*1,  tW*48, tH*1);
			buf.addVertexWithUV(p*15, p*1,	p*1,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*1,  p*15,	p*1,  tW*17, tH*4);
			buf.addVertexWithUV(p*1,  p*15,   p*15,  tW*17, tH*1);
			buf.addVertexWithUV(p*15, p*15,   p*15,  tW*48, tH*1);
			buf.addVertexWithUV(p*15, p*15,	p*1,  tW*48, tH*4);
			
			buf.addVertexWithUV(p*15, p*1,	p*1,  tW*48, tH*4);
			buf.addVertexWithUV(p*15, p*1,   p*15,  tW*48, tH*1);
			buf.addVertexWithUV(p*1,  p*1,   p*15,  tW*17, tH*1);
			buf.addVertexWithUV(p*1,  p*1,	p*1,  tW*17, tH*4);	
			buf.draw();
		
	}
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		OpenGLM.translate(x,y,z);
		OpenGLM.enableLighting();
		OpenGLM.enableCull();
		
		this.renderSides();
		this.renderConections(EnumFacing.DOWN);
		this.renderConections(EnumFacing.EAST);
		this.renderConections(EnumFacing.NORTH);
		this.renderConections(EnumFacing.SOUTH);
		this.renderConections(EnumFacing.UP);
		this.renderConections(EnumFacing.WEST);
		
		
		OpenGLM.enableLighting();
		OpenGLM.translate(-x, -y, -z);
	}
	
}