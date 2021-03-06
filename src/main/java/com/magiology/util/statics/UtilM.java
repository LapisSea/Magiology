package com.magiology.util.statics;

import com.magiology.SoundM;
import com.magiology.core.MReference;
import com.magiology.util.interf.Locateable;
import com.magiology.util.interf.Worldabale;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.ItemM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.FloatBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.mojang.realmsclient.gui.ChatFormatting.GOLD;
import static com.mojang.realmsclient.gui.ChatFormatting.RESET;

public class UtilM{
	
	public class U extends UtilM{}
	
	public static final float  p       =1F/16F;
	public static final String LINE_REG="(\n|"+System.lineSeparator()+")";
	
	public static String floatBufferToString(FloatBuffer buff){
		StringBuilder print=new StringBuilder("Buffer{");
		buff=buff.duplicate();
		if(buff.capacity()>0){
			int j=0;
			print.append(buff.get(j));
			for(j=1; j<buff.capacity(); j++)
				print.append(", ").append(buff.get(j));
		}
		print.append('}');
		return print.toString();
	}
	
	public static boolean axisAlignedBBEqual(AxisAlignedBB box1, AxisAlignedBB box2){
		if(box1==box2) return true;
		return !isNull(box1, box2)&&box1.minX==box2.minX&&box1.minY==box2.minY&&box1.minZ==box2.minZ&&box1.maxX==box2.maxX&&box1.maxY==box2.maxY&&
				   box1.maxZ==box2.maxZ;
	}
	
	public static BlockPos BlockPos(int[] array3i){
		return new BlockPos(array3i[0], array3i[1], array3i[2]);
	}
	
	public static BlockPos[] BlockPosArray(int[] pos1, int[] pos2, int[] pos3){
		BlockPos[] result=new BlockPos[0];
		for(int i=0; i<pos1.length; i++)
			result=ArrayUtils.add(result, new BlockPos(pos1[i], pos2[i], pos3[i]));
		return result;
	}
	
	public static int booleanToInt(boolean bool){
		if(bool) return 1;
		return 0;
	}
	
	public static ColorM codeToColorF(int code){
		float[] data=codeToRGBABPercentage(code);
		return new ColorM(data[0], data[1], data[2], data[3]);
	}
	
	public static int[] codeToRGBABByte(int code){
		return colorToRGBABByte(new Color(code));
	}
	
	public static float[] codeToRGBABPercentage(int code){
		return colorToRGBABPercentage(new Color(code, true));
	}
	
	public static int colorToCode(Color color){
		return color.hashCode();
	}
	
	public static int[] colorToRGBABByte(Color color){
		return new int[]{color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
	}
	
	public static float[] colorToRGBABPercentage(Color color){
		int[] data=colorToRGBABByte(color);
		return new float[]{data[0]/255F, data[1]/255F, data[2]/255F, data[3]/255F};
	}
	
	public static float[] countedArray(float start, float end){
		float[] result=new float[(int)(end-start)];
		for(int i=0; i<result.length; i++)
			result[i]=i;
		return null;
	}
	
	public static int[] countedArray(int start, int end){
		int[] result=new int[end-start];
		for(int i=0; i<result.length; i++)
			result[i]=i+start;
		return result;
	}
	
	public static List<Vec3M> dotsOnRay(Vec3M start, Vec3M end, float differenceBetweenDots){
		List<Vec3M> result=new ArrayList<>();
		
		Vec3M difference=start.sub(end), direction=difference.normalize();
		
		float lenght=difference.length(), posMul=differenceBetweenDots;
		
		result.add(start);
		while(posMul<lenght){
			result.add(direction.mul(posMul).add(end));
			posMul+=differenceBetweenDots;
		}
		
		result.add(end);
		
		return result;
	}
	
	public static EntityItem dropBlockAsItem(World world, double x, double y, double z, ItemStack stack){
		if(!world.isRemote&&!world.restoringBlockSnapshots){
			EntityItem entity=new EntityItem(world, x, y, z, stack);
			entity.setPickupDelay(0);
			spawnEntity(entity);
			return entity;
		}
		return null;
	}
	
	private static final Stack<Long> timeStack=new Stack<>();
	
	public static void timerSafety(){
		if(!timeStack.isEmpty()) timeStack.clear();
		
	}
	
	public static long endTime(){
		
		return System.currentTimeMillis()-timeStack.pop();
	}
	
	public static void startTime(){
		timeStack.push(System.currentTimeMillis());
	}
	
	public static void exit(int exitCode){
		LogUtil.println(MReference.NAME, "exiting! o/");
		FMLCommonHandler.instance().exitJava(exitCode, false);
	}
	
	public static boolean FALSE(){
		return false;
	}
	
	public static Block getBlock(Worldabale worldContainer, BlockPos pos){
		return worldContainer.getWorld().getBlockState(pos).getBlock();
	}
	
	public static Block getBlock(IBlockAccess world, BlockPos pos){
		return world.getBlockState(pos).getBlock();
	}
	
	public static Block getBlock(IBlockAccess world, int x, int y, int z){
		return getBlock(world, new BlockPos(x, y, z));
	}
	
	public static double getDistance(Entity entity, int x, int y, int z){
		Vec3M entityPos=new Vec3M(entity.posX, entity.posY, entity.posZ);
		Vec3M blockPos=new Vec3M(x+0.5, y+0.5, z+0.5);
		return entityPos.distanceTo(blockPos);
	}
	
	public static double getDistance(TileEntity tile, int x, int y, int z){
		Vec3M entityPos=new Vec3M(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getY());
		Vec3M blockPos=new Vec3M(x+0.5, y+0.5, z+0.5);
		return entityPos.distanceTo(blockPos);
	}
	
	public static Vec3M getEntityPos(Entity entity){
		return new Vec3M(entity.posX, entity.posY, entity.posZ);
	}
	
	public static Vec3M getEyePosition(Entity entity){
		return getEntityPos(entity).addY(entity.getEyeHeight());
	}
	
	public static <T, E> T getMapKey(Map<T,E> map, E value){
		for(Entry<T,E> entry : map.entrySet())
			if(Objects.equals(value, entry.getValue())) return entry.getKey();
		return null;
	}
	
	public static <T, E> Set<T> getMapKeySet(Map<T,E> map, E value){
		return map.entrySet().stream().filter(entry->Objects.equals(entry.getValue(), value)).map(Map.Entry::getKey).collect(Collectors.toSet());
	}
	
	public static <T> int getPosInArray(T object, T[] array){
		if(isNull(object, array)) return -1;
		if(array.length==0||isArray(object)) return -1;
		int pos=-2;
		for(int a=0; a<array.length; a++)
			if(array[a]==object){
				pos=a;
				a=array.length;
			}
		return pos;
	}
	
	public static String getStackTrace(){
		StringBuilder Return=new StringBuilder();
		
		StackTraceElement[] a1=Thread.currentThread().getStackTrace();
		int length=0;
		DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal=Calendar.getInstance();
		Return.append("Invoke time: ").append(dateFormat.format(cal.getTime())).append("\n");
		for(int i=2; i<a1.length; i++){
			StackTraceElement a=a1[i];
			String s=a.toString();
			Return.append(s).append("\n");
			length=Math.max(s.length(), length);
		}
		for(int b=0; b<length/4; b++)
			Return.append("_/\\_");
		
		return Return.toString();
	}
	
	public static World world(Entity object){
		return object.world;
	}
	
	public static World world(TileEntity object){
		return object.getWorld();
	}
	
	public static World world(EntityEvent object){
		return object.getEntity().world;
	}
	
	public static World world(BlockEvent object){
		return object.getWorld();
	}
	
	public static World world(World object){
		return object;
	}
	
	public static World world(Worldabale object){
		return object.getWorld();
	}
	
	@Deprecated
	public static World world(Object object){
		if(object instanceof Entity) return ((Entity)object).world;
		if(object instanceof World) return (World)object;
		if(object instanceof TileEntity) return ((TileEntity)object).getWorld();
		if(object instanceof EntityEvent) return ((EntityEvent)object).getEntity().world;
		if(object instanceof BlockEvent) return ((BlockEvent)object).getWorld();
		
		return null;
	}
	
	public static long worldTime(Entity worldContainer){
		return world(worldContainer).getTotalWorldTime();
	}
	
	public static long worldTime(World worldContainer){
		return world(worldContainer).getTotalWorldTime();
	}
	
	public static long worldTime(TileEntity worldContainer){
		return world(worldContainer).getTotalWorldTime();
	}
	
	public static long worldTime(EntityEvent worldContainer){
		return world(worldContainer).getTotalWorldTime();
	}
	
	public static long worldTime(BlockEvent worldContainer){
		return world(worldContainer).getTotalWorldTime();
	}
	
	public static long worldTime(Worldabale worldContainer){
		return world(worldContainer).getTotalWorldTime();
	}
	
	@Deprecated
	public static long worldTime(Object worldContainer){
		return world(worldContainer).getTotalWorldTime();
	}
	
	public static boolean isRemote(Entity object){
		return world(object).isRemote;
	}
	
	public static boolean isRemote(TileEntity object){
		return world(object).isRemote;
	}
	
	public static boolean isRemote(EntityEvent object){
		return world(object).isRemote;
	}
	
	public static boolean isRemote(BlockEvent object){
		return world(object).isRemote;
	}
	
	public static boolean isRemote(World object){
		return world(object).isRemote;
	}
	
	public static boolean isRemote(Worldabale object){
		return world(object).isRemote;
	}
	
	@Deprecated
	public static boolean isRemote(Object object){
		World w=world(object);
		if(w!=null) return w.isRemote;
		else return isRemote();
	}
	
	public static boolean isRemote(){
		return FMLCommonHandler.instance().getEffectiveSide()==Side.CLIENT;
	}
	
	public static boolean peridOf(Entity object, int period){
		return peridOf(world(object), period);
	}
	
	public static boolean peridOf(TileEntity object, int period){
		return peridOf(world(object), period);
	}
	
	public static boolean peridOf(EntityEvent object, int period){
		return peridOf(world(object), period);
	}
	
	public static boolean peridOf(BlockEvent object, int period){
		return peridOf(world(object), period);
	}
	
	public static boolean peridOf(World world, int period){
		return (world==null?System.currentTimeMillis()/50:worldTime(world))%period==0;
	}
	
	public static double handleSpeedFolower(double speed, double pos, double wantedPos, double acceleration){
		if(pos==wantedPos) return speed;
		if(pos>wantedPos) speed-=acceleration;
		else speed+=acceleration;
		return speed;
	}
	
	public static float handleSpeedFolower(float speed, float pos, float wantedPos, float acceleration){
		return (float)handleSpeedFolower((double)speed, (double)pos, (double)wantedPos, (double)acceleration);
	}
	
	public static boolean instanceOf(Class toBeTested, Class instance){
		try{
			toBeTested.asSubclass(instance);
			return true;
		}catch(Exception ignored){
		}
		return false;
	}
	
	public static boolean instanceOf(Object toBeTested, Class instance){
		return toBeTested!=null&&instanceOf(toBeTested.getClass(), instance);
	}
	
	public static boolean instanceOf(Class toBeTested, Object instance){
		return instanceOf(toBeTested, instance.getClass());
	}
	
	public static boolean instanceOf(Object toBeTested, Object instance){
		return instanceOf(toBeTested.getClass(), instance.getClass());
	}
	
	public static boolean intToBoolean(int i){
		return i==1;
	}
	
	public static List<ItemStack> inventoryToList(IInventory inv){
		List<ItemStack> result=new ArrayList<>();
		int size=inv.getSizeInventory();
		for(int i=0; i<size; i++)
			result.add(inv.getStackInSlot(i));
		return result;
	}
	
	public static boolean isAny(Object tester, Object... objects){
		for(Object object : objects)
			if(tester==object) return true;
		return false;
	}
	
	public static boolean isArray(Object object){
		return object!=null&&object.getClass().isArray();
	}
	
	public static boolean isBoolean(String str){
		return str!=null&&(str.equals("true")||str.equals("false"));
	}
	
	public static <T> boolean isInArray(T object, T[] array){
		return getPosInArray(object, array)>=0;
	}
	
	public static boolean isInteger(String str){
		if(str==null) return false;
		int length=str.length();
		if(length==0) return false;
		int i=0;
		if(str.charAt(0)=='-'){
			if(length==1) return false;
			i=1;
		}
		for(; i<length; i++){
			char c=str.charAt(i);
			if(c<='/'||c>=':') return false;
		}
		return true;
	}
	
	/**
	 * Returns if stack contains a specific item
	 * Note: no danger of null pointer exception!
	 * @param itemClass
	 * @param stack a
	 * @return a
	 */
	public static boolean isItemInStack(Class<? extends Item> itemClass, ItemStack stack){
		if(stack==null) return false;
		Item item=stack.getItem();
		if(item==null) return false;
		return item.getClass()==itemClass;
	}
	/**
	 * 
	 * use {@link ItemM#isInStack(ItemStack)}
	 * 
	 * @param item
	 * @param stack
	 * @return
	 */
	@Deprecated
	public static boolean isItemInStack(ItemM item, ItemStack stack){
		return item.isInStack(stack);
	}
	public static boolean isItemInStack(Item item, ItemStack stack){
		if(stack==null) return false;
		return stack.getItem()==item;
	}
	
	/**
	 * Returns false if all objects are not null and it returns true if any of object/s are true
	 * Note: you'll might need to add "!" on using it
	 * @param objects a
	 * @return a
	 */
	public static boolean isNull(Object... objects){
		for(Object object : objects)
			if(object==null) return true;
		return false;
	}
	
	public static boolean isTileInWorld(TileEntity tile){
		if(!tile.hasWorld()) return false;
		return tile.getWorld().getTileEntity(tile.getPos())==tile;
	}
	
	public static String join(CharSequence splitter, Object[] args){
		StringBuilder result=new StringBuilder();
		for(Object o : args)
			result.append(o).append(splitter);
		return result.substring(0, result.length()-splitter.length());
	}
	
	public static String join(Object[] args){
		StringBuilder result=new StringBuilder();
		for(Object o : args)
			result.append(o);
		return result.toString();
	}
	
	public static Object objFromString(String s) throws IOException, ClassNotFoundException{
		byte[] data=Base64.getDecoder().decode(s);
		ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(data));
		Object o=ois.readObject();
		ois.close();
		return o;
	}
	
	public static String objToString(Serializable o) throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
	
	public static boolean playerEqual(EntityPlayer player, EntityPlayer player2){
		if(player==null||player2==null) return false;
		return player.getGameProfile().getId().equals(player2.getGameProfile().getId());
	}
	
	public static void playSoundAtEntity(SoundM sound, Entity entity, float volume, float pitch){
		playSoundAtEntity(sound, entity.world, getEntityPos(entity), volume, pitch);
	}
	
	public static void playSoundAtEntity(SoundM sound, World world, Vec3M pos, float volume, float pitch){
		if(world.isRemote) return;
		world.playSound(pos.x(), pos.y(), pos.z(), sound.toSoundEvent(), sound.category, volume, pitch, false);
	}
	
	public static void printTime(){
		LogUtil.println(endTime());
	}
	
	public static int rgbByteToCode(int r, int g, int b, int alpha){
		return colorToCode(new Color(r, g, b, alpha));
	}
	
	public static int rgbPercentageToCode(double r, double g, double b, double alpha){
		int r1=(int)(255*r), g1=(int)(255*g), b1=(int)(255*b), alpha1=(int)(255*alpha);
		return rgbByteToCode(r1, g1, b1, alpha1);
	}
	
	public static float round(float decimal, int decimalPlace){
		return BigDecimal.valueOf(decimal).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	public static void setBlock(World world, BlockPos pos, Block block){
		world.setBlockState(pos, block.getDefaultState(), 3);
	}
	
	public static String signature(){
		return signature(RESET);
	}
	
	public static String signature(ChatFormatting... colorAfter){
		String result=GOLD+"["+ChatFormatting.DARK_GREEN+MReference.NAME+GOLD+"] ";
		for(ChatFormatting a : colorAfter)
			result+=a;
		return result;
	}
	
	public static void sleep(int time){
		try{
			Thread.sleep(time);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static ColorM graduallyEqualize(ColorM variable, ColorM goal, float speed){
		return new ColorM(graduallyEqualize(variable.r(), goal.r(), speed), graduallyEqualize(variable.g(), goal.g(), speed),
						  graduallyEqualize(variable.b(), goal.b(), speed), graduallyEqualize(variable.a(), goal.a(), speed));
	}
	
	public static float[] exponentiallyEqualize(float[] variable, float[] goal, float speed){
		float[] result=new float[variable.length];
		for(int i=0; i<result.length; i++)
			result[i]=exponentiallyEqualize(variable[i], goal[i], speed);
		return result;
	}
	
	public static float exponentiallyEqualize(float variable, float goal, float speed){
		if(speed==0) return goal;
		return (variable*speed+goal)/(speed+1);
	}
	
	public static double exponentiallyEqualize(double variable, double goal, double speed){
		if(speed==0) return goal;
		return (variable*speed+goal)/(speed+1);
	}
	
	public static float[] graduallyEqualize(float[] variable, float[] goal, float speed){
		float[] result=new float[variable.length];
		for(int i=0; i<result.length; i++)
			result[i]=graduallyEqualize(variable[i], goal[i], speed);
		return result;
	}
	
	public static float graduallyEqualize(float variable, float goal, float speed){
		return (float)graduallyEqualize((double)variable, (double)goal, (double)speed);
	}
	
	public static double graduallyEqualize(double variable, double goal, double speed){
		if(speed==0) return variable;
		speed=Math.abs(speed);
		if(variable+speed>goal&&Math.abs(variable+speed-goal)<speed*1.001) return goal;
		if(variable-speed<goal&&Math.abs(variable-speed-goal)<speed*1.001) return goal;
		
		if(variable<goal) variable+=speed;
		else if(variable>goal) variable-=speed;
		return variable;
	}
	
	public static Entity spawnEntity(Entity entity){
		if(isRemote(entity)) return null;
		entity.world.spawnEntity(entity);
		entity.forceSpawn=true;
		return entity;
	}
	
	public static String[] stringNewlineSplit(String toSplit){
		return toSplit.split("\\r\\n|\\n\\r|\\r|\\n");
	}
	
	public static TargetPoint TargetPoint(TileEntity tile, int range){
		return new TargetPoint(tile.getWorld().provider.getDimension(), x(tile), y(tile), z(tile), range);
	}
	
	public static String toString(Object... objs){
		StringBuilder print=new StringBuilder();
		
		if(objs!=null) for(int i=0; i<objs.length; i++){
			Object a=objs[i];
			if(isArray(a)) print.append(unknownArrayToString(a));
			else if(a instanceof FloatBuffer) print.append(floatBufferToString((FloatBuffer)a));
			else print.append(toString(a)+(i==objs.length-1?"":" "));
		}
		else print.append("null");
		
		return print.toString();
	}
	
	public static String toString(Object obj){
		StringBuilder print=new StringBuilder();
		
		if(obj!=null){
			if(isArray(obj)) print.append(unknownArrayToString(obj));
			else if(obj instanceof FloatBuffer) print.append(floatBufferToString((FloatBuffer)obj));
			else print.append(obj.toString());
		}else print.append("null");
		
		return print.toString();
	}
	
	private static String unknownArrayToString(Object arr){
		if(arr instanceof boolean[]) return Arrays.toString((boolean[])arr);
		if(arr instanceof float[]) return Arrays.toString((float[])arr);
		if(arr instanceof byte[]) return Arrays.toString((byte[])arr);
		if(arr instanceof int[]) return Arrays.toString((int[])arr);
		if(arr instanceof long[]) return Arrays.toString((long[])arr);
		if(arr instanceof short[]) return Arrays.toString((short[])arr);
		if(arr instanceof char[]) return Arrays.toString((char[])arr);
		if(arr instanceof double[]) return Arrays.toString((double[])arr);
		if(arr instanceof Object[]) return Arrays.toString((Object[])arr);
		return "ERR: "+arr;
	}
	
	public static boolean TRUE(){
		return true;
	}
	
	public static int x(TileEntity tile){
		return tile.getPos().getX();
	}
	
	public static int y(TileEntity tile){
		return tile.getPos().getZ();
	}
	
	public static int z(TileEntity tile){
		return tile.getPos().getY();
	}
	
	public static EnumActionResult booleanToActionResult(boolean b){
		return b?EnumActionResult.SUCCESS:EnumActionResult.FAIL;
	}
	
	public static boolean isIdInArray(Object[] array, int id){
		return array.length>0&&id>=0&&id<array.length;
	}
	
	/**
	 * @param name=ThisIs_A_NOTStandardizedName
	 * @return this_is_a_not_standardized_name
	 */
	public static String standardizeName(String name){
		StringBuilder result=new StringBuilder();
		char[] src=name.toCharArray();
		
		for(int i=0; i<src.length; i++){
			char c=src[i];
			if(i>0&&Character.isUpperCase(c)){
				char prev=src[i-1];
				
				if(prev!='_'){
					boolean isPrevLower=Character.isLowerCase(prev);
					if(i+1<src.length&&Character.isLowerCase(src[i+1])&&!isPrevLower) result.append('_');
					else if(isPrevLower) result.append('_');
				}
			}
			result.append(Character.toLowerCase(c));
		}
		
		return result.toString();
	}
	
	public static String removeMcObjectEnd(String name){
		String lower=name.toLowerCase();
		if(lower.endsWith("block")) name=name.substring(0, name.length()-"block".length());
		if(lower.endsWith("tileentity")) name=name.substring(0, name.length()-"tileentity".length());
		if(lower.endsWith("entity")) name=name.substring(0, name.length()-"entity".length());
		if(lower.endsWith("item")) name=name.substring(0, name.length()-"item".length());
		
		if(lower.startsWith("block")) name=name.substring("block".length());
		if(lower.startsWith("tileentity")) name=name.substring("tileentity".length());
		if(lower.startsWith("entity")) name=name.substring("entity".length());
		if(lower.startsWith("item")) name=name.substring("item".length());
		return name;
	}
	
	public static String classNameToMcName(String name){
		return standardizeName(removeMcObjectEnd(name));
	}
	
	public static String classNameToMcName(Class clazz){
		return classNameToMcName(clazz.getSimpleName());
	}
	
	final protected static char[] hexArray="0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes){
		char[] hexChars=new char[bytes.length*2];
		for(int j=0; j<bytes.length; j++){
			int v=bytes[j]&0xFF;
			hexChars[j*2]=hexArray[v>>>4];
			hexChars[j*2+1]=hexArray[v&0x0F];
		}
		return new String(hexChars);
	}
	
	public static void writeStacksToNBT(ItemStack[] stacks, NBTTagCompound compound, String baseName){
		NBTTagList nbtStacks=new NBTTagList();
		for(int i=0; i<stacks.length; i++)
			if(stacks[i]!=null){
				NBTTagCompound stackNbt=new NBTTagCompound();
				stackNbt.setInteger("Slot", i);
				stacks[i].writeToNBT(stackNbt);
				nbtStacks.appendTag(stackNbt);
			}
		compound.setTag(baseName, nbtStacks);
		
	}
	
	public static ItemStack[] readStacksFromNBT(NBTTagCompound compound, String baseName){
		NBTTagList nbtStacks=compound.getTagList(baseName, 10);
		ItemStack[] stacks=new ItemStack[nbtStacks.tagCount()];
		for(int i=0; i<stacks.length; i++){
			NBTTagCompound stackNbt=nbtStacks.getCompoundTagAt(i);
			stacks[stackNbt.getInteger("Slot")]=new ItemStack(stackNbt);
		}
		return stacks;
	}
	
	public static <T extends Worldabale&Locateable<? extends BlockPos>> List<TileEntity> getTileSides(T worldPointer){
		return getTileSides(worldPointer.getWorld(), worldPointer.getPos());
	}
	
	public static List<TileEntity> getTileSides(World worldObj, BlockPos pos){
		List<TileEntity> list=new ArrayList<>();
		
		for(EnumFacing side : EnumFacing.values()){
			TileEntity tile=worldObj.getTileEntity(pos.offset(side));
			if(tile!=null) list.add(tile);
		}
		
		return list;
	}
	
	public static <T extends Worldabale&Locateable<? extends BlockPos>> Map<EnumFacing,TileEntity> getTileSidesDir(T worldPointer){
		return getTileSidesDir(worldPointer.getWorld(), worldPointer.getPos());
	}
	
	public static Map<EnumFacing,TileEntity> getTileSidesDir(World worldObj, BlockPos pos){
		Map<EnumFacing,TileEntity> map=new HashMap<>();
		
		for(EnumFacing side : EnumFacing.values()){
			TileEntity tile=worldObj.getTileEntity(pos.offset(side));
			if(tile!=null) map.put(side, tile);
		}
		
		return map;
	}
	
	public static <J extends Worldabale&Locateable<? extends BlockPosM>, T> List<T> getTileSides(J worldPointer, Class<T> type){
		return getTileSides(worldPointer.getWorld(), worldPointer.getPos(), type);
	}
	
	public static <T> List<T> getTileSides(World worldObj, BlockPosM pos, Class<T> type){
		List<T> list=new ArrayList<>();
		
		for(EnumFacing side : EnumFacing.values()){
			T tile=pos.offset(side).getTile(worldObj, type);
			if(tile!=null) list.add(tile);
		}
		
		return list;
	}
	
	public static <J extends Worldabale&Locateable<? extends BlockPosM>, T extends TileEntity> Map<EnumFacing,T> getTileSidesDir(J worldPointer,
																																 Class<T> type){
		return getTileSidesDir(worldPointer.getWorld(), worldPointer.getPos(), type);
	}
	
	public static <T extends TileEntity> Map<EnumFacing,T> getTileSidesDir(World worldObj, BlockPosM pos, Class<T> type){
		Map<EnumFacing,T> map=new HashMap<>();
		
		for(EnumFacing side : EnumFacing.values()){
			TileEntity tile=pos.offset(side).getTile(worldObj, type);
			if(tile!=null) map.put(side, (T)tile);
		}
		
		return map;
	}
	
	public static String localize(String input, Object... format){
		return I18n.translateToLocalFormatted(input, format);
	}
	
	public static float fluctuateLin(World world, double speed, double offset, double min, double max){
		if(min==max) return (float)min;
		
		double wtt=world.getTotalWorldTime()+offset;
		double value=wtt%speed/(speed/2F);
		double precent=value>1?2-value:value;
		return (float)(min+(max-min)*precent);
	}
	
	public static float fluctuateExp(World world, double speed, double offset, double min, double max){
		if(min==max) return (float)min;
		
		speed*=20;
		
		double wtt=world.getTotalWorldTime()+offset;
		double value=wtt%speed/(speed/2F);
		double precent=Math.sin((value>1?2-value:value)*Math.PI);
		return (float)(min+(max-min)*precent);
	}
	
	public static <T> T[] mixedToArray(Class<T> type, Object... data){
		List<T> list=new ArrayList<>();
		add(list, data);
		return list.toArray((T[])Array.newInstance(type, list.size()));
	}
	
	private static <T> void add(List<T> list, Object o){
		if(o==null){
			list.add(null);
			return;
		}
		if(o.getClass().isArray()){
			for(Object object : (Object[])o)
				add(list, object);
			return;
		}
		list.add((T)o);
	}
	
	public static <E, T extends Collection<E>> T removeCollectionDuplicates(T collection){
		Set<E> hs=new HashSet<>(collection);
		
		if(hs.size()!=collection.size()){
			collection.clear();
			collection.addAll(hs);
		}
		
		return collection;
	}
	
	public static <T> T[] newArray(Class<T> type, int size){
		return (T[])Array.newInstance(type, size);
	}
	
	private static enum COLOR_WORDS{
		WHITE(new int[]{255, 255, 255, 255}),
		LIGHT_GRAY(new int[]{192, 192, 192, 255}),
		GRAY(new int[]{128, 128, 128, 255}),
		DARK_GRAY(new int[]{64, 64, 64, 255}),
		BLACK(new int[]{0, 0, 0, 255}),
		RED(new int[]{255, 0, 0, 255}),
		PINK(new int[]{255, 175, 175, 255}),
		ORANGE(new int[]{255, 200, 0, 255}),
		YELLOW(new int[]{255, 255, 0, 255}),
		GREEN(new int[]{0, 255, 0, 255}),
		MAGENTA(new int[]{255, 0, 255, 255}),
		CYAN(new int[]{0, 255, 255, 255}),
		BLUE(new int[]{0, 0, 255, 255});
		
		private final int[] data;
		
		private COLOR_WORDS(int[] data){
			this.data=data;
		}
		
	}
	
	private static int[] parseColor(String s){
		if(s==null||s.length()<4) return COLOR_WORDS.WHITE.data;
		
		if(s.charAt(0)=='#'){
			
			s=s.substring(1);
			
			switch(s.length()){
			case 3:
				return new int[]{//RGB
								 Integer.parseInt(s.charAt(0)+""+s.charAt(0), 16),
								 Integer.parseInt(s.charAt(1)+""+s.charAt(1), 16),
								 Integer.parseInt(s.charAt(2)+""+s.charAt(2), 16),
								 255
				};
			case 4:
				return new int[]{//RGBA
								 Integer.parseInt(s.charAt(0)+""+s.charAt(0), 16),
								 Integer.parseInt(s.charAt(1)+""+s.charAt(1), 16),
								 Integer.parseInt(s.charAt(2)+""+s.charAt(2), 16),
								 Integer.parseInt(s.charAt(3)+""+s.charAt(3), 16),
								 };
			case 6:
				return new int[]{//RRGGBB
								 Integer.parseInt(s.substring(0, 1), 16),
								 Integer.parseInt(s.substring(2, 3), 16),
								 Integer.parseInt(s.substring(4, 5), 16),
								 255
				};
			case 8:
				return new int[]{//RRGGBBAA
								 Integer.parseInt(s.substring(0, 1), 16),
								 Integer.parseInt(s.substring(2, 3), 16),
								 Integer.parseInt(s.substring(4, 5), 16),
								 Integer.parseInt(s.substring(6, 7), 16)
				};
			}
			return COLOR_WORDS.WHITE.data;
		}else{
			try{
				return COLOR_WORDS.valueOf(s.toUpperCase()).data;
			}catch(Exception e){
				return COLOR_WORDS.WHITE.data;
			}
		}
	}
	
	public static TileEntity getTile(World world, Locateable<? extends BlockPos> pos){
		return world.getTileEntity(pos.getPos());
	}
	
	public static <T extends Worldabale&Locateable<? extends BlockPos>> TileEntity getTile(T worldPointer){
		return worldPointer.getWorld().getTileEntity(worldPointer.getPos());
	}
	
	public static <T extends Worldabale&Locateable<? extends BlockPos>> IBlockState getState(T worldPointer){
		return worldPointer.getWorld().getBlockState(worldPointer.getPos());
	}
	
	public static <T extends Worldabale&Locateable<? extends BlockPos>> Block getBlock(T worldPointer){
		return getState(worldPointer).getBlock();
	}

	public static int howFarUnderI(Entity entity){
		return (int)Math.floor(howFarUnder(entity));
	}
	public static double howFarUnder(Entity entity){
		double y=entity.posY;
		if(y<0)return Double.POSITIVE_INFINITY;
		
		World w=entity.getEntityWorld();
		
		MutableBlockPos pos=new MutableBlockPos(MathHelper.floor(entity.posX),Math.min(MathHelper.floor(y), 256),MathHelper.floor(entity.posZ));
		IBlockState state=null;
		
		while(pos.getY()>0){
			
			state=w.getBlockState(pos);
			
			if(state.getCollisionBoundingBox(w, pos)==null){
				pos.move(EnumFacing.DOWN);
				
				if(pos.getY()==0){
					state=null;
					break;
				}
			}else break;
		}
		
		double hitY;
		
		if(state!=null)hitY=state.getBoundingBox(w, pos).maxY;
		else hitY=pos.getY();
		
		
		return Math.max(hitY-y, 0);
	}
	
}
