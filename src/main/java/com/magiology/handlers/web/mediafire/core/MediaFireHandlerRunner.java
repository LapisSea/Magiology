package com.magiology.handlers.web.mediafire.core;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.magiology.handlers.web.mediafire.packets.Ping;
import com.magiology.handlers.web.mediafire.packets.WorkDone;

public class MediaFireHandlerRunner{
	
	private static MediaFireHandlerRunner instance;
	public static MediaFireHandlerRunner getInstance(){
		if(instance==null)instance=new MediaFireHandlerRunner();
		return instance;
	}
	private static void sleep(long time){
		try{Thread.sleep(time);}catch(Exception e){}
	}
	private Object changeObj;
	private boolean isRunning,mfWorking=false,isConnected;
	
	public Runnable changeHook=()->{System.out.println(changeObj.toString());};
	
	private long lastInteractionTime;
	private ServerSocket server;
	
	private Socket socket;
	
	private List<Object> toSend=new ArrayList<>();
	private MediaFireHandlerRunner(){
		try{
			server=new ServerSocket(0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void comunicate()throws Exception{
		
		setChangeObj("Waiting for external program connection!");
		isConnected=false;
		socket=server.accept();
		isConnected=true;
		isRunning=true;
		setChangeObj("External program connected!");
		
		
		Thread 
			t1=new Thread(()->{
				while(isRunning){
					if(lastInteractionTime+4000-System.currentTimeMillis()<0){
						try{
							writeObj(new Ping());
						}catch(Exception e){
							isRunning=false;
						}
					}
					
					sleep(200);
				}
			}),
			outThread=new Thread(()->{
				while(isRunning){
					if(!toSend.isEmpty()){
						try{
							ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
							out.writeObject(toSend.remove(0));
							out.flush();
						}catch(Exception e){
							isRunning=false;
						}
					}
					else sleep(20);
				}
			}),
			inThread=new Thread(()->{
				try{
					while(isRunning){
						ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
						onObjReceve(in.readObject());
					}
				}catch(Exception e){
					isRunning=false;
				}
			});
		
		inThread.start();
		outThread.start();
		t1.start();
		
		while(isRunning)sleep(100);
		
		setChangeObj("External program crashed or closed! Killing connection threads!");
		isRunning=false;
		while(t1.isAlive()||outThread.isAlive()||inThread.isAlive())sleep(10);
		if(!socket.isClosed())socket.close();
		setChangeObj("External program disconnected!");
	}
	
	public void connect(File file, String email, String password, String[] filesToSync, String mediaFirePath){
		if(!file.exists()||!file.isFile())throw new IllegalArgumentException("Jar file not foud at "+file.getAbsolutePath()+"!");
		try{
			
			Runtime rt = Runtime.getRuntime();
			StringBuilder args=new StringBuilder();
			
			args.append("email=\"").append(email).append("\" ");
			args.append("password=\"").append(password).append("\" ");
			args.append("files=\"");
			for(int i=0;i<filesToSync.length;i++){
				args.append(filesToSync[i]);
				if(i==filesToSync.length-1)args.append("\" ");
				else args.append(", ");
			}
			
			args.append("mediaFirePath=\"").append(mediaFirePath).append("\" ");
			args.append("port=").append(server.getLocalPort());
			
			System.out.println(args);
			
			rt.exec("java -jar "+file+" "+args);
			comunicate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void doAction(MFAction action){
		writeObj(action);
	}
	
	public Object getChangeObj(){
		return changeObj;
	}
	
	public boolean isConnected(){
		return isConnected;
	}
	public boolean isMFWorking(){
		return mfWorking;
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	private void onObjReceve(Object obj){
		lastInteractionTime=System.currentTimeMillis();
		
		boolean shouldUpdate=true;
		
		if(obj instanceof Ping)shouldUpdate=false;
		else if(obj instanceof WorkDone)mfWorking=false;
		
		if(shouldUpdate)setChangeObj(obj);
	}
	
	private void setChangeObj(Object obj){
		changeObj=obj;
		changeHook.run();
	}

	private void writeObj(Object obj){
		lastInteractionTime=System.currentTimeMillis();
		toSend.add(obj);
	}
}
