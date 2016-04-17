package com.magiology.handlers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.magiology.util.utilclasses.PrintUtil;

import net.minecraft.entity.player.EntityPlayer;
import scala.collection.mutable.StringBuilder;

public class PremiumHandeler{
	
	public class PlayerPremiumData{
		private boolean hasOfflineUUID,isPremium;
		
		private PlayerPremiumData(boolean hasOfflineUUID, boolean isPremium){
			this.hasOfflineUUID=hasOfflineUUID;
			this.isPremium=isPremium;
		}
		
		private PlayerPremiumData copy(){
			return instance.new PlayerPremiumData(hasOfflineUUID, isPremium);
		}
		
		public boolean isHasOfflineUUID(){
			return hasOfflineUUID;
		}

		public boolean isPremium(){
			return isPremium;
		}

		@Override
		public String toString(){
			StringBuilder result=new StringBuilder();
			result.append("PlayerPremiumData(").append("\n");
			result.append("\t").append("Has offline UUID: ").append(hasOfflineUUID).append("\n");
			result.append("\t").append("Can access internet: ").append(canAccesInternet).append("\n");
			result.append("\t").append("Is premium: ").append(isPremium).append("\n");
			result.append(")");
			return result.toString();
		}
	}
	
	private static final boolean canAccesInternet=testInternet();
	
	private static Map<String, PlayerPremiumData> data=new HashMap<String, PlayerPremiumData>();
	private static final PremiumHandeler instance=new PremiumHandeler();
	
	
	
	public static boolean canAccesInternet(){
		return canAccesInternet;
	}
	
	private static void check(EntityPlayer player){
		if(isMissing(player))fill(player);
	}
	
	private static void fill(EntityPlayer player){
		data.put(name(player), generateData(player));
	}
	
	private static PlayerPremiumData generateData(EntityPlayer player){
		
		UUID
			premiumUUID=player.getGameProfile().getId(),
			offlineUUID=player.getOfflineUUID(player.getGameProfile().getName());
		
		boolean 
			hasOfflineUUID=premiumUUID.equals(offlineUUID),
			isPremium=canAccesInternet?!hasOfflineUUID:false;
		
		PlayerPremiumData result=instance.new PlayerPremiumData(hasOfflineUUID,isPremium);
		
		PrintUtil.println("Player:",name(player),"has been checked for premium!\nThe results are:\n",result);
		
		return result;
	}
	
	public static PremiumHandeler get(){
		return instance;
	}
	private static PlayerPremiumData get(EntityPlayer player){
		check(player);
		return data.get(name(player));
	}
	public static Map<String, PlayerPremiumData> getAll(){
		final Map<String, PlayerPremiumData> result=new HashMap<String, PlayerPremiumData>();
		data.entrySet().forEach(entry->result.put(entry.getKey(), entry.getValue().copy()));
		return result;
	}
	public static boolean hasOfflineUUID(EntityPlayer player){
		return get(player).hasOfflineUUID;
	}
	
	
	
	public static boolean isMissing(EntityPlayer player){
		return !data.containsKey(name(player));
	}
	
	public static boolean isPremium(EntityPlayer player){
		return get(player).isPremium;
	}
	
	private static String name(EntityPlayer player){
		return player.getGameProfile().getName();
	}
	
	public static PlayerPremiumData remove(EntityPlayer player){
		return data.remove(name(player));
	}
	private static boolean testInternet(){
		boolean result=false;
		try{
			result=InetAddress.getByName("minecraft.net")!=null;
		}catch(UnknownHostException e){}
		return result;
	}
	
	public static String toString(EntityPlayer player){
		return get(player).toString();
	}
	
	
	
	
	private PremiumHandeler(){}
}
