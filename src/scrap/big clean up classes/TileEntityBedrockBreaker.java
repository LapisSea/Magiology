package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.effect.EntityFacedFX;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBedrockBreaker extends TileEntityM implements ITickable{
	EffectRenderer efrenderer = Get.Render.ER();
	public double animation=40,speed=0,positionForLaser=0;
	public float p=1F/16F;
	//RMB=is there a valid block to process?
	public boolean RMB,IRFA=false;
	public int IDROW=0;
	SlowdownUtil optimizer=new SlowdownUtil(10);
	int state=0,progres=0;

	@Override
	public void update(){
		
		
		if(IRFA==true)action();
		else{
			progres=progres-progres;
		}
		if(optimizer.isTimeWithAddProgress()){
		//optimizer start
		this.Update();
		if(IRFA==true)
		{
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+0.5,pos.getY(),pos.getZ()+0.5, 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+p*0.5, pos.getZ()+0.5, (worldObj.rand.nextFloat()/50)-0.01, 0.005, (worldObj.rand.nextFloat()/59)-0.01);
			if(efrenderer!=null)efrenderer.addBlockHitEffects(pos.add(0, -1, 0), EnumFacing.UP);
		}
		
		}//optimizer end+
		this.animation();
		if(IRFA==true)worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+UtilM.RF(), pos.getY()+UtilM.RF(), pos.getZ()+UtilM.RF(), 0, 0, 0);
		
	}
	
	public void animation(){
		
		if(RMB){
			speed-=0.12;
		}else{
			speed+=0.1;
		}
		animation+=speed;
		if(animation>34){
			animation=34;
			if(speed>3)speed*=-0.1;
			else speed=0;
		}
		else if(animation<-53){
			state=1;
			animation=-53;
			if(speed<-3)speed*=-0.2;
			else speed=0;
		}else state=0;
		
		
		//positionForLaser
		{
			if(state==1)
			{
				if(positionForLaser>0)positionForLaser-=0.01;
				else{
					IRFA=true;
					positionForLaser=0;
				}
			}
			else
			{
				if(positionForLaser<p*5){positionForLaser=positionForLaser+p/10;IRFA=false;}
				else IRFA=false;
			}
		}
	}
	
	public void Update(){
		if(U.getBlock(worldObj, pos.add(0,-1,0))==Blocks.bedrock||U.getBlock(worldObj, pos.add(0,-1,0))==Blocks.obsidian)RMB=true;
		else RMB=false;
	}
	
	public void action(){
		double[] a=UtilM.circleXZ(UtilM.RInt(360));
		a[0]*=UtilM.RF()/3;
		a[1]*=UtilM.RF()/3;
		UtilM.spawnEntityFX(new EntityFacedFX(worldObj, pos.getX()+0.5+a[0], pos.getY()+0.101, pos.getZ()+0.5+a[1], 0, 0.001, 0, 300, 10, 0, 1, 1, 0.2+UtilM.RF()*0.2, 0.2+UtilM.RF()*0.2, 0.3));
		progres++;
			if(U.getBlock(worldObj, pos.add(0,-1,0))==Blocks.bedrock){
				if(progres>=2000)
				{
					progres=progres-progres;
//					if(efrenderer!=null)efrenderer.addBlockDestroyEffects(pos.add(0,-1,0), H.getBlock(worldObj, pos.add(0,-1,0)), 0);
					worldObj.setBlockToAir(pos.add(0,-1,0));
				}
			}
			else if(U.getBlock(worldObj, pos.add(0,-1,0))==Blocks.obsidian){
				if(progres>=100)
				{
					progres=0;
//					if(efrenderer!=null)efrenderer.addBlockDestroyEffects(xCoord, yCoord-1, zCoord, worldObj.getBlock(xCoord, yCoord-1, zCoord), 0);
					worldObj.setBlockToAir(pos.add(0,-1,0));
				}
			}
		}
	

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 4096.0D;
	}
	
	@Override
	@SideOnly(Side.CLIENT)public AxisAlignedBB getRenderBoundingBox(){return new AxisAlignedBB(pos.add(-0.1,0,-0.1), pos.add(1.1,1,1.1));}
	
	
}
