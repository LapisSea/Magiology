package com.magiology.core.init;

import com.magiology.forgepowered.packets.core.AbstractPacket;
import com.magiology.forgepowered.packets.packets.toclient.SavableDataWithKeyPacket;
import com.magiology.forgepowered.packets.packets.toclient.UpdateClientPlayerDataPacket;
import com.magiology.forgepowered.packets.packets.toserver.ClickHologramPacket;
import com.magiology.forgepowered.packets.packets.toserver.HandActionPacket;
import com.magiology.forgepowered.packets.packets.toserver.HoloObjectUploadPacket;
import com.magiology.forgepowered.packets.packets.toserver.HologramProjectorUpload;
import com.magiology.forgepowered.packets.packets.toserver.NotifyPointedBoxChangePacket;
import com.magiology.forgepowered.packets.packets.toserver.OpenGuiPacket;
import com.magiology.forgepowered.packets.packets.toserver.OpenProgramContainerInGui;
import com.magiology.forgepowered.packets.packets.toserver.UploadPlayerDataPacket;
import com.magiology.io.WorldData.SyncClientsWorldData;
import com.magiology.io.WorldData.SyncServerWorldData;

public class MPackets{

	public static void preInit(){
		AbstractPacket.registerNewMessage(OpenGuiPacket.class);
		AbstractPacket.registerNewMessage(UploadPlayerDataPacket.class);
		AbstractPacket.registerNewMessage(ClickHologramPacket.class);
		AbstractPacket.registerNewMessage(HoloObjectUploadPacket.class);
		AbstractPacket.registerNewMessage(NotifyPointedBoxChangePacket.class);
		AbstractPacket.registerNewMessage(OpenProgramContainerInGui.class);
		AbstractPacket.registerNewMessage(OpenProgramContainerInGui.ExitGui.class);
		AbstractPacket.registerNewMessage(HologramProjectorUpload.class);
		AbstractPacket.registerNewMessage(UpdateClientPlayerDataPacket.class);
		AbstractPacket.registerNewMessage(SavableDataWithKeyPacket.class);
		AbstractPacket.registerNewMessage(SyncServerWorldData.class);
		AbstractPacket.registerNewMessage(SyncClientsWorldData.class);
		AbstractPacket.registerNewMessage(HandActionPacket.class);
	}

}
