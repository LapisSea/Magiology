package com.magiology.client.gui.gui;

import java.awt.Rectangle;
import java.io.IOException;

import com.magiology.api.power.ISidedPower;
import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.container.ISidedPowerInstructorContainer;
import com.magiology.client.gui.guiutil.gui.buttons.ColoredGuiButton;
import com.magiology.client.gui.guiutil.gui.buttons.TexturedColoredButton;
import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.SimpleCounter;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class GuiISidedPowerInstructor extends GuiContainer implements Updateable{
	
	
	private CubeModel cube;
	int mouseStartX,mouseStartY;
	
	int mouseX,mouseY,buttonId=0;
	TileEntitySpecialRenderer renderer;
	
	private final TileEntity tile;
	
	PhysicsFloat xRotation,yRotation,zRotation;
	public GuiISidedPowerInstructor(EntityPlayer player,TileEntity tile){
		super(new ISidedPowerInstructorContainer(player, tile));
		this.tile=tile;
		this.xSize=176;
		this.ySize=166;
		renderer=TileEntityRendererDispatcher.instance.getSpecialRenderer(tile);
		
		xRotation=new PhysicsFloat(RandUtil.CRI(100)-player.rotationPitch,2);
		yRotation=new PhysicsFloat(RandUtil.CRI(100)-player.rotationYaw,2);
		zRotation=new PhysicsFloat(RandUtil.CRI(100),2);
		xRotation.friction=yRotation.friction=zRotation.friction=0.9F;
		xRotation.wantedPoint=-player.rotationPitch;
		yRotation.wantedPoint=-player.rotationYaw;
		zRotation.wantedPoint=0;
		
		AxisAlignedBB aa=U.getBlock(player.worldObj,tile.getPos()).getBoundingBox(tile.getWorld().getBlockState(tile.getPos()), tile.getWorld(), tile.getPos());
		cube=new CubeModel((float)aa.minX, (float)aa.minY, (float)aa.minZ, (float)aa.maxX, (float)aa.maxY, (float)aa.maxZ);
	}
	
	
	@Override
	protected void actionPerformed(GuiButton b){
		int id=b.id;
		if(id<6){
			buttonId=id;
		}
//		int side=convert(buttonId);
//		if(id==6){
//			UtilM.sendMessage(new GenericServerIntPacket(7, side));
//		}else if(id==7){
//			UtilM.sendMessage(new GenericServerIntPacket(8, side));
//		}else if(id==8){
//			UtilM.sendMessage(new GenericServerIntPacket(9, side));
//		}
		if(b instanceof ColoredGuiButton)((ColoredGuiButton)b).blink(1);
	}
	private int convert(int id){
		switch (id){
		case 4:return 5;
		case 3:return 4;
		case 5:return 3;
		case 0:return 1;
		case 1:return 0;
		case 2:return 2;
		}
		return -1;
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int x, int y){
		OpenGLM.disableLighting();
		TessUtil.bindTexture(Textures.ISidedIns);
		GL11U.setUpOpaqueRendering(2);
		OpenGLM.color(1, 1, 1, 0.9F);
		OpenGLM.translate(0, 0, 0);
		this.drawTexturedModalRect(guiLeft+14, guiTop+12, xSize, 0, 46, 46);
		GL11U.endOpaqueRendering();
		if(isShiftKeyDown()){
			OpenGLM.pushMatrix();
			OpenGLM.translate(guiLeft, guiTop+12, 0);
			OpenGLM.color(1, 0.6F, 0.6F, 1F);
			OpenGLM.lineWidth(1F);
			Renderer.LINES.begin();
			Renderer.LINES.addVertex(20,0,0);
			Renderer.LINES.addVertex(20,46,0);
			Renderer.LINES.addVertex(52,0,0);
			Renderer.LINES.addVertex(52,46,0);
			Renderer.LINES.draw();
			OpenGLM.popMatrix();
		}
	}
	@Override
	public void drawGuiContainerForegroundLayer(int x,int y){
		
	}
	
	@Override
	public void drawScreen(int x, int y, float partialTicks){
		float xRot=xRotation.getPoint()+180, 
			  yRot=yRotation.getPoint(),
			  zRot=zRotation.getPoint();
		
		OpenGLM.pushMatrix();
		OpenGLM.disableLighting();
		super.drawScreen(x, y, partialTicks);
		RenderHelper.enableGUIStandardItemLighting();
		
		
		try{
			if(tile==null)return;
			mouseX=x;
			mouseY=y;
			
			
			//TODO: BlockRendererDispatcher#renderBlock may be able to do what you want. Look at RenderChunk#rebuildChunk to see how it's used.
//			Helper.getRenderBlocks().blockAccess=tile.getWorld();
			
			
			
			OpenGLM.pushMatrix();
			OpenGLM.translate(guiLeft+37, guiTop+35, 120);
			GL11U.glScale(35);
			GL11U.glRotate(xRot,yRot,zRot);
			OpenGLM.scale((-1), 1, 1);
			if(renderer!=null){
				OpenGLM.cullFace(CullFace.FRONT);
				renderer.renderTileEntityAt(tile, -0.5, -0.5, -0.5, PartialTicksUtil.partialTicks,0);
				OpenGLM.cullFace(CullFace.BACK);
				renderer.renderTileEntityAt(tile, -0.5, -0.5, -0.5, PartialTicksUtil.partialTicks,0);
			}
			Block block=U.getBlock(tile.getWorld(), tile.getPos());
			if(block!=null){
				OpenGLM.pushMatrix();
				try{
					TessUtil.bindTexture(TextureMap.locationBlocksTexture);
//					Renderer.beginQuads();
					//TODO: BlockRendererDispatcher#renderBlock may be able to do what you want. Look at RenderChunk#rebuildChunk to see how it's used.
//					Helper.getRenderBlocks().renderBlockByRenderType(block, tile.getPos());
//					OpenGLM.translate(-tile.getPos().getX()-0.5, -tile.getPos().getY()-0.5, -tile.getPos().getZ()-0.5);
//					Renderer.LINES.draw();
				}catch(Exception e){e.printStackTrace();}
				OpenGLM.popMatrix();
			}
			int id=-1;
			for(int a=0;a<cube.willSideRender.length;a++){
				GuiButton b=buttonList.get(a);
				if(x>=b.xPosition &&y>=b.yPosition&&x<b.xPosition+b.width&&y<b.yPosition+b.height){
					id=a;
					continue;
				}
			}
			if(id!=-1){
				switch (id){
				case 0:id=3;break;
				case 1:id=2;break;
				case 2:id=4;break;
				case 3:id=5;break;
				case 4:id=1;break;
				case 5:id=0;break;
				}
				for(int a=0;a<cube.willSideRender.length;a++)cube.willSideRender[a]=a==id;
				
				
				
				OpenGLM.pushMatrix();
				OpenGLM.disableCull();
				OpenGLM.disableTexture2D();
				GL11U.setUpOpaqueRendering(1);
				GL11U.glScale(1.001);
				OpenGLM.translate(-0.5, -0.5, -0.5);
				double 
				r=UtilC.fluctuate(9, 0)*0.4,
				g=0.5-UtilC.fluctuate(17, 0)*0.3,
				b=1-UtilC.fluctuate(27, 0)*0.2;
				OpenGLM.color(r,g,b, 0.4);
				OpenGLM.disableLighting();
				cube.draw();
				OpenGLM.disableDepth();
				OpenGLM.color(r,g,b, 0.2);
				cube.draw();
				OpenGLM.enableDepth();
				GL11U.endOpaqueRendering();
				OpenGLM.enableCull();
				OpenGLM.enableTexture2D();
				OpenGLM.popMatrix();
			}
			OpenGLM.popMatrix();
		}catch(Exception e){e.printStackTrace();}
		OpenGLM.disableLighting();
		
		TessUtil.bindTexture(Textures.ISidedIns);
		OpenGLM.color(1, 1, 1, 1);
		OpenGLM.disableDepth();
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(guiLeft+82, guiTop+60, 120);
		GL11U.setUpOpaqueRendering(1);
		
		OpenGLM.pushMatrix();
		OpenGLM.enableLighting();
		
		OpenGLM.color(0.65F+UtilC.fluctuate(41, 0)*0.1,0.65+UtilC.fluctuate(25, 0)*0.05,0.65+UtilC.fluctuate(73, 0)*0.15,1);
		GL11U.glScale(12.9);
		GL11U.glRotate(UtilC.fluctuate(164, 0)*180+xRot/4, UtilC.fluctuate(84, 0)*60+yRot/4, UtilC.fluctuate(508, 0)*360+zRot/4);

		OpenGLM.disableLighting();
		
		OpenGLM.popMatrix();
		
		
		OpenGLM.pushMatrix();
		GL11U.glRotate(xRotation.wantedPoint+180, yRotation.wantedPoint, zRotation.wantedPoint);
		OpenGLM.color(1, 0, 0, 1F);
		OpenGLM.lineWidth(2.5F);
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(0,0,0);
		Renderer.LINES.addVertex(13,0,0);
		Renderer.LINES.draw();
		OpenGLM.color(1, 0, 0, 0.3F);
		OpenGLM.lineWidth(5F);
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(0,0,0);
		Renderer.LINES.addVertex(13,0,0);
		Renderer.LINES.draw();
		OpenGLM.popMatrix();
		
		
		OpenGLM.pushMatrix();
		GL11U.glRotate(xRot,yRot,zRot);
		OpenGLM.color(0, 1, 0, 1F);
		OpenGLM.lineWidth(2.5F);
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(0,0,0);
		Renderer.LINES.addVertex(13,0,0);
		Renderer.LINES.draw();
		OpenGLM.color(0, 1, 0, 0.3F);
		OpenGLM.lineWidth(5F);
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(0,0,0);
		Renderer.LINES.addVertex(13,0,0);
		Renderer.LINES.draw();
		OpenGLM.popMatrix();
		
		
		GL11U.endOpaqueRendering();
		OpenGLM.popMatrix();

		OpenGLM.disableLighting();
		
		for(int k=0;k<buttonList.size();++k){this.buttonList.get(k).drawButton(mc, x, y);}

		OpenGLM.popMatrix();
	}
	@Override
	public void initGui(){
		super.initGui();
		SimpleCounter sc=new SimpleCounter();
		for(int a=0;a<6;a++){
			buttonList.add(new TexturedColoredButton(a,100+guiLeft,10+guiTop+a*22, 40, 20, EnumFacing.getFront(a).toString()));
			sc.add();
		}
		buttonList.add(new TexturedColoredButton(sc.getAndAdd(),15+guiLeft, 80+guiTop, 72, 20, "Cricle"));
		buttonList.add(new TexturedColoredButton(sc.getAndAdd(),10+guiLeft,102+guiTop, 40, 20, "Output"));
		buttonList.add(new TexturedColoredButton(sc.getAndAdd(),52+guiLeft,102+guiTop, 40, 20, "Input"));
		actionPerformed(buttonList.get(0));
	}
	@Override
	protected void mouseClicked(int x, int y, int buttonClicked)throws IOException{
		super.mouseClicked(x, y, buttonClicked);
		mouseStartX=x;
		mouseStartY=y;
	}
	@Override
	protected void mouseClickMove(int x, int y, int lastButtonClicked, long totalMoved){
		super.mouseClickMove(x, y, lastButtonClicked, totalMoved);
		if(new Rectangle(guiLeft+14,guiTop+12,46,46).contains(x, y)){
			yRotation.wantedPoint-=(x-mouseStartX)*2;
			if(x<guiLeft+20)zRotation.wantedPoint-=(y-mouseStartY)*2;
			else if(x>guiLeft+52)zRotation.wantedPoint-=(y-mouseStartY)*2;
			else xRotation.wantedPoint-=(y-mouseStartY)*2;
		}
	}
	@Override
	public void update(){
		
		double xDifference=xRotation.difference()+Math.abs(xRotation.speed/4),
			   yDifference=yRotation.difference()+Math.abs(yRotation.speed/4),
			   zDifference=zRotation.difference()+Math.abs(zRotation.speed/4);
		
		xRotation.friction=yRotation.friction=zRotation.friction=0.9F;
		
		if(xDifference<5)xRotation.friction=0.9F;
		if(xDifference<3)xRotation.friction=0.8F;
		if(xDifference<2)xRotation.friction=0.7F;
		if(xDifference<1)xRotation.friction=0.1F;

		if(yDifference<5)yRotation.friction=0.9F;
		if(yDifference<3)yRotation.friction=0.8F;
		if(yDifference<2)yRotation.friction=0.7F;
		if(yDifference<1)yRotation.friction=0.1F;
		
		if(zDifference<5)zRotation.friction=0.9F;
		if(zDifference<3)zRotation.friction=0.8F;
		if(zDifference<2)zRotation.friction=0.7F;
		if(zDifference<1)zRotation.friction=0.1F;
		
		xRotation.update();
		yRotation.update();
		zRotation.update();
//		((ColoredGuiButton)buttonList.get(7)).update();
//		((ColoredGuiButton)buttonList.get(8)).update();
		for(Object b:buttonList){
			if(b instanceof TexturedColoredButton)((TexturedColoredButton)b).update();
//			if(b instanceof ColoredGuiButton)((ColoredGuiButton)b).update();
		}
		int side=convert(buttonId);
		boolean 
		allowedReceive=((ISidedPower)tile).getAllowedReceaver(side),
		allowedSend   =((ISidedPower)tile).getAllowedSender  (side),
		receive=((ISidedPower)tile).getIn(side),
		send   =((ISidedPower)tile).getOut   (side);
		
		buttonList.get(7).enabled=allowedSend;
		buttonList.get(8).enabled=allowedReceive;
		((ColoredGuiButton)buttonList.get(7)).wantedR=send?0.1F:1;
		((ColoredGuiButton)buttonList.get(7)).wantedG=send?1:0.1F;
		((ColoredGuiButton)buttonList.get(7)).wantedB=0.1F;
		((ColoredGuiButton)buttonList.get(8)).wantedR=receive?0.1F:1;
		((ColoredGuiButton)buttonList.get(8)).wantedG=receive?1:0.1F;
		((ColoredGuiButton)buttonList.get(8)).wantedB=0.1F;
	}
}
