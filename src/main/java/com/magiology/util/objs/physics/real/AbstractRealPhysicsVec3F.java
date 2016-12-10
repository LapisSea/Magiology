package com.magiology.util.objs.physics.real;

import java.util.List;

import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.AngularVec3;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.physics.real.entitymodel.Colideable;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.PhysicsUtil;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.MathUtil;
import com.magiology.util.statics.math.MatrixUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class AbstractRealPhysicsVec3F{
	
	public AbstractRealPhysicsVec3F(World world, Vec3M pos){
		setWorld(world);
		setPos(pos);
		setPrevPos(pos);
		setLastPos(pos);
	}
	public void addPos(Vec3M add){
		setPos(getPos().add(add));
	}
	public void addVelocity(Vec3M add){
		setVelocity(getVelocity().add(add));
	}
	public void applyForce(Vec3M force){
		addVelocity(PhysicsUtil.getAcceleration(getMass(), force));
	}
	private void checkWorldClipping(Vec3M start, Vec3M end, List<Colideable> coliders){
		if(isWorldClipping()){
			if(getWillColideWithBlocks()){
				RayTraceResult hit=getWorld().rayTraceBlocks(start.conv(), end.conv());
				if(hit!=null&&hit.typeOfHit!=RayTraceResult.Type.MISS){
					IBlockState state=getWorld().getBlockState(hit.getBlockPos());
					Block block=state.getBlock();
					AxisAlignedBB bounding=block.getCollisionBoundingBox(state, getWorld(), hit.getBlockPos());
					if(bounding!=null){
						surfaceHit(new PairM<Vec3M, Vec3M>(new Vec3M(hit.hitVec), new Vec3M().offset(hit.sideHit)));
						Vec3M pos=getPos();
						if(bounding.isVecInside(pos.conv())){
							double 
								toMaxYDist=Math.abs(pos.y()-bounding.maxY),
								toMinYDist=Math.abs(pos.y()-bounding.minY),
								toMinXDist=Math.abs(pos.x()-bounding.minX),
								toMaxXDist=Math.abs(pos.x()-bounding.maxX),
								toMinZDist=Math.abs(pos.z()-bounding.minZ),
								toMaxZDist=Math.abs(pos.z()-bounding.maxZ),
								min=Math.min(toMaxYDist, Math.min(toMinYDist, Math.min(toMinXDist, Math.min(toMaxXDist, Math.min(toMinZDist, toMaxZDist)))));
							if(min==toMaxYDist)addPos(new Vec3M(0, toMaxYDist, 0));
							else if(min==toMinYDist)addPos(new Vec3M(0, -toMinYDist, 0));
							else if(min==toMinXDist)addPos(new Vec3M(0, -toMinXDist, 0));
							else if(min==toMaxXDist)addPos(new Vec3M(0, toMaxXDist, 0));
							else if(min==toMinZDist)addPos(new Vec3M(0, -toMinZDist, 0));
							else if(min==toMaxZDist)addPos(new Vec3M(0, toMaxZDist, 0));
							
						}
					}
				}
			}
			if(Math.abs(getStress())<0.1)for(Colideable c:coliders){
				PairM<Vec3M, Vec3M> result=c.rayTrace(start, end);
				if(result!=null){
					surfaceHit(result);
					return;
				}
			}
		}
	}
	public abstract float getAirBorneFriction();
	
	public abstract float getBounciness();
	
	public abstract Vec3M getLastPos();
	
	public abstract float getMass();
	public abstract Vec3M getPos();
	public abstract Vec3M getPrevPos();
	public abstract Vec3M getPrevVelocity();
	
	public abstract float getStress();
	
	public abstract float getSurfaceFriction();
	public abstract Vec3M getVelocity();
	public abstract boolean getWillColideWithBlocks();
	public abstract World getWorld();
	public abstract boolean isWorldClipping();
	public void move(List<Colideable> coliders){
		move(getVelocity(), coliders);
	}
	public void move(Vec3M move, List<Colideable> coliders){
		setLastPos(getPos());
		addPos(move);
		checkWorldClipping(getPos(), getLastPos(), coliders);
	}
	public void moveTo(Vec3M destination, List<Colideable> coliders){
		move(destination.sub(getPos()),coliders);
	}
	public void mulVelocity(float mul){
		mulVelocity(new Vec3M(mul, mul, mul));
	}
	public void mulVelocity(Vec3M mul){
		setVelocity(getVelocity().mul(mul));
	}
	public abstract void setAirBorneFriction(float airBorneFriction);
	public abstract void setBounciness(float bounciness);
	public abstract void setLastPos(Vec3M lastPos);
	public abstract void setMass(float weight);
	public abstract void setPos(Vec3M pos);
	public abstract void setPrevPos(Vec3M prevPos);
	public abstract void setPrevVelocity(Vec3M prevVelocity);
	public abstract void setStress(float stress);
	public abstract void setSurfaceFriction(float surfaceFriction);
	public abstract void setVelocity(Vec3M velocity);
	public abstract void setWillColideWithBlocks(boolean colide);
	public abstract void setWorld(World world);
	public abstract void setWorldClipping(boolean isWorldClipping);
	public void surfaceHit(PairM<Vec3M, Vec3M> result){
		
		Vec3M hitMul=result.obj2.mul(getBounciness()).abs();
		AngularVec3 hitMulAngle=new AngularVec3(hitMul);
		Vec3M velocityMulBase=new Vec3M(0, 0, -1).transform(MatrixUtil.createMatrixXY(hitMulAngle.getXRotation(), hitMulAngle.getYRotation()).finish());
		
		
		setPos(result.obj1.add(velocityMulBase.mul(0.01)));
		mulVelocity(velocityMulBase.mul(getSurfaceFriction()));
	}
	public void update(List<Colideable> coliders){

		setPrevPos(getPos());
		setPrevVelocity(getVelocity().copy());
		getVelocity().subSelfY(getMass());
		if(!MathUtil.isNumValid(getVelocity().x())){
			if(MathUtil.isNumValid(getPrevVelocity().x()))setVelocity(getPrevVelocity());
			else setVelocity(new Vec3M());
		}
		move(coliders);
		
		float mediaMul=1;
		Block block=UtilM.getBlock(getWorld(), new BlockPosM(getPos()));
		
		if(block instanceof BlockLiquid){
			mediaMul=Math.max(1, ((BlockLiquid)block).tickRate(getWorld())/2F);
		}
		
		mulVelocity(getAirBorneFriction()/mediaMul);
	}
	public void velocityTo(Vec3M pos){
		addVelocity(pos.sub(getPos()));
	}
}
