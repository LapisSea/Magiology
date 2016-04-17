package com.magiology.mcobjects.effect;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

import net.minecraft.world.World;

public class EntitySparkFX extends EntityFXM{
	
	private class Fragment{
		public Fragment bindedFragment,nextFragment;
		public PhysicsFloat r,g,b,a;
		
		public float xOffset,yOffset,zOffset;
		
		public Fragment(float xOffset,float yOffset,float zOffset,Fragment bindedFragment,float r,float g,float b,float a,float rWanted,float gWanted,float bWanted,float aWanted,float colorSpeed){
			this.bindedFragment=bindedFragment;
			this.xOffset=xOffset;
			this.yOffset=yOffset;
			this.zOffset=zOffset;
			float sp=(colorSpeed*numberOfSplitsPerUpdate)/slowdown.lenght;
			this.r=new PhysicsFloat(r, sp);
			this.g=new PhysicsFloat(g, sp);
			this.b=new PhysicsFloat(b, sp);
			this.a=new PhysicsFloat(a, sp);
			
			this.r.simpleVersion=
			this.g.simpleVersion=
			this.b.simpleVersion=
			this.a.simpleVersion=true;
			
			this.r.wantedPoint=rWanted;
			this.g.wantedPoint=gWanted;
			this.b.wantedPoint=bWanted;
			this.a.wantedPoint=aWanted;
		}
		
		public float[] getRootOffset(){
			float[] Return={xOffset,yOffset,zOffset};
			if(!isLastInLine())Return=MathUtil.addToFloatArray(Return, bindedFragment.getRootOffset());
			else{
				Return[0]-=xOffset;
				Return[1]-=yOffset;
				Return[2]-=zOffset;
			}
			return Return;
		}
		
		public boolean hasNextFragment(){
			return nextFragment!=null;
		}
		public boolean isLastInLine(){
			return bindedFragment==null;
		}
		public void render(){
			float[] root=getRootOffset(),end=MathUtil.addToFloatArray(root,new float[]{-xOffset,-yOffset,-zOffset});
			OpenGLM.color(r.getPoint(),g.getPoint(),b.getPoint(),a.getPoint());
			TessUtil.drawLine(root[0],root[1],root[2], end[0],end[1],end[2], fragmentWidth, false, null, 0, 0);
		}
		public void update(){
			r.update();
			g.update();
			b.update();
			a.update();
		}
	}
	public boolean doesLockOnTheSpot=true;
	public List<Fragment> fragments=new ArrayList<Fragment>();
	public float fragmentWidth,fragmentSize,xDirection,yDirection,zDirection;
	public int numberOfSplitsPerUpdate=1,size=20;
	public SlowdownUtil slowdown=new SlowdownUtil(2);
	
	public ColorF startColor=new ColorF(1,0.3,0,0.5),endColor=new ColorF(0.5,0,1,0.05);
	public EntitySparkFX(World w, double xp, double yp, double zp,float fragmentWidth,float fragmentSize){
		super(w, xp, yp, zp);
		this.fragmentSize=fragmentSize;
		this.fragmentWidth=fragmentWidth;
		New(null);
	}
	
	public EntitySparkFX(World w, double xp, double yp, double zp,float fragmentWidth,float fragmentSize,int fragmetingSpeed,int numberOfSplitsPerUpdate,int size,Vec3M dir){
		this(w, xp, yp, zp, fragmentWidth, fragmentSize);
		slowdown.lenght=fragmetingSpeed;
		this.numberOfSplitsPerUpdate=Math.max(numberOfSplitsPerUpdate, 1);
		this.size=size;
		xDirection=(float)dir.x;
		yDirection=(float)dir.y;
		zDirection=(float)dir.z;
	}
	@Override
	public void motionHandler(){
		if(doesLockOnTheSpot)xSpeed=ySpeed=zSpeed=0;
		else super.motionHandler();
	}
	private void New(Fragment lastFragment){
		double[] ball=MathUtil.createBallXYZ(fragmentSize/3+RandUtil.CRF(fragmentSize), false);
		
		Fragment f=new Fragment((float)ball[0]+xDirection,(float)ball[1]+yDirection,(float)ball[2]+zDirection,lastFragment,
				startColor.r+RandUtil.RF(0.1),startColor.g+RandUtil.CRF(0.1),startColor.b+RandUtil.RF(0.1),startColor.a,
				endColor.r+RandUtil.CRF(0.1),endColor.g+RandUtil.RF(0.1),endColor.b+RandUtil.RF(0.1),endColor.a,
				0.05F
				);
		fragments.add(f);
		if(lastFragment!=null)lastFragment.nextFragment=f;
	}
	
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		
		for(Fragment f:fragments)f.update();
		
		if(fragments.size()<size&&slowdown.isTimeWithAddProgress()){
			for(int b=0;b<numberOfSplitsPerUpdate;b++){
				int siz=fragments.size();
				double chanse=1;
				for(int a=0;a<siz;a++){
					Fragment lastFragment=fragments.get(a);
					if(!lastFragment.hasNextFragment()){
						if(RandUtil.RB(chanse)){
							chanse-=0.1;
							if(lastFragment!=null&&RandUtil.RB(6)){
								New(lastFragment);
								New(lastFragment);
							}else{
								New(lastFragment);
							}
						}
					}
				}
			}
			if(fragments.size()>size){
				slowdown.progress=0;
				slowdown.lenght=10;
			}
		}
		if(fragments.size()>=size){
			for(Fragment a:fragments){
				a.a.wantedPoint*=0.8;
				a.a.wantedPoint=MathUtil.snap(a.a.wantedPoint, 0, 1);
			}
		}
		if(fragments.size()>size&&slowdown.isTimeWithAddProgress())setExpired();
	}
	
	@Override
	public void render(){
		OpenGLM.pushMatrix();
		OpenGLM.disableTexture2D();
		OpenGLM.disableCull();
		GL11U.setUpOpaqueRendering(1);
		float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
		float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
		float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
		OpenGLM.translate(x,y,z);
		
		for(Fragment fragment:fragments)fragment.render();
		
		
		OpenGLM.popMatrix();
		OpenGLM.enableTexture2D();
		OpenGLM.enableCull();
		GL11U.endOpaqueRendering();
	}
}
