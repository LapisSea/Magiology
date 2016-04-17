package com.magiology.mcobjects.items.armor;

import java.util.List;

import com.magiology.client.render.models.ModelWingsFromTheBlackFire;
import com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.NBTUtil;

import net.minecraft.block.BlockLiquid;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CyborgWingsFromTheBlackFireItem extends ItemArmor{


	public String textureName;
	
	public CyborgWingsFromTheBlackFireItem(String unlocalizedName, ArmorMaterial material, String textureName, EntityEquipmentSlot type,CreativeTabs creativeTab){
		super(material, 0, type);
		this.textureName = textureName;
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
		if(TheDFWings.hasTagCompound()){
			NBTTagCompound NBT=TheDFWings.getTagCompound();
			boolean prevState=NBT.getBoolean("HS");
			int x=(int) player.posX,y=(int) player.posY,z=(int) player.posZ;
			if(x<0)x--;
			if(y<0)y--;
			if(z<0)z--;
			if(world.getTotalWorldTime()%3==0&&world.isRemote){
				boolean bol=true;
				float rotation=-player.rotationYaw;
				double[] c=MathUtil.circleXZ(rotation+180);c[0]*=0.5;c[1]*=0.5;
				for(int l=0;l<3;l++){
					double[] a=MathUtil.circleXZ(rotation+90-10+l*10),b=MathUtil.circleXZ(rotation-90-10+l*10);
					a[0]*=2.7;b[0]*=2.7;
					a[1]*=2.7;b[1]*=2.7;
					Vec3d pp=new Vec3d(player.posX,player.posY,player.posZ);
					RayTraceResult 
					aObj=world.rayTraceBlocks(pp, new Vec3d(player.posX+a[0]+c[0],player.posY,player.posZ+a[1]+c[1]), false, false, true),
					bObj=world.rayTraceBlocks(pp, new Vec3d(player.posX+b[0]+c[0],player.posY,player.posZ+b[1]+a[1]), false, false, true);
					boolean bul=aObj!=null&&bObj!=null?aObj.typeOfHit!=RayTraceResult.Type.BLOCK&&bObj.typeOfHit!=RayTraceResult.Type.BLOCK:false;
					if(!bul){
						bol=false;
						continue;
					}
				}
				boolean isUnderWater=UtilM.getBlock(world, x,y,z) instanceof BlockLiquid;
				if((bol&&!isUnderWater)!=prevState||world.getTotalWorldTime()%20==0)UtilM.sendMessage(new GenericServerIntPacket(5, UtilM.booleanToInt(bol&&!isUnderWater||true)));
			}
		}else NBTUtil.createNBT(TheDFWings);
	}
}
