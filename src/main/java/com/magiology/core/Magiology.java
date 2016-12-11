package com.magiology.core;

import static com.magiology.core.MReference.*;

import com.google.common.collect.Lists;
import com.magiology.Development;
import com.magiology.core.registry.AssistantBotLaucher;
import com.magiology.forge.networking.SimpleNetworkWrapperM;
import com.magiology.forge.proxy.CommonProxy;
import com.magiology.io.IOManager;
import com.magiology.util.statics.LogUtil;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.InstanceFactory;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid=MODID, version=VERSION, name=NAME, acceptedMinecraftVersions=ACCEPTED_MC_VERSION)
public class Magiology extends GenericModContainerImpl{
	
	/////////////////////////PRE_PRE_INIT\\\\\\\\\\\\\\\\\\\\\\\\\
	@SideOnly(Side.CLIENT)
	private static final boolean CLIENT_ONLY_REMOVED_TESTER=true;
	
	public static final boolean IS_DEV, CLIENT_ONLY_REMOVED;
	static{
		CompatibilityChecker.checkJava8();
		getClassLoader().addClassLoaderExclusion("jdk.nashorn");
		
		CLIENT_ONLY_REMOVED=Lists.newArrayList(Magiology.class.getDeclaredMethods()).stream().anyMatch(method->method.getName().equals("CLIENT_ONLY_REMOVED_TESTER"));
		IS_DEV=SOURCE_FILE==null;
		if(IS_DEV) LogUtil.printWrapped(NAME+" is running in development environment! Work Lapis! Work! NO! CLOSE THAT YOUTUBE VIDEO!");
	}
	
	/////////////////////////VARIABLES\\\\\\\\\\\\\\\\\\\\\\\\\
	@Instance(value=MODID)
	private static Magiology	INSTANCE;
	@SidedProxy(modId=MODID, clientSide=CLIENT_PROXY_LOCATION, serverSide=SERVER_PROXY_LOCATION)
	public static CommonProxy	SIDE_PROXY;
	@Metadata(MODID)
	private static ModMetadata	META_DATA;
	
	public static CommonProxy					COMMON_PROXY	=new CommonProxy();
	public static final SimpleNetworkWrapperM	NETWORK_CHANNEL	=new SimpleNetworkWrapperM(CHANNEL_NAME);
	private static String						MARKER			=NAME+"_"+MC_VERSION+"-"+VERSION;
	public static final IOManager				EXTRA_FILES		=new IOManager();
	private static final ConfigM				CONFIG			=new ConfigM();
	
	@InstanceFactory
	private static Magiology newMagiologyInstance(){
		return new Magiology();
	}
	
	public Magiology(){
		super(ACCEPTED_MC_VERSION, null, "com.magiology");
		
		INSTANCE=this;
		AssistantBotLaucher.run();
		Development.startupTest();
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			COMMON_PROXY.onExit();
			SIDE_PROXY.onExit();
		}));
	}
	
	/////////////////////////FORGE_EVENTS\\\\\\\\\\\\\\\\\\\\\\\\\
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		MReference.overrideMetadata(META_DATA);
		
		COMMON_PROXY.loadModFiles();
		SIDE_PROXY.loadModFiles();
		
		LogUtil.printWrapped(MARKER+" -> Pre initialization started!");
		COMMON_PROXY.preInit();
		SIDE_PROXY.preInit();
		LogUtil.printWrapped(MARKER+" -> Pre initialization compleate!");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		
		LogUtil.printWrapped(MARKER+" -> Initialization started!");
		COMMON_PROXY.init();
		SIDE_PROXY.init();
		LogUtil.printWrapped(MARKER+" -> Initialization compleate!");
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		
		LogUtil.printWrapped(MARKER+" -> Post initialization started!");
		COMMON_PROXY.postInit();
		SIDE_PROXY.postInit();
		LogUtil.printWrapped(MARKER+" -> Post initialization compleate!");
		MARKER=null;
	}
	
	/////////////////////////GETTERS\\\\\\\\\\\\\\\\\\\\\\\\\
	public static Magiology getMagiology(){
		return INSTANCE;
	}
	
	public static LaunchClassLoader getClassLoader(){
		return (LaunchClassLoader)Magiology.class.getClassLoader();
	}
	
	@Override
	public Magiology getMod(){
		return INSTANCE;
	}
	
	@Override
	public String toString(){
		return MODID+'_'+MC_VERSION+'-'+VERSION;
	}
	
	public static Configuration getConfig(){
		return CONFIG;
	}
	
	@Override
	public ModMetadata getMetadata(){
		LogUtil.println(META_DATA);
		return META_DATA;
	}
}
