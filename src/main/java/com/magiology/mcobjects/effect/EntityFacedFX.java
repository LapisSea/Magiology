package com.magiology.mcobjects.effect;

import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.world.World;

public class EntityFacedFX extends EntityFXM{
	
	boolean active;
	double length,gravity,friction;
	double r_e,g_e,b_e,opacity_e;
	public Vec3M rotation=new Vec3M();
	double[] sideOpacity=new double[8],sideOpacityMultiplayer=new double[8];
	int[] sideOpacityChange=new int[8];
	int state;
	String texture;
	int type;
	
	public EntityFacedFX(World w,double xp, double yp, double zp, double xs, double ys, double zs, int siz, double lengt,double gravit, boolean activ,int typ,String textur ,double Ra,double Ga,double Ba,double opacita,double frictio){
		super(w, xp, yp, zp, xs, ys, zs);
		xSpeed =xs;
		ySpeed =ys;
		zSpeed =zs;
		friction=frictio;
		if(texture=="tx1"){
			for(int a=0;a<sideOpacity.length;a++){
				sideOpacityChange[a]=worldObj.rand.nextInt(3)-1;
				sideOpacity[a]=worldObj.rand.nextDouble();
			}
		}
		type=typ;
		if(type==1){particleMaxAge=siz;particleScale=siz/10;}
		if(type==2){particleMaxAge=siz;particleScale=0;state=1;}
		else if(type>=3&&type<=10){particleMaxAge=siz;particleScale=siz/10;}
		gravity=gravit*0.001;
		length=lengt;
		active=activ;
		r_e=Ra;
		g_e=Ga;
		b_e=Ba;
		opacity_e=opacita;
		texture=textur;
	}
	public EntityFacedFX(World w,double xp, double yp, double zp, double xs, double ys, double zs, int siz, double lengt,double gravit ,double Ra,double Ga,double Ba,double opacita){
		this(w,xp, yp, zp, xs, ys, zs, siz, lengt, gravit, 1, Ra, Ga, Ba, opacita);
	}
	public EntityFacedFX(World w,double xp, double yp, double zp, double xs, double ys, double zs, int siz, double lengt,double gravit,int typ ,double Ra,double Ga,double Ba,double opacita){
		this(w,xp, yp, zp, xs, ys, zs, siz, lengt, gravit, typ, Ra, Ga, Ba, opacita, 0.99);
	}
	public EntityFacedFX(World w,double xp, double yp, double zp, double xs, double ys, double zs, int siz, double lengt,double gravit,int typ ,double Ra,double Ga,double Ba,double opacita,double frictio){
		this(w,xp, yp, zp, xs, ys, zs, siz, lengt, gravit, false, typ, "tx1", Ra, Ga, Ba, opacita, frictio);
	}
	
	@Override
	public void motionHandler(){
		xSpeed*=friction;
		ySpeed*=friction;
		zSpeed*=friction;
		ySpeed +=gravity;
		moveEntity(xSpeed, ySpeed, zSpeed);
		
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		
		float random=worldObj.rand.nextFloat()*4;
		
		if(texture=="tx1")opacityHandler();
		if(type==1){
			particleScale-=0.001*particleMaxAge/(length/10);
			if(particleScale<2)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<4)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<6)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<8)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<0){
				setExpired();
			}
		}
		else if(type==2){
			if(particleAge<particleMaxAge/8.5/2){
				particleScale+=0.004*particleMaxAge;
				double x1=(0.025-0.05*worldObj.rand.nextFloat())*particleScale/10;
				double y1=(0.025-0.05*worldObj.rand.nextFloat())*particleScale/10;
				double z1=(0.025-0.05*worldObj.rand.nextFloat())*particleScale/10;
				if(U.getMC().gameSettings.particleSetting==0&&worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX+x1*4, posY+y1*4, posZ+z1*4, -x1/3+xSpeed,-y1/3+ySpeed, -z1/3+zSpeed, particleMaxAge/2, 3,0, false,1,"tx1",r_e,g_e,b_e, 1.0, 0.99));
			}
			if(particleAge>particleMaxAge/8.5/2){
				particleScale-=0.004*particleMaxAge;
				if(active==true&&U.getMC().gameSettings.particleSetting==0&&worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ, 0.025-0.05*worldObj.rand.nextFloat()+xSpeed,0.025-0.05*worldObj.rand.nextFloat()+ySpeed, 0.025-0.05*worldObj.rand.nextFloat()+zSpeed, particleMaxAge/2, 3,-10, false,1,"tx1",r_e,g_e,b_e, 1.0, 0.99));
			}
			if(particleAge>particleMaxAge/8.5){
				setExpired();
				if(active==true){
					for(int t=0;t<2;t++)UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,0.025-0.05*worldObj.rand.nextFloat()+xSpeed,0.025-0.05*worldObj.rand.nextFloat()+ySpeed, 0.025-0.05*worldObj.rand.nextFloat()+zSpeed, particleMaxAge/2, 4,-1, false,1,"tx1",r_e,g_e,b_e, opacity_e, 0.99));
				}
			}
		}
		else if(type==3){
			particleScale-=0.001*particleMaxAge/(length/10);
			if(particleScale<2)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<4)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<6)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<8)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<0){
				setExpired();
				for(int a=0;a<40;a++){
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,(0.25-0.5*worldObj.rand.nextFloat())*random,(0.25-0.5*worldObj.rand.nextFloat())*random, (0.25-0.5*worldObj.rand.nextFloat())*random,1500, 5,-5, false,1,"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
					}
			}
		}
		else if(type==4){
			particleScale-=0.001*particleMaxAge/(length/10);
			if(particleScale<2)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<4)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<6)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<8)particleScale-=0.001*particleMaxAge/(length/10)/10;
			if(particleScale<0){
				setExpired();
				for(int a=0;a<20;a++){
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX+(5-10*worldObj.rand.nextFloat())*random, posY+(5-10*worldObj.rand.nextFloat())*random, posZ+(5-10*worldObj.rand.nextFloat())*random,(0.025-0.05*worldObj.rand.nextFloat())*random,(0.025-0.05*worldObj.rand.nextFloat())*random, (0.025-0.05*worldObj.rand.nextFloat())*random,1000, 5,-5, false,2,"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
				}
			}
		}
		else if(type==5){
			if(particleAge<particleMaxAge/8.5/3){
				particleScale+=0.004*particleMaxAge;
				}
			else{
				setExpired();
					for(int t=0;t<40;t++)UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,1-2*worldObj.rand.nextFloat()+xSpeed,3-4*worldObj.rand.nextFloat()+ySpeed*2, 1-2*worldObj.rand.nextFloat()+zSpeed, particleMaxAge*4, 6,-1, false,1,"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
			}
		}
		else if(type==6){
//			motionY-=0.001;
			if(particleAge<particleMaxAge/8.5/3){
				particleScale+=0.006*particleMaxAge;
			}
			else{
				if(ySpeed<0.01){
					setExpired();
					for(int t=0;t<8;t++){
						for(int t1=0;t1<5;t1++){
							if(worldObj.rand.nextInt(3)==0){
								UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,
										1.5-3*worldObj.rand.nextFloat()+xSpeed,2.5-3*worldObj.rand.nextFloat()+ySpeed*2, 1.5-3*worldObj.rand.nextFloat()+zSpeed, 
										800*5, 6,-1, false,2,
												"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
							}else{
								UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,
										1.5-3*worldObj.rand.nextFloat()+xSpeed,2.5-3*worldObj.rand.nextFloat()+ySpeed*2, 1.5-3*worldObj.rand.nextFloat()+zSpeed,  
										800*20, 40,-1, false,1,
												"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
							}
						}
						UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,
								(2-4*worldObj.rand.nextFloat()+xSpeed)/2, 1+(0.5-worldObj.rand.nextFloat()+xSpeed)/2, (2-4*worldObj.rand.nextFloat()+zSpeed)/2, 
								800, 400,-5, false,8,
										"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
						
					}
				}
				else UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,1-2*worldObj.rand.nextFloat()+xSpeed,1-2*worldObj.rand.nextFloat()+ySpeed, 1-2*worldObj.rand.nextFloat()+zSpeed, particleMaxAge*2, 4,-20, false,1,"tx1",r_e,g_e,b_e, opacity_e, 0.99));
			}
		}
		else if(type==7){
			if(particleAge>particleMaxAge/8.5){
				setExpired();
			}
			else {
				for(int e=0;e<3;e++){
					double[] AB=MathUtil.circleXZ((particleAge/4.0)+(e>=1?180*e:0));
					
					
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX, posY, posZ,
					AB[0]*3-RandUtil.RF()/2,3-RandUtil.RF(), AB[1]*3-RandUtil.RF()/2,
					(int)(particleMaxAge*2.5), 10,-100, false,1,"tx1",worldObj.rand.nextFloat(),worldObj.rand.nextFloat(),worldObj.rand.nextFloat(), 1, 0.99));
				}
				
			}
		}
		else if(type==8){
			if(particleScale<=0){
				setExpired();
				
				for(int a=0;a<10;a++){

					float xrand=0.5F-worldObj.rand.nextFloat();
					float yrand=0.5F-(worldObj.rand.nextFloat()*0.5F);
					float zrand=0.5F-worldObj.rand.nextFloat();
					
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX+xrand, posY+yrand, posZ+zrand,
							-xrand/21, -yrand/21, -zrand/21,200,4,0,true,1,"tx1", r_e+(0.5-worldObj.rand.nextFloat())/10, g_e+(0.5-worldObj.rand.nextFloat())/10, b_e+(0.5-worldObj.rand.nextFloat())/10,1, 0.99));
				}
			}
		}
		else if(type==9){
			if(particleScale<=0){
				setExpired();
				
				for(int a=0;a<10;a++){
					
					float xrand=0.5F-worldObj.rand.nextFloat();
					float yrand=0.5F-(worldObj.rand.nextFloat()*0.5F);
					float zrand=0.5F-worldObj.rand.nextFloat();
					
					UtilM.spawnEntityFX(new EntityFacedFX(worldObj,posX+xrand, posY+yrand, posZ+zrand,
							-xrand/21, -yrand/21, -zrand/21,200,4,0,true,1,"tx1", r_e+(0.5-worldObj.rand.nextFloat())/10, g_e+(0.5-worldObj.rand.nextFloat())/10, b_e+(0.5-worldObj.rand.nextFloat())/10,1, 0.99));
				}
			}
		}
		else type=1;
	}
	
	public void opacityHandler(){
		
		if(particleAge%12==0){
			for(int a=0;a<sideOpacity.length;a++){
				sideOpacityChange[a]=worldObj.rand.nextInt(5)-2;
			}
			for(int a=0;a<sideOpacity.length;a++){
				if(sideOpacityMultiplayer[a]>0.99)sideOpacityChange[a]=worldObj.rand.nextInt(3)-2;
				else if(sideOpacityMultiplayer[a]<0.01)sideOpacityChange[a]=worldObj.rand.nextInt(3);
			}
		}
		for(int a=0;a<sideOpacityMultiplayer.length;a++){
			sideOpacityMultiplayer[a]+=sideOpacityChange[a]/30.0;
			sideOpacity[a]=sideOpacityMultiplayer[a]*opacity_e;
		}
		
	}
	@Override
	public void render(){
		OpenGLM.disableFog();
		GL11U.setUpOpaqueRendering(2);
		
		
		float PScale = 0.01F*particleScale;
		float x=(float)(prevPosX+(posX-prevPosX)*par2-interpPosX);
		float y=(float)(prevPosY+(posY-prevPosY)*par2-interpPosY);
		float z=(float)(prevPosZ+(posZ-prevPosZ)*par2-interpPosZ);
		
		
		if(texture=="tx1")	 U.getMC().renderEngine.bindTexture(Textures.SmoothBuble1);
		else if(texture=="tx2")U.getMC().renderEngine.bindTexture(Textures.SmoothBuble2);
		else if(texture=="tx3")U.getMC().renderEngine.bindTexture(Textures.SmoothBuble3);
		OpenGLM.pushMatrix();
		OpenGLM.translate(x,y,z);
		OpenGLM.translate(0, -0.095, 0);
		GL11U.glRotate(rotation.x, rotation.y, rotation.z);
//		OpenGLM.scale(1, 1.3, 1);
		GL11U.glCulFace(false);
		Renderer.POS_UV_COLOR.beginQuads();
		Renderer.POS_UV_COLOR.addVertex( PScale, PScale,0, 0, 0, (float)r_e, (float)g_e, (float)b_e, (float)opacity_e);
		Renderer.POS_UV_COLOR.addVertex(-PScale, PScale,0, 1, 0, (float)r_e, (float)g_e, (float)b_e, (float)opacity_e);
		Renderer.POS_UV_COLOR.addVertex(-PScale,-PScale,0, 1, 1, (float)r_e, (float)g_e, (float)b_e, (float)opacity_e);
		Renderer.POS_UV_COLOR.addVertex( PScale,-PScale,0, 0, 1, (float)r_e, (float)g_e, (float)b_e, (float)opacity_e);
		Renderer.POS_UV_COLOR.draw();
		GL11U.glCulFace(true);
		
		
		OpenGLM.popMatrix();

		GL11U.endOpaqueRendering();
	}
}
