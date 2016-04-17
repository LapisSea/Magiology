package com.magiology.mcobjects.tileentityes.corecomponents.multibox;

import java.util.List;

import com.magiology.api.connection.IConnection;
import com.magiology.forgepowered.packets.packets.NotifyPointedBoxChangePacket;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class DefaultMultiColisionProvider{
		private static final float p=1F/16;
		
		public static <instance extends TileEntity&MultiColisionProvider>void detectChanges(instance instance){
			if(!U.isRemote(instance))return;
			if(instance.getPointedBoxID()==instance.getPrevPointedBoxID()&&instance.getGhostHits().equals(instance.getPrevGhostHits()))return;
			instance.setChangesDetected(true);
		}
		
		public static List<CollisionBox> getStandardBoxes(){
			return CollisionBox.genColisionBoxList(new DoubleObject[]{
					new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(0,   p*6, p*6, p*6,  p*10, p*10),EnumFacing.getFront(5)),
					new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, 0,   p*6, p*10, p*6,  p*10),EnumFacing.getFront(1)),
					new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, p*6, 0,   p*10, p*10, p*6 ),EnumFacing.getFront(2)),
					new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*10,p*6, p*6, 1,    p*10, p*10),EnumFacing.getFront(3)),
					new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, p*10,p*6, p*10, 1,    p*10),EnumFacing.getFront(0)),
					new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, p*6, p*10,p*10, p*10, 1   ),EnumFacing.getFront(4)),
					new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, p*6, p*6, p*10, p*10, p*10),null)
			});
		}
		public static <instance extends TileEntity&MultiColisionProvider>void sendChanges(instance instance){
			instance.setChangesDetected(false);
			UtilM.sendMessage(new NotifyPointedBoxChangePacket(instance));
		}

		public static <instance extends UpdateableTile&MultiColisionProvider>void setStandardConnectionToBox(instance instance){
			IConnection[] connections=instance.getConnections();
			List<CollisionBox>[] sides=MultiColisionProviderHandler.sortBoxesBySide(instance);
			for(int i=0;i<sides.length;i++){
				final int j=i;
				sides[j].forEach(box->box.isGhost=!connections[j].getMain());
			}
		}
		
		private DefaultMultiColisionProvider(){}
		
	}