package com.magiology.mc_objects.features.dimension_stabiliser;

import com.magiology.core.registry.init.ParticlesM;
import com.magiology.util.m_extensions.TileEntityMTickable;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilM;

public class TileEntityDimensionStabiliser extends TileEntityMTickable{
	
	public TileEntityDimensionStabiliser(){
		
	}
	
	@Override
	public void update(){
		if(isRemote()&&UtilM.peridOf(this, 5)&&RandUtil.RB(4)){
			ParticlesM.MIST_BUBBLE.spawn(new Vec3M(getPos()).add((3+RandUtil.RI(3)*5)/16F, 7/16F, (3+RandUtil.RI(3)*5)/16F), new Vec3M(0, 0, 0), 3/16F, 60, 0.001F, new ColorM(0.1, 0.3+RandUtil.RF(0.3), 0.5+RandUtil.RF(0.5), 0.5));
			//			Particles.MISTY_BUBBLE.spawn(new Vec3M(getPos()).add(0.5, 1, 0.5), new Vec3M(0, 0, 0), 1, 60, 0, new ColorF(0.1, 0.3+RandUtil.RF(0.3), 0.5+RandUtil.RF(0.5), 0.5));
		}
		
	}
	
}
