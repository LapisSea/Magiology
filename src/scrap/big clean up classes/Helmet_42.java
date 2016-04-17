package com.magiology.mcobjects.items.armor;

import java.util.List;

import com.magiology.client.render.models.ModelHelmet42;
import com.magiology.mcobjects.effect.EntityFollowingBubleFX;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.mcobjects.items.upgrades.skeleton.UpgradeableArmor;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.NBTUtil;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Helmet_42 extends UpgradeableArmor{
	
	public String textureName;
	
	
	public Helmet_42(String unlocalizedName, ArmorMaterial material, String textureName, int type,CreativeTabs creativeTab){
		super(material, 0, type);
		this.textureName = textureName;
		this.setUnlocalizedName(unlocalizedName);
//		this.setTextureName(MReference.MODID + ":" + unlocalizedName);
		this.setCreativeTab(creativeTab);
		this.setMaxDamage(25);
		initUpgrade(Container.Helmet42);
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type){
		return null;
	}
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		super.addInformation(itemStack, player, list, par4);
	}
	
	@Override
	public void onArmorTick(World world,EntityPlayer player,ItemStack helmet42){
		if(helmet42.hasTagCompound()){
			if(!player.isInvisible()){
				double[] roundXYZ=UtilM.createBallXYZ(1,true);
				roundXYZ[1]-=0.5;
				roundXYZ[4]-=0.5;
				EntityFollowingBubleFX part=new EntityFollowingBubleFX(world, roundXYZ[0]+player.posX, roundXYZ[1]+player.posY, roundXYZ[2]+player.posZ, UtilM.CRandF(0.01), UtilM.CRandF(0.01), UtilM.CRandF(0.01), player, 0, roundXYZ[3], roundXYZ[4], roundXYZ[5], 300, 3+(player.isSneaking()?10:0), UtilM.RF(), UtilM.RF(), UtilM.RF(), 1-(player==U.getMC().thePlayer?(U.getMC().gameSettings.thirdPersonView==0?0.95:0):0));
				part.noClip=false;
				part.isChangingPos=false;
				UtilM.spawnEntityFX(part);
			}
		}else NBTUtil.createNBT(helmet42);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, int armorSlot){
		ModelBiped armorModel = null; 
		if(stack != null&&stack.getItem() instanceof Helmet_42)armorModel=new ModelHelmet42();
		return armorModel;
	}
}
