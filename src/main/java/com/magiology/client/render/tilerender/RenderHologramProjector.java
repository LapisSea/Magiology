package com.magiology.client.render.tilerender;

import java.util.Iterator;

import com.magiology.mcobjects.tileentityes.hologram.Button;
import com.magiology.mcobjects.tileentityes.hologram.Field;
import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.mcobjects.tileentityes.hologram.Slider;
import com.magiology.mcobjects.tileentityes.hologram.TextBox;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;

public class RenderHologramProjector extends TileEntitySpecialRendererM{
	
	private TileEntityHologramProjector tile;
	
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float partialTicks){
		tile=(TileEntityHologramProjector)t;
//		tile.arraySize.updateValue(tile.holoObjects);
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(x,y,z);
		GL11U.texture(false);
		GL11U.glLighting(true);
		TessUtil.drawCube(t.getBlockType().getBlockBoundsMinX(),t.getBlockType().getBlockBoundsMinY(),t.getBlockType().getBlockBoundsMinZ(),t.getBlockType().getBlockBoundsMaxX(),t.getBlockType().getBlockBoundsMaxY(),t.getBlockType().getBlockBoundsMaxZ());
		GL11U.setUpOpaqueRendering(1);
		GL11U.glScale(0.99999);
		ColorF color=new ColorF(UtilM.fluctuateSmooth(10, 0)*0.2+tile.mainColor.x,UtilM.fluctuateSmooth(35, 0)*0.2+tile.mainColor.y,UtilM.fluctuateSmooth(16, 0)*0.2+tile.mainColor.z,0.2);
		color.bind();
		OpenGLM.translate(tile.offset.x, tile.offset.y-UtilM.p*1.45F, 0.5F);
		tile.main.draw();
		OpenGLM.translate(tile.size.x, tile.size.y, 0);
		boolean selected=false;
		tile.selectedObj=null;
		Iterator<HoloObject> holos=tile.holoObjects.iterator();
		while(holos.hasNext()){
			HoloObject ro=holos.next();
			if(ro.host==null)ro.host=tile;
			OpenGLM.pushMatrix();
			OpenGLM.translate(ro.position.x, ro.position.y, 0);
			ro.render(color);
			OpenGLM.popMatrix();
			if((
				(ro.getClass()==TextBox.class&&tile.highlights[0])||
				(ro.getClass()==Button.class&&tile.highlights[1])||
				(ro.getClass()==Slider.class&&tile.highlights[3])||
				(ro.getClass()==Field.class&&tile.highlights[2])
				)&&!selected&&(ro.isHighlighted||ro.moveMode)){
				selected=true;
				OpenGLM.color(0,0,0,0.4F);
				OpenGLM.lineWidth(1);
				GL11U.texture(false);
				ro.drawHighlight();
				GL11U.texture(true);
			}
		}
		GL11U.glColor(ColorF.WHITE);
		GL11U.glCulFace(true);
		GL11U.texture(true);
		GL11U.endOpaqueRendering();
		OpenGLM.popMatrix();
		
//		tile.arraySize.updateValue(tile.holoObjects);
	}
	
}
