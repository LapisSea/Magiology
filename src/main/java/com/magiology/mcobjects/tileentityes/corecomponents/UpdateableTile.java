package com.magiology.mcobjects.tileentityes.corecomponents;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.connection.IConnectionProvider;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.m_extension.TileEntityM;
import com.magiology.util.utilobjects.vectors.Vec2i;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface UpdateableTile extends IConnectionProvider{
	
	public class UpdateablePipeHandler{
		public static <T extends TileEntityM&UpdateableTile> void onConnectionUpdate(T tile){
			if(!tile.hasWorldObj()||tile.getLastUpdateTime()==tile.getTime())tile.connectionUpdateWaiting();
		}
		
		public static<T extends TileEntity&UpdateableTile,arrayType> void setConnections(arrayType[] array,arrayType trueValue,arrayType falseValue,T tile){
			List<Class> excluded=new ArrayList<Class>(),included=new ArrayList<Class>();
			tile.getValidTileEntitys(included, excluded);
			TileEntity[] tiles=SideUtil.getTilesOnSides(tile);
			for(int i=0;i<6;i++){
				if(tiles[i]!=null){
					TileEntity possibleConector=tiles[i];
					boolean pass=false;
					
					for(int j=0;j<excluded.size();j++){
						if(UtilM.instanceOf(possibleConector,excluded.get(j)))pass=tile.getExtraClassCheck(excluded.get(j), possibleConector,array, j);
						if(pass)j=excluded.size();
					}
					for(int j=0;j<included.size();j++){
						if(UtilM.instanceOf(possibleConector,included.get(j)))pass=tile.getExtraClassCheck(included.get(j), possibleConector,array, j);
						if(pass)j=included.size();
					}
					if(pass)array[i]=trueValue;
					else	array[i]=falseValue;
				}else	   array[i]=falseValue;
			}
		}
		
		public static<T extends TileEntity&UpdateableTile> void setConnections(EnumFacing[] array,T tile){
			setConnections(array, EnumFacing.DOWN, null, tile);
			for(int i=0;i<6;i++){
				if(array[i]!=null)switch(i){
				case 0:array[i]=EnumFacing.UP;break;
				case 1:array[i]=EnumFacing.DOWN;break;
				case 2:array[i]=EnumFacing.NORTH;break;
				case 3:array[i]=EnumFacing.EAST;break;
				case 4:array[i]=EnumFacing.SOUTH;break;
				case 5:array[i]=EnumFacing.WEST;break;
				}
			}
		}
		public static void updatein3by3(World world,BlockPos pos){
			if(U.isNull(world,pos))return;
			for(int x1=-1;x1<2;x1++)
				for(int y1=-1;y1<2;y1++)
					for(int z1=-1;z1<2;z1++){
						BlockPos Pos=pos.add(x1,y1,z1);
						if(!Pos.equals(pos))updatePipe(world, Pos);
					}
		}

		public static void updatePipe(World world, BlockPos pos){
			if(U.isNull(world,pos))return;
			TileEntity tile=world.getTileEntity(pos);
			if(tile instanceof UpdateableTile){
				if(tile.hasWorldObj()){
					((UpdateableTile)tile).updateConnections();
				}else ((UpdateableTile)tile).connectionUpdateWaiting();
			}
		}

		private UpdateablePipeHandler(){}
	}
	public static final Vec2i[] SIDE_TO_CONNECTION_TABLE={
			new Vec2i(3,5),
			new Vec2i(4,3),
			new Vec2i(2,2),
			new Vec2i(5,4),
			new Vec2i(1,0),
			new Vec2i(0,1)
	};
	
	public void connectionUpdateWaiting();
	public <T extends TileEntity> boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array,int side);
	
	public long getLastUpdateTime();
	public void getValidTileEntitys(List<Class> included,List<Class> excluded);
	
	public boolean isUpdateWaiting();
	public void updateConnections();
	
	public void updateWaitingUpdate();
}
