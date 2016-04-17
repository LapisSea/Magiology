package com.magiology.mcobjects.tileentityes;

import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.structures.Structure;
import com.magiology.structures.Structures;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;

public class TileEntityPLauncher extends TileEntityM implements ITickable{
	
	SlowdownUtil optimizer=new SlowdownUtil(100);
	Structure MultiBlock=null;
	int spawn=0;
	int buffer=0;
	int rand=0;
	int block1=-1;
	int block2=-1;
	int block3=-1;
	int block4=-1;
	int block5=-1;
	int block6=-1;
	int block7=-1;
	
	@Override
	public void update(){
		if(MultiBlock==null)MultiBlock=Structures.generateNewStructure(1);
//		if(worldObj.getTotalWorldTime()%30==0)MultiBlock=Structures.generateNewStructure(1);
		MultiBlock.checkForNextBlock(worldObj, pos);
		MultiBlock.isStructureCompleate();
		if(worldObj.isRemote){
//				BlockAt blat=MultiBlock.BlocksAt[MultiBlock.nextBlock];
//				int id=MultiBlock.nextBlock;
//				if(MultiBlock.isBlockInPlace[id]==false)UtilM.println(
//				MultiBlock.isStructureCompleate()+" "+MultiBlock.StructureSize+":"+id+" "+
//				blat.bl.getUnlocalizedName()+":\t"+blat.x+" "+blat.y+" "+blat.z
//				+"\n");
			
			
			if(MultiBlock.isStructureCompleate()&&worldObj.canBlockSeeSky(pos)){
				if(optimizer.isTimeWithAddProgress()){
					blocks();
					if(worldObj.rand.nextInt(2)==0){
						if(!(worldObj.isBlockIndirectlyGettingPowered(pos)>0)){
							spawnP();
						}
						else {
							buffer++;
							UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+0.5, y()+1.2, z()+0.5, 0, 0.03, 0,400,4,1,true,2,"tx1", 1, 1, 1, 0.5, 0.99));
						}
					}
				}
				
				if(worldObj.isBlockIndirectlyGettingPowered(pos)==0&&buffer>0){
					spawnP();
					buffer--;
				}
				
				
				if(spawn>0){
					spawn--; 
					boolean random=worldObj.rand.nextBoolean();
					UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+1+worldObj.rand.nextFloat(), y()+3.2, z()-1+worldObj.rand.nextFloat(), 0, 0.01, 0,200,4+worldObj.rand.nextInt(2),1,1, random?0:1, 0, random?1:0, 1, 0.99));
					UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()-1+worldObj.rand.nextFloat(), y()+3.2, z()-1+worldObj.rand.nextFloat(), 0, 0.01, 0,200,4+worldObj.rand.nextInt(2),1,1, random?0:1, 0, random?1:0, 1, 0.99));
					UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()+1+worldObj.rand.nextFloat(), y()+3.2, z()+1+worldObj.rand.nextFloat(), 0, 0.01, 0,200,4+worldObj.rand.nextInt(2),1,1, random?0:1, 0, random?1:0, 1, 0.99));
					UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,x()-1+worldObj.rand.nextFloat(), y()+3.2, z()+1+worldObj.rand.nextFloat(), 0, 0.01, 0,200,4+worldObj.rand.nextInt(2),1,1, random?0:1, 0, random?1:0, 1, 0.99));
				}
				
				
				if(block1==-1&&block2==-1&&block3==-1&&block4==-1&&block5==-1&&block6==-1)rand=worldObj.rand.nextInt(7);
				else{
					for(int g=1;g!=-1;){
						g=0;
						int rendem=worldObj.rand.nextInt(6);
						if(rendem==0&&block1==1){
							rand=1;
						}
						else if(rendem==1&&block2==1){
							rand=2;
						}
						else if(rendem==2&&block3==1){
							rand=3;
						}
						else if(rendem==3&&block4==1){
							rand=4;
						}
						else if(rendem==4&&block5==1){
							rand=5;
						}
						else if(rendem==5&&block6==1){
							rand=6;
						}
						else g=-1;
					}
				}
				
			}
			else worldObj.spawnParticle(EnumParticleTypes.FLAME, x()+worldObj.rand.nextFloat(), y()+1.1, z()+worldObj.rand.nextFloat(),0,0.01,0);
		}
	}
	
	public void blocks(){
		if(	U.getBlock(worldObj,pos.add(1, 1, 0))==Blocks.redstone_lamp||
			U.getBlock(worldObj,pos.add(-1, 1, 0))==Blocks.redstone_lamp||
			U.getBlock(worldObj,pos.add(0, 1, 1))==Blocks.redstone_lamp||
			U.getBlock(worldObj,pos.add(0, 1, -1))==Blocks.redstone_lamp
			){
			block1=1;
		}
		else block1=-1;
		if(	U.getBlock(worldObj,pos.add(1, 1, 0))==Blocks.bookshelf||
				U.getBlock(worldObj,pos.add(-1, 1, 0))==Blocks.bookshelf||
				U.getBlock(worldObj,pos.add(0, 1, 1))==Blocks.bookshelf||
				U.getBlock(worldObj,pos.add(0, 1, -1))==Blocks.bookshelf
				){
			block1=1;
		}
		else block1=-1;
		if(	U.getBlock(worldObj,pos.add(1, 1, 0))==Blocks.glass||
				U.getBlock(worldObj,pos.add(-1, 1, 0))==Blocks.glass||
				U.getBlock(worldObj,pos.add(0, 1, 1))==Blocks.glass||
				U.getBlock(worldObj,pos.add(0, 1, -1))==Blocks.glass
				){
			block2=1;
		}
		else block2=-1;
		if(	U.getBlock(worldObj,pos.add(1, 1, 0))==Blocks.sand||
				U.getBlock(worldObj,pos.add(-1, 1, 0))==Blocks.sand||
				U.getBlock(worldObj,pos.add(0, 1, 1))==Blocks.sand||
				U.getBlock(worldObj,pos.add(0, 1, -1))==Blocks.sand
				){
			block3=1;
		}
		else block3=-1;
		if(	U.getBlock(worldObj,pos.add(1, 1, 0))==Blocks.wool||
				U.getBlock(worldObj,pos.add(-1, 1, 0))==Blocks.wool||
				U.getBlock(worldObj,pos.add(0, 1, 1))==Blocks.wool||
				U.getBlock(worldObj,pos.add(0, 1, -1))==Blocks.wool
				){
			block4=1;
		}
		else block4=-1;
		if(	U.getBlock(worldObj,pos.add(1, 1, 0))==Blocks.soul_sand||
				U.getBlock(worldObj,pos.add(-1, 1, 0))==Blocks.soul_sand||
				U.getBlock(worldObj,pos.add(0, 1, 1))==Blocks.soul_sand||
				U.getBlock(worldObj,pos.add(0, 1, -1))==Blocks.soul_sand
				){
			block5=1;
		}
		else block5=-1;
		if(	U.getBlock(worldObj,pos.add(1, 1, 0))==Blocks.nether_brick_fence||
				U.getBlock(worldObj,pos.add(-1, 1, 0))==Blocks.nether_brick_fence||
				U.getBlock(worldObj,pos.add(0, 1, 1))==Blocks.nether_brick_fence||
				U.getBlock(worldObj,pos.add(0, 1, -1))==Blocks.nether_brick_fence
				){
			block6=1;
		}
		else block6=-1;
	}
	
	private void spawnP() {
		
//		rand=5;
		
		float random1=worldObj.rand.nextFloat();
		if(worldObj.isRemote){
			for(int a=0;a<2;a++){
		UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,
				x()+worldObj.rand.nextFloat(), y()+3+worldObj.rand.nextFloat(), z()+worldObj.rand.nextFloat(), (0.5-worldObj.rand.nextFloat())*random1, -1+(worldObj.rand.nextFloat())*random1, (0.5-worldObj.rand.nextFloat())*random1,
				300+worldObj.rand.nextInt(500),50,0.2,1, worldObj.rand.nextFloat(), worldObj.rand.nextFloat(), worldObj.rand.nextFloat(), 0.5+worldObj.rand.nextFloat()/2, 0.99));
		} }
		
		float random=worldObj.rand.nextFloat();
		if(rand==0){
			for(int a=0;a<10;a++){
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,
						x()+worldObj.rand.nextFloat(), y()+1+worldObj.rand.nextFloat(), z()+worldObj.rand.nextFloat(), 
						(0.5-worldObj.rand.nextFloat())*random, 1+(worldObj.rand.nextFloat())*random, (0.5-worldObj.rand.nextFloat())*random,
						300+worldObj.rand.nextInt(500),50,0.2,1, 
						worldObj.rand.nextFloat(), worldObj.rand.nextFloat(), worldObj.rand.nextFloat(), 0.5+worldObj.rand.nextFloat()/2, 0.99));
				}
		}
		else if(rand==1){
			for(int a=0;a<10;a++){
				UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,
						x()+worldObj.rand.nextFloat(), y()+1+worldObj.rand.nextFloat(), z()+worldObj.rand.nextFloat(), 
						(0.5-worldObj.rand.nextFloat())*random, 1+(worldObj.rand.nextFloat())*random, (0.5-worldObj.rand.nextFloat())*random,
						300+worldObj.rand.nextInt(500),50,0.2,true,2,"tx1", 
						worldObj.rand.nextFloat(), worldObj.rand.nextFloat(), worldObj.rand.nextFloat(), 0.5+worldObj.rand.nextFloat()/2, 0.99));
				}
		}
		else if(rand==2){
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,
					x()+0.5, y()+1.1, z()+0.5, 
					0, 2+worldObj.rand.nextFloat(), 0,
					800,10,1,true,3,"tx3", 
					0, 0, 1, 1, 0.99));
		}
		else if(rand==3){
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,
					x()+0.5, y()+1.1, z()+0.5, 
					0, 2+worldObj.rand.nextFloat(), 0,
					800,10,1,true,4,"tx3", 
					0, 0, 1, 1, 0.99));
		}
		else if(rand==4){
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,
					x()+0.5, y()+1.1, z()+0.5, 
					0, 3+worldObj.rand.nextFloat(), 0,
					800,10,1,true,5,"tx3", 
					0, 0, 1, 1, 0.99));
		}
		else if(rand==5){
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,
					x()+0.5, y()+1.1, z()+0.5, 
					0, 1.5+worldObj.rand.nextFloat()*0.5, 0,
					800,10,-2,true,6,"tx3", 
					1, 0, 0, 1, 0.99));
		}
		else if(rand==6){
			UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,
					x()+0.5, y()+1.1, z()+0.5, 
					0, 5+worldObj.rand.nextFloat(), 0,
					800,15,-2,true,7,"tx3", 
					1, 0, 0, 1, 0.99));
		}
		
		
	}
	
}
