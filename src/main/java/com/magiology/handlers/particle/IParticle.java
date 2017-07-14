package com.magiology.handlers.particle;

import com.magiology.util.interf.Worldabale;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec2i;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.MathUtil;
import com.magiology.util.statics.math.PartialTicksUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class IParticle implements Worldabale{
	
	public IParticle(Vec3M pos){
		this(pos, new Vec3M());
	}
	
	public IParticle(Vec3M pos, Vec3M speed){
		setPosTo(pos);
		setSpeed(speed);
		setBoundingBoxFromPos();
	}
	
	public abstract boolean isDead();
	
	public abstract void kill();
	
	public abstract void onDeath();
	
	public abstract Vec3M getPos();
	
	public abstract Vec3M getPrevPos();
	
	public abstract Vec3M getSpeed();
	
	public abstract void setPos(Vec3M pos);
	
	public abstract void setPosX(double x);
	
	public abstract void setPosY(double y);
	
	public abstract void setPosZ(double z);
	
	public abstract void setPrevPos(Vec3M prevPos);
	
	public abstract void setSpeed(Vec3M speed);
	
	public abstract ColorM getColor();
	
	public abstract void setColor(ColorM color);
	
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
	
	public abstract ParticleFactory getFactorfy();
	
	@Override
	public World getWorld(){
		return UtilC.getTheWorld();
	}
	
	@Override
	public boolean isRemote(){
		return true;
	}
	
	@Override
	public boolean client(){
		return true;
	}
	
	@Override
	public boolean server(){
		return false;
	}
	
	public abstract void update();
	
	public void moveParticle(double xSpeed, double ySpeed, double zSpeed){
		moveParticle(new Vec3M(xSpeed, ySpeed, zSpeed));
	}
	
	public void moveParticle(Vec3M speed){
		if(noClip()){
			setBoundingBox(getBoundingBox().offset(speed.x(), speed.y(), speed.z()));
			setPosFromBoundingBox();
			return;
		}
		Vec3M originalSpeed=speed.copy();
		List<AxisAlignedBB> boundingBoxes=getWorld().getCollisionBoxes(null, getBoundingBox().contract(speed.x(), speed.y(), speed.z()));
		boundingBoxes.forEach(box->speed.setX(box.calculateXOffset(getBoundingBox(), speed.x())));
		setBoundingBox(getBoundingBox().offset(speed.x(), 0, 0));
		boundingBoxes.forEach(box->speed.setY(box.calculateYOffset(getBoundingBox(), speed.y())));
		setBoundingBox(getBoundingBox().offset(0, speed.y(), 0));
		boundingBoxes.forEach(box->speed.setZ(box.calculateZOffset(getBoundingBox(), speed.z())));
		setBoundingBox(getBoundingBox().offset(0, 0, speed.z()));
		setPosFromBoundingBox();
		final boolean xColided=originalSpeed.x()!=speed.x(), yColided=originalSpeed.y()!=speed.y(), zColided=originalSpeed.z()!=speed.z();
		setCollided(xColided||yColided||zColided);
		if(isCollided()){
			int x=0, y=0, z=0;
			if(xColided&&getSpeed().x()!=0) x=MathUtil.getNumPrefix(getSpeed().x());
			if(yColided&&getSpeed().y()!=0) y=MathUtil.getNumPrefix(getSpeed().y());
			if(zColided&&getSpeed().z()!=0) z=MathUtil.getNumPrefix(getSpeed().z());
			onCollided(new Vec3i(x, y, z));
		}
	}
	
	protected void setPosFromBoundingBox(){
		AxisAlignedBB box=getBoundingBox();
		setPos(new Vec3M((box.minX+box.maxX)/2, (box.minY+box.maxY)/2, (box.minZ+box.maxZ)/2));
	}
	
	public void setBoundingBoxFromPos(){
		Vec3M pos=getPos();
		float size=getSize()/2.0F;
		setBoundingBox(new AxisAlignedBB(pos.x()-size, pos.y()-size, pos.z()-size, pos.x()+size, pos.y()+size, pos.z()+size));
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
	
	public abstract int getModelId();
	
	@Override
	public String toString(){
		return getClass().getSimpleName();
	}
	
	public void pushOutOfBlocks(){
		float growth=getSize()-getPrevSize();
		if(growth<=0) return;
		growth/=2;
		
		//get world intersection
		List<AxisAlignedBB> boundingBoxes=getWorld().getCollisionBoxes(null, getBoundingBox());
		//exit if nothing to process
		if(boundingBoxes.isEmpty()) return;
		
		Vec3M pos=getPos();
		
		//pos = position of the entity and center position of bounding box
		
		AxisAlignedBB bb=getBoundingBox();
		
		//how much the bounding box has grown on a side
		
		//total movement for bounding box to exit any colliding boxes
		Vec3M push=new Vec3M();
		
		if(boundingBoxes.size()==1){
			
			AxisAlignedBB box=boundingBoxes.get(0);
			
			if((box.minX+box.maxX)/2<pos.x()) push.setX(box.maxX-bb.minX+growth);
			else push.setX(box.maxX-bb.minX-growth);
			
			if((box.minY+box.maxY)/2<pos.y()) push.setY(box.maxY-bb.minY+growth);
			else push.setY(box.minY-bb.maxY-growth);
			
			if((box.minZ+box.maxZ)/2<pos.z()) push.setZ(box.maxZ-bb.minZ+growth);
			else push.setZ(box.minZ-bb.maxZ-growth);
			
		}else for(AxisAlignedBB box : boundingBoxes){
			
			if((box.minX+box.maxX)/2<pos.x()) push.setX(Math.max(push.x(), box.maxX-bb.minX+growth));
			else push.setX(Math.min(push.x(), box.maxX-bb.minX-growth));
			
			if((box.minY+box.maxY)/2<pos.y()) push.setY(Math.max(push.y(), box.maxY-bb.minY+growth));
			else push.setY(Math.min(push.y(), box.minY-bb.maxY-growth));
			
			if((box.minZ+box.maxZ)/2<pos.z()) push.setZ(Math.max(push.z(), box.maxZ-bb.minZ+growth));
			else push.setZ(Math.min(push.z(), box.minZ-bb.maxZ-growth));
			
		}
		
		//absolute vector of push used to determine that plane should be used to minimize distance pushed
		Vec3M absPush=push.abs();
		
		if(absPush.x()<absPush.y()&&absPush.x()<absPush.z()){
			setPosX(pos.x()+push.x()*1.01);
			return;
		}
		if(absPush.y()<absPush.x()&&absPush.y()<absPush.z()){
			setPosY(pos.y()+push.y()*1.01);
			return;
		}
		if(absPush.z()<absPush.x()&&absPush.z()<absPush.x()){
			setPosZ(pos.z()+push.z()*1.01);
		}
	}
	
	protected void transformSimpleParticleColored(){
		transformSimpleParticle();
		OpenGLM.color(getColor());
	}
	
	protected void transformSimpleParticle(){
		EntityPlayer player=UtilC.getThePlayer();
		
		OpenGLM.translate(PartialTicksUtil.calculate(this));
		GlStateManager.rotate(-player.rotationYaw+90, 0, 1, 0);
		GlStateManager.rotate(player.rotationPitch, 0, 0, 1);
		OpenGLM.scale(PartialTicksUtil.calculate(getPrevSize(), getSize()));
	}
}
