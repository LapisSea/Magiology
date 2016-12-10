package com.magiology.util.objs.block_bounds;

import com.magiology.client.rendering.highlight.BlockHighlightRenderer;
import com.magiology.client.rendering.highlight.types.BasicBlockHighlightRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BasicBlockBounds implements IBlockBounds{
	
	private boolean fullCube;
	private AxisAlignedBB box;
	
	@SideOnly(Side.CLIENT)
	private BlockHighlightRenderer renderer=new BasicBlockHighlightRenderer(this);
	
	
	public BasicBlockBounds(){
		this(0, 0, 0, 1, 1, 1);
	}
	
	public BasicBlockBounds(double x1, double y1, double z1, double x2, double y2, double z2){
		setBlockBounds(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
	}
	public BasicBlockBounds(AxisAlignedBB box){
		setBlockBounds(box);
	}
	
	public void setBlockBounds(AxisAlignedBB box){
		renderer.markDirty();
		this.box=box;
		fullCube=
				box.minX==0&&box.maxX==1&&
				box.minY==0&&box.maxY==1&&
				box.minZ==0&&box.maxZ==1;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return fullCube;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return box;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockHighlightRenderer getHighlightRenderer(){
		return renderer;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setHighlightRenderer(BlockHighlightRenderer renderer){
		this.renderer=renderer;
	}
	
}
