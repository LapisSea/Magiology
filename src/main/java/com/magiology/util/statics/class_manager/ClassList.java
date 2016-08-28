package com.magiology.util.statics.class_manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.util.statics.PrintUtil;

public class ClassList{
	
	static String[]
			classes={
				"com.magiology.client.entity.EntityPenguinRenderer",
				"com.magiology.client.renderers.AdvancedRenderer",
				"com.magiology.client.renderers.GenericModels",
				"com.magiology.client.renderers.Renderer",
				"com.magiology.client.rendering.tile_render.DummyTileEntityRenderer",
				"com.magiology.client.shaders.effects.PositionAwareEffect",
				"com.magiology.client.shaders.effects.SoftEffectsShader",
				"com.magiology.client.shaders.programs.InvisibleEffect",
				"com.magiology.client.shaders.programs.SepiaShader",
				"com.magiology.client.shaders.ShaderHandler",
				"com.magiology.client.shaders.ShaderProgram",
				"com.magiology.client.shaders.upload.UniformUploaderArray",
				"com.magiology.client.shaders.upload.UniformUploaderBase",
				"com.magiology.client.shaders.upload.UniformUploaderColor",
				"com.magiology.client.shaders.upload.UniformUploaderCustom",
				"com.magiology.client.shaders.upload.UniformUploaderF1",
				"com.magiology.client.shaders.upload.UniformUploaderF2",
				"com.magiology.client.shaders.upload.UniformUploaderF3",
				"com.magiology.client.shaders.upload.UniformUploaderF4",
				"com.magiology.client.shaders.upload.UniformUploaderVec",
				"com.magiology.core.Config",
				"com.magiology.core.Magiology",
				"com.magiology.core.MReference",
				"com.magiology.forge_powered.events.ClientEvents",
				"com.magiology.forge_powered.events.EntityEvents",
				"com.magiology.forge_powered.events.RenderEvents",
				"com.magiology.forge_powered.events.TickEvents",
				"com.magiology.forge_powered.networking.NBTPacket",
				"com.magiology.forge_powered.networking.PacketBufferM",
				"com.magiology.forge_powered.networking.PacketM",
				"com.magiology.forge_powered.networking.Packets",
				"com.magiology.forge_powered.networking.SimpleNetworkWrapperM",
				"com.magiology.forge_powered.proxy.ClientProxy",
				"com.magiology.forge_powered.proxy.CommonProxy",
				"com.magiology.forge_powered.proxy.ServerProxy",
				"com.magiology.handlers.particle.IParticle",
				"com.magiology.handlers.particle.ParticleFactory",
				"com.magiology.handlers.particle.ParticleHandler",
				"com.magiology.handlers.particle.ParticleM",
				"com.magiology.mc_objects.entitys.EntityPenguin",
				"com.magiology.mc_objects.features.dimension_stabiliser.DimensionStabiliserBlock",
				"com.magiology.mc_objects.features.dimension_stabiliser.TileEntityDimensionStabiliser",
				"com.magiology.mc_objects.items.ItemJetpack",
				"com.magiology.mc_objects.items.SimpleItems",
				"com.magiology.mc_objects.MBlocks",
				"com.magiology.mc_objects.MItems",
				"com.magiology.mc_objects.MTabs",
				"com.magiology.mc_objects.particles.ParticleBubbleFactory",
				"com.magiology.mc_objects.particles.ParticleCubeFactory",
				"com.magiology.mc_objects.particles.ParticleMistBubbleFactory",
				"com.magiology.mc_objects.particles.ParticleMistyEnergyFactory",
				"com.magiology.mc_objects.particles.Particles",
				"com.magiology.SoundM",
				"com.magiology.util.interf.BooleanReturn",
				"com.magiology.util.interf.Calculable",
				"com.magiology.util.interf.IObjectFactory",
				"com.magiology.util.interf.ObjectConverter",
				"com.magiology.util.interf.ObjectConverterThrows",
				"com.magiology.util.interf.ObjectProcessor",
				"com.magiology.util.interf.ObjectReturn",
				"com.magiology.util.interf.ObjectSimpleCallback",
				"com.magiology.util.interf.ObjectSimpleProcessor",
				"com.magiology.util.m_extensions.BlockContainerM",
				"com.magiology.util.m_extensions.BlockM",
				"com.magiology.util.m_extensions.BlockPosM",
				"com.magiology.util.m_extensions.EntityCreatureM",
				"com.magiology.util.m_extensions.GuiContainerM",
				"com.magiology.util.m_extensions.ResourceLocationM",
				"com.magiology.util.m_extensions.TileEntityM",
				"com.magiology.util.m_extensions.TileEntityMTickable",
				"com.magiology.util.m_extensions.TileEntitySpecialRendererM",
				"com.magiology.util.objs.AngularVec3",
				"com.magiology.util.objs.BlockPosM",
				"com.magiology.util.objs.ColorF",
				"com.magiology.util.objs.DatabaseStorage",
				"com.magiology.util.objs.DoubleObject",
				"com.magiology.util.objs.EnhancedRobot",
				"com.magiology.util.objs.LinearAnimation",
				"com.magiology.util.objs.NBTUtil",
				"com.magiology.util.objs.ObjectHolder",
				"com.magiology.util.objs.physics.PhysicsFloat",
				"com.magiology.util.objs.physics.PhysicsVec3F",
				"com.magiology.util.objs.physics.real.AbstractRealPhysicsVec3F",
				"com.magiology.util.objs.physics.real.entitymodel.Colideable",
				"com.magiology.util.objs.physics.real.GeometryUtil",
				"com.magiology.util.objs.physics.real.RealPhysicsMesh",
				"com.magiology.util.objs.physics.real.RealPhysicsVec3F",
				"com.magiology.util.objs.QuadUV",
				"com.magiology.util.objs.QuadUVGenerator",
				"com.magiology.util.objs.RegistrableDatabaseStorage",
				"com.magiology.util.objs.Vec2FM",
				"com.magiology.util.objs.Vec2i",
				"com.magiology.util.objs.Vec3M",
				"com.magiology.util.statics.AxisAlignedBBFloat",
				"com.magiology.util.statics.class_manager.ClassFinder",
				"com.magiology.util.statics.class_manager.ClassList",
				"com.magiology.util.statics.class_manager.ListCompiler",
				"com.magiology.util.statics.CollectionConverter",
				"com.magiology.util.statics.FileUtil",
				"com.magiology.util.statics.math.ArrayMath",
				"com.magiology.util.statics.math.MathUtil",
				"com.magiology.util.statics.math.MatrixUtil",
				"com.magiology.util.statics.math.PartialTicksUtil",
				"com.magiology.util.statics.OpenGLM",
				"com.magiology.util.statics.PhysicsUtil",
				"com.magiology.util.statics.PrintUtil",
				"com.magiology.util.statics.RandUtil",
				"com.magiology.util.statics.RayTracer",
				"com.magiology.util.statics.UtilC",
				"com.magiology.util.statics.UtilM"
			};
	
	
	static final Map<Class<?>, List<Class<?>>> implementations=new HashMap<>(),directImplementations=new HashMap<>();
	
	static final List<Class<?>> allClasses=new ArrayList<>();
	private static List<Class<?>> classesToGet=new ArrayList<>();
	
	static List<Class<?>> getClassesToSort(){		
		return classesToGet;
	}
	/**
	 * testedClass extends givenClass = true, testedClass extends someClass extends givenClass = false
	 * @return
	 */
	public static <T> List<Class<T>> getDirectImplementations(Class<T>... classes){
		List<Class<T>> list=new ArrayList<>();
		
		boolean added=false;
		for(Class<? extends T> cl:classes){
			if(getDirectImplementations().get(cl)==null){
				classesToGet.add(cl);
				added=true;
			}
		}
		if(added)ClassFinder.load();
		
		PrintUtil.println(added);
		PrintUtil.println(implementations);
		PrintUtil.println(allClasses);
		
		for(Class<T> cl:classes)getDirectImplementations().get(cl).forEach(c->list.add((Class<T>)c));
		return list;
	}
	public static Map<Class<?>, List<Class<?>>> getDirectImplementations(){
		ClassFinder.load();
		return directImplementations;
	}
	
	public static <T> List<Class<T>> getImplementations(Class<? extends T>... classes){
		List<Class<T>> list=new ArrayList<>();
		try{
			boolean added=false;
			for(Class<? extends T> cl:classes){
				if(getImplementations().get(cl)==null){
					classesToGet.add(cl);
					added=true;
				}
			}
			if(added)ClassFinder.load();
			
			for(Class<? extends T> cl:classes){
				getImplementations().get(cl).forEach(c->list.add((Class<T>)c));
			}
		}catch(Exception e){
			PrintUtil.println((Object)classes);
			throw e;
		}
		return list;
	}
	public static Map<Class<?>, List<Class<?>>> getImplementations(){
		return implementations;
	}
	
	public static List<Class<?>> getLoadedclasses(){
		return allClasses;
	}
}
