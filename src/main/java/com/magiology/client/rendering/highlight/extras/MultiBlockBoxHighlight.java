package com.magiology.client.rendering.highlight.extras;

import com.magiology.client.renderers.Renderer;
import com.magiology.client.rendering.highlight.BlockHighlightRenderer.HighlightListener;
import com.magiology.util.objs.block_bounds.MultiBlockBounds;
import com.magiology.util.objs.color.IColorM;
import com.magiology.util.statics.UtilC;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class MultiBlockBoxHighlight implements HighlightListener<MultiBlockBounds>{
	
	@Override
	public void drawBoundsOutline(MultiBlockBounds parent, IBlockState state, World world, BlockPos pos, RayTraceResult hit){
		int id=hit.subHit;
		if(id==-1)return;
		
		float alphaRaw=UtilC.fluctuateLinSmooth(50, 0, 0, 1),alpha=alphaRaw*0.3F+0.1F;
		AxisAlignedBB box=parent.getBox(id).expandXyz(0.005);
		IColorM.BLACK.bindWithA(alpha);
		Renderer.LINES.begin();
		
		Renderer.LINES.addVertex(box.minX,box.minY,box.minZ);
		Renderer.LINES.addVertex(box.maxX,box.minY,box.minZ);
		
		Renderer.LINES.addVertex(box.minX,box.minY,box.maxZ);
		Renderer.LINES.addVertex(box.maxX,box.minY,box.maxZ);
		
		Renderer.LINES.addVertex(box.minX,box.maxY,box.minZ);
		Renderer.LINES.addVertex(box.maxX,box.maxY,box.minZ);
		
		Renderer.LINES.addVertex(box.minX,box.maxY,box.maxZ);
		Renderer.LINES.addVertex(box.maxX,box.maxY,box.maxZ);
		
		
		Renderer.LINES.addVertex(box.minX,box.minY,box.minZ);
		Renderer.LINES.addVertex(box.minX,box.maxY,box.minZ);
		
		Renderer.LINES.addVertex(box.maxX,box.minY,box.minZ);
		Renderer.LINES.addVertex(box.maxX,box.maxY,box.minZ);
		
		Renderer.LINES.addVertex(box.minX,box.minY,box.maxZ);
		Renderer.LINES.addVertex(box.minX,box.maxY,box.maxZ);
		
		Renderer.LINES.addVertex(box.maxX,box.minY,box.maxZ);
		Renderer.LINES.addVertex(box.maxX,box.maxY,box.maxZ);
		
		
		Renderer.LINES.addVertex(box.minX,box.minY,box.minZ);
		Renderer.LINES.addVertex(box.minX,box.minY,box.maxZ);
		
		Renderer.LINES.addVertex(box.maxX,box.minY,box.minZ);
		Renderer.LINES.addVertex(box.maxX,box.minY,box.maxZ);
		
		Renderer.LINES.addVertex(box.minX,box.maxY,box.minZ);
		Renderer.LINES.addVertex(box.minX,box.maxY,box.maxZ);
		
		Renderer.LINES.addVertex(box.maxX,box.maxY,box.minZ);
		Renderer.LINES.addVertex(box.maxX,box.maxY,box.maxZ);
		
		Renderer.LINES.draw();
		
	}
	
}
