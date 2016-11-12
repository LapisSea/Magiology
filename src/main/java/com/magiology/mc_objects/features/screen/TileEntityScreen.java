package com.magiology.mc_objects.features.screen;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.magiology.handlers.TileEntityOneBlockStructure;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.MultiTypeContainers.*;
import com.magiology.util.objs.vec.*;
import com.magiology.util.statics.*;
import com.magiology.util.statics.Structure.Plane;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.*;

public class TileEntityScreen extends TileEntityOneBlockStructure<TileEntityScreen>{
	
	private static TileEntityScreen highlightedTile;
	
	public static void setHighlighted(TileEntityScreen tile, float hitX,float hitY,float hitZ){
		TileEntityScreen brain=null;
		if(tile!=null){
			if(tile.hasBrain())brain=tile.getBrain();
		}
		
		if(highlightedTile==tile){
			if(brain!=null)brain.onHighlight(BlockScreen.calcScreenPos(tile, hitX, hitY, hitZ));
			return;
		}
		
		if(highlightedTile!=null){
			if(tile==null&&highlightedTile.hasBrain())highlightedTile.getBrain().onExit();
		}else if(brain!=null){
			brain.onHighlight(BlockScreen.calcScreenPos(tile, hitX, hitY, hitZ));
			brain.onEnter();
		}
		highlightedTile=tile;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean screenDirty=true;
	@SideOnly(Side.CLIENT)
	public TemporaryFrame screenTexture;
	private int mbId=-1;
	public Vec2FM click=new Vec2FM(-1,-1),highlight=new Vec2FM(-1,-1);
	public boolean highlighted;
	
	
	public int getMbId(){
		if(hasBrain()){
			if(mbId==-1)mbId=getBrain().getMultiblock().indexOf(this);
		}
		else if(mbId!=-1)mbId=-1;
		
		return mbId;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		return super.writeToNBT(compound);
	}
	
	public EnumFacing getRotation(){
		return BlockScreen.getRotation(getState());
	}
	@Override
	public void filterBlocks(List<TileEntityScreen> parts,Vec3i clickedPos){
		parts.removeIf(t->t.hasBrain());
		if(parts.isEmpty())return;
		TileEntityScreen clicked=new BlockPosM(clickedPos).getTile(worldObj, TileEntityScreen.class);
		Plane<TileEntityScreen> i=Structure.buildScreen(parts,clicked);
		
		parts.removeIf(t->i.parts.stream().noneMatch(p->p.tile==t));
	}
	@Override
	protected List<TileEntityScreen> explore(List<TileEntityScreen> parts){
		parts.add(this);
		EnumFacing rotation=getRotation();
		UtilM.getTileSides(getWorld(), getPos()).forEach(tile->{
			if(checkType(tile)){
				TileEntityScreen t=(TileEntityScreen)tile;
				if(t.getBrain()==null&&!parts.contains(t)&&rotation==t.getRotation())t.explore(parts);
			}
		});
		
		return parts;
	}
	@Override
	public boolean validateLoaded(TileEntity tile){
		return true;
	}
	@SuppressWarnings("incomplete-switch")
	@Override
	protected MultiTypeContainerX createBrainObject(List<TileEntityScreen> multiblock){
		
		MultiTypeContainer2<List<Vec2i>,Vec2i> data=new MultiTypeContainer2<List<Vec2i>,Vec2i>(){
			
			List<Vec2i> offsets;
			@Override public List<Vec2i> get1(){return offsets;}
			@Override public void set1(List<Vec2i> t1){offsets=t1;}
			
			Vec2i size;
			@Override public Vec2i get2(){return size;}
			@Override public void set2(Vec2i t2){size=t2;}
		};
		try{
			Vec2i min=new Vec2i(Integer.MAX_VALUE,Integer.MAX_VALUE),max=new Vec2i(Integer.MIN_VALUE,Integer.MIN_VALUE);
			
			List<Vec3M> offsets=CollectionConverter.convLi(multiblock,Vec3M.class,t->new Vec3M(t.getPos().subtract(getPos())));
			
			boolean 
				noneXOffset=offsets.stream().allMatch(b->b.getX()==0),
				noneYOffset=offsets.stream().allMatch(b->b.getY()==0);/*,
				noneZOffset=noneYOffset?offsets.stream().allMatch(b->b.getZ()==0):true;*/
			data.set1(CollectionConverter.convLi(offsets,Vec2i.class,offset->{
				
				Vec2FM offset2D_float=new Vec2FM();
				if(noneXOffset)offset.swizzleZY(offset2D_float);
				else if(noneYOffset)offset.swizzleXZ(offset2D_float);
				else offset.swizzleXY(offset2D_float);
				
				Vec2i offset2D=offset2D_float.toVec2i();
				
				if(min.x>offset2D.x||min.y>offset2D.y){
					min.x=offset2D.x;
					min.y=offset2D.y;
				}
				if(max.x<offset2D.x||max.y<offset2D.y){
					max.x=offset2D.x;
					max.y=offset2D.y;
				}
				
				return offset2D;
			}));
			data.set2(max.sub(min).add(1));
			IntStream minxs=data.get1().stream().mapToInt(off->off.x),minys=data.get1().stream().mapToInt(off->off.y);
			OptionalInt minxo=null,minyo=null;
			switch(getRotation()){
			case UP:{
				minxo=minxs.min();
				minyo=minys.max();
			}break;
			case DOWN:{
				minxo=minxs.min();
				minyo=minys.max();
			}break;
			case EAST:{
				minxo=minxs.max();
				minyo=minys.max();
			}break;
			case NORTH:{
				minxo=minxs.max();
				minyo=minys.max();
			}break;
			case SOUTH:{
				minxo=minxs.min();
				minyo=minys.max();
			}break;
			case WEST:{
				minxo=minxs.min();
				minyo=minys.max();
			}break;
			}
			
			int minx=minxo.getAsInt(),miny=minyo.getAsInt();
			for(Vec2i off:data.get1()){
				off.x-=minx;
				off.y-=miny;
			}
			switch(getRotation()){
			case EAST:for(Vec2i off:data.get1())off.x*=-1;break;
			case NORTH:for(Vec2i off:data.get1())off.x*=-1;break;
			case UP:for(Vec2i off:data.get1())off.y=1-data.get2().y-off.y;break;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return data;
	}
	@Override
	protected void onMultiblockJoin(List<TileEntityScreen> multiblock,TileEntityScreen brain){
		mbId=multiblock.indexOf(this);
	}
	@Override
	protected void onMultiblockLeave(List<TileEntityScreen> multiblock){
		mbId=-1;
	}
	
	@Override
	public MultiTypeContainer2<List<Vec2i>,Vec2i> getBrainObjects(){
		return (MultiTypeContainer2<List<Vec2i>,Vec2i>)super.getBrainObjects();
	}

	public void onClick(Vec2FM pos){
		click=pos;
	}
	public void onHighlight(Vec2FM pos){
		highlight=pos;
	}
	public void onEnter(){
		highlighted=true;
	}
	public void onExit(){
		highlighted=false;
	}
}
