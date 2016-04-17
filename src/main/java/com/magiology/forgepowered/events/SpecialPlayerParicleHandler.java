package com.magiology.forgepowered.events;

import com.magiology.core.init.MBlocks;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.effect.EntityFollowingBubleFX;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.SlowdownUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpecialPlayerParicleHandler{

	boolean isActive=false;
	SlowdownUtil slowdown=new SlowdownUtil(10);
	SlowdownUtil slowdown2=new SlowdownUtil(20);
	
	public void onHitTheGround(World world,double x,double y,double z,EntityPlayer player, float distance){
		if(isActive){
			
			if(distance>3)for(int a=0;a<4*(int)distance;a++){
				double xr=x,yr=y,zr=z,scale=0.2;
				xr=x+RandUtil.CRF(0.8);
				yr=y-1.5;
				zr=z+RandUtil.CRF(0.8);
				scale=world.rand.nextDouble()*0.4;
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(world,xr, yr, zr,  RandUtil.CRF(0.1+scale), RandUtil.CRF(0.1+scale), RandUtil.CRF(0.1+scale), 300+world.rand.nextInt(300), 3+world.rand.nextInt(2), -20,1, world.rand.nextFloat(),world.rand.nextFloat(),world.rand.nextFloat(), 1, 0.95));
				
			}
			
		}
	}
	
	public void onJump(World world,EntityPlayer player,double x,double y,double z){
		if(isActive){
			int ammount=1;double R=0,G=0,B=0;
			ItemStack itemS=player.getHeldItemMainhand();
			
			if(itemS!=null&&Block.getBlockFromItem(itemS.getItem())==Blocks.lapis_block)ammount=5;
			if(itemS!=null&&itemS.getItem()==Items.dye&&itemS.getItemDamage()==4)ammount=2;
			double xr=x,yr=y,zr=z,scale=0.2;
			for(int i=0;i<20*ammount;i++){
				xr=x+RandUtil.CRF(0.8);
				yr=y-1.5;
				zr=z+RandUtil.CRF(0.8);
				scale=world.rand.nextDouble()*0.4;
				R=world.rand.nextFloat()/3;G=world.rand.nextFloat()/3;B=1;
				if(itemS!=null&&(itemS.getItem()==MItems.fireHammer||Block.getBlockFromItem(itemS.getItem())==MBlocks.firePipe)){
					R=1;G=world.rand.nextFloat()/3;B=world.rand.nextFloat()/3;
				}
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(world,xr, yr, zr,  RandUtil.CRF(0.1+scale), RandUtil.CRF(0.1+scale)+0.5, RandUtil.CRF(0.1+scale), (int)(300+world.rand.nextInt(300)*(player.isSneaking()?0.1:1)), 3+world.rand.nextInt(2), -20,true,player.isSneaking()?2:1,"tx1", R,G,B, 1, 0.95));
			}
			if(ammount!=1)for(int a=0;a<ammount*2;a++){
				xr=x+RandUtil.CRF(0.8);
				yr=y-1.5;
				zr=z+RandUtil.CRF(0.8);
				R=world.rand.nextFloat()/3;G=world.rand.nextFloat()/3;B=1;
				if(itemS!=null&&(itemS.getItem()==MItems.fireHammer||Block.getBlockFromItem(itemS.getItem())==MBlocks.firePipe)){
					R=1;G=world.rand.nextFloat()/3;B=world.rand.nextFloat()/3;
				}
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(world,xr, yr, zr,  RandUtil.CRF(0.1+scale), RandUtil.CRF(0.4+scale)+1, RandUtil.CRF(0.1+scale), 300+world.rand.nextInt(300), 3+world.rand.nextInt(2), -20,1, R,G,B, 1, 1));
			}
		}
	}
	
	public void onUpdate(World world,EntityPlayer player,double x,double y,double z,double xv,double yv,double zv){
		if(slowdown.isTimeWithAddProgress()){
			InventoryPlayer inv=player.inventory;
			isActive=false;
			if(inv.getStackInSlot(7)!=null&&inv.getStackInSlot(8)!=null)if(Block.getBlockFromItem(inv.getStackInSlot(7).getItem())==MBlocks.firePipe&&Block.getBlockFromItem(inv.getStackInSlot(8).getItem())==MBlocks.fireLamp)isActive=true;
		}
		
		int ammount=1,size=300+world.rand.nextInt(100);double R=0,G=0,B=0;
		size/=(player.isSprinting()&&isActive?1:10);
		
		if(isActive){
			if(player.capabilities.isCreativeMode){
				int angle=world.rand.nextInt(360);
				if(world.getTotalWorldTime()%2==0){
					double[] ab=MathUtil.circleXZ(angle);ab[0]/=3;ab[1]/=3;
					UtilM.spawnEntityFX(new EntityFollowingBubleFX(world,x+ab[0], y+0.3, z+ab[1], xv*1.2, yv*1.2, zv*1.2,player,angle, ab[0], 0.3, ab[1], 250, 12*(player.isSneaking()?2:1), world.rand.nextFloat(),world.rand.nextFloat(),world.rand.nextFloat(),0.5));
				}
			}
			
			if((player.isOnLadder()&&Math.round((player.motionY+0.06)*10)!=0)){
				ItemStack itemS=player.getHeldItemMainhand();
				if(itemS!=null&&Block.getBlockFromItem(itemS.getItem())==Blocks.lapis_block)ammount=5;
				if(itemS!=null&&itemS.getItem()==Items.dye&&itemS.getItemDamage()==4)ammount=2;
				for (int i=0;i<ammount;i++){
					double xr=x+RandUtil.CRF(0.8);
					double yr=y+RandUtil.CRF(1.8)-0.6;
					double zr=z+RandUtil.CRF(0.8);
					R=world.rand.nextFloat()/3;G=world.rand.nextFloat()/3;B=1;
					if(itemS!=null&&(itemS.getItem()==MItems.fireHammer||Block.getBlockFromItem(itemS.getItem())==MBlocks.firePipe)){
						R=1;G=world.rand.nextFloat()/3;B=world.rand.nextFloat()/3;
					}
					UtilM.spawnEntityFX(new EntitySmoothBubleFX(world,xr,yr,zr, xv*1.1,yv*1.1,zv*1.1, size, 2+world.rand.nextInt(2), 0,1, R,G,B, 1, 0.98));
				}
			}
			
			if(slowdown2.isTimeWithAddProgress())for(int a=0;a<ammount;a++){
				int xP,yP,zP,count=0;
				Block block;
				do{
					count++;
					xP=(int)(25-world.rand.nextInt(50)+x);
					yP=(int)(25-world.rand.nextInt(50)+y);
					zP=(int)(25-world.rand.nextInt(50)+z);
					block=UtilM.getBlock(world, xP, yP, zP);
				}while(block!=Blocks.air&&count<100);
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(world,xP+0.5, yP+1.5, zP+0.5,0.04*(world.rand.nextBoolean()?1:-1), 0.04*(world.rand.nextBoolean()?1:-1), 0.04*(world.rand.nextBoolean()?1:-1),600,0,0,true,2,"tx1",world.rand.nextFloat(),world.rand.nextFloat(),world.rand.nextFloat(),1, 0.99));
				
			}
		}
		if((isActive?true:world.rand.nextInt(3)==0)){
			ItemStack itemS=player.getHeldItemMainhand();
			if(itemS!=null&&Block.getBlockFromItem(itemS.getItem())==Blocks.lapis_block)ammount=5;
			if(itemS!=null&&itemS.getItem()==Items.dye&&itemS.getItemDamage()==4)ammount=2;
//				UtilM.println("\n"+BB);
			for (int i=0;i<ammount;i++){
				if(!isActive)i=ammount;
				double xr=x+RandUtil.CRF(0.8);
				double yr=y+RandUtil.CRF(1.8)-0.6;
				double zr=z+RandUtil.CRF(0.8);
				R=world.rand.nextFloat()/3;G=world.rand.nextFloat()/3;B=1;
				if(itemS!=null&&(itemS.getItem()==MItems.fireHammer||Block.getBlockFromItem(itemS.getItem())==MBlocks.firePipe)){
					R=1;G=world.rand.nextFloat()/3;B=world.rand.nextFloat()/3;
				}
				if(player.isSneaking()){
					R=world.rand.nextFloat();G=world.rand.nextFloat();B=world.rand.nextFloat();
				}
				if((player.isSneaking()?world.rand.nextBoolean():true))UtilM.spawnEntityFX(new EntitySmoothBubleFX(world,xr,yr,zr, xv*0.5+RandUtil.CRF(0.05),yv*0.5+RandUtil.CRF(0.05),zv*0.5+RandUtil.CRF(0.05), size*(player.isSneaking()?2:1), 2+world.rand.nextInt(2), player.isSneaking()?1:6,true,player.isSneaking()?2:1,"tx1", R,G,B, 1, 0.98));
			}
			
		}
	}
	
}
