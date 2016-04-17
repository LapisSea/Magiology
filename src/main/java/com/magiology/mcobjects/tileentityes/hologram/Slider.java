package com.magiology.mcobjects.tileentityes.hologram;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.mcobjects.tileentityes.hologram.interactions.AbstractInteraction;
import com.magiology.mcobjects.tileentityes.hologram.interactions.InteractionSlide;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

import net.minecraft.entity.player.EntityPlayer;

public class Slider extends HoloObject{
	
	public CubeModel main, scroll;
	public PhysicsFloat renderSliderPos=new PhysicsFloat(0, 0.2F, true);
	public float sliderPos;
	
	public Slider(){}
	public Slider(TileEntityHologramProjector tile, Vector2f siz){
		super(tile);
		originalSize=siz;
	}

	@Override
	public void drawHighlight(){
		VertexRenderer buff=TessUtil.getVB();
		float offset=-renderSliderPos.getPoint()*size.y;
		CubeModel sliderBox=new CubeModel(scroll).expand(0.002F).translate(0,offset,0);
		
		buff.pushMatrix();
		buff.setDrawAsWire(true);
		buff.cleanUp();
		
		buff.translate(position.x, position.y, 0);
		
		buff.importComplexCube(new CubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2).expand(0.002F),sliderBox);
		
		buff.draw();
		
		buff.setDrawAsWire(false);
		buff.popMatrix();
	}

	@Override
	public void getInteractions(List<AbstractInteraction<HoloObject>> interations){
		super.getInteractions(interations);
		interations.add(new InteractionSlide<HoloObject>());
	}

	public float getSliderPrecentqage(){
		return UtilM.round(sliderPos*(1F/0.75F), 6);
	}
	
	@Override
	public List<DoubleObject<String,Object>> getStandardVars(){
		List<DoubleObject<String,Object>> result=super.getStandardVars();
		result.add(new DoubleObject<String,Object>("slide", getSliderPrecentqage()));
		return result;
	}
	@Override
	public boolean isFullBlown(){
		return true;
	}
	@Override
	public void onPressed(EntityPlayer player){
		if(moveMode||player.isSneaking()||host==null)return;
		try{
			sliderPos=MathUtil.snap((float)((position.y-host.point.pointedPos.y-size.y/8)/(size.y)),0,0.75F);
		}catch(Exception e){
			e.printStackTrace();
		}
		sendCommand();
	}
	@Override
	public void readData(Iterator<Integer> integers, Iterator<Boolean> booleans, Iterator<Byte> bytes___, Iterator<Long> longs___,Iterator<Double> doubles_, Iterator<Float> floats__, Iterator<String> strings_, Iterator<Short> shorts__){
		super.readData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		sliderPos=floats__.next();
		renderSliderPos.wantedPoint=renderSliderPos.point=renderSliderPos.prevPoint=sliderPos;
	}
	@Override
	public void render(ColorF color){
		checkHighlight();
		ColorF col=UtilM.calculateRenderColor(prevColor, this.color);
		
		if(scroll==null){
			main=new CubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
			scroll=new CubeModel(0, 0, -UtilM.p/2, -size.x, -size.y/4, UtilM.p/2);
		}
		col.bind();
		
		GL11U.texture(false);
		main.draw();
		OpenGLM.translate(0, -renderSliderPos.getPoint()*size.y, 0);
		col.blackNWhite().bind();
		scroll.draw();
		GL11U.texture(true);
		//UtilM.println(col,prevColor);
	}
	@Override
	public void sendCommand(){
		standard.sendStandardCommand();
	}
	@Override
	public void update(){
//		Util.printInln(getActivationTarget(),Util.isRemote());
		renderSliderPos.wantedPoint=sliderPos;
		renderSliderPos.update();
		if(UtilM.getWorldTime(host)%40==0){
			main=new CubeModel(0, 0, -UtilM.p/2, -size.x, -size.y, UtilM.p/2);
			scroll=new CubeModel(0, 0, -UtilM.p/2, -size.x, -size.y/4, UtilM.p/2);
		}
		size=new Vector2f(originalSize.x*scale, originalSize.y*scale);
		prevColor=color;
		
		color=UtilM.slowlyEqalizeColor(color, setColor, 0.1F);
	}
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_,List<Float> floats__, List<String> strings_, List<Short> shorts__){
		super.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
		floats__.add(sliderPos);
	}
}
