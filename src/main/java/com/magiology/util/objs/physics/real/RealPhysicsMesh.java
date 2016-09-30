package com.magiology.util.objs.physics.real;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.magiology.util.interf.ObjectProcessor;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.physics.real.entitymodel.Colideable;
import com.magiology.util.objs.vec.*;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.math.MathUtil;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class RealPhysicsMesh{
	
	public static enum MaterialStrategy{
		NO_INTERACTION(          new ObjectProcessor<Boolean>(){@Override public Boolean process(Boolean object, Object... objects){return false;}},false),
		ONLY_BIGGER_SUPPRESSING( new ObjectProcessor<Boolean>(){@Override public Boolean process(Boolean object, Object... objects){return (Float)objects[0]>0;}},true),
		ONLY_SMALLER_SUPPRESSING(new ObjectProcessor<Boolean>(){@Override public Boolean process(Boolean object, Object... objects){return (Float)objects[0]<0;}},true),
		STATIC_DISTANCE(         new ObjectProcessor<Boolean>(){@Override public Boolean process(Boolean object, Object... objects){return true;}},true);
		
		private final ObjectProcessor<Boolean> check;
		public final boolean shouldCheck;
		
		private MaterialStrategy(ObjectProcessor<Boolean> check,boolean shouldCheck){
			this.check=check;
			this.shouldCheck=shouldCheck;
		}
		
		public boolean shouldInteract(float difference){
			return check.process(false, difference);
		}
	}
	
	public class RealPhysicsMeshHook{
		
		private ObjectProcessor<Vec3M> hook;
		private int vertexID;
		
		private RealPhysicsMeshHook(int vertexID, ObjectProcessor<Vec3M> hook){
			this.vertexID=vertexID;
			this.hook=hook;
		}
		
		private PairM<Vec3M, Integer> getHook(){
			return new PairM<Vec3M, Integer>(hook.process(vertices.get(vertexID).getPos(), RealPhysicsMesh.this,this,vertexID), vertexID);
		}
	}
	public static PairM<List<Vec3M>, List<Vec2i>> create2DPlane(int xCubes, int yCubes ,float xSize, float ySize){
		List<Vec3M> vertices=new ArrayList<>();
		List<Vec2i> indices=new ArrayList<>();
		
		int 
			xPoints=xCubes+1,
			yPoints=yCubes+1;
		
		for(int i=0;i<yPoints;i++)for(int j=0;j<xPoints;j++)vertices.add(new Vec3M(xSize*j,ySize*i,0));
		for(int i=0;i<yCubes;i++){
			for(int j=0;j<xCubes;j++){
				int 
					pos1=0,        pos2=1,
					pos3=xPoints,pos4=xPoints+1;
				
				pos1+=xPoints*i+j;
				pos2+=xPoints*i+j;
				pos3+=xPoints*i+j;
				pos4+=xPoints*i+j;
				
				indices.add(new Vec2i(pos1, pos2));
				indices.add(new Vec2i(pos2, pos4));
				indices.add(new Vec2i(pos4, pos3));
				indices.add(new Vec2i(pos3, pos1));
				
				indices.add(new Vec2i(pos1, pos4));
				indices.add(new Vec2i(pos2, pos3));
			}
		}
		
		return new PairM<List<Vec3M>, List<Vec2i>>(vertices, indices);
	}
	public List<Colideable> coliders=new ArrayList<>();
	private boolean criticalState=false;
	private int criticalStress=0;
	private List<Vec2i> indices=new ArrayList<>();
	private MaterialStrategy interactStrategy=MaterialStrategy.STATIC_DISTANCE;
	private float originalDistances[],noise=0;
	
	public Vec3M originPos=new Vec3M();
	
	private List<RealPhysicsMeshHook> physicsHooks=new ArrayList<>();
	
	private List<AbstractRealPhysicsVec3F> vertices=new ArrayList<>();
	private World world;
	
	public RealPhysicsMesh(final World world, final List<Vec3M> vertices, final List<Vec2i> indices, final AbstractRealPhysicsVec3F example){
		this.world=world;
		vertices.forEach(pos->{
			RealPhysicsVec3F vec3f=new RealPhysicsVec3F(world, pos);
			vec3f.setAirBorneFriction(example.getAirBorneFriction());
			vec3f.setBounciness(example.getBounciness());
			vec3f.setMass(example.getMass());
			vec3f.addPos(example.getPos());
			vec3f.setPrevPos(vec3f.getPos());
			vec3f.setLastPos(vec3f.getPos());
			vec3f.setPrevVelocity(example.getPrevVelocity());
			vec3f.setSurfaceFriction(example.getSurfaceFriction());
			vec3f.setVelocity(example.getVelocity());
			vec3f.setWorldClipping(example.isWorldClipping());
			vec3f.setWillColideWithBlocks(example.getWillColideWithBlocks());
			this.vertices.add(vec3f);
		});
		this.indices=indices;
		originalDistances=new float[indices.size()];
		for(int i=0;i<originalDistances.length;i++){
			Vec2i connection=indices.get(i);
			originalDistances[i]=(float)vertices.get(connection.x).distanceTo(vertices.get(connection.y));
		}
	}
	
	public RealPhysicsMesh(final World world, final List<Vec3M> vertices, final List<Vec2i> indices, final AbstractRealPhysicsVec3F example,MaterialStrategy interactStrategy){
		this(world, vertices, indices, example);
		setInteractStrategy(interactStrategy);
	}
	
	public void addWorldHook(ObjectProcessor<Vec3M> hook, int...vertexIDs){
		for(int i:vertexIDs)addWorldHook(hook, i);
	}
	public void addWorldHook(ObjectProcessor<Vec3M> hook, int vertexID){
		final RealPhysicsMeshHook newHook=new RealPhysicsMeshHook(vertexID, hook);
		physicsHooks.removeIf(hook0->hook0.vertexID==vertexID);
		physicsHooks.add(newHook);
	}
	public List<Vec2i> getIndices(){
		return indices;
	}
	
	public MaterialStrategy getInteractStrategy(){
		return interactStrategy;
	}
	
	public List<AbstractRealPhysicsVec3F> getVertices(){
		return vertices;
	}

	public World getWorld(){
		return world;
	}

	public boolean isCritical(){
		return criticalState;
	}

	public boolean isVertexHooked(int id){
		for(RealPhysicsMeshHook hook:physicsHooks)if(hook.vertexID==id)return true;
		return false;
	}

	public RealPhysicsMeshHook remove(int hookID){
		return physicsHooks.remove(hookID);
	}

	public void setIndices(List<Vec2i> indices){
		this.indices=indices;
	}

	public void setInteractStrategy(MaterialStrategy interactStrategy){
		this.interactStrategy=interactStrategy==null?MaterialStrategy.NO_INTERACTION:interactStrategy;
	}

	public void setVertices(List<AbstractRealPhysicsVec3F> vertices){
		this.vertices=vertices;
	}
	public void setWorld(World world){
		this.world=world;
	}
	
	public void update(){
		if(world!=null){
			float mul=world.isThundering()?0.1F:world.isRaining()?0.05F:0;
			if(mul>0){
				BlockPosM pos=new BlockPosM(originPos);
				Biome chunk=world.getChunkFromBlockCoords(pos).getBiome(pos, world.getBiomeProvider());
				
				if(world.canBlockSeeSky(pos))noise=mul*chunk.getRainfall();
				
			}
			else noise=0;
		}else noise=0;
		coliders.forEach(c->c.setModelOffset(originPos));
		if(noise>0)for(AbstractRealPhysicsVec3F v:vertices){
			v.addVelocity(new Vec3M(RandUtil.CRF(noise), RandUtil.CRF(noise), RandUtil.CRF(noise)));
		}
		List<Vec3M> hooksPos=new ArrayList<>();
		for(RealPhysicsMeshHook hook:physicsHooks){
			PairM<Vec3M, Integer> result=hook.getHook();
			AbstractRealPhysicsVec3F vertex=vertices.get(result.obj2);
			hooksPos.add(result.obj1);
			vertex.setPos(result.obj1);
			vertex.setVelocity(new Vec3M());
		}
		
		if(interactStrategy.shouldCheck){
			boolean criticalStressReached=false;
			for(int pos=0;pos<indices.size();pos++){
				Vec2i con=indices.get(pos);
				
				if(con.x>con.y)con=new Vec2i(con.y, con.x);

				boolean
					xHooked=isVertexHooked(con.x),
					yHooked=isVertexHooked(con.y);
				if(!xHooked||!yHooked){
					AbstractRealPhysicsVec3F 
						vertex1=vertices.get(con.x),
						vertex2=vertices.get(con.y);
					
					Vec3M distance=vertex1.getPos().sub(vertex2.getPos()).add(vertex1.getVelocity()).add(vertex2.getVelocity().mul(-1));
					float d1stance=distance.length();
					float difference=d1stance-originalDistances[pos];
					if(!MathUtil.isNumValid(difference)){
						criticalState=true;
						return;
					}
					if(interactStrategy.shouldInteract(difference)){
						
						Vec3M fix=distance.normalize().mul(-difference/2);
						float
							mul1=xHooked? 0:yHooked?2: 1,
							mul2=xHooked?-2:yHooked?0:-1;
						
						vertex1.setStress(Math.abs(difference));
						vertex2.setStress(Math.abs(difference));
						float s1=vertex1.getStress(),s2=vertex2.getStress();
						vertex1.addVelocity(fix.mul(mul1*(1+s1)));
						vertex2.addVelocity(fix.mul(mul2*(1+s2)));
						
						if(Math.abs(difference)>3){
							criticalStressReached=true;
						}
					}
				}
			}
			Iterator<Vec3M> hooks=hooksPos.iterator();
			for(RealPhysicsMeshHook realPhysicsMeshHook:physicsHooks){
				Vec3M pos=hooks.next();
				AbstractRealPhysicsVec3F vert=vertices.get(realPhysicsMeshHook.vertexID);
				if(Math.abs(pos.distanceTo(vert.getPos())-originalDistances[realPhysicsMeshHook.vertexID])>1){
					criticalState=true;
				}
			}
			if(criticalStressReached)criticalStress+=2;
			if(criticalStress>0)criticalStress--;
		}
		if(criticalStress>30){
			criticalState=true;
		}
		int id=0;
		for(AbstractRealPhysicsVec3F v:vertices){
			if(!isVertexHooked(id))for(Colideable colideable:coliders)colideable.applyColideableMove(v);
			v.update(coliders);
			id++;
		}
	}
	
}
