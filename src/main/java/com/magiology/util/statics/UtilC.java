package com.magiology.util.statics;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.util.objs.Vec2i;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UtilC{

	public static Minecraft getMC(){
		return Minecraft.getMinecraft();
	}

	public static EntityPlayer getThePlayer(){
		return getMC().thePlayer;
	}

	public static World getTheWorld(){
		return getMC().theWorld;
	}

	public static long getWorldTime(){
		return getTheWorld().getTotalWorldTime();
	}

	public static boolean isWorldOpen(){
		return getTheWorld()!=null;
	}

	public static float fluctuate(double speed, double offset){
		double wtt=getWorldTime()+offset;
		double helper=(wtt%speed)/(speed/2F);
		return (float) (helper>1?2-helper:helper);
	}

	public static float fluctuateSmooth(double speed, double offset){
		float
			fluctuate=fluctuate(speed, offset),
			prevFluctuate=fluctuate(speed, offset-1);
		return PartialTicksUtil.calculate(prevFluctuate, fluctuate);
	}

	public static void exitSoft(){
		getMC().shutdown();
	}

	public static int getFPS(){
		return Minecraft.getDebugFPS();
	}

	public static float getGuiScale(){
		return Math.max(UtilC.getGuiScaleRaw()/4F,1);
	}

	public static int getGuiScaleRaw(){
		return new ScaledResolution(getMC()).getScaleFactor();
	}

	public static Vec2i[] arrangeStringsForGui(final String[]strings,int lines,int marginX,int marginY){
		FontRenderer fr=getMC().fontRendererObj;
		Vec2i[] result=new Vec2i[strings.length];
		int columns=(int)Math.floor(strings.length/(float)lines)+1;
		String[][] formattedStrings=new String[columns][lines];
		
		
		int[] longestInColumn=new int[columns],columnOffsets=new int[columns];
		for(int i=0;i<columns;i++){
			for(int j=0;j<lines;j++){
				int id=i*(columns+1)+j;
				if(id<strings.length)formattedStrings[i][j]=strings[id];
			}
		}
		for(int i=0;i<formattedStrings.length;i++)while(ArrayUtils.contains(formattedStrings[i], null))formattedStrings[i]=ArrayUtils.removeElement(formattedStrings[i], null);
		for(int i=0;i<columns;i++)for(int j=0;j<formattedStrings[i].length;j++)longestInColumn[i]=Math.max(longestInColumn[i], fr.getStringWidth(formattedStrings[i][j]));
		for(int i=0;i<columns;i++){
			columnOffsets[i]=marginX;
			for(int j=0;j<i;j++)columnOffsets[i]+=longestInColumn[j]+marginX;
		}
		
		for(int i=0;i<strings.length;i++)result[i]=new Vec2i(columnOffsets[(i/lines)%columns], (i%lines)*(fr.FONT_HEIGHT+marginY)+marginY);
		return result;
	}

	public static String getStringForSize(String text, float allowedWidth){
		if(text.isEmpty())return text;
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

}
