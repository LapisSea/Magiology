package com.magiology.mc_objects.entitys;

import com.magiology.mc_objects.entitys.ai.EntityAIWatchClosestM;
import com.magiology.mc_objects.items.ItemJetpack;
import com.magiology.util.m_extensions.EntityAgeableM;
import com.magiology.util.objs.Vec2FM;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.UtilM;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;

public class EntityPenguin extends EntityAgeableM{
	

	protected static DataParameter<Boolean> IS_LYING_DOWN=EntityDataManager.createKey(EntityPenguin.class, DataSerializers.BOOLEAN);
	protected static DataParameter<Moods> MOOD=EntityDataManager.createKey(EntityPenguin.class, new DataSerializer<Moods>(){
		
		@Override
		public void write(PacketBuffer buf, Moods value){
			buf.writeByte(value.ordinal());
		}
		
		@Override
		public Moods read(PacketBuffer buf){
			try{
				return Moods.values()[buf.readByte()];
			}catch(Exception e){
				return Moods.NEUTRAL;
			}
		}
		
		@Override
		public DataParameter<Moods> createKey(int id){
			return new DataParameter(id, this);
		}
	});
	public static enum Moods{
		NEUTRAL		(true, false,true, false),//when nothing special is happening
		HAPPY		(true, false,true, false),//when a player gives food or if job done
		SAD			(true, false,true, false),//if job failed to be executed (moving stuff and being blocked by terrain) or if an accident occurs 
		SCARED		(true, true, true, false),//when a penguin is hurt
		SCARED2		(false,true, true, true ),//when a mate is killed
		IN_LOVE		(false,false,true, false),//sexy time ;)
		ANGRY		(false,false,true, true ),//after a period of time of being SCARED2 (refuses to work for player unless given a gift)
		LONELY		(true, false,true, false),//when there is no mates around
		CHEERFUL	(false,false,false,false),//when a big project is successfully executed
		ENERGETIC	(false,false,true, false),//when not adult (Grandma: damn kinds wont shut up!)
		PROTECTFUL	(false,false,false,false),//when a female is protecting eggs
		RESPECTFUL	(true, false,true, false);//when passing by the player owner or king penguin
		
		public final boolean willingToWork,runsAway,canMove,isAggressive;

		private Moods(boolean willingToWork, boolean runsAway, boolean canMove, boolean isAggressive){
			this.willingToWork=willingToWork;
			this.runsAway=runsAway;
			this.canMove=canMove;
			this.isAggressive=isAggressive;
		}
		
	}
	
	protected float baseSpeed=0.15F;
	protected int lyingDownStabiliser=0;
	protected EntityAIWatchClosestM watcher;
	
	public EntityPenguin(World worldIn){
		super(worldIn);
		this.dataManager.register(IS_LYING_DOWN, false);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
		setAir(5000);
		setSize(10/16F, 14/16F);
	}
	@Override
	protected void initEntityAI(){
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, watcher=new EntityAIWatchClosestM(this, Entity.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}
	public final Animator animator=isRemote()?new Animator():null;
	public class Animator{

		public Vec3M armTipLeft,armTipRight;//Just a tiiip!
		public float slidingTransition, prevSlidingTransition, swimmingMul, prevSwimmingMul;
		public Vec2FM armRotLeft1=new Vec2FM(),armRotLeft2=new Vec2FM(),armRotRight1=new Vec2FM(),armRotRight2=new Vec2FM();
		
		private void update(){
			armTipLeft=null;
			armTipRight=null;
			
			prevSlidingTransition=slidingTransition;
			prevSwimmingMul=swimmingMul;
			
			slidingTransition=UtilM.graduallyEqualize(slidingTransition, getIsLyingDown()?1:0, 0.2F);
			swimmingMul=UtilM.graduallyEqualize(slidingTransition, getIsLyingDown()&&isInWater()?1:0, 0.2F);
		}
		public Vec3M getArmTipLeft(){
			if(armTipLeft==null)armTipLeft=getPos().add(0,0,0);
			return armTipLeft;
		}
		public Vec3M getArmTipRight(){
			if(armTipRight==null)armTipRight=getPos().add(0,0,0);
			return armTipRight;
		}
	}
	
	@Override
	public void onEntityUpdate(){
		if(isRemote())animator.update();
		
		super.onEntityUpdate();
		if(server()){
			BlockPos pos=this.getPosition();
			Biome biome=worldObj.provider.getBiomeForCoords(pos);
			
			if(UtilM.peridOf(this, 10)&&biome.getTempCategory()==TempCategory.WARM){
				addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 1));
			}
			
			double speed;
			boolean stateLyingDown=false;
			IBlockState steppingState=worldObj.getBlockState(pos.add(0, -1, 0));
			
			if(isInWater()){
				speed=baseSpeed*25;
				stateLyingDown=true;
			}else{
				float slippySloo=steppingState.getBlock().slipperiness/0.6F;
				if(slippySloo>1){
					speed=slippySloo*baseSpeed*1.4;
					stateLyingDown=true;
				}else speed=baseSpeed;
			}
			getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
			
			if(lyingDownStabiliser!=0&&lyingDownStabiliser>0!=stateLyingDown)lyingDownStabiliser=0;
			
			if(stateLyingDown)lyingDownStabiliser++;
			else lyingDownStabiliser--;
			
			if(Math.abs(lyingDownStabiliser)>5)setIsLyingDown(stateLyingDown);
			
//			setIsLyingDown(lyingDownStabiliser>0.5F)
//			PrintUtil.println(getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
		}else{
//			Particles.CUBE.spawn(this.getPos(), new Vec3M(RandUtil.CRD(0.005),RandUtil.CRD(0.005),RandUtil.CRD(0.005)).sub(posX-prevPosX, posY-prevPosY, posZ-prevPosZ), 1.5F/16, 150, 0, ColorF.randomRGB());
		}
	}
	@Override
	public boolean canBreatheUnderwater(){
		return false;
	}
	
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, @Nullable ItemStack stack){
		switch(slotIn.getSlotType()){
		case HAND:{
			super.setItemStackToSlot(slotIn, stack);
		}break;
		case ARMOR:{
			if(slotIn==EntityEquipmentSlot.CHEST){
				if(UtilM.isItemInStack(ItemJetpack.get(), stack))super.setItemStackToSlot(slotIn, stack);
			}
			else if(slotIn==EntityEquipmentSlot.HEAD){
				if(UtilM.isItemInStack(ItemJetpack.get(), stack))super.setItemStackToSlot(slotIn, stack);
			}
		}
		}
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata){
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	public boolean getIsLyingDown(){
		return dataManager.get(IS_LYING_DOWN);
	}
	
	public void setIsLyingDown(boolean isSliding){
		dataManager.set(IS_LYING_DOWN, isSliding);
	}

	public boolean hasJetpack(){
		return getItemStackFromSlot(EntityEquipmentSlot.CHEST)!=null;
	}

	
	public boolean hasHelemt(){
		return getItemStackFromSlot(EntityEquipmentSlot.HEAD)!=null;
	}
	public PathNavigate path(){
		return this.navigator;
	}
	public double getActualMotionX(){
		return this.posX-this.prevPosX;
	}
	public double getActualMotionY(){
		return this.posY-this.prevPosY;
	}
	public double getActualMotionZ(){
		return this.posZ-this.prevPosZ;
	}
	
	public Vec3M getActualMotion(){
		return new Vec3M(getActualMotionX(), getActualMotionY(), getActualMotionZ());
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable){
		EntityPenguin child=new EntityPenguin(worldObj);
		return child;
	}
}
