package com.magiology.forgepowered.events.client;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface CustomRenderedItem{
	@SideOnly(Side.CLIENT)
	public interface CustomRenderedItemRenderer{
		public void renderItem(ItemStack stack, TransformType position, boolean leftHand);
		public boolean shouldRenderSpecial(ItemStack stack, TransformType position, boolean leftHand);
	}
	@SideOnly(Side.CLIENT)
	CustomRenderedItemRenderer getRenderer(ItemStack stack);
}
