package com.magiology.mcobjects.entitys;

import com.magiology.core.MReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExtendedPlayerData{

	@CapabilityInject(IMagiologyPlayerData.class)
	private static final Capability<IMagiologyPlayerData> MAGIOLOGY_PLAYER_DATA=null;
	
	public void register(){
		CapabilityManager.INSTANCE.register(IMagiologyPlayerData.class, new Storage(), DefaultMagiologyPlayerData.class);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@CapabilityInject(IMagiologyPlayerData.class)
	private static void capRegistered(Capability<IMagiologyPlayerData> cap){
		System.out.println("IExampleCapability was registered wheeeeee!");
	}
	
	
	private static IMagiologyPlayerData getData(EntityPlayer player){
		return player.getCapability(MAGIOLOGY_PLAYER_DATA, EnumFacing.DOWN);
	}
	
	public static int getEnergy(EntityPlayer player){
		return getData(player).getEnergy();
	}
	public static void setEnergy(EntityPlayer player, int energy){
		getData(player).setEnergy(energy);
	}
	public static int getMaxEnergy(EntityPlayer player){
		return getData(player).getMaxEnergy();
	}
	public static void setMaxEnergy(EntityPlayer player, int maxEnergy){
		getData(player).setMaxEnergy(maxEnergy);
	}
	
	public static boolean isFlappingDown(EntityPlayer player){
		return getData(player).isFlappingDown();
	}
	public static void setFlappingDown(EntityPlayer player, boolean flappingDown){
		getData(player).setFlappingDown(flappingDown);
	}
	
	
	
	public static interface IMagiologyPlayerData{
		public int getEnergy();
		public void setEnergy(int energy);
		public int getMaxEnergy();
		public void setMaxEnergy(int energy);
		
		public boolean isFlappingDown();
		public void setFlappingDown(boolean flappingDown);
	}

	public static class Storage implements IStorage<IMagiologyPlayerData>{
		
		@Override
		public NBTBase writeNBT(Capability<IMagiologyPlayerData> capability, IMagiologyPlayerData instance, EnumFacing side){
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("en", instance.getEnergy());
			nbt.setBoolean("fd", instance.isFlappingDown());
			return nbt;
		}
		
		@Override
		public void readNBT(Capability<IMagiologyPlayerData> capability, IMagiologyPlayerData instance, EnumFacing side, NBTBase nbtBase){
			NBTTagCompound nbt=(NBTTagCompound)nbtBase;
			instance.setEnergy(nbt.getInteger("en"));
			instance.setFlappingDown(nbt.getBoolean("fd"));
		}
		
	}
	
	public static class DefaultMagiologyPlayerData implements IMagiologyPlayerData{
		
		private DefaultMagiologyPlayerData(){}
		
		@Override
		public int getEnergy(){
			return -1;
		}

		@Override
		public void setEnergy(int energy){
			
		}

		@Override
		public boolean isFlappingDown(){
			return false;
		}

		@Override
		public void setFlappingDown(boolean flappingDown){
			
		}

		@Override
		public int getMaxEnergy(){
			return 0;
		}

		@Override
		public void setMaxEnergy(int energy){
			
		}
	}
	
	public class MagiologyPlayerData implements ICapabilityProvider, IMagiologyPlayerData{
		
		private int energy,maxEnergy;
		private boolean flappingDown;
		
		private MagiologyPlayerData(){}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing){
			return capability!=null&&capability==MAGIOLOGY_PLAYER_DATA;
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing){
			return capability==MAGIOLOGY_PLAYER_DATA?(T)this:null;
		}
		
		@Override
		public int getEnergy(){
			return energy;
		}
		@Override
		public void setEnergy(int energy){
			this.energy=energy;
		}
		@Override
		public int getMaxEnergy(){
			return maxEnergy;
		}
		@Override
		public void setMaxEnergy(int maxEnergy){
			this.maxEnergy=maxEnergy;
		}
		
		@Override
		public boolean isFlappingDown(){
			return flappingDown;
		}
		@Override
		public void setFlappingDown(boolean flappingDown){
			this.flappingDown=flappingDown;
		}

	}

	@SubscribeEvent
	public void onPlayerLoad(AttachCapabilitiesEvent.Entity event){
		if(!(event.getEntity()instanceof EntityPlayer))return;
		
		event.addCapability(new ResourceLocation(MReference.MODID+":MagiologyData"), new MagiologyPlayerData());
	}

}
