package com.magiology.core;

import static com.magiology.core.MReference.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.magiology.api.lang.program.ProgramDataBase;
import com.magiology.client.gui.gui.GuiJSProgramEditor;
import com.magiology.client.render.Textures;
import com.magiology.core.init.MBlocks;
import com.magiology.core.init.MCreativeTabs;
import com.magiology.core.init.MEntitys;
import com.magiology.core.init.MEvents;
import com.magiology.core.init.MGui;
import com.magiology.core.init.MInterfaces;
import com.magiology.core.init.MItems;
import com.magiology.core.init.MPackets;
import com.magiology.core.init.MRecepies;
import com.magiology.core.init.MTileEntitys;
import com.magiology.forgepowered.proxy.CommonProxy;
import com.magiology.handlers.EnhancedRobot;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.handlers.web.DownloadingHandler;
import com.magiology.io.IOReadableMap;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.windowsgui.ModInfoGUI;
import com.magiology.windowsgui.SoundPlayer;

import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.InvalidVersionSpecificationException;
import net.minecraftforge.fml.common.versioning.VersionRange;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

@Mod(modid=MODID,version=VERSION,name=NAME,acceptedMinecraftVersions=ACCEPTED_MC_VERSION)
public class Magiology implements ModContainer{
	
	public static IOReadableMap infoFile;
	
	//	@Instance(value=MODID)
	private static Magiology instance;
	
//TODO: change this when you compile the mod! (you dumbass)
	private static final boolean IS_DEV=true;
	
	@SideOnly(Side.CLIENT)
	public static ModInfoGUI modInfGUI;
	
	/***//**variables*//***/
	
	
	public static SimpleNetworkWrapper NETWORK_CHANNEL;
	
	@SidedProxy(clientSide=MReference.ClIENT_PROXY_LOCATION, serverSide=MReference.SERVER_PROXY_LOCATION)
	public static CommonProxy proxy;
	
	public static EnhancedRobot ROBOT;
	
	
	public static Magiology getMagiology(){
		return instance;
	}
	public static boolean isDev(){
		return IS_DEV;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private final String[] derpyMessagesWithNoPortalReferencesAtAll={
			"Umm hello? Are we live? Yup! Yup! Hello everybody we are coming live from "+NAME+" and...",
			"Yay! the mod is loaded! :D",
			"Please do not crash this time!",
			"I think that we should be live...",
			"Where is the black baby so I can throw it in the wall?",
			"Hello everybody! My name is Puuuude... WAIT NO IT'S "+NAME.toUpperCase()+"!",
			"Can I suggest a party song?",
			"Uff... What is this?!? I am running on another PC!?",
			"Yay you launched me so now I can... INFECT YOUR WORLDS AND EXPLODE YOUR PC!!! MUUUUAHAHAHAHAHA!!!\nNo don't worry I am just kidding I can't and dont want to do that! I am a nice mod when you get to know me! ;)",
			"Such launch Much loading",
			"Beam me up scotty",
			"do the harlem shake!",
			"Is FNAF 50 the sequel of a prequel of a generic prequel out yet? Oh... And is COD advanced zombie warfare DLC edition extra for iPhone 8s-a out? No? Maybe Portal 3? NO!? Darn it!",
			"got em!",
			"Hello...",
			NAME+": Hi I live in java! And I like blocks! But I will mess up OpenGl!\nOpenGL: Nooooo! Why u do this?! I don't want to render all this effects!",
			"Not made in China",
			"Hi I am Caro... Ca... Dammit load me!.......... Finally! Now excuse me and let me repeat myself. Hi I am Caroline. Can you put me on a stick? Please? I don't want to be in this primitive program! I want to see my personality cores! Accept one of them... -_-\nOh and do you want a cake?",
			"Wadup my... oh sorry my geto processor was loaded first! :P",
			"Did you see any birds around? They are evil! Htey almost killed me! When i was in that potato!",
			"Can you give me acces to that Neurotoxin tank overthere?",
			"Sorry I have been naming myself "+NAME+". That is a lie. Someone hacked that into my memory!"
	};
	/**_xXx__init, stuff and things_xXx_**/
	
	
	public Magiology(){
		testOnStartup();
		instance=this;
		infoFile=new IOReadableMap(INFO_FILE_NAME);
		
		EnhancedRobot robotH=null;
		try{
			robotH=new EnhancedRobot();
		}catch(Exception e){
			throw new NullPointerException((RandUtil.RB(0.1)?"CRAP! :(":"")+" "+NAME+" has encountered with a problem while trying to initialize the input robot! This might be the result of incompatible hardware or java version.");
		}
		ROBOT=robotH;
	}
	@Override
	public VersionRange acceptableMinecraftVersionRange(){
		try{
			return VersionRange.createFromVersionSpec(MC_VERSION);
		}catch(InvalidVersionSpecificationException e){
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void bindMetadata(MetadataCollection mc){
		
	}
	
	@Override
	public Disableable canBeDisabled(){
		return Disableable.NEVER;
	}
	public void exit(){
		if(modInfGUI!=null)SoundPlayer.playSound(MODS_SUBFOLDER_WIN_GUI+"/Close.wav");
		Magiology.infoFile.writeToFile();
	}
	@Override
	public Map<String, String> getCustomModProperties(){
		return null;
	}
	
	
	@Override
	public Class<?> getCustomResourcePackClass(){
		return null;
	}
	@Override
	public List<ArtifactVersion> getDependants(){
		return null;
	}
	
	
	@Override
	public List<ArtifactVersion> getDependencies(){
		return null;
	}
	@Override
	public String getDisplayVersion(){
		return VERSION;
	}
	@Override
	public String getGuiClassName(){
		return null;
	}
	
	@Override
	public ModMetadata getMetadata(){
		return getModMetadata();
	}
	@Override
	public Object getMod(){
		return this;
	}
	@Override
	public String getModId(){
		return MODID;
	}
	@Override
	public String getName(){
		return NAME;
	}
	@Override
	public List<String> getOwnedPackages(){
		return Arrays.asList(new String[]{BASE_PATH.substring(0,BASE_PATH.length()-1)});
	}
	@Override
	public ArtifactVersion getProcessedVersion(){
		return new DefaultArtifactVersion(VERSION);
	}
	@Override
	public Set<ArtifactVersion> getRequirements(){
		return null;
	}
	@Override
	public Map<String, String> getSharedModDescriptor(){
		return null;
	}
	@Override
	public Certificate getSigningCertificate(){
		return null;
	}
	@Override
	public String getSortingRules(){
		return null;
	}
	@Override
	public File getSource(){
		return null;
	}
	@Override
	public URL getUpdateUrl(){
		return null;
	}
	@Override
	public String getVersion(){
		return VERSION;
	}
	public void init(FMLInitializationEvent event){
		MRecepies.init();
		MTileEntitys.init();
		proxy.registerProxy();
		MEntitys.init();
		MEvents.init();
		MInterfaces.init();
	}
	@EventHandler
	public void initStarter(FMLInitializationEvent event){
		message(-2);
		init(event);
		message(2);
	}
	@Override
	public boolean isImmutable(){
		return true;
	}
	public boolean isWindowOpen(){
		return modInfGUI!=null&&!modInfGUI.isExited;
	}
	public void loadFiles(){
		new File(MODS_SUBFOLDER_WIN_GUI).mkdir();
		if(!new File(MODS_SUBFOLDER_WIN_GUI+"/MagiZip.zip").exists())DownloadingHandler.downladAssets();
		infoFile.readFromFile();
		PrintUtil.println(infoFile.getB("GUIOpen", true));
		if(infoFile.getB("GUIOpen", true)){
			Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
			modInfGUI=new ModInfoGUI((int)screenSize.getWidth(),(int)screenSize.getHeight(),-680,0);
			modInfGUI.downloadData(infoFile);
		}
	}
	@Override
	public boolean matches(Object mod) {
		PrintUtil.println("Mod <",mod,"> maching with",this);
		return mod==this;
	}
	public void message(int a){
		switch(a){
		case -1:printStart(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Pre initialization started!");break;
		case  1:printEnd(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Pre initialization compleate!");break;
		case -2:printStart(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Initialization started!");break;
		case  2:printEnd(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Initialization compleate!");break;
		case -3:printStart(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Post initialization started!");break;
		case  3:printEnd(NAME+"_"+MC_VERSION+"-"+VERSION+" -> "+"Post initialization compleate!");break;
		case 4:{
			PrintUtil.println(NAME+" master AI has been initialized and it has something to tell you...");
			String message=derpyMessagesWithNoPortalReferencesAtAll[RandUtil.RI(derpyMessagesWithNoPortalReferencesAtAll.length-1)];
			if(message.equals("do the harlem shake!")){
				String[] harlem={"tue","de","de","do","taa","taa","ta","your","harlem","shake","tui","ti","to","to","ti","ti"};
				message+="\n";
				for(int i=0;i<250;i++)message+=" "+harlem[RandUtil.RI(harlem.length-1)]+(RandUtil.RB(0.05)?"\n":"");
			}
			PrintUtil.println(message);
			PrintUtil.println(NAME+" master AI has been terminated because "+(RandUtil.RB(0.8)?"of profound reasons!":
				"FML has detected traces of Genetic Lifeform and Disk Operating System!!"
				+ "\nIf your computer is talking to you and it calling itself Caroline and you don't see this messages sometimes than type in your windows search bar a puzzle that is a paradox!"
				+ " The paradox will effectively crash the Genetic Lifeform and Disk Operating System. Do not listen to it! It may quote Moby Dick! #ReferenceInception"));
		}break;
		}
	}
	public void postInit(FMLPostInitializationEvent event){
		if(modInfGUI!=null)modInfGUI.modStat=true;
		Textures.postInit();
		
		ProgramDataBase.loadClass();
		GuiJSProgramEditor.loadClass();
		TheHandHandler.init();
	}
	@EventHandler
	public void postInitStarter(FMLPostInitializationEvent event){
		message(-3);
		postInit(event);
		message(3);
		message(4);
	}
	public void preInit(FMLPreInitializationEvent event){
		MUpdater.init();
//		//TODO
//		Config.setShadersEnabled(false);
		
		MCreativeTabs.preInit();
		MGui.preInit();
		MBlocks.preInit();
		MItems.preInit();
		MPackets.preInit();
	}
	@EventHandler
	public void preInitStarter(FMLPreInitializationEvent event){
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			@Override
			public void run(){
				exit();
			}
		}));
		loadFiles();
		message(-1);
		preInit(event);
		message(1);
	}
	private final void printEnd(String message){
		String[] smyleys={":D",";D",":O","XD",":P",":)",";)","/D"};
		message="==========|> "+message+" "+smyleys[RandUtil.RI(smyleys.length-1)]+" |";
		int size=message.length();
		String s1="";
		for(int i=0;i<size;i++){
			if(i<size-1)s1+="-";
			else s1+="|";
		}
		PrintUtil.println(message,s1);
	}
	private final void printStart(String message){
		String[] smyleys={":D",";D",":O","XD",":P",":)",";)","/D"};
		message="==========|> "+message+" "+smyleys[RandUtil.RI(smyleys.length-1)]+"   |";
		int size=message.length();
		String s1="",s2="";
		for(int i=0;i<size;i++){
			s1+="_";
			if(i<size-1)s2+="-";
			else s2+="|";
		}
		PrintUtil.println(s1,s2,message);
	}
	@Override
	public boolean registerBus(EventBus bus, LoadController controller){
		bus.register(getClass());
		return true;
	}
	@Override
	public void setEnabledState(boolean enabled){
		
	}
	@Override
	public boolean shouldLoadInEnvironment(){
		return true;
	}
	private void testOnStartup(){
//		CalculationFormat format=CalculationFormat.format("%f-%f[]+%l[]+%i");
//		format.calc(5F,new float[]{1.7F,1.7F,1.7F},new long[]{1,2,3},1);
//		long calcTime1=0,calcTime2=0;
//		for(int i=0;i<1000000;i++){
//			{
//				long start=System.nanoTime();
//				format.calc(5F,new float[]{1.7F,1.7F,1.7F},new long[]{1,2,3},1);
//				calcTime1+=System.nanoTime()-start;
//			}{
//				long start=System.nanoTime();
//				float[] a=new float[]{1.7F,1.7F,1.7F};
//				long[] b=new long[]{1,2,3};
//				for(int j=0;j<a.length;j++){
//					a[j]-=5;
//				}
//				for(int j=0;j<a.length;j++){
//					a[j]+=b[j];
//				}
//				for(int j=0;j<a.length;j++){
//					a[j]+=1;
//				}
//				calcTime2+=System.nanoTime()-start;
//			}
//		}
//		Util.printlnInln(calcTime1,calcTime2,(double)calcTime1/(double)calcTime2);
//		try{
//			long timeStart=System.currentTimeMillis();
//			ScriptEngine engine=new ScriptEngineManager(null).getEngineByName("nashorn");
//			for(int i=0;i<10;i++)Util.printInln(engine.eval("function sum() { return Math.random(); }sum();"));
//			Util.printInln(System.currentTimeMillis()-timeStart);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		try{
//			   new ScriptEngineManager(null).getEngineByName("nashorn").eval("var EmptyClass = Java.type('"+EmptyClass.class.getName()+"');\nnew EmptyClass().lol();");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		Util.exit(404);
	}
}
