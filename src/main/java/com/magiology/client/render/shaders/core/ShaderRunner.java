package com.magiology.client.render.shaders.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.client.render.shaders.BlurRenderer;
import com.magiology.client.render.shaders.ColorCutRenderer;
import com.magiology.client.render.shaders.ColorRenderer;
import com.magiology.core.Config;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShaderRunner{
	
	public List<ShaderAspectRenderer> handlers=new ArrayList<ShaderAspectRenderer>();
	Minecraft mc=UtilC.getMC();
	EntityPlayer player=UtilC.getThePlayer();
	
	public boolean redrawShaders=false,inited=false;
	
	private Vector2f screenSize=new Vector2f();
	public Map<Integer, ShaderGroup> shaderGroups=new HashMap();
	
	public ShaderGroup[] shaders;
	
	public ShaderRunner(){}
	
	public void deactivateShader(int shaderId){
		shaderGroups.remove(shaderId);
	}
	public void init(){
		handlers.clear();
		handlers.add(new BlurRenderer());
		handlers.add(new ColorRenderer());
		handlers.add(new ColorCutRenderer());
		ResourceLocation[] shaderPaths=null;
		for(ShaderAspectRenderer handler:handlers){
			ResourceLocation loc=handler.getShaderLocation();
			boolean contains=false;
			if(shaderPaths!=null)for(ResourceLocation i:shaderPaths){
				if(i.toString().equals(loc.toString())){
					contains=true;
				}
			}
			if(!contains)shaderPaths=ArrayUtils.add(shaderPaths, loc);
		}
		shaders=new ShaderGroup[shaderPaths.length];
		for(int i=0;i<shaderPaths.length;i++)try{
			shaders[i]=new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shaderPaths[i]);
		}catch(Exception e){e.printStackTrace();}
	}
	@SubscribeEvent
	public void renderShaders(RenderGameOverlayEvent.Pre event){
		if(Config.isShadersEnabled()&&event.getType()==ElementType.ALL&&OpenGlHelper.shadersSupported/*&&!shaderGroups.isEmpty()*/){
			for(ShaderAspectRenderer handler:handlers){
				if(handler.uniforms.isEmpty())handler.init(this);
			}
			
			player=UtilC.getThePlayer();
			updateFrameBuffers();
			World w=UtilC.getTheWorld();
			for(int i=0;i<handlers.size();i++){
				ShaderAspectRenderer shaderAspect=handlers.get(i);
				shaderAspect.player=player;
				shaderAspect.world=w;
				shaderAspect.mc=mc;
				if(shaderAspect.getConditionForActivation())shaderAspect.redner();
			}
			GL11.glMatrixMode(5890);
			GL11.glLoadIdentity();
			for(ShaderGroup sg:shaderGroups.values()){
				OpenGLM.pushMatrix();
				try{sg.loadShaderGroup(event.getPartialTicks());}catch(Exception e){e.printStackTrace();}
				OpenGLM.popMatrix();
			}
			mc.getFramebuffer().bindFramebuffer(true);
		}
	}
	public void setShader(ShaderGroup target, int shaderId){
		if(OpenGlHelper.shadersSupported){
			if(shaderGroups.containsKey(shaderId)){
				shaderGroups.get(shaderId).deleteShaderGroup();
				shaderGroups.remove(Integer.valueOf(shaderId));
			}
			try{
				if(target==null)deactivateShader(shaderId);
				else{
					redrawShaders=true;
					shaderGroups.put(shaderId, target);
				}
			}catch(Exception e){
				shaderGroups.remove(shaderId);
			}
		}
	}
	private void setShaderEnabled(int id,boolean enabled){
		try{
			if(enabled){
				if(!shaderGroups.containsKey(id))setShader(shaders[id],id);
			}else if(shaderGroups.containsKey(id))deactivateShader(id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void updateFrameBuffers(){
		if(redrawShaders||mc.displayWidth!=screenSize.x||screenSize.y!=mc.displayHeight){
			for(ShaderGroup sg:shaderGroups.values())sg.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			screenSize.x=mc.displayWidth;
			screenSize.y=mc.displayHeight;
		}
	}
	@SideOnly(value=Side.CLIENT)
	@SubscribeEvent
	public void updateShaders(LivingUpdateEvent event){
		if(!Config.isShadersEnabled()||!U.isRemote(event.getEntity())||event.getEntity()!=UtilC.getThePlayer())return;
		player=UtilC.getThePlayer();
		if(handlers.isEmpty())init();
		Map<Integer, Boolean> enabledMap=new HashMap<Integer, Boolean>();
		World w=UtilC.getTheWorld();
		for(int i=0;i<handlers.size();i++){
			ShaderAspectRenderer shaderAspect=handlers.get(i);
			shaderAspect.player=player;
			shaderAspect.world=w;
			shaderAspect.mc=mc;
			
			boolean active=shaderAspect.getConditionForActivation();
			
			if(active)shaderAspect.update();
			
			int id=shaderAspect.getShaderId();
			if(enabledMap.containsKey(id)){
				boolean prevActive=enabledMap.get(id);
				enabledMap.put(id, prevActive||active);
			}else enabledMap.put(id, active);
		}
		for(int i:enabledMap.keySet()){
			setShaderEnabled(i, enabledMap.get(i));
		}
		
	}
}
