package com.magiology.handlers.particle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;

public abstract class ParticleFactory<T extends IParticle>{
	
	
	protected static final int defultSpawnDistance=32;
	private List<T> particles=new ArrayList();
	private int id=-1;
	
	public final void setId(int id){
		if(id!=-1) return;
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
	
	public abstract void setUpOpenGl();
	
	public abstract void resetOpenGl();
	
	//particle handling 
	protected void addParticle(T particle){
		particles.add(particle);
	}
	
	public void clear(){
		particles.clear();
	}
	
	public void removeLast(){
		if(!particles.isEmpty()) particles.remove(0);
	}
	
	public int size(){
		return particles.size();
	}
	
	public void update(){
		Queue<IParticle> toRemove=new ArrayDeque<>();
		particles.forEach(particle->{
			particle.update();
			if(particle.isDead()) toRemove.add(particle);
		});
		particles.removeAll(toRemove);
	}
	
	public void render(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){
		if(!particles.isEmpty()){
			setUpOpenGl();
			if(hasStaticModel()){
				for(T particle:particles){
					OpenGLM.pushMatrix();
					particle.setUpOpenGl();
					for(int i:particle.getModelIds())GlStateManager.callList(i);
					OpenGLM.popMatrix();
				}
			}else{
				for(T particle:particles){
					OpenGLM.pushMatrix();
					particle.setUpOpenGl();
					particle.renderModel(xRotation, zRotation, yzRotation, xyRotation, xzRotation);
					OpenGLM.popMatrix();
				}
			}
			resetOpenGl();
		}
	}
	
	private int listId=-1;
	
	protected void startList(){
		if(listId!=-1) throw new StackOverflowError("List is already started!");
		listId=GLAllocation.generateDisplayLists(1);
		GlStateManager.glNewList(listId, 4864);
	}
	
	protected int endList(){
		if(listId==-1) throw new StackOverflowError("List already ended!");
		GlStateManager.glEndList();
		int id=listId;
		listId=-1;
		return id;
	}
	
	public abstract void compileDisplayList();
}
