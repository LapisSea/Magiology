package com.magiology.util.objs.block_bounds;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.magiology.util.objs.vec.IVec3M;
import com.magiology.util.objs.vec.Vec3M;

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
	void drawBoundsOutline(IBlockState state, World world, BlockPos pos);
	
	
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
			
			if(entityBox.intersectsWith(axisalignedbb)){
				collidingBoxes.add(axisalignedbb);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	default List<IVec3M> generateBoxLines(AxisAlignedBB box){
		List<IVec3M> positions=new ArrayList<>();
		
		
		positions.add(new Vec3M(box.minX,box.minY,box.minZ));
		positions.add(new Vec3M(box.maxX,box.minY,box.minZ));
		
		positions.add(new Vec3M(box.minX,box.minY,box.maxZ));
		positions.add(new Vec3M(box.maxX,box.minY,box.maxZ));
		
		positions.add(new Vec3M(box.minX,box.maxY,box.minZ));
		positions.add(new Vec3M(box.maxX,box.maxY,box.minZ));
		
		positions.add(new Vec3M(box.minX,box.maxY,box.maxZ));
		positions.add(new Vec3M(box.maxX,box.maxY,box.maxZ));
		
		
		positions.add(new Vec3M(box.minX,box.minY,box.minZ));
		positions.add(new Vec3M(box.minX,box.maxY,box.minZ));
		
		positions.add(new Vec3M(box.maxX,box.minY,box.minZ));
		positions.add(new Vec3M(box.maxX,box.maxY,box.minZ));
		
		positions.add(new Vec3M(box.minX,box.minY,box.maxZ));
		positions.add(new Vec3M(box.minX,box.maxY,box.maxZ));
		
		positions.add(new Vec3M(box.maxX,box.minY,box.maxZ));
		positions.add(new Vec3M(box.maxX,box.maxY,box.maxZ));
		
		
		positions.add(new Vec3M(box.minX,box.minY,box.minZ));
		positions.add(new Vec3M(box.minX,box.minY,box.maxZ));
		
		positions.add(new Vec3M(box.maxX,box.minY,box.minZ));
		positions.add(new Vec3M(box.maxX,box.minY,box.maxZ));
		
		positions.add(new Vec3M(box.minX,box.maxY,box.minZ));
		positions.add(new Vec3M(box.minX,box.maxY,box.maxZ));
		
		positions.add(new Vec3M(box.maxX,box.maxY,box.minZ));
		positions.add(new Vec3M(box.maxX,box.maxY,box.maxZ));
		
		
		return positions;
	}
}
