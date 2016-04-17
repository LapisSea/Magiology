package com.magiology.handlers.animationhandlers;

import com.magiology.SoundM;
import com.magiology.core.Config;
import com.magiology.core.init.MItems;
import com.magiology.forgepowered.packets.packets.UploadPlayerDataPacket;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.CyborgWingsFromTheBlackFireData;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.SimpleCounter;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WingsFromTheBlackFireHandler{
	public static enum Positions{
		ErrorPos(false,counter.getAndAdd(),new float[][]{{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0}}),
		FlapBackwardsPos(false,counter.getAndAdd(),new float[][]{{0,-1.5F,-2.5F},{0,-1,-3},{0,-1.5F,-3.5F},{0,-1,-4},{0,-1.5F,-1},{0,-0.5F,-1},{0,-0.5F,-1}}),
		FlapDownPos(false,counter.getAndAdd(),new float[][]{{-17,-3,-3},{-13,0,-2},{-9,0,0},{8,0,0},{7,0,0},{6,0,0},{5,0,0}}),
		FlapForwardPos(false,counter.getAndAdd(),new float[][]{{0,2,2.5F},{0,2.5F,3},{0,3.5F,3.5F},{0,1,4},{0,1.5F,1},{0,0.5F,1},{0,0.5F,1}}),
		FlapUpPos(false,counter.getAndAdd(),new float[][]{{17,3,3},{15,0,2},{12,0,1},{-8,0,0},{-7,0,0},{-6,0,0},{-5,0,0}}),
		FlyBackvardPos(true,counter.getAndAdd(),new float[][]{{10,20,-10},{-5,10,-5},{-5,-10,-5},{0,-10,-5},{-5,-10,-5},{-5,-0,0},{-5,0,0}}),
		FlyForvardPos(true,counter.getAndAdd(),new float[][]{{10,10,20},{0,10,0},{0,10,0},{0,-10,0},{0,-10,0},{-2,-4,0},{-3,-6,0}}),
		FlyStationarPos(true,counter.getAndAdd(),new float[][]{{10,10,0},{0,10,0},{0,-10,0},{0,-10,0},{0,-10,0},{-2,-4,0},{-3,-6,0}}),
		HighSpeedPos(true,counter.getAndAdd(),new float[][]{{-10,-20,0},{-10,-20,0},{10,-15,0},{0,-20,20},{-5,0,0},{-5,0,0},{-10,0,0}}),
		HoverPos(true,counter.getAndAdd(),new float[][]{{10,20,-10},{-10,10,0},{-10,-10,0},{0,-10,0},{-10,-10,0},{-10,-10,0},{-10,-5,0}}),
		NormalPos(true,counter.getAndAdd(),new float[][]{{-30,10,-30},{-20,-20,-5},{-10,-20,-5},{-10,-20,-5},{-10,-30,-5},{-10,-30,-5},{-10,-30,-5}}),
		ProtectivePos(true,counter.getAndAdd(),new float[][]{{6,5,-30},{-40,10,-10},{-55,0,-5},{-45,30,-5},{-40,20,0},{-20,10,10},{-20,-10,0}});
		public static Positions get(int id){
			if(id>-1&&id<values().length)return values()[id]; 
			return ErrorPos;
		}
		public int id;
		public boolean wanted;
		public float[][] wantedRotationAnglesBase;
		private Positions(boolean wanted,int id,float[][] wantedRotationAnglesBase){
			this.wantedRotationAnglesBase=wantedRotationAnglesBase;
			this.id=id;this.wanted=wanted;
		}
	}
	static SimpleCounter counter=new SimpleCounter();
	public static WingsFromTheBlackFireHandler instaince=new WingsFromTheBlackFireHandler();
	//static noise section
	public static float[][]noiseSpeedRotationAnglesBase=new float[7][3],wantedNoiseRotationAnglesBase=new float[7][3],noiseRotationAnglesBase=new float[7][3];
	static float p=1F/16F;
	@SideOnly(Side.CLIENT)
	public static void animateModel(EntityPlayer player){
		if(!player.worldObj.isRemote)return;
		CyborgWingsFromTheBlackFireData data=ComplexPlayerRenderingData.getFastCyborgWingsFromTheBlackFireData(player);
		Positions pos=getPos(player);
		//prev variables update
		data.calcPrevRotationAnglesBase=data.calcRotationAnglesBase.clone();
		data.prevPlayerAngle=data.playerAngle;
		//---------------------
		boolean active=getIsActive(player)&&pos!=null;
		if(active){
			try{data.canFlap=(pos==Positions.FlyStationarPos||pos==Positions.FlyForvardPos||pos==Positions.FlyBackvardPos)&&!player.isCollidedVertically;}catch(Exception e){data.canFlap=false;}
			if(data.canFlap||pos==Positions.HighSpeedPos){
				double[] a=MathUtil.circleXZ(-player.rotationYaw);
				int x1=a[0]>0?1:-1,z1=a[1]>0?1:-1;
				double rot=(player.motionX*x1+player.motionZ*z1)*90;
				if(rot!=0)rot-=rot/10;
				if(rot<0)rot/=2F;
				data.playerAngle=(float)UtilM.slowlyEqualize(data.playerAngle,rot, 1.5);
			}
			else data.playerAngle=UtilM.slowlyEqualize(data.playerAngle, 0, 2);
			
			if(data.playerAngle>80)data.playerAngle=80;
			
			Positions[] flap=getFlap(player);
			for(int a=0;a<data.rotationAnglesBase.length;a++)for(int b=0;b<data.rotationAnglesBase[a].length;b++){
				data.rotationAnglesBase[a][b]=UtilM.slowlyEqualize(data.rotationAnglesBase[a][b], pos.wantedRotationAnglesBase[a][b], 1+2*RandUtil.RF());
				ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
				if(data.canFlap)data.flapAnglesBase[a][b]=(float)UtilM.slowlyEqualize(data.flapAnglesBase[a][b], flap[0].wantedRotationAnglesBase[a][b]+flap[1].wantedRotationAnglesBase[a][b], (3.3+RandUtil.RF())*(extendedData!=null?extendedData.soulFlame<10?0.5:1:1));
				else data.flapAnglesBase[a][b]=(float)UtilM.slowlyEqualize(data.flapAnglesBase[a][b], 0, 1.5+2*RandUtil.RF());
			}
		}else for(int a=0;a<data.rotationAnglesBase.length;a++)for(int b=0;b<data.rotationAnglesBase[a].length;b++){
			data.rotationAnglesBase[a][b]=0;
			data.calcRotationAnglesBase[a][b]=0;
			data.wantedRotationAnglesBase[a][b]=0;
			data.calcPrevRotationAnglesBase[a][b]=0;
		}
		updateStaticNoise(player.worldObj.getTotalWorldTime());
		data.calcRotationAnglesBase=MathUtil.addToDoubleFloatArray(data.flapAnglesBase,MathUtil.addToDoubleFloatArray(data.rotationAnglesBase,noiseRotationAnglesBase));
	}
	public static Positions[] getFlap(EntityPlayer player){
		
		int random=0,speed=8;
		char[] playerName=player.getName().toCharArray();
		for(int a=0;a<playerName.length;a++)random+=a*playerName[a];
		random=(int)(random*1.6743546);
		long time=player.worldObj.getTotalWorldTime()+random;
		int timer=(int)(time%(speed*2));
		ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
		int x=extendedData!=null?extendedData.getKeysX():0;
		Positions mainFlap=Positions.FlapDownPos;
		if(timer>=speed){
			x*=-1;
			mainFlap=Positions.FlapUpPos;
		}
		Positions xFlap=x==1?Positions.FlapForwardPos:x==-1?Positions.FlapBackwardsPos:Positions.ErrorPos;
		if(!player.worldObj.isRemote&&timer==0){
			Positions id=getPos(player);
			int y=extendedData.getKeysY();
			if(mainFlap==Positions.FlapDownPos&&(id==Positions.FlyForvardPos||id==Positions.FlyStationarPos||id==Positions.ErrorPos))UtilM.playSoundAtEntity(SoundM.WingSwingFX, player, 0.3+(y!=0?(y==1?0.2:-0.1):0), 1);
		}
		return new Positions[]{mainFlap,xFlap};
	}
	public static boolean getIsActive(EntityPlayer player){
		if(player==null)return false;
		if(!UtilM.isItemInStack(MItems.WingsFTBFI, player.getCurrentArmor(2)))return false;
		return player.getCurrentArmor(2).hasTagCompound();
	}
	public static Positions getPos(EntityPlayer player){
//		if(H.TRUE())return Positions.ErrorPos;
		Config.setWingsThick(true);
		if(player.capabilities.isFlying)return Positions.NormalPos;
		int id=getPosId(player);
		Positions result=id>=0?Positions.values()[id]:Positions.ErrorPos;
		ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
		if(extendedData==null)return Positions.ErrorPos;
		if(result!=Positions.ProtectivePos&&(extendedData.getJupmCount()==0||player.isOnLadder()))return Positions.NormalPos;
		if(result==Positions.HighSpeedPos){
			int x=(int) player.posX,y=(int) player.posY,z=(int) player.posZ;
			if(x<0)x--;if(y<0)y--;if(z<0)z--;
			for(int a=0;a<6;a++){
				Material mat=UtilM.getBlock(player.worldObj, new BlockPos(x, y-a, z)).getMaterial();
				if(mat!=Material.air){
					if(player.fallDistance>3){
						if(player.worldObj.isRemote){
							CyborgWingsFromTheBlackFireData data=ComplexPlayerRenderingData.getFastCyborgWingsFromTheBlackFireData(player);
							Positions[] flaps=getFlap(player);
							if(data!=null)for(int a1=0;a1<data.rotationAnglesBase.length;a1++)for(int b=0;b<data.rotationAnglesBase[a1].length;b++)data.rotationAnglesBase[a1][b]=UtilM.slowlyEqualize(data.rotationAnglesBase[a1][b], flaps[0].wantedRotationAnglesBase[a1][b]+flaps[1].wantedRotationAnglesBase[a1][b], 1+2*RandUtil.RF());
						}
						return Positions.HoverPos;
					}
					if(result==Positions.HighSpeedPos&&extendedData.getKeysX()!=1)return Positions.NormalPos;
					return Positions.NormalPos;
				}
			}
		}
		if(result==Positions.HighSpeedPos&&extendedData.getKeysX()!=1)return Positions.NormalPos;
		ItemStack wings=player.getCurrentArmor(2);
		if(getIsActive(player)&&!wings.getTagCompound().getBoolean("HS"))return Positions.NormalPos;
		
		if(result==Positions.HoverPos&&player.motionY>=0)return Positions.NormalPos;
		return result;
	}
	public static int getPosId(EntityPlayer player){
		int id=-1;
		ItemStack wings=player.getCurrentArmor(2);
		if(getIsActive(player))id=wings.getTagCompound().getInteger("WMID");
		return id;
	}
	public static void nextPosition(EntityPlayer player){
		CyborgWingsFromTheBlackFireData data=ComplexPlayerRenderingData.getFastCyborgWingsFromTheBlackFireData(player);
		ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
		if(UtilM.isNull(data,extendedData))return;
		int posId=getPosId(player),finishHim=posId;
		Positions[] val=Positions.values();
		if(posId==2||posId==3||posId==4)finishHim=5;
		else{
			try{do{finishHim++;}while(!val[finishHim].wanted);}
			catch(Exception e){finishHim=0;}
		}
		setPosId(player, finishHim);
	}
	public static void setFlap(EntityPlayer player, Positions flap, Positions xFlap){setFlap(player, new Positions[]{flap,xFlap});}
	public static void setFlap(EntityPlayer player, Positions[] flap){
		CyborgWingsFromTheBlackFireData data=ComplexPlayerRenderingData.getFastCyborgWingsFromTheBlackFireData(player);
		data.flap=flap.clone();
	}
	public static void setPosId(EntityPlayer player,int id){
		ItemStack wings=player.getCurrentArmor(2);
		if(!getIsActive(player))return;
		wings.getTagCompound().setInteger("WMID", id);
	}
	public static void updateModel(EntityPlayer player){
			ExtendedPlayerData.enshure(player);
			ExtendedPlayerData extendedData=ExtendedPlayerData.get(player);
			ComplexPlayerRenderingData.enshure(player);
			if(extendedData==null)return;
			if(player.worldObj.isRemote)animateModel(player);
			Positions pos=getPos(player);
			boolean active=getIsActive(player)&&pos!=null;
			if(active){
				Positions[] flap=getFlap(player);
				if(extendedData.isFlappingDown){
					setFlap(player,flap);
					int x=extendedData.getKeysX();
					if(x==0)setPosId(player, 3);
					else if(x==1)setPosId(player, 2);
					else if(x==-1)setPosId(player, 4);
				}else{
					int id=getPosId(player);
					if(id==2||id==4)setPosId(player, 3);
				}
			}
			if(player.worldObj.isRemote){
				boolean prevVar=extendedData.isFlappingDown;
				Positions[] flaps=getFlap(player);
				try{extendedData.isFlappingDown=ComplexPlayerRenderingData.getFastCyborgWingsFromTheBlackFireData(player).canFlap&&flaps[0]==Positions.FlapDownPos;}
				catch(Exception e){extendedData.isFlappingDown=false;}
				if(prevVar!=extendedData.isFlappingDown)UtilM.sendMessage(new UploadPlayerDataPacket(player));
			}
		}
		public static void updateStaticNoise(long wTime){
			if(wTime%5==0)for(int a=0;a<wantedNoiseRotationAnglesBase.length;a++)for(int b=0;b<wantedNoiseRotationAnglesBase[a].length;b++)if(RandUtil.RI(4)==0)wantedNoiseRotationAnglesBase[a][b]=RandUtil.CRF(0.5);
			for(int a=0;a<noiseSpeedRotationAnglesBase.length;a++){
				float speed=0.4F;
				for(int b=0;b<noiseSpeedRotationAnglesBase[a].length;b++){
					if(noiseSpeedRotationAnglesBase[a][b]>wantedNoiseRotationAnglesBase[a][b])
						 noiseSpeedRotationAnglesBase[a][b]-=speed;
					else noiseSpeedRotationAnglesBase[a][b]+=speed;
					noiseRotationAnglesBase[a][b]+=noiseSpeedRotationAnglesBase[a][b];
					noiseRotationAnglesBase[a][b]*=0.94;
				}
			}
		}
}
