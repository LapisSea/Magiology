package com.magiology.api.network;


public interface Messageable{
	public void onMessageReceved(String action);
	public void sendMessage();
}
