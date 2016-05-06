package com.magiology.mcobjects.items.armor;

import java.util.List;

import com.magiology.client.render.models.ModelWingsFromTheBlackFire;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Wings extends ItemArmor{


	public String textureName;
	
	public Wings(String unlocalizedName, ArmorMaterial material, EntityEquipmentSlot type,CreativeTabs creativeTab){
		super(material, 0, type);
		this.textureName = unlocalizedName;
		this.setUnlocalizedName(unlocalizedName);
//		this.setTextureName(MReference.MODID + ":" + unlocalizedName);
		this.setCreativeTab(creativeTab);
		this.setMaxDamage(25);
	}
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		super.addInformation(itemStack, player, list, par4);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot armorSlot, ModelBiped _default){
		return new ModelWingsFromTheBlackFire();
	}
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
		return null;
	}
	
	@Override
	public void onArmorTick(World world,EntityPlayer player,ItemStack TheDFWings){
		
	}
}
