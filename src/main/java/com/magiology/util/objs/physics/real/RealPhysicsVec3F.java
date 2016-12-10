package com.magiology.util.objs.physics.real;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.math.MathUtil;

import net.minecraft.world.World;

public class RealPhysicsVec3F extends AbstractRealPhysicsVec3F{
	
	private boolean isWorldClipping=true,willColideWithBlocks=true;
	private Vec3M pos,prevPos=new Vec3M(),velocity=new Vec3M(),prevVelocity=new Vec3M(),lastPos=new Vec3M(),posBackup=new Vec3M();
	private List<Float> stress;
	private float weight=0.1F,airBorneFriction=0.95F,surfaceFriction=0.95F,bounciness=0;
	private World world;
	
	
	public RealPhysicsVec3F(World world, Vec3M pos){
		super(world, pos);
		if(this.stress==null){
			this.stress=new ArrayList<>();
			for(int i=0;i<5;i++)this.stress.add(0F);
		}
	}
	
	@Override
	public float getAirBorneFriction(){
		return airBorneFriction;
	}
	@Override
	public float getBounciness(){
		return bounciness;
	}
	@Override
	public Vec3M getLastPos(){
		return lastPos;
	}
	@Override
	public float getMass(){
		return weight;
	}
	@Override
	public Vec3M getPos(){
		return pos;
	}
	@Override
	public Vec3M getPrevPos(){
		return prevPos;
	}
	@Override
	public Vec3M getPrevVelocity(){
		return prevVelocity;
	}
	@Override
	public float getStress(){
		if(this.stress==null){
			this.stress=new ArrayList<>();
			for(int i=0;i<5;i++)this.stress.add(0F);
			return 0;
		}
		float sum=0;
		for(Float f:stress)sum+=f;
//		if(Double.isInfinite(sum/stress.size()))PrintUtil.println(sum,stress.size());
		return sum/stress.size();
	}
	@Override
	public float getSurfaceFriction(){
		return surfaceFriction;
	}
	@Override
	public Vec3M getVelocity(){
		return velocity;
	}
	@Override
	public boolean getWillColideWithBlocks(){
		return willColideWithBlocks;
	}
	@Override
	public World getWorld(){
		return world;
	}
	@Override
	public boolean isWorldClipping(){
		return isWorldClipping;
	}
	@Override
	public void setAirBorneFriction(float airBorneFriction){
		this.airBorneFriction=airBorneFriction;
	}
	@Override
	public void setBounciness(float bounciness){
		this.bounciness=bounciness;
	}
	@Override
	public void setLastPos(Vec3M lastPos){
		this.lastPos=lastPos;
	}
	@Override
	public void setMass(float weight){
		this.weight=weight;
	}
	@Override
	public void setPos(Vec3M pos){
		if(MathUtil.isNumValid(pos.x()))this.pos=pos;
		if(!MathUtil.isNumValid(this.pos.x()))this.pos=posBackup;
		else posBackup=this.pos;
	}
	@Override
	public void setPrevPos(Vec3M prevPos){
		this.prevPos=prevPos;
	}
	@Override
	public void setPrevVelocity(Vec3M prevVelocity){
		this.prevVelocity=prevVelocity;
	}
	@Override
	public void setStress(float stress){
		if(this.stress==null){
			this.stress=new ArrayList<>();
			for(int i=0;i<5;i++)this.stress.add(0F);
		}
		if(!MathUtil.isNumValid(stress))return;
		this.stress.remove(0);
		this.stress.add(stress);
	}
	@Override
	public void setSurfaceFriction(float surfaceFriction){
		this.surfaceFriction=surfaceFriction;
	}
	@Override
	public void setVelocity(Vec3M velocity){
		if(MathUtil.isNumValid(velocity.x()))this.velocity=velocity;
	}
	@Override
	public void setWillColideWithBlocks(boolean colide){
		willColideWithBlocks=colide;
	}

	@Override
	public void setWorld(World world){
		this.world=world;
	}

	@Override
	public void setWorldClipping(boolean isWorldClipping){
		this.isWorldClipping=isWorldClipping;
	}
}
