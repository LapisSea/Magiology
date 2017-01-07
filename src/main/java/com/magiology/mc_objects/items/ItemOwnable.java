package com.magiology.mc_objects.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.magiology.util.m_extensions.ItemM;
import com.magiology.util.objs.NBTUtil;
import com.magiology.util.statics.UtilM;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemOwnable extends ItemM{
	
	private static final String				OWNER		="owner",LEGIT="128-database";
	public final boolean					showPlayer;
	private static final Map<UUID,String>	NAME_MAPPER	=new HashMap<>();
	
	public ItemOwnable(boolean showPlayer){
		this.showPlayer=showPlayer;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		if(!showPlayer) return;
		
		UUID uuid=getUUID(stack);
		EntityPlayer pl=getOwner(uuid, playerIn.world);
		String name;
		
		if(pl!=null){
			name=pl.getName();
			NAME_MAPPER.put(uuid, name);
		}else{
			if(uuid!=null) name=NAME_MAPPER.get(uuid);
			name="-----";
		}
		
		tooltip.add("Owner: "+name);
		
	}
	
	/**
	 * Searches for {@link EntityPlayer} by {@link UUID} in world.
	 * @param stack = source of {@link UUID}
	 * @param world = {@link World} that is searched
	 * @return
	 */
	public EntityPlayer getOwner(ItemStack stack, World world){
		return getOwner(getUUID(stack), world);
	}
	
	private EntityPlayer getOwner(UUID uuid, World world){
		if(uuid==null) return null;
		return world.getPlayerEntityByUUID(uuid);
	}
	
	private UUID getUUID(ItemStack stack){
		return NBTUtil.getUUID(stack, "owner");
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player){
		applyOwner(stack, player);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		applyOwner(hand, player);
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand){
		applyOwner(hand, player);
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
		applyOwner(stack, entityIn);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity){
		applyOwner(stack, entity);
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player){
		applyOwner(stack, player);
		return super.onDroppedByPlayer(stack, player);
	}
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		applyOwner(hand, player);
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack){
		applyOwner(stack, player);
		super.onArmorTick(world, player, stack);
	}
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack){
		applyOwner(stack, entity);
		return super.onEntitySwing(entity, stack);
	}
	
	private static void applyOwner(ItemStack stack, Entity entity){
		if(entity instanceof EntityPlayer) applyOwner(stack, (EntityPlayer)entity);
	}
	
	/**
	 * Sets first player that comes in contact with stack.
	 * Overrides origional owner if origional user is using cracked client and new owner is premium to ensure no loss of property in case of upgrading to premium.
	 */
	private static void applyOwner(EnumHand hand, EntityPlayer player){
		if(UtilM.isRemote(player)) return;
		applyOwner0(player.getHeldItem(hand), player);
	}
	
	private static void applyOwner(ItemStack stack, EntityPlayer player){
		if(UtilM.isRemote(player)) return;
		applyOwner0(stack, player);
	}
	
	private static void applyOwner0(ItemStack stack, EntityPlayer player){
		
		NBTTagCompound nbt=NBTUtil.createNBT(stack);
		
		GameProfile profile=player.getGameProfile();
		UUID id=player.getUUID(profile);
		boolean key=profile.isLegacy();
		
		if(NBTUtil.initUUID(stack, OWNER, id)) NBTUtil.initBoolean(stack, LEGIT, key);
		else if(!NBTUtil.getBoolean(stack, LEGIT)&&key){
			NBTUtil.setUUID(stack, OWNER, id);
			NBTUtil.setBoolean(stack, LEGIT, true);
		}
		
	}
}
