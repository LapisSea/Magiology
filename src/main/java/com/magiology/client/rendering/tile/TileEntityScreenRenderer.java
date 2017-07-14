package com.magiology.client.rendering.tile;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.magiology.client.renderers.FastNormalRenderer;
import com.magiology.client.renderers.Renderer;
import com.magiology.core.registry.init.ParticlesM;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
import com.magiology.mc_objects.features.screen.TileEntityScreen.ScreenMultiblockHandler;
import com.magiology.util.interf.Locateable.LocateableBlockM;
import com.magiology.util.m_extensions.TileEntitySpecialRendererM;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.color.IColorM;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.objs.vec.Vec2i;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.GeometryUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.UtilC;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import scala.tools.nsc.doc.base.comment.Link;

public class TileEntityScreenRenderer extends TileEntitySpecialRendererM<TileEntityScreen>{
	
	@Override
	public void render(TileEntityScreen tile, Vec3M renderPos, float partialTicks){
		//		if(RandUtil.RB(0.01))ParticleBubbleFactory.get().spawn(new Vec3M(tile.getPos()).add(3.5),new Vec3M(),1,20,0,ColorF.randomRGB());
		List<PairM<TileEntityScreen,IColorM>> ll=new ArrayList<>();
		
		if(tile.isBrain()){
			for(TileEntityScreen t:tile.getHandler().loaded){
				ll.add(new PairM<>(t,ColorM.GREEN));
			}
			for(TileEntityScreen t:tile.getHandler().pending){
				ll.add(new PairM<>(t,ColorM.RED));
			}
		}
		else if(tile.hasBrain()){
			ll.add(new PairM<>(tile.getBrain(),ColorM.BLUE));
		}
		
		if(ll!=null){
			OpenGLM.pushMatrix();
			OpenGLM.translate(new Vec3M(tile.getPos()).mulSelf(-1).addSelf(renderPos).addSelf(0.5F).addSelf(tile.getRotation()));
			
			OpenGLM.disableTexture2D();
			OpenGLM.disableDepth();
			OpenGLM.setUpOpaqueRendering(BlendFunc.NORMAL);
			try{
				
				for(PairM<TileEntityScreen,IColorM> i:ll){
					GL11.glLineWidth(ll.size()*2);
					i.obj2.bindWithA(0.2F);
					Renderer.LINES.begin();
					Renderer.LINES.addVertex(new Vec3M(tile.getPos()));
					Renderer.LINES.addVertex(new Vec3M(i.obj1.getPos()));
					Renderer.LINES.draw();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			OpenGLM.enableTexture2D();
			OpenGLM.enableDepth();
			
			OpenGLM.popMatrix();
		}
		ColorM.WHITE.bind();
		
		try{
			double minX=0,minY=0,maxX=1,maxY=1;
			ColorM.WHITE.bind();
			
			boolean renderScreen=tile.hasBrain();
			if(renderScreen){
				ScreenMultiblockHandler mb=tile.getBrain().getHandler();
				renderScreen=mb!=null;
			}
			
			if(renderScreen){
				boolean highl=false;
				if(UtilC.getThePlayer().isSneaking()) try{
					highl=UtilC.getMC().objectMouseOver.getBlockPos().equals(tile.getPos());
				}catch(Exception e){}
				
				try{
					TileEntityScreen brain=tile.getBrain();
					TemporaryFrame screen=brain.screenTexture;
					
					Vec2i size=brain.getSize2d();
					if(screen==null){
						screen=brain.screenTexture=new TemporaryFrame(size.x*64, size.y*64, false);
						screen.setRednerHook(f->renderScreen(brain, f.getWidth(), f.getHeight()));
					}else{
						screen.setSize(Math.max(size.x, 1)*64, Math.max(size.y, 1)*64);
					}
					Vec2i offset=brain.getPositions().get(tile.getMbId());
					if(tile.screenDirty) screen.requestRender();
					screen.bindTexture();
					
					minX=offset.x;
					minY=offset.y+size.y-1;
					maxX=offset.x+1;
					maxY=offset.y+size.y;
					
					minX/=size.x;
					minY/=size.y;
					maxX/=size.x;
					maxY/=size.y;
					
				}catch(Exception e){
					if(UtilC.getThePlayer().isSneaking()){
						ParticlesM.MESSAGE.spawn(new Vec3M(tile.getPos()).add(0.5).subX(1), new Vec3M(-0.01, 0, 0), 3/16F, 40, ColorM.RED, e.toString());
						e.printStackTrace();
					}
					ColorM.BLUE.bind();
					OpenGLM.disableTexture2D();
				}
			}else OpenGLM.disableTexture2D();
			
			OpenGLM.pushMatrix();
			OpenGLM.translate(renderPos.addSelf(0.5F));
			
			OpenGLM.rotate(GeometryUtil.rotFromFacing(tile.getRotation()));
			OpenGLM.translate(-0.5F, -0.5F, -0.5F);
			
			if(Minecraft.isAmbientOcclusionEnabled()) GlStateManager.shadeModel(7425);
			else GlStateManager.shadeModel(7424);
			
			try{
				FastNormalRenderer buf=new FastNormalRenderer();
				buf.begin(true, FastNormalRenderer.POS_UV);
				buf.add(0, 1, 11.01F/16F, minX, maxY);
				buf.add(0, 0, 11.01F/16F, minX, minY);
				buf.add(1, 0, 11.01F/16F, maxX, minY);
				buf.add(1, 1, 11.01F/16F, maxX, maxY);
				buf.draw();
				
			}catch(Exception e){
				if(UtilC.getThePlayer().isSneaking()) e.printStackTrace();
			}
			
			if(!tile.hasBrain()) OpenGLM.enableTexture2D();
			OpenGLM.popMatrix();
		}catch(Exception e){
			e.printStackTrace();
		}
		OpenGLM.enableTexture2D();
		
	}
	
	private void renderScreen(TileEntityScreen tile, int width, int height){
		
		TileEntityScreen brain=tile.getBrain();
		
		OpenGLM.disableTexture2D();
		ColorM.WHITE.bind();
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex(1, 1, 0);
		Renderer.POS.addVertex(1, height-1, 0);
		Renderer.POS.addVertex(width-1, height-1, 0);
		Renderer.POS.addVertex(width-1, 1, 0);
		Renderer.POS.draw();
		
		ColorM.ORANGE.bind();
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(1, 1, 0);
		Renderer.LINES.addVertex(width-1, height-1, 0);
		Renderer.LINES.addVertex(1, height-1, 0);
		Renderer.LINES.addVertex(width-1, 1, 0);
		Renderer.LINES.draw();
		
		OpenGLM.pushMatrix();
		Vec2FM pos=brain.click;
		OpenGLM.translate(pos.x, pos.y, 0);
		
		ColorM.RED.bind();
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex(1, 1, 0);
		Renderer.POS.addVertex(1, -1, 0);
		Renderer.POS.addVertex(-1, -1, 0);
		Renderer.POS.addVertex(-1, 1, 0);
		Renderer.POS.draw();
		
		OpenGLM.popMatrix();
		
		if(brain.highlighted){
			OpenGLM.pushMatrix();
			
			Vec2FM pos1=brain.highlight;
			OpenGLM.translate(pos1.x, pos1.y, 0);
			
			ColorM.BLUE.bind();
			Renderer.POS.beginQuads();
			Renderer.POS.addVertex(1, 1, 0);
			Renderer.POS.addVertex(1, -1, 0);
			Renderer.POS.addVertex(-1, -1, 0);
			Renderer.POS.addVertex(-1, 1, 0);
			Renderer.POS.draw();
			
			OpenGLM.popMatrix();
		}
		
		ColorM.BLACK.bind();
		Renderer.POS.beginQuads();
		
		Renderer.POS.addVertex(0, 0, 0);
		Renderer.POS.addVertex(0, 1, 0);
		Renderer.POS.addVertex(width, 1, 0);
		Renderer.POS.addVertex(width, 0, 0);
		
		Renderer.POS.addVertex(0, height-1, 0);
		Renderer.POS.addVertex(0, height, 0);
		Renderer.POS.addVertex(width, height, 0);
		Renderer.POS.addVertex(width, height-1, 0);
		
		Renderer.POS.addVertex(0, 0, 0);
		Renderer.POS.addVertex(0, height, 0);
		Renderer.POS.addVertex(1, height, 0);
		Renderer.POS.addVertex(1, 0, 0);
		
		Renderer.POS.addVertex(width-1, 0, 0);
		Renderer.POS.addVertex(width-1, height, 0);
		Renderer.POS.addVertex(width, height, 0);
		Renderer.POS.addVertex(width, 0, 0);
		
		Renderer.POS.draw();
		ColorM.WHITE.bind();
		OpenGLM.enableTexture2D();
	}
	
}
