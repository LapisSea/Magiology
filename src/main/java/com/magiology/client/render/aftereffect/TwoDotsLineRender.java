package com.magiology.client.render.aftereffect;

import com.magiology.core.MReference;
import com.magiology.core.init.MItems;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.vectors.TwoDots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TwoDotsLineRender extends LongAfterRenderRendererBase{

	private static EntityPlayer player=UtilC.getMC().thePlayer;
	public  double alpha=0,prevAlpha;
	TwoDots td;
	public TileEntity tile;
	
	public TwoDotsLineRender(TwoDots td,TileEntity tile){
		this.td=td;
		this.tile=tile;
	}
	@Override
	public void render(){
		boolean upgraded=true;//UtilM.isItemInStack(MItems.pants_42I, player.getCurrentArmor(1));
		OpenGLM.enableTexture2D();
		GL11U.setUpOpaqueRendering(1);
		TessUtil.getVB().cleanUp();
		
		int tim=(int)((UtilC.getTheWorld().getTotalWorldTime())%20);
		float st=PartialTicksUtil.calculate(tim, tim+1)/10F;
		for(int a=0;a<(upgraded?6:2);a++){
			float width=1;
			{
				switch (a){
				case 0:width=1;break;
				case 1:width=2;break;
				case 2:width=3;break;
				case 3:width=4;break;
				case 4:width=5;break;
				case 5:{
					width=5;
					if(upgraded)OpenGLM.disableDepth();
				}break;
				}
				TessUtil.drawLine(td.x1, td.y1, td.z1, td.x2, td.y2, td.z2, width/20, true,TessUtil.getVB(),st,1);
				OpenGLM.color(0.7+RandUtil.RF()*0.2, RandUtil.RF()*0.1, RandUtil.RF()*0.1, (upgraded?0.14:0.09)*PartialTicksUtil.calculate(prevAlpha,alpha));
				OpenGLM.depthMask(false);
				OpenGLM.disableCull();
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"textures/models/visual_connection.png"));
				TessUtil.getVB().draw();
				OpenGLM.enableCull();
				OpenGLM.depthMask(true);
			}
		}
		GL11U.endOpaqueRendering();
		OpenGLM.enableDepth();
	}
	@Override
	public void update(){
		player=UtilC.getThePlayer();
		if(player==null)return;
		
		prevAlpha=alpha;
		
		alpha+=0.4*(UtilM.isItemInStack(MItems.fireHammer, player.getHeldItemMainhand())?1:-1);
		
		alpha=MathUtil.snap(alpha, 0, 1);
		if(alpha<0.05)kill();
	}
}
