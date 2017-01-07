package com.magiology.mc_objects.tile.multiblock;

import com.magiology.util.m_extensions.BlockPosM;

public class Link{
	
	private final BlockPosM	point,source;
	private LinkStatus		status	=LinkStatus.UNKNOWN, prevStatus=LinkStatus.UNKNOWN;
	
	public LinkStatus getPrevStatus(){
		return prevStatus;
	}
	
	public Link(BlockPosM point,BlockPosM source){
		this.point=point;
		this.source=source;
	}
	
	public LinkStatus getStatus(){
		return status;
	}
	
	public void setStatus(LinkStatus status){
		this.status=status;
	}
	
	public boolean change(){
		boolean b=status!=prevStatus;
		prevStatus=status;
		return b;
	}
	
	@Override
	public String toString(){
		return "["+point.toString()+", stat="+status+"]";
	}
	
	@Override
	public int hashCode(){
		return point.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return obj instanceof Link&&equals((Link)obj);
	}
	
	public boolean equals(Link obj){
		return obj.getPoint().equals(getPoint())&&obj.getSrcPos().equals(getSrcPos());
	}
	
	public BlockPosM getPoint(){
		return point;
	}
	public BlockPosM getSrcPos(){
		return point;
	}
}
