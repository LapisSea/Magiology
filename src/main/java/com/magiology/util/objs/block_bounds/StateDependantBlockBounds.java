package com.magiology.util.objs.block_bounds;

import com.magiology.client.renderers.Renderer;
import com.magiology.util.interf.IntReturn;
import com.magiology.util.statics.OpenGLM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StateDependantBlockBounds implements IBlockBounds{
	
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
			if(drawModel!=-1)OpenGLM.glDeleteLists(drawModel, 1);
			drawModel=GLAllocation.generateDisplayLists(1);
			GlStateManager.glNewList(drawModel, 4864);
			
			Renderer.LINES.begin();
			generateBoxLines(box.expandXyz(0.0020000000949949026D)).forEach(Renderer.LINES::addVertex);
			Renderer.LINES.draw();
			
			GlStateManager.glEndList();
		}
		
		@Override
		protected void finalize(){
			if(drawModel!=-1)OpenGLM.glDeleteLists(drawModel, 1);
		}
	}
	
	private final StateBounds[] boxes;
	private final IntReturn<IBlockState> boxPicker;
	
	public StateDependantBlockBounds(IntReturn<IBlockState> boxPicker,AxisAlignedBB... boxes){
		this.boxes=new StateBounds[boxes.length];
		
		for(int i=0;i<boxes.length;i++){
			this.boxes[i]=new StateBounds(boxes[i]);
		}
		this.boxPicker=boxPicker;
	}
	
	
	private StateBounds getBox(IBlockState state){
		return boxes[boxPicker.get(state)];
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
	public void drawBoundsOutline(IBlockState state, World world, BlockPos pos){
		StateBounds box=getBox(state);
		if(box.drawModelInvalid) box.createModel();
		
		OpenGLM.callList(box.drawModel);
	}
	
}
