package com.magiology.util.statics.classload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.util.interf.Calculable;
import com.magiology.util.m_extensions.TileEntityM;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassList{
	
	static final String[]
			classes={
				"com.magiology.client.Renderer",
				"com.magiology.client.rendering.tile_render.DummyTileEntityRenderer",
				"com.magiology.client.shaders.effects.PositionAwareEffect",
				"com.magiology.client.shaders.effects.SoftEffectsShader",
				"com.magiology.client.shaders.programs.SepiaShader",
				"com.magiology.client.shaders.programs.Template",
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
				"com.magiology.forge_powered.events.RenderEvents",
				"com.magiology.forge_powered.events.TickEvents",
				"com.magiology.forge_powered.proxy.ClientProxy",
				"com.magiology.forge_powered.proxy.CommonProxy",
				"com.magiology.forge_powered.proxy.ServerProxy",
				"com.magiology.handlers.particle.IParticle",
				"com.magiology.handlers.particle.ParticleFactory",
				"com.magiology.handlers.particle.ParticleHandler",
				"com.magiology.handlers.particle.ParticleM",
				"com.magiology.handlers.particle.Particles",
				"com.magiology.mc_objects.blocks.DummyBlock",
				"com.magiology.mc_objects.particles.ParticleCubeFactory",
				"com.magiology.mc_objects.particles.ParticleMistBubble",
				"com.magiology.mc_objects.particles.ParticleMistBubbleFactory",
				"com.magiology.mc_objects.tileentitys.DummyTileEntity",
				"com.magiology.SoundM",
				"com.magiology.util.interf.BooleanReturn",
				"com.magiology.util.interf.Calculable",
				"com.magiology.util.interf.IObjectFactory",
				"com.magiology.util.interf.ObjectConverter",
				"com.magiology.util.interf.ObjectProcessor",
				"com.magiology.util.interf.ObjectReturn",
				"com.magiology.util.m_extensions.BlockContainerM",
				"com.magiology.util.m_extensions.BlockM",
				"com.magiology.util.m_extensions.BlockPosM",
				"com.magiology.util.m_extensions.GuiContainerM",
				"com.magiology.util.m_extensions.TileEntityM",
				"com.magiology.util.m_extensions.TileEntitySpecialRendererM",
				"com.magiology.util.objs.AngularVec3",
				"com.magiology.util.objs.BlockPosM",
				"com.magiology.util.objs.ColorF",
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
				"com.magiology.util.objs.Vec2FM",
				"com.magiology.util.objs.Vec2i",
				"com.magiology.util.objs.Vec3M",
				"com.magiology.util.statics.AxisAlignedBBFloat",
				"com.magiology.util.statics.classload.ClassFinder",
				"com.magiology.util.statics.classload.ClassList",
				"com.magiology.util.statics.CollectionConverter",
				"com.magiology.util.statics.FileUtil",
				"com.magiology.util.statics.LogUtil",
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
	
	protected static boolean error=false;
	
	static final Map<Class<?>, List<Class<?>>> implementations=new HashMap<>(),directImplementations=new HashMap<>();
	
	static final List<Class<?>> loadedClasses=new ArrayList<>();
	
	static List<Class<?>> getClassesToSort(){
		List<Class<?>> classes=new ArrayList<>();
		
		classes.add(IClassTransformer.class);
		classes.add(Calculable.class);
		classes.add(TileEntityM.class);
		classes.add(ShaderProgram.class);
		
		return classes;
	}
	public static <T> List<Class<T>> getDirectImplementations(Class<T> clazz){
		List<Class<T>> list=new ArrayList<>();
		getDirectImplementations().get(clazz).forEach(c->list.add((Class<T>)c));
		return list;
	}
	public static Map<Class<?>, List<Class<?>>> getDirectImplementations(){
		ClassFinder.load();
		return directImplementations;
	}
	
	public static <T> List<Class<T>> getImplementations(Class<T> clazz){
		List<Class<T>> list=new ArrayList<>();
		getImplementations().get(clazz).forEach(c->list.add((Class<T>)c));
		return list;
	}
	public static Map<Class<?>, List<Class<?>>> getImplementations(){
		ClassFinder.load();
		return implementations;
	}
	
	public static List<Class<?>> getLoadedclasses(){
		ClassFinder.load();
		return loadedClasses;
	}
}
