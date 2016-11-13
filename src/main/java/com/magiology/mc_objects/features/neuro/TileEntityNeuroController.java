package com.magiology.mc_objects.features.neuro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.magiology.forge.events.TickEvents;
import com.magiology.mc_objects.particles.ParticleBubbleFactory;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.UtilM;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;


public class TileEntityNeuroController extends TileEntityNeuroBase{
	
	private List<NeuroPart> parts=new ArrayList<>();
	protected static final String NBT_PARTS_TAG="parts";
	
	public TileEntityNeuroController(){
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		Runnable r=()->{
			NBTTagCompound partsTags=compound.getCompoundTag(NBT_PARTS_TAG);
			for(int i=0;i<partsTags.getSize();i++){
				NeuroPart p=BlockPosM.fromLong(partsTags.getLong(""+i)).getTile(worldObj, NeuroPart.class);
				if(p!=null)parts.add(p);
			}
		};
		if(worldObj!=null)r.run();
		else TickEvents.nextTick(r);
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		if(parts.size()>1){
			NBTTagCompound partsTags=new NBTTagCompound();
			for(int i=0;i<parts.size();i++){
				partsTags.setLong(""+i, ((TileEntity)parts.get(i)).getPos().toLong());
			}
			compound.setTag(NBT_PARTS_TAG, partsTags);
		}
		return super.writeToNBT(compound);
	}
	
	@Override
	public void update(){
		if(isRemote()){
			for(NeuroPart p:parts){
				ParticleBubbleFactory.get().spawn(new Vec3M(((TileEntity)p).getPos()).add(0.5,1,0.5), new Vec3M(), 1, 20, 0, ColorF.randomRGB());
			}
		}
	}
	
	public void refreshConnected(){
		List<NeuroPart> newParts=new ArrayList<>();
		getConnect(newParts, this);

		//old           --------------------
		//new    ----------------------
		//remove                       -----
		//add    -------
		
		parts.stream().filter(p->!newParts.contains(p)).collect(Collectors.toList()).forEach(p->{
			p.onDisconnect();
			p.setController(null);
			parts.remove(p);
		});
		newParts.stream().filter(p->!parts.contains(p)).collect(Collectors.toList()).forEach(p->{
			parts.add(p);
			p.setController(this);
			p.onConnect();
		});
		
	}
	
	private void getConnect(List<NeuroPart> newParts, NeuroPart part){
		if(part!=this&&part instanceof TileEntityNeuroController)return;
		part.getConnected().forEach(t->{
			if((t.getController()==this||t.getController()==null)&&!newParts.contains(t)){
				newParts.add(t);
				getConnect(newParts, t);
			}
		});
	}
	
	@Override
	public TileEntityNeuroController getController(){
		return this;
	}
	
	@Override
	public Collection<NeuroPart> getConnected(){
		return UtilM.getTileSides(getWorld(), new BlockPosM(getPos()), NeuroPart.class);
	}

	@Override
	public void onConnect(){}

	@Override
	public void onDisconnect(){}

	@Override
	@Deprecated
	public void setController(TileEntityNeuroController controller){}
	
}
