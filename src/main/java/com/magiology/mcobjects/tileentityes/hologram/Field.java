package com.magiology.mcobjects.tileentityes.hologram;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;

import net.minecraft.entity.player.EntityPlayer;

public class Field extends HoloObject{
	
	public CubeModel body;
	
	public Field(){}
	public Field(TileEntityHologramProjector tile, Vector2f siz){
		super(tile);
		originalSize=siz;
		body=new CubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
	}

	@Override
	public boolean isFullBlown(){
		return false;
	}

	@Override
	public void onPressed(EntityPlayer player){
	}

	@Override
	public void render(ColorF color){
		checkHighlight();
		if(body==null)body=new CubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
		PartialTicksUtil.calculate(prevColor,this.color).bind();
		GL11U.texture(false);
		body.draw();
		GL11U.texture(true);
	}
	@Override
	public void sendCommand(){}
	@Override
	public void update(){
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		if(host.getWorld().getTotalWorldTime()%40==0)body=new CubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
		prevColor=color;
		color=UtilM.graduallyEqualize(color, setColor, 0.1F);
	}
}
