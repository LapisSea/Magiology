package com.magiology.handlers.animationhandlers.thehand;

import com.magiology.client.render.itemrender.ItemRendererTheHand;
import com.magiology.core.init.MItems;
import com.magiology.handlers.animationhandlers.thehand.animation.CommonHand;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimation;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimationBase;
import com.magiology.handlers.animationhandlers.thehand.animation.LinearHandAnimation;
import com.magiology.mcobjects.entitys.EntityBallOfEnergy;
import com.magiology.util.renderers.ValueWithPrev;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.ArrayMath;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.PhysicsVec3F;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TheHandHandler{
	public static HandAnimationBase activeAnimation;
	
	
//	private static Map<String, HandData> handData[]=new HashMap[2];
	@SideOnly(Side.CLIENT)
	private static HandDataCollection leftHand=new HandDataCollection(),rightHand=new HandDataCollection();
	
	public static HandDataCollection getHand(boolean handLeft){
		return handLeft?leftHand:rightHand;
	}
	
	@SideOnly(Side.CLIENT)
	protected static class HandDataCollection{
		
		public HandData 
			pos=new HandData(),
			noise=new HandData(),
			wantedNoise=new HandData(),
			noiseSpeed=new HandData();
		public DoubleObject<PhysicsVec3F,PhysicsVec3F>[] cubes=new DoubleObject[8];
		public ValueWithPrev<HandData> calculated=new ValueWithPrev(new HandData());
		
		public HandDataCollection(){
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
		
		public void updatePrev(){
			calculated.update();
		}
	}
	
	public static TheHandHandler instance=new TheHandHandler();
	
	static float p=1F/16F;
	
	@SideOnly(Side.CLIENT)
	public static void actionAnimation(EntityPlayer player){
		if(activeAnimation!=null)return;
		activeAnimation=CommonHand.chargeUp;
	}
	@SideOnly(Side.CLIENT)
	public static void animate(EntityPlayer player){
		
//		setActivePositionId(player, 2);
//		PrintUtil.println(pos.name);
		ItemRendererTheHand.get().secure();
		
		animateHand(player, leftHand);
		animateHand(player, rightHand);
		
	}
	
	@SideOnly(Side.CLIENT)
	private static void animateHand(EntityPlayer player, HandDataCollection hand){
		hand.updatePrev();
		
		HandData wanted=null;
		if(activeAnimation!=null){
			
			if(activeAnimation instanceof HandAnimation){
				HandAnimation anim=(HandAnimation)activeAnimation;
				if(!anim.isDone())anim.update(UtilC.getWorldTime());
				if(anim.isDone())activeAnimation=null;
			}
			else if(activeAnimation instanceof LinearHandAnimation){
				LinearHandAnimation animation=(LinearHandAnimation)activeAnimation;
				boolean holding=player.isHandActive()&&player.getActiveHand()==EnumHand.MAIN_HAND&&hand==rightHand;
				animation.progressHandler.setHolding(holding);
				animation.update();
				if(animation.progressHandler.isInactive())activeAnimation=null;
			}
			if(activeAnimation!=null)wanted=activeAnimation.getWantedPos();
		}
		if(wanted==null)wanted=getActivePosition(player).data;
		
		handleSpeed(hand.pos, wanted, 2);
		updateNoise();
		
		updateCubes(leftHand);
		updateCubes(rightHand);
		
		hand.calculated.value.base=ArrayMath.calc(hand.pos.base, hand.noise.base, '+');
		for(int i=0;i<hand.calculated.value.fingers.length;i++){
			hand.calculated.value.fingers[i]=ArrayMath.calc(hand.pos.fingers[i], hand.noise.fingers[i], '+');
		}
		hand.calculated.value.thumb=ArrayMath.calc(hand.pos.thumb, hand.noise.thumb, '+');
	}
	
	@SideOnly(Side.CLIENT)
	private static void updateCubes(HandDataCollection hand){
		float globalDist=0;
		for(DoubleObject<PhysicsVec3F,PhysicsVec3F> obj:hand.cubes){
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
		globalDist/=8F;
		
		hand.calculated.value.cubeGlowPrecentage=UtilM.graduallyEqualize(hand.calculated.value.cubeGlowPrecentage,globalDist,0.4F);
		hand.calculated.value.calciferiumPrecentage=UtilM.graduallyEqualize(hand.calculated.value.calciferiumPrecentage,hand.calculated.value.cubeGlowPrecentage,hand.calculated.value.calciferiumPrecentage<hand.calculated.value.cubeGlowPrecentage?0.02F:0.01F);
	}
	
	
	
	@SideOnly(Side.CLIENT)
	public static void generateNewNoiseValue(){
		ObjectProcessor<HandData> gen=(data,o)->{
			for(int i=0;i<data.base.length;i++)
				if(RandUtil.RI(4)==0)data.base[i]=RandUtil.CRF(i<3?p*2:p);
			
			for(int i=0;i<data.thumb.length;i++)
				if(RandUtil.RI(4)==0)data.thumb[i]=RandUtil.CRF(3);
			
			for(int i=0;i<data.fingers.length;i++){
				float noise=RandUtil.CRF(3);
				for(int j=0;j<data.fingers[i].length;j++){
					if(j==0)data.fingers[i][j]=RandUtil.CRF(3);
					else data.fingers[i][j]=noise;
				}
			}
			return data;
		};
		leftHand.wantedNoise=gen.pocess(leftHand.wantedNoise);
		rightHand.wantedNoise=gen.pocess(rightHand.wantedNoise);
		
	}
	
	public static HandPosition getActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getHeldItemMainhand().getTagCompound().getInteger("AP");
		if(id<0)return CommonHand.errorPos;
		if(id>=HandPosition.values().length)return CommonHand.errorPos;
		return HandPosition.values()[id];
	}
	public static HandPosition getLastActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getHeldItemMainhand().getTagCompound().getInteger("LAP");
		if(id<0)return CommonHand.errorPos;
		if(id>=HandPosition.values().length)return CommonHand.errorPos;
		return HandPosition.values()[id];
	}
	
	public static HandData getRenderLeftHandData(){
		return getRenderHandData(leftHand);
	}
	public static HandData getRenderRightHandData(){
		return getRenderHandData(rightHand);
	}
	private static HandData getRenderHandData(HandDataCollection hand){
		if(hand.calculated.prevValue.fingers[0].length==0)hand.calculated.prevValue.set(hand.calculated.value);
		
		HandData result=new HandData(false);
		result.base=PartialTicksUtil.calculate(hand.calculated.prevValue.base,hand.calculated.value.base);
		result.thumb=PartialTicksUtil.calculate(hand.calculated.prevValue.thumb,hand.calculated.value.thumb);
		result.fingers=PartialTicksUtil.calculate(hand.calculated.prevValue.fingers,hand.calculated.value.fingers);
		result.cubes=hand.calculated.value.cubes;
		result.cubeGlowPrecentage=hand.calculated.value.cubeGlowPrecentage;
		result.calciferiumPrecentage=hand.calculated.value.calciferiumPrecentage;
		return result;
	}
	
	private static void handleSpeed(HandData pos,HandData wantedPos, float speed){
		
		pos.base=UtilM.exponentiallyEqualize(pos.base, wantedPos.base, 1F);
		
		pos.thumb=UtilM.exponentiallyEqualize(pos.thumb, wantedPos.thumb, 1F);
		for(int i=0;i<pos.fingers.length;i++){
			pos.fingers[i]=UtilM.exponentiallyEqualize(pos.fingers[i], wantedPos.fingers[i], 1F);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void handUseAnimation(EntityPlayer player){
		if(activeAnimation!=null)return;
		activeAnimation=CommonHand.rightClickAnimation;
		CommonHand.rightClickAnimation.start();
	}
	@SideOnly(Side.CLIENT)
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
		if(player.worldObj.isRemote)spawnParticle(player);
		else{
			EntityBallOfEnergy entity=new EntityBallOfEnergy(player,2,0);
			entity.setPosition(pos.x,pos.y,pos.z);
			UtilM.spawnEntity(entity);
		}
	}
	@SideOnly(Side.CLIENT)
	private static void spawnParticle(EntityPlayer player){
		unsettleCubes(0.25F,false);
//		List<Vec3M> positions=new ArrayList<Vec3M>();
//		
//		for(int i=0;i<ItemRendererTheHand.get().handModelSolid.rednerLeftover.size();i++)
//			positions.add(ItemRendererTheHand.getVertexInWorld(i));
//		for(int i=0;i<20;i++)
//			positions.add(ItemRendererTheHand.getPalmRand());
//		Vec3M look=Vec3M.conv(player.getLook(PartialTicksUtil.partialTicks)).mul(0.01);
//		positions.forEach(pos1->Particles.MIST_BUBBLE.spawn(pos1,look,0.1F,50,0.5F,new ColorF(1,0.2,0,1)));
	}
	
	
	public static void unsettleCubes(float ammount, boolean handLeft){
		for(DoubleObject<PhysicsVec3F,PhysicsVec3F> obj:getHand(handLeft).cubes){
			obj.obj1.x.speed+=RandUtil.CRF(ammount);
			obj.obj1.y.speed+=RandUtil.CRF(ammount*1.5);
			obj.obj1.z.speed+=RandUtil.CRF(ammount);
		}
	}
	
	public static void update(EntityPlayer player){
		if(UtilM.isNull(UtilC.getTheWorld()))return;
		if(!isActive(player))return;
		
		HandPosition handPos=getActivePosition(player);
		if(handPos==CommonHand.errorPos)setActivePositionId(player, 3);
		
		if(UtilM.isRemote(player))animate(player);
	}
	public static void updateNoise(){
		if(UtilC.getWorldTime()%40==0)generateNewNoiseValue();
		
		ObjectProcessor<HandDataCollection> update=(hand,spee)->{
			hand.noiseSpeed.base=UtilM.graduallyEqualize(hand.noiseSpeed.base, hand.wantedNoise.base, 0.01F);
			
			hand.noiseSpeed.thumb=UtilM.graduallyEqualize(hand.noiseSpeed.thumb, hand.wantedNoise.thumb, 0.01F);
			for(int i=0;i<hand.noiseSpeed.fingers.length;i++){
				hand.noiseSpeed.fingers[i]=UtilM.graduallyEqualize(hand.noiseSpeed.fingers[i], hand.wantedNoise.fingers[i], 0.01F);
			}
			
			return null;
		};
		
//		handleSpeed(noiseSpeed, noise, noiseWanted, 0.01F, 0.00001F, 0.95F);
		update.pocess(leftHand);
		update.pocess(rightHand);
	}
	
}
