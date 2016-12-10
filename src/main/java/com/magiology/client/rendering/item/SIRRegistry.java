package com.magiology.client.rendering.item;

import java.util.HashMap;
import java.util.Map;

import com.magiology.core.registry.init.ItemsM;
import com.magiology.util.interf.Renderable;
import com.magiology.util.interf.SpecialRender;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilC;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Special Item Rrenderer Registry
 * @author LapisSea
 */
@SideOnly(Side.CLIENT)
public class SIRRegistry{
	
	private static Map<ResourceLocation,RegistryObject> registry=new HashMap<>();
	
	public interface IItemRenderer extends Renderable<ItemStack>{//di**s out for IItemRenderer
		
		@Override
		void render(ItemStack stack);
	}
	
	private static class RegistryObject{
		
		private IItemRenderer rednerer;
		//More objects
		
		public RegistryObject(IItemRenderer rednerer){
			this.rednerer=rednerer;
		}
		
	}
	
	private static class DummyMesh implements ItemMeshDefinition{
		
		private ModelResourceLocation fallback;
		
		public DummyMesh(ResourceLocation loc){
			fallback=new ModelResourceLocation(loc, "inventory");
		}
		
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack){
			return fallback;
		}
	}
	
	public static <T extends Item&SpecialRender> void registerItem(T item){
		try{
			ResourceLocation loc=item.getRegistryName();
			registry.put(new ResourceLocation(loc.getResourceDomain(), "models/item/"+loc.getResourcePath()), new RegistryObject((IItemRenderer)item.getRenderer()));
			
			UtilC.getRI().getItemModelMesher().register(item, new DummyMesh(loc));
		}catch(Exception e){
			for(int i=0;i<ItemsM.get().getDatabase().length;i++){
				LogUtil.println(ItemsM.get().getDatabase()[i].getRegistryName());
			}
			throw e;
		}
	}
	
	public static <T extends Item&SpecialRender> void register(){
		MagiologyTEISR.wrapp();
		ModelLoaderRegistry.registerLoader(new ModelLoaderM());
		for(SpecialRender item:ItemsM.get().getByExtension(SpecialRender.class)){
			T itemRender=(T)item;
			if(itemRender.getRenderer()instanceof IItemRenderer){
				registerItem(itemRender);
			}
		}
	}
	
	public static boolean shouldRender(ItemStack stack){
		if(stack==null) return false;
		return stack.getItem() instanceof SpecialRender;
	}
	
	public static void renderStack(ItemStack stack){
		Renderable renderer=((SpecialRender)stack.getItem()).getRenderer();
		((IItemRenderer)renderer).render(stack);
	}
	
	public static boolean isIndexed(ResourceLocation location){
		return registry.containsKey(location);
	}
	
}
