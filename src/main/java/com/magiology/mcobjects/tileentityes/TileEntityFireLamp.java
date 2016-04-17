package com.magiology.mcobjects.tileentityes;

import static com.magiology.api.power.SixSidedBoolean.Modifier.*;
import static com.magiology.util.utilclasses.SideUtil.*;

import com.magiology.api.power.SixSidedBoolean;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.effect.EntitySparkFX;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPowGen;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.effect.EntitySmokeFXM;
import com.magiology.util.utilobjects.vectors.Pos;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFireLamp extends TileEntityPowGen{
	public BlockPos control=new Pos(),c=new Pos();
	public TileEntityControlBlock controlBlock=null;
	
	EffectRenderer efr=Get.Render.ER();
	public boolean isInMultiblockStructureBFCHelper=false,IsControlledByOSC=false,active,first=true,onOf=false;
	
	
	SlowdownUtil optimizer=new SlowdownUtil(20),optimizer2=new SlowdownUtil(4), optimizer3=new SlowdownUtil(3);
	
	
	TileEntity tileE;
	public int XZr=15,Yr=4,mode=0;
	
	public TileEntityFireLamp(){
		super(SixSidedBoolean.create(First6True,Include,DOWN(),Last6True,Include,UP()).sides,SixSidedBoolean.lastGen.sides.clone(), 1, 2, 5, 115, 100);
	}
	
	@Override
	public boolean canGeneratePowerAddon(){
		return onOf&&fuel>0;
	}
	@Override
	public void generateFunction(){
		currentEnergy+=3;
		fuel--;
	}
	
	public void getExistingControlBlockinR(int xz2,int y2){
		BlockPos pos1=new BlockPos(0, 0, 0);
		
		for(int x=-xz2;x<xz2+1;x++){
			for(int y=-y2;y<y2+1;y++){
				for(int z=-xz2;z<xz2+1;z++){
					if(worldObj.getTileEntity(pos.add(x,y,z))instanceof TileEntityControlBlock){
						pos1=new BlockPos(x, y, z);
						x=xz2;y=y2;z=xz2;
					}
				}
			}
		}
		
		control=pos.add(pos1);
	}
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		AxisAlignedBB dc=super.getRenderBoundingBox();
		dc=new AxisAlignedBB(
			dc.maxX+(control.getX()-pos.getX()>0?control.getX()-pos.getX():0),
			dc.maxY+(control.getY()-pos.getY()>0?control.getY()-pos.getY():0),
			dc.maxZ+(control.getZ()-pos.getZ()>0?control.getZ()-pos.getZ():0),
			dc.minX+(control.getX()-pos.getX()<0?control.getX()-pos.getX():0),
			dc.minY+(control.getY()-pos.getY()<0?control.getY()-pos.getY():0),
			dc.minZ+(control.getZ()-pos.getZ()<0?control.getZ()-pos.getZ():0));
		return dc;
	}
	public void isInMultiblockStructureBFC(){
		for(int x1=-1;x1<=1;x1++){for(int y1=-2;y1<=-1;y1++){for(int z1=-1;z1<=1;z1++){
			
			tileE=worldObj.getTileEntity(pos.add(x1,y1,z1));
			if(tileE instanceof TileEntityBigFurnaceCore){
				TileEntityBigFurnaceCore BFC=(TileEntityBigFurnaceCore)tileE;
				c=pos.add(x1,y1,z1);
				
				control=c.add(0, 0, 0);
				
				if(BFC.isMultiblockHelper)c=new BlockPos(c.getX(),pos.getY()+y1,c.getZ());
				else c=new BlockPos(c.getX(),10000,c.getZ());
				x1=2;
				y1=2;
				z1=2;
			}
			else{
				c=new BlockPos(c.getX(),10000,c.getZ());
		}}}
			
		}
		if(c.getY()==10000)isInMultiblockStructureBFCHelper=false;
		else isInMultiblockStructureBFCHelper=true;
	}
	
	@Override
	public boolean isPowerKeptOnWrench(){
		return true;
	}
	
	
	public void power(){
		
		int i=getEnergy();
		handleStandardPowerTransmission(true);
		if(i!=getEnergy())if(worldObj.isRemote&&optimizer3.progress==0){
			EntitySmoothBubleFX particle=new EntitySmoothBubleFX(worldObj, pos.getX()+0.5, pos.getY()+p*15, pos.getZ()+0.5, 0, 0.02, 0, 180, 2.5, 0.5, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 1);
			if(RandUtil.RI(3)==0)UtilM.spawnEntityFX(particle);
			else UtilM.spawnEntityFX(particle,20);
		}
		
		if(isInMultiblockStructureBFCHelper==true){
			currentEnergy=0;
			controlBlock=null;
		}
		else if(canGeneratePower(3))generateFunction();
		
		if(controlBlock!=null){
			for(int a=0;a<2;a++)if(controlBlock.tank>0&&fuel<maxFuel){
				fuel+=1;
				controlBlock.tank-=1;
			}
		}
//		tryToSendEnergyToObj();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound NBTTC){
		super.readFromNBT(NBTTC);
		fuel=NBTTC.getInteger("FT");
		maxFuel=NBTTC.getInteger("MFT");
		IsControlledByOSC=NBTTC.getBoolean("ICBOSC");
		control=readPos(NBTTC, "C");
	}
	
	@SideOnly(Side.CLIENT)
	public void spawnPaticle(boolean isforced){
		if(!worldObj.isRemote)return;
		if(optimizer3.isTimeWithAddProgress())return;
		
		if(canGeneratePower(3)||isforced){
			if(optimizer2.isTimeWithAddProgress()){
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+p*4, pos.getY()+p*11, pos.getZ()+p*4, -0.02, -0.005, -0.02));
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+p*12, pos.getY()+p*11, pos.getZ()+p*4, 0.02, -0.005, -0.02));
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+p*12, pos.getY()+p*11, pos.getZ()+p*12, 0.02, -0.005, 0.02));
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+p*4, pos.getY()+p*11, pos.getZ()+p*12, -0.02, -0.005, 0.02));
				UtilM.spawnEntityFX(new EntitySparkFX(worldObj, pos.getX()+0.5, pos.getY()+p*5.5, pos.getZ()+0.5, 0.5F/16F, 0.1F,1,5,100,new Vec3M(0,0,0)));
			}else{
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+p*4, pos.getY()+p*11, pos.getZ()+p*4, -0.02, -0.005, -0.02),16);
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+p*12, pos.getY()+p*11, pos.getZ()+p*4, 0.02, -0.005, -0.02),16);
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+p*12, pos.getY()+p*11, pos.getZ()+p*12, 0.02, -0.005, 0.02),16);
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+p*4, pos.getY()+p*11, pos.getZ()+p*12, -0.02, -0.005, 0.02),16);
			}
			if(RandUtil.RB())UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj, pos.getX()+p*6+(RandUtil.RF()*p*4), pos.getY()+p*11-RandUtil.RF()*p*5, pos.getZ()+p*6+(RandUtil.RF()*p*4), RandUtil.CRF(0.005), -0.01, RandUtil.CRF(0.005), 300, 2, -1, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 0.7),20);
			else UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj, pos.getX()+0.35+(RandUtil.RF()*0.35), pos.getY()+RandUtil.RF()*p*5, pos.getZ()+0.35+(RandUtil.RF()*0.35), RandUtil.CRF(0.005), 0.01, RandUtil.CRF(0.005), 300, 2, 1, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 0.7),20);
		}
		
		
	}
	
	public void tryToSendEnergyToObj(){
		TileEntity aa=worldObj.getTileEntity(pos.add(0,1,0));
		if(aa instanceof TileEntityFirePipe){
			TileEntityFirePipe pipe= (TileEntityFirePipe)aa;
			if(PowerUtil.tryToDrainFromTo(this, pipe, PowerUtil.getHowMuchToSendFromToForDrain(this, pipe),EnumFacing.UP.getIndex()))if(worldObj.isRemote&&optimizer3.progress==0){
				EntitySmoothBubleFX particle=new EntitySmoothBubleFX(worldObj, pos.getX()+0.5, pos.getY()+p*15, pos.getZ()+0.5, 0, 0.02, 0, 180, 2.5, 0.5, 1, 0.2+RandUtil.RF()*0.5, 0.2+RandUtil.RF()*0.2, 1);
				if(RandUtil.RI(3)==0)UtilM.spawnEntityFX(particle);
				else UtilM.spawnEntityFX(particle,20);
			}
		}
	}

	@Override
	public void update(){
		if(control.equals(pos))active=false;
		else active=true;
		
		
		TileEntity TileEn=worldObj.getTileEntity(control);
		if(TileEn instanceof TileEntityControlBlock){
			controlBlock=(TileEntityControlBlock)TileEn;
		}else controlBlock=null;
		
		if(controlBlock!=null)onOf=controlBlock.onOf;
		else onOf=false;
		
		
		if(active&&onOf){
			power();
			if(isInMultiblockStructureBFCHelper!=true)spawnPaticle(false);
		}
		
		if(optimizer.isTimeWithAddProgress()){
			isInMultiblockStructureBFC();
			updateConnections();
			if(isInMultiblockStructureBFCHelper!=true)updateControlBlock();
			
			
			if(first==true){
				first=false;
				getExistingControlBlockinR(XZr,Yr);
			}else{
				if(control.getX()>pos.getX()&&control.getX()-XZr>pos.getX()){control=pos.add(0, 0, 0);}
				if(control.getZ()>pos.getZ()&&control.getZ()-XZr>pos.getZ()){control=pos.add(0, 0, 0);}
				if(control.getY()>pos.getY()&&control.getY()-Yr>pos.getY()) {control=pos.add(0, 0, 0);}
				
				if(control.getX()<pos.getX()&&control.getX()+XZr<pos.getX()){control=pos.add(0, 0, 0);}
				if(control.getZ()<pos.getZ()&&control.getZ()+XZr<pos.getZ()){control=pos.add(0, 0, 0);}
				if(control.getY()<pos.getY()&&control.getY()+Yr<pos.getY()) {control=pos.add(0, 0, 0);}
			}
		}
		
		
		PowerUtil.sortSides(this);
	}

	@Override
	public void updateConnections(){
		UpdateablePipeHandler.onConnectionUpdate(this);
		TileEntity tile=worldObj.getTileEntity(pos.add(0, -1, 0));
		if(tile instanceof TileEntityFirePipe)connections[1].setMain(((TileEntityFirePipe)tile).connections[0].getMain());
		else connections[1].setMain(false);
		connections[1].setForce(true);
	}
	public void updateControlBlock(){
		TileEntity tileTest=worldObj.getTileEntity(control);
		//if(IsControlledByOSC&&tileTest instanceof TileEntityOreStructureCore)return;
		IsControlledByOSC=false;
		
		if(tileTest instanceof TileEntityControlBlock)return;
		else control=pos.add(0,0,0);
	}
	@Override
	public void writeToNBT(NBTTagCompound NBTTC){
		super.writeToNBT(NBTTC);
		NBTTC.setInteger("FT", fuel);
		NBTTC.setInteger("MFT", maxFuel);
		NBTTC.setBoolean("ICBOSC", IsControlledByOSC);
		writePos(NBTTC, control, "C");
	}
}