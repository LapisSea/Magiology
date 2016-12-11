package com.magiology.client.rendering.highlight.types;

import com.magiology.client.renderers.Renderer;
import com.magiology.client.rendering.highlight.BlockHighlightRenderer;
import com.magiology.util.objs.block_bounds.StateDependantBlockBounds;
import com.magiology.util.objs.block_bounds.StateDependantBlockBounds.StateBounds;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class StateDependantBlockHighlightRenderer extends BlockHighlightRenderer<StateDependantBlockBounds>{
	
	public StateDependantBlockHighlightRenderer(StateDependantBlockBounds owner){
		super(owner);
	}

	@Override
	public void drawBoundsOutline(IBlockState state, World world, BlockPos pos, RayTraceResult hit){
		StateBounds box=getOwner().getBox(state);
		if(box.getDrawModel()==-1)box.setDrawModel(createModel(box));
		
		OpenGLM.callList(box.getDrawModel());
	}
	
	private int createModel(StateBounds box){
		int drawModel=GLAllocation.generateDisplayLists(1);
		LogUtil.println(drawModel);
		GlStateManager.glNewList(drawModel, 4864);
		
		Renderer.LINES.begin();
		generateBoxLines(box.box.expandXyz(0.0020000000949949026D)).forEach(Renderer.LINES::addVertex);
		Renderer.LINES.draw();
		
		GlStateManager.glEndList();
		return drawModel;
	}
	@Override
	public void markDirty(){
		for(StateBounds box:getOwner().boxes){
			box.setDrawModel(-1);
		}
	}
}
