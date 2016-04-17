package com.magiology.mcobjects.tileentityes.hologram;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.SavableData;
import com.magiology.api.SavableData.SavableDataHandler;
import com.magiology.api.network.interfaces.registration.InterfaceBinder;
import com.magiology.forgepowered.packets.packets.ClickHologramPacket;
import com.magiology.mcobjects.effect.EntityFacedFX;
import com.magiology.mcobjects.effect.EntityMovingParticleFX;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.m_extension.TileEntityM;
import com.magiology.util.utilobjects.vectors.Plane;
import com.magiology.util.utilobjects.vectors.RayDeprecated;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class TileEntityHologramProjector extends TileEntityM implements ITickable{
	
	public static List<TileEntityHologramProjector> hologramProjectors=new ArrayList<>();
	public static boolean invokeRayTrace(PlayerInteractEvent event,EntityPlayer player){
		Object[][] rayTraceResult=TileEntityHologramProjector.rayTraceHolograms(player, 7);
		if(rayTraceResult[0].length>0){
			boolean hologramCloserThanHit=false,miss=false;
			if(UtilM.isRemote(player)){
				float distance=0;
				int id=0;
				for(int i=0;i<rayTraceResult[0].length;i++){
					float distanceTo=(float)player.getLook(1).distanceTo(((Vec3M)rayTraceResult[0][i]).conv());
					if(distance<distanceTo){
						id=i;
						distance=distanceTo;
					}
				}
				Vec3M hit=(Vec3M)rayTraceResult[0][id];
				TileEntityHologramProjector tile=(TileEntityHologramProjector)rayTraceResult[1][id];
				RayTraceResult target=player.rayTrace(4, 0);
				
				hologramCloserThanHit=hit.conv().distanceTo(player.getLook(0))>target.hitVec.distanceTo(player.getLook(0));
				miss=target.typeOfHit==MovingObjectType.MISS;
				if(miss||!hologramCloserThanHit){
					tile.onPressed(player);
				}
			}
			if(miss||hologramCloserThanHit){
				event.setCanceled(true);
				return true;
			}
		}
		return false;
	}
	public static Object[][] rayTraceHolograms(EntityPlayer player,float length){
		Object[][] result={{},{}};
		try{
			Vec3M Vec3M=UtilM.getEyePosition(player);
			Vec3M vec31=com.magiology.util.utilobjects.vectors.Vec3M.conv(player.getLook(PartialTicksUtil.partialTicks));
			Vec3M vec32=Vec3M.add(vec31.x * length, vec31.y * length, vec31.z * length);
			
			RayDeprecated ray=new RayDeprecated(Vec3M, vec32);
			for(int a=0;a<hologramProjectors.size();a++){
				TileEntityHologramProjector tile=null;
				TileEntity test=player.worldObj.getTileEntity(hologramProjectors.get(a).getPos());
				if(test instanceof TileEntityHologramProjector)tile=(TileEntityHologramProjector)test;
				if(tile!=null){
					Vec3M hit=new Vec3M(0, 0, 0);
					Vec3M
						min=tile.main.getPoint(false,false,false),
						max=tile.main.getPoint(true,true,true);

					if(UtilM.intersectLinePlane(ray, new Plane(
							new Vec3M(
									tile.x()+min.x+tile.offset.x,
									tile.y()+min.y+tile.offset.y,
									tile.z()+min.z+0.5),
							new Vec3M(
									tile.x()+max.x+tile.offset.x,
									tile.y()+min.y+tile.offset.y,
									tile.z()+min.z+0.5),
							new Vec3M(
									tile.x()+max.x+tile.offset.x,
									tile.y()+max.y+tile.offset.y,
									tile.z()+min.z+0.5)
							), hit)){
						result[0]=ArrayUtils.add(result[0], hit);
						result[1]=ArrayUtils.add(result[1], tile);
					}
				}else hologramProjectors.remove(a);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public boolean[] highlights=new boolean[4];
	public final ArrayList<HoloObject> holoObjects=new ArrayList<>();
	public HoloObject lastPartClicked;
	public CubeModel main;
	public Vec3M mainColor=new Vec3M(0.05,0.5,0.8);
	public Point point=new Point();
	
	public HoloObject selectedObj;
	
	public Vector2f size,offset;
	public TileEntityHologramProjector(){
		size=new Vector2f(11,6);
		offset=new Vector2f(-5, 1+UtilM.p*1.45F);
		main=new CubeModel(0,0,-UtilM.p/2, size.x,size.y,UtilM.p/2);
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		Vec3M
			p1=main.getPoint(false,false,false).add(offset.x, offset.y, 0),
			p2=main.getPoint(true,true,true).add(offset.x, offset.y, 0);
		return super.getRenderBoundingBox().union(new AxisAlignedBB(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z).addCoord(x(), y(), z()));
	}
	public void onPressed(EntityPlayer player){
		selectedObj=null;
		if(UtilM.isRemote(player))for(int a=0;a<360;a+=30){
			double[] b=MathUtil.circleXZ(a+RandUtil.CRF(16));
			b[0]*=0.06;
			b[1]*=0.06;
			EntityFacedFX part=new EntityFacedFX(worldObj, size.x+offset.x+point.pointedPos.x+x(), size.y+offset.y+point.pointedPos.y+y(), point.pointedPos.z+z()+0.5,
					b[0], b[1], 0, 200, 0.8, 0, mainColor.x, mainColor.y, mainColor.z, 0.1);
			UtilM.spawnEntityFX(part);
			part.rotation.x=180;
		}
		
		boolean changed=false;
		for(HoloObject ro:holoObjects){
			if(ro.getClass()!=Field.class||highlights[2]){
				ro.checkHighlight();
				if(ro.isHighlighted){
					if(!changed){
						changed=true;
						lastPartClicked=ro;
					}
					ro.handleGuiAndMovment(player);
					ro.onPressed(player);
				}else if(ro.moveMode){
					ro.handleGuiAndMovment(player);
					ro.onPressed(player);
				}
			}
		}
		if(!changed)lastPartClicked=null;
		if(UtilM.isRemote(player))UtilM.sendMessage(new ClickHologramPacket(point.pointedPos,pos));
		else{
			if(!worldObj.playerEntities.isEmpty()&&worldObj.playerEntities.get(0)instanceof EntityPlayerMP){
				(worldObj.playerEntities).forEach(player0->((EntityPlayerMP)player0).playerNetServerHandler.sendPacket(getDescriptionPacket()));
			}
		}
	}
	
	
	
	
	
	
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		InterfaceBinder.readInterfaceFromNBT(this, NBT);
		int dataSize=NBT.getInteger("DS");
		List<SavableData> data=SavableDataHandler.loadDataFromNBT(NBT, "ID",false);
		for(int i=0;i<dataSize;i++){
			HoloObject newHolo=(HoloObject)data.get(i);
			holoObjects.removeIf(existing->existing.id==newHolo.id);
			holoObjects.add(newHolo);
		}
		for(int i=0;i<3;i++) highlights[i]=NBT.getBoolean("h"+i);
	}

	@Override
	public void update(){
		if(UtilM.isRemote(this)){
			UtilM.spawnEntityFX(new EntityMovingParticleFX(worldObj,
					x()+RandUtil.RF(), y()+U.p*11, z()+RandUtil.RF(), x()+offset.x+size.x*RandUtil.RF(), y()+offset.y, z()+0.5, 200, mainColor.x,mainColor.y,mainColor.z,0.1));
			switch (RandUtil.RI(3)){
			case 0:{
				UtilM.spawnEntityFX(new EntityMovingParticleFX(worldObj,
						x()+offset.x+size.x*RandUtil.RF(), y()+offset.y+size.y, z()+0.5, x()+offset.x+size.x*RandUtil.RF(), y()+offset.y+size.y, z()+0.5, 200, mainColor.x,mainColor.y,mainColor.z,0.1));
			}break;
			case 1:{
				UtilM.spawnEntityFX(new EntityMovingParticleFX(worldObj,
						x()+offset.x, y()+offset.y+size.y*RandUtil.RF(), z()+0.5, x()+offset.x, y()+offset.y+size.y*RandUtil.RF(), z()+0.5, 200, mainColor.x,mainColor.y,mainColor.z,0.1));
			}break;
			case 2:{
				UtilM.spawnEntityFX(new EntityMovingParticleFX(worldObj,
						x()+offset.x+size.x, y()+offset.y+size.y*RandUtil.RF(), z()+0.5, x()+offset.x+size.x, y()+offset.y+size.y*RandUtil.RF(), z()+0.5, 200, mainColor.x,mainColor.y,mainColor.z,0.1));
			}break;
			}
		}
		boolean contains=false;
		for(TileEntityHologramProjector a:hologramProjectors){
			if(a==this){
				contains=true;
				break;
			}
		}
		if(!contains)hologramProjectors.add(this);
		for(int i=0;i<holoObjects.size();i++){
			HoloObject ho=holoObjects.get(i);
			if(!ho.isDead){
				ho.host=this;
				ho.fixPos();
				ho.update();
			}else holoObjects.remove(i);
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		InterfaceBinder.writeInterfaceToNBT(this, NBT);
		
		
		List<SavableData> savableData=holoObjects.stream().filter(obj->obj!=null).collect(Collectors.toList());

		int dataSize=savableData.size();
		NBT.setInteger("DS", dataSize);
		SavableDataHandler.saveDataToNBT(savableData, NBT, "ID");
		for(int i=0;i<3;i++)NBT.setBoolean("h"+i, highlights[i]);
	}
}
