package com.magiology.mc_objects.features.screen;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.magiology.handlers.frame_buff.InWorldFrame;
import com.magiology.util.interf.IBlockBreakListener;
import com.magiology.util.m_extensions.*;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.*;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class TileEntityScreen extends TileEntityM implements IBlockBreakListener{
	
	public boolean isMultiblockBrain=true, screenDirty=true;
	public int xScreenOff,yScreenOff;
	public Plane screen;
	@SideOnly(Side.CLIENT)
	public InWorldFrame screenTexture;
	
	private TileEntityScreen brain;
	
	
	public class Plane{
		public List<TileEntityScreen> blocks;
		public boolean isVerticalX, isHorisontal;
		public int xSize, ySize, minX,minY,minZ;
		public Plane(List<TileEntityScreen> blocks){
			this.blocks=blocks;
		}
		public Plane(List<TileEntityScreen> blocks, boolean isVerticalX, boolean isHorisontal, int xSize, int ySize, int minX, int minY, int minZ){
			this.blocks=blocks;
			this.isVerticalX=isVerticalX;
			this.isHorisontal=isHorisontal;
			this.xSize=xSize;
			this.ySize=ySize;
			this.minX=minX;
			this.minY=minY;
			this.minZ=minZ;
		}
		
	}
	
	public void structurize(Vec3M clickedPos){
		List<TileEntityScreen> blocks=explore(new ArrayList<>());
		TileEntityScreen brain=this;
		while(blocks.isEmpty()){
			try{
				brain.screen=brain.buildScreen(blocks,clickedPos);
				for(TileEntityScreen tile:brain.screen.blocks){
//					ParticleMistBubbleFactory.get().spawn(new Vec3M(tile.pos).add(0.5F), new Vec3M(), 0.5F, 10, 0, ColorF.RED);
					tile.brain=brain;
					if(tile.screenTexture!=null)tile.screenTexture=null;
					BlockPos off=tile.getPos().subtract(new BlockPos(brain.screen.minX,brain.screen.minY,brain.screen.minZ));
					PrintUtil.println(brain.screen.isVerticalX,brain.screen.isHorisontal);
					tile.xScreenOff=brain.screen.isVerticalX? off.getX():off.getZ();
					tile.yScreenOff=brain.screen.isHorisontal?off.getZ():off.getY();
				}
				blocks.removeAll(brain.screen.blocks);
				if(!blocks.isEmpty())brain=blocks.get(0);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onBroken(World world, BlockPos pos, IBlockState state){
		if(isBrain())breakScreen();
		else if(hasBrain()){
			getBrain().breakScreen();
		}
	}
	private void breakScreen(){
		screen.blocks.forEach(tile->{
			tile.screenTexture=null;
			tile.brain=null;
		});
		screen=null;
	}
	
	private Plane buildScreen(List<TileEntityScreen> blocks,Vec3M clickedPos){
		
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
				
				for(TileEntityScreen tile:rawPlane.blocks){
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
			rawPlane.isHorisontal=isHorisontal;
			rawPlane.isVerticalX=isVerticalX;
			rawPlane.xSize=xSize;
			rawPlane.ySize=ySize;
			rawPlane.minX=minX;
			rawPlane.minY=minY;
			rawPlane.minZ=minZ;
			
			if(rawPlane.blocks.size()<2)return;//is 1 block or empty? Than it can't have holes, hence nothing is there to be fixed
			// Number of elements shows that the plane can be only full.
			// Discontinuing fixing!
			if(xSize*ySize==rawPlane.blocks.size())return;
			

			TileEntityScreen[][] grid=new TileEntityScreen[xSize][ySize];
			
			if(isHorisontal){//convert mixed tileEntitys from list to 2D grid
				for(TileEntityScreen t:rawPlane.blocks){
					grid[t.x()-rawPlane.minX][t.z()-rawPlane.minZ]=t;
				}
			}else if(isVerticalX){
				for(TileEntityScreen t:rawPlane.blocks){
					grid[t.x()-rawPlane.minX][t.y()-rawPlane.minY]=t;
				}
			}else{
				for(TileEntityScreen t:rawPlane.blocks){
					grid[t.z()-rawPlane.minZ][t.y()-rawPlane.minY]=t;
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
					
					//convert bounding rectangle to usable list
					List<TileEntityScreen> newPlaneBlocks=new ArrayList<>();
					
					for(int x1=minX1;x1<maxX1+1;x1++){
						for(int y1=minY1;y1<maxY1+1;y1++){
							TileEntityScreen tile1=grid[x1][y1];
							if(tile1!=null){
								newPlaneBlocks.add(tile1);
							}
						}
					}
					//did the plane expand? Than add it to said plane collection. 
					if(newPlaneBlocks.size()>1)toAdd.add(new Plane(newPlaneBlocks, isVerticalX, isHorisontal, xSize, ySize,minX,minY,minZ));
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
		
		if(allPlanes.size()==1)return allPlanes.get(0);//only 1 plane? than no need to choose a plane
		else{
			int maxSize=allPlanes.stream().max((p1, p2)->Integer.compare(p1.blocks.size(), p2.blocks.size())).get().blocks.size();
			//get all planes with biggest size
			List<Plane> biggestPlanes=allPlanes.stream().filter(plane->plane.blocks.size()==maxSize).collect(Collectors.toList());
			if(biggestPlanes.size()==1)return biggestPlanes.get(0);//only 1 plane with maxSize?
			
			double smallestDistance=1000000000;
			Plane closestPlane=null;
			for(Plane plane:biggestPlanes){
				double distance=plane.blocks.stream().mapToDouble(tile->clickedPos.sub(0.5F).distanceTo(tile.getPos())).min().getAsDouble();
				if(smallestDistance>distance){
					smallestDistance=distance;
					closestPlane=plane;
				}
			}
			return closestPlane;
		}
	}
	
	private List<TileEntityScreen> explore(List<TileEntityScreen> parts){//crawl trough all connected blocks
		UtilM.getTileSides(getWorld(), new BlockPosM(getPos()), TileEntityScreen.class).stream().filter(t->!parts.contains(t)&&!t.hasBrain()).forEach(t->{
			parts.add(t);
			t.explore(parts);
		});
		return parts;
	}

	public boolean isBrain(){
		return getBrain()==this;
	}
	public boolean hasBrain(){
		return getBrain()!=null;
	}
	
	public TileEntityScreen getBrain(){
		return brain;
	}
	public EnumFacing getRotation(IBlockState state){
		return getState().getValue(BlockScreen.ROT);
	}
}
