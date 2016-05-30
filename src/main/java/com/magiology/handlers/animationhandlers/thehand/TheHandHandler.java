package com.magiology.handlers.animationhandlers.thehand;

import com.magiology.client.render.itemrender.ItemRendererTheHand;
import com.magiology.core.init.MItems;
import com.magiology.forgepowered.packets.packets.toserver.HandActionPacket;
import com.magiology.handlers.animationhandlers.thehand.animation.AnimationEvent;
import com.magiology.handlers.animationhandlers.thehand.animation.AnimationPart;
import com.magiology.handlers.animationhandlers.thehand.animation.AnimationPart.AnimationFactory;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimData;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimation;
import com.magiology.handlers.animationhandlers.thehand.animation.HandAnimationBase;
import com.magiology.handlers.animationhandlers.thehand.animation.LinearHandAnimProgressHandler;
import com.magiology.handlers.animationhandlers.thehand.animation.LinearHandAnimation;
import com.magiology.mcobjects.entitys.EntityBallOfEnergy;
import com.magiology.util.renderers.ValueWithPrev;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.ArrayMath;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;
import com.magiology.util.utilobjects.codeinsert.ObjectReturn;
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
	private static HandDataCollection rightHand=new HandDataCollection();
	
	
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
		activeAnimation=HandAnimData.chargeUp;
	}
	@SideOnly(Side.CLIENT)
	public static void animate(EntityPlayer player){
		
//		setActivePositionId(player, 2);
//		PrintUtil.println(pos.name);
		ItemRendererTheHand.get().secure();
		
		animateHand(player, rightHand);
		
	}
	
	@SideOnly(Side.CLIENT)
	private static void animateHand(EntityPlayer player, HandDataCollection hand){
		try {
			HandAnimData.chargeUpRelease=new LinearHandAnimation(3,HandAnimData.chargeUpEnd,new LinearHandAnimProgressHandler(){
				protected AnimationEvent shootEvent=new AnimationEvent(()->{
					Vec3M pos=ItemRendererTheHand.getPalmMiddle();
					TheHandHandler.shoot(UtilC.getThePlayer(),pos);
					UtilM.sendMessage(new HandActionPacket(0,pos));
				},()->timeHeld==10);
				
				protected int timeMul=1;
				
				@Override
				public float getProgress(){
					return UtilC.fluctuate(80, 0)-0.001F;//progress;//
				}
				@Override
				public boolean isInactive(){
					boolean inactive=getProgress()==1;
					if(inactive){
						timeHeld=0;
						shootEvent.called=false;
					}
					return inactive;
				}
				
				@Override
				public void onHoldingEnd(){
					timeMul=1;
				}

				@Override
				public void onHoldingStart(){
					timeMul=1;
				}

				@Override
				public void update(){
					timeHeld=MathUtil.snap(timeHeld+timeMul, 0, 20);
					progress=timeHeld/(float)20;
					if(shootEvent.getShouldCall().get()){
						shootEvent.called=true;
						shootEvent.getOnEvent().run();
					}
				}
				@Override
				public boolean willRestrictItemSwitching(){
					return true;
				}
			}, new ObjectReturn<AnimationPart[]>(){
				@Override
				public AnimationPart[] process(){
					//close fist
					AnimationFactory factory=new AnimationFactory();
					factory.setGroup(2)
						.gen(0,  2,6)
						.gen(1,  2,23F)
						.gen(2,  2,9)
						.gen(3,  2,22);
					
					factory.setGroup(3)
						.gen(0,  2,1F)
						.gen(1,  2,20)
						.gen(2,  2,14)
						.gen(3,  2,24);

					factory.setGroup(4)
						.gen(0,  3,-1F)
						.gen(1,  3,11)
						.gen(2,  3,12)
						.gen(3,  3,20);
					
					factory.setGroup(5)
						.gen(0,  3,-4F)
						.gen(1,  3,8)
						.gen(2,  3,7)
						.gen(3,  3,20);
					
					factory.setGroup(1)
						.setDelay(1)
						.gen(0,  2,-3F)
						.gen(1,  2,-5F)
						.gen(3,  2,-6F)
						.gen(4,  2,-7F)
						.setDelay(0);
					
					return factory.compile();
				}
			}.process(),"chargeUpRelease");
			activeAnimation=HandAnimData.chargeUpRelease;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
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
		
		handleSpeed(hand.pos, wanted, 0.8F);
		updateNoise();
		
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
		if(hand.cubes[0].obj1.x.wantedPoint==0){
			hand.cubes[1].obj1.x.wantedPoint=p*0.75F;
			hand.cubes[2].obj1.x.wantedPoint=p*0.75F;
			hand.cubes[2].obj1.z.wantedPoint=p*0.75F;
			hand.cubes[3].obj1.z.wantedPoint=p*0.75F;
			
			hand.cubes[4].obj1.x.wantedPoint=p*0.75F;
			hand.cubes[4].obj1.y.wantedPoint=p*0.75F;
			hand.cubes[5].obj1.x.wantedPoint=p*0.75F;
			hand.cubes[5].obj1.z.wantedPoint=p*0.75F;
			hand.cubes[5].obj1.y.wantedPoint=p*0.75F;
			hand.cubes[6].obj1.z.wantedPoint=p*0.75F;
			hand.cubes[6].obj1.y.wantedPoint=p*0.75F;
			hand.cubes[7].obj1.y.wantedPoint=p*0.75F;
		}
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
		rightHand.wantedNoise=gen.process(rightHand.wantedNoise);
		
	}
	
	public static HandPosition getActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getHeldItemMainhand().getTagCompound().getInteger("AP");
		if(id<0)return HandAnimData.errorPos;
		if(id>=HandPosition.values().length)return HandAnimData.errorPos;
		return HandPosition.values()[id];
	}
	public static HandPosition getLastActivePosition(EntityPlayer player){
		if(!isActive(player))return null;
		int id=player.getHeldItemMainhand().getTagCompound().getInteger("LAP");
		if(id<0)return HandAnimData.errorPos;
		if(id>=HandPosition.values().length)return HandAnimData.errorPos;
		return HandPosition.values()[id];
	}
	
	public static HandData getRenderHandData(){
		HandDataCollection hand=rightHand;
		if(hand.calculated.prevValue.fingers[0].length==0)hand.calculated.prevValue.set(hand.calculated.value);
		
		HandData result=new HandData(false);
		result.base=PartialTicksUtil.calculate(hand.calculated.prevValue.base,hand.calculated.value.base);
		result.thumb=PartialTicksUtil.calculate(hand.calculated.prevValue.thumb,hand.calculated.value.thumb);
		result.fingers=PartialTicksUtil.calculate(hand.calculated.prevValue.fingers,hand.calculated.value.fingers);
		result.cubes=hand.cubes;
		result.cubeGlowPrecentage=hand.calculated.value.cubeGlowPrecentage;
		result.calciferiumPrecentage=hand.calculated.value.calciferiumPrecentage;
		return result;
	}
	
	private static void handleSpeed(HandData pos,HandData wantedPos, float speed){
		
		pos.base=UtilM.exponentiallyEqualize(pos.base, wantedPos.base, speed);
		
		pos.thumb=UtilM.exponentiallyEqualize(pos.thumb, wantedPos.thumb, speed);
		for(int i=0;i<pos.fingers.length;i++){
			pos.fingers[i]=UtilM.exponentiallyEqualize(pos.fingers[i], wantedPos.fingers[i], speed);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void handUseAnimation(EntityPlayer player){
		if(activeAnimation!=null)return;
		activeAnimation=HandAnimData.rightClickAnimation;
		HandAnimData.rightClickAnimation.start();
	}
	@SideOnly(Side.CLIENT)
	public static void init(){
		HandAnimData.load();
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
		if(values[now]==HandAnimData.errorPos)now++;
		if(now==values.length)now=0;
		setActivePositionId(player, now);
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
		for(DoubleObject<PhysicsVec3F,PhysicsVec3F> obj:rightHand.cubes){
			obj.obj1.x.speed+=RandUtil.CRF(ammount);
			obj.obj1.y.speed+=RandUtil.CRF(ammount*1.5);
			obj.obj1.z.speed+=RandUtil.CRF(ammount);
		}
	}
	
	public static void update(EntityPlayer player){
		if(UtilM.isNull(UtilC.getTheWorld()))return;
		if(!isActive(player))return;
		
		HandPosition handPos=getActivePosition(player);
		if(handPos==HandAnimData.errorPos)setActivePositionId(player, 3);
		
		if(UtilM.isRemote(player))animate(player);
	}
	public static void updateNoise(){
		if(UtilC.getWorldTime()%40==0)generateNewNoiseValue();
		
		ObjectProcessor<HandDataCollection> update=(hand,spee)->{
//			hand.noiseSpeed=new HandData();
//			hand.noise=new HandData();
			for(int i=0;i<hand.noiseSpeed.base.length;i++){
				hand.noiseSpeed.base[i]=UtilM.handleSpeedFolower(hand.noiseSpeed.base[i], hand.noise.base[i], hand.wantedNoise.base[i], 0.001F*(i<3?0.002F:1))*0.98F;
			}
			for(int i=0;i<hand.noiseSpeed.thumb.length;i++){
				hand.noiseSpeed.thumb[i]=UtilM.handleSpeedFolower(hand.noiseSpeed.thumb[i], hand.noise.thumb[i], hand.wantedNoise.thumb[i], 0.001F)*0.98F;
			}
			
			
			for(int i=0;i<hand.noiseSpeed.fingers.length;i++){
				for(int j=0;j<hand.noiseSpeed.fingers[i].length;j++){
					hand.noiseSpeed.fingers[i][j]=UtilM.handleSpeedFolower(hand.noiseSpeed.fingers[i][j], hand.noise.fingers[i][j], hand.wantedNoise.fingers[i][j], 0.001F)*0.98F;
				}
			}
			hand.noise.set(hand.noise.add(hand.noiseSpeed));
			return null;
		};
		
		update.process(rightHand);
	}
	
}
