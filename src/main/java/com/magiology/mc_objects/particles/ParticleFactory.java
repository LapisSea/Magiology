package com.magiology.mc_objects.particles;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.magiology.handlers.particle.IParticle;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.UtilC;

public abstract class ParticleFactory<T extends IParticle>{
	
	protected static final int defultSpawnDistance=32;
	
	private List<T> particles=new ArrayList();
	private int id=-1;
	
	public final void setId(int id){
		if(id!=-1)return;
		this.id=id;
	}
	public int getID(){
		return id;
	}
	protected boolean shouldSpawn(Vec3M spawnPos){
		return ParticleHandler.instance.shouldSpawn(this, spawnPos);
	}
	protected double getDistance(Vec3M spawnPos){
		return spawnPos.distanceTo(UtilC.getViewEntity().getPositionVector());
	}
	
	public abstract float getSpawnDistanceInBlocks();
	public abstract boolean hasDistanceLimit();
	public abstract boolean hasStaticModel();
	
	
	
	//particle handling 
	protected void addParticle(T particle){
		particles.add(particle);
	}
	
	public void clear(){
		particles.clear();
	}
	
	public void removeLast(){
		if(!particles.isEmpty())particles.remove(0);
	}

	public int size(){
		return particles.size();
	}
	
	
	public void update(){
		Queue<IParticle> toRemove=new ArrayDeque<>();
		particles.forEach(particle->{
			particle.update();
			if(particle.isDead())toRemove.add(particle);
		});
		particles.removeAll(toRemove);
	}
	
	public void render(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){
//		if(!particles.isEmpty()){
//			VertexBuffer buff=OpenGLM.getWB();
//			T zeroParticle=particles.get(0);
//			zeroParticle.setUpOpenGl();
//			Renderer.PARTICLE.beginQuads();
//			if(hasStaticModel()){
//				Queue<PositionTextureVertex> model=zeroParticle.renderModel(xRotation,zRotation,yzRotation,xyRotation,xzRotation);
//				particles.forEach(particle->{
//					Vec3M pos=PartialTicksUtil.calculate(particle.getPrevPos(), particle.getPos());
//					ColorF color=particle.getColor();
//					Vec2i lightpos=particle.getLightPos(pos);
//					
//					buff.setTranslation(pos.x, pos.y, pos.z);
//					model.forEach(vert->Renderer.PARTICLE.addVertex(vert.vector3D, vert.texturePositionX, vert.texturePositionY, color, lightpos.x, lightpos.y));
//				});
//			}else{
//				
//			}
//			particles.forEach(particle->{
//				Vec3M pos=PartialTicksUtil.calculate(particle.getPrevPos(), particle.getPos());
//				ColorF color=particle.getColor();
//				Vec2i lightpos=particle.getLightPos(pos);
//				
//				buff.setTranslation(pos.x, pos.y, pos.z);
//				particle.renderModel(xRotation,zRotation,yzRotation,xyRotation,xzRotation).forEach(vert->Renderer.PARTICLE.addVertex(vert.vector3D, vert.texturePositionX, vert.texturePositionY, color, lightpos.x, lightpos.y));
//			});
//			Renderer.PARTICLE.draw();
//			zeroParticle.resetOpenGl();
//		}
		//TODO: fix this
	}
	
}