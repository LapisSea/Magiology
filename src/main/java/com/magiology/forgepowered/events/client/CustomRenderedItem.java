package com.magiology.forgepowered.events.client;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;

public interface CustomRenderedItem{
	public interface CustomRenderedItemRenderer{
		public void renderItem(ItemStack stack, TransformType position);
		public boolean shouldRenderSpecial(ItemStack stack, TransformType position);
	}
	CustomRenderedItemRenderer getRenderer(ItemStack stack);
}
