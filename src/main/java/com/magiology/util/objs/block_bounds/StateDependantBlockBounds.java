package com.magiology.util.objs.block_bounds;

import com.magiology.client.rendering.highlight.BlockHighlightRenderer;
import com.magiology.client.rendering.highlight.types.BasicBlockHighlightRenderer;
import com.magiology.util.interf.IntReturn;
import com.magiology.util.statics.OpenGLM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StateDependantBlockBounds implements IBlockBounds{
	
	public final class StateBounds{
		
		@SideOnly(Side.CLIENT)
		private int drawModel=-1;
		
		@SideOnly(Side.CLIENT)
		public int getDrawModel(){
			return drawModel;
		}
		
		@SideOnly(Side.CLIENT)
		public void setDrawModel(int drawModel){
			if(this.drawModel==drawModel) return;
			if(drawModel!=-1)OpenGLM.glDeleteLists(this.drawModel, 1);
			this.drawModel=drawModel;
		}
		
		public final AxisAlignedBB	box;
		final boolean				fullCube;
		
		public StateBounds(AxisAlignedBB box){
			this.box=box;
			fullCube=box.minX==0&&box.maxX==1&&
					box.minY==0&&box.maxY==1&&
					box.minZ==0&&box.maxZ==1;
		}
		
		@Override
		protected void finalize(){
			if(drawModel!=-1)OpenGLM.glDeleteLists(drawModel, 1);
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	public BlockHighlightRenderer renderer=new BasicBlockHighlightRenderer(this);
	
	public final StateBounds[]				boxes;
	private final IntReturn<IBlockState>	boxPicker;
	
	public StateDependantBlockBounds(IntReturn<IBlockState> boxPicker, AxisAlignedBB...boxes){
		this.boxes=new StateBounds[boxes.length];
		
		for(int i=0;i<boxes.length;i++){
			this.boxes[i]=new StateBounds(boxes[i]);
		}
		this.boxPicker=boxPicker;
	}
	
	public StateBounds getBox(IBlockState state){
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
	public BlockHighlightRenderer getHighlightRenderer(){
		return renderer;
	}
	
	@Override
	public void setHighlightRenderer(BlockHighlightRenderer renderer){
		this.renderer=renderer;
	}
	
}
