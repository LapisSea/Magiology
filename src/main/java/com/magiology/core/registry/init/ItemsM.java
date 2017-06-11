package com.magiology.core.registry.init;

import com.magiology.core.registry.imp.AutoReferencedRegistry;
import com.magiology.mc_objects.items.ItemJetpack;
import com.magiology.mc_objects.items.ItemMatterJumper;
import com.magiology.mc_objects.items.SimpleItems.ItemMistyPowder;
import com.magiology.util.m_extensions.ItemM;
import com.magiology.util.statics.UtilM;
import net.minecraftforge.fml.common.registry.GameRegistry;

//<GEN:	IMPORTS START>
import com.magiology.mc_objects.items.ItemJetpack;
import com.magiology.mc_objects.items.ItemMatterJumper;
import com.magiology.mc_objects.items.SimpleItems.ItemMistyPowder;
//<GEN:	IMPORTS END>

public class ItemsM extends AutoReferencedRegistry<ItemM>{
	
	private static final ItemsM instance=new ItemsM();
	
	public static ItemsM get(){
		return instance;
	}
	
	//<GEN:	REFERENCE START>
	public static ItemJetpack      JETPACK;
	public static ItemMatterJumper MATTER_JUMPER;
	public static ItemMistyPowder  MISTY_POWDER;
	//<GEN:	REFERENCE END>
	
	private ItemsM(){
		super(ItemM.class);
	}
	
	@Override
	public void registerObj(ItemM obj){
		String name=UtilM.classNameToMcName(obj.getClass());
		obj.setRegistryName(name);
		obj.setUnlocalizedName(name);
		GameRegistry.register(obj);
	}
	
	@Override
	protected void init(){
		//<GEN:	INIT START>
		add(JETPACK      =new ItemJetpack());
		add(MATTER_JUMPER=new ItemMatterJumper());
		add(MISTY_POWDER =new ItemMistyPowder());
		//<GEN:	INIT END>
	}
}
