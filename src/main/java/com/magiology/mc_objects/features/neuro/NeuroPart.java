package com.magiology.mc_objects.features.neuro;

import com.magiology.util.interf.ISidedConnection;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.NBTUtil;
import com.magiology.util.statics.UtilM;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface NeuroPart extends ISidedConnection{
	
	String NBT_CONTROLLER_TAG="brain";
	
	TileEntityNeuroController getController();
	
	void setController(TileEntityNeuroController controller);
	
	default boolean hasController(){
		return getController()!=null;
	}
	
	default TileEntityNeuroController getControllerSecure(){
		if(!hasController()) return null;
		TileEntityNeuroController controller=getController();
		if(!controller.isInWorld()){
			setController(null);
			return null;
		}
		return controller;
	}
	
	/**
	 * Returns a list of {@link NeuroPart}s that touch the block or it's multiblock/structure.
	 */
	default List<NeuroPart> getConnected(){
		TileEntity that=(TileEntity)this;
		return UtilM.getTileSides(that.getWorld(), new BlockPosM(that.getPos()), NeuroPart.class);
	}
	
	default List<NeuroPart> getConnectedProcessed(){
		List<NeuroPart> parts=getConnected();
		
		TileEntity that=(TileEntity)this;
		
		parts.removeIf(pRaw->{
			NeuroPart p=pRaw.getSelf();
			if(p==null) return true;
			if(!ISidedConnection.handshake(this, pRaw)) return true;
			
			TileEntityNeuroController ctrl=p.getController();
			return controllerCompatabile(ctrl);
		});
		
		return parts;
	}
	
	default void writeNeuroPartToNbt(NBTTagCompound compound){
		TileEntityNeuroController controller=getController();
		if(controller!=null) NBTUtil.setBPos(compound, NBT_CONTROLLER_TAG, controller.getPos());
	}
	
	default void readNeuroPartFromNbt(NBTTagCompound compound){
		TileEntity that=(TileEntity)this;
		if(compound.hasKey(NBT_CONTROLLER_TAG)){
			setController(NBTUtil.getBPos(compound, NBT_CONTROLLER_TAG).getTile(that.getWorld(), TileEntityNeuroController.class));
		}
	}
	
	default void onConnect(){}
	
	default void onDisconnect(){}
	
	@Override
	default boolean canConnect(ISidedConnection o){
		if(!(o instanceof TileEntity&&o instanceof NeuroPart)) return false;
		
		NeuroPart p=(NeuroPart)o;
		BlockPos thisPos=((TileEntity)this).getPos();
		
		if(!BlockPosM.isPosNextTo(thisPos, ((TileEntity)p.getSelf()).getPos())){
			if(p instanceof NeuroInterface){
				
				for(NeuroInterface tile : ((NeuroInterface<NeuroInterface>)p).getWholeInterface()){
					if(BlockPosM.isPosNextTo(thisPos, ((TileEntity)tile).getPos())) return true;
				}
			}
			return false;
		}
		
		return true;
	}
	
	default NeuroPart getSelf(){
		return this;
	}
	
	default boolean controllerCompatabile(TileEntityNeuroController otherCtrl){
		if(otherCtrl==null) return true;
		
		TileEntityNeuroController thisCtrl=getController();
		return thisCtrl==otherCtrl||thisCtrl==null;
		
	}
	
	default Collection<NeuroPart> startConnectProtocol(TileEntityNeuroController controller){
		Set<NeuroPart> added=new HashSet<>();
		connectProtocol(controller, added);
		return added;
	}
	
	default void connectProtocol(TileEntityNeuroController controller, Collection<NeuroPart> added){
		TileEntityNeuroController thisCtrl=getController();
		if(!controllerCompatabile(controller)) return;
		
		added.add(this);
		
		getConnected().forEach(pRaw->{
			NeuroPart p=pRaw.getSelf();
			if(p==null) return;
			if(!ISidedConnection.handshake(this, pRaw)) return;
			if(!added.contains(p)) p.connectProtocol(controller, added);
		});
	}
}
