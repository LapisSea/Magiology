package com.magiology.forge.proxy;

import com.magiology.client.entity.EntityPenguinRenderer;
import com.magiology.client.rendering.item.SIRRegistry;
import com.magiology.core.MReference;
import com.magiology.core.TypeArgumentsUtils;
import com.magiology.core.registry.init.BlocksM;
import com.magiology.core.registry.init.ParticlesM;
import com.magiology.core.registry.init.ShadersM;
import com.magiology.mc_objects.entitys.EntityPenguin;
import com.magiology.util.m_extensions.BlockContainerM;
import com.magiology.util.m_extensions.BlockContainerM.MixedRender;
import com.magiology.util.objs.EnhancedRobot;
import com.magiology.util.objs.animation.AnimationBank;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilM;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{
	
	public static EnhancedRobot ROBOT;
	
	@Override
	public void loadModFiles(){
		AnimationBank.load();
	}
	
	@Override
	public void preInit(){
		EnhancedRobot robotH=null;
		try{
			robotH=new EnhancedRobot();
		}catch(Exception e){
		}
		ROBOT=robotH;
		ShadersM.reload();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityPenguin.class, EntityPenguinRenderer::new);
	}
	
	@Override
	public void init(){
		registerTileRendering();
		
		ParticlesM.get().register();
	}
	
	@Override
	public void postInit(){
		SIRRegistry.register();
	}
	
	@Override
	public void onExit(){
		
	}
	
	private void registerTileRendering(){
		
		for(BlockContainerM block : BlocksM.get().getByExtension(BlockContainerM.class)){
			
			EnumBlockRenderType type;
			try{
				type=block.getRenderType(block.getDefaultState());
			}catch(Exception e){
				type=EnumBlockRenderType.INVISIBLE;
			}
			boolean mixed=block instanceof MixedRender;
			if(type!=EnumBlockRenderType.INVISIBLE){
				if(mixed) ((MixedRender<BlockContainerM>)block).registerModel(block);
				else if(type==EnumBlockRenderType.ENTITYBLOCK_ANIMATED) autoTESR(block);
				else autoJsonModel(block);
			}
		}
	}
	
	public static <T extends TileEntity> void autoTESR(BlockContainerM<T> block){
		try{
			Class blockClass=block.getClass();
			Class<T> tileClass=TypeArgumentsUtils.getFirstTypeArgument(BlockContainerM.class, blockClass);
			if(tileClass==null) throw new IllegalArgumentException(UtilM.toString(new Object[]{
				blockClass,
				"has no generic argument. Please match generic class argument to the",
				"TileEntity that the container uses! (or a super type of single block is using",
				"multiple TileEntity classes)"
			}));
			
			String rendererPath="com.magiology.client.rendering.tile."+tileClass.getSimpleName()+"Renderer";
			
			ClientRegistry.bindTileEntitySpecialRenderer(tileClass, (TileEntitySpecialRenderer<T>)Class.forName(rendererPath).newInstance());
		}catch(Exception e){
			e.printStackTrace();
			LogUtil.printlnEr("Could not auto bind TileEntitySpecialRenderer for", block);
		}
	}
	
	public static void autoJsonModel(BlockContainerM block){
		OpenGLM.getRI().getItemModelMesher()
			   .register(block.toItem(), 0, new ModelResourceLocation(MReference.MODID+":"+block.getUnlocalizedName().substring(5), "inventory"));
	}
	
}

