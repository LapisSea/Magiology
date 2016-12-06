package com.magiology.util.m_extensions;

import com.magiology.util.objs.vec.IVec3M;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class AxisAlignedBBM extends AxisAlignedBB{
	
	public AxisAlignedBBM(BlockPos pos){
		super(pos);
	}
	
	public AxisAlignedBBM(BlockPos pos1, BlockPos pos2){
		super(pos1, pos2);
	}

	public AxisAlignedBBM(Vec3d vec1, Vec3d vec2){
		super(vec1, vec2);
	}
	
	public AxisAlignedBBM(IVec3M vec1, IVec3M vec2){
		super(vec1.x(), vec1.y(), vec1.z(), vec2.x(), vec2.y(), vec2.z());
	}
	
	public AxisAlignedBBM(double x1, double y1, double z1, double x2, double y2, double z2){
		super(x1, y1, z1, x2, y2, z2);
	}
	
}
