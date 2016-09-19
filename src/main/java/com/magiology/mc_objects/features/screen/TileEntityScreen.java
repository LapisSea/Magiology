package com.magiology.mc_objects.features.screen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.magiology.handlers.frame_buff.InWorldFrame;
import com.magiology.mc_objects.particles.ParticleMistBubbleFactory;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.PrintUtil;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilM;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityScreen extends TileEntityM{
	
	public boolean isMultiblockBrain=true, screenDirty=true;
	
	private TileEntityScreen brain;
	
	@SideOnly(Side.CLIENT)
	public InWorldFrame screenTexture;
	
	
	public void structurize(){
		List<TileEntityScreen> blocks=explore(new ArrayList<>());
		PrintUtil.println("--------------------");
			List<TileEntityScreen> plane=buildScreen(blocks);
			for(TileEntityScreen tileEntityScreen:plane){
				for(int k=0; k<5; k++)
					ParticleMistBubbleFactory.get().spawn(new Vec3M(tileEntityScreen.getPos()).add(0.3, 0.5, 0.5), new Vec3M(RandUtil.CRD(0.005), RandUtil.CRD(0.005), RandUtil.CRD(0.005)), 3/16F, 50, 0, ColorF.WHITE);
			}
	}
	
	private List<TileEntityScreen> buildScreen(List<TileEntityScreen> blocks){
		
		Map<Integer, List<TileEntityScreen>> xBuildPlanes=new HashMap(), yBuildPlanes=new HashMap<>(), zBuildPlanes=new HashMap<>();
		blocks.forEach(t->{
			List<TileEntityScreen> plane;
			int x=t.x(), y=t.y(), z=t.z();
			
			plane=xBuildPlanes.get(x);
			if(plane==null)
				xBuildPlanes.put(x, plane=new ArrayList<>());
			plane.add(t);
			
			plane=yBuildPlanes.get(y);
			if(plane==null)
				yBuildPlanes.put(y, plane=new ArrayList<>());
			plane.add(t);
			
			plane=zBuildPlanes.get(z);
			if(plane==null)
				zBuildPlanes.put(z, plane=new ArrayList<>());
			plane.add(t);
		});
		List<List<TileEntityScreen>> xPlanes=new ArrayList(xBuildPlanes.values()), yPlanes=new ArrayList<>(yBuildPlanes.values()), zPlanes=new ArrayList<>(zBuildPlanes.values());
		
		Queue<List<TileEntityScreen>> toRemove=new ArrayDeque<>(), toAdd=new ArrayDeque<>();
		Consumer<? super List<TileEntityScreen>> fix=rawPlane->{
			if(rawPlane.size()<2)return;//is 1 block or empty? Than it can't have holes, hence nothing is there to be fixed
			
			int xSize, ySize,
				minX=Integer.MAX_VALUE, minY=Integer.MAX_VALUE, minZ=Integer.MAX_VALUE, 
				maxX=Integer.MIN_VALUE, maxY=Integer.MIN_VALUE, maxZ=Integer.MIN_VALUE;
			boolean isVerticalX, isHorisontal;
			{//braces here to dump values
				int xSizeCalc, ySizeCalc;//using helper values to make xSize/ySize effectively final
				
				for(TileEntityScreen tile:rawPlane){
					BlockPos pos=tile.getPos();
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
			
			// Number of elements shows that the plane can be only full.
			// Discontinuing fixing!
			if(xSize*ySize==rawPlane.size())return;
			

			TileEntityScreen[][] grid=new TileEntityScreen[xSize][ySize];
			
			if(isHorisontal){//convert mixed tileEntitys from list to 2D grid
				for(TileEntityScreen t:rawPlane){
					grid[t.x()-minX][t.z()-minZ]=t;
				}
			}else if(isVerticalX){
				for(TileEntityScreen t:rawPlane){
					grid[t.x()-minX][t.y()-minY]=t;
				}
			}else{
				for(TileEntityScreen t:rawPlane){
					grid[t.z()-minZ][t.y()-minY]=t;
				}
			}
			
			for(int x=0;x<grid.length;x++){
				for(int y=0;y<grid[x].length;y++){
					TileEntityScreen tile=grid[x][y];
					if(tile==null)continue;//is a hole? No need to use that!
					
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
					
					//convert bounding rectangle to useable list
					List<TileEntityScreen> newPlane=new ArrayList<>();
					
					for(int x1=minX1;x1<maxX1+1;x1++){
						for(int y1=minY1;y1<maxY1+1;y1++){
							TileEntityScreen tile1=grid[x1][y1];
							if(tile1!=null){
								newPlane.add(tile1);
							}
						}
					}
					//did the plane expand? Than add it to said plane collection. 
					if(newPlane.size()>1)toAdd.add(newPlane);
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
		List<List<TileEntityScreen>> allPlanes=new ArrayList<>();
		allPlanes.addAll(xPlanes);
		allPlanes.addAll(yPlanes);
		allPlanes.addAll(zPlanes);
		
		if(allPlanes.size()==1)return allPlanes.get(0);//only 1 plane? than no need to choose a plane
		else{
			int maxSize=allPlanes.stream().max((p1, p2)->Integer.compare(p1.size(), p2.size())).get().size();
			//get all planes with biggest size
			List<List<TileEntityScreen>> biggestPlanes=allPlanes.stream().filter(plane->plane.size()==maxSize).collect(Collectors.toList());
			
			if(biggestPlanes.size()==1)return biggestPlanes.get(0);//only 1 plane with maxSize?
			
			return null;//get the biggest plane
		}
	}
	
	private List<TileEntityScreen> explore(List<TileEntityScreen> parts){//crawl trough all connected blocks
		UtilM.getTileSides(getWorld(), new BlockPosM(getPos()), TileEntityScreen.class).forEach(t->{
			if(!parts.contains(t)){
				parts.add(t);
				t.explore(parts);
			}
		});
		return parts;
	}
	
	public boolean hasBrain(){
		return getBrain()!=null;
	}
	
	public TileEntityScreen getBrain(){
		return brain;
	}
	
}
