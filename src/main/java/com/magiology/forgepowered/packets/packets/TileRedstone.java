package com.magiology.forgepowered.packets.packets;
import java.io.IOException;

import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.util.utilclasses.PrintUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;


public class TileRedstone extends AbstractToServerMessage{
	 private int id;
	 private BlockPos pos;
	 public TileRedstone(){}
	 public TileRedstone(TileEntityControlBlock tile){
		 id=tile.redstoneC;
		 pos=tile.getPos();
	 }
	@Override
	public IMessage process(EntityPlayer player, Side side){
		 World world=player.worldObj;
		 TileEntity tile=world.getTileEntity(pos);
		 if(tile!=null&&tile instanceof TileEntityControlBlock){
			 ((TileEntityControlBlock)tile).redstoneC=id;
			 return null;
		 }
		 PrintUtil.println("PACKET HAS FAILED TO DELIVER THE DATA!\n");
		return null;
	}
	@Override
	public void read(PacketBuffer buffer)throws IOException{
		 id=buffer.readInt();
		 pos=readPos(buffer);
	}
	@Override
	public void write(PacketBuffer buffer)throws IOException{
		 buffer.writeInt(id);;
		 writePos(buffer, pos);
	}
}