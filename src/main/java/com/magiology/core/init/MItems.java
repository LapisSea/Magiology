package com.magiology.core.init;

import com.magiology.core.MReference;
import com.magiology.mcobjects.items.FireHammer;
import com.magiology.mcobjects.items.IPowerSidenessInstructor;
import com.magiology.mcobjects.items.NetworkPointer;
import com.magiology.mcobjects.items.PowerCounter;
import com.magiology.mcobjects.items.ProgramContainer;
import com.magiology.mcobjects.items.TheHand;
import com.magiology.mcobjects.items.armor.CyborgWingsFromTheBlackFireItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MItems{

	//normal Item references
	public static Item
		powerCounter,fireHammer,theHand,iPowerSidenessInstructor,
		networkPointer,commandContainer;
	
	//armor Item/Material references
	public static ArmorMaterial WingsFTBFA;
	public static Item		    WingsFTBFI;
	
	private static ArmorMaterial add(String name, int durability, int[] reductionAmounts, int enchantability){
		return EnumHelper.addArmorMaterial(name, name, durability, reductionAmounts, enchantability);
	}
	
	public static void armorRegister(){
		WingsFTBFA=add("DFTheWings", 1, new int[]{2, 6, 5, 2}, 30);
		
		WingsFTBFI=init(new CyborgWingsFromTheBlackFireItem("WingsFTBF", WingsFTBFA, "WingsFTBF", 1,MCreativeTabs.Whwmmt_core));
	}

	private static <T extends Item>T init(T item){
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
		return item;
	}public static void initRenders(){
//		bindItemWRender(Item.getItemFromBlock(MBlocks.FirePipe), new ItemRendererFirePipe());
//		bindItemWRender(PowerCounter, new ItemRendererPowerCounter());
//		bindItemWRender(pants_42I, new ItemRendererPants42());
//		bindItemWRender(helmet_42I, new ItemRendererHelmet42());
//		bindItemWRender(TheHand, TheHandHandler.getRenderer());
//		bindItemWRender(Magiology., new ItemRenderer);
	}
	
	public static void preInit(){
		register();
		armorRegister();
	}
	
	public static void register(){
		//Random items
		powerCounter=init(new PowerCounter().setTextureName(MReference.MODID + ":" + "PowerCounter").setUnlocalizedName("PowerCounter").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		iPowerSidenessInstructor=init(new IPowerSidenessInstructor().setTextureName(MReference.MODID + ":" + "PowerCounter").setUnlocalizedName("IPowerSidenessInstructor").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		fireHammer=init(new FireHammer().setTextureName(MReference.MODID + ":" + "FireHammer").setUnlocalizedName("FireHammer").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		theHand=init(new TheHand().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("TheHand").setMaxStackSize(1));
		networkPointer=init(new NetworkPointer().setUnlocalizedName("NetworkPointer").setCreativeTab(MCreativeTabs.Whwmmt_core).setMaxStackSize(1));
		commandContainer=init(new ProgramContainer());
	}

//	public static void setGenericUpgradeRenderer(Item item){
//		if(item instanceof GenericItemUpgrade)MinecraftForgeClient.registerItemRenderer(item, new ItemRendererGenericUpgrade());
//		else PrintUtil.printlnEr("Item: "+item.getUnlocalizedName()+" cannot be registered as a GenericUpgrade renderer!\n");
//	}

//	public static void bindItemWRender(Item item, IItemRenderer renderer){
//		MinecraftForgeClient.registerItemRenderer(item,renderer);
//	}
}
