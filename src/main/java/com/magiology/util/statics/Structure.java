package com.magiology.util.statics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.vec.Vec3M;

import net.minecraft.util.math.BlockPos;

public class Structure{
	

	public static class Plane<T extends TileEntityM>{
		public List<PlanePart<T>> parts;
		public boolean isVerticalX, isHorisontal;
		public int xSize, ySize, minX,minY,minZ,maxX,maxY,maxZ;
		public Plane(List<PlanePart<T>> blocks){
			this.parts=blocks;
		}
		public Plane(List<PlanePart<T>> blocks, boolean isVerticalX, boolean isHorisontal, int xSize, int ySize, int minX, int minY, int minZ){
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
	public static class PlanePart<T extends TileEntityM>{
		public T tile;
		public int x,y;
		
		public PlanePart(T tile,int x,int y){
			this.tile=tile;
			this.x=x;
			this.y=y;
		}
		
	}
	
	public static <T extends TileEntityM> Plane<T> buildPlane(List<T> blocks,T clicked){
		
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
		List<Plane<T>> xPlanes=new ArrayList(), yPlanes=new ArrayList<>(), zPlanes=new ArrayList<>();
		xBuildPlanes.forEach((i,v)->xPlanes.add(new Plane(v)));
		yBuildPlanes.forEach((i,v)->yPlanes.add(new Plane(v)));
		zBuildPlanes.forEach((i,v)->zPlanes.add(new Plane(v)));
		
		Queue<Plane<T>> toRemove=new ArrayDeque<>(), toAdd=new ArrayDeque<>();
		Consumer<? super Plane<T>> fix=rawPlane->{
			
			int xSize, ySize,
				minX=Integer.MAX_VALUE, minY=Integer.MAX_VALUE, minZ=Integer.MAX_VALUE,
				maxX=Integer.MIN_VALUE, maxY=Integer.MIN_VALUE, maxZ=Integer.MIN_VALUE;
			boolean isVerticalX, isHorisontal;
			{//braces here to dump values
				int xSizeCalc, ySizeCalc;//using helper values to make xSize/ySize effectively final
				
				for(PlanePart<T> tile:rawPlane.parts){
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
			
			

			PlanePart<T>[][] grid=new PlanePart[xSize][ySize];
			
			if(isHorisontal){//convert mixed tileEntitys from list to 2D grid
				for(PlanePart<T> p:rawPlane.parts){
					T t=p.tile;
					grid[p.x=(t.x()-rawPlane.minX)][p.y=(t.z()-rawPlane.minZ)]=p;
				}
			}else if(isVerticalX){
				for(PlanePart<T> p:rawPlane.parts){
					T t=p.tile;
					grid[p.x=(t.x()-rawPlane.minX)][p.y=(t.y()-rawPlane.minY)]=p;
				}
			}else{
				for(PlanePart<T> p:rawPlane.parts){
					T t=p.tile;
					grid[p.x=(t.z()-rawPlane.minZ)][p.y=(t.y()-rawPlane.minY)]=p;
				}
			}
			if(rawPlane.parts.size()<2)return;//is 1 block or empty? Than it can't have holes, hence nothing is there to be fixed
			// Number of elements shows that the plane can be only full.
			// Discontinuing fixing!
			if(xSize*ySize==rawPlane.parts.size())return;
			
			for(int x=0;x<grid.length;x++){
				for(int y=0;y<grid[x].length;y++){
					PlanePart<T> part=grid[x][y];
					if(part==null)continue;//is a hole? No need to use that!
					T tile=part.tile;
					
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
					List<T> newPlaneBlocks=new ArrayList<>();
					
					for(int x1=minX1;x1<maxX1+1;x1++){
						for(int y1=minY1;y1<maxY1+1;y1++){
							T tile1=grid[x1][y1].tile;
							if(tile1!=null){
								newPlaneBlocks.add(tile1);
							}
						}
					}
					//did the plane expand? Than add it to said plane collection.
					if(newPlaneBlocks.size()>1){
						Plane<T> pl=new Plane(CollectionConverter.convLi(newPlaneBlocks,PlanePart.class,t->new PlanePart(t,0,0)), isVerticalX, isHorisontal, xSize, ySize,minX,minY,minZ);
						toAdd.add(pl);

						if(isHorisontal){//convert mixed tileEntitys from list to 2D grid
							for(PlanePart<T> p:pl.parts){
								T t=p.tile;
								p.x=t.x()-rawPlane.minX;
								p.y=t.z()-rawPlane.minZ;
							}
						}else if(isVerticalX){
							for(PlanePart<T> p:pl.parts){
								T t=p.tile;
								p.x=t.x()-rawPlane.minX;
								p.y=t.y()-rawPlane.minY;
							}
						}else{
							for(PlanePart<T> p:pl.parts){
								T t=p.tile;
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
		List<Plane<T>> allPlanes=new ArrayList<>();
		allPlanes.addAll(xPlanes);
		allPlanes.addAll(yPlanes);
		allPlanes.addAll(zPlanes);
		
		allPlanes.removeIf(p->p.parts.stream().noneMatch(part->part.tile==clicked));
		
		if(allPlanes.size()==1)return allPlanes.get(0);//only 1 plane? than no need to choose a plane
		else{
			int maxSize=allPlanes.stream().mapToInt(p->p.parts.size()).max().orElse(allPlanes.get(0).parts.size());
			//get all planes with biggest size
			List<Plane> biggestPlanes=allPlanes.stream().filter(plane->plane.parts.size()==maxSize).collect(Collectors.toList());
			if(biggestPlanes.size()==1)return biggestPlanes.get(0);//only 1 plane with maxSize?
			
			double smallestDistance=1000000000;
			Plane<T> closestPlane=null;
			for(Plane<T> plane:biggestPlanes){
				double distance=plane.parts.stream().mapToDouble(tile->new Vec3M(clicked.getPos()).sub(0.5F).distanceTo(tile.tile.getPos())).min().orElse(1000000000);
				if(smallestDistance>distance){
					smallestDistance=distance;
					closestPlane=plane;
				}
			}
			return closestPlane;
		}
	}
	
	
}
