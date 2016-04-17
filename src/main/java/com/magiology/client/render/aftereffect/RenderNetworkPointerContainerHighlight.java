package com.magiology.client.render.aftereffect;

import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.CricleUtil;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class RenderNetworkPointerContainerHighlight extends LongAfterRenderRendererBase{
	private static final float p= 1F/16F;
	private static EntityPlayer player=U.getMC().thePlayer;
	public int highlightedBoxId;
	public PhysicsFloat progress;
	public String text;
	public TileEntityNetworkRouter tile;
	
	public RenderNetworkPointerContainerHighlight(TileEntityNetworkRouter tile){
		this.tile=tile;
		progress=new PhysicsFloat(0.01F, 0.06F);
		progress.friction=0.9F;
		progress.addWall("zero", 0, false);
		progress.addWall("one", 1, true);
		highlightedBoxId=tile.getPointedBoxID();
	}

	private Vec3M getOffset(){
		float x=0,y=0,z=0;
		int id=highlightedBoxId-7,idX=id/3,idY=id%3,side=tile.getOrientation(),multi=(side%2==0?-1:1);
		switch(side/2){
		case 0:{
			y=4*p*multi;
			x+=p*2;
			z+=p*2;
			x-=p*2*idX;
			z-=p*2*idY;
		}break;
		case 1:{
			z=4*p*multi;
			x+=p*2;
			y+=p*2;
			x-=p*2*idX;
			y-=p*2*idY;
		}break;
		case 2:{
			x=4*p*multi;
			z+=p*2;
			y+=p*2;
			z-=p*2*idX;
			y-=p*2*idY;
		}break;
		}
		return new Vec3M(x, y, z);
	}
	
	@Override
	public void render(){
		if(progress.getPoint()<0.01)return;
		//setup openGl
		OpenGLM.pushMatrix();
		GL11U.glLighting(false);
		//move to block
		OpenGLM.translate(tile.x()+0.5, tile.y()+0.5, tile.z()+0.5);
		//move to side
		
		Vec3M off=getOffset();
		
		GL11U.glTranslate(off);
		//set up openGl opaque
		GL11U.setUpOpaqueRendering(3);
		GL11U.glScale(0.5F);
		GL11U.glScale(-U.p/4);
		
		//calculation of rotation
		float
			point=progress.getPoint(),
			point2=MathUtil.snap(point, 0, 1),
			playerX=PartialTicksUtil.calculatePosX(player),
			playerY=PartialTicksUtil.calculatePosY(player)+player.getEyeHeight(),
			playerZ=PartialTicksUtil.calculatePosZ(player),
			txtX=tile.x()+0.5F+off.getX(),
			txtY=tile.y()+0.5F+off.getY(),
			txtZ=tile.z()+0.5F+off.getZ(),
			difX=playerX-txtX,
			difY=playerY-txtY,
			difZ=playerZ-txtZ,
			camPich=(float)Math.toDegrees(Math.atan2(difY,Math.sqrt(difX*difX+difZ*difZ))),
			camYaw=(float)Math.toDegrees(Math.atan2(difZ,difX))+90;
		int time360=(int)((tile.getWorld().getTotalWorldTime()+highlightedBoxId*20)%360);
		double time=PartialTicksUtil.calculatePos(time360, time360+1);
		
		//rotation
		GL11U.glRotate(0					 , -camYaw-80+80*point2,				   0);
		GL11U.glRotate(camPich+120-120*point2, 0				   , Math.sin(time/20)*2);
		GL11U.glScale(point2);
		//center string
		OpenGLM.translate(-TessUtil.getFontRenderer().getStringWidth(text)/2, -TessUtil.getFontRenderer().FONT_HEIGHT/2, 0);
		
		float r=0.8F,g=UtilM.fluctuateSmooth(20, 0)*0.15F+0.15F,b=0.1F;
		//draw in front
		TessUtil.getFontRenderer().drawString(text, 0, 0, new ColorF(r,g,b,(point-0.4)).toCode());
		//draw behind block
		OpenGLM.disableDepth();
		TessUtil.getFontRenderer().drawString(text, 0, 0, new ColorF(r,g,b,(point-0.4)*0.3).toCode());
		VertexRenderer buff=TessUtil.getVB();
		float expand=-1;
		buff.cleanUp();
		for(int i=0;i<5;i++){
			buff.addVertexWithUV(-expand, -expand, -0.001, 0, 0);
			buff.addVertexWithUV(-expand, expand+TessUtil.getFontRenderer().FONT_HEIGHT, -0.001, 0, 0);
			buff.addVertexWithUV(expand+TessUtil.getFontRenderer().getStringWidth(text), expand+TessUtil.getFontRenderer().FONT_HEIGHT, -0.001, 0, 0);
			buff.addVertexWithUV(expand+TessUtil.getFontRenderer().getStringWidth(text), -expand, -0.001, 0, 0);
			expand++;
		}
		OpenGLM.color(0, 0, 0, 0.06F);
		GL11U.texture(false);
		GL11U.blendFunc(1);
		buff.setClearing(false);
		buff.setDrawAsWire(true);
		buff.draw();
		buff.setDrawAsWire(false);
		OpenGLM.color(0, 0.4F+U.fluctuateSmooth(50, 0)*0.6F, 0.6F+U.fluctuateSmooth(97, 61)*0.4F, 0.01F);
		buff.draw();
		buff.setClearing(true);
		OpenGLM.enableDepth();
		buff.draw();
		GL11U.texture(true);
		//reset openGl
		GL11U.endOpaqueRendering();
		GL11U.glLighting(true);
		OpenGLM.popMatrix();
	}
	@Override
	public void update(){
		player=UtilM.getThePlayer();
		if(player==null)return;
		progress.update();
		if(tile.getPointedBoxID()==highlightedBoxId)progress.wantedPoint=1;
		else{
			progress.wantedPoint=0F;
			progress.point-=progress.speed/1.5;
		}
		
		if(progress.wantedPoint==0&&progress.point<1F/256F){
			kill();
			return;
		}
		if(tile==null){
			kill();
			return;
		}
		if(tile.getWorld().getTileEntity(tile.getPos())!=tile){
			kill();
			return;
		}
		
		if(tile.getStackInSlot(highlightedBoxId-7)!=null){
			NBTTagCompound nbt=tile.getStackInSlot(highlightedBoxId-7).getTagCompound();
			if(nbt==null)text="no bound position";
			else{
				int x=nbt.getInteger("xLink"),y=nbt.getInteger("yLink"),z=nbt.getInteger("zLink");
				text="target = ["+x+", "+y+", "+z+"]";
			}
		}else text="empty";
		
		Vec3M off=getOffset();
		float
			point=progress.getPoint(),
			playerX=(float)player.posX,
			playerZ=(float)player.posZ,
			txtX=tile.x()+0.5F+off.getX(),
			txtZ=tile.z()+0.5F+off.getZ(),
			difX=playerX-txtX,
			difZ=playerZ-txtZ,
			camYaw=(float)Math.toDegrees(Math.atan2(difZ,difX))+90,
			width=TessUtil.getFontRenderer().getStringWidth(text)*U.p/4;
		TessUtil.getFontRenderer();
		float leftX=CricleUtil.sin(-camYaw+90)*width/2, leftZ=CricleUtil.cos(-camYaw+90)*width/2, rand=RandUtil.CRF(1);
		
		
		float r=0.8F,g=UtilM.fluctuate(20, 0)*0.15F+0.15F,b=0.1F;
		UtilM.spawnEntityFX(new EntitySmoothBubleFX(player.worldObj,
				tile.x()+0.5+off.x+leftX*rand+RandUtil.CRF(0.1), tile.y()+0.5+off.y+RandUtil.CRF(0.1), tile.z()+0.5+off.z+leftZ*rand+RandUtil.CRF(0.1),
				RandUtil.CRF(0.01), RandUtil.CRF(0.01), RandUtil.CRF(0.01), 100, 25, -RandUtil.RF(0.1), r,g,b,0.05*point));
	}
}
