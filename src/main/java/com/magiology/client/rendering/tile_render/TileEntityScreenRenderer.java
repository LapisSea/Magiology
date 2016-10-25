package com.magiology.client.rendering.tile_render;

import org.lwjgl.opengl.GL11;

import com.magiology.client.renderers.FastNormalRenderer;
import com.magiology.client.renderers.Renderer;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.mc_objects.features.screen.TileEntityScreen;
import com.magiology.util.m_extensions.TileEntitySpecialRendererM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.vec.IVec3M;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.objs.vec.Vec2i;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.GeometryUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.MathUtil;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class TileEntityScreenRenderer extends TileEntitySpecialRendererM<TileEntityScreen>{
	
	
	@Override
	public void renderTileEntityAt(TileEntityScreen tile, Vec3M renderPos, float partialTicks){
//		if(RandUtil.RB(0.01))ParticleBubbleFactory.get().spawn(new Vec3M(tile.getPos()).add(3.5),new Vec3M(),1,20,0,ColorF.randomRGB());
		try{
			double minX=0,minY=0,maxX=1,maxY=1;
			ColorF.WHITE.bind();
			if(tile.hasBrain()){
				boolean highl=false;
				if(UtilC.getThePlayer().isSneaking())try{
					highl=UtilC.getMC().objectMouseOver.getBlockPos().equals(tile.getPos());
				}catch(Exception e){}
				
				try{
					TileEntityScreen brain=tile.getBrain();
					TemporaryFrame screen=brain.screenTexture;
					
					Vec2i size=tile.getBrainObjects().get2();
					if(screen==null){
						screen=brain.screenTexture=new TemporaryFrame(size.x*64,size.y*64,false);
						screen.setRednerHook(f->renderScreen(brain,f.getWidth(),f.getHeight()));
					}else{
						screen.setSize(Math.max(size.x, 1)*64,Math.max(size.y, 1)*64);
					}
					if(tile.mbId==-1){
						tile.mbId=brain.getMultiblock().indexOf(tile);
					}
					Vec2i offset=brain.getBrainObjects().get1().get(tile.mbId);
					if(tile.screenDirty)screen.requestRender();
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
					if(UtilC.getThePlayer().isSneaking())e.printStackTrace();
					ColorF.BLUE.bind();
					OpenGLM.disableTexture2D();
				}
			}else{
				OpenGLM.disableTexture2D();
			}
			
			OpenGLM.pushMatrix();
			OpenGLM.translate(renderPos.addSelf(0.5F));
			OpenGLM.rotate(GeometryUtil.rotFromFacing(tile.getRotation()));
			OpenGLM.translate(-0.5F,-0.5F,-0.5F);
			
//			FastNormalRenderer renderer=new FastNormalRenderer();
//			renderer.begin(true,FastNormalRenderer.POS_UV);
//			
//			renderer.add(0, 1, 0.5,  minX-1,maxY+1);
//			renderer.add(0, 0, 0.5,  minX-1,minY-1);
//			renderer.add(1, 0, 0.5,  maxX+1,minY-1);
//			renderer.add(1, 1, 0.5,  maxX+1,maxY+1);
//			
//			renderer.draw();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderHelper.disableStandardItemLighting();
            
            if(Minecraft.isAmbientOcclusionEnabled())GlStateManager.shadeModel(7425);
            else GlStateManager.shadeModel(7424);
            
			try{
				
				FastNormalRenderer buf=new FastNormalRenderer();
				buf.begin(true, FastNormalRenderer.BLOCK).usingQuadConversion();
				buf.add(0, 1, 11/16F,  minX,maxY,1,1,1,1,0,0);
				buf.add(0, 0, 11/16F,  minX,minY,1,1,1,1,0,0);
				buf.add(1, 0, 11/16F,  maxX,minY,1,1,1,1,0,0);
				buf.add(1, 1, 11/16F,  maxX,maxY,1,1,1,1,0,0);
				buf.draw();
				
//				VertexBuffer buf=Tessellator.getInstance().getBuffer();
//				buf.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
////		        BLOCK.addElement(POSITION_3F);
////		        BLOCK.addElement(COLOR_4UB);
////		        BLOCK.addElement(TEX_2F);
////		        BLOCK.addElement(TEX_2S);
//
//		        int i = getWorld().getCombinedLight(tile.getPos(), 0);
//		        int j = i >> 16 & 65535;
//		        int k = i & 65535;
//				int l=getWorld().getLight(tile.getPos(), true);
//		        
//				buf.pos(0D, 1D, 11/16D).color(255, 255, 255, 255).tex(minX,maxY).lightmap(j,k).endVertex();
//				buf.pos(0D, 0D, 11/16D).color(255, 255, 255, 255).tex(minX,minY).lightmap(j,k).endVertex();
//				buf.pos(1D, 0D, 11/16D).color(255, 255, 255, 255).tex(maxX,minY).lightmap(j,k).endVertex();
//				buf.pos(1D, 1D, 11/16D).color(255, 255, 255, 255).tex(maxX,maxY).lightmap(j,k).endVertex();
//				
//				Tessellator.getInstance().draw();
			}catch(Exception e){
				if(UtilC.getThePlayer().isSneaking())e.printStackTrace();
			}
			
			if(!tile.hasBrain())OpenGLM.enableTexture2D();
			OpenGLM.popMatrix();
		}catch(Exception e){
			e.printStackTrace();
		}
		OpenGLM.enableTexture2D();
		ColorF.WHITE.bind();
	}
	
	protected static class CrummySmoothLightUtil{
		World world;
		private Vec2i[][] data=new Vec2i[2][4];
		
		private static final Vec3M offPos=new Vec3M();
		//                      y xz s data
		private static final int[][][][] sides={
			{//    x     y     z
				{{0,0},{1,0},{0,0}},
				{{0,0},{1,0},{0,0}},
				{{0,0},{1,0},{0,0}},
				{{0,0},{1,0},{0,0}},
			},
			{
				{{1,0},{0,0},{1,0}},
				{{1,0},{0,0},{1,0}},
				{{1,0},{0,0},{1,0}},
				{{1,0},{0,0},{1,0}},
			}
		};
//		data[0][0]=calc(pos,down,north,west);
//		data[0][1]=calc(pos,down,north,east);
//		data[0][2]=calc(pos,down,south,east);
//		data[0][3]=calc(pos,down,south,west);
//
//		data[1][0]=calc(pos,up,north,west);
//		data[1][1]=calc(pos,up,north,east);
//		data[1][2]=calc(pos,up,south,east);
//		data[1][3]=calc(pos,up,south,west);
		
//	    DOWN(0, 1, -1, "down", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Y, new Vec3i(0, -1, 0)),
//	    UP(1, 0, -1, "up", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Y, new Vec3i(0, 1, 0)),
//	    NORTH(2, 3, 2, "north", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, -1)),
//	    SOUTH(3, 2, 0, "south", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, 1)),
//	    WEST(4, 5, 1, "west", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X, new Vec3i(-1, 0, 0)),
//	    EAST(5, 4, 3, "east", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X, new Vec3i(1, 0, 0));
		
		public Vec2i get(IVec3M inBlock){
			int x,y=inBlock.y()<0.5?0:1,z,plane;
			
			boolean north=inBlock.z()<0.5;
			
			if(inBlock.x()<0.5){
//				WEST
				plane=north?0:3;
				x=0;
			}else{
//				EAST
				plane=north?1:2;
				x=1;
			}
			z=north?0:1;
			offPos.set(x,y,z);
			offPos.sub(inBlock);
			
			Vec2i start=data[y][plane],end;
			
			double value=offPos.x();
			int xyz=0;
			
			if(value<offPos.y()){
				value=offPos.y();
				xyz=1;
			}
			if(value<offPos.z()){
				value=offPos.z();
				xyz=2;
			}
			
			int[] endPoint=sides[y][plane][xyz];
			
			end=data[endPoint[0]][endPoint[1]];

			float xDif=end.x-start.x;
			float yDif=end.y-start.y;
			
			return new Vec2i((int)(start.x+xDif*value), (int)(start.y+yDif*value));
		}
		
		public void update(BlockPos pos,World world){
			this.world=world;
			BlockPos up=pos.up(),down=pos.down(),west=pos.west(),east=pos.east(),south=pos.south(),north=pos.north();

			data[0][0]=calc(pos,down,north,west);
			data[0][1]=calc(pos,down,north,east);
			data[0][2]=calc(pos,down,south,east);
			data[0][3]=calc(pos,down,south,west);

			data[1][0]=calc(pos,up,north,west);
			data[1][1]=calc(pos,up,north,east);
			data[1][2]=calc(pos,up,south,east);
			data[1][3]=calc(pos,up,south,west);
		}
		
		private Vec2i calc(BlockPos p1, BlockPos p2, BlockPos p3, BlockPos p4){
			int i=world.getCombinedLight(p1, world.getCombinedLight(p2, world.getCombinedLight(p3, world.getCombinedLight(p4, 0))));
			return new Vec2i(i>>16&65535, i&65535);
		}
	}
	
	
	private void renderScreen(TileEntityScreen tile, int width, int height){
		
		OpenGLM.disableTexture2D();
		ColorF.WHITE.bind();
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex(1,       1,        0);
		Renderer.POS.addVertex(1,       height-1, 0);
		Renderer.POS.addVertex(width-1, height-1, 0);
		Renderer.POS.addVertex(width-1, 1,        0);
		Renderer.POS.draw();

		ColorF.ORANGE.bind();
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(1,1,0);
		Renderer.LINES.addVertex(width-1,height-1,0);
		Renderer.LINES.addVertex(1,height-1,0);
		Renderer.LINES.addVertex(width-1,1,0);
		Renderer.LINES.draw();
		ColorF.GREEN.bind();
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex( 10,  10, 0);
		Renderer.POS.addVertex( 10, -10, 0);
		Renderer.POS.addVertex(-10, -10, 0);
		Renderer.POS.addVertex(-10,  10, 0);
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(width/2F, height/2F, 0);
		OpenGLM.rotateZ(Math.sin(((((double)UtilM.worldTime(tile))+PartialTicksUtil.partialTicks)/20+tile.getPos().toLong())%(Math.PI*2))*360);
		Renderer.POS.draw();
		OpenGLM.popMatrix();
		
		
		
		ColorF.BLACK.bind();
		Renderer.POS.beginQuads();
		
		Renderer.POS.addVertex(0,     0, 0);
		Renderer.POS.addVertex(0,     1, 0);
		Renderer.POS.addVertex(width, 1, 0);
		Renderer.POS.addVertex(width, 0, 0);
		
		Renderer.POS.addVertex(0,     height-1, 0);
		Renderer.POS.addVertex(0,     height,   0);
		Renderer.POS.addVertex(width, height,   0);
		Renderer.POS.addVertex(width, height-1, 0);

		Renderer.POS.addVertex(0, 0,      0);
		Renderer.POS.addVertex(0, height, 0);
		Renderer.POS.addVertex(1, height, 0);
		Renderer.POS.addVertex(1, 0,      0);

		Renderer.POS.addVertex(width-1, 0,      0);
		Renderer.POS.addVertex(width-1, height, 0);
		Renderer.POS.addVertex(width,   height, 0);
		Renderer.POS.addVertex(width,   0,      0);
		
		Renderer.POS.draw();
		ColorF.WHITE.bind();
		OpenGLM.enableTexture2D();
	}
	
	
}
