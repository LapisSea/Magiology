package com.magiology.util.objs.block_bounds;

import java.util.ArrayList;
import java.util.List;

import com.magiology.client.renderers.Renderer;
import com.magiology.util.statics.OpenGLM;

import joptsimple.internal.Objects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiBlockBounds implements IBlockBounds{
	
	private boolean drawModelInvalid;
	private final List<AxisAlignedBB> boxes=new ArrayList<>();
	private AxisAlignedBB union;
	
	@SideOnly(Side.CLIENT)
	private int drawModel=-1;
	
	
	public MultiBlockBounds(AxisAlignedBB...box){
		setBlockBounds(box);
	}
	
	public void modifyBlockBounds(int id,AxisAlignedBB box){
		Objects.ensureNotNull(box);
		boxes.set(id, box);
		drawModelInvalid=true;
		updateUnion();
	}
	
	public void setBlockBounds(AxisAlignedBB...boxes){
		Objects.ensureNotNull(boxes);
		if(boxes.length==0)throw new IllegalArgumentException("Boxes are empty!");
		
		drawModelInvalid=true;
		this.boxes.clear();
		for(AxisAlignedBB box:boxes){
			Objects.ensureNotNull(box);
			this.boxes.add(box);
		}
		updateUnion();
	}
	
	private void updateUnion(){
		union=boxes.get(0);
		for(int i=1;i<boxes.size();i++){
			union=union.union(boxes.get(i));
		}
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return union;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity){
		boxes.forEach(box->addCollisionBox(pos, entityBox, collidingBoxes, box));
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
		//TODO: make the nightmare sexy outline
		generateBoxLines(union.expand(0.02, 0.02, 0.02)).forEach(Renderer.LINES::addVertex);
		Renderer.LINES.draw();
		
		GlStateManager.glEndList();
	}
	
	@Override
	protected void finalize(){
		if(drawModel!=-1)OpenGLM.glDeleteLists(drawModel, 1);
	}
	
}
