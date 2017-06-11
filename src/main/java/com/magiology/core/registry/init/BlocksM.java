package com.magiology.core.registry.init;

import com.magiology.core.MReference;
import com.magiology.core.registry.imp.AutoReferencedRegistry;
import com.magiology.mc_objects.features.dimension_stabiliser.BlockDimensionStabiliser;
import com.magiology.mc_objects.features.machines.shaker.BlockShaker;
import com.magiology.mc_objects.features.neuro.BlockNeuroController;
import com.magiology.mc_objects.features.neuro.BlockNeuroDuct;
import com.magiology.mc_objects.features.screen.BlockScreen;
import com.magiology.util.m_extensions.BlockM;
import com.magiology.util.m_extensions.ItemBlockM;
import com.magiology.util.statics.UtilM;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

//<GEN:	IMPORTS START>
//<GEN:	IMPORTS END>

public class BlocksM extends AutoReferencedRegistry<BlockM>{
	
	private static final BlocksM instance=new BlocksM();
	
	public static BlocksM get(){
		return instance;
	}
	
	//<GEN:	REFERENCE START>
	public static BlockDimensionStabiliser DIMENSION_STABILISER;
	public static BlockShaker              SHAKER;
	public static BlockNeuroController     NEURO_CONTROLLER;
	public static BlockNeuroDuct           NEURO_DUCT;
	public static BlockScreen              SCREEN;
	//<GEN:	REFERENCE END>
	
	private BlocksM(){
		super(BlockM.class);
	}
	
	@Override
	public void registerObj(BlockM block){
		String name=UtilM.standardizeName(UtilM.removeMcObjectEnd(block.getClass().getSimpleName()));
		block.setRegistryName(MReference.MODID, name);
		block.setUnlocalizedName(name);
		ItemBlock ib=new ItemBlockM(block);
		GameRegistry.register(block);
		GameRegistry.register(ib.setRegistryName(MReference.MODID, name));
	}
	
	@Override
	protected void init(){
		//<GEN:	INIT START>
		add(DIMENSION_STABILISER=new BlockDimensionStabiliser());
		add(SHAKER              =new BlockShaker());
		add(NEURO_CONTROLLER    =new BlockNeuroController());
		add(NEURO_DUCT          =new BlockNeuroDuct());
		add(SCREEN              =new BlockScreen());
		//<GEN:	INIT END>
	}
}
