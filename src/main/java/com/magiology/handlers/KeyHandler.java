package com.magiology.handlers;

import static com.magiology.handlers.KeyHandler.Keys.*;

import org.lwjgl.input.Keyboard;

import com.magiology.client.gui.custom.hud.HandModeChangerHUD;
import com.magiology.client.gui.custom.hud.StatsDisplayHUD;
import com.magiology.core.MReference;
import com.magiology.core.init.MGui;
import com.magiology.forgepowered.packets.packets.OpenGuiPacket;
import com.magiology.forgepowered.packets.packets.UploadPlayerDataPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerStringPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerVoidPacket;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.SimpleCounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyHandler{
	public static enum Keys{
		ArmorGui("key.armor",Keyboard.KEY_V),
		BusGui("key.bus",Keyboard.KEY_I),
  		HandMode("key.bus",Keyboard.KEY_F),
		StatsGui("key.stats",Keyboard.KEY_B),
//		test("key.bus",Keyboard.KEY_L)
		;
		public final String  keyDesc;
		public final int keyValue,id;
		private Keys(String desc,int value){
			keyDesc=MReference.MODID+"."+desc;
			keyValue=value;
			id=counter.getAndAdd();
		}
		public boolean check(int pressedId){
			return pressedId==id;
		}
	}
	static SimpleCounter counter=new SimpleCounter();
	private static String[] keyDesc;
	private static int[] keyValues;
	private KeyBinding[] keys;
	Minecraft mc=U.getMC();
	
	public KeyHandler(){
		int lenght=Keys.values().length;
		keyDesc=new String[lenght];
		keyValues=new int[lenght];
		keys=new KeyBinding[lenght];
		
		for(int i=0;i<keyDesc.length;i++){
			Keys a=Keys.values()[i];
			keyDesc[i]=a.keyDesc;
			keyValues[i]=a.keyValue;
		}
		for(int i=0;i<keyValues.length;i++)keys[i]=regKey(new KeyBinding(keyDesc[i], keyValues[i], MReference.MODID+".key_bindings"));
	}
	// 2.
	public void decodeKeyInput(int rawKey){
//		try {
//			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
//				IResourceManager IRM=H.getMC().getResourceManager();
//				Map domainResourceManagers=DataStalker.getVariable(SimpleReloadableResourceManager.class, "domainResourceManagers",IRM);
//				IResourceManager iresourcemanager = (IResourceManager)domainResourceManagers.get(Textures.BedrockBreakerBase.getResourceDomain());
//				Helper.println(iresourcemanager.getResource(Textures.BedrockBreakerBase));
//			}
//		} catch (Exception e){e.printStackTrace();}
		GameSettings gs=U.getMC().gameSettings;
		boolean[] keys={Keyboard.isKeyDown(gs.keyBindForward.getKeyCode()),Keyboard.isKeyDown(gs.keyBindBack.getKeyCode()),Keyboard.isKeyDown(gs.keyBindJump.getKeyCode()),Keyboard.isKeyDown(gs.keyBindSneak.getKeyCode()),Keyboard.isKeyDown(gs.keyBindRight.getKeyCode()),Keyboard.isKeyDown(gs.keyBindLeft.getKeyCode())};
		if(Keyboard.getEventKeyState()){
			for(Keys i:Keys.values()){
				if(rawKey==keyValues[i.id]){
					keyInput(i.id);
					return;
				}
			}
			if(rawKey==gs.keyBindJump.getKeyCode()){
				int x=0,z=0;
				if(keys[0])x=1;
				if(keys[1])x+=2;
				if(keys[4])z=1;
				if(keys[5])z+=2;
				UtilM.sendMessage(new GenericServerStringPacket(0,x+","+z));
			}
		}
		if(rawKey==gs.keyBindForward.getKeyCode()||rawKey==gs.keyBindBack.getKeyCode()||rawKey==gs.keyBindJump.getKeyCode()||rawKey==gs.keyBindSneak.getKeyCode()||rawKey==gs.keyBindRight.getKeyCode()||rawKey==gs.keyBindLeft.getKeyCode()){
			ExtendedPlayerData data=ExtendedPlayerData.get(UtilM.getThePlayer());
			if(data!=null){
				data.keys=keys.clone();
				UtilM.sendMessage(new UploadPlayerDataPacket(UtilM.getThePlayer()));
			}
		}
	}
	// 3.
	public void keyInput(int keyId){
		if(StatsGui.check(keyId)){
			StatsDisplayHUD.instance.isStatsShowed=!StatsDisplayHUD.instance.isStatsShowed;
		}
		else if(ArmorGui.check(keyId)){
			UtilM.sendMessage(new OpenGuiPacket(MGui.GuiArmor));
		}
		else if(BusGui.check(keyId)){
			
		}
		else if(HandMode.check(keyId)){
			if(HandModeChangerHUD.instance.handAlpha>0.9){
				UtilM.sendMessage(new GenericServerVoidPacket(0));
			}
		}
//		else if(test.check(keyId)){
//			FMLClientHandler.instance().showGuiScreen(new GuiContainer(new ContainerEmpty()){
//				
//				@Override
//				protected void drawGuiContainerBackgroundLayer(float partialTicks,
//						int mouseX, int mouseY) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//		}
		
	}
	private KeyBinding regKey(KeyBinding key){
		ClientRegistry.registerKeyBinding(key);
		return key;
	}
	// 1.
	@SubscribeEvent
	public void startKeyInput(InputEvent.KeyInputEvent event){
		if(FMLClientHandler.instance().isGUIOpen(GuiChat.class))return;
		decodeKeyInput(Keyboard.getEventKey());
	}
}
