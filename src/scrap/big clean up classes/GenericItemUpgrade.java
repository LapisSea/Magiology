package com.magiology.mcobjects.items;

import java.util.List;

import com.magiology.core.init.MCreativeTabs;
import com.magiology.mcobjects.effect.EntitySmoothBubleFX;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.UpgradeType;
import com.magiology.util.utilclasses.FontEffectUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.NBTUtil;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GenericItemUpgrade extends Item{
	int Level;
	public UpgradeType UT;
	public GenericItemUpgrade(int level, UpgradeType ut,String UnlocalizedName){
		String typeTS;
		Level=level;
		UT=ut;
		typeTS=UT.toString();
		if(UnlocalizedName!=null)this.setUnlocalizedName(UnlocalizedName);
		else{
			this.setUnlocalizedName(typeTS+" upgrade level."+level);
		}
		this.setCreativeTab(MCreativeTabs.Whwmmt_upgrades);
//		this.setTextureName(MReference.MODID+":"+typeTS+"Upgrades");
		this.setMaxStackSize(1);
	}
	
	public void registerItemUpgrade(){
		RegisterItemUpgrades.registerItemUpgrades(this,Level,UT);
		GameRegistry.registerItem(this, this.getUnlocalizedName());
//		MItems.setGenericUpgradeRenderer(this);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer player){
		NBTUtil.createNBT(is);
		if(w.isRemote){
//			int ID=RegisterUpgrades.getItemUpgradeID(is.getItem());
//			UtilM.println("ID="+ID+","+RegisterUpgrades.getItemTypeID(ID)+" level:"+RegisterUpgrades.getItemUpgradeLevel(ID)+" type: "+RegisterUpgrades.getItemUpgradeType(ID).toString()+"\n");
			
		}
		
		
		return is;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		boolean result=false;
		if(NBTUtil.createNBT(stack)!=null){
			UpgradeType type=RegisterItemUpgrades.getItemUpgradeType(RegisterItemUpgrades.getItemUpgradeID(stack.getItem()));
			Block block=UtilM.getBlock(world, pos);
			if(!player.isSneaking()&&type==UpgradeType.Priority){
				double MaxX=block.getBlockBoundsMaxX(),MinX=block.getBlockBoundsMinX();
				double MaxY=block.getBlockBoundsMaxY(),MinY=block.getBlockBoundsMinY();
				double MaxZ=block.getBlockBoundsMaxZ(),MinZ=block.getBlockBoundsMinZ();
				
				
				switch (side.getIndex()){
				case 0:stack.getTagCompound().setInteger("side", 1);break;
				case 1:stack.getTagCompound().setInteger("side", 0);break;
				case 2:stack.getTagCompound().setInteger("side", 2);break;
				case 3:stack.getTagCompound().setInteger("side", 4);break;
				case 4:stack.getTagCompound().setInteger("side", 5);break;
				case 5:stack.getTagCompound().setInteger("side", 3);break;
				}
				
				for(int a=0;a<20;a++)switch(side.getIndex()){
				case 0:UtilM.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+UtilM.RF()*(MaxX-MinX)+MinX, pos.getY(),	  pos.getZ()+UtilM.RF()*(MaxZ-MinZ)+MinZ, 0, 0, 0, 500, 1+UtilM.CRandF(0.5), -10+UtilM.CRandF(0.5), UtilM.RInt(10)==0?2:1, UtilM.RF(), UtilM.RF(), UtilM.RF(), 0.8));break;
				case 1:UtilM.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+UtilM.RF()*(MaxX-MinX)+MinX, pos.getY()+MaxY, pos.getZ()+UtilM.RF()*(MaxZ-MinZ)+MinZ, 0, 0, 0, 500, 1+UtilM.CRandF(0.5), 10+UtilM.CRandF(0.5), UtilM.RInt(10)==0?2:1, UtilM.RF(), UtilM.RF(), UtilM.RF(), 0.8));break;
				case 2:UtilM.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+UtilM.RF()*(MaxX-MinX)+MinX, pos.getY()+UtilM.RF()*(MaxY-MinY)+MinY, pos.getZ()+MinZ, 0, 0, -0.1, 500, 1+UtilM.CRandF(0.5), UtilM.CRandF(5), UtilM.RInt(10)==0?2:1, UtilM.RF(), UtilM.RF(), UtilM.RF(), 0.8));break;
				case 3:UtilM.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+UtilM.RF()*(MaxX-MinX)+MinX, pos.getY()+UtilM.RF()*(MaxY-MinY)+MinY, pos.getZ()+MaxZ, 0, 0, 0.1, 500, 1+UtilM.CRandF(0.5), UtilM.CRandF(5), UtilM.RInt(10)==0?2:1, UtilM.RF(), UtilM.RF(), UtilM.RF(), 0.8));break;
				case 4:UtilM.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+MinX, pos.getY()+UtilM.RF()*(MaxY-MinY)+MinY, pos.getZ()+UtilM.RF()*(MaxZ-MinZ)+MinZ, -0.1, 0, 0, 500, 1+UtilM.CRandF(0.5), UtilM.CRandF(5), UtilM.RInt(10)==0?2:1, UtilM.RF(), UtilM.RF(), UtilM.RF(), 0.8));break;
				case 5:UtilM.spawnEntityFX(new EntitySmoothBubleFX(world, pos.getX()+MaxX, pos.getY()+UtilM.RF()*(MaxY-MinY)+MinY, pos.getZ()+UtilM.RF()*(MaxZ-MinZ)+MinZ, 0.1, 0, 0, 500, 1+UtilM.CRandF(0.5), UtilM.CRandF(5), UtilM.RInt(10)==0?2:1, UtilM.RF(), UtilM.RF(), UtilM.RF(), 0.8));break;
				}
			}
			
		}
		
		return result;
	}
	@Override
	public void onUpdate(ItemStack is, World w, Entity entity, int var1, boolean b1){
		if(NBTUtil.createNBT(is)!=null){
			if(RegisterItemUpgrades.getItemUpgradeType(UT.GetTypeID())==UpgradeType.Priority){
				if(!is.getTagCompound().hasKey("side"))is.getTagCompound().setInteger("side", 0);
			}
			
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b){
		int i=0;
		if(GuiScreen.isShiftKeyDown())i++;
		if(GuiScreen.isCtrlKeyDown()) i++;
		switch (UT.GetTypeID()){
		case 0:{
			list.add(FontEffectUtil.AQUA+"Increases range");
			if(i>0){
				list.add(FontEffectUtil.getRandEff()+"How surprising!");
				if(i==2)list.add(FontEffectUtil.GOLD+"IT IS! :-)");
			}
		}break;
		case 1:{
			list.add(FontEffectUtil.YELLOW+"Speeds up effects of objects!");
			if(i>0){
				list.add(FontEffectUtil.AQUA+"If this is in a block it speeds up things like processing of materials");
				list.add(FontEffectUtil.AQUA+"or if it is in a aromur peace like pants it makes you run faster.");
				if(i==2)list.add(FontEffectUtil.BLUE+"I am so boring. Like extreamly boring!");
			}
		}break;
		case 2:{
			list.add("Adds extra controll to objects.");
			if(i>0){
				list.add(FontEffectUtil.DARK_GRAY+"IT CONTROLS EVERYTHING! "+FontEffectUtil.UNDERLINE+FontEffectUtil.AQUA+">:D");
				if(i==2)list.add(FontEffectUtil.BLUE+"It controls every player in the worldObj and that includes you!");
			}
		}break;
		case 3:{
			list.add(FontEffectUtil.BLUE+"Adds ability to "+FontEffectUtil.DARK_RED+"fly"+FontEffectUtil.BLUE+"!");
			if(i>0){
				if(UtilM.RInt(20)==0)list.add("When pigs fly");
				
				if(i==2)if(UtilM.RInt(80)==0){
					String lolITrollerYou=FontEffectUtil.RED+""+FontEffectUtil.UNDERLINE+FontEffectUtil.OBFUSCATED+"aaa  "+FontEffectUtil.RESET+FontEffectUtil.RED+""+FontEffectUtil.UNDERLINE+"illuminati is"+(UtilM.RB()?" not":"")+" real".toUpperCase()+"!  "+FontEffectUtil.OBFUSCATED+"  aaa";
					list.add(lolITrollerYou);
					list.add(lolITrollerYou);
				}
			}
		}break;
		case 4:{
			String[] side={"up","down","left","right","forward","back","nowhere","overthere","to your mama","away from me","to hell","to store","to your computer","to that upgrade overthere"};
			int r1=UtilM.RInt(side.length),r2=UtilM.RInt(side.length);
			list.add(FontEffectUtil.BLUE+"Adds priority to a specific side."+(i==1&&i==2?"			  ":""));
			
			if(stack.hasTagCompound()){
				int id=stack.getTagCompound().getInteger("side");
				switch(id){
				case 0:id=1;break;
				case 1:id=0;break;
				case 2:id=2;break;
				case 3:id=5;break;
				case 4:id=3;break;
				case 5:id=4;break;
				}
				list.add("Current side: "+EnumFacing.getFront(id).toString().toLowerCase());
			}
			else list.add(FontEffectUtil.RED+""+FontEffectUtil.UNDERLINE+"No NBT on stack!");
			if(i==2){
				list.add(FontEffectUtil.GOLD+"First go "+FontEffectUtil.RED+side[r1]+FontEffectUtil.GOLD+"!");
				list.add(FontEffectUtil.GOLD+"No actually go "+FontEffectUtil.RED+side[r2]+FontEffectUtil.GOLD+"!");
			}
		}break;
		case 5:{
			list.add(FontEffectUtil.BLUE+"Adds more capacity to containers.");
			if(i>0){
				list.add("If you could only press another button to see more.");
				if(i==2)list.add("But unfortunately you can't.");
			}
		}break;
		case 6:{
			list.add(FontEffectUtil.BLUE+"In right place it could let you see more than ");
			list.add(FontEffectUtil.BLUE+"only your "+(i!=0?FontEffectUtil.RED+"xRay ":"")+FontEffectUtil.BLUE+"eyes can see.");
			if(i>0){
				list.add(FontEffectUtil.GOLD+"If you could only press another button to see"+(i==1?" more":"")+(i==1?"":(" "+FontEffectUtil.getRandEff()+"A"+FontEffectUtil.getRandEff()+"B"+FontEffectUtil.getRandEff()+"S"+FontEffectUtil.getRandEff()+"O"+FontEffectUtil.getRandEff()+"L"+FontEffectUtil.getRandEff()+"U"+FontEffectUtil.getRandEff()+"T"+FontEffectUtil.getRandEff()+"E"+FontEffectUtil.getRandEff()+"L"+FontEffectUtil.getRandEff()+"pos.getY()"+FontEffectUtil.getRandEff()+" "+FontEffectUtil.getRandEff()+"E"+FontEffectUtil.getRandEff()+"V"+FontEffectUtil.getRandEff()+"E"+FontEffectUtil.getRandEff()+"R"+FontEffectUtil.getRandEff()+"pos.getY()"+FontEffectUtil.getRandEff()+"T"+FontEffectUtil.getRandEff()+"H"+FontEffectUtil.getRandEff()+"I"+FontEffectUtil.getRandEff()+"N"+FontEffectUtil.getRandEff()+"G"+FontEffectUtil.getRandEff()+"!"+FontEffectUtil.getRandEff()+"!")));
			}
		}break;

		default:{
			list.add("Invalid desc!");
		}break;
		}
	}
}
