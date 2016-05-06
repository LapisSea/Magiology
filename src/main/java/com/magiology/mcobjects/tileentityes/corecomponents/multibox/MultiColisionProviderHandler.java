package com.magiology.mcobjects.tileentityes.corecomponents.multibox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.magiology.mcobjects.blocks.MultiColisionProviderBlock;
import com.magiology.util.utilclasses.CollectionConverter;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class MultiColisionProviderHandler{
		public static AxisAlignedBBM combine(List<CollisionBox> boxes){
			AxisAlignedBBM result=boxes.get(0).box;
			for(CollisionBox box:boxes)result=result.combine(box.box);
			return result.union(AxisAlignedBBM.fullBlock());
		}
		
		public static AxisAlignedBBM combineNormal(MultiColisionProvider parent){
			return combine(parent.getBoxes());
		}
		public static List<CollisionBox> getBoxes(final MultiColisionProvider parent){
			return parent.getBoxes().stream().filter(box->!box.isGhost).collect(Collectors.toList());
		}
		public static List<CollisionBox> getBoxesOnSide(final MultiColisionProvider parent, final EnumFacing direction){
			return parent.getBoxes().stream().filter(box->!box.isGhost&&box.direction==direction).collect(Collectors.toList());
		}
		public static List<CollisionBox> getBoxesOnSide(final MultiColisionProvider parent, final int direction){
			return getBoxesOnSide(parent,EnumFacing.getFront(direction));
		}
		public static List<CollisionBox> getGhostBoxes(final MultiColisionProvider parent){
			return parent.getBoxes().stream().filter(box->box.isGhost).collect(Collectors.toList());
		}
		public static List<CollisionBox> getGhostBoxesOnSide(final MultiColisionProvider parent, final EnumFacing direction){
			return parent.getBoxes().stream().filter(box->box.isGhost&&box.direction==direction).collect(Collectors.toList());
		}
		public static <tile extends TileEntity&MultiColisionProvider> RayTraceResult handleRayTracing(final tile tile, final Vec3d startVec, final Vec3d endVec){
			List<CollisionBox> normal,ghost;
			{
				DoubleObject<List<CollisionBox>,List<CollisionBox>> split=splitBoxesBoxesByGhost(tile);
				normal=split.obj1;
				ghost=split.obj2;
			}
			MultiColisionProviderBlock block=(MultiColisionProviderBlock)UtilM.getBlock(tile.getWorld(),tile.getPos());
			
			List<AxisAlignedBBM> boxes=CollectionConverter.convLi(normal,AxisAlignedBBM.class,box->box.box);
			
			RayTraceResult hits[]=new RayTraceResult[boxes.size()];
			
			for(int i=0,l=boxes.size();i<l;i++){
				AxisAlignedBBM box=boxes.get(i);
				block.setBlockBounds((float)box.minX,(float)box.minY,(float)box.minZ,(float)box.maxX,(float)box.maxY,(float)box.maxZ);
				hits[i]=block.superCollisionRayTrace(tile.getWorld(), tile.getPos(), startVec, endVec);
			}
			block.setBlockBounds(combineNormal(tile));
			
			int    closestID=-1;
			double closestDistance=Double.MAX_VALUE;
			
			for(int i=0;i<hits.length;i++){
				RayTraceResult newVec=hits[i];
				if(newVec!=null&&newVec.hitVec!=null){
					double newDistance=startVec.distanceTo(newVec.hitVec);
					if(newDistance<closestDistance){
						closestDistance=newDistance;
						closestID=i;
					}
				}
			}
			
			if(closestID==-1){
				tile.setPointedBoxID(-1);
				tile.detectChanges();
				tile.sendChanges();
				return null;
			}
			
			RayTraceResult hit=hits[closestID];
			
			tile.setPointedBoxID(normal.get(closestID).getId());
			tile.setGhostHits(
				CollectionConverter.convLi(
					ghost.stream().filter(box->box.canBeClickedInGhost&&box.isInside(Vec3M.conv(hit.hitVec).sub(tile.getPos()))).collect(Collectors.toList()),
					Integer.class,
					(box)->box.getId()
				)
			);
			tile.detectChanges();
			tile.sendChanges();
			return hit;
		}
		
		public static List<CollisionBox>[] sortBoxesBySide(final MultiColisionProvider parent){
			List<CollisionBox>[] result=new List[6];
			for(int i=0;i<result.length;i++)result[i]=new ArrayList<CollisionBox>();
			for(CollisionBox box:parent.getBoxes())if(box.direction!=null)result[box.direction.getIndex()].add(box);
			return result;
		}
		/**
		 * @param parent
		 * @param side
		 * @return list1=normal boxes, list2=ghost boxes
		 */
		public static DoubleObject<List<CollisionBox>,List<CollisionBox>> splitBoxesBoxesByGhost(MultiColisionProvider parent){
			List<CollisionBox> normal=new ArrayList<>(),ghost=new ArrayList<>();
			parent.getBoxes().forEach(box->{
				if(box.isGhost)ghost.add(box);
				else normal.add(box);
			});
			return new DoubleObject<List<CollisionBox>,List<CollisionBox>>(normal,ghost);
		}
		
		private MultiColisionProviderHandler(){}
		
	}