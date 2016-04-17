package com.magiology.client.render.tilerender;

import java.util.List;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.CollisionBox;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProviderHandler;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;

public class RenderMultiBox extends TileEntitySpecialRendererM{

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt){
		GL11U.protect();
		OpenGLM.translate(x,y,z);
		new ColorF(0.6,0.6,0.6,0.5).bind();
		GL11U.texture(false);
		List<CollisionBox> cubes=MultiColisionProviderHandler.getBoxes((MultiColisionProvider)tile);
		if(tile instanceof ISidedNetworkComponent&&((ISidedNetworkComponent)tile).getBrain()==null)GL11U.setUpOpaqueRendering(1);
		else GL11U.endOpaqueRendering();
		for(CollisionBox a:cubes){
			TessUtil.drawCube(a.box);
		}
		GL11U.endOpaqueRendering();
		GL11U.texture(true);
		GL11U.endProtection();
	}

}
