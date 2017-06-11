package com.magiology.util.interf;

import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.vec.IVec3M;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface Locateable<PositionType>{
	
	public interface LocateableBlock extends Locateable<BlockPos>{}
	
	public interface LocateableBlockM extends Locateable<BlockPosM>{}
	
	public interface LocateableVec3M extends Locateable<IVec3M>{}
	
	public interface LocateableVec3D extends Locateable<Vec3d>{}
	
	PositionType getPos();
}
