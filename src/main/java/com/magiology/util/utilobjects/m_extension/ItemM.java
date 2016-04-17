package com.magiology.util.utilobjects.m_extension;

import java.util.HashMap;
import java.util.Map;

import com.magiology.core.MReference;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemM extends Item{

	// #fucka youuu json models!
	public static Map<Item, ModelResourceLocation> modelsInit=new HashMap<Item, ModelResourceLocation>();

	public static void registerItemModels(){
		for(Item item:modelsInit.keySet()){
			try{
				Get.Render.RI().getItemModelMesher().register(item, 0, modelsInit.get(item));
				((ItemM)item).initModel();
			}catch(Exception e){
				PrintUtil.println("failed!", item, Get.Render.RI());
				e.printStackTrace();
				U.exit(404);
			}
		}
		modelsInit.clear();
	}

	@SideOnly(Side.CLIENT)
	public void initModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(MReference.MODID+":simpletextureditem", "inventory"));
	}

	public ItemM setBlockTextureName(){
		return setTextureName(MReference.MODID+":"+getUnlocalizedName().substring(5));
	}

	public ItemM setTextureName(String name){
		modelsInit.put(this, new ModelResourceLocation(name, "inventory"));
		return this;
	}
}
