package com.magiology.handlers.animationhandlers.thehand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.client.render.itemrender.ItemRendererTheHand;
import com.magiology.core.init.MItems;
import com.magiology.handlers.animationhandlers.thehand.animation.CommonHand;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimation;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimationBase;
import com.magiology.handlers.animationhandlers.thehand.animation.LinearHandAnimation;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.entitys.EntityBallOfEnergy;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.PhysicsVec3F;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TheHandHandler{
	public static HandAnimationBase activeAnimation;
	
	public static DoubleObject<PhysicsVec3F,PhysicsVec3F>[] cubes=new DoubleObject[8];
	private static Map<String, HandData> handData;

	public static TheHandHandler instance=new TheHandHandler();
	
	static float p=1F/16F;
	
	static ItemRendererTheHand renderer=new ItemRendererTheHand();
	
	
	private static final ObjectProcessor<Float> speedHandler=new ObjectProcessor<Float>(){
		@Override
		public Float pocess(Float speed, Object...objects){
			float 
				pos=(float)objects[0],
				wantedPos=(float)objects[1],
				acceleration=(float)objects[2],
				friction=(float)objects[3],
				diff=Math.abs(wantedPos-pos),
				act=acceleration,
				speed1=(diff/act);
			
			if(speed1>1)speed1=1;
			else speed1=(speed1*speed1+speed1)/2;
			
			return UtilM.handleSpeedFolower(speed, pos, wantedPos, acceleration)*friction*speed1;
		}
	};
	static{
		for(int i=0;i<cubes.length;i++){
			cubes[i]=new DoubleObject<>();
			cubes[i].obj1=new PhysicsVec3F(new Vec3M(),new Vec3M(0.004,0.004,0.004));
			cubes[i].obj2=new PhysicsVec3F(new Vec3M(),new Vec3M(0.5,0.5,0.5));
			cubes[i].obj2.setFriction(0.9F);
			
			cubes[i].obj1.y.addWall("hand",0, false);
		}
		cubes[1].obj1.x.wantedPoint=p*0.75F;
		cubes[2].obj1.x.wantedPoint=p*0.75F;
		cubes[2].obj1.z.wantedPoint=p*0.75F;
		cubes[3].obj1.z.wantedPoint=p*0.75F;
		
		cubes[4].obj1.x.wantedPoint=p*0.75F;
		cubes[4].obj1.y.wantedPoint=p*0.75F;
		cubes[5].obj1.x.wantedPoint=p*0.75F;
		cubes[5].obj1.z.wantedPoint=p*0.75F;
		cubes[5].obj1.y.wantedPoint=p*0.75F;
		cubes[6].obj1.z.wantedPoint=p*0.75F;
		cubes[6].obj1.y.wantedPoint=p*0.75F;
		cubes[7].obj1.y.wantedPoint=p*0.75F;

		for(int i=0;i<cubes.length;i++){
			cubes[i].obj1.x.point=cubes[i].obj1.x.prevPoint=cubes[i].obj1.x.wantedPoint;
			cubes[i].obj1.y.point=cubes[i].obj1.y.prevPoint=cubes[i].obj1.y.wantedPoint;
			cubes[i].obj1.z.point=cubes[i].obj1.z.prevPoint=cubes[i].obj1.z.wantedPoint;
		}
	}
	@SideOnly(Side.CLIENT)
	public static void actionAnimation(EntityPlayer player){
		if(activeAnimation!=null)return;
		activeAnimation=CommonHand.chargeUp;
	}
	@SideOnly(Side.CLIENT)
	public static void animate(EntityPlayer player){
		
//		setActivePositionId(player, 2);
//		PrintUtil.println(pos.name);
		renderer.secure();
		
		Map<String, HandData> data=getHandData();
		HandData 
			main=data.get("main"),
			actual=data.get("actual"),
			speed=handData.get("speed");
		data.get("prev").set((HandData)actual.clone());
		
		HandData wanted=null;
		if(activeAnimation!=null){
			
			if(activeAnimation instanceof HandAnimation){
				HandAnimation anim=(HandAnimation)activeAnimation;
				if(!anim.isDone())anim.update(UtilM.getWorldTime());
				if(anim.isDone())activeAnimation=null;
			}
			else if(activeAnimation instanceof LinearHandAnimation){
				
				LinearHandAnimation animation=(LinearHandAnimation)activeAnimation;
				boolean holding=player.isUsingItem();
				animation.progressHandler.setHolding(holding);
				animation.update();
				if(animation.progressHandler.isInactive())activeAnimation=null;
			}
			if(activeAnimation!=null){
				wanted=activeAnimation.getWantedPos();
			}
		}
		if(wanted==null)wanted=getActivePosition(player).data;
		
		handleSpeed(speed, main, wanted, 14F, 0.2F, 0.6F);
		
		main.set(main.add(speed));
		
		updateNoise(player);
		actual.set(main.add(data.get("noise")));
		
		float globalDist=0;
		
		for(DoubleObject<PhysicsVec3F,PhysicsVec3F> obj:cubes){
			obj.obj1.x.acceleration=0.006F;
			obj.obj1.y.acceleration=0.006F;
			obj.obj1.z.acceleration=0.006F;
			float dist=(float)Math.min(1,
					obj.obj1.getPoint().distanceTo(
							obj.obj1.x.wantedPoint+obj.obj1.x.speed,
							obj.obj1.y.wantedPoint+obj.obj1.y.speed,
							obj.obj1.z.wantedPoint+obj.obj1.z.speed)/p*2
					);
			if(dist<0.001){
				obj.obj1.x.point=obj.obj1.x.wantedPoint;
				obj.obj1.y.point=obj.obj1.y.wantedPoint;
				obj.obj1.z.point=obj.obj1.z.wantedPoint;
			}else{
				globalDist+=dist;
				obj.obj1.setFriction(0.9F*dist);
				obj.obj1.update();
			}
			if(obj.obj1.x.speed!=0)obj.obj2.x.speed+=RandUtil.CRF(Math.abs(obj.obj1.x.speed))*360;
			if(obj.obj1.y.speed!=0)obj.obj2.y.speed+=RandUtil.CRF(Math.abs(obj.obj1.y.speed))*360;
			if(obj.obj1.z.speed!=0)obj.obj2.z.speed+=RandUtil.CRF(Math.abs(obj.obj1.z.speed))*360;
			obj.obj2.update();
		}
		
		
		actual.cubeGlowPrecentage=UtilM.slowlyEqualize(actual.cubeGlowPrecentage,globalDist/8,0.4F);
		actual.calciferiumPrecentage=UtilM.slowlyEqualize(actual.calciferiumPrecentage,actual.cubeGlowPrecentage,actual.calciferiumPrecentage<actual.cubeGlowPrecentage?0.02F:0.01F);
	}
	
	
	
	@SideOnly(Side.CLIENT)
	public static void generateNewNoiseValue(){
		HandData noiseSpeed=getHandData().get("noiseWanted");
		for(int i=0;i<noiseSpeed.base.length;i++)
			if(RandUtil.RI(4)==0)noiseSpeed.base[i]=RandUtil.CRF(i<3?p*2:p);
		
		for(int i=0;i<noiseSpeed.thumb.length;i++)
			if(RandUtil.RI(4)==0)noiseSpeed.thumb[i]=RandUtil.CRF(3);
		
		for(int i=0;i<noiseSpeed.fingers.length;i++){
			float noise=RandUtil.CRF(3);
			for(int j=0;j<noiseSpeed.fingers[i].length;j++){
				if(j==0)noiseSpeed.fingers[i][j]=RandUtil.CRF(3);
				else noiseSpeed.fingers[i][j]=noise;
			}
		}
	}
	public static HandPosition getActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getHeldItemMainhand().getTagCompound().getInteger("AP");
		if(id<0)return CommonHand.errorPos;
		if(id>=HandPosition.values().length)return CommonHand.errorPos;
		return HandPosition.values()[id];
	}
	public static Map<String, HandData> getHandData(){
		if(handData!=null)return handData;
		handData=new HashMap<>();
		handData.put("prev", new HandData());
		handData.put("actual", new HandData());
		
		handData.put("main", new HandData());
		handData.put("wanted", new HandData());
		handData.put("speed", new HandData());
		
		handData.put("noise", new HandData());
		handData.put("noiseSpeed", new HandData());
		handData.put("noiseWanted", new HandData());
		return handData;
	}
	public static HandPosition getLastActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getHeldItemMainhand().getTagCompound().getInteger("LAP");
		if(id<0)return CommonHand.errorPos;
		if(id>=HandPosition.values().length)return CommonHand.errorPos;
		return HandPosition.values()[id];
	}
	
	
	public static ItemRendererTheHand getRenderer(){return renderer;}
	public static HandData getRenderHandData(){
//		handData=null;
		Map<String, HandData> handData=getHandData();
		HandData
			prev=handData.get("prev"),
			actual=handData.get("actual");
		if(prev.fingers[0].length==0)prev.set(actual);
		
		HandData result=new HandData(false);
		result.base=PartialTicksUtil.calculatePos(prev.base,actual.base);
		result.thumb=PartialTicksUtil.calculatePos(prev.thumb,actual.thumb);
		result.fingers=PartialTicksUtil.calculatePos(prev.fingers,actual.fingers);
		result.cubes=cubes;
		result.cubeGlowPrecentage=actual.cubeGlowPrecentage;
		result.calciferiumPrecentage=actual.calciferiumPrecentage;
		return result;
	}
	private static void handleSpeed(HandData speed, HandData pos,HandData wantedPos,float accelerationRot,float accelerationPos, float friction){
		
		for(int i=0;i<speed.base.length;i++)
			speed.base[i]=speedHandler.pocess(speed.base[i], pos.base[i], wantedPos.base[i], i<3?accelerationPos:accelerationRot,friction);
		
		for(int i=0;i<speed.thumb.length;i++)
			speed.thumb[i]=speedHandler.pocess(speed.thumb[i], pos.thumb[i], wantedPos.thumb[i], accelerationRot,friction);
		
		for(int i=0;i<speed.fingers.length;i++)for(int j=0;j<speed.fingers[i].length;j++)
			speed.fingers[i][j]=speedHandler.pocess(speed.fingers[i][j], pos.fingers[i][j], wantedPos.fingers[i][j], accelerationRot,friction);
	}
	
	@SideOnly(Side.CLIENT)
	public static void handUseAnimation(EntityPlayer player){
		if(activeAnimation!=null)return;
		activeAnimation=CommonHand.rightClickAnimation;
		CommonHand.rightClickAnimation.start();
	}
	public static void init(){
		CommonHand.load();
		HandPosition.compile();
	}
	public static boolean isActive(EntityPlayer player){
		if(!UtilM.isItemInStack(MItems.theHand, player.getHeldItemMainhand()))return false;
		return player.getHeldItemMainhand().hasTagCompound();
	}
	
	public static HandPosition nextPosition(EntityPlayer player){
		if(!isActive(player))return null;
		HandPosition[] values=HandPosition.values();
		int now=UtilM.getPosInArray(TheHandHandler.getActivePosition(player), values);
		now++;
		if(now==values.length)now=0;
		if(values[now]==CommonHand.errorPos)now++;
		if(now==values.length)now=0;
		TheHandHandler.setActivePositionId(player, now);
		return values[now];
	}
	public static void setActivePositionId(EntityPlayer player,int id){
		if(!isActive(player))return;
		player.getHeldItemMainhand().getTagCompound().setInteger("LAP", player.getHeldItemMainhand().getTagCompound().getInteger("AP"));
		player.getHeldItemMainhand().getTagCompound().setInteger( "AP", id);
	}
	public static void shoot(EntityPlayer player,Vec3M pos){
		if(player.worldObj.isRemote){
			unsettleCubes(0.25F);
			List<Vec3M> positions=new ArrayList<Vec3M>();
			
			for(int i=0;i<TheHandHandler.getRenderer().handModelSolid.rednerLeftover.size();i++)
				positions.add(ItemRendererTheHand.getVertexInWorld(i));
			for(int i=0;i<20;i++)
				positions.add(ItemRendererTheHand.getPalmRand());
			Vec3M look=Vec3M.conv(player.getLook(PartialTicksUtil.partialTicks)).mul(0.01);
			positions.forEach(pos1->UtilM.spawnEntityFX(
				new EntitySmoothBubleFX(UtilM.getTheWorld(),pos1.x,pos1.y,pos1.z,look.x,look.y,look.z,40,5,0.5F,1,0.2,0,1)
			));
		}
		else{
			EntityBallOfEnergy entity=new EntityBallOfEnergy(player,2,0);
			entity.setPosition(pos.x,pos.y,pos.z);
			UtilM.spawnEntity(entity);
		}
	}
	
	
	public static void unsettleCubes(float ammount){
		for(DoubleObject<PhysicsVec3F,PhysicsVec3F> obj:cubes){
			obj.obj1.x.speed+=RandUtil.CRF(ammount);
			obj.obj1.y.speed+=RandUtil.CRF(ammount*1.5);
			obj.obj1.z.speed+=RandUtil.CRF(ammount);
		}
	}
	
	public static void update(EntityPlayer player){
		if(UtilM.isNull(UtilM.getTheWorld()))return;
		if(!isActive(player))return;
		
		HandPosition handPos=getActivePosition(player);
		if(handPos==CommonHand.errorPos)setActivePositionId(player, 3);
		
		if(UtilM.isRemote(player))animate(player);
	}
	public static void updateNoise(EntityPlayer player){
		
		HandData
			noise=handData.get("noise"),
			noiseSpeed=handData.get("noiseSpeed"),
			noiseWanted=handData.get("noiseWanted");
		
		if(UtilM.getWorldTime(player)%40==0)generateNewNoiseValue();
		
		handleSpeed(noiseSpeed, noise, noiseWanted, 0.01F, 0.00001F, 0.95F);
		
		noise.set(noise.add(noiseSpeed));
	}
	
}
