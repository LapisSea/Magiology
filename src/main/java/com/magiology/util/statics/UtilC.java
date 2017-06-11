package com.magiology.util.statics;

import com.magiology.util.objs.vec.Vec2i;
import com.magiology.util.statics.math.PartialTicksUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

@SideOnly(Side.CLIENT)
public class UtilC{
	
	public static final FloatBuffer BUF=BufferUtils.createFloatBuffer(16);
	
	public static Minecraft getMC(){
		return Minecraft.getMinecraft();
	}
	
	public static EntityPlayerSP getThePlayer(){
		return getMC().player;
	}
	
	public static WorldClient getTheWorld(){
		return getMC().world;
	}
	
	public static long getWorldTime(){
		return getTheWorld().getTotalWorldTime();
	}
	
	public static boolean isWorldOpen(){
		return getTheWorld()!=null;
	}
	
	public static float fluctuateLin(double speed, double offset, double min, double max){
		return UtilM.fluctuateLin(getTheWorld(), speed, offset, min, max);
	}
	
	public static float fluctuateExp(double speed, double offset, double min, double max){
		return UtilM.fluctuateExp(getTheWorld(), speed, offset, min, max);
	}
	
	public static float fluctuateLinSmooth(double speed, double offset, double min, double max){
		float fluctuate=fluctuateLin(speed, offset, min, max), prevFluctuate=fluctuateLin(speed, offset-1, min, max);
		return PartialTicksUtil.calculate(prevFluctuate, fluctuate);
	}
	
	public static float fluctuateExpSmooth(double speed, double offset, double min, double max){
		return UtilM.fluctuateExp(getTheWorld(), speed, offset, min, max);
	}
	
	public static float fluctuateLin_0_1(double speed, double offset){
		return fluctuateLin(speed, offset, 0, 1);
	}
	
	public static float fluctuateLin_N1_1(double speed, double offset){
		return fluctuateLin(speed, offset, -1, 1);
	}
	
	public static void exitSoft(){
		getMC().shutdown();
	}
	
	public static int getFPS(){
		return Minecraft.getDebugFPS();
	}
	
	public static float getGuiScale(){
		return Math.max(getGuiScaleRaw()/4F, 1);
	}
	
	public static int getGuiScaleRaw(){
		return new ScaledResolution(getMC()).getScaleFactor();
	}
	
	public static Vec2i[] arrangeStringsForGui(final String[] strings, int lines, int marginX, int marginY){
		FontRenderer fr=getMC().fontRendererObj;
		Vec2i[] result=new Vec2i[strings.length];
		int columns=(int)Math.floor(strings.length/(float)lines)+1;
		String[][] formattedStrings=new String[columns][lines];
		
		int[] longestInColumn=new int[columns], columnOffsets=new int[columns];
		for(int i=0; i<columns; i++){
			for(int j=0; j<lines; j++){
				int id=i*(columns+1)+j;
				if(id<strings.length) formattedStrings[i][j]=strings[id];
			}
		}
		for(int i=0; i<formattedStrings.length; i++)
			while(ArrayUtils.contains(formattedStrings[i], null)) formattedStrings[i]=ArrayUtils.removeElement(formattedStrings[i], null);
		for(int i=0; i<columns; i++)
			for(int j=0; j<formattedStrings[i].length; j++)
				longestInColumn[i]=Math.max(longestInColumn[i], fr.getStringWidth(formattedStrings[i][j]));
		for(int i=0; i<columns; i++){
			columnOffsets[i]=marginX;
			for(int j=0; j<i; j++)
				columnOffsets[i]+=longestInColumn[j]+marginX;
		}
		
		for(int i=0; i<strings.length; i++)
			result[i]=new Vec2i(columnOffsets[i/lines%columns], i%lines*(fr.FONT_HEIGHT+marginY)+marginY);
		return result;
	}
	
	public static String getStringForSize(String text, float allowedWidth){
		if(text.isEmpty()) return text;
		String Return=""+text;
		FontRenderer font=getMC().fontRendererObj;
		while(font.getStringWidth(Return)>allowedWidth){
			Return=Return.substring(0, Return.length()-1);
		}
		return Return;
	}
	
	public static Entity getViewEntity(){
		return getMC().getRenderViewEntity();
	}
	
	public static EntityRenderer getER(){
		return Minecraft.getMinecraft().entityRenderer;
	}
	
	public static boolean peridOf(int period){
		return UtilM.peridOf(getTheWorld(), period);
	}
	
	public static ItemRenderer getIR(){
		return getMC().getItemRenderer();
	}
	
	public static RenderItem getRI(){
		return getMC().getRenderItem();
	}
	
	public static void loadModelViewToBuffer(){
		BUF.clear();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, BUF);
	}
	
	public static Matrix4f getModelView(){
		loadModelViewToBuffer();
		Matrix4f mat=new Matrix4f();
		mat.load(BUF);
		return mat;
	}
}
