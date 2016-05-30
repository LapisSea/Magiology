package com.magiology.forgepowered.events;

import com.magiology.api.power.PowerCore;
import com.magiology.client.gui.GuiUpdater;
import com.magiology.core.Magiology;
import com.magiology.core.init.MGui;
import com.magiology.core.init.MItems;
import com.magiology.handlers.GuiHandlerM;
import com.magiology.handlers.PlayerClothPhysiscHandeler;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.registry.events.PlayerWrenchEvent;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.SlowdownUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityEvents{
//	ResourceLocation lol = new ResourceLocation(Magiology.MODID+":"+"/textures/blocks/background orginal.png");
//	WorldRenderer tess=TessHelper.getWR();
	boolean isFP;
	SlowdownUtil slowdown2=new SlowdownUtil(20);
	
	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event){
		World world=event.getEntity().worldObj;
		
		
		if(event.getEntity() instanceof EntityPlayer){
			EntityPlayer player=(EntityPlayer) event.getEntity();
			
			if(player.getName().equals("LapisSea")){
				PlayerClothPhysiscHandeler.addPlayer(player);
			}
			
			GuiUpdater.tryToUpdate(player);
			if(world.isRemote)if(ComplexPlayerRenderingData.get(player)==null)ComplexPlayerRenderingData.registerEntityPlayerRenderer(player);
			
			if(WingsFromTheBlackFireHandler.getIsActive(player))SpecialMovmentEvents.instance.handleWingPhysics(player);
			TheHandHandler.update(player);
			
			//---------------------
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(RightClickBlock event){
		
		World world=event.getWorld();
		EntityPlayer player=event.getEntityPlayer();
		BlockPos pos=event.getPos();
		if(TileEntityHologramProjector.invokeRayTrace(event, player))return;
		PlayerWrenchEvent.create(player, pos, event.getFace());
		if(UtilM.isItemInStack(MItems.fireHammer, player.getHeldItemMainhand())&&player.isSneaking()){
			TileEntity tile=world.getTileEntity(pos);
			if(tile instanceof TileEntityPow){
				if(!world.isRemote){
					GuiHandlerM.openGui(player, Magiology.getMagiology(), MGui.GuiUpgrade, pos);
					event.setCanceled(true);
				}
			}
		}
	}
	@SubscribeEvent
	public void onPlayerWrenchEvent(PlayerWrenchEvent event){
		World world=event.world;
		EntityPlayer player=event.entityPlayer;
		BlockPos pos=event.pos;
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof PowerCore){
			PowerCore.SavePowerToItemEvents.onPowerCoreWrenched(pos, player, world, tile);
		}else{
			
		}
	}
}
