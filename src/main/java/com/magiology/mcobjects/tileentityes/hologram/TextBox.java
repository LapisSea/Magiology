package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.ColorF;

import net.minecraft.entity.player.EntityPlayer;

public class TextBox extends HoloObject implements StringContainer{
	
	public String txt;
	
	public TextBox(){}
	public TextBox(TileEntityHologramProjector host,String txt){
		super(host);
		this.txt=txt;
		scale=1F;
	}
	@Override
	public String getString(){
		return txt;
	}

	@Override
	public boolean isFullBlown(){
		return false;
	}
	@Override
	public boolean isTextLimitedToObj(){
		return false;
	}
	@Override
	public void onPressed(EntityPlayer player){
		
	}
	@Override
	public void readData(Iterator<Integer> integers, Iterator<Boolean> booleans, Iterator<Byte> bytes___, Iterator<Long> longs___,Iterator<Double> doubles_, Iterator<Float> floats__, Iterator<String> strings_, Iterator<Short> shorts__){
		super.readData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		txt=strings_.next();
	}
	@Override
	public void render(ColorF color){
		GL11U.glColor(U.calculateRenderColor(prevColor, this.color));
		GL11U.glCulFace(false);
		checkHighlight();
		GL11U.glScale(-scale*U.p);
		TessUtil.getFontRenderer().drawString(txt, 0, 0, this.setColor.toCode());
		GL11U.glCulFace(true);
	}
	@Override
	public void sendCommand(){}
	@Override
	public void setString(String string){
		txt=string;
	}
	
	@Override
	public void update(){
		prevColor=color.copy();
		size=new Vector2f(originalSize);
		if(isHighlighted||moveMode)color=U.slowlyEqalizeColor(color, new ColorF(1,1,1,0.6).mix(setColor), 0.15F);
		else color=U.slowlyEqalizeColor(color, setColor, 0.15F);
		
		size.x=TessUtil.getFontRenderer().getStringWidth(txt)*scale*U.p;
		size.y=TessUtil.getFontRenderer().FONT_HEIGHT*scale*U.p;
	}
	
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_,List<Float> floats__, List<String> strings_, List<Short> shorts__){
		super.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		strings_.add(txt);
	}
}
