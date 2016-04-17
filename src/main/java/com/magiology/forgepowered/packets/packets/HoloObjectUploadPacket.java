package com.magiology.forgepowered.packets.packets;

import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.lang.ICommandInteract;
import com.magiology.forgepowered.packets.core.AbstractToServerMessage;
import com.magiology.mcobjects.tileentityes.hologram.Button;
import com.magiology.mcobjects.tileentityes.hologram.Field;
import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.mcobjects.tileentityes.hologram.Slider;
import com.magiology.mcobjects.tileentityes.hologram.StringContainer;
import com.magiology.mcobjects.tileentityes.hologram.TextBox;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.utilobjects.ColorF;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class HoloObjectUploadPacket extends AbstractToServerMessage{
	
	private ColorF color;
	private int id,type;
	private boolean isCommand;
	private boolean moveMode,suportsText,isDead;
	private Vector2f offset,size,originalSize;
	private BlockPos pos;
	private float scale;
	private String text,name,programName,args;
	
	public HoloObjectUploadPacket(){}
	public HoloObjectUploadPacket(HoloObject ho){
		pos=ho.host.getPos();
		id=ho.id;
		
		if(ho.getClass()==TextBox.class)type=1;
		else if(ho.getClass()==Button.class)type=2;
		else if(ho.getClass()==Field.class)type=3;
		else if(ho.getClass()==Slider.class)type=4;
		
		moveMode=ho.moveMode;
		scale=ho.scale;
		offset=ho.position;
		size=ho.size;
		color=ho.setColor;
		suportsText=ho instanceof StringContainer;
		originalSize=ho.originalSize;
		isDead=ho.isDead;
		if(suportsText)text=((StringContainer)ho).getString();
		isCommand=ho!=null;
		if(isCommand){
			name=((ICommandInteract)ho).getName();
			programName=((ICommandInteract)ho).getProgramName();
			args=((ICommandInteract)ho).getArgs();
		}
	}
	@Override
	public IMessage process(EntityPlayer player, Side side){
		TileEntity test=player.worldObj.getTileEntity(pos);
		if(test instanceof TileEntityHologramProjector){
			TileEntityHologramProjector tile=(TileEntityHologramProjector)test;
			boolean found=false;
			int roId=-1;
			for(int i=0;i<tile.holoObjects.size();i++){
				if(tile.holoObjects.get(i).id==id){
					found=true;
					roId=i;
					continue;
				}
			}
			if(found){
				tile.holoObjects.get(roId).moveMode=moveMode;
				tile.holoObjects.get(roId).scale=scale;
				tile.holoObjects.get(roId).position=offset;
				tile.holoObjects.get(roId).size=size;
				tile.holoObjects.get(roId).originalSize=originalSize;
				tile.holoObjects.get(roId).setColor=color;
				tile.holoObjects.get(roId).isDead=isDead;
				if(suportsText&&tile.holoObjects.get(roId) instanceof StringContainer)((StringContainer)tile.holoObjects.get(roId)).setString(text);
				if(isCommand&&tile.holoObjects.get(roId)!=null){
					((ICommandInteract)tile.holoObjects.get(roId)).setProgramName(programName);
					((ICommandInteract)tile.holoObjects.get(roId)).setArgs(args);
					((ICommandInteract)tile.holoObjects.get(roId)).setName(name);
				}
			}else{
				HoloObject newObject=null;
				if(type==1)newObject=new TextBox(tile, text);
				else if(type==2)newObject=new Button(tile, size);
				else if(type==3)newObject=new Field(tile, size);
				else if(type==4)newObject=new Slider(tile, size);
				if(newObject!=null){
					newObject.moveMode=moveMode;
					newObject.scale=scale;
					newObject.position=offset;
					newObject.size=size;
					newObject.originalSize=originalSize;
					newObject.color=color;
					newObject.isDead=isDead;
					if(suportsText)((StringContainer)newObject).setString(text);
					if(isCommand){
						((ICommandInteract)tile.holoObjects.get(roId)).setProgramName(programName);
						((ICommandInteract)tile.holoObjects.get(roId)).setArgs(args);
						((ICommandInteract)newObject).setName(name);
					}
					tile.holoObjects.add(newObject);
				}
			}
			tile.sync();
		}
		return null;
	}
	@Override
	public void read(PacketBuffer buffer) throws IOException{
		pos=readPos(buffer);
		id=buffer.readInt();
		type=buffer.readInt();
		moveMode=buffer.readBoolean();
		isDead=buffer.readBoolean();
		scale=buffer.readFloat();
		offset=read2F(buffer);
		size=read2F(buffer);
		color=readColor(buffer);
		suportsText=buffer.readBoolean();
		originalSize=read2F(buffer);
		if(suportsText){
			int lenght=buffer.readInt();
			text=lenght>0?buffer.readStringFromBuffer(lenght):"";
		}
		isCommand=buffer.readBoolean();
		if(isCommand){
			programName=readString(buffer);
			args=readString(buffer);
			name=readString(buffer);
		}
	}
	@Override
	public void write(PacketBuffer buffer) throws IOException{
		writePos(buffer, pos);
		buffer.writeInt(id);
		buffer.writeInt(type);
		buffer.writeBoolean(moveMode);
		buffer.writeBoolean(isDead);
		buffer.writeFloat(scale);
		write2F(buffer, offset);
		write2F(buffer, size);
		writeColor(buffer, color);
		buffer.writeBoolean(suportsText);
		write2F(buffer, originalSize);
		if(suportsText){
			buffer.writeInt(text.length());
			if(text.length()>0)buffer.writeString(text);
		}
		buffer.writeBoolean(isCommand);
		if(isCommand){
			writeString(buffer, programName);
			writeString(buffer, args);
			writeString(buffer, name);
		}
	}

}
