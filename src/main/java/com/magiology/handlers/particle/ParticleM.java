package com.magiology.handlers.particle;

import java.util.ArrayDeque;
import java.util.Queue;

import com.magiology.util.objs.BlockPosM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.QuadUV;
import com.magiology.util.objs.Vec2i;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ParticleM extends IParticle{
	private boolean isDead=false,colided=false,noClip=false;
	private Vec3M pos=new Vec3M(),prevPos=new Vec3M(),speed=new Vec3M(),gravity=new Vec3M();
	private ColorF color=new ColorF();
	private float size=0.6F,prevSize=0.6F;
	private AxisAlignedBB box;
	private int particleAge=0;
	
	private static final QuadUV allUV=new QuadUV(0,0, 1,0, 1,1, 0,1);
	
	public float friction=1;
	
	public ParticleM(Vec3M pos){
		this(pos, new Vec3M());
	}
	public ParticleM(Vec3M pos,Vec3M speed){
		super(pos,speed);
		this.pos=prevPos=pos;
		setSpeed(speed);
	}
	
	@Override
	public boolean isDead(){
		return isDead;
	}

	@Override
	public void kill(){
		isDead=true;
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
	public Vec3M getSpeed(){
		return speed;
	}

	@Override
	public void setPos(Vec3M pos){
		this.pos=pos;
		setBoundingBoxFromPos();
	}

	@Override
	public void setPrevPos(Vec3M prevPos){
		this.prevPos=prevPos;
	}

	@Override
	public void setSpeed(Vec3M speed){
		this.speed=speed;
	}

	@Override
	public ColorF getColor(){
		return color;
	}

	@Override
	public void setColor(ColorF color){
		this.color=color;
	}

	@Override
	public boolean isCollided(){
		return colided;
	}

	@Override
	public void setCollided(boolean isColided){
		colided=isColided;
	}

	@Override
	public boolean noClip(){
		return noClip;
	}

	@Override
	public void setNoClip(boolean noClip){
		this.noClip=noClip;
	}

	@Override
	public float getSize(){
		return size;
	}
	
	@Override
	public void setSize(float size){
		if(this.size!=size){
			this.size=Math.max(0, size);
			Vec3M pos=getPos().sub(size/2);
			this.setBoundingBox(new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x+size, pos.y+size, pos.z+size));
		}
	}
	@Override
	public float getPrevSize(){
		return prevSize;
	}

	@Override
	public void setPrevSize(float prevSize){
		this.prevSize=prevSize;
	}

	@Override
	public void onCollided(Vec3i direction){
		
	}

	@Override
	public AxisAlignedBB getBoundingBox(){
		return box;
	}

	@Override
	public void setBoundingBox(AxisAlignedBB box){
		this.box=box;
	}
	
	@Override
	public Vec2i getLightPos(Vec3M pos){
		BlockPos blockpos=new BlockPosM(getPos());
		int i=getWorld().isBlockLoaded(blockpos)?getWorld().getCombinedLight(blockpos, 0):0;
		return new Vec2i(i >> 16 & 65535, i & 65535);
	}

	@Override
	public Queue<PositionTextureVertex> renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){
		float renderSize=PartialTicksUtil.calculate(getPrevSize(), getSize());
		return renderStandardModel(xRotation, zRotation, yzRotation, xyRotation, xzRotation, renderSize);
	}
	protected Queue<PositionTextureVertex> renderStandardModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation, float size){
		return renderStandardModel(xRotation, zRotation, yzRotation, xyRotation, xzRotation, size, allUV);
	}
	protected Queue<PositionTextureVertex> renderStandardModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation, float size, QuadUV uv){
		Queue<PositionTextureVertex> data=new ArrayDeque<>();
		data.add(new PositionTextureVertex(-xRotation*size-xyRotation*size, -zRotation*size, -yzRotation*size-xzRotation*size, uv.x1, uv.y1));
		data.add(new PositionTextureVertex(-xRotation*size+xyRotation*size,  zRotation*size, -yzRotation*size+xzRotation*size, uv.x2, uv.y2));
		data.add(new PositionTextureVertex( xRotation*size+xyRotation*size,  zRotation*size,  yzRotation*size+xzRotation*size, uv.x3, uv.y3));
		data.add(new PositionTextureVertex( xRotation*size-xyRotation*size, -zRotation*size,  yzRotation*size-xzRotation*size, uv.x4, uv.y4));
		return data;
	}
	protected Queue<PositionTextureVertex> renderStandardModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation, float size, QuadUV... textureUVs){
		Queue<PositionTextureVertex> data=new ArrayDeque<>();
		Vec3M pos1=new Vec3M(-xRotation*size-xyRotation*size, -zRotation*size, -yzRotation*size-xzRotation*size);
		Vec3M pos2=new Vec3M(-xRotation*size+xyRotation*size,  zRotation*size, -yzRotation*size+xzRotation*size);
		Vec3M pos3=new Vec3M( xRotation*size+xyRotation*size,  zRotation*size,  yzRotation*size+xzRotation*size);
		Vec3M pos4=new Vec3M( xRotation*size-xyRotation*size, -zRotation*size,  yzRotation*size-xzRotation*size);
		
		for(QuadUV uv:textureUVs){
			data.add(new PositionTextureVertex(pos1.conv(), uv.x1, uv.y1));
			data.add(new PositionTextureVertex(pos2.conv(), uv.x2, uv.y2));
			data.add(new PositionTextureVertex(pos3.conv(), uv.x3, uv.y3));
			data.add(new PositionTextureVertex(pos4.conv(), uv.x4, uv.y4));
		}
		
		return data;
	}
	
	@Override
	public int getParticleAge(){
		return particleAge;
	}
	@Override
	public void setParticleAge(int age){
		particleAge=age;
	}
	@Override
	public void addParticleAge(int ageAdd){
		particleAge+=ageAdd;
	}
	@Override
	public void addParticleAge(){
		addParticleAge(1);
	}
	public Vec3M getGravity(){
		return gravity;
	}
	public void setGravity(Vec3M grav){
		gravity=grav;
	}
	
	@Override
	public void update(){
		updatePrev();
		addParticleAge();
//		PrintUtil.println(getSpeed());
		setSpeed(getSpeed().mul(friction).add(getGravity()));
		this.moveEntity(getSpeed());
	}
}
