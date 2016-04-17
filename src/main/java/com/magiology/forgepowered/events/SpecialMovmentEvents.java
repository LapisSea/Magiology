package com.magiology.forgepowered.events;

import com.magiology.core.init.MItems;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler.Positions;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.m_extension.effect.EntitySmokeFXM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


public class SpecialMovmentEvents{
	public static final SpecialMovmentEvents instance=new SpecialMovmentEvents();
	public void doubleJumpEvent(EntityPlayer player,int x,int y){
		World world=player.worldObj;
		boolean isRemote=world.isRemote;
		ExtendedPlayerData playerData=ExtendedPlayerData.get(player);
		if(UtilM.TRUE()/*!UtilM.isItemInStack(MItems.pants_42I, pantsSlot)||pantsSlot==null*/)return;
//		else if(((Pants_42)pantsSlot.getItem()).hasUpgrade(pantsSlot, MItems.flightUpgrades)==-1)return;
		if(!player.isCollidedVertically&&!player.capabilities.isFlying){
			if(playerData.soulFlame<200)return;
			if(playerData.getJupmCount()>1)return;
			playerData.onJump();
			double xChange=0,yChange=0,zChange=0;
			yChange+=0.7;
			double[] a;
			if(x!=0||y!=0){
				int xRot=0,yRot=0,rot=0;
				if(x==-1)xRot=-180;
				yRot=90*y;
				if(xRot!=0&&yRot!=0)rot=(xRot);
				else{
					if(xRot!=0)rot=xRot;
					if(yRot!=0)rot=yRot;
				}
				a=MathUtil.circleXZ(player.rotationYaw+rot);
//				Helper.printInln(rot,xRot,yRot);
				xChange+=-a[0]*0.5;
				zChange+=a[1]*0.5;
			}else a=MathUtil.circleXZ(player.rotationYaw);
			if(isRemote){
				for(int a1=0;a1<15;a1++){
					float rand=RandUtil.CRF(0.45);
					double[] a2=MathUtil.circleXZ(player.rotationYaw+90);
					double xPos=player.posX-a2[0]*rand+a[0]*0.2,yPos=player.posY-0.9,zPos=player.posZ+a2[1]*rand-a[1]*0.2;
					boolean rb=RandUtil.RI(20)!=0;
					EntitySmoothBubleFX particle=new EntitySmoothBubleFX(world, xPos, yPos, zPos, RandUtil.CRF(0.1)-xChange/10, RandUtil.CRF(0.1)-yChange/10, RandUtil.CRF(0.1)-zChange/10,300, 1, rb?50:0, rb?1:2, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 0.8);
					particle.noClip=false;
					UtilM.spawnEntityFX(particle);
					UtilM.spawnEntityFX(new EntitySmokeFXM(world, xPos, yPos, zPos, RandUtil.CRF(0.1)-xChange, RandUtil.CRF(0.1)-yChange, RandUtil.CRF(0.1)-zChange));
					UtilM.spawnEntityFX(new EntitySmokeFXM(world, xPos, yPos, zPos, RandUtil.CRF(0.1)-xChange, RandUtil.CRF(0.1)-yChange, RandUtil.CRF(0.1)-zChange));
				}
			}
			player.motionX+=xChange;
			player.motionY+=yChange;
			player.motionZ+=zChange;
			if(playerData.getJupmCount()>1){
				if(!player.capabilities.isCreativeMode){
					playerData.setReducedFallDamage(playerData.getReducedFallDamage()+4);
				}
				playerData.soulFlame-=200;
			}
			playerData.sendData();
		}
	}
	public void handleWingPhysics(EntityPlayer player){
		Positions position=WingsFromTheBlackFireHandler.getPos(player);
		if(!UtilM.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2)))return;
		double[] a=MathUtil.circleXZ(player.rotationYaw);
		if(position==Positions.HoverPos||position==Positions.FlyBackvardPos||position==Positions.FlyStationarPos||position==Positions.FlyForvardPos){
			player.motionX*=0.9;
			player.motionY*=0.8;
			player.motionZ*=0.9;
			player.fallDistance=Math.abs((float)(player.motionY*5));
		}else if(position==Positions.HighSpeedPos){
			if(!player.capabilities.isFlying){
				ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
				if(extendedData!=null){
					if(extendedData.soulFlame<3)return;
					extendedData.soulFlame-=3;
				}
				double multi=Math.abs(player.motionY/2);
				player.motionY*=0.7;
				player.motionX-=a[0]*multi;
				player.motionZ+=a[1]*multi;
			}
		}
	}
	public void onFlap(EntityPlayer player,int x,int y,int z){
		Positions position=WingsFromTheBlackFireHandler.getPos(player);
		if(player.capabilities.isFlying)return;
		ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
		if(extendedData!=null){
			if(extendedData.soulFlame<13)return;
			extendedData.soulFlame-=13;
		}
		double[] a=MathUtil.circleXZ(player.rotationYaw);
		a[0]*=0.15;a[1]*=0.15;
		double y1=0.16;
		if(y==1&&extendedData.soulFlame>4){
			extendedData.soulFlame-=4;
			y1=0.3;
		}
		else if(y==-1){
			extendedData.soulFlame+=5;
			y1=-0.05;
		}
		if(position==Positions.FlyBackvardPos){
			player.motionY+=y1;
			player.motionX+=a[0];
			player.motionZ+=-a[1];
		}else if(position==Positions.FlyStationarPos){
			player.motionY+=y1;
		}else if(position==Positions.FlyForvardPos){
			player.motionY+=y1;
			player.motionX-=a[0];
			player.motionZ-=-a[1];
		}
	}
}
