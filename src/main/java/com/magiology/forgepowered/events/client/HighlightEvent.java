package com.magiology.forgepowered.events.client;

import java.util.List;

import com.magiology.api.network.NetworkInterface;
import com.magiology.api.power.PowerCore;
import com.magiology.client.render.aftereffect.LongAfterRenderRenderer;
import com.magiology.client.render.aftereffect.RenderNetworkPointerContainerHighlight;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.items.NetworkPointer;
import com.magiology.mcobjects.tileentityes.TileEntityBFCPowerOut;
import com.magiology.mcobjects.tileentityes.TileEntityBateryGeneric;
import com.magiology.mcobjects.tileentityes.TileEntityBigFurnaceCore;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProviderHandler;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.CollectionConverter;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/**
 * ONLY CLIENT SIDE
 * @author LapisSea
 */
public class HighlightEvent {
	
	float p=1F/16F;
	private void doMultiColision(BlockPos pos, ItemStack item, DrawBlockHighlightEvent event){
		MultiColisionProvider colisionProvider=(MultiColisionProvider)event.player.worldObj.getTileEntity(pos);
		double ex=0.002;
		int DFPBBwidth=2;
		double DFPBBalpha=0.6;
		
		Vec3M off=PartialTicksUtil.calculatePos(event.player).mul(-1).add(new Vec3M(pos));
		OpenGLM.pushMatrix();
		GL11U.glTranslate(off);
		
		AxisAlignedBBM 
			mainBox=colisionProvider.getMainBox().box,
			pointedBox=colisionProvider.getBoxes().get(colisionProvider.getPointedBoxID()).box;
		
		long wtt=event.player.worldObj.getTotalWorldTime();
		double centerAlphaHelper=(wtt%80.0)/40.0,centerAlpha=centerAlphaHelper>1?2-centerAlphaHelper:centerAlphaHelper;
		OpenGLM.disableLighting();
		OpenGLM.depthMask(false);

		GL11U.setUpOpaqueRendering(1);
		OpenGLM.disableTexture2D();
//		List<CollisionBox> boxes=MultiColisionProviderHandler.getBoxes(colisionProvider);
//		PrintUtil.println(boxes);
//		ColorF.BLUE.bind();
//		for(CollisionBox collisionBox:boxes){
//			TessUtil.drawCube(collisionBox.box);
//		}
//		ColorF.ORANGE.bind();
//		TessUtil.drawCube(pointedBox.expand(0.002));
//		ColorF.WHITE.bind();
		event.setCanceled(true);
		
		double selectionAlphaHelper=(wtt%120.0)/60.0,selectionAlpha=selectionAlphaHelper>1?2-selectionAlphaHelper:selectionAlphaHelper;
		if(UtilM.isItemInStack(MItems.fireHammer, UtilM.getThePlayer().getHeldItemMainhand()))drawBox(mainBox.minX-ex,mainBox.maxX+ex,mainBox.minY-ex,mainBox.maxY+ex,mainBox.minZ-ex,mainBox.maxZ+ex,0.9, 0.1, 0.2, 0.1+0.05*centerAlpha);
		
		
		if(UtilM.isItemInStack(MItems.fireHammer,item)){
			drawBox(pointedBox.minX-ex*2,pointedBox.maxX+ex*2,pointedBox.minY-ex*2,pointedBox.maxY+ex*2,pointedBox.minZ-ex*2,pointedBox.maxZ+ex*2, 0.1, 0.1, 0.9, 0.1+0.2*selectionAlpha);
			GL11U.setUpOpaqueRendering(1);
			drawSelectionBox(pointedBox.minX-ex*2,pointedBox.maxX+ex*2,pointedBox.minY-ex*2,pointedBox.maxY+ex*2,pointedBox.minZ-ex*2,pointedBox.maxZ+ex*2,UtilM.fluctuateSmooth(11, 21)/4, UtilM.fluctuateSmooth(16, 45)/4, 0.7+UtilM.fluctuateSmooth(36, 74)*0.3, 2.5, 0.5);
		}
		else GL11U.setUpOpaqueRendering(1);
		
//		I am adding this to make the code much more easy to understand and to make it shorter! :)
		List<AxisAlignedBBM>
		up=   CollectionConverter.convLi(MultiColisionProviderHandler.getBoxesOnSide(colisionProvider,1),AxisAlignedBBM.class,box->box.box),
		down= CollectionConverter.convLi(MultiColisionProviderHandler.getBoxesOnSide(colisionProvider,0),AxisAlignedBBM.class,box->box.box),
		east= CollectionConverter.convLi(MultiColisionProviderHandler.getBoxesOnSide(colisionProvider,5),AxisAlignedBBM.class,box->box.box),
		west= CollectionConverter.convLi(MultiColisionProviderHandler.getBoxesOnSide(colisionProvider,4),AxisAlignedBBM.class,box->box.box),
		north=CollectionConverter.convLi(MultiColisionProviderHandler.getBoxesOnSide(colisionProvider,2),AxisAlignedBBM.class,box->box.box),
		south=CollectionConverter.convLi(MultiColisionProviderHandler.getBoxesOnSide(colisionProvider,3),AxisAlignedBBM.class,box->box.box);
		
		boolean upCon=!up.isEmpty(),downCon=!down.isEmpty(),
				eastCon=!east.isEmpty(),westCon=!west.isEmpty(),
				northCon=!north.isEmpty(),southCon=!south.isEmpty(),
//				This are booleans that decide if a line will render so all of them merge into a dynamic multi box structure
				centerToWestUp=(westCon==upCon),centerToEastUp=(eastCon==upCon),centerToEastSouth=(eastCon==southCon),
				centerToWestNorth=(northCon==westCon),centerToSouthWest=(westCon==southCon),centerToEastNorth=(eastCon==northCon),
				centerToWestDown=(westCon==downCon),centerToNorthDown=(northCon==downCon),centerToNorthUp=(northCon==upCon),
				centerToEastDown=(eastCon==downCon),centerToSouthDown=(southCon==downCon),centerToSouthUp=(southCon==upCon);
//		This calls drawSelectionBox or drawRawSelectionBox with booleans to decide what to render
		boolean westConOk=false,downConOk=false,northConOk=false,eastConOk=false,upConOk=false,southConOk=false;
		
		if(westCon){
			boolean[] bs={true,false,false,true,true,true,true,true,false,false,true,true};
			for(AxisAlignedBBM box:west)if(box!=null){
				AxisAlignedBBM box1=box.expand(0, ex, ex).offset(-ex, 0, 0);
				if(box.minZ==mainBox.minZ&&box.minY==mainBox.minY&&box.maxZ==mainBox.maxZ&&box.maxY==mainBox.maxY&&box.maxX==mainBox.minX){
					drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
					westConOk=true;
				}
				else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
				
			}
		}
		if(downCon){
			boolean[] bs={true,true,true,true,true,false,true,false,true,false,true,false};
			for(AxisAlignedBBM box:down)if(box!=null){
				AxisAlignedBBM box1=box.expand(ex, 0, ex).offset(0, -ex, 0);
				if(box.minX==mainBox.minX&&box.minZ==mainBox.minZ&&box.maxX==mainBox.maxX&&box.maxZ==mainBox.maxZ&&box.maxY==mainBox.minY){
					drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
					downConOk=true;
				}
				else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
				
			}
		}
		if(northCon){
			boolean[] bs={true,true,false,false,true,true,true,true,true,true,false,false};
			for(AxisAlignedBBM box:north)if(box!=null){
				AxisAlignedBBM box1=box.expand(ex, ex, 0).offset(0, 0, -ex);
				if(box.minX==mainBox.minX&&box.minY==mainBox.minY&&box.maxX==mainBox.maxX&&box.maxY==mainBox.maxY&&box.maxZ==mainBox.minZ){
					drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
					northConOk=true;
				}
				else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
				
			}
		}
		if(eastCon){
			boolean[] bs={false,true,true,false,false,false,true,true,true,true,true,true};
			for(AxisAlignedBBM box:east)if(box!=null){
				AxisAlignedBBM box1=box.expand(0, ex, ex).offset(ex, 0, 0);
				if(box.minZ==mainBox.minZ&&box.minY==mainBox.minY&&box.maxZ==mainBox.maxZ&&box.maxY==mainBox.maxY&&box.minX==mainBox.maxX){
					drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
					eastConOk=true;
				}
				else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
				
			}
		}
		if(upCon){
			boolean[] bs={true,true,true,true,false,true,false,true,false,true,false,true};
			for(AxisAlignedBBM box:up)if(box!=null){
				AxisAlignedBBM box1=box.expand(ex, 0, ex).offset(0, ex, 0);
				if(box.minX==mainBox.minX&&box.minZ==mainBox.minZ&&box.maxX==mainBox.maxX&&box.maxZ==mainBox.maxZ&&box.minY==mainBox.maxY){
					drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
					upConOk=true;
				}
				else drawSelectionBox(box.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
				
			}
		}
		if(southCon){
			boolean[] bs={false,false,true,true,true,true,false,false,true,true,true,true};
			for(AxisAlignedBBM box:south)if(box!=null){
				AxisAlignedBBM box1=box.expand(ex, ex, 0).offset(0, 0, ex);
				if(box.minX==mainBox.minX&&box.minY==mainBox.minY&&box.maxX==mainBox.maxX&&box.maxY==mainBox.maxY&&box.minZ==mainBox.maxZ){
					drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
					southConOk=true;
				}
				else drawSelectionBox(box1, 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha);
				
			}
		}
		if(mainBox!=null){
			if(!northConOk&&northCon)centerToEastNorth=centerToNorthDown=centerToNorthUp=centerToWestNorth=true;
			if(!southConOk&&southCon)centerToEastSouth=centerToSouthDown=centerToSouthUp=centerToSouthWest=true;
			if(!downConOk && downCon)centerToEastDown=centerToNorthDown=centerToSouthDown=centerToWestDown=true;
			if(!westConOk && westCon)centerToSouthWest=centerToWestDown=centerToWestNorth=centerToWestUp=true;
			if(!eastConOk && eastCon)centerToEastDown=centerToEastNorth=centerToEastSouth=centerToEastUp=true;
			if(!upConOk   &&   upCon)centerToEastUp=centerToNorthUp=centerToSouthUp=centerToWestUp=true;
			
			boolean[] bs={centerToWestNorth,centerToEastNorth,centerToEastSouth,centerToSouthWest,centerToWestDown,centerToWestUp,centerToNorthDown,centerToNorthUp,centerToEastDown,centerToEastUp,centerToSouthDown,centerToSouthUp};
			drawSelectionBox(mainBox.expand(ex, ex, ex), 0.1, 0.1, 0.1, DFPBBwidth,DFPBBalpha,bs);
		}
		OpenGLM.popMatrix();
	}
	
	private void doNetRouterHighlight(BlockPos pos, TileEntityNetworkRouter tile, DrawBlockHighlightEvent event){
		if(tile==null)return;
		NetworkInterface boundedInterface=tile.getBoundedInterface();
		if(boundedInterface==null)return;
		
		OpenGLM.pushMatrix();
		GL11U.glTranslate(UtilM.getEntityPos(event.player).mul(-1));
		World world=event.player.worldObj;
		GL11U.texture(false);
		GL11U.setUpOpaqueRendering(1);
		Block sb1=tile.getBlockType();
		sb1.setBlockBoundsBasedOnState(world, tile.getPos());
		AxisAlignedBB source1=sb1.getSelectedBoundingBox(world, tile.getPos());
		for(TileEntityNetworkRouter i:tile.getBoundedInterface().getPointerContainers()){
			Block sb=i.getBlockType();
			sb.setBlockBoundsBasedOnState(world, i.getPos());
			AxisAlignedBB source=sb.getSelectedBoundingBox(world, i.getPos());
			
			if(i!=tile){
				GL11U.glDepth(false);
				OpenGLM.color(1, 0, 0, 0.5F);
				OpenGLM.lineWidth(3);
				GL11U.texture(false);
				Renderer.LINES.begin();
				Renderer.LINES.addVertex((source1.minX+source1.maxX)/2, (source1.minY+source1.maxY)/2, (source1.minZ+source1.maxZ)/2);
				Renderer.LINES.addVertex((source.minX+source.maxX)/2, (source.minY+source.maxY)/2, (source.minZ+source.maxZ)/2);
				Renderer.LINES.draw();
			}
			
			for(ItemStack j:i.slots){
				if(j!=null&&j.hasTagCompound()){
					if(event.player.isSneaking())for(int k=0;k<2;k++){
						if(k==0){
							GL11U.glDepth(false);
							drawHighlightToBlock(i.getPos(), NetworkPointer.getTarget(j), new ColorF(0,0,0,0.6), 1);
							GL11U.glDepth(true);
						}
						else drawHighlightToBlock(i.getPos(), NetworkPointer.getTarget(j), new ColorF(0,1,1,0.2), 2.5);
					}else drawHighlightToBlock(i.getPos(), NetworkPointer.getTarget(j), new ColorF(0,0,0,0.6), 1);
				}
			}
		}
		GL11U.endOpaqueRendering();
		GL11U.texture(true);
		GL11U.glDepth(true);
		OpenGLM.popMatrix();
	}
	private void doPowerCounterDisplay(BlockPos pos, ItemStack item, DrawBlockHighlightEvent event){
		double powerBar=0;
		int currentEn=0,maxEn=0;
		boolean okBlock=true;
		NBTTagCompound PC=item.getTagCompound();
		TileEntity tile=event.player.worldObj.getTileEntity(pos);
		PC.setString("block", UtilM.getBlock(event.player.worldObj,pos).getLocalizedName());
		
		if(tile instanceof PowerCore){
			powerBar=(float)((PowerCore)tile).getEnergy()/(float)((PowerCore)tile).getMaxEnergy();
			currentEn=((PowerCore)tile).getEnergy();
			maxEn=((PowerCore)tile).getMaxEnergy();
		}else if(tile instanceof TileEntityBigFurnaceCore){
			powerBar=(float)((TileEntityBigFurnaceCore)tile).getEnergy()/(float)((TileEntityBigFurnaceCore)tile).getMaxEnergy();
			
			currentEn=((TileEntityBigFurnaceCore)tile).getEnergy();
			maxEn=((TileEntityBigFurnaceCore)tile).getMaxEnergy();
		}else if(tile instanceof TileEntityBFCPowerOut){
			int x1=0;int y1=1000;int z1=0;
				
				 if(((TileEntityBFCPowerOut)tile).CallDir[0]!=null){x1=pos.getX()-2;y1=pos.getY()-1;z1=pos.getZ();}
			else if(((TileEntityBFCPowerOut)tile).CallDir[1]!=null){x1=pos.getX()+2;y1=pos.getY()-1;z1=pos.getZ();}
			else if(((TileEntityBFCPowerOut)tile).CallDir[2]!=null){x1=pos.getX();y1=pos.getY()-1;z1=pos.getZ()-2;}
			else if(((TileEntityBFCPowerOut)tile).CallDir[3]!=null){x1=pos.getX();y1=pos.getY()-1;z1=pos.getZ()+2;}
			
			if(y1!=1000){
				pos=new BlockPos(x1, y1, z1);
				tile= event.player.worldObj.getTileEntity(pos);
				powerBar=(float)((TileEntityBigFurnaceCore)tile).getEnergy()/(float)((TileEntityBigFurnaceCore)tile).getMaxEnergy();
				currentEn=((TileEntityBigFurnaceCore)tile).getEnergy();
				maxEn=((TileEntityBigFurnaceCore)tile).getMaxEnergy();
			}
		}else if(tile instanceof TileEntityBateryGeneric){
			powerBar=(float)((TileEntityBateryGeneric)tile).getEnergy()/(float)((TileEntityBateryGeneric)tile).getMaxEnergy();
			currentEn=((TileEntityBateryGeneric)tile).getEnergy();
			maxEn=((TileEntityBateryGeneric)tile).getMaxEnergy();
		}else if(tile instanceof TileEntityFireLamp){
			powerBar=(float)((TileEntityFireLamp)tile).getEnergy()/(float)((TileEntityFireLamp)tile).getMaxEnergy();
			currentEn=((TileEntityFireLamp)tile).getEnergy();
			maxEn=((TileEntityFireLamp)tile).getMaxEnergy();
		}else okBlock=false;
		
		if(okBlock==false){currentEn=0;maxEn=0;}
		if(powerBar>0)powerBar+=-0.01;
		
		PC.setDouble("powerBar", powerBar);
		PC.setInteger("currentEn", currentEn);
		PC.setInteger("maxEn", maxEn);
	}
	public void drawBox(double minX, double maxX, double minY, double maxY, double minZ, double maxZ,double rColor,double gColor,double bColor,double alpha,boolean[]... bs){
		if(bs.length!=1||bs[0].length!=6){
			bs=new boolean[1][1];
			bs[0]=new boolean[]{true,true,true,true,true,true};
		}
		float[] xpoints=new float[8], ypoints=new float[8], zpoints=new float[8];
		xpoints[0]=(float)minX;xpoints[1]=(float)minX;
		ypoints[0]=(float)minY;ypoints[1]=(float)maxY;
		zpoints[0]=(float)minZ;zpoints[1]=(float)minZ;
		
		xpoints[2]=(float)maxX;xpoints[3]=(float)maxX;
		ypoints[2]=(float)minY;ypoints[3]=(float)maxY;
		zpoints[2]=(float)minZ;zpoints[3]=(float)minZ;
		
		xpoints[4]=(float)maxX;xpoints[5]=(float)maxX;
		ypoints[4]=(float)minY;ypoints[5]=(float)maxY;
		zpoints[4]=(float)maxZ;zpoints[5]=(float)maxZ;
		
		xpoints[6]=(float)minX;xpoints[7]=(float)minX;
		ypoints[6]=(float)minY;ypoints[7]=(float)maxY;
		zpoints[6]=(float)maxZ;zpoints[7]=(float)maxZ;
		
		drawRawBox(xpoints, ypoints, zpoints, rColor, gColor, bColor,alpha,bs[0]);
	}

	private void drawHighlightToBlock(BlockPos posStart,BlockPos posTarget,ColorF color, double lineWidth){
		BlockPos targetPos=posTarget;
		AxisAlignedBB bounds=UtilM.getBlock(UtilM.getTheWorld(),targetPos).getSelectedBoundingBox(UtilM.getTheWorld(), targetPos).expand(0.003, 0.003, 0.003);
		AxisAlignedBB bounds2=UtilM.getBlock(UtilM.getTheWorld(),posStart).getSelectedBoundingBox(UtilM.getTheWorld(), posStart).expand(0.003, 0.003, 0.003);
		drawSelectionBox(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY, bounds.minZ, bounds.maxZ,color.r,color.g,color.b,lineWidth,color.a);
		GL11U.texture(false);
		Renderer.LINES.begin();
		Renderer.LINES.addVertex((bounds.minX+bounds.maxX)/2, (bounds.minY+bounds.maxY)/2, (bounds.minZ+bounds.maxZ)/2);
		Renderer.LINES.addVertex((bounds2.minX+bounds2.maxX)/2, (bounds2.minY+bounds2.maxY)/2, (bounds2.minZ+bounds2.maxZ)/2);
		Renderer.LINES.draw();
		GL11U.texture(true);
	}
	
	public void drawRawBox(float[] xpoints,float[] ypoints,float[] zpoints,double rColor,double gColor,double bColor,double alpha,boolean[] bs){
		if(bs.length!=6)return;
		OpenGLM.disableTexture2D();
		OpenGLM.depthMask(false);
		OpenGLM.disableCull();
		
		OpenGLM.color(rColor,gColor,bColor,alpha);
		Renderer.POS.beginQuads();
		
		int a=0;
		if(bs[a]){
			Renderer.POS.addVertex(xpoints[0], ypoints[0], zpoints[0]);
			Renderer.POS.addVertex(xpoints[1], ypoints[1], zpoints[1]);
			Renderer.POS.addVertex(xpoints[3], ypoints[3], zpoints[3]);
			Renderer.POS.addVertex(xpoints[2], ypoints[2], zpoints[2]);
		}a++;
		if(bs[a]){
			Renderer.POS.addVertex(xpoints[2], ypoints[2], zpoints[2]);
			Renderer.POS.addVertex(xpoints[3], ypoints[3], zpoints[3]);
			Renderer.POS.addVertex(xpoints[5], ypoints[5], zpoints[5]);
			Renderer.POS.addVertex(xpoints[4], ypoints[4], zpoints[4]);
		}a++;
		if(bs[a]){
			Renderer.POS.addVertex(xpoints[4], ypoints[4], zpoints[4]);
			Renderer.POS.addVertex(xpoints[5], ypoints[5], zpoints[5]);
			Renderer.POS.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			Renderer.POS.addVertex(xpoints[6], ypoints[6], zpoints[6]);
		}a++;
		if(bs[a]){
			Renderer.POS.addVertex(xpoints[6], ypoints[6], zpoints[6]);
			Renderer.POS.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			Renderer.POS.addVertex(xpoints[6], ypoints[7], zpoints[0]);
			Renderer.POS.addVertex(xpoints[0], ypoints[0], zpoints[0]);
		}a++;
		if(bs[a]){
			Renderer.POS.addVertex(xpoints[1], ypoints[7], zpoints[1]);
			Renderer.POS.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			Renderer.POS.addVertex(xpoints[2], ypoints[7], zpoints[7]);
			Renderer.POS.addVertex(xpoints[2], ypoints[7], zpoints[2]);
		}a++;
		if(bs[a]){
			Renderer.POS.addVertex(xpoints[1], ypoints[6], zpoints[1]);
			Renderer.POS.addVertex(xpoints[7], ypoints[6], zpoints[7]);
			Renderer.POS.addVertex(xpoints[2], ypoints[6], zpoints[7]);
			Renderer.POS.addVertex(xpoints[2], ypoints[6], zpoints[2]);
		}
		Renderer.POS.draw();
		OpenGLM.enableCull();
		OpenGLM.depthMask(true);
		OpenGLM.enableDepth();
	}
	
	/**
	 * draws a box with coordinates for every point
	 * */
	public void drawRawSelectionBox(float[] xpoints,float[] ypoints,float[] zpoints,double rColor,double gColor,double bColor,double thickens,double alpha,boolean[] bs){
		if(bs.length!=12)return;
		OpenGLM.disableTexture2D();
		OpenGLM.color(rColor,gColor,bColor,alpha);
		OpenGLM.lineWidth((float)thickens);
		Renderer.LINES.begin();
		{
			int a=0;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[0], ypoints[0], zpoints[0]);
				Renderer.LINES.addVertex(xpoints[1], ypoints[1], zpoints[1]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[2], ypoints[2], zpoints[2]);
				Renderer.LINES.addVertex(xpoints[3], ypoints[3], zpoints[3]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[4], ypoints[4], zpoints[4]);
				Renderer.LINES.addVertex(xpoints[5], ypoints[5], zpoints[5]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[6], ypoints[6], zpoints[6]);
				Renderer.LINES.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[0], ypoints[0], zpoints[0]);
				Renderer.LINES.addVertex(xpoints[6], ypoints[6], zpoints[6]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[1], ypoints[1], zpoints[1]);
				Renderer.LINES.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[0], ypoints[0], zpoints[0]);
				Renderer.LINES.addVertex(xpoints[2], ypoints[2], zpoints[2]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[1], ypoints[1], zpoints[1]);
				Renderer.LINES.addVertex(xpoints[3], ypoints[3], zpoints[3]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[2], ypoints[2], zpoints[2]);
				Renderer.LINES.addVertex(xpoints[4], ypoints[4], zpoints[4]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[3], ypoints[3], zpoints[3]);
				Renderer.LINES.addVertex(xpoints[5], ypoints[5], zpoints[5]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[4], ypoints[4], zpoints[4]);
				Renderer.LINES.addVertex(xpoints[6], ypoints[6], zpoints[6]);
			}a++;
			if(bs[a]){
				Renderer.LINES.addVertex(xpoints[5], ypoints[5], zpoints[5]);
				Renderer.LINES.addVertex(xpoints[7], ypoints[7], zpoints[7]);
			}
		}Renderer.LINES.draw();
		OpenGLM.enableTexture2D();
	}
	
	
	/**
	 * mainly used for helping to call drawRawSelectionBox because drawRawSelectionBox has raw box x,y,z coordinates an drawSelectionBox has max and min x,y,z
	 * */
	public void drawSelectionBox(AxisAlignedBBM box,double rColor,double gColor,double bColor,double thickens,double alpha,boolean[]... bs){
		drawSelectionBox(box.minX, box.maxX, box.minY, box.maxY, box.minZ, box.maxZ, rColor, gColor, bColor, thickens, alpha, bs);
	}
	public void drawSelectionBox(double minX, double maxX, double minY, double maxY, double minZ, double maxZ,double rColor,double gColor,double bColor,double thickens,double alpha,boolean[]... bs){
		if(bs.length==0||bs[0].length!=12){
			bs=new boolean[1][1];
			bs[0]=new boolean[]{true,true,true,true,true,true,true,true,true,true,true,true};
		}
		float[] xpoints=new float[8], ypoints=new float[8], zpoints=new float[8];
		xpoints[0]=(float)minX;xpoints[1]=(float)minX;
		ypoints[0]=(float)minY;ypoints[1]=(float)maxY;
		zpoints[0]=(float)minZ;zpoints[1]=(float)minZ;
		
		xpoints[2]=(float)maxX;xpoints[3]=(float)maxX;
		ypoints[2]=(float)minY;ypoints[3]=(float)maxY;
		zpoints[2]=(float)minZ;zpoints[3]=(float)minZ;
		
		xpoints[4]=(float)maxX;xpoints[5]=(float)maxX;
		ypoints[4]=(float)minY;ypoints[5]=(float)maxY;
		zpoints[4]=(float)maxZ;zpoints[5]=(float)maxZ;
		
		xpoints[6]=(float)minX;xpoints[7]=(float)minX;
		ypoints[6]=(float)minY;ypoints[7]=(float)maxY;
		zpoints[6]=(float)maxZ;zpoints[7]=(float)maxZ;
		
		drawRawSelectionBox(xpoints, ypoints, zpoints, rColor, gColor, bColor, thickens,alpha,bs[0]);
	}
	public void onDrawHFireReceaver(DrawBlockHighlightEvent event,TileEntity tile,BlockPos pos){
		double x1=(event.player.lastTickPosX+(event.player.posX-event.player.lastTickPosX)*event.partialTicks);
		double y1=(event.player.lastTickPosY+(event.player.posY-event.player.lastTickPosY)*event.partialTicks);
		double z1=(event.player.lastTickPosZ+(event.player.posZ-event.player.lastTickPosZ)*event.partialTicks);
		AxisAlignedBB bounds=UtilM.getBlock(event.player.worldObj,pos).getSelectedBoundingBox(event.player.worldObj, pos).expand(0.003, 0.003, 0.003).offset(-x1, -y1, -z1);
		AxisAlignedBB bounds2=UtilM.getBlock(event.player.worldObj,event.target.getBlockPos()).getSelectedBoundingBox(event.player.worldObj, event.target.getBlockPos()).expand(0.003, 0.003, 0.003).offset(-x1, -y1, -z1);
		
		OpenGLM.disableTexture2D();
		OpenGLM.lineWidth(2F);
		OpenGLM.color(0F, 0F, 1F, 1F);
		Renderer.LINES.begin();
		float[] xpoints=new float[8];float[] ypoints=new float[8];float[] zpoints=new float[8];
		
		for(int a=0;a<xpoints.length;a++){xpoints[a]=0;ypoints[a]=0;zpoints[a]=0;}
		
		xpoints[0]+=bounds.minX;
		ypoints[0]+=bounds.minY;
		zpoints[0]+=bounds.minZ;
		
		Renderer.LINES.addVertex(xpoints[0]+0.5, ypoints[0]+0.5, zpoints[0]+0.5);
		Renderer.LINES.addVertex(bounds2.minX+0.5, bounds2.minY+0.5, bounds2.minZ+0.5);
		Renderer.LINES.draw();
		OpenGLM.enableTexture2D();
		drawSelectionBox(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY, bounds.minZ, bounds.maxZ,0,0,1,2,1);
	}
	
	
	@SubscribeEvent
	public void onDrawHighlight(DrawBlockHighlightEvent event){
		
		BlockPos pos=event.target.getBlockPos();
		ItemStack item=event.currentItem;
		EntityPlayer player=event.player;
		World world=player.worldObj;
		if(world==null)return;
		try{if(event.target.typeOfHit!=MovingObjectType.ENTITY){
			TileEntity tileEn=player.worldObj.getTileEntity(pos);
			
			Object[][] rayTraceResult=TileEntityHologramProjector.rayTraceHolograms(player, 7);
			if(rayTraceResult[0].length>0){
				float distance=0;
				int id=0;
				for(int i=0;i<rayTraceResult[0].length;i++){
					float distanceTo=(float)player.getLook(1).distanceTo(((Vec3M)rayTraceResult[0][i]).conv());
					if(distance<distanceTo){
						id=i;
						distance=distanceTo;
					}
				}
				Vec3M hit=(Vec3M)rayTraceResult[0][id];
				TileEntityHologramProjector tile=(TileEntityHologramProjector)rayTraceResult[1][id];
				
				boolean
					hologramCloserThanHit=hit.conv().distanceTo(player.getLook(event.partialTicks))>event.target.hitVec.distanceTo(player.getLook(event.partialTicks)),
					miss=event.target.typeOfHit==MovingObjectType.MISS;
				if(miss||!hologramCloserThanHit){
					tile.point.isPointing=true;
					tile.point.pointedPos=hit.add(
							-tile.x()+tile.offset.x-1,
							-tile.y()-tile.offset.y-tile.size.y,
							0);
					tile.point.pointedPos.z=0;
					tile.point.pointingPlayer=player;
					event.setCanceled(true);
					return;
				}
			}else{
				for(int a=0;a<TileEntityHologramProjector.hologramProjectors.size();a++){
					TileEntityHologramProjector tile=null;
					TileEntity test=world.getTileEntity(TileEntityHologramProjector.hologramProjectors.get(a).getPos());
					if(test instanceof TileEntityHologramProjector)tile=(TileEntityHologramProjector) test;
					if(tile!=null)tile.point.isPointing=false;
				}
			}
			
			
			if(event.target.typeOfHit==MovingObjectType.BLOCK){
				if(event.target.hitVec==null)return;
				
//				if(tileEn instanceof TileEntityFirePipe){
//					TileEntityFirePipe ti=((TileEntityFirePipe)tileEn);
//					
//					if(ti.connections[0].isEnding())Helper.printInln("Ending");
//					else if(ti.connections[0].isIntersection())Helper.printInln("Intersection");
//					else Helper.printInln("Line");
//					
//				}
				
				if(tileEn instanceof TileEntityNetworkRouter)doNetRouterHighlight(pos, (TileEntityNetworkRouter)tileEn ,event);
				
				if(item!=null&&item.getItem().equals(MItems.fireHammer)){
					if(tileEn instanceof TileEntityFireLamp){
						TileEntityFireLamp tile=(TileEntityFireLamp)tileEn;
						onDrawHlFireLmap(event,tile,tile.getPos());
					}
					if(tileEn instanceof TileEntityFireMatrixReceaver){
						TileEntityFireMatrixReceaver tile=(TileEntityFireMatrixReceaver) tileEn;
						try{
							onDrawHlFireLmap(event,tile,tile.transferp);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				if(item!=null&&item.getTagCompound()!=null&&item.getItem().equals(MItems.powerCounter))doPowerCounterDisplay(pos,item,event);
				try{
					if(tileEn instanceof MultiColisionProvider){
						doMultiColision(pos, item, event);
						if(tileEn instanceof TileEntityNetworkRouter){
							int curentHighlight=((MultiColisionProvider)tileEn).getPointedBoxID();
							boolean contains=false;
							for(LongAfterRenderRenderer part:RenderEvents.universalLongRender){
								if(part instanceof RenderNetworkPointerContainerHighlight){
									RenderNetworkPointerContainerHighlight Part=(RenderNetworkPointerContainerHighlight) part;
									if(!Part.isDead()&&Part.tile.getPos().equals(pos)&&Part.highlightedBoxId==curentHighlight){
										contains=true;
									}
								}
							}
							if(!contains&&curentHighlight-7>=0)RenderEvents.spawnLARR(new RenderNetworkPointerContainerHighlight((TileEntityNetworkRouter) tileEn));
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}else{
			
		}}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onDrawHlFireLmap(DrawBlockHighlightEvent event,TileEntity tile,BlockPos pos){
		
		Vec3M off=PartialTicksUtil.calculatePos(event.player);
		AxisAlignedBB bounds=UtilM.getBlock(event.player.worldObj,pos).getSelectedBoundingBox(event.player.worldObj, pos).expand(0.003, 0.003, 0.003).offset(-off.x, -off.y, -off.z);
		
		OpenGLM.disableTexture2D();
		OpenGLM.disableDepth();
		OpenGLM.lineWidth(2F);
		OpenGLM.color(0F, 0F, 1F, 1F);
		
		drawSelectionBox(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY, bounds.minZ, bounds.maxZ,0,0,1,2,1);
		OpenGLM.enableTexture2D();
		OpenGLM.enableDepth();
	}
}
