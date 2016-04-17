package com.magiology.mcobjects.tileentityes;

import com.magiology.core.init.MBlocks;
import com.magiology.mcobjects.effect.EntityCustomfireFX;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.effect.EntitySparkFX;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.structures.Structure;
import com.magiology.structures.Structures;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.effect.EntityFlameFXM;
import com.magiology.util.utilobjects.m_extension.effect.EntitySmokeFXM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;

public class TileEntityOreStructureCore extends TileEntityPow{

	Structure mainMultiBlock,diamondAddon,bedrockAddon,beaconAddon,pipeStructure[]=new Structure[3];
	SlowdownUtil optimizer=new SlowdownUtil(30),optimizer2=new SlowdownUtil(20),optimizer3=new SlowdownUtil(10);
	public int go=0,set=0,yes=10,no=6,processing=0,level=-1;
	public boolean firstTime,validOreInPlace,updateStructureHelper;
	public boolean[] structureUpg=new boolean[4];
	BlockPos[] poses=UtilM.BlockPosArray(new int[]{3,-3, 3,-3,-3,-2,-3,-2,3,2,3,2},new int[]{3, 3, 3, 3, 4, 4, 4, 4,4,4,4,4},new int[]{3, 3,-3,-3,-2,-3,2,3,-2,-3,2,3});
	Block[] block={
			Blocks.iron_ore,Blocks.tnt, MBlocks.IronLevel2, MBlocks.IronLevel3, MBlocks.IronLevel4, MBlocks.IronLevel5, MBlocks.SuperDuperIron,
			Blocks.gold_ore, MBlocks.GoldLevel2, MBlocks.GoldLevel3, MBlocks.GoldLevel4, MBlocks.GoldLevel5, MBlocks.SuperDuperGold,
			Blocks.coal_ore, MBlocks.CoalLevel2, MBlocks.CoalLevel3, MBlocks.CoalLevel4, MBlocks.CoalLevel5, MBlocks.SuperDuperCoal,
			Blocks.diamond_ore, MBlocks.DiamondLevel2, MBlocks.DiamondLevel3, MBlocks.DiamondLevel4, MBlocks.DiamondLevel5, MBlocks.SuperDuperDiamond,
			Blocks.emerald_ore, MBlocks.EmeraldLevel2, MBlocks.EmeraldLevel3, MBlocks.EmeraldLevel4, MBlocks.EmeraldLevel5, MBlocks.SuperDuperEmerald,
			Blocks.quartz_ore, MBlocks.NetherQuartzLevel2, MBlocks.NetherQuartzLevel3, MBlocks.NetherQuartzLevel4, MBlocks.NetherQuartzLevel5, MBlocks.SuperDuperNetherQuartz,
			Blocks.lapis_ore, MBlocks.LapisLazuliLevel2, MBlocks.LapisLazuliLevel3, MBlocks.LapisLazuliLevel4, MBlocks.LapisLazuliLevel5, MBlocks.SuperDuperLapisLazuli
			};
	
	public TileEntityOreStructureCore(){
		super(null, null, 1, 5, 50, 500000);
		mainMultiBlock=Structures.generateNewStructure(2);
		diamondAddon=Structures.generateNewStructure(3);
		bedrockAddon=Structures.generateNewStructure(4);
		beaconAddon=Structures.generateNewStructure(5);
		setReceaveOnSide(SideUtil.UP(), true);
		setReceaveOnSide(SideUtil.DOWN(), true);
		initUpgrades(Container.Machine);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		updateStructureHelper=nbt.getBoolean("USH");
		for(int a=0;a<structureUpg.length;a++)structureUpg[a]=nbt.getBoolean("HP"+a);
		level=nbt.getInteger("LVL");
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setBoolean("USH", updateStructureHelper);
		for(int a=0;a<structureUpg.length;a++)nbt.setBoolean("HP"+a, structureUpg[a]);
		nbt.setInteger("LVL", level);
	}
	
	@Override
	public void update(){
			if(mainMultiBlock==null){
				mainMultiBlock=Structures.generateNewStructure(2);
				diamondAddon=Structures.generateNewStructure(3);
				bedrockAddon=Structures.generateNewStructure(4);
				beaconAddon=Structures.generateNewStructure(5);
				pipeStructure=new Structure[3];
				pipeStructure[0]=Structures.generateNewStructure(6);
				pipeStructure[1]=Structures.generateNewStructure(7);
				pipeStructure[2]=Structures.generateNewStructure(8);
				go=5;
				updateStructure();
			}
			
			if(worldObj.getTotalWorldTime()%20==0){
				pipeStructure=new Structure[3];
				pipeStructure[0]=Structures.generateNewStructure(6);
				pipeStructure[1]=Structures.generateNewStructure(7);
				pipeStructure[2]=Structures.generateNewStructure(8);
			}
			
			mainMultiBlock.checkForNextBlock(worldObj, pos);
			if((worldObj.getTotalWorldTime()  )%2==0)beaconAddon.checkForNextBlock(worldObj, pos);
			if((worldObj.getTotalWorldTime()+1)%2==0){
				diamondAddon.checkForNextBlock(worldObj, pos);
				bedrockAddon.checkForNextBlock(worldObj, pos);
			}
			if(worldObj.getTotalWorldTime()%3==0){
				structureUpg[1]=beaconAddon.isStructureCompleate();
				structureUpg[2]=diamondAddon.isStructureCompleate();
				structureUpg[3]=bedrockAddon.isStructureCompleate();
				Structures.updateArray(pipeStructure, worldObj, pos);
				level=0;
				for(boolean bol:structureUpg)if(bol)level++;
			}
			processing();
			if(worldObj.isRemote){
				spawnProcesParticelsHandler();
				spawnCustomFire();
			}
			
			//This is for slowing down updateEntity to 0.5s for most of things to reduce lag. :)
			   //___________________________________________________________________________________
			{
				if(optimizer.isTimeWithAddProgress()){
				//------------------------------
					if(updateStructureHelper==true){
						if(worldObj.isAirBlock(pos.add(3, 1, 0)))UtilM.setBlock(worldObj, pos.add(3, 1, 0), MBlocks.FireGun);
						if(worldObj.isAirBlock(pos.add(-3, 1,0)))UtilM.setBlock(worldObj, pos.add(-3,1, 0), MBlocks.FireGun);
						if(worldObj.isAirBlock(pos.add(0, 1, 3)))UtilM.setBlock(worldObj, pos.add(0, 1, 3), MBlocks.FireGun);
						if(worldObj.isAirBlock(pos.add(0, 1,-3)))UtilM.setBlock(worldObj, pos.add(0, 1,-3), MBlocks.FireGun);
						if(worldObj.isAirBlock(pos.add(0, 4, 0)))UtilM.setBlock(worldObj, pos.add(0, 4, 0), MBlocks.FireExhaust);
					}
					this.updateStructure();
				}
				//------------------------------
				//___________________________________________________________________________________
				
			}
			PowerUtil.sortSides(this);
		
	}
	
	public void spawnProcesParticelsHandler(){
		if(updateStructureHelper)this.spawnParticleDa();
		else this.spawnParticleNe();
		if(validOreInPlace==true&&updateStructureHelper==true){
			if(worldObj.isRemote){
				if(processing<16){
					processingParticels();
					for(int a=0;a<poses.length;a++){
						TileEntity tileTest=worldObj.getTileEntity(pos.add(poses[a]));
						if(tileTest instanceof TileEntityFireLamp){
							TileEntityFireLamp tile=(TileEntityFireLamp)tileTest;
						if(worldObj.getTotalWorldTime()%2==0)tile.spawnPaticle(true);
						}
					}
				}
				if(processing>16&&processing<19)processingParticelsEnding();
				if(processing==19)processingParticelsEnding2();
				
				if(processing>3) spawnBubles(1);
				if(processing>8) spawnBubles(2);
				if(processing>13)spawnBubles(3);
			}
			if(processing==1)firstTime=true;
			if(processing==16){
				if(firstTime==true&&worldObj.isRemote)processingParticelsForGunz();
				firstTime=false;
			}
		}
	}
	
	public void updateStructure(){
		go++;
		if(go>set)go=go-set;
		checkForValidOre();
		if(go==6){
			if(mainMultiBlock.isStructureCompleate()){
				set=yes;
				U.setMetadata(worldObj, pos, 1);
				updateStructureHelper=true;
				findLevelOfStructure();

				for(int a=0;a<poses.length;a++){
					TileEntity tileTest=worldObj.getTileEntity(pos.add(poses[a]));
					if(tileTest instanceof TileEntityFireLamp){
						TileEntityFireLamp tile=(TileEntityFireLamp)tileTest;
						
						tile.IsControlledByOSC=true;
						tile.control=pos.add(0,0,0);
					}
				}
				
			}
			else{
				set=no;
				UtilM.setMetadata(worldObj,pos, 2);
				worldObj.createExplosion((Entity)null, pos.getX()+0.5, pos.getY()+2, pos.getZ()+0.5, 1, false);
				updateStructureHelper=false;
			}
		}
	}
	public void findLevelOfStructure(){
		if(pipeStructure[0]==null)return;
		structureUpg[0]=false;
		
		if(pipeStructure[0].isStructureCompleate()&&UtilM.booleanToInt(pipeStructure[1].isStructureCompleate())+UtilM.booleanToInt(pipeStructure[2].isStructureCompleate())==1){
			structureUpg[0]=true;
			
		}
		
		
	}

	public void checkForValidOre(){
		if(UtilM.isNull(block,worldObj))return;
		boolean ok=false;
		for(int a=0;a<block.length;a++){
			if(block[a]==U.getBlock(worldObj,pos.add(0,1,0))){
				a=block.length;
				ok=true;
			}
		}
		if(ok)validOreInPlace=true;
		else validOreInPlace=false;
	}
	
	public void processing(){
		if(updateStructureHelper==true){
			if(optimizer2.isTimeWithAddProgress()&&validOreInPlace&&currentEnergy>100)this.processing++;
			if(validOreInPlace&&this.currentEnergy>100)currentEnergy-=6;
			if(validOreInPlace!=true)processing=0;
			if(processing>20)processing=processing-processing;
			if(processing==20){
				if(worldObj.isRemote){
//					Get.Render.ER().addBlockDestroyEffects(pos.add(0,1,0), H.getBlock(worldObj,pos.add(0,1,0)), 0);
					UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5, pos.getY()+1.5, pos.getZ()+0.5,  0, 0.06, 0,600,99,0.1,true,2,"tx3", 0, 0, 1, 1, 0.99));
					UtilM.spawnEntityFX(new EntitySparkFX(worldObj, pos.getX()+0.5, pos.getY()+100, pos.getZ()+0.5, 0.5F, 4F, 1, 4, 130, new Vec3M(0, -2F, 0)));
				}
				else U.setBlock(worldObj,pos.add(0,1,0), Blocks.air);
				processing=0;
			}
		}
		else processing=0;
	}
	
	public void processingParticels(){
		if(worldObj.getTotalWorldTime()%4!=0){
			worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.RF(p*7)+p*5, pos.getY()+5-p*12, pos.getZ()+UtilM.RF(p*7)+p*5,  0, -0.05-0.1*UtilM.RF(2), 0);
			worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.RF(p*7)+p*5, pos.getY()+5-p*12, pos.getZ()+UtilM.RF(p*7)+p*5,  0, -0.05-0.1*UtilM.RF(2), 0);
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+UtilM.RF(p*7)+p*5, pos.getY()+5-p*12, pos.getZ()+UtilM.RF(p*7)+p*5,  0, -0.05-0.1*UtilM.RF(2), 0);
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+UtilM.RF(p*7)+p*5, pos.getY()+5-p*12, pos.getZ()+UtilM.RF(p*7)+p*5,  -0.05+0.1*UtilM.RF(2), -0.05-0.1*UtilM.RF(2), 0);
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+UtilM.RF(p*7)+p*5, pos.getY()+5-p*12, pos.getZ()+UtilM.RF(p*7)+p*5,  -0.05+0.1*UtilM.RF(2), -0.05-0.1*UtilM.RF(2), 0);
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+UtilM.RF(p*7)+p*5, pos.getY()+5-p*12, pos.getZ()+UtilM.RF(p*7)+p*5,  -0.05+0.1*UtilM.RF(2), -0.05-0.1*UtilM.RF(2), 0);
		}
		else{
			UtilM.spawnEntityFX(new EntityFlameFXM(worldObj, pos.getX()+UtilM.RF(p*7)+p*5, pos.getY()+5-p*12, pos.getZ()+UtilM.RF(p*7)+p*5,  0, -0.05-0.1*UtilM.RF(2), 0));
			UtilM.spawnEntityFX(new EntityFlameFXM(worldObj, pos.getX()+UtilM.RF(p*7)+p*5, pos.getY()+5-p*12, pos.getZ()+UtilM.RF(p*7)+p*5,  0, -0.05-0.1*UtilM.RF(2), 0));
		}

	}
	public void processingParticelsForGunz(){
		for(int k=0; k<10; k++){
		UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+2.55, pos.getY()+1.3, pos.getZ()+0.5, -0.4, 0.025-0.05*UtilM.RF(), 0.025-0.05*UtilM.RF(),250,3+UtilM.RInt(2),0.1,1, 1, 0, 0, 0.7, 0.99));
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+2.7, pos.getY()+1.35, pos.getZ()+0.5, -0.05, 0.025-0.05*UtilM.RF(), 0.025-0.05*UtilM.RF());
		UtilM.spawnEntityFX(new EntitySparkFX(worldObj, pos.getX()+2.55, pos.getY()+1.3, pos.getZ()+0.5, 0.5F/16F, 0.1F,1,6,50,new Vec3M(-0.05F,0,0)));
		
		UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()-1.7, pos.getY()+1.35, pos.getZ()+0.5, 0.3, 0.025-0.05*UtilM.RF(), 0.025-0.05*UtilM.RF(),250,3+UtilM.RInt(2),0.1,1, 1, 0, 0, 0.7, 0.99));
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()-1.7, pos.getY()+1.35, pos.getZ()+0.5, 0.05, 0.025-0.05*UtilM.RF(), 0.025-0.05*UtilM.RF());
		UtilM.spawnEntityFX(new EntitySparkFX(worldObj, pos.getX()-1.7, pos.getY()+1.35, pos.getZ()+0.5, 0.5F/16F, 0.1F,1,6,50, new Vec3M(0.05F,0,0)));
		
		UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5, pos.getY()+1.35, pos.getZ()+2.7, 0.025-0.05*UtilM.RF(), 0.025-0.05*UtilM.RF(), -0.3,250,3+UtilM.RInt(2),0.1,1, 1, 0, 0, 0.7, 0.99));
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+0.5, pos.getY()+1.35, pos.getZ()+2.7, 0.025-0.05*UtilM.RF(), 0.025-0.05*UtilM.RF(), -0.05);
		UtilM.spawnEntityFX(new EntitySparkFX(worldObj, pos.getX()+0.5, pos.getY()+1.35, pos.getZ()+2.7, 0.5F/16F, 0.1F,1,6,50, new Vec3M(0,0,-0.05F)));
		
		UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5, pos.getY()+1.35, pos.getZ()-1.7, 0.025-0.05*UtilM.RF(), 0.025-0.05*UtilM.RF(), 0.3,250,3+UtilM.RInt(2),0.1,1, 1, 0, 0, 0.7, 0.99));
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+0.5, pos.getY()+1.35, pos.getZ()-1.7, 0.025-0.05*UtilM.RF(), 0.025-0.05*UtilM.RF(), 0.05);
		UtilM.spawnEntityFX(new EntitySparkFX(worldObj, pos.getX()+0.5, pos.getY()+1.35, pos.getZ()-1.7, 0.5F/16F, 0.1F,1,6,50,new Vec3M( 0,0, 0.05F)));
		}
	}
	public void processingParticelsEnding(){
		for(int r=0; r<2;r++){
		worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+1.08, pos.getY()+2, pos.getZ()+UtilM.RF(), 0, -0.1*UtilM.RF(2), 0);
		worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.RF(), pos.getY()+2, pos.getZ()+1.08, 0, -0.1*UtilM.RF(2), 0);
		worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()+UtilM.RF(), pos.getY()+2, pos.getZ()-0.08, 0, -0.1*UtilM.RF(2), 0);
			worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX()-0.08, pos.getY()+2, pos.getZ()+UtilM.RF(), 0, -0.1*UtilM.RF(2), 0);
		}
		worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+UtilM.RF(), pos.getY()+2, pos.getZ()+UtilM.RF(), 0, 0, 0);
		
		for(int f=0; f<10;f++)worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()-2.5+UtilM.RF(5), pos.getY()+1.01, pos.getZ()-2.5+UtilM.RF(5), 0, 0.1*UtilM.RF(2), 0);
		
	}
	public void processingParticelsEnding2(){
		for(int l=0; l<3;l++){
			worldObj.spawnParticle(EnumParticleTypes.CLOUD, pos.getX()+1.08,						  pos.getY()+1+UtilM.RF(), pos.getZ()+UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.CLOUD, pos.getX()+UtilM.RF(), pos.getY()+1+UtilM.RF(), pos.getZ()+1.08,						  0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.CLOUD, pos.getX()+UtilM.RF(), pos.getY()+1+UtilM.RF(), pos.getZ()-0.08,						  0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.CLOUD, pos.getX()-0.08,						  pos.getY()+1+UtilM.RF(), pos.getZ()+UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.CLOUD, pos.getX()+UtilM.RF(), pos.getY()+2,							   pos.getZ()+UtilM.RF(), 0, 0, 0);
		}
		
		for(int l=0; l<4;l++)worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()-2.5+UtilM.RF(5), pos.getY()+1.21, pos.getZ()-2.5+UtilM.RF(5), 0, 0.1*UtilM.RF(2), 0);
	
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX()+1.58,						  pos.getY()+1+UtilM.RF(), pos.getZ()+UtilM.RF(2), 0, 0, 0);
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX()+UtilM.RF(2), pos.getY()+1+UtilM.RF(), pos.getZ()+1.58,						  0, 0, 0);
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX()+UtilM.RF(2), pos.getY()+1+UtilM.RF(), pos.getZ()-0.58,						  0, 0, 0);
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX()-0.58,						  pos.getY()+1+UtilM.RF(), pos.getZ()+UtilM.RF(2), 0, 0, 0);
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX()+UtilM.RF(2), pos.getY()+2.5,							 pos.getZ()+UtilM.RF(2), 0, 0, 0);
		UtilM.spawnEntityFX(new EntitySparkFX(worldObj, pos.getX()+UtilM.RF()*5-2, pos.getY()+1, pos.getZ()+UtilM.RF()*5-2, 0.5F/16F, 0.1F,1,4,40, new Vec3M(0,0.03F,0)));
	}
	public void spawnCustomFire(){
		if(updateStructureHelper==true){
			if(worldObj.isRemote){
				if(!structureUpg[0]){
					if(optimizer3.progress==1+UtilM.RInt(1)){
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()+3.5+UtilM.RF()*p, pos.getY()+4-p*5, pos.getZ()+3.5+UtilM.RF()*p, -0.05, 0.3, -0.05,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()-2.5+UtilM.RF()*p, pos.getY()+4-p*5, pos.getZ()+3.5+UtilM.RF()*p,  0.05, 0.3, -0.05,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()+3.5+UtilM.RF()*p, pos.getY()+4-p*5, pos.getZ()-2.5+UtilM.RF()*p, -0.05, 0.3,  0.05,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()-2.5+UtilM.RF()*p, pos.getY()+4-p*5, pos.getZ()-2.5+UtilM.RF()*p,  0.05, 0.3,  0.05,false,1));
						
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()+2.5+UtilM.RF()*p, pos.getY()+5-p*5, pos.getZ()+3.5+UtilM.RF()*p, -0.05, 0.2, -0.072,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()-1.5+UtilM.RF()*p, pos.getY()+5-p*5, pos.getZ()+3.5+UtilM.RF()*p,  0.05, 0.2, -0.072,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()+2.5+UtilM.RF()*p, pos.getY()+5-p*5, pos.getZ()-2.5+UtilM.RF()*p, -0.05, 0.2,  0.072,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()-1.5+UtilM.RF()*p, pos.getY()+5-p*5, pos.getZ()-2.5+UtilM.RF()*p,  0.05, 0.2,  0.072,false,1));
						
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()+3.5+UtilM.RF()*p, pos.getY()+5-p*5, pos.getZ()+2.5+UtilM.RF()*p, -0.072, 0.2, -0.05,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()-2.5+UtilM.RF()*p, pos.getY()+5-p*5, pos.getZ()+2.5+UtilM.RF()*p,  0.072, 0.2, -0.05,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()+3.5+UtilM.RF()*p, pos.getY()+5-p*5, pos.getZ()-1.5+UtilM.RF()*p, -0.072, 0.2,  0.05,false,1));
						if(UtilM.RB())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()-2.5+UtilM.RF()*p, pos.getY()+5-p*5, pos.getZ()-1.5+UtilM.RF()*p,  0.072, 0.2,  0.05,false,1));
					}
				}
				else if(optimizer3.progress==1&&UtilM.RInt(3)==0)UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, pos.getX()+0.5+UtilM.RF()*p, pos.getY()+6-p*6, pos.getZ()+0.5+UtilM.RF()*p, 0.5-UtilM.RF(), 0.2+0.3*UtilM.RF(), 0.5-UtilM.RF(),true,1));
			}
		}
	}
	public void spawnBubles(int number){
		for(int pm=-1;pm<=1;pm+=2){
			if(number==1){
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5-4*pm, pos.getY()+1.2, pos.getZ()+0.5-2,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 0, 0.25, 0.8, 0.5, 0.99));
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5-4*pm, pos.getY()+1.2, pos.getZ()+0.5+2,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 0, 0.25, 0.8, 0.5, 0.99));
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5+2*pm, pos.getY()+1.2, pos.getZ()+0.5-4,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 0, 0.25, 0.8, 0.5, 0.99));
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5+2*pm, pos.getY()+1.2, pos.getZ()+0.5+4,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 0, 0.25, 0.8, 0.5, 0.99));
			}
			if(number==2){
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5-5*pm, pos.getY()+1.2, pos.getZ()+0.5-1,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 0.5, 0, 1, 0.5, 0.99));
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5-5*pm, pos.getY()+1.2, pos.getZ()+0.5+1,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 0.5, 0, 1, 0.5, 0.99));
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5+1*pm, pos.getY()+1.2, pos.getZ()+0.5-5,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 0.5, 0, 1, 0.5, 0.99));
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5+1*pm, pos.getY()+1.2, pos.getZ()+0.5+5,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 0.5, 0, 1, 0.5, 0.99));
			}
			if(number==3){
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5+5*pm, pos.getY()+1.2, pos.getZ()+0.5,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 1, 0, 0, 0.5, 0.99));
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,pos.getX()+0.5, pos.getY()+1.2, pos.getZ()+0.5+5*pm,  0, 0, 0,150,3+UtilM.RInt(2),1,true,1,"tx1", 1, 0, 0, 0.5, 0.99));
			}
		}
	}
	
	public void spawnParticleDa(){
		if(optimizer3.isTimeWithAddProgress()){
			EntitySmoothBubleFX a=new EntitySmoothBubleFX(worldObj,0.5+-15+UtilM.RInt(30)+pos.getX(), 1.5+UtilM.RInt(10)+pos.getY(), 0.5+-15+UtilM.RInt(30)+pos.getZ(),
					0.04*(UtilM.RB()?1:-1), 0.04*(UtilM.RB()?1:-1), 0.04*(UtilM.RB()?1:-1),
					300+UtilM.RInt(300),0,0,true,2,"tx1",UtilM.RF(),UtilM.RF(),UtilM.RF(),1, 0.99);
			UtilM.spawnEntityFX(a);
			a.noClip=true;
		}
		
		for (int la = 0; la < 4; ++la){
			double v0=UtilM.CRandD(3);
			double v2=UtilM.CRandD(3);
			double v1=UtilM.CRandD(1);
			if(UtilM.RB(5))worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+0.5F+v0, pos.getY() + 1.1F, pos.getZ()+0.5F+v2, -v0/18, v1/2, -v2/18);
			else UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5F+v0, pos.getY() + 1.1F, pos.getZ()+0.5F+v2, -v0/18, v1/2, -v2/18));
			}
		 	if(this.optimizer2.progress==1){
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()-7+UtilM.RF(), pos.getY()+1, pos.getZ()-3+UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()-7+UtilM.RF(), pos.getY()+1, pos.getZ()+3+UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+7+UtilM.RF(), pos.getY()+1, pos.getZ()-3+UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+7+UtilM.RF(), pos.getY()+1, pos.getZ()+3+UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()-3+UtilM.RF(), pos.getY()+1, pos.getZ()-7+UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+ 3F + UtilM.RF(), pos.getY() + 1, pos.getZ()- 7 + UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()-3+UtilM.RF(), pos.getY()+1, pos.getZ()+7 + UtilM.RF(), 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+3+UtilM.RF(), pos.getY()+1, pos.getZ()+7+UtilM.RF(), 0, 0, 0);
			}
		}
	public void spawnParticleNe(){
		for (int la = 0; la < 1; ++la)worldObj.spawnParticle(EnumParticleTypes.LAVA, pos.getX()+UtilM.RF(), pos.getY()+1,pos.getZ()+UtilM.RF(), 0,0,0);
	}

	@Override
	public void updateConnections() {
		// TODO Auto-generated method stub
		
	}


}
