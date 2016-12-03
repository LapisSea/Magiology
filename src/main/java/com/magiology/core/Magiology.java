package com.magiology.core;

import static com.magiology.core.MReference.*;

import java.io.File;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.magiology.DevOnly.DevOnlyBlockCutter;
import com.magiology.core.class_manager.ClassList;
import com.magiology.forge.networking.SimpleNetworkWrapperM;
import com.magiology.forge.proxy.CommonProxy;
import com.magiology.io.IOManager;
import com.magiology.util.statics.LogUtil;

import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionRange;
import scala.actors.threadpool.Arrays;

@Mod(modid=MODID, version=VERSION, name=NAME, acceptedMinecraftVersions=ACCEPTED_MC_VERSION)
public class Magiology implements ModContainer{
	
	private static final boolean IS_DEV;
	static{
		getClassLoader().addClassLoaderExclusion("jdk.nashorn");
		
		CompatibilityChecker.checkJava8();
		IS_DEV=SOURCE_FILE==null;
		if(IS_DEV)LogUtil.printWrapped(NAME+" is running in development environment! Work Lapis! Work! NO! CLOSE THAT YOUTUBE VIDEO!");
		else getClassLoader().registerTransformer(DevOnlyBlockCutter.class.getName());
	}
	
	/***//** variables *//***/
	@SidedProxy(modId=MODID, clientSide=CLIENT_PROXY_LOCATION, serverSide=SERVER_PROXY_LOCATION)
	public static CommonProxy			sideProxy;
	public static CommonProxy			commonProxy	=new CommonProxy();
	public static SimpleNetworkWrapperM	NETWORK_CHANNEL;
	@Instance(value=MODID)
	private static Magiology			instance;
	private static String				marker		=NAME+"_"+MC_VERSION+"-"+VERSION;
	public static IOManager				extraFiles	=new IOManager();

	public Magiology(){
		instance=this;
		
//		final String script ="\n"
//				+  "function main(){\n"
//				+  "	var JavaHomeGetter = Java.type(\"com.magiology.handlers.scripting.bridge.JavaHomeGetter\");\n"
//				+  "	print([JavaHomeGetter.get(),2+1.2]);\n"
//				+  "}\n"
//				+  "function render(){\n"
//				+  "	print(\"rendered\");\n"
//				+  "}\n"
//				+  "function update(){\n"
//				+  "	print(\"updated\");\n"
//				+  "}\n";
//		LogUtil.println("\n \n \n-----------------------------------------------------------------");
//		try{
//			RenderNUpdateScript sc=new RenderNUpdateScript(script);
//			sc.callMain();
//			sc.update();
//			sc.render();
//			for(ScriptWrapper j:sc.getLogs()){
//				for(ScriptLogLine i:j.getLog().getAllLog())LogUtil.println(i.type,i.isError,i.msg);
//				LogUtil.println("-----------------");
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		LogUtil.println("-----------------------------------------------------------------\n \n \n");
//		UtilM.exit(404);
		
		ClassList.getImplementations();
	}
	
	/***//** forge events *//***/
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		
		commonProxy.loadModFiles();
		sideProxy.loadModFiles();
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			commonProxy.onExit();
			sideProxy.onExit();
		}));
		
		LogUtil.printWrapped(marker+" -> Pre initialization started!");
		commonProxy.preInit();
		sideProxy.preInit();
		LogUtil.printWrapped(marker+" -> Pre initialization compleate!");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		
		LogUtil.printWrapped(marker+" -> Initialization started!");
		commonProxy.init();
		sideProxy.init();
		LogUtil.printWrapped(marker+" -> Initialization compleate!");
		
	}
	
	@EventHandler
	public void postInitStarter(FMLPostInitializationEvent event){
		
		LogUtil.printWrapped(marker+" -> Post initialization started!");
		commonProxy.postInit();
		sideProxy.postInit();
		LogUtil.printWrapped(marker+" -> Post initialization compleate!");
		
		marker=null;
	}
	
	/***/
	/** getters */
	/***/
	public static Magiology getMagiology(){
		return instance;
	}
	
	public static boolean isDev(){
		return IS_DEV;
	}
	
	public static LaunchClassLoader getClassLoader(){
		return (LaunchClassLoader)Magiology.class.getClassLoader();
	}
	
	/***/
	/** interfaces */
	/***/
	@Override
	public VersionRange acceptableMinecraftVersionRange(){
		try{
			return VersionRange.createFromVersionSpec(MC_VERSION);
		}catch(Exception e){}
		return null;
	}
	
	@Override
	public void bindMetadata(MetadataCollection mc){}
	
	@Override
	public Disableable canBeDisabled(){
		return Disableable.NEVER;
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
		return Arrays.asList(new String[]{BASE_PATH.substring(0, BASE_PATH.length()-1)});
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
	
	@Override
	public boolean isImmutable(){
		return true;
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller){
		bus.register(getClass());
		return true;
	}
	
	@Override
	public void setEnabledState(boolean enabled){
		String keemstar="Alex is a stupid niger";
		keemstar.equals("Gnome");
	}
	
	@Override
	public boolean shouldLoadInEnvironment(){
		return true;
	}
	
	@Override
	public boolean matches(Object mod){
		return mod==this;
	}
	
	@Override
	public void setClassVersion(int classVersion){}
	
	@Override
	public int getClassVersion(){
		return -1;
	}
}
