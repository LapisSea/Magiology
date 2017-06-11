package com.magiology.core.registry.init;

import com.magiology.core.Magiology;
import com.magiology.core.registry.imp.AutoRegistry;
import com.magiology.forge.networking.PacketM;
import com.magiology.forge.networking.UpdateTileNBTPacket;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

//<GEN:	IMPORTS START>
import com.magiology.forge.networking.UpdateTileNBTPacket;
//<GEN:	IMPORTS END>

public class PacketRegistry extends AutoRegistry<PacketM>{

	private static final PacketRegistry instance=new PacketRegistry();

	public static PacketRegistry get(){return instance;}
	
	private PacketRegistry(){
		super(PacketM.class);
	}
	
	@Override
	public void registerObj(Class<PacketM> packet){
		try{
			//searching for "public static final Side RECEIVER_SIDE"
			
			Field sideF=packet.getDeclaredField("RECEIVER_SIDE");
			int mod=sideF.getModifiers();
			
			if(!Modifier.isPublic(mod)) throw new IllegalStateException("SIDE has to be public!");
			if(!Modifier.isStatic(mod)) throw new IllegalStateException("SIDE has to be static!");
			if(!Modifier.isFinal(mod)) throw new IllegalStateException("SIDE has to be final!");
			
			Magiology.NETWORK_CHANNEL.registerPacket(packet, (Side)sideF.get(null));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void init(){
		//<GEN:	INIT START>
		add(UpdateTileNBTPacket.class);
		//<GEN:	INIT END>
	}
	
}
