package com.magiology.mc_objects.features.screen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

import org.lwjgl.util.vector.Vector3f;

import com.magiology.handlers.frame_buff.InWorldFrame;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.statics.UtilM;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

public class TileEntityScreen extends TileEntityM{
	
	public boolean isMultiblockBrain=true,screenDirty=true;
	
	private TileEntityScreen brain;
	
	@SideOnly(Side.CLIENT)
	public InWorldFrame screenTexture;
	
	
	public void structurize(){
		List<TileEntityScreen> blocks=explore(new ArrayList<>());
		buildScreen(blocks);
	}
	
	private List<TileEntityScreen> buildScreen(List<TileEntityScreen> blocks){
		
		Map<Integer, List<TileEntityScreen>> xBuildPlanes=new HashMap(),yBuildPlanes=new HashMap<>(),zBuildPlanes=new HashMap<>();
		blocks.forEach(t->{
			List<TileEntityScreen> plane;
			int x=t.x(),y=t.y(),z=t.z();
			
			plane=xBuildPlanes.get(x);
			if(plane==null)xBuildPlanes.put(x, plane=new ArrayList<>());
			plane.add(t);
			
			plane=yBuildPlanes.get(y);
			if(plane==null)yBuildPlanes.put(y, plane=new ArrayList<>());
			plane.add(t);
			
			plane=zBuildPlanes.get(z);
			if(plane==null)zBuildPlanes.put(z, plane=new ArrayList<>());
			plane.add(t);
		});
		List<List<TileEntityScreen>> xPlanes=new ArrayList(xBuildPlanes.values()),yPlanes=new ArrayList<>(yBuildPlanes.values()),zPlanes=new ArrayList<>(zBuildPlanes.values());
		
		Queue<List<TileEntityScreen>> toRemove=new ArrayDeque<>();
		Consumer<? super List<TileEntityScreen>> fix=p->{
			if(p.size()>1){
				List<BlockPosM> positions=new ArrayList<>();
				int
					minX=Integer.MIN_VALUE,maxX=Integer.MAX_VALUE,
					minY=0,				   maxY=Integer.MAX_VALUE,
					minZ=Integer.MIN_VALUE,maxZ=Integer.MAX_VALUE;
				
				//collect positions for future reference
				for(TileEntityScreen t:p){
					BlockPosM pos=t.getPos();
					positions.add(pos);
					
					if(minX>pos.getX())minX=pos.getX();
					if(maxX>pos.getX())maxX=pos.getX();
					
					if(minY>pos.getY())minY=pos.getY();
					if(maxY>pos.getY())maxY=pos.getY();
					
					if(maxZ>pos.getZ())maxZ=pos.getZ();
					if(maxZ>pos.getZ())maxZ=pos.getZ();
				}
				
				int xSize=maxX-minX,ySize=maxY-minY,zSize=maxZ-minZ;
				EnumFacing planeFace=null;
				if(xSize==0){
					planeFace=EnumFacing.EAST;
					xSize=1;
					maxX++;
				}else if(zSize==0){
					planeFace=EnumFacing.SOUTH;
					zSize=1;
					maxZ++;
				}else if(ySize==0){
					planeFace=EnumFacing.UP;
					ySize=1;
					maxY++;
				}
				List<EnumFacing> sides=Arrays.asList(EnumFacing.values());
				sides.remove(planeFace);
				sides.remove(planeFace.getOpposite());
				
				//is there any defects with the rectangle? length has to be equal to multiplication to be completely filled
				if(positions.size()!=xSize*ySize*zSize){
					List<BlockPosM> holes=new ArrayList<>();
					for(int x=minX;x<maxX;x++){
						for(int y=minX;y<maxX;y++){
							for(int z=minX;z<maxX;z++){
								BlockPosM pos=new BlockPosM(x,y,z);
								if(!positions.contains(pos))holes.add(pos);
							}	
						}
					}
					List<List<TileEntityScreen>> solutions=new ArrayList<>();
					holes.forEach(h->{
						sides.forEach(side->{
							BlockPosM off=h.offset(side);
							if(!holes.contains(off)){
								Vec3i vec=side.getDirectionVec();
								boolean
									xNotMore=vec.getX()<0,xNotLess=vec.getX()>0,
									yNotMore=vec.getY()<0,yNotLess=vec.getY()>0,
									zNotMore=vec.getZ()<0,zNotLess=vec.getZ()>0;
								Vector3f 
									min=new Vector3f(Integer.MIN_VALUE, 0, Integer.MIN_VALUE),
									max=new Vector3f(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
									
								holes.stream().forEach(hole->{
									BlockPosM diff=hole.subtract(off);
									if(diff.getX()>=0&&xNotMore)return;
									if(diff.getX()<=0&&xNotLess)return;
									
									if(diff.getY()>=0&&yNotMore)return;
									if(diff.getY()<=0&&yNotLess)return;
									
									if(diff.getZ()>=0&&zNotMore)return;
									if(diff.getZ()<=0&&zNotLess)return;
									
									min.x=Math.min(hole.getX(), min.x);
									min.y=Math.min(hole.getY(), min.y);
									min.z=Math.min(hole.getZ(), min.z);
									max.x=Math.max(hole.getX(), max.x);
									max.y=Math.max(hole.getY(), max.y);
									max.z=Math.max(hole.getZ(), max.z);
								});
								List<TileEntityScreen> cutOffPlane=new ArrayList<>();
								p.forEach(t->{
									BlockPosM pos=t.getPos();
									if(
										pos.getX()>=min.x&&pos.getX()<=max.x&&
										pos.getY()>=min.y&&pos.getY()<=max.y&&
										pos.getZ()>=min.z&&pos.getZ()<=max.z
										){
										cutOffPlane.add(t);
									}
								});
								
								if(!cutOffPlane.isEmpty())solutions.add(cutOffPlane);
							}
						});
					});
					if(!solutions.isEmpty())toRemove.add(p);
				}
			}
		};
		
		xPlanes.forEach(fix);
		
		xPlanes.removeAll(toRemove);
		toRemove.clear();
		
		yPlanes.forEach(fix);
		yPlanes.removeAll(toRemove);
		
		toRemove.clear();
		
		zPlanes.forEach(fix);
		zPlanes.removeAll(toRemove);
		
		List<List<TileEntityScreen>> allPlanes=new ArrayList<>(xPlanes);
		allPlanes.addAll(yPlanes);
		allPlanes.addAll(zPlanes);
		
		List<TileEntityScreen> plane;
		if(allPlanes.size()==1)plane=allPlanes.get(0);
		else plane=allPlanes.stream().max((p1, p2)->Integer.compare(p1.size(), p2.size())).get();
		return plane;
	}
	
	private List<TileEntityScreen> explore(List<TileEntityScreen> parts){
		UtilM.getTileSides(getWorld(), getPos(),TileEntityScreen.class).forEach(t->{
			parts.add(t);
			t.explore(parts);
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
