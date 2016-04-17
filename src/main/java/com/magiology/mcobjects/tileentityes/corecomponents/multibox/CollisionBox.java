package com.magiology.mcobjects.tileentityes.corecomponents.multibox;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class CollisionBox{
		
		public static void fixIds(MultiColisionProvider instance){
			List<CollisionBox> boxes=instance.getBoxes();
			for(int i=0;i<boxes.size();i++){
				boxes.get(i).id=i;
			}
		}
		public static CollisionBox[] genColisionBoxArray(DoubleObject<AxisAlignedBBM, EnumFacing>[] data){
			CollisionBox[] result=new CollisionBox[data.length];
			
			for(int i=0;i<data.length;i++){
				result[i]=new CollisionBox(data[i].obj1,data[i].obj2);
				result[i].id=i;
			}
			
			return result;
		}
		public static List<CollisionBox> genColisionBoxList(DoubleObject<AxisAlignedBBM, EnumFacing>[] data){
			List<CollisionBox> result=new ArrayList<>();
			
			for(int i=0;i<data.length;i++){
				CollisionBox box=new CollisionBox(data[i].obj1,data[i].obj2);
				result.add(box);
				box.id=i;
			}
			
			return result;
		}
		public final AxisAlignedBBM box;
		public final EnumFacing direction;
		
		private AxisAlignedBBM ghostBox;
		
		private int id;
		public boolean isGhost=false,canBeClickedInGhost=true;
		
		private CollisionBox(AxisAlignedBBM box, EnumFacing direction){
			
			this.direction=direction;
			this.box=box;
		}
		public int getId(){
			return id;
		}
		
		
		public boolean isInside(Vec3d hit){
			if(ghostBox==null)ghostBox=box.expand(0.0001);
			return isGhost?canBeClickedInGhost&&ghostBox.isVecInside(hit):box.isVecInside(hit);
		}

		public boolean isInside(Vec3M hit){
			if(ghostBox==null)ghostBox=box.expand(0.0001);
			return isGhost?canBeClickedInGhost&&ghostBox.isVecInside(hit):box.isVecInside(hit);
		}
		@Override
		public String toString(){
			return "CollisionBox[box="+box+",direction="+direction+",isGhost="+isGhost+",id="+id+"]";
		}
	}