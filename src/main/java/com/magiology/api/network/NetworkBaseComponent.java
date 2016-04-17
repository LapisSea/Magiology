package com.magiology.api.network;

import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;


public interface NetworkBaseComponent{
	
	public class NetworkBaseComponentHandler{

		private static void check(NetworkBaseComponent component){
//			if(!component.isInitialized())throw new IllegalStateException("NetworkComponent has an invalid init method fix it by setting the isInitialized to true in the init method!");
		}
		public static <T extends NetworkBaseComponent> T createComponent(T component){
			initTheComponent(component);
			return component;
		}
		public static TileEntityNetworkController getBrain(NetworkBaseComponent component){
			check(component);
			return component.getBrain();
		}
		public static long getNetworkId(NetworkBaseComponent component){
			check(component);
			return component.getNetworkId();
		}
		public static void initTheComponent(NetworkBaseComponent component){
			component.initTheComponent();
			check(component);
		}
		public static boolean isInitialized(NetworkBaseComponent component){
			return component.isInitialized();
		}
		public static void setBrain(TileEntityNetworkController brain,NetworkBaseComponent component){
			check(component);
			component.setBrain(brain);
		}
	}
	public TileEntityNetworkController getBrain();
	public long getNetworkId();
	public void initTheComponent();
	public boolean isInitialized();
	
	public void setBrain(TileEntityNetworkController brain);
}
