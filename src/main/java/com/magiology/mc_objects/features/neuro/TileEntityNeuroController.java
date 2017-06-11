package com.magiology.mc_objects.features.neuro;

import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.util.interf.IBlockBreakListener;
import com.magiology.util.m_extensions.TileEntityMTickable;
import com.magiology.util.objs.NBTUtil;
import com.magiology.util.objs.ObjectHolder;
import com.magiology.util.statics.UtilM;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntityNeuroController extends TileEntityMTickable implements NeuroPart, IBlockBreakListener{
	
	private                List<NeuroPart> parts                    =new ArrayList<>();
	protected static final String          NBT_PARTS_TAG            ="parts";
	private                boolean         requestedConnectedRefresh=false;
	
	public TileEntityNeuroController(){
		
	}
	
	public void requestConnectedRefresh(){
		requestedConnectedRefresh=true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
	}
	
	@Override
	protected void readFromNbtWithWorld(NBTTagCompound compound){
		NBTTagCompound partsTags=compound.getCompoundTag(NBT_PARTS_TAG);
		List<NeuroPart> newParts=new ArrayList<>();
		for(int i=0; i<partsTags.getSize(); i++){
			NeuroPart p=NBTUtil.getBPos(partsTags, ""+i).getTile(world, NeuroPart.class);
			if(p!=null) newParts.add(p);
		}
		
		applyNewParts(newParts);
		//		refreshConnected();
	}
	
	@Override
	protected boolean nbtUsingWorld(NBTTagCompound compound){
		return true;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		if(parts.size()>1){
			NBTTagCompound partsTags=new NBTTagCompound();
			
			for(int i=0; i<parts.size(); i++){
				NBTUtil.setBPos(partsTags, ""+i, (TileEntity)parts.get(i));
			}
			compound.setTag(NBT_PARTS_TAG, partsTags);
		}
		return super.writeToNBT(compound);
	}
	
	@Override
	public void update(){
		if(requestedConnectedRefresh&&!isRemote()) refreshConnected();
		if(isRemote()){
			//			for(NeuroPart p:parts){
			//				if(RandUtil.RB(0.4)){
			//					ParticleMistBubbleFactory.get().spawn(
			//							new Vec3M(((TileEntity)p).getPos()).add(0.5, 0.5, 0.5),
			//							new AngularVec3(RandUtil.RF()*360, RandUtil.RF()*360, 0.01F).toVec3M(),
			//							0.5F, 40, 0, ColorF.randomRGB());
			//				}
			//			}
		}else{
			if(UtilM.peridOf(this, 100)){
				refreshConnected();
			}
		}
		
	}
	
	private void refreshConnected(){
		//		LogUtil.printStackTrace();
		//		LogUtil.println(startConnectProtocol(this));
		requestedConnectedRefresh=false;
		applyNewParts(startConnectProtocol(this));
	}
	
	private void applyNewParts(Collection<NeuroPart> newParts){
		//		LogUtil.println(worldTime(),newParts);
		ObjectHolder<Boolean> changed=new ObjectHolder<>(false);
		parts.stream().filter(p->!newParts.contains(p)).collect(Collectors.toList()).forEach(p->{
			p.onDisconnect();
			p.setController(null);
			parts.remove(p);
			if(!changed.getVar()) changed.setVar(true);
		});
		newParts.stream().filter(p->!parts.contains(p)).collect(Collectors.toList()).forEach(p->{
			parts.add(p);
			p.setController(this);
			p.onConnect();
			if(!changed.getVar()) changed.setVar(true);
		});
		
		if(changed.getVar()) UpdateTileNBTPacket.markForSync(this);
	}
	
	@Override
	public TileEntityNeuroController getController(){
		return this;
	}
	
	@Override
	@Deprecated
	public void setController(TileEntityNeuroController controller){}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
		return oldState.getBlock()!=newSate.getBlock();
	}
	
	@Override
	public void onBroken(World world, BlockPos pos, IBlockState state){
		applyNewParts(new ArrayList<>());
	}
	
	public List<NeuroPart> getParts(){
		return parts;
	}
}
