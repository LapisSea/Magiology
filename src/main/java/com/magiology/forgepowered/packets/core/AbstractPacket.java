package com.magiology.forgepowered.packets.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.SavableData;
import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.m_extension.BlockPosM;
import com.magiology.util.utilobjects.m_extension.SimpleNetworkWrapperM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/* Inspired by Integrated-Circuits, thanks! o/ */
public abstract class AbstractPacket<T extends AbstractPacket<T>> implements IMessage, IMessageHandler<T, IMessage>{
	public static int registrationId=0;
	
	
	public static void registerNewMessage(Class<? extends AbstractPacket> message){
		if(UtilM.instanceOf(message, AbstractToServerMessage.class))registerPacket(message, Side.SERVER);
		else if(UtilM.instanceOf(message, AbstractToClientMessage.class))registerPacket(message, Side.CLIENT);
	}
	
	private static <T extends IMessage & IMessageHandler<T, IMessage>> void registerPacket(Class<T> clazz, Side side){
		if(Magiology.NETWORK_CHANNEL==null)Magiology.NETWORK_CHANNEL=new SimpleNetworkWrapperM(MReference.CHANNEL_NAME);
		if(side==Side.CLIENT)Magiology.NETWORK_CHANNEL.registerMessage(clazz, clazz, registrationId, Side.CLIENT);
		else if(side==Side.SERVER)Magiology.NETWORK_CHANNEL.registerMessage(clazz, clazz, registrationId, Side.SERVER);
		registrationId++;
	}
	
	@Override
	public final void fromBytes(ByteBuf buf){
		try{
			read(new PacketBuffer(buf));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public final IMessage onMessage(T message, MessageContext ctx){
		try{
			return message.process(ctx.side.isServer()?ctx.getServerHandler().playerEntity:UtilC.getThePlayer(),ctx.side);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public abstract IMessage process(EntityPlayer player, Side side);
	
	public abstract void read(PacketBuffer buffer) throws IOException;

	public Vector2f read2F(PacketBuffer buffer){
		return new Vector2f(buffer.readFloat(), buffer.readFloat());
	}
	
	public int[] read3i(PacketBuffer buffer){
		return new int[]{
				buffer.readInt(),
				buffer.readInt(),
				buffer.readInt()};
	}
	public ColorF readColor(PacketBuffer buffer){
		return new ColorF(
				buffer.readFloat(),
				buffer.readFloat(),
				buffer.readFloat(),
				buffer.readFloat());
	}
	public List<Float> readFloatList(PacketBuffer buffer){
		List<Float> floats=new ArrayList<>();
		for(int i=0,l=buffer.readInt();i<l;i++)floats.add(buffer.readFloat());
		return floats;
	}
	public List<Integer> readIntList(PacketBuffer buffer){
		List<Integer> ints=new ArrayList<>();
		for(int i=0,l=buffer.readInt();i<l;i++)ints.add(buffer.readInt());
		return ints;
	}
	public BlockPosM readPos(PacketBuffer buffer){
		return new BlockPosM(buffer.readInt(),buffer.readInt(),buffer.readInt());
	}
	public SavableData readSavableData(PacketBuffer buffer){
		SavableData result=null;
		List<Integer> integers=new ArrayList<Integer>();
		List<Boolean> booleans=new ArrayList<Boolean>();
		List<Byte> bytes___=new ArrayList<Byte>();
		List<Long> longs___=new ArrayList<Long>();
		List<Double> doubles_=new ArrayList<Double>();
		List<Float> floats__=new ArrayList<Float>();
		List<String> strings_=new ArrayList<String>();
		List<Short> shorts__=new ArrayList<Short>();
		try{
			result=(SavableData)Class.forName(readString(buffer)).newInstance();
		}catch(Exception e){e.printStackTrace();}
		if(result==null)return null;
		int intS=buffer.readInt();
		int bolS=buffer.readInt();
		int bytS=buffer.readInt();
		int lonS=buffer.readInt();
		int douS=buffer.readInt();
		int floS=buffer.readInt();
		int strS=buffer.readInt();
		int shoS=buffer.readInt();
		for(int i=0;i<intS;i++)integers.add(buffer.readInt	());
		for(int i=0;i<bolS;i++)booleans.add(buffer.readBoolean());
		for(int i=0;i<bytS;i++)bytes___.add(buffer.readByte   ());
		for(int i=0;i<lonS;i++)longs___.add(buffer.readLong   ());
		for(int i=0;i<douS;i++)doubles_.add(buffer.readDouble ());
		for(int i=0;i<floS;i++)floats__.add(buffer.readFloat  ());
		for(int i=0;i<strS;i++)strings_.add(readString  (buffer));
		for(int i=0;i<shoS;i++)shorts__.add(buffer.readShort  ());
		result.readData(integers.iterator(), booleans.iterator(), bytes___.iterator(), longs___.iterator(), doubles_.iterator(), floats__.iterator(), strings_.iterator(), shorts__.iterator());
		return result;
	}
	public String readString(PacketBuffer buffer){
		return ByteBufUtils.readUTF8String(buffer);
	}
	public Vec3M readVec3M(PacketBuffer buffer){
		return new Vec3M(buffer.readFloat(),buffer.readFloat(),buffer.readFloat());
	}
	@Override
	public final void toBytes(ByteBuf buf){
		try{
			write(new PacketBuffer(buf));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public abstract void write(PacketBuffer buffer) throws IOException;
	public void write2F(PacketBuffer buffer, Vector2f ff){
		buffer.writeFloat(ff.x);buffer.writeFloat(ff.y);
	}
	public void write3i(PacketBuffer buffer,int[] pos){
		buffer.writeInt(pos[0]);
		buffer.writeInt(pos[1]);
		buffer.writeInt(pos[2]);
	}
	public void writeColor(PacketBuffer buffer, ColorF color){
		buffer.writeFloat(color.r);
		buffer.writeFloat(color.g);
		buffer.writeFloat(color.b);
		buffer.writeFloat(color.a);
	}
	public void writeFloatList(PacketBuffer buffer, List<Float> floats){
		buffer.writeInt(floats.size());
		for(Float float1:floats)buffer.writeFloat(float1);
	}
	public void writeIntList(PacketBuffer buffer, List<Integer> ints){
		buffer.writeInt(ints.size());
		for(int in:ints)buffer.writeInt(in);
	}
	public void writePos(PacketBuffer buffer, Vec3i pos){
		buffer.writeInt(pos.getX());
		buffer.writeInt(pos.getY());
		buffer.writeInt(pos.getZ());
	}
	public void writeSavableData(PacketBuffer buffer, SavableData data){
		List<Integer> integers=new ArrayList<Integer>();
		List<Boolean> booleans=new ArrayList<Boolean>();
		List<Byte>	bytes___=new ArrayList<Byte>();
		List<Long>	longs___=new ArrayList<Long>();
		List<Double>  doubles_=new ArrayList<Double>();
		List<Float>   floats__=new ArrayList<Float>();
		List<String>  strings_=new ArrayList<String>();
		List<Short>   shorts__=new ArrayList<Short>();
		data.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		writeString(buffer, data.getClass().getName());
		buffer.writeInt(integers.size());
		buffer.writeInt(booleans.size());
		buffer.writeInt(bytes___.size());
		buffer.writeInt(longs___.size());
		buffer.writeInt(doubles_.size());
		buffer.writeInt(floats__.size());
		buffer.writeInt(strings_.size());
		buffer.writeInt(shorts__.size());
		for(int i=0;i<integers.size();i++)buffer.writeInt	(integers.get(i));
		for(int i=0;i<booleans.size();i++)buffer.writeBoolean(booleans.get(i));
		for(int i=0;i<bytes___.size();i++)buffer.writeByte   (bytes___.get(i));
		for(int i=0;i<longs___.size();i++)buffer.writeLong   (longs___.get(i));
		for(int i=0;i<doubles_.size();i++)buffer.writeDouble (doubles_.get(i));
		for(int i=0;i<floats__.size();i++)buffer.writeFloat  (floats__.get(i));
		for(int i=0;i<strings_.size();i++)writeString (buffer,strings_.get(i));
		for(int i=0;i<shorts__.size();i++)buffer.writeShort  (shorts__.get(i));
	}
	public void writeString(PacketBuffer buffer, String string){
		ByteBufUtils.writeUTF8String(buffer, string!=null?string:"");
	}
	public void writeVec3M(PacketBuffer buffer, Vec3M vec){
		buffer.writeFloat((float)vec.x);
		buffer.writeFloat((float)vec.y);
		buffer.writeFloat((float)vec.z);
	}
}
//public class DummyPacket extends AbstractPacket{
//	protected int var;
//	public DummyPacket(){}
//	public DummyPacket(int var){
//		this.var=var;
//	}
//	@Override
//	public void read(PacketBuffer buffer) throws IOException{
//		this.var=buffer.readInt();
//	}
//	@Override
//	public void write(PacketBuffer buffer) throws IOException{
//		buffer.writeInt(var);
//	}
//	@Override
//	public void process(EntityPlayer player, Side side) {
//		//code..........
//	}
//}
