package com.magiology.structures;

import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.vectors.Pos;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class SymmetryBoot{
	
	/**
	 * Use this when structure is symmetrical. (if block on x position is the same at -x position)
	 * see all functions at (http://oi60.tinypic.com/2ih5slw.jpg)
	 * X symmetry type=1
	 * Z symmetry type=2
	 * X & Z  symmetry type=3
	 * WARNING y is not supported
	*/
	public void symmetryBoot(BlockAt blockat,int type,Structure s){
		if(!s.isStructureInitialized()){
			BlockAt[] blocksat;
			Block block=blockat.bl;
			int x=blockat.pos.getX(),y=blockat.pos.getY(),z=blockat.pos.getZ();
			switch (type){
			case 1://----------------------------
				if(blockat.pos.getX()==0){
					blocksat=new BlockAt[1];
					blocksat[0]=blockat;
				}else{
					blocksat=new BlockAt[2];
					blocksat[0]=new BlockAt(block, blockat.pos);
					blocksat[1]=new BlockAt(block, new BlockPos(-blockat.pos.getX(), -blockat.pos.getY(), -blockat.pos.getZ()));
				}
			break;
			case 2://----------------------------
				if(blockat.pos.getZ()==0){
					blocksat=new BlockAt[1];
					blocksat[0]=blockat;
				}else{
					blocksat=new BlockAt[2];
					blocksat[0]=new BlockAt(block,  blockat.pos);
					blocksat[1]=new BlockAt(block,  new Pos(x, y,-z));
				}
			break;
			case 3://----------------------------
				if(x==0&&z==0){
					blocksat=new BlockAt[1];
					blocksat[0]=blockat;
				}else if(z==0||x==0){
					final int size=z==0?x:z;
					blocksat=new BlockAt[4];
					blocksat[0]=new BlockAt(block,  size, y, 0);
					blocksat[1]=new BlockAt(block, -size, y, 0);
					blocksat[2]=new BlockAt(block,  0, y, size);
					blocksat[3]=new BlockAt(block, 0, y, -size);
				}else{
					blocksat=new BlockAt[4];
					blocksat[0]=new BlockAt(block, blockat.pos);
					blocksat[1]=new BlockAt(block, new BlockPos(-blockat.pos.getX(), -blockat.pos.getY(), -blockat.pos.getZ()));
					blocksat[2]=new BlockAt(block,  x, y,-z);
					blocksat[3]=new BlockAt(block, -x, y,-z);
				}
			break;//----------------------------
			default:blocksat=null;break;
			}
			if(blocksat!=null)for(int a=0;a<blocksat.length;a++)s.BlocksAtInit.add(blocksat[a]);
			else PrintUtil.println("WRONG TYPE ADDED!\nTHIS FUNCTION CANCELD ALL EFFECTS!\n");
		}else PrintUtil.println("YOU CAN'T ADD BLOCKS TO STRUCTURE AFTER INITIALIZING!\nFUNCTION CANCELD!\n");
	}
}
