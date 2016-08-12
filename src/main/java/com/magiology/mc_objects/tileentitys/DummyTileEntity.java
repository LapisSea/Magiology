package com.magiology.mc_objects.tileentitys;

import com.magiology.handlers.particle.Particles;
import com.magiology.util.m_extensions.TileEntityMTickable;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilM;

public class DummyTileEntity extends TileEntityMTickable{
	
	public DummyTileEntity(){
		
	}

	@Override
	public void update(){
		if(isRemote()&&UtilM.peridOf(this, 10)){
			Particles.MISTY_BUBBLE.spawn(new Vec3M(getPos()).add((3+RandUtil.RI(3)*5)/16F, 6/16F, (3+RandUtil.RI(3)*5)/16F), new Vec3M(0, 0, 0), 4/16F, 60, 0.001F, new ColorF(0.1, 0.3+RandUtil.RF(0.3), 0.5+RandUtil.RF(0.5), 0.5));
		}
	}
	
}