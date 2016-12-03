package com.magiology.util.objs;

import static net.minecraft.util.EnumFacing.*;

import net.minecraft.util.EnumFacing;

public class BlockSides{
	
	private final boolean	sides[]			=new boolean[6];
	private int				straight		=3;
	private boolean			straightDirty	=true;
	
	public void setSide(EnumFacing facing, boolean flag){
		setSide(facing.getIndex(), flag);
	}
	
	public void setSide(int facing, boolean flag){
		sides[facing]=flag;
		straightDirty=true;
	}
	
	public boolean getSide(EnumFacing facing){
		return getSide(facing.getIndex());
	}
	
	public boolean getSide(int facing){
		return sides[facing];
	}

	public boolean isStraight(EnumFacing facing){
		return isStraight(facing);
	}
	/**
	 * 3 = no straight lines
	 */
	public int getStraight(){
		if(straightDirty){
			straightDirty=false;
			straight=calcMirrored();
		}
		return straight;
	}
	
	public boolean isStraight(int facing){
		return facing/2==getStraight();
	}
	
	public boolean getSideNotStraight(EnumFacing facing){
		return getSideNotStraight(facing.getIndex());
	}
	
	public boolean getSideNotStraight(int facing){
		return getStraight()==3&&getSide(facing);
	}
	
	protected int calcMirrored(){
		if(getSide(UP)&&getSide(DOWN)&&    !getSide(NORTH)&&!getSide(SOUTH)&&!getSide(EAST)&&!getSide(WEST)  )return 0;
		if(getSide(NORTH)&&getSide(SOUTH)&&!getSide(UP)&&!getSide(DOWN)&&    !getSide(EAST)&&!getSide(WEST)  )return 1;
		if(getSide(EAST)&&getSide(WEST)&&  !getSide(UP)&&!getSide(DOWN)&&    !getSide(NORTH)&&!getSide(SOUTH))return 2;
		return 3;
	}
	
}
