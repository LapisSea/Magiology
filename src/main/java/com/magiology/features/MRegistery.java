package com.magiology.features;

import com.magiology.features.SimpleItems.MistyPowder;
import com.magiology.features.dimension_stabiliser.TileEntityDimensionStabiliser;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.mc_objects.MBlocks;
import com.magiology.mc_objects.particles.Particles;
import com.magiology.util.interf.ObjectProcessor;
import com.magiology.util.m_extensions.TileEntityM;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MRegistery{
	
	public static void registerItems(ObjectProcessor<Item> register){
		register.process(new MistyPowder());
	}
	public static void registerBlocks(ObjectProcessor<Block> register){
		for(Block block:MBlocks.allBlocks())register.process(block);
		
	}
	public static void registerTileEntitys(ObjectProcessor<Class<? extends TileEntityM>> register){
		register.process(TileEntityDimensionStabiliser.class);
		
	}
	public static void registerEntitys(ObjectProcessor<EntityLivingBase> register){
		
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerParticles(ObjectProcessor<ParticleFactory> register){
		register.process(Particles.MIST_BUBBLE);
		register.process(Particles.CUBE);
		register.process(Particles.MISTY_ENERGY);
		register.process(Particles.MISTY_BUBBLE);
	}
	
}
