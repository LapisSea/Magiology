package com.magiology.mc_objects.entitys;

import javax.annotation.Nullable;
import javax.vecmath.Vector2d;

import com.magiology.mc_objects.entitys.ai.EntityAIWatchClosestM;
import com.magiology.mc_objects.items.ItemJetpack;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.EntityAgeableM;
import com.magiology.util.objs.data_parameter_wappers.DataParamBlockPos;
import com.magiology.util.objs.data_parameter_wappers.DataParamBoolean;
import com.magiology.util.objs.data_parameter_wappers.DataParamEnum;
import com.magiology.util.objs.data_parameter_wappers.DataParamFloat;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityPenguin extends EntityAgeableM{
	
	
	protected static final DataParamBoolean IS_LYING_DOWN=new DataParamBoolean(EntityPenguin.class);
	protected static final DataParamEnum<Moods, Entity> MOOD=new DataParamEnum(EntityPenguin.class, Moods.NEUTRAL);
	protected static final DataParamFloat STAMINA=new DataParamFloat(EntityPenguin.class);
	protected static final DataParamBlockPos AI_TARGET=new DataParamBlockPos(EntityPenguin.class);
	
	public static enum BehaviorPolicies{
		NOTHING,
		ATTACK_ON_APPROACH, //attacks if target walks in to range
		ATTACK_ON_SIGHT,
		WILL_WORK_ALWAYS
	}
	
	public static enum Moods{
		NEUTRAL(true, false, true, BehaviorPolicies.NOTHING), //when nothing special is happening
		HAPPY(true, false, true, BehaviorPolicies.NOTHING), //when a player gives food or if job done
		SAD(true, false, true, BehaviorPolicies.NOTHING), //if job failed to be executed or if an accident occurs
		SCARED(true, true, true, BehaviorPolicies.NOTHING), //when a penguin is hurt
		SCARED2(false, true, true, BehaviorPolicies.ATTACK_ON_APPROACH), //when a mate is killed
		IN_LOVE(false, false, true, BehaviorPolicies.NOTHING), //sexy time ;)
		ANGRY(false, false, true, BehaviorPolicies.ATTACK_ON_SIGHT), //after a period of time of being SCARED2 (refuses to work for player unless given a gift)
		LONELY(true, false, true, BehaviorPolicies.NOTHING), //when there is no mates around
		CHEERFUL(false, false, false, BehaviorPolicies.NOTHING), //when a big project is successfully executed
		ENERGETIC(false, false, true, BehaviorPolicies.NOTHING), //when not adult (Grandma: damn kinds wont shut up!)
		PROTECTFUL(false, false, false, BehaviorPolicies.ATTACK_ON_APPROACH), //when a female is protecting eggs
		RESPECTFUL(true, false, true, BehaviorPolicies.NOTHING);//when passing by the player owner or king penguin
		
		public final boolean runsAway, canMove, willingToWork;
		public final BehaviorPolicies aggressive;
		
		private Moods(boolean willingToWork, boolean runsAway, boolean canMove, BehaviorPolicies aggressive){
			this.willingToWork=willingToWork;
			this.runsAway=runsAway;
			this.canMove=canMove;
			this.aggressive=aggressive;
		}
	}
	
	protected float baseSpeed=0.15F;
	protected int lyingDownStabiliser=0;
	protected EntityAIWatchClosestM watcher;
	
	public EntityPenguin(World worldIn){
		super(worldIn);
		IS_LYING_DOWN.register(this, false);
		MOOD.register(this, Moods.NEUTRAL);
		STAMINA.register(this, 1);
		AI_TARGET.register(this, new BlockPos(0,-1,0));
		setAISpeed(0.3);
		setSize(10/16F, 14/16F);
	}
	
	@Override
	protected void initEntityAI(){
		
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.25D));
		tasks.addTask(6, new EntityAIWander(this, 1.0D));
		tasks.addTask(7, watcher=new EntityAIWatchClosestM(this, Entity.class, 6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
	}
	
	public final Animator animator=isRemote()?new Animator():null;
	
	public class Animator{
		
		
		public Vec3M armTipLeft, armTipRight;//Just a tiiip!
		public float slidingTransition, prevSlidingTransition, swimmingMul, prevSwimmingMul,swimRot,prevSwimRot;
		public Vec2FM armRotLeft1=new Vec2FM(), armRotLeft2=new Vec2FM(), armRotRight1=new Vec2FM(), armRotRight2=new Vec2FM();
		
		private void update(){
			armTipLeft=null;
			armTipRight=null;
			prevSlidingTransition=slidingTransition;
			prevSwimmingMul=swimmingMul;
			prevSwimRot=swimRot;
			slidingTransition=UtilM.graduallyEqualize(slidingTransition, getIsLyingDown()?1:0, 0.2F);
			swimmingMul=UtilM.graduallyEqualize(slidingTransition, isInWater()?1:0, 0.2F);
			swimRot=UtilM.exponentiallyEqualize(swimRot, -(float)Math.toDegrees(Math.atan2(getActualMotionY(), new Vector2d(getActualMotionX(),getActualMotionZ()).length())), 20);
		}
		
		public Vec3M getArmTipLeft(){
			if(armTipLeft==null) armTipLeft=getPos().add(0, 0, 0);
			return armTipLeft;
		}
		
		public Vec3M getArmTipRight(){
			if(armTipRight==null) armTipRight=getPos().add(0, 0, 0);
			return armTipRight;
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound){
		super.readEntityFromNBT(compound);
		IS_LYING_DOWN.set(this, compound.getBoolean("LyingDown"));
		STAMINA.set(this, compound.getFloat("Stamina"));
		MOOD.set(this, compound.getByte("Mood"));
		int[] pos=compound.getIntArray("aiTarg");
		if(pos.length==0)AI_TARGET.set(this, new BlockPos(0,0,0));
		else AI_TARGET.set(this, new BlockPosM(pos));
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound){
		super.writeEntityToNBT(compound);
		compound.setBoolean("LyingDown", IS_LYING_DOWN.get(this));
		compound.setFloat("Stamina", STAMINA.get(this));
		compound.setByte("Mood", MOOD.get(this));
		BlockPos pos=AI_TARGET.get(this);
		compound.setIntArray("aiTarg", new int[]{pos.getX(),pos.getY(),pos.getZ()});
	}
	
	@Override
	public void onEntityUpdate(){
		if(isRemote())animator.update();
		super.onEntityUpdate();
		
		
		
		if(server()){
			EntityPlayer pl=world.getClosestPlayer(posX, posY, posZ, 20, false);
			if(pl!=null){
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, pl.getHeldItemMainhand());
			}
			if(navigator!=null&&navigator.getPath()!=null)AI_TARGET.set(this, new BlockPos(navigator.getPath().getFinalPathPoint().xCoord,navigator.getPath().getFinalPathPoint().yCoord,navigator.getPath().getFinalPathPoint().zCoord));
			else AI_TARGET.set(this, new BlockPos(0,-1,0));
			
			BlockPos pos=this.getPosition();
			//Biome biome=worldObj.provider.getBiomeForCoords(pos);
			//
			//if(UtilM.peridOf(this, 10)&&biome.getTempCategory()==TempCategory.WARM){
			//	addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 1));
			//}
			double speed;
			boolean stateLyingDown=false;
			IBlockState steppingState=world.getBlockState(pos.add(0, -1, 0));
			if(isInWater()){
				speed=baseSpeed*2.5;
				stateLyingDown=true;
			}else{
				float slippySloo=steppingState.getBlock().slipperiness/0.6F;
				if(slippySloo>1){
					speed=slippySloo*baseSpeed*1.4;
					stateLyingDown=true;
				}else speed=baseSpeed;
			}
			setAISpeed(speed);
			if(lyingDownStabiliser!=0&&lyingDownStabiliser>0!=stateLyingDown) lyingDownStabiliser=0;
			if(stateLyingDown) lyingDownStabiliser++;
			else lyingDownStabiliser--;
			if(Math.abs(lyingDownStabiliser)>5) setIsLyingDown(stateLyingDown);
			//setIsLyingDown(lyingDownStabiliser>0.5F)
			//PrintUtil.println(getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
		}else{
			//Particles.CUBE.spawn(this.getPos(), new Vec3M(RandUtil.CRD(0.005),RandUtil.CRD(0.005),RandUtil.CRD(0.005)).sub(posX-prevPosX, posY-prevPosY, posZ-prevPosZ), 1.5F/16, 150, 0, ColorF.randomRGB());
		}
		
	}
	
	@Override
	protected int decreaseAirSupply(int air){
		int i=EnchantmentHelper.getRespirationModifier(this);
		return i>0&&rand.nextInt((i+1)*4)>0?air:air-1;
	}
	
	
	@Override
	public boolean canBreatheUnderwater(){
		return isInsideOfMaterial(Material.WATER);
	}
	
	@Override
	protected boolean canDespawn(){
		return false;
	}
	
	@Override
	public boolean handleWaterMovement(){
		if(this.getRidingEntity() instanceof EntityBoat){
			inWater=false;
		}else{
			if(world.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D), Material.WATER, this)){
				
				if(!inWater&&!firstUpdate){
					this.resetHeight();
				}
				fallDistance=0.0F;
				inWater=true;
				setFire(0);
			}else{
				inWater=false;
			}
		}
		return inWater;
	}
	
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, @Nullable ItemStack stack){
		switch(slotIn.getSlotType()){
		case HAND:{
			super.setItemStackToSlot(slotIn, stack);
		}
		break;
		case ARMOR:{
			if(slotIn==EntityEquipmentSlot.CHEST){
				if(UtilM.isItemInStack(ItemJetpack.get(), stack)) super.setItemStackToSlot(slotIn, stack);
			}else if(slotIn==EntityEquipmentSlot.HEAD){
				if(UtilM.isItemInStack(ItemJetpack.get(), stack)) super.setItemStackToSlot(slotIn, stack);
			}
		}
		}
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata){
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	public boolean getIsLyingDown(){
		return IS_LYING_DOWN.get(this);
	}
	
	public void setIsLyingDown(boolean isSliding){
		IS_LYING_DOWN.set(this, isSliding);
	}
	
	public Moods getMood(){
		return MOOD.getEnum(this);
	}
	
	public void setMood(Moods mood){
		MOOD.setEnum(this, mood);
	}
	
	public float getStamina(){
		return STAMINA.get(this);
	}
	
	public void setStamina(float isSliding){
		STAMINA.set(this, isSliding);
	}
	
	public boolean hasJetpack(){
		return getItemStackFromSlot(EntityEquipmentSlot.CHEST)!=null;
	}
	
	public boolean hasHelemt(){
		return getItemStackFromSlot(EntityEquipmentSlot.HEAD)!=null;
	}
	
	public PathNavigate path(){
		return navigator;
	}
	
	public double getActualMotionX(){
		return posX-prevPosX;
	}
	
	public double getActualMotionY(){
		return posY-prevPosY;
	}
	
	public double getActualMotionZ(){
		return posZ-prevPosZ;
	}
	
	public Vec3M getActualMotion(){
		return new Vec3M(getActualMotionX(), getActualMotionY(), getActualMotionZ());
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable){
		EntityPenguin child=new EntityPenguin(world);
		child.setGrowingAge(-24000*5);//TODO: 5 mc days config this shit
		return child;
	}
	
	protected void setAISpeed(double speed){
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
	}
	
	protected double getAISpeed(){
		return getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
	}
	
	public BlockPos getAiTarget(){
		return AI_TARGET.get(this);
	}
}
