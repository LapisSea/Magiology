package com.magiology.handlers.animationhandlers.thehand.animation;

import com.magiology.client.render.itemrender.ItemRendererTheHand;
import com.magiology.forgepowered.packets.packets.toserver.HandActionPacket;
import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.handlers.animationhandlers.thehand.animation.AnimationPart.AnimationFactory;
import com.magiology.handlers.animationhandlers.thehand.animation.LinearHandAnimProgressHandler.LinearHandAnimProgressHandlerClassic;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.codeinsert.ObjectReturn;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandAnimData{
	private static float p=1F/16F;
	
	public static HandPosition
		errorPos=new HandPosition(new HandData(),"errorPos"),
		closedFist=new HandPosition(((ObjectReturn<HandData>)()->{
			HandData data=new HandData();
			data.base[0]=0;
			data.base[1]=0;
			data.base[2]=0;
			
			data.base[3]=0;
			data.base[4]=0;
			data.base[5]=0;
			
			data.thumb[0]=28;
			data.thumb[1]=10;
			data.thumb[2]=20;
			data.thumb[3]=-20;
			data.thumb[4]=-90;
			
			data.fingers[0][1]=80;
			data.fingers[0][2]=110;
			data.fingers[0][3]=75;
			
			data.fingers[1][1]=80;
			data.fingers[1][2]=100;
			data.fingers[1][3]=90;
			
			data.fingers[2][1]=85;
			data.fingers[2][2]=95;
			data.fingers[2][3]=80;
			
			data.fingers[3][1]=95;
			data.fingers[3][2]=80;
			data.fingers[3][3]=110;
			return data;
		}).process(),"closedFist"),
		weaponHolder=new HandPosition(((ObjectReturn<HandData>)()->{
			HandData data=new HandData();
			data.base[1]=-p*3;
			data.base[3]=-30;
			data.base[4]=-5;
			
			data.thumb[0]=35;
			data.thumb[1]=25;
			data.thumb[2]=10;
			data.thumb[3]=-5;
			data.thumb[4]=-25;
			
			data.fingers[0][0]=-5;
			data.fingers[0][1]=25;
			data.fingers[0][2]=35;
			data.fingers[0][3]=10;
			
			data.fingers[1][0]=0;
			data.fingers[1][1]=28;
			data.fingers[1][2]=20;
			data.fingers[1][3]=10;
			
			data.fingers[2][0]=5;
			data.fingers[2][1]=30;
			data.fingers[2][2]=15;
			data.fingers[2][3]=5;
			
			data.fingers[3][0]=10;
			data.fingers[3][1]=35;
			data.fingers[3][2]=10;
			data.fingers[3][3]=5;
			return data;
		}).process(),"weaponHolder"),
		lookAtSomething=new HandPosition(((ObjectReturn<HandData>)()->{
			HandData data=new HandData();
			data.base[0]=p*11;
			data.base[1]=-p*8;
			data.base[2]=-0.1F;
			data.base[3]=-15;
			data.base[4]=0;
			data.base[5]=170;
			
			data.thumb[0]=15;
			data.thumb[1]=40;
			data.thumb[2]=15;
			data.thumb[3]=-10;
			data.thumb[4]=-25;
			
			data.fingers[0][1]=15;
			data.fingers[0][2]=55;
			data.fingers[0][3]=5;
			
			data.fingers[1][1]=40;
			data.fingers[1][2]=25;
			data.fingers[1][3]=5;
			
			data.fingers[2][1]=35;
			data.fingers[2][2]=30;
			data.fingers[2][3]=7;
			
			data.fingers[3][1]=50;
			data.fingers[3][2]=15;
			data.fingers[3][3]=5;
			return data;
		}).process(),"lookAtSomething"),
		naturalPosition=new HandPosition(((ObjectReturn<HandData>)()->{
			HandData data=new HandData();
			data.base[1]=-p*7;
			data.base[3]=-15;
			data.base[4]=0;
			data.base[5]=5;
	
			data.thumb[0]=22;
			data.thumb[1]=32;
			data.thumb[3]=-15;
			data.thumb[4]=-30;
			
			data.fingers[0][1]=12;
			data.fingers[0][2]=40;
			data.fingers[0][3]=5;
			
			data.fingers[1][1]=17;
			data.fingers[1][2]=20;
			data.fingers[1][3]=2;
			
			data.fingers[2][1]=17;
			data.fingers[2][2]=23;
			data.fingers[2][3]=2;
			
			data.fingers[3][1]=24;
			data.fingers[3][2]=15;
			data.fingers[3][3]=2;
			return data;
		}).process(),"naturalPosition");
	
	public static LinearHandAnimation chargeUp=new LinearHandAnimation(3,weaponHolder,new LinearHandAnimProgressHandlerClassic(){
		@Override
		public int getTimeForAnimation(){
			return 20;
		}
		
		@Override
		public void onHoldingEnd(){
			timeMul=-1;
			if(progress>0.7)passOnAnimation(chargeUpRelease);
		}
		
		@Override
		public void update(){
			super.update();
//			if(timeHeld>7){
//				for(int i=0;i<2;i++){
//					Vec3M pos1=ItemRendererTheHand.getPalmRand();
//					EntitySmoothBubleFX part=new EntitySmoothBubleFX(UtilM.getTheWorld(),pos1.x,pos1.y,pos1.z,RandUtil.CRF(0.01),RandUtil.CRF(0.01),RandUtil.CRF(0.01),100,2,1,1,0.2,0,1);
//					part.hasDepth=true;
//					UtilM.spawnEntityFX(part);
//				}
//			}
		}
	}, new ObjectReturn<AnimationPart[]>(){
		@Override
		public AnimationPart[] process(){
			AnimationFactory factory=new AnimationFactory();
			
			factory.gen(11, 4,0,  2,-5);
			factory.gen(12, 2,-2, 2,0,  2,8F);
			factory.gen(13, 2,-2, 2,0,  2,7);
			factory.gen(14, 2,-2, 2,0,  2,13);
			
			factory.gen(15, 5,0,  2,-2F);
			factory.gen(16, 2,-2, 3,0,  2,8);
			factory.gen(17, 2,-2, 3,0,  2,8);
			factory.gen(18, 2,-2, 3,0,  2,9);
			
			factory.gen(19, 6,0,  2,-2F);
			factory.gen(20, 2,-2, 4,0,  2,9);
			factory.gen(21, 2,-2, 4,0,  2,8);
			factory.gen(22, 2,-2, 4,0,  2,8);
			
			factory.gen(13, 7,0,  2,0F);
			factory.gen(24, 2,-2, 5,0,  2,13);
			factory.gen(25, 2,-2, 5,0,  2,7);
			factory.gen(26, 2,-2, 5,0,  2,5);
			
			factory.gen(6, 4,0,  3, 3F);
			factory.gen(7, 4,0,  3, 2F);
			
			factory.gen(9, 4,0,  3,-2F);
			factory.gen(10, 4,0,  3,-9F);
			
			factory.gen(2, 2,0,  1,p, 1,p/2, 1,-p/2, 2,-p);
			factory.gen(5,       2,23, 1,19, 2,15);
			
			return factory.compile();
		}
	}.process(),"chargeUp");
	public static HandPosition chargeUpEnd=new HandPosition(chargeUp.data.get(1)[0],"chargeUpEnd");
	
	
	public static LinearHandAnimation chargeUpRelease=new LinearHandAnimation(3,chargeUpEnd,new LinearHandAnimProgressHandler(){
		protected AnimationEvent shootEvent=new AnimationEvent(()->{
			Vec3M pos=ItemRendererTheHand.getPalmMiddle();
			TheHandHandler.shoot(UtilC.getThePlayer(),pos);
			UtilM.sendMessage(new HandActionPacket(0,pos));
		},()->timeHeld==10);
		
		protected int timeMul=1;
		
		@Override
		public float getProgress(){
//			float m=0.4F;
			return progress;//UtilM.fluctuate(20, 0)*m+1-m-0.00001F;//
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
			factory.gen(11,  2,6);
			factory.gen(12,  2,22F);
			factory.gen(13,  2,9);
			factory.gen(14,  2,10);
			
			factory.gen(15,  2,5F);
			factory.gen(16,  2,15);
			factory.gen(17,  2,12);
			factory.gen(18,  2,16);
			
			factory.gen(19,  3,-1F);
			factory.gen(20,  3,10);
			factory.gen(21,  3,8);
			factory.gen(22,  3,13);
			
			factory.gen(23,  3,-4F);
			factory.gen(24,  3,8);
			factory.gen(25,  3,7);
			factory.gen(26,  3,18);
			
			factory.gen(6,  3,2.5F);
			factory.gen(7,  3,-6F);
			
			factory.gen(9,  3,-4F);
			factory.gen(10,  3,-7F);
			
			//turn hand around
			factory.gen(5,  3,0,  1,-14,  3,-25,  1,-12,  1,-7);
			factory.gen(3,  3,0,  6,-5);
			
			factory.gen(2,  3,0,  8,-p*0.6F);
			
			//blast off
			factory.gen(11,  14,0,  1,-4);
			factory.gen(12,  14,0,  1,-39F);
			factory.gen(13,  14,0,  1,-45);
			factory.gen(14,  14,0,  1,-35);
			
			factory.gen(15,  14,0,  1,-5F);
			factory.gen(16,  14,0,  1,-47);
			factory.gen(17,  14,0,  1,-32);
			factory.gen(18,  14,0,  1,-40);
			
			factory.gen(19,  14,0,  1,-1F);
			factory.gen(20,  14,0,  1,-46);
			factory.gen(21,  14,0,  1,-30);
			factory.gen(22,  14,0,  1,-40);
//			
			factory.gen(23,  14,0,  1,3F);
			factory.gen(24,  14,0,  1,-44);
			factory.gen(25,  14,0,  1,-30);
			factory.gen(26,  14,0,  1,-50);
			
			factory.gen(6,   14,0,  1,2F);
			factory.gen(7,   14,0,  1,20F);
			factory.gen(8,   14,0,  1,5F);
//			
			factory.gen(9,   14,0,  1,4F);
			factory.gen(10,  14,0,  1,28F);
			
			factory.gen(2,  12,0,  5,p*1.5F);
			
			//blast off recoil
			factory.gen(2,  25,0,  1,-p*2);
			factory.gen(2,  32,0,  4,p/2);
			
			int slowDown=2;
			
			//go back to original pos
			factory.gen(2,   36,0,  8*slowDown,-0.0128F/slowDown);
			factory.gen(3,   36,0,  8*slowDown,4.1172F/slowDown);
			factory.gen(5,   36,0,  8*slowDown,2.4444F/slowDown);
			
			factory.gen(6,   36,0,  8*slowDown,1.5555F/slowDown);
			factory.gen(7,   36,0,  8*slowDown,-4.4444F/slowDown);
			factory.gen(8,   36,0,  8*slowDown,-1.111F/slowDown);
			factory.gen(9,   36,0,  8*slowDown,1.3333F/slowDown);
			
			factory.gen(11,  36,0,  8*slowDown,0.2222F/slowDown);
			factory.gen(12,  36,0,  8*slowDown,-0.4444F/slowDown);
			factory.gen(13,  36,0,  8*slowDown,4.7777F/slowDown);
			factory.gen(14,  36,0,  8*slowDown,0.4444F/slowDown);
			factory.gen(15,  36,0,  8*slowDown,0.7777F/slowDown);
			factory.gen(16,  36,0,  8*slowDown,2.3333F/slowDown);
			factory.gen(17,  36,0,  8*slowDown,1.7777F/slowDown);
			factory.gen(18,  36,0,  8*slowDown,1.3333F/slowDown);
			factory.gen(19,  36,0,  8*slowDown,1.1111F/slowDown);
			factory.gen(20,  36,0,  8*slowDown,2.6666F/slowDown);
			factory.gen(21,  36,0,  8*slowDown,0.8888F/slowDown);
			factory.gen(22,  36,0,  8*slowDown,0.8888F/slowDown);
			factory.gen(23,  36,0,  8*slowDown,1.1111F/slowDown);
			factory.gen(24,  36,0,  8*slowDown,1.3333F/slowDown);
			factory.gen(25,  36,0,  8*slowDown,1.3333F/slowDown);
			factory.gen(26,  36,0,  8*slowDown,1.7777F/slowDown);
			
			return factory.compile();
		}
	}.process(),"chargeUpRelease");
	
	
	public static HandAnimation rightClickAnimation=new HandAnimation(naturalPosition, 
			new ObjectReturn<AnimationPart[]>(){
				@Override
				public AnimationPart[] process(){
					AnimationFactory factory=new AnimationFactory();
					factory.gen(12,       3,-2,  2,-1,  1,-0.5F,  2,1,  3,2);
					factory.gen(13, 2,0,  3,-3,  2,-2,  1,-1,  2,2,  3,3);
					factory.gen(14, 5,0,  4,-0.5F,  4,0.5F);
					
					factory.gen(16,       3,-2,  2,-1,  1,-0.5F,  2,1,  3,2);
					factory.gen(17, 2,0,  3,-1,  2,-2,  1,-1,  2,2,  3,1);
					factory.gen(18, 5,0,  4,-0.5F,  4,0.5F);
					
					factory.gen(20,       3,-2,  2,-1,  1,-0.5F,  2,1,  3,2);
					factory.gen(21, 2,0,  3,-1,  2,-2,  1,-1,  2,2,  3,1);
					factory.gen(22, 5,0,  4,-0.5F,  4,0.5F);
					
					factory.gen(24,       3,-2,  2,-1,  1,-0.5F,  2,1,  3,2);
					factory.gen(25, 2,0,  3,-1,  2,-2,  1,-1,  2,2,  3,1);
					factory.gen(26, 5,0,  4,-0.5F,  4,0.5F);
					
					factory.gen(7,        3,2,  2,1,  2,0.5F,  2,-1,  3,-2);
					factory.gen(10,       3,4,  2,2,  2,1F,  2,-2,  3,-4);
					
					factory.gen(2,        3,p*2,  2,p*1F,  2,-p*1F,  3,-p*2F);
					factory.gen(3,        4,-1,  4,1F);
					return factory.compile();
				}
			}.process(),"rightClickAnimation");
	
	static{
		HandPosition.registerPosition(errorPos);
		HandPosition.registerPosition(closedFist);
		HandPosition.registerPosition(weaponHolder);
		HandPosition.registerPosition(lookAtSomething);
		HandPosition.registerPosition(naturalPosition);
		HandPosition.registerPosition(chargeUpEnd);
	}
	
	public static void load(){}


	
}
