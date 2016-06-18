package com.magiology.mc_objects.particles;

import java.util.Queue;

import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleMistBubble extends ParticleM{
	
	protected float lifeTime,originalSize,originalAlpha;
	
	protected ParticleMistBubble(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
		super(pos, speed);
		setSizeTo(0);
		this.lifeTime=lifeTime;
		setGravity(gravity);
		setColor(color);
		originalSize=size;
		originalAlpha=color.a;
	}
	
	@Override
	public void update(){
		super.update();
		float mul=1-Math.abs((getParticleAge()*2-lifeTime)/lifeTime);
		setSize(originalSize*(float)Math.sqrt(mul));
		getColor().a=mul*2*originalAlpha;
		if(getParticleAge()>lifeTime)kill();
	}
	
	@Override
	public void onCollided(Vec3i direction){
		super.onCollided(direction);
	}
	
	@Override
	public void setUpOpenGl(){
//		OpenGLM.bindTexture(Textures.SmoothBuble1);
//		GL11U.blendFunc(2);
		//TODO: fix this shit
	}
	@Override
	public Queue<PositionTextureVertex> renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){
		return super.renderModel(xRotation, zRotation, yzRotation, xyRotation, xzRotation);
	}
	@Override
	public void resetOpenGl(){
//		GL11U.blendFunc(1);
	}
}
