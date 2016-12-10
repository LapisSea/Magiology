package com.magiology.client.rendering.highlight.types;

import com.magiology.client.renderers.Renderer;
import com.magiology.client.rendering.highlight.BlockHighlightRenderer;
import com.magiology.util.objs.block_bounds.IBlockBounds;
import com.magiology.util.statics.OpenGLM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BasicBlockHighlightRenderer extends BlockHighlightRenderer{

	protected int drawModel=-1;
	
	public BasicBlockHighlightRenderer(IBlockBounds owner){
		super(owner);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBoundsOutline(IBlockState state, World world, BlockPos pos, RayTraceResult hit){
		if(drawModel==-1)createModel(state, world, pos);
		
		OpenGLM.callList(drawModel);
	}
	
	@SideOnly(Side.CLIENT)
	private void createModel(IBlockState state, World world, BlockPos pos){
		if(drawModel!=-1)OpenGLM.glDeleteLists(drawModel, 1);
		drawModel=GLAllocation.generateDisplayLists(1);
		GlStateManager.glNewList(drawModel, 4864);
		
		Renderer.LINES.begin();
		generateBoxLines(getOwner().getBoundingBox(state, world, pos).expandXyz(0.0020000000949949026D)).forEach(Renderer.LINES::addVertex);
		Renderer.LINES.draw();
		
		GlStateManager.glEndList();
	}
	
	@Override
	protected void finalize(){
		if(drawModel!=-1)OpenGLM.glDeleteLists(drawModel, 1);
	}

	@Override
	public void markDirty(){
		if(drawModel!=-1){
			OpenGLM.glDeleteLists(drawModel, 1);
			drawModel=-1;
		}
	}
}
