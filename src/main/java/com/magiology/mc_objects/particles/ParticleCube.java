package com.magiology.mc_objects.particles;

import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleCube extends ParticleM{
	
	protected float lifeTime,originalSize,originalAlpha;
	protected Vec3M rotation=new Vec3M(RandUtil.RF(90),RandUtil.RF(90),RandUtil.RF(90)),prevRotation=new Vec3M(),rotationSpeed=new Vec3M(RandUtil.CRF(4),RandUtil.CRF(4),RandUtil.CRF(4));
	
	
	protected ParticleCube(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
		super(pos, speed);
		setSizeTo(0);
		this.lifeTime=lifeTime;
		setGravity(gravity);
		setColor(color);
		getColor().a=1;
		originalSize=size;
		originalAlpha=1;
	}
	
	@Override
	public void update(){
		super.update();
		float mul=1-Math.abs((getParticleAge()*2-lifeTime)/lifeTime);
		setSize(originalSize*(float)Math.sqrt(mul));
		prevRotation.set(rotation);
		if(!isCollided()){
			rotationSpeed=rotationSpeed.add(RandUtil.CRF(1)*getSpeed().x*120,RandUtil.CRF(1)*getSpeed().y*120,RandUtil.CRF(1)*getSpeed().z*120);
			rotation.x+=rotationSpeed.x;
			rotation.y+=rotationSpeed.y;
			rotation.y+=rotationSpeed.z;
		}
		if(getParticleAge()>lifeTime){
			kill();
			return;
		}
		moveEntity(getSpeed());
		pushOutOfBlocks();
	}
	
	@Override
	public void onCollided(Vec3i direction){
//		PrintUtil.println(getSpeed());
		if(direction.getX()!=0)getSpeed().x*=-1;
		if(direction.getY()!=0)getSpeed().y*=-1;
		if(direction.getZ()!=0)getSpeed().z*=-1;
		
		rotationSpeed=rotationSpeed.mul(-0.3);
		rotation.x=(rotation.x*2+Math.round(rotation.x/90)*90)/3;
		rotation.y=(rotation.y*2+Math.round(rotation.y/90)*90)/3;
		rotation.z=(rotation.z*2+Math.round(rotation.z/90)*90)/3;
//		PrintUtil.println(getSpeed());
//		PrintUtil.println(direction);
//		super.onCollided(direction);
	}
	
	@Override
	public void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){}

	@Override
	public void setUpOpenGl(){
		
		OpenGLM.translate(PartialTicksUtil.calculate(this));
		OpenGLM.rotate(PartialTicksUtil.calculate(prevRotation, rotation));
		OpenGLM.scale(PartialTicksUtil.calculate(getPrevSize(), getSize()));
		
		OpenGLM.color(getColor());
	}

	@Override
	public int[] getModelIds(){
		return ParticleCubeFactory.defultModel;
	}
}