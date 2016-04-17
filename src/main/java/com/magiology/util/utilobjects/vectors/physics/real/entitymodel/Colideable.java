package com.magiology.util.utilobjects.vectors.physics.real.entitymodel;

import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.real.AbstractRealPhysicsVec3F;

public interface Colideable{
	public void applyColideableMove(AbstractRealPhysicsVec3F vec);
	public Vec3M getModelOffset();
	/**
	 * returns double object with the first vector as position of the ray trace and the second as normal of the hit plane
	 * @param start
	 * @param end
	 * @return
	 */
	public DoubleObject<Vec3M, Vec3M> rayTrace(Vec3M start,Vec3M end);
	public void setModelOffset(Vec3M modelOffset);
}
