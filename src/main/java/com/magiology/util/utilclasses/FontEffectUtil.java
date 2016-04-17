package com.magiology.util.utilclasses;

import com.mojang.realmsclient.gui.ChatFormatting;

/**
 * FontEffectHelper
 * */
public class FontEffectUtil{
	public static ChatFormatting AQUA=ChatFormatting.AQUA;
	public static ChatFormatting BLACK=ChatFormatting.BLACK;
	public static ChatFormatting BLUE=ChatFormatting.BLUE;
	public static ChatFormatting BOLD=ChatFormatting.BOLD;
	public static ChatFormatting DARK_AQUA=ChatFormatting.DARK_AQUA;
	public static ChatFormatting DARK_BLUE=ChatFormatting.DARK_BLUE;
	public static ChatFormatting DARK_GRAY=ChatFormatting.DARK_GRAY;
	public static ChatFormatting DARK_GREEN=ChatFormatting.DARK_GREEN;
	public static ChatFormatting DARK_PURPLE=ChatFormatting.DARK_PURPLE;
	public static ChatFormatting DARK_RED=ChatFormatting.DARK_RED;
	public static ChatFormatting GOLD=ChatFormatting.GOLD;
	public static ChatFormatting GRAY=ChatFormatting.GRAY;
	public static ChatFormatting GREEN=ChatFormatting.GREEN;
	public static ChatFormatting ITALIC=ChatFormatting.ITALIC;
	public final static int length=22;
	public static ChatFormatting LIGHT_PURPLE=ChatFormatting.LIGHT_PURPLE;
	public static ChatFormatting OBFUSCATED=ChatFormatting.OBFUSCATED;
	public static ChatFormatting RED=ChatFormatting.RED;
	public static ChatFormatting RESET=ChatFormatting.RESET;
	public static ChatFormatting STRIKETHROUGH=ChatFormatting.STRIKETHROUGH;
	public static ChatFormatting UNDERLINE=ChatFormatting.UNDERLINE;
	public static ChatFormatting WHITE=ChatFormatting.WHITE;
	public static ChatFormatting YELLOW=ChatFormatting.YELLOW;
	
	public static ChatFormatting getEffById(int id){
		switch (id){
		case 0: return AQUA;
		case 1: return BLACK;
		case 2: return BLUE;
		case 3: return BOLD;
		case 4: return DARK_AQUA;
		case 5: return DARK_BLUE;
		case 6: return DARK_GRAY;
		case 7: return DARK_GREEN;
		case 8: return DARK_PURPLE;
		case 9: return DARK_RED;
		case 10:return GOLD;
		case 11:return GRAY;
		case 12:return GREEN;
		case 13:return ITALIC;
		case 14:return LIGHT_PURPLE;
		case 15:return OBFUSCATED;
		case 16:return RED;
		case 17:return RESET;
		case 18:return STRIKETHROUGH;
		case 19:return UNDERLINE;
		case 20:return WHITE;
		case 21:return YELLOW;
		}
		return null;
	}
	public static ChatFormatting getRandColor(){
		int i=0;
		do{i=RandUtil.RI(length);}while(i==13||i==15||i==17||i==18||i==19);
		return getEffById(i);
	}
	
	public static ChatFormatting getRandEff(){
		return getEffById(RandUtil.RI(length));
	}
	public static ChatFormatting getRandEffect(){
		int i[]={13,15,17,18,19};
		return getEffById(i[RandUtil.RI(i.length)]);
	}
	
}
