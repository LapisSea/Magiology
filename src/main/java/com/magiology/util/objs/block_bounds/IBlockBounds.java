package com.magiology.util.objs.block_bounds;

import com.magiology.client.rendering.highlight.BlockHighlightRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public interface IBlockBounds{
	
	boolean isFullCube(IBlockState state);
	
	AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos);
	
	default RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end){
		return rayTrace(pos, start, end, getBoundingBox(state, world, pos));
	}
	
	default void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity){
		addCollisionBox(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(world, pos));
	}
	
	@SideOnly(Side.CLIENT)
	BlockHighlightRenderer getHighlightRenderer();
	
	@SideOnly(Side.CLIENT)
	void setHighlightRenderer(BlockHighlightRenderer renderer);
	
	//========================================UTILS========================================
	default RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB box){
		Vec3d vec3d=start.subtract(pos.getX(), pos.getY(), pos.getZ());
		Vec3d vec3d1=end.subtract(pos.getX(), pos.getY(), pos.getZ());
		RayTraceResult raytraceresult=box.calculateIntercept(vec3d, vec3d1);
		return raytraceresult==null?null:new RayTraceResult(raytraceresult.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), raytraceresult.sideHit, pos);
	}
	
	default void addCollisionBox(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable AxisAlignedBB blockBox){
		if(blockBox!=null){
			AxisAlignedBB axisalignedbb=blockBox.offset(pos);
			
			if(entityBox.intersects(axisalignedbb)){
				collidingBoxes.add(axisalignedbb);
			}
		}
	}
	
}
