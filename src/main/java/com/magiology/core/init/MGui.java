package com.magiology.core.init;

import com.magiology.client.gui.GuiUpdater;
import com.magiology.client.gui.custom.hud.HandModeChangerHUD;
import com.magiology.client.gui.custom.hud.SoulFlameHUD;
import com.magiology.client.gui.custom.hud.StatsDisplayHUD;
import com.magiology.client.gui.custom.hud.WingModeChangerHUD;
import com.magiology.core.Magiology;
import com.magiology.forgepowered.events.client.RenderEvents;
import com.magiology.handlers.GuiHandlerM;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraftforge.fml.common.network.NetworkRegistry;

public class MGui{
	public static final byte
		GuiControlBock=0,
		GuiArmor=1,
		GuiUpgrade=2,
		GuiStats=3,
		GuiSC=4,
		GuiISidedPowerInstructor=5,
		HologramProjectorObjectCustomGui=6,
		HologramProjectorMainGui=7,
		CommandCenterGui=8,
		JSProgramEditor=9;
	
	public static void preInit(){
		new GuiUpdater().toString();
	}

	public static void registerGuis(){
		NetworkRegistry.INSTANCE.registerGuiHandler(Magiology.getMagiology(), new GuiHandlerM());
		try{
			RenderEvents.registerFirstPersonGui(SoulFlameHUD.instance);
			RenderEvents.registerFirstPersonGui(WingModeChangerHUD.instance);
			RenderEvents.registerFirstPersonGui(StatsDisplayHUD.instance);
			RenderEvents.registerFirstPersonGui(HandModeChangerHUD.instance);
		}catch(Exception e){
			e.printStackTrace();
			U.exit(404);
		}
	}
}
