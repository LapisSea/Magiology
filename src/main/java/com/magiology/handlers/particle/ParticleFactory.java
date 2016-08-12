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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ParticleFactory{
	
	@SideOnly(Side.CLIENT)
	protected static final int defultSpawnDistance=32;
	@SideOnly(Side.CLIENT)
	private List<IParticle> particles=new ArrayList();
	@SideOnly(Side.CLIENT)
	private int id=-1;

	@SideOnly(Side.CLIENT)
	public final void setId(int id){
		if(id!=-1) return;
		this.id=id;
	}

	@SideOnly(Side.CLIENT)
	public int getID(){
		return id;
	}

	@SideOnly(Side.CLIENT)
	protected boolean shouldSpawn(Vec3M spawnPos){
		return ParticleHandler.instance.shouldSpawn(this, spawnPos);
	}

	@SideOnly(Side.CLIENT)
	protected double getDistance(Vec3M spawnPos){
		return spawnPos.distanceTo(UtilC.getViewEntity().getPositionVector());
	}

	@SideOnly(Side.CLIENT)
	public abstract float getSpawnDistanceInBlocks();

	@SideOnly(Side.CLIENT)
	public abstract boolean hasDistanceLimit();

	@SideOnly(Side.CLIENT)
	public abstract boolean hasStaticModel();

	@SideOnly(Side.CLIENT)
	public abstract void setUpOpenGl();

	@SideOnly(Side.CLIENT)
	public abstract void resetOpenGl();
	
	//particle handling 
	@SideOnly(Side.CLIENT)
	protected void addParticle(IParticle particle){
		particles.add(particle);
	}

	@SideOnly(Side.CLIENT)
	public void clear(){
		particles.clear();
	}

	@SideOnly(Side.CLIENT)
	public void removeLast(){
		if(!particles.isEmpty()) particles.remove(0);
	}

	@SideOnly(Side.CLIENT)
	public int size(){
		return particles.size();
	}

	@SideOnly(Side.CLIENT)
	public void update(){
		Queue<IParticle> toRemove=new ArrayDeque<>();
		particles.forEach(particle->{
			particle.update();
			if(particle.isDead()) toRemove.add(particle);
		});
		particles.removeAll(toRemove);
	}

	@SideOnly(Side.CLIENT)
	public void render(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){
		if(!particles.isEmpty()){
			setUpOpenGl();
			if(hasStaticModel()){
				for(IParticle particle:particles){
					OpenGLM.pushMatrix();
					particle.setUpOpenGl();
					int[] ids=particle.getModelIds();
					if(ids==null)GlStateManager.callList(particle.getModelId());
					else for(int i:ids)GlStateManager.callList(i);
					OpenGLM.popMatrix();
				}
			}else{
				for(IParticle particle:particles){
					OpenGLM.pushMatrix();
					particle.setUpOpenGl();
					particle.renderModel(xRotation, zRotation, yzRotation, xyRotation, xzRotation);
					OpenGLM.popMatrix();
				}
			}
			resetOpenGl();
		}
	}

	@SideOnly(Side.CLIENT)
	private int listId=-1;

	@SideOnly(Side.CLIENT)
	protected void startList(){
		if(listId!=-1) throw new StackOverflowError("List is already started!");
		listId=GLAllocation.generateDisplayLists(1);
		GlStateManager.glNewList(listId, 4864);
	}

	@SideOnly(Side.CLIENT)
	protected int endList(){
		if(listId==-1) throw new StackOverflowError("List already ended!");
		GlStateManager.glEndList();
		int id=listId;
		listId=-1;
		return id;
	}

	@SideOnly(Side.CLIENT)
	public abstract void compileDisplayList();
}