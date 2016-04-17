package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import com.magiology.api.lang.JSProgramContainer;
import com.magiology.api.lang.program.ProgramDataBase;
import com.magiology.client.gui.container.CommandCenterContainer;
import com.magiology.core.init.MItems;
import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class OpenProgramContainerInGui extends AbstractToServerMessage{
	
	public static class ExitGui extends AbstractToServerMessage{
		
		private String data,name;
		private int slotId;
		private BlockPosM tilePos;
		
		public ExitGui(){}
		public ExitGui(int slotId, String data, String name, BlockPos tilePos){
			this.slotId=slotId;
			this.tilePos=tilePos!=null?new BlockPosM(tilePos):null;
			this.data=data;
			this.name=name;
		}
		
		
		@Override
		public IMessage process(EntityPlayer player, Side side){
			JSProgramContainer program;
			ItemStack stack=player.inventory.mainInventory[slotId];
			if(UtilM.isItemInStack(MItems.commandContainer, stack)&&stack.hasTagCompound()){
				program=(JSProgramContainer)stack.getItem();
				ProgramDataBase.code_set(program.getId(stack), name, data);
			}
			return null;
		}

		@Override
		public void read(PacketBuffer buffer)throws IOException{
			slotId=buffer.readInt();
			data=readString(buffer);
			name=readString(buffer);
			if(buffer.readBoolean())tilePos=new BlockPosM(buffer.readBlockPos());
		}

		@Override
		public void write(PacketBuffer buffer)throws IOException{
			buffer.writeInt(slotId);
			writeString(buffer, data);
			writeString(buffer, name);
			buffer.writeBoolean(tilePos!=null);
			if(tilePos!=null)buffer.writeBlockPos(tilePos);
		}

	}
	
	private int slotId;
	public OpenProgramContainerInGui(){}
	
	
	public OpenProgramContainerInGui(int slotId){
		this.slotId=slotId;
	}

	@Override
	public IMessage process(EntityPlayer player, Side side){
		try{
			if(player.openContainer instanceof CommandCenterContainer){
				CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
				ItemStack stack=container.inventorySlots.get(slotId).getStack();
				stack.getItem().onItemRightClick(stack, player.worldObj, player);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void read(PacketBuffer buffer)throws IOException{
		slotId=buffer.readInt();
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		buffer.writeInt(slotId);
	}
}
