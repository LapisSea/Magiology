package com.magiology.forge.proxy;

import com.magiology.client.entity.EntityPenguinRenderer;
import com.magiology.client.rendering.item.SIRRegistry;
import com.magiology.core.MReference;
import com.magiology.core.registry.init.BlocksM;
import com.magiology.core.registry.init.ParticlesM;
import com.magiology.core.registry.init.ShadersM;
import com.magiology.forge.events.RenderEvents;
import com.magiology.mc_objects.entitys.EntityPenguin;
import com.magiology.util.objs.EnhancedRobot;
import com.magiology.util.objs.animation.AnimationBank;
import com.magiology.util.statics.OpenGLM;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.common.MinecraftForge;
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
		}catch(Exception e){}
		ROBOT=robotH;
		ShadersM.load();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityPenguin.class, (manager)->new EntityPenguinRenderer(manager));
	}
	
	@Override
	public void init(){
		MinecraftForge.EVENT_BUS.register(new RenderEvents());
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
		ItemModelMesher mesher=OpenGLM.getRI().getItemModelMesher();
		
		for(ITileEntityProvider i:BlocksM.get().getByExtension(ITileEntityProvider.class)){
			Block block=(Block)i;
			EnumBlockRenderType type=EnumBlockRenderType.INVISIBLE;
			try{
				type=block.getRenderType(block.getDefaultState());
			}catch(Exception e){}
			if(type!=EnumBlockRenderType.INVISIBLE){
				if(type==EnumBlockRenderType.ENTITYBLOCK_ANIMATED){
					
					try{
						Class tileClass=i.createNewTileEntity(null, 0).getClass();
						Class rederer=Class.forName("com.magiology.client.rendering.tile."+tileClass.getSimpleName()+"Renderer");
						ClientRegistry.bindTileEntitySpecialRenderer(tileClass,(TileEntitySpecialRenderer)rederer.newInstance());
					}catch(Exception e){}
				}
				else mesher.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(MReference.MODID+":"+block.getUnlocalizedName().substring(5), "inventory"));
			}
		}
	}
	
}

