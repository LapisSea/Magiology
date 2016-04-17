package com.magiology.core.coremod.corehooks;

import com.magiology.forgepowered.events.client.CustomRenderedItem;
import com.magiology.forgepowered.events.client.CustomRenderedItem.CustomRenderedItemRenderer;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHooksM extends CommonHooksM{
	private static TransformType cameraTransformType;
	public static void addTransformType(TransformType cameraTransformType1){
		cameraTransformType=cameraTransformType1;
	}
	public static boolean renderItem(ItemStack stack){
		Item item=stack.getItem();
		if(item instanceof CustomRenderedItem){
			
			CustomRenderedItemRenderer renderer=((CustomRenderedItem)item).getRenderer(stack);
			
			if(renderer.shouldRenderSpecial(stack,cameraTransformType)){
				renderer.renderItem(stack,cameraTransformType);
				cameraTransformType=null;
				return false;
			}
			
		}
		return true;
	}
}
