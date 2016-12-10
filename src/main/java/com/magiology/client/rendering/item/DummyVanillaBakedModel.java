package com.magiology.client.rendering.item;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class DummyVanillaBakedModel implements IBakedModel{
	
	private ItemCameraTransforms transforms;
	
	public DummyVanillaBakedModel(ItemCameraTransforms transforms){
		this.transforms=transforms;
	}
	
	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
		return Collections.emptyList();
	}
	
	@Override
	public boolean isAmbientOcclusion(){
		return false;
	}
	
	@Override
	public boolean isGui3d(){
		return true;
	}
	
	@Override
	public boolean isBuiltInRenderer(){
		return true;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture(){
		return null;
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms(){
		return transforms;
	}
	
	@Override
	public ItemOverrideList getOverrides(){
		return new ItemOverrideList(Collections.<ItemOverride>emptyList());
	}
}
