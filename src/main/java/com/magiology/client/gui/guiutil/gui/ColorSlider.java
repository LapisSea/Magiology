package com.magiology.client.gui.guiutil.gui;

import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.ColorF;

public class ColorSlider{
	private static ColorF[] image;
	static{
		int[] imageRaw={-64768,-60928,-56832,-52736,-48896,-45312,-41216,-37120,-33536,-29440,-25344,-21760,-17664,-13568,-9984,-5888,-2048,-590080,-1573120,-2621696,-3670272,-4587776,-5636352,-6684928,-7602432,-8585472,-9634048,-10682624,-11600128,-12648704,-13697280,-14614784,-15663360,-16580863,-16711922,-16711908,-16711892,-16711876,-16711862,-16711846,-16711830,-16711816,-16711800,-16711784,-16711769,-16711755,-16711739,-16711723,-16711709,-16711693,-16712705,-16716545,-16720641,-16724737,-16728321,-16732417,-16736257,-16740353,-16743937,-16748033,-16752129,-16755713,-16759809,-16763905,-16767489,-16771329,-16775425,-16252673,-15269633,-14221057,-13172481,-12254977,-11206401,-10157825,-9240321,-8191745,-7208705,-6160129,-5242625,-4194049,-3145473,-2227969,-1179393,-196355,-65295,-65310,-65326,-65342,-65356,-65372,-65388,-65402,-65418,-65434,-65448,-65463,-65479,-65495,-65509,-65525};
		image=new ColorF[imageRaw.length];
		for(int i=0;i<image.length;i++)image[i]=UtilM.codeToColorF(imageRaw[i]);
	}
	public static ColorF getColor(float a){
		a=MathUtil.snap(a, 0, 1);
		return image[(int)(image.length*a)];
	}
	public static ColorF getColor(int a){
		if(a>=image.length){
			a=0;
			PrintUtil.println("Invalid color id!");
		}
		return image[a];
	}
}
