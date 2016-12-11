package com.magiology.core;

import java.io.File;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.SourceVersion;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;

/**
 * 
 * This is a generic {@link ModContainer} that can be used by any mod for pretty much anything.
 * <p>
 * You may:<br>
 * copy it -> <b>Do not</b> remove the <em>credits</em>!<br>
 * copy it and modify it -> <b>Keep</b> the <em>"@author LapisSea"</em>! (you may add any other author/s to @author list)
 * </p>
 * @author LapisSea
 */
public abstract class GenericModContainerImpl implements ModContainer{
	
	private ArtifactVersion	processedVersion;
	private int				classVersion;
	
	protected final List<ArtifactVersion>	dependants			=new ArrayList<>();
	protected final List<ArtifactVersion>	dependencies		=new ArrayList<>();
	protected final Set<ArtifactVersion>	requirements		=new HashSet<>();
	protected final Map<String, String>		customModProperties	=new HashMap<>();
	
	private final VersionRange	acceptedMcVersion;
	private final String		modGuiClassName;
	private final List<String>	ownedPackages;
	
	public GenericModContainerImpl(String acceptedMcVersion, Class<IModGuiFactory> modGuiOptional){
		this(acceptedMcVersion, modGuiOptional, "");
	}
	
	public GenericModContainerImpl(String acceptedMcVersion){
		this(acceptedMcVersion, null, new String[0]);
	}
	
	public GenericModContainerImpl(Class<IModGuiFactory> modGuiOptional, String...ownedPackages){
		this(null, modGuiOptional, ownedPackages);
	}
	
	public GenericModContainerImpl(Class<IModGuiFactory> modGuiOptional){
		this(null, modGuiOptional);
	}
	
	public GenericModContainerImpl(){
		this(null, null, new String[0]);
	}
	
	public GenericModContainerImpl(String acceptedMcVersion, Class<IModGuiFactory> modGui, String...ownedPackages){
		this.acceptedMcVersion=acceptedMcVersion==null?Loader.instance().getMinecraftModContainer().getStaticVersionRange():VersionParser.parseRange(acceptedMcVersion);
		modGuiClassName=modGui==null?null:modGui.getName();
		this.ownedPackages=Arrays.asList(ownedPackages);
		this.ownedPackages.removeIf(pack->{
			if(pack==null) return true;
			if(pack.isEmpty()) return true;
			
			boolean valid=SourceVersion.isIdentifier(pack)&&Arrays.asList(pack.split(".")).stream().noneMatch(SourceVersion::isKeyword);
			if(!valid) FMLLog.severe("Package \""+pack+" by "+this+" is not valid! It will be ignored.");
			return valid;
		});
	}
	
	@Override
	public void bindMetadata(MetadataCollection mc){}
	
	@Override
	public List<ArtifactVersion> getDependants(){
		return dependants;
	}
	
	@Override
	public List<ArtifactVersion> getDependencies(){
		return dependencies;
	}
	
	@Override
	public Set<ArtifactVersion> getRequirements(){
		return requirements;
	}
	
	@Override
	public String getModId(){
		return getMetadata().modId;
	}
	
	@Override
	public String getName(){
		return getMetadata().name;
	}
	
	@Override
	public String getSortingRules(){
		return "";
	}
	
	@Override
	public File getSource(){
		return null;
	}
	
	@Override
	public String getVersion(){
		return getMetadata().version;
	}
	
	@Override
	public boolean matches(Object mod){
		return mod==this;
	}
	
	@Override
	public void setEnabledState(boolean enabled){}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller){
		return false;
	}
	
	@Override
	public ArtifactVersion getProcessedVersion(){
		if(processedVersion==null) processedVersion=new DefaultArtifactVersion(getModId(), getVersion());
		return processedVersion;
	}
	
	@Override
	public boolean isImmutable(){
		return false;
	}
	
	@Override
	public String getDisplayVersion(){
		return getMetadata().version;
	}
	
	@Override
	public VersionRange acceptableMinecraftVersionRange(){
		return acceptedMcVersion;
	}
	
	@Override
	public Certificate getSigningCertificate(){
		return null;
	}
	
	@Override
	public Map<String, String> getCustomModProperties(){
		return customModProperties;
	}
	
	@Override
	public Class<?> getCustomResourcePackClass(){
		return null;
	}
	
	@Override
	public Map<String, String> getSharedModDescriptor(){
		return null;
	}
	
	@Override
	public Disableable canBeDisabled(){
		return Disableable.NEVER;
	}
	
	@Override
	public String getGuiClassName(){
		return modGuiClassName;
	}
	
	@Override
	public List<String> getOwnedPackages(){
		return ownedPackages;
	}
	
	@Override
	public boolean shouldLoadInEnvironment(){
		return true;
	}
	
	@Override
	public void setClassVersion(int classVersion){
		this.classVersion=classVersion;
	}
	
	@Override
	public int getClassVersion(){
		return classVersion;
	}
	
	@Override
	@Deprecated
	public final URL getUpdateUrl(){
		return null;
	}
}
