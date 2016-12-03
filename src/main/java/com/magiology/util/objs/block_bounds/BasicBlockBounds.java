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

public class BasicBlockBounds implements IBlockBounds{
	
	private boolean fullCube,drawModelInvalid;
	private AxisAlignedBB box;
	
	@SideOnly(Side.CLIENT)
	private int drawModel=-1;
	
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
		drawModelInvalid=true;
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
	public void drawBoundsOutline(IBlockState state, IBlockAccess source, BlockPos pos){
		if(drawModelInvalid)createModel();
		
		OpenGLM.callList(drawModel);
	}
	
	@SideOnly(Side.CLIENT)
	private void createModel(){
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
