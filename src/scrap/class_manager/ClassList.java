package com.magiology.core.class_manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.magiology.util.statics.LogUtil;

public class ClassList{
	
	static String[] classes;
	static{
		new ClassList();
	}
	public ClassList(){
		classes=new String[]{
			"com.magiology.client.entity.EntityPenguinRenderer",
			"com.magiology.client.post.PostProcessFX",
			"com.magiology.client.renderers.AdvancedRenderer",
			"com.magiology.client.renderers.FastNormalRenderer",
			"com.magiology.client.renderers.GenericModels",
			"com.magiology.client.renderers.Renderer",
			"com.magiology.client.rendering.highlight.BlockHighlightRenderer",
			"com.magiology.client.rendering.highlight.extras.MultiBlockBoxHighlight",
			"com.magiology.client.rendering.highlight.types.BasicBlockHighlightRenderer",
			"com.magiology.client.rendering.highlight.types.MultiHighlightRenderer",
			"com.magiology.client.rendering.highlight.types.StateDependantBlockHighlightRenderer",
			"com.magiology.client.rendering.item.DummyVanillaBakedModel",
			"com.magiology.client.rendering.item.ItemRendererModelDummy",
			"com.magiology.client.rendering.item.MagiologyTEISR",
			"com.magiology.client.rendering.item.ModelLoaderM",
			"com.magiology.client.rendering.item.renderers.ItemMatterJumperRenderer",
			"com.magiology.client.rendering.item.SIRRegistry",
			"com.magiology.client.rendering.RandomAnimation",
			"com.magiology.client.rendering.ShaderMultiTransformModel",
			"com.magiology.client.rendering.tile.DummyTileEntityRenderer",
			"com.magiology.client.rendering.tile.TileEntityScreenRenderer",
			"com.magiology.client.shaders.effects.PositionAwareEffect",
			"com.magiology.client.shaders.effects.SoftEffectsShader",
			"com.magiology.client.shaders.programs.InvisibleEffect",
			"com.magiology.client.shaders.programs.MatterJumperShader",
			"com.magiology.client.shaders.programs.MultiTransformShader",
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
			"com.magiology.core.class_manager.ClassFinder",
			"com.magiology.core.class_manager.ClassList",
			"com.magiology.core.class_manager.ListCompiler",
			"com.magiology.core.CompatibilityChecker",
			"com.magiology.core.Config",
			"com.magiology.core.Magiology",
			"com.magiology.core.MReference",
			"com.magiology.cross_mod.ExtensionLoader",
			"com.magiology.cross_mod.jei.BasicRecepieHandler",
			"com.magiology.cross_mod.jei.JeiUidsM",
			"com.magiology.cross_mod.jei.MagiologyPlugin_JEI",
			"com.magiology.cross_mod.jei.shaker.ShakerCategory",
			"com.magiology.cross_mod.jei.shaker.ShakingRecipe",
			"com.magiology.cross_mod.jei.shaker.ShakingRecipeHandler",
			"com.magiology.cross_mod.jei.shaker.ShakingRecipeMaker",
			"com.magiology.cross_mod.ModChecker",
			"com.magiology.DevOnly",
			"com.magiology.forge.events.ClientEvents",
			"com.magiology.forge.events.EntityEvents",
			"com.magiology.forge.events.RenderEvents",
			"com.magiology.forge.events.TickEvents",
			"com.magiology.forge.events.WorldEvents",
			"com.magiology.forge.networking.NBTPacket",
			"com.magiology.forge.networking.PacketBufferM",
			"com.magiology.forge.networking.PacketM",
			"com.magiology.forge.networking.Packets",
			"com.magiology.forge.networking.SimpleNetworkWrapperM",
			"com.magiology.forge.networking.UpdateTileNBTPacket",
			"com.magiology.forge.proxy.ClientProxy",
			"com.magiology.forge.proxy.CommonProxy",
			"com.magiology.forge.proxy.ServerProxy",
			"com.magiology.handlers.frame_buff.TemporaryFrame",
			"com.magiology.handlers.frame_buff.TemporaryFrameBufferHandler",
			"com.magiology.handlers.particle.IParticle",
			"com.magiology.handlers.particle.ParticleFactory",
			"com.magiology.handlers.particle.ParticleHandler",
			"com.magiology.handlers.particle.ParticleM",
			"com.magiology.handlers.scripting.bridge.ForbiddenClass",
			"com.magiology.handlers.scripting.bridge.JavaHomeGetter",
			"com.magiology.handlers.scripting.Script",
			"com.magiology.handlers.scripting.ScriptLog",
			"com.magiology.handlers.scripting.ScriptManager",
			"com.magiology.handlers.scripting.ScriptResult",
			"com.magiology.handlers.scripting.ScriptWrapper",
			"com.magiology.handlers.scripting.script_types.JsScript",
			"com.magiology.handlers.scripting.script_types.RenderNUpdateScript",
			"com.magiology.handlers.scripting.script_types.RenderScript",
			"com.magiology.handlers.TileEntityOneBlockStructure",
			"com.magiology.io.IOManager",
			"com.magiology.mc_objects.BlockRegistry",
			"com.magiology.mc_objects.BlockStates",
			"com.magiology.mc_objects.entitys.ai.EntityAIWatchClosestM",
			"com.magiology.mc_objects.entitys.ai.PathNavigateSmart",
			"com.magiology.mc_objects.entitys.EntityPenguin",
			"com.magiology.mc_objects.features.dimension_stabiliser.BlockDimensionStabiliser",
			"com.magiology.mc_objects.features.dimension_stabiliser.TileEntityDimensionStabiliser",
			"com.magiology.mc_objects.features.machines.shaker.BlockShaker",
			"com.magiology.mc_objects.features.machines.shaker.TileEntityShaker",
			"com.magiology.mc_objects.features.neuro.BlockNeuroBase",
			"com.magiology.mc_objects.features.neuro.BlockNeuroController",
			"com.magiology.mc_objects.features.neuro.BlockNeuroDuct",
			"com.magiology.mc_objects.features.neuro.NeuroInterface",
			"com.magiology.mc_objects.features.neuro.NeuroPart",
			"com.magiology.mc_objects.features.neuro.TileEntityNeuroController",
			"com.magiology.mc_objects.features.neuro.TileEntityNeuroDuct",
			"com.magiology.mc_objects.features.screen.BlockScreen",
			"com.magiology.mc_objects.features.screen.TileEntityScreen",
			"com.magiology.mc_objects.ItemRegistry",
			"com.magiology.mc_objects.items.ItemJetpack",
			"com.magiology.mc_objects.items.ItemMatterJumper",
			"com.magiology.mc_objects.items.ItemOwnable",
			"com.magiology.mc_objects.items.SimpleItems",
			"com.magiology.mc_objects.particles.ParticleBubbleFactory",
			"com.magiology.mc_objects.particles.ParticleCubeFactory",
			"com.magiology.mc_objects.particles.ParticleMistBubbleFactory",
			"com.magiology.mc_objects.particles.ParticleMistyEnergyFactory",
			"com.magiology.mc_objects.particles.Particles",
			"com.magiology.mc_objects.recepies.ShakerRecepies",
			"com.magiology.mc_objects.TabRegistry",
			"com.magiology.SoundM",
			"com.magiology.util.interf.BooleanReturn",
			"com.magiology.util.interf.Calculable",
			"com.magiology.util.interf.IBlockBreakListener",
			"com.magiology.util.interf.IntReturn",
			"com.magiology.util.interf.IntToBooleanFunction",
			"com.magiology.util.interf.IObjectFactory",
			"com.magiology.util.interf.ISidedConnection",
			"com.magiology.util.interf.ObjectConverter",
			"com.magiology.util.interf.ObjectConverterThrows",
			"com.magiology.util.interf.ObjectProcessor",
			"com.magiology.util.interf.ObjectReturn",
			"com.magiology.util.interf.ObjectSimpleCallback",
			"com.magiology.util.interf.ObjectSimpleProcessor",
			"com.magiology.util.interf.Renderable",
			"com.magiology.util.interf.SpecialRender",
			"com.magiology.util.interf.Worldabale",
			"com.magiology.util.m_extensions.AxisAlignedBBM",
			"com.magiology.util.m_extensions.BlockContainerM",
			"com.magiology.util.m_extensions.BlockM",
			"com.magiology.util.m_extensions.BlockPosM",
			"com.magiology.util.m_extensions.EntityAgeableM",
			"com.magiology.util.m_extensions.EntityCreatureM",
			"com.magiology.util.m_extensions.GuiContainerM",
			"com.magiology.util.m_extensions.ItemBlockM",
			"com.magiology.util.m_extensions.ResourceLocationM",
			"com.magiology.util.m_extensions.TileEntityM",
			"com.magiology.util.m_extensions.TileEntityMTickable",
			"com.magiology.util.m_extensions.TileEntitySpecialRendererM",
			"com.magiology.util.objs.AngularVec3",
			"com.magiology.util.objs.animation.AbstractAnimation",
			"com.magiology.util.objs.animation.AnimationBank",
			"com.magiology.util.objs.animation.AnimationM",
			"com.magiology.util.objs.animation.AnimationMReference",
			"com.magiology.util.objs.animation.LinearAnimation",
			"com.magiology.util.objs.animation.MultipartAnimation",
			"com.magiology.util.objs.BlockSides",
			"com.magiology.util.objs.block_bounds.BasicBlockBounds",
			"com.magiology.util.objs.block_bounds.IBlockBounds",
			"com.magiology.util.objs.block_bounds.MultiBlockBounds",
			"com.magiology.util.objs.block_bounds.StateDependantBlockBounds",
			"com.magiology.util.objs.CalculableFloat",
			"com.magiology.util.objs.CalculableList",
			"com.magiology.util.objs.color.ColorM",
			"com.magiology.util.objs.color.ColorMFinal",
			"com.magiology.util.objs.color.ColorMRead",
			"com.magiology.util.objs.color.IColorM",
			"com.magiology.util.objs.data.DatabaseStorageArray",
			"com.magiology.util.objs.data.DatabaseStorageCollection",
			"com.magiology.util.objs.data.IDatabaseStorage",
			"com.magiology.util.objs.data.RegistrableDatabaseStorageArray",
			"com.magiology.util.objs.data.RegistrableDatabaseStorageCollection",
			"com.magiology.util.objs.data_parameter_wappers.DataParamBase",
			"com.magiology.util.objs.data_parameter_wappers.DataParamBlockPos",
			"com.magiology.util.objs.data_parameter_wappers.DataParamBoolean",
			"com.magiology.util.objs.data_parameter_wappers.DataParamByte",
			"com.magiology.util.objs.data_parameter_wappers.DataParamEnum",
			"com.magiology.util.objs.data_parameter_wappers.DataParamFloat",
			"com.magiology.util.objs.EnhancedRobot",
			"com.magiology.util.objs.LockabaleArrayList",
			"com.magiology.util.objs.LogBuffer",
			"com.magiology.util.objs.ModelRendererDummy",
			"com.magiology.util.objs.NBTUtil",
			"com.magiology.util.objs.ObjectHolder",
			"com.magiology.util.objs.PairM",
			"com.magiology.util.objs.physics.PhysicsFloat",
			"com.magiology.util.objs.physics.PhysicsVec3F",
			"com.magiology.util.objs.physics.real.AbstractRealPhysicsVec3F",
			"com.magiology.util.objs.physics.real.entitymodel.Colideable",
			"com.magiology.util.objs.physics.real.RealPhysicsMesh",
			"com.magiology.util.objs.physics.real.RealPhysicsVec3F",
			"com.magiology.util.objs.QuadUV",
			"com.magiology.util.objs.QuadUVGenerator",
			"com.magiology.util.objs.vec.IVec3M",
			"com.magiology.util.objs.vec.Vec2FM",
			"com.magiology.util.objs.vec.Vec2i",
			"com.magiology.util.objs.vec.Vec3M",
			"com.magiology.util.objs.vec.Vec3MFinal",
			"com.magiology.util.objs.vec.Vec3MRead",
			"com.magiology.util.statics.AxisAlignedBBFloat",
			"com.magiology.util.statics.CollectionBuilder",
			"com.magiology.util.statics.CollectionConverter",
			"com.magiology.util.statics.FileUtil",
			"com.magiology.util.statics.GeometryUtil",
			"com.magiology.util.statics.LogUtil",
			"com.magiology.util.statics.math.ArrayMath",
			"com.magiology.util.statics.math.MathUtil",
			"com.magiology.util.statics.math.MatrixUtil",
			"com.magiology.util.statics.math.PartialTicksUtil",
			"com.magiology.util.statics.OpenGLM",
			"com.magiology.util.statics.PhysicsUtil",
			"com.magiology.util.statics.RandUtil",
			"com.magiology.util.statics.Structure",
			"com.magiology.util.statics.TestingAnimationM",
			"com.magiology.util.statics.UtilC",
			"com.magiology.util.statics.UtilM"
			};
	}
	
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
		for(Class<? extends T> cl:classes)
			if(getDirectImplementations().get(cl)==null){
				classesToGet.add(cl);
				added=true;
			}
		if(added)ClassFinder.load();
		
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
			for(Class<? extends T> cl:classes)
				if(getImplementations().get(cl)==null){
					classesToGet.add(cl);
					added=true;
				}
			if(added)ClassFinder.load();
			
			for(Class<? extends T> cl:classes)
				getImplementations().get(cl).forEach(c->list.add((Class<T>)c));
		}catch(Exception e){
			LogUtil.println((Object)classes);
			throw e;
		}
		return list;
	}
	
	public static List<Class<?>> getByPackage(String packag2){
		return allClasses.stream().filter(cl->cl.getPackage().getName().equals(packag2)).collect(Collectors.toList());
	}
	
	public static Map<Class<?>, List<Class<?>>> getImplementations(){
		return implementations;
	}
	
	public static List<Class<?>> getLoadedclasses(){
		return allClasses;
	}
}
