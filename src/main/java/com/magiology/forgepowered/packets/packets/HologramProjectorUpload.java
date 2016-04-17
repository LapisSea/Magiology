package com.magiology.forgepowered.packets.packets;
import java.io.IOException;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;


public class HologramProjectorUpload extends AbstractToServerMessage{
	 private boolean[] highlights;
	 private BlockPosM pos;
	 public HologramProjectorUpload(){}
	 public HologramProjectorUpload(TileEntityHologramProjector tile){
		 pos=new BlockPosM(tile.getPos());
		 highlights=tile.highlights;
	 }
	@Override
	public IMessage process(EntityPlayer player, Side side){
		 TileEntity tile=pos.getTile(player.worldObj);
		 if(tile instanceof TileEntityHologramProjector){
			 ((TileEntityHologramProjector)tile).highlights=highlights;
			 return null;
		 }
		 PrintUtil.println("PACKET HAS FAILED TO DELIVER THE DATA!");
		return null;
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		 int j=buffer.readInt();
		 pos=new BlockPosM(readPos(buffer));
		 highlights=new boolean[j];
		 for(int i=0;i<j;i++)highlights[i]=buffer.readBoolean();
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		 buffer.writeInt(highlights.length);
		 writePos(buffer, pos);
		 for(int i=0;i<highlights.length;i++)buffer.writeBoolean(highlights[i]);
	}
}