package com.magiology.util.utilclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SideUtil{
	
	private static List<Integer> normalSides=new ArrayList<>();
	static Random rand = new Random();
	static int[] x={0,0,0,1,0,-1};
	static int[] y={1,-1,0,0,0,0};
	static int[] z={0,0,-1,0,1,0};
	static{
		for(int i=0;i<6;i++)normalSides.add(i);
	}
	
//	switch(EnumFacing){
//	case 0:{y+=1;}break;
//	case 1:{y-=1;}break;
//	case 2:{z-=1;}break;
//	case 3:{x+=1;}break;
//	case 4:{z+=1;}break;
//	case 5:{x-=1;}break;
//	}
	
	public static int DOWN(){
		return enumFacingOrientation(EnumFacing.DOWN);
	}
	
	
	public static int EAST(){
		return enumFacingOrientation(EnumFacing.EAST);
	}
	public static int enumFacingOrientation(EnumFacing fDir){
		if(fDir==null)return -1;
		return fDir.getIndex();
	}
	public static int getOppositeSide(int side){
		int result=-1;
		switch(side){
		case 0:result=1;break;
		case 1:result=0;break;
		case 2:result=3;break;
		case 3:result=2;break;
		case 4:result=5;break;
		case 5:result=4;break;
		}
//		Helper.printInln(EnumFacing.getFront(5));
		return result;
	}


	public static TileEntity[] getTilesOnSides(TileEntity tileEntity){
		return getTilesOnSides(tileEntity.getWorld(), tileEntity.getPos());
	}
	public static TileEntity[] getTilesOnSides(World world, BlockPos pos){
		TileEntity[] result=new TileEntity[6];
		if(!U.isNull(world,pos))for(int i=0;i<6;i++)result[i]=world.getTileEntity(offsetNew(i, pos));
		return result;
	}
	public static int NORTH(){
		return enumFacingOrientation(EnumFacing.NORTH);
	}
	public static BlockPos offset(int side,BlockPos pos){
		return pos.add(x[side],y[side],z[side]);
	}
	public static BlockPos offsetNew(int side,BlockPos pos){
		return pos.offset(EnumFacing.getFront(side));
	}
	public static int[] randomizeSides(){
		int[] side=new int[6];
		List<Integer> list=new ArrayList<>();
		list.addAll(normalSides);
		for(int i=0;i<side.length;i++){
			side[i]=list.remove(RandUtil.RI(list.size()));
		}
		return side;
	}
	public static int SOUTH(){
		return enumFacingOrientation(EnumFacing.SOUTH);
	}
	public static int UP(){
		return enumFacingOrientation(EnumFacing.UP);
	}
	public static int WEST(){
		return enumFacingOrientation(EnumFacing.WEST);
	}
}
