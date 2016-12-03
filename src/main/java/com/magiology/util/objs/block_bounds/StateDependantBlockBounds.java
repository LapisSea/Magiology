package com.magiology.util.objs.block_bounds;

import com.magiology.client.renderers.Renderer;
import com.magiology.util.statics.OpenGLM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class StateDependantBlockBounds implements IBlockBounds{
	
	private final class StateBounds{
		
		@SideOnly(Side.CLIENT)
		private int			drawModel		=-1;
		boolean				drawModelInvalid=true;
		final AxisAlignedBB	box;
		final boolean		fullCube;
		
		public StateBounds(AxisAlignedBB box){
			this.box=box;
			fullCube=box.minX==0&&box.maxX==1&&
					 box.minY==0&&box.maxY==1&&
					 box.minZ==0&&box.maxZ==1;
		}
		
		@SideOnly(Side.CLIENT)
		void createModel(){
			drawModelInvalid=false;
			
			drawModel=GLAllocation.generateDisplayLists(1);
			GlStateManager.glNewList(drawModel, 4864);
			
			Renderer.LINES.begin();
			generateBoxLines(box.expand(0.02, 0.02, 0.02)).forEach(Renderer.LINES::addVertex);
			Renderer.LINES.draw();
			
			GlStateManager.glEndList();
		}
		
		@Override
		protected void finalize(){
			if(drawModel!=-1)OpenGLM.glDeleteLists(drawModel, 1);
		}
	}
	
	private final StateBounds[] boxes;
	
	public StateDependantBlockBounds(AxisAlignedBB... boxes){
		this.boxes=new StateBounds[boxes.length];
		
		for(int i=0;i<boxes.length;i++){
			this.boxes[i]=new StateBounds(boxes[i]);
		}
	}
	
	public abstract int chooseBox(IBlockState state);
	
	private StateBounds getBox(IBlockState state){
		return boxes[chooseBox(state)];
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return getBox(state).fullCube;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return getBox(state).box;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBoundsOutline(IBlockState state, IBlockAccess source, BlockPos pos){
		StateBounds box=getBox(state);
		if(box.drawModelInvalid) box.createModel();
		
		OpenGLM.callList(box.drawModel);
	}
	
}
