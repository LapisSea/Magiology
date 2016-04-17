package com.magiology.core.init;

import com.magiology.forgepowered.packets.core.AbstractPacket;
import com.magiology.forgepowered.packets.packets.ClickHologramPacket;
import com.magiology.forgepowered.packets.packets.HandActionPacket;
import com.magiology.forgepowered.packets.packets.HoloObjectUploadPacket;
import com.magiology.forgepowered.packets.packets.HologramProjectorUpload;
import com.magiology.forgepowered.packets.packets.NotifyPointedBoxChangePacket;
import com.magiology.forgepowered.packets.packets.OpenGuiPacket;
import com.magiology.forgepowered.packets.packets.OpenProgramContainerInGui;
import com.magiology.forgepowered.packets.packets.RightClickBlockPacket;
import com.magiology.forgepowered.packets.packets.SavableDataWithKeyPacket;
import com.magiology.forgepowered.packets.packets.SendPlayerDataPacket;
import com.magiology.forgepowered.packets.packets.TileRedstone;
import com.magiology.forgepowered.packets.packets.UploadPlayerDataPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerStringPacket;
import com.magiology.forgepowered.packets.packets.generic.GenericServerVoidPacket;
import com.magiology.io.WorldData.SyncClientsWorldData;
import com.magiology.io.WorldData.SyncServerWorldData;

public class MPackets{

	public static void preInit(){
		AbstractPacket.registerNewMessage(RightClickBlockPacket.class);
		AbstractPacket.registerNewMessage(TileRedstone.class);
		AbstractPacket.registerNewMessage(OpenGuiPacket.class);
		AbstractPacket.registerNewMessage(GenericServerIntPacket.class);
		AbstractPacket.registerNewMessage(GenericServerVoidPacket.class);
		AbstractPacket.registerNewMessage(GenericServerStringPacket.class);
		AbstractPacket.registerNewMessage(UploadPlayerDataPacket.class);
		AbstractPacket.registerNewMessage(ClickHologramPacket.class);
		AbstractPacket.registerNewMessage(HoloObjectUploadPacket.class);
		AbstractPacket.registerNewMessage(NotifyPointedBoxChangePacket.class);
		AbstractPacket.registerNewMessage(OpenProgramContainerInGui.class);
		AbstractPacket.registerNewMessage(OpenProgramContainerInGui.ExitGui.class);
		AbstractPacket.registerNewMessage(HologramProjectorUpload.class);
		AbstractPacket.registerNewMessage(SendPlayerDataPacket.class);
		AbstractPacket.registerNewMessage(SavableDataWithKeyPacket.class);
		AbstractPacket.registerNewMessage(SyncServerWorldData.class);
		AbstractPacket.registerNewMessage(SyncClientsWorldData.class);
		AbstractPacket.registerNewMessage(HandActionPacket.class);
	}

}
