package com.magiology.core.init;

import com.magiology.core.MReference;
import com.magiology.mcobjects.items.FireHammer;
import com.magiology.mcobjects.items.IPowerSideInstructor;
import com.magiology.mcobjects.items.NetworkPointer;
import com.magiology.mcobjects.items.PowerCounter;
import com.magiology.mcobjects.items.ProgramContainer;
import com.magiology.mcobjects.items.TheHand;
import com.magiology.mcobjects.items.armor.Wings;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
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
		return EnumHelper.addArmorMaterial(name, name, durability, reductionAmounts, enchantability, SoundEvents.item_armor_equip_iron);
	}
	
	public static void armorRegister(){
		WingsFTBFA=add("DFTheWings", 1, new int[]{2, 6, 5, 2}, 30);
		
		WingsFTBFI=init(new Wings("WingsFTBF", WingsFTBFA, EntityEquipmentSlot.CHEST,MCreativeTabs.Whwmmt_core));
	}

	private static <T extends Item>T init(T item){
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
		return item;
	}
	
	public static void preInit(){
		register();
		armorRegister();
	}
	
	public static void register(){
		//Random items
		powerCounter=init(new PowerCounter().setTextureName(MReference.MODID + ":" + "PowerCounter").setUnlocalizedName("PowerCounter").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		iPowerSidenessInstructor=init(new IPowerSideInstructor().setTextureName(MReference.MODID + ":" + "PowerCounter").setUnlocalizedName("IPowerSidenessInstructor").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		fireHammer=init(new FireHammer().setTextureName(MReference.MODID + ":" + "FireHammer").setUnlocalizedName("FireHammer").setCreativeTab(MCreativeTabs.Whwmmt_power).setMaxStackSize(1));
		theHand=init(new TheHand().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("TheHand").setMaxStackSize(1));
		networkPointer=init(new NetworkPointer().setUnlocalizedName("NetworkPointer").setCreativeTab(MCreativeTabs.Whwmmt_core).setMaxStackSize(1));
		commandContainer=init(new ProgramContainer());
	}
}
