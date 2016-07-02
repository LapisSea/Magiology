package com.magiology.handlers.particle;

import java.util.List;

import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec2i;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.MathUtil;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class IParticle{
	
	
	public IParticle(Vec3M pos){
		this(pos,new Vec3M());
	}
	
	public IParticle(Vec3M pos,Vec3M speed){
		setPosTo(pos);
		setSpeed(speed);
		setBoundingBoxFromPos();
	}
	
	public abstract boolean isDead();
	
	public abstract void kill();
	
	public abstract Vec3M getPos();
	
	public abstract Vec3M getPrevPos();
	
	public abstract Vec3M getSpeed();
	
	public abstract void setPos(Vec3M pos);
	
	public abstract void setPrevPos(Vec3M prevPos);
	
	public abstract void setSpeed(Vec3M speed);
	
	public abstract ColorF getColor();
	
	public abstract void setColor(ColorF color);
	
	public abstract boolean isCollided();
	
	public abstract void setCollided(boolean isColided);
	
	public abstract boolean noClip();
	
	public abstract void setNoClip(boolean noClip);
	
	public abstract float getSize();
	
	public abstract void setSize(float size);
	
	public abstract float getPrevSize();
	
	public abstract void setPrevSize(float prevSize);
	
	public abstract void onCollided(Vec3i direction);
	
	public abstract AxisAlignedBB getBoundingBox();
	
	public abstract void setBoundingBox(AxisAlignedBB box);
	
	public abstract int getParticleAge();
	
	public abstract void setParticleAge(int age);
	
	public abstract void addParticleAge(int ageAdd);
	
	public abstract void addParticleAge();
	
	protected World getWorld(){
		return UtilC.getTheWorld();
	}
	
	public abstract void update();
	
	public void moveEntity(double xSpeed, double ySpeed, double zSpeed){
		moveEntity(new Vec3M(xSpeed,ySpeed,zSpeed));
	}
	
	public void moveEntity(Vec3M speed){
		if(noClip()){
			setBoundingBox(getBoundingBox().offset(speed.x,speed.y,speed.z));
			setPosFromBoundingBox();
			return;
		}
		Vec3M originalSpeed=speed.copy();
		List<AxisAlignedBB> boundingBoxes=getWorld().getCubes((Entity)null,getBoundingBox().addCoord(speed.x,speed.y,speed.z));
		boundingBoxes.forEach(box->speed.x=box.calculateXOffset(getBoundingBox(),speed.x));
		setBoundingBox(getBoundingBox().offset(speed.x,0.0D,0.0D));
		boundingBoxes.forEach(box->speed.y=box.calculateYOffset(getBoundingBox(),speed.y));
		setBoundingBox(getBoundingBox().offset(0.0D,speed.y,0.0D));
		boundingBoxes.forEach(box->speed.z=box.calculateZOffset(getBoundingBox(),speed.z));
		setBoundingBox(getBoundingBox().offset(0.0D,0.0D,speed.z));
		setPosFromBoundingBox();
		boolean xColided=originalSpeed.x!=speed.x,yColided=originalSpeed.y!=speed.y,zColided=originalSpeed.z!=speed.z;
		setCollided(xColided||yColided||zColided);
		if(isCollided()){
			int x=0,y=0,z=0;
			if(xColided){
				if(getSpeed().x!=0) x=MathUtil.getNumPrefix(getSpeed().x);
				getSpeed().x=0;
			}
			if(yColided){
				if(getSpeed().y!=0) y=MathUtil.getNumPrefix(getSpeed().y);
				getSpeed().y=0;
			}
			if(zColided){
				if(getSpeed().z!=0) z=MathUtil.getNumPrefix(getSpeed().z);
				getSpeed().z=0;
			}
			onCollided(new Vec3i(x,y,z));
		}
	}
	
	protected void setPosFromBoundingBox(){
		AxisAlignedBB box=getBoundingBox();
		setPos(new Vec3M((box.minX+box.maxX)/2,(box.minY+box.maxY)/2,(box.minZ+box.maxZ)/2));
	}
	
	public void setBoundingBoxFromPos(){
		Vec3M pos=getPos();
		float size=getSize()/2.0F;
		setBoundingBox(new AxisAlignedBB(pos.x-size,pos.y-size,pos.z-size,pos.x+size,pos.y+size,pos.z+size));
	}
	
	public void setPosTo(Vec3M pos){
		setPrevPos(pos);
		setPos(pos);
	}
	
	public void setSizeTo(float size){
		setSize(size);
		setPrevSize(size);
	}
	
	public void updatePrev(){
		setPrevPos(getPos());
		setPrevSize(getSize());
	}
	
	//REDERING
	public abstract Vec2i getLightPos(Vec3M pos);
	
	public abstract void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation);
	
	public abstract void setUpOpenGl();
	
	public abstract int[] getModelIds();
	
	@Override
	public String toString(){
		return getClass().getSimpleName();
	}
}
