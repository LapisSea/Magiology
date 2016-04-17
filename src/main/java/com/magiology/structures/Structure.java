package com.magiology.structures;

import java.util.ArrayList;

import com.magiology.util.utilclasses.UtilM;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Structure{
	
	public BlockAt[] BlocksAt=null;
	
	public ArrayList<BlockAt> BlocksAtInit=new ArrayList<BlockAt>();
	public boolean[] isBlockInPlace=null;
	private boolean isStructureCompleate,isStructureInitialized=false,firstCheck=true;
	public int StructureSize=0,
			
			nextBlock=0;
	
	/**
	 * @param world : world of the core block
	 * @param x : x of the core block
	 * @param y : y of the core block
	 * @param z : z of the core block
	 */
	public void checkForNextBlock(World world,BlockPos pos){
		if(!isStructureInitialized())throw new IllegalStateException("STRUCTURE NOT INITIALIZED!");
		if(firstCheck){
			firstCheck=false;
			for(int a=0;a<BlocksAt.length;a++)checkForNextBlock(world, pos);
			return;
		}
		BlockAt BA=BlocksAt[nextBlock];
		pos=pos.add(BA.pos);
		Block block=UtilM.getBlock(world, pos);
		isBlockInPlace[nextBlock]=(block==BA.bl);
//		if(!isBlockInPlace[nextBlock]){
//			Helper.spawnEntityFX(new EntityFlameFX(world, x+0.5, y+0.5, z+0.5, 0, 0.05, 0));
//			Helper.printInln(block,BA.bl);
//		}
		nextBlock++;
		if(nextBlock>StructureSize)nextBlock=0;
	}
	/**
	 * Call this after adding all blocks. Note: you can't repeat this!
	 */
	public Structure initializeStructure(){
		if(isStructureInitialized())throw new IllegalStateException("STRUCTURE ALREADY INITIALIZED");
		if(BlocksAtInit.isEmpty())  throw new NullPointerException("NO BLOCKS IN STRUCTURE");
		StructureSize=BlocksAtInit.size();
		isBlockInPlace=new boolean[StructureSize];
		BlocksAt=new BlockAt[StructureSize];
		for(int a=0;a<StructureSize;a++)BlocksAt[a]=BlocksAtInit.get(a);
		BlocksAtInit.clear();
		StructureSize-=1;
		return this;
	}
	public boolean isStructureCompleate(){
		isStructureInitialized=true;
		for(boolean a:isBlockInPlace)if(!a)isStructureInitialized=false;
		return isStructureInitialized;
	}
	public boolean isStructureInitialized(){
		if(BlocksAt!=null&&isBlockInPlace!=null&&StructureSize>0)isStructureCompleate=true;
		else isStructureCompleate=false;
		return isStructureCompleate;
	}
	
}
