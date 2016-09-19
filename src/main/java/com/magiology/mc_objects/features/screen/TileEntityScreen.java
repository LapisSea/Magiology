package com.magiology.mc_objects.features.screen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

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
		Thread t=new Thread(()->{
			try{
				List<TileEntityScreen> plane=buildScreen(blocks);
				for(TileEntityScreen tileEntityScreen:plane){
					for(int k=0; k<5; k++)
						ParticleMistBubbleFactory.get().spawn(new Vec3M(tileEntityScreen.getPos()).add(0.3, 0.5, 0.5), new Vec3M(RandUtil.CRD(0.005), RandUtil.CRD(0.005), RandUtil.CRD(0.005)), 3/16F, 50, 0, ColorF.WHITE);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		});
		t.start();
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
		Consumer<? super List<TileEntityScreen>> fix=p->{
			if(p.size()<2)
				return;
			// calculate 2D size of
			// plane---------------------------------------------------
			int xSize, ySize, minX=Integer.MAX_VALUE, maxX=Integer.MIN_VALUE, minY=Integer.MAX_VALUE, maxY=Integer.MIN_VALUE, minZ=Integer.MAX_VALUE, maxZ=Integer.MIN_VALUE;
			boolean isVerticalX, isHorisontal;
			{// braces here to dump values
				int xSi, ySi;
				
				for(TileEntityScreen t:p){
					BlockPos pos=t.getPos();
					minX=Math.min(minX, pos.getX());
					minY=Math.min(minY, pos.getY());
					minZ=Math.min(minZ, pos.getZ());
					
					maxX=Math.max(maxX, pos.getX());
					maxY=Math.max(maxY, pos.getY());
					maxZ=Math.max(maxZ, pos.getZ());
				}
				int ySiz=maxY-minY, zSiz=maxZ-minZ;
				isVerticalX=zSiz==0;
				isHorisontal=ySiz==0;
				
				if(isHorisontal){
					xSi=maxX-minX;
					ySi=zSiz;
				}else if(isVerticalX){
					xSi=maxX-minX;
					ySi=ySiz;
				}else{
					xSi=zSiz;
					ySi=ySiz;
				}
				xSize=xSi+1;
				ySize=ySi+1;
			}
			// -----------------------------------------------------------------------------
			
			// Number of elements shows that the plane can be only full.
			// Discontinuing fixing!
			if(xSize*ySize==p.size())
				return;
			
			// create
			// plane-----------------------------------------------------------------

			TileEntityScreen[][] grid=new TileEntityScreen[xSize][ySize];
			
			if(isHorisontal){
				for(TileEntityScreen t:p){
					grid[t.x()-minX][t.z()-minZ]=t;
				}
			}else if(isVerticalX){
				for(TileEntityScreen t:p){
					grid[t.x()-minX][t.y()-minY]=t;
				}
			}else{
				for(TileEntityScreen t:p){
					grid[t.z()-minZ][t.y()-minY]=t;
				}
			}
			// -----------------------------------------------------------------------------
			
			for(int x=0;x<grid.length;x++){
				for(int y=0;y<grid[x].length;y++){
					TileEntityScreen tile=grid[x][y];
					if(tile==null)continue;
					
					int minX1=x, minY1=y, maxX1=minX1, maxY1=minY1;
					boolean minX1p=true, minY1p=true;
					
					while(minX1p||minY1p){
						if(minY1p){
							if(minY1==0)minY1p=false;
							else{
								minY1--;
								for(int x1=minX1; x1<maxX1+1; x1++){
									if(grid[x1][minY1]==null){
										if(minY1p){
											minY1++;
											minY1p=false;
										}
									}
								}
							}
						}
						if(minX1p){
							if(minX1==0)minX1p=false;
							else{
								minX1--;
								for(int y1=minY1; y1<maxY1+1; y1++){
									if(grid[minX1][y1]==null){
										if(minX1p){
											minX1++;
											minX1p=false;
										}
									}
								}
							}
						}
					}
					
					List<TileEntityScreen> newPlane=new ArrayList<>();

					for(int x1=minX1;x1<maxX1+1;x1++){
						for(int y1=minY1;y1<maxY1+1;y1++){
							TileEntityScreen tile1=grid[x1][y1];
							if(tile1!=null){
								newPlane.add(tile1);
							}
						}
					}
					
					if(newPlane.size()>1)toAdd.add(newPlane);
				}
			}
			
			if(!toAdd.isEmpty())toRemove.add(p);
		};
		
		PrintUtil.println("fixing x planes");
		xPlanes.forEach(fix);
		
		xPlanes.removeAll(toRemove);
		xPlanes.addAll(toAdd);
		toRemove.clear();
		toAdd.clear();
		
		PrintUtil.println("fixing y planes");
		yPlanes.forEach(fix);
		
		yPlanes.removeAll(toRemove);
		yPlanes.addAll(toAdd);
		toRemove.clear();
		toAdd.clear();
		
		PrintUtil.println("fixing z planes");
		zPlanes.forEach(fix);
		
		zPlanes.removeAll(toRemove);
		zPlanes.addAll(toAdd);
		
		// for(List<TileEntityScreen> plane:xPlanes){
		// ColorF color=new
		// ColorF(RandUtil.CRF(0.5),RandUtil.CRF(0.5),RandUtil.CRF(0.5)+0.7,1);
		// for(TileEntityScreen tileEntityScreen:plane){
		// ParticleMistBubbleFactory.get().spawn(
		// new Vec3M(tileEntityScreen.getPos()).add(0.5F),
		// new Vec3M(),
		// 4/16F, 50, 0, color);
		// }
		// }
		// for(List<TileEntityScreen> plane:yPlanes){
		// ColorF color=new
		// ColorF(RandUtil.CRF(0.5)+0.7,RandUtil.CRF(0.5),RandUtil.CRF(0.5),1);
		// for(TileEntityScreen tileEntityScreen:plane){
		// ParticleMistBubbleFactory.get().spawn(
		// new Vec3Mw(tileEntityScreen.getPos()).add(0.5,0.7,0.5),
		// new Vec3M(),
		// 4/16F, 50, 0, color);
		// }
		// }
		// for(List<TileEntityScreen> plane:zPlanes){
		// ColorF color=new
		// ColorF(RandUtil.CRF(0.5),RandUtil.CRF(0.5)+0.7,RandUtil.CRF(0.5),1);
		// for(TileEntityScreen tileEntityScreen:plane){
		// ParticleMistBubbleFactory.get().spawn(
		// new Vec3M(tileEntityScreen.getPos()).add(0.5,0.3,0.5),
		// new Vec3M(),
		// 4/16F, 50, 0, color);
		// }
		// }
		
		List<List<TileEntityScreen>> allPlanes=new ArrayList<>();
		allPlanes.addAll(xPlanes);
		allPlanes.addAll(yPlanes);
		allPlanes.addAll(zPlanes);
		
		List<TileEntityScreen> plane;
		if(allPlanes.size()==1)
			plane=allPlanes.get(0);
		else
			plane=allPlanes.stream().max((p1, p2)->Integer.compare(p1.size(), p2.size())).get();
		return plane;
	}
	
	private List<TileEntityScreen> explore(List<TileEntityScreen> parts){
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
