package com.magiology.mc_objects.features.screen;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.magiology.handlers.TileEntityOneBlockStructure;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.util.objs.MultiTypeContainers.*;
import com.magiology.util.objs.vec.*;
import com.magiology.util.statics.*;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.*;

public class TileEntityScreen extends TileEntityOneBlockStructure<TileEntityScreen>{
	
	public boolean screenDirty=true;
	@SideOnly(Side.CLIENT)
	public TemporaryFrame screenTexture;
	public int mbId=-1;
	
	
	public class Plane{
		public List<PlanePart> parts;
		public boolean isVerticalX, isHorisontal;
		public int xSize, ySize, minX,minY,minZ,maxX,maxY,maxZ;
		public Plane(List<PlanePart> blocks){
			this.parts=blocks;
		}
		public Plane(List<PlanePart> blocks, boolean isVerticalX, boolean isHorisontal, int xSize, int ySize, int minX, int minY, int minZ){
			this.parts=blocks;
			this.isVerticalX=isVerticalX;
			this.isHorisontal=isHorisontal;
			this.xSize=xSize;
			this.ySize=ySize;
			this.minX=minX;
			this.minY=minY;
			this.minZ=minZ;
		}
		
	}
	private class PlanePart{
		TileEntityScreen tile;
		int x,y;
		
		public PlanePart(TileEntityScreen tile,int x,int y){
			this.tile=tile;
			this.x=x;
			this.y=y;
		}
		
	}
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		return super.writeToNBT(compound);
	}
	
	
	private Plane buildScreen(List<TileEntityScreen> blocks,Vec3M clickedPos){
		
		if(blocks.size()==1){
			EnumFacing face=getRotation();
			List<PlanePart> l=new ArrayList<>();
			l.add(new PlanePart(blocks.get(0),0,0));
			return new Plane(l,face.getFrontOffsetZ()!=0,face.getFrontOffsetY()!=0,1,1,this.x(),this.y(),this.z());
		}
		
		Map<Integer, List<PlanePart>> xBuildPlanes=new HashMap(), yBuildPlanes=new HashMap<>(), zBuildPlanes=new HashMap<>();
		blocks.forEach(t->{
			List<PlanePart> plane;
			int x=t.x(), y=t.y(), z=t.z();
			
			plane=xBuildPlanes.get(x);
			if(plane==null)xBuildPlanes.put(x, plane=new ArrayList<>());
			plane.add(new PlanePart(t,0,0));
			
			plane=yBuildPlanes.get(y);
			if(plane==null)yBuildPlanes.put(y, plane=new ArrayList<>());
			plane.add(new PlanePart(t,0,0));
			
			plane=zBuildPlanes.get(z);
			if(plane==null)zBuildPlanes.put(z, plane=new ArrayList<>());
			plane.add(new PlanePart(t,0,0));
		});
		List<Plane> xPlanes=new ArrayList(), yPlanes=new ArrayList<>(), zPlanes=new ArrayList<>();
		xBuildPlanes.forEach((i,v)->xPlanes.add(new Plane(v)));
		yBuildPlanes.forEach((i,v)->yPlanes.add(new Plane(v)));
		zBuildPlanes.forEach((i,v)->zPlanes.add(new Plane(v)));
		
		Queue<Plane> toRemove=new ArrayDeque<>(), toAdd=new ArrayDeque<>();
		Consumer<? super Plane> fix=rawPlane->{
			
			int xSize, ySize,
				minX=Integer.MAX_VALUE, minY=Integer.MAX_VALUE, minZ=Integer.MAX_VALUE, 
				maxX=Integer.MIN_VALUE, maxY=Integer.MIN_VALUE, maxZ=Integer.MIN_VALUE;
			boolean isVerticalX, isHorisontal;
			{//braces here to dump values
				int xSizeCalc, ySizeCalc;//using helper values to make xSize/ySize effectively final
				
				for(PlanePart tile:rawPlane.parts){
					BlockPos pos=tile.tile.getPos();
					minX=Math.min(minX, pos.getX());
					minY=Math.min(minY, pos.getY());
					minZ=Math.min(minZ, pos.getZ());
					
					maxX=Math.max(maxX, pos.getX());
					maxY=Math.max(maxY, pos.getY());
					maxZ=Math.max(maxZ, pos.getZ());
				}
				int ySizeHelper=maxY-minY, zSizeHelper=maxZ-minZ;//calculate on spot to prevent double calculation
				isVerticalX=zSizeHelper==0;//determine orientation of plane
				isHorisontal=ySizeHelper==0;
				
				if(isHorisontal){//convert 3D space to normalized (origin at 0) 2D space
					xSizeCalc=maxX-minX;
					ySizeCalc=zSizeHelper;
				}else if(isVerticalX){
					xSizeCalc=maxX-minX;
					ySizeCalc=ySizeHelper;
				}else{
					xSizeCalc=zSizeHelper;
					ySizeCalc=ySizeHelper;
				}
				xSize=xSizeCalc+1;//apply final size
				ySize=ySizeCalc+1;
			}
			rawPlane.isHorisontal=isHorisontal;
			rawPlane.isVerticalX=isVerticalX;
			rawPlane.xSize=xSize;
			rawPlane.ySize=ySize;
			rawPlane.minX=minX;
			rawPlane.minY=minY;
			rawPlane.minZ=minZ;
			rawPlane.maxX=maxX;
			rawPlane.maxY=maxY;
			rawPlane.maxZ=maxZ;
			
			

			PlanePart[][] grid=new PlanePart[xSize][ySize];
			
			if(isHorisontal){//convert mixed tileEntitys from list to 2D grid
				for(PlanePart p:rawPlane.parts){
					TileEntityScreen t=p.tile;
					grid[p.x=(t.x()-rawPlane.minX)][p.y=(t.z()-rawPlane.minZ)]=p;
				}
			}else if(isVerticalX){
				for(PlanePart p:rawPlane.parts){
					TileEntityScreen t=p.tile;
					grid[p.x=(t.x()-rawPlane.minX)][p.y=(t.y()-rawPlane.minY)]=p;
				}
			}else{
				for(PlanePart p:rawPlane.parts){
					TileEntityScreen t=p.tile;
					grid[p.x=(t.z()-rawPlane.minZ)][p.y=(t.y()-rawPlane.minY)]=p;
				}
			}
			if(rawPlane.parts.size()<2)return;//is 1 block or empty? Than it can't have holes, hence nothing is there to be fixed
			// Number of elements shows that the plane can be only full.
			// Discontinuing fixing!
			if(xSize*ySize==rawPlane.parts.size())return;
			
			for(int x=0;x<grid.length;x++){
				for(int y=0;y<grid[x].length;y++){
					PlanePart part=grid[x][y];
					if(part==null)continue;//is a hole? No need to use that!
					TileEntityScreen tile=part.tile;
					
					int minX1=x, minY1=y, maxX1=minX1, maxY1=minY1;
					boolean xHasNoHole=true, yHasNoHole=true;
					
					while(xHasNoHole||yHasNoHole){//try to expand until x and y get stuck on a hole
						if(yHasNoHole){
							if(minY1==0)yHasNoHole=false;
							else{
								minY1--;//expand rectangle
								for(int x1=minX1; x1<maxX1+1; x1++){
									if(grid[x1][minY1]==null){//hole found! Reverting expansion and discontinuing it. 
										minY1++;
										yHasNoHole=false;
										break;
									}
								}
							}
						}
						if(xHasNoHole){
							if(minX1==0)xHasNoHole=false;
							else{
								minX1--;
								for(int y1=minY1; y1<maxY1+1; y1++){
									if(grid[minX1][y1]==null){
										minX1++;
										xHasNoHole=false;
										break;
									}
								}
							}
						}
					}
					
					//convert bounding rectangle to usable list
					List<TileEntityScreen> newPlaneBlocks=new ArrayList<>();
					
					for(int x1=minX1;x1<maxX1+1;x1++){
						for(int y1=minY1;y1<maxY1+1;y1++){
							TileEntityScreen tile1=grid[x1][y1].tile;
							if(tile1!=null){
								newPlaneBlocks.add(tile1);
							}
						}
					}
					//did the plane expand? Than add it to said plane collection. 
					if(newPlaneBlocks.size()>1){
						Plane pl=new Plane(CollectionConverter.convLi(newPlaneBlocks,PlanePart.class,t->new PlanePart(t,0,0)), isVerticalX, isHorisontal, xSize, ySize,minX,minY,minZ);
						toAdd.add(pl);

						if(isHorisontal){//convert mixed tileEntitys from list to 2D grid
							for(PlanePart p:pl.parts){
								TileEntityScreen t=p.tile;
								p.x=t.x()-rawPlane.minX;
								p.y=t.z()-rawPlane.minZ;
							}
						}else if(isVerticalX){
							for(PlanePart p:pl.parts){
								TileEntityScreen t=p.tile;
								p.x=t.x()-rawPlane.minX;
								p.y=t.y()-rawPlane.minY;
							}
						}else{
							for(PlanePart p:pl.parts){
								TileEntityScreen t=p.tile;
								p.x=t.z()-rawPlane.minZ;
								p.y=t.y()-rawPlane.minY;
							}
						}
					}
				}
			}
			//anything generated from broken plane? Than remove it. 
			if(!toAdd.isEmpty())toRemove.add(rawPlane);
		};
		
		//call fix on each axis of planes and handle removing/adding
		xPlanes.forEach(fix);
		xPlanes.removeAll(toRemove);
		xPlanes.addAll(toAdd);
		toRemove.clear();
		toAdd.clear();
		
		yPlanes.forEach(fix);
		yPlanes.removeAll(toRemove);
		yPlanes.addAll(toAdd);
		toRemove.clear();
		toAdd.clear();
		
		zPlanes.forEach(fix);
		zPlanes.removeAll(toRemove);
		zPlanes.addAll(toAdd);
		toRemove.clear();
		toAdd.clear();
		
		//combine for analysis
		List<Plane> allPlanes=new ArrayList<>();
		allPlanes.addAll(xPlanes);
		allPlanes.addAll(yPlanes);
		allPlanes.addAll(zPlanes);
		
		allPlanes.removeIf(p->p.parts.stream().noneMatch(part->part.tile==this));
		
		if(allPlanes.size()==1)return allPlanes.get(0);//only 1 plane? than no need to choose a plane
		else{
			int maxSize=allPlanes.stream().max((p1, p2)->Integer.compare(p1.parts.size(), p2.parts.size())).orElse(null).parts.size();
			//get all planes with biggest size
			List<Plane> biggestPlanes=allPlanes.stream().filter(plane->plane.parts.size()==maxSize).collect(Collectors.toList());
			if(biggestPlanes.size()==1)return biggestPlanes.get(0);//only 1 plane with maxSize?
			
			double smallestDistance=1000000000;
			Plane closestPlane=null;
			for(Plane plane:biggestPlanes){
				double distance=plane.parts.stream().mapToDouble(tile->clickedPos.sub(0.5F).distanceTo(tile.tile.getPos())).min().orElse(1000000000);
				if(smallestDistance>distance){
					smallestDistance=distance;
					closestPlane=plane;
				}
			}
			return closestPlane;
		}
	}
	
	public EnumFacing getRotation(){
		return BlockScreen.getRotation(getState());
	}
	@Override
	public void filterBlocks(List<TileEntityScreen> parts,Vec3i clickedPos){
		Plane i=buildScreen(parts,new Vec3M(clickedPos));
		parts.clear();
		i.parts.forEach(p->parts.add(p.tile));
//		parts.removeIf(t->i.parts.stream().noneMatch(p->p.tile==t));
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
}
