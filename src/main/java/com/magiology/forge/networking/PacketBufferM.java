package com.magiology.forge.networking;

import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.objs.vec.Vec3M;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class PacketBufferM extends PacketBuffer{
	
	public PacketBufferM(ByteBuf wrapped){
		super(wrapped);
	}
	
	public Vec2FM read2FM(){
		return new Vec2FM(readFloat(), readFloat());
	}
	
	public Vector2f read2F(){
		return new Vector2f(readFloat(), readFloat());
	}
	
	public PacketBufferM write2FM(Vec2FM vec){
		writeFloat(vec.x).writeFloat(vec.y);
		return this;
	}
	
	public PacketBufferM write2F(Vector2f vec){
		writeFloat(vec.x).writeFloat(vec.y);
		return this;
	}
	
	public int[] read3i(){
		return new int[]{readInt(), readInt(), readInt()};
	}
	
	public PacketBufferM write3i(int[] arr){
		writeInt(arr[0]).writeInt(arr[1]).writeInt(arr[2]);
		return this;
	}
	
	public ColorM readColor(){
		return new ColorM(readFloat(), readFloat(), readFloat(), readFloat());
	}
	
	public PacketBufferM writeColor(ColorM color){
		writeFloat(color.r()).writeFloat(color.g()).writeFloat(color.b()).writeFloat(color.a());
		return this;
	}
	
	public List<Float> readFloatList(){
		List<Float> floats=new ArrayList<>();
		for(int i=0, l=readInt(); i<l; i++) floats.add(readFloat());
		return floats;
	}
	
	public List<Integer> readIntList(){
		List<Integer> ints=new ArrayList<>();
		for(int i=0, l=readInt(); i<l; i++) ints.add(readInt());
		return ints;
	}
	
	public PacketBufferM writeFloatList(List<Float> list){
		writeInt(list.size());
		for(float f : list) writeFloat(f);
		return this;
	}
	
	public PacketBufferM writeIntList(List<Integer> list){
		writeInt(list.size());
		for(int i : list) writeInt(i);
		return this;
	}
	
	public String readString(){
		return ByteBufUtils.readUTF8String(this);
	}
	
	@Override
	public PacketBufferM writeString(String s){
		ByteBufUtils.writeUTF8String(this, s);
		return this;
	}
	
	public Vec3M readVec3M(){
		return new Vec3M(readFloat(), readFloat(), readFloat());
	}
}
