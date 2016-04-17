package com.magiology.client.gui.guiutil.gui;

import java.awt.Rectangle;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec2i;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

public class GuiSlider{
	
	private static class MarginPixel implements ObjectProcessor<Vec2i>{
		private int margin;
		public MarginPixel(int margin){
			this.margin=margin;
		}
		@Override
		public Vec2i pocess(Vec2i object, Object... objects){
			return new Vec2i(margin, margin);
		}
	}
	private static class MarginPrecent implements ObjectProcessor<Vec2i>{
		private float margin;
		public MarginPrecent(float margin){
			this.margin=margin;
		}
		@Override
		public Vec2i pocess(Vec2i object, Object... objects){
			return object.mul(margin);
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// use this in the class that contains the slider/s
	public static interface SliderParent{
		public Vec2i getMousePos();
		public Vec2i getPos();
		public Vec2i getSize();
		public Vector2f getSlideableOffset();
		public Vec2i getSlideableSize();
		public void setSlideableOffset(Vector2f offset);
	}
	public static void addOutline(float x, float y, float width, float height){
		Renderer.LINES.addVertex(x,       y+height, 0);
		Renderer.LINES.addVertex(x+width, y+height, 0);
		
		Renderer.LINES.addVertex(x+width, y+height, 0);
		Renderer.LINES.addVertex(x+width, y,        0);
		
		Renderer.LINES.addVertex(x+width, y,        0);
		Renderer.LINES.addVertex(x,       y,        0);
		
		Renderer.LINES.addVertex(x,       y,        0);
		Renderer.LINES.addVertex(x,       y+height, 0);
	}
	public static void addQuad(float x, float y, float width, float height){
		Renderer.POS.addVertex(x,       y+height, 0);
		Renderer.POS.addVertex(x+width, y+height, 0);
		Renderer.POS.addVertex(x+width, y,        0);
		Renderer.POS.addVertex(x,       y,        0);
	}
	private PhysicsFloat
		colorQuadAlpha=new PhysicsFloat(0.2F, 0.15F, true),
		colorLineAlpha=new PhysicsFloat(0, 0.15F, true),
		overallAlpha=new PhysicsFloat(0, 0.15F, true);
	
	private ObjectProcessor<Vec2i> margin1,margin2;
	private SliderParent parent;
	
	private Vec2i pos=Vec2i.zero(),size=Vec2i.zero(),clickPos=Vec2i.zero();
	

	private Vector2f prevSlideableOffset;
	
	private float slidePrecentage;
	private boolean snapToParentSize=true,isHorisontal=false,side=true,highlighted,innerBoxHighlighted;
	
	private int width,parentMargin;
	
	public GuiSlider(SliderParent parent, String margin, int width, boolean side, boolean snapToParentSize, boolean isHorisontal){
		this.snapToParentSize=snapToParentSize;
		this.isHorisontal=isHorisontal;
		this.parent=parent;
		this.side=side;
		this.width=width;
		phraseMargin(margin);
		snapToParent();
		
	}
	private Rectangle boundingBox(){
		return new Rectangle(pos.x,pos.y,size.x,size.y);
	}
	private void checkHighlights(Vec2i mouse){
		
		highlighted=boundingBox().contains(mouse.x, mouse.y)||(Mouse.isButtonDown(0)&&boundingBox().contains(clickPos.x, clickPos.y));
		innerBoxHighlighted=highlighted&&innerBoundingBox().contains(mouse.x, mouse.y);
	}
	
	private float fromPrecent(float totalSize, float inSize, float precentage){
		return MathUtil.snap((totalSize-inSize)*precentage, 0, totalSize);
	}
	
	private float getInnerOffset(){
		return ((isHorisontal?size.x:size.y)-getInnerSize())*slidePrecentage;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// util 

	private int getInnerSize(){
		return (int)(getInnerSizePrecentage()*(isHorisontal?size.x:size.y));
	}
	private float getInnerSizePrecentage(){
		float slideableSize=isHorisontal?getSlideableSize().x:getSlideableSize().y;
		float sliderSize=isHorisontal?size.x:size.y;
		
		return MathUtil.snap(sliderSize/slideableSize, 0.1F, 1);
	}
	
	private Vec2i getMousePos(){
		return parent.getMousePos();
	}
	private Vec2i getParentPos(){
		return parent.getPos();
	}
	
	private Vec2i getParentSize(){
		return parent.getSize();
	}
	private Vector2f getSlideableOffset(){
		return parent.getSlideableOffset();
	}
	private Vec2i getSlideableSize(){
		return parent.getSlideableSize();
	}
	private boolean handleSlider(Vec2i mouse){
		mouse=mouse.copy();
		checkHighlights(mouse);
		if(!highlighted||getInnerSizePrecentage()==1||overallAlpha.getPoint()<0.01)return false;
		
		int innerSize=getInnerSize();
		if(isHorisontal){
			float 
				innerPos=mouse.x-innerSize/2,
				sliderSize=size.x-innerSize;
			
			slidePrecentage=MathUtil.snap(innerPos/sliderSize, 0, 1);
		}else{
			float 
				innerPos=mouse.y-innerSize/2,
				sliderSize=size.y-innerSize;
			
			slidePrecentage=MathUtil.snap(innerPos/sliderSize, 0, 1);
		}
		
		Vector2f slideableOffset=getSlideableOffset();
		if(isHorisontal)slideableOffset.x=fromPrecent(getSlideableSize().x, getParentSize().x, slidePrecentage);
		else slideableOffset.y=fromPrecent(getSlideableSize().y, getParentSize().y, slidePrecentage);
		setSlideableOffset(slideableOffset);
		
		return true;
	}
	private Rectangle innerBoundingBox(){
		int inner=getInnerSize();
		int offset=(int)(getInnerOffset());
		return new Rectangle(pos.x+offset,pos.y+offset,isHorisontal?inner:size.x,isHorisontal?size.y:inner);
	}
	public boolean mouseClicked(){
		clickPos=getMousePos();
		
		handleSlider(clickPos);
		return highlighted;
	}
	public boolean mouseDragged(){
		Vec2i mouse=getMousePos();
		if(boundingBox().contains(clickPos.x, clickPos.y))handleSlider(mouse);
		return highlighted;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// not to just some margin calculating fun ;)
	private void phraseMargin(String margin){
		String[] margins=margin.split(" ");
		if(margins.length==1){
			StringBuilder number=new StringBuilder(),extension=new StringBuilder();
			
			boolean num=true;
			for(char c:margins[0].toCharArray()){
				if(num&&Character.getType(c)!=Character.DECIMAL_DIGIT_NUMBER&&c!='.')num=false;
				if(num)number.append(c);
				else extension.append(c);
			}
			
			String extensi0n=extension.toString(),num8er=number.toString();
			ObjectProcessor<Vec2i> marg1n;
			if(num8er.equals("0")){
				marg1n=new MarginPixel(0);
			}else{
				if(extensi0n.toLowerCase().equals("px"))marg1n=new MarginPixel(Integer.parseInt(number.toString()));
				else if(extensi0n.equals("%"))marg1n=new MarginPrecent(Float.parseFloat(num8er));
				else throw new IllegalStateException("Invalid margin! Cause: Invalid nuber extension: "+extensi0n);
			}
			margin1=margin2=marg1n;
		}else if(margins.length==2){
			for(int i=0;i<2;i++){
				StringBuilder number=new StringBuilder(),extension=new StringBuilder();
				
				boolean num=true;
				for(char c:margins[i].toCharArray()){
					if(num&&Character.getType(c)!=Character.DECIMAL_DIGIT_NUMBER&&c!='.')num=false;
					if(num)number.append(c);
					else extension.append(c);
				}
				
				String extensi0n=extension.toString(),num8er=number.toString();;
				ObjectProcessor<Vec2i> marg1n;
				if(num8er.equals("0")){
					marg1n=new MarginPixel(0);
				}else{
					if(extensi0n.toLowerCase().equals("px"))marg1n=new MarginPixel(Integer.parseInt(number.toString()));
					else if(extensi0n.equals("%"))marg1n=new MarginPrecent(Float.parseFloat(number.toString()));
					else throw new IllegalStateException("Invalid margin! Cause: Invalid nuber extension: "+extensi0n);
				}
				if(i==0)margin1=marg1n;
				else margin2=marg1n;
			}
		}else throw new IllegalStateException("Only 2 margins are alloved!");
	}
	public void render(){
		Vec2i mouse=getMousePos();
		snapToParent();
		senseOutsideChanges();
		checkHighlights(mouse);
		float overallAlpha=this.overallAlpha.getPoint();
		if(overallAlpha<0.01)return;
		
		int 
			minX=getParentPos().x+( isHorisontal?parentMargin:0),
			minY=getParentPos().y+(!isHorisontal?parentMargin:0),
			w=getParentSize().x-( isHorisontal?parentMargin*2:0),
			h=getParentSize().y-(!isHorisontal?parentMargin*2:0),
			maxX=minX+w,
			maxY=minY+h;
		
		
		
		pos=Vec2i.zero();
		try{
			if(isHorisontal){
				pos.x=minX;
				if(side)pos.y=maxY-size.y;
				else    pos.y=minY;
			}else{
				pos.y=minY;
				if(side)pos.x=maxX-size.x;
				else    pos.x=minX;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		int innerSize=getInnerSize();
		
		float 
			offset=getInnerOffset(),
			offsetX=(isHorisontal?offset:0),
			offsetY=(isHorisontal?0:offset),
			innserSize=getInnerSize()/2F;
		
		OpenGLM.disableTexture2D();
		GL11U.setUpOpaqueRendering(2);
		OpenGLM.color(0, 0, 1, colorQuadAlpha.getPoint()*overallAlpha);
		Renderer.POS.beginQuads();
		addQuad(pos.x, pos.y, size.x, size.y);
		addQuad(pos.x+offsetX, pos.y+offsetY, (isHorisontal?innerSize:size.x), (isHorisontal?size.y:innerSize));
		Renderer.POS.draw();
		OpenGLM.color(0, 0, 1, colorLineAlpha.getPoint()*overallAlpha);
		
		
		Renderer.LINES.begin();
		addOutline(pos.x+offsetX, pos.y+offsetY, (isHorisontal?innerSize:size.x), (isHorisontal?size.y:innerSize));
		
		for(int i=0;i<10;i++){
			float forOff=Math.min(size.x, size.y)/4F*i;
			for(int j=0;j<2;j++){
				Renderer.LINES.addVertex(pos.x+offsetX+(isHorisontal?innserSize-forOff:size.x/4F), pos.y+offsetY+(isHorisontal?size.y/4F:innserSize-forOff),0);
				Renderer.LINES.addVertex(pos.x+offsetX+(isHorisontal?innserSize-forOff:size.x*(3/4F)), pos.y+offsetY+(isHorisontal? size.y*(3/4F):innserSize-forOff),0);
				forOff*=-1;
			}
		}
		
		Renderer.LINES.draw();
		OpenGLM.color(1,1,1,1);
		GL11U.endOpaqueRendering();
		OpenGLM.enableTexture2D();
	}
	private void senseOutsideChanges(){
		if(getSlideableOffset().equals(prevSlideableOffset)){
			updatePrevSlideableOffset();
			slidePrecentage=toPrecent(isHorisontal?getSlideableSize().x:getSlideableSize().y, isHorisontal?getParentSize().x:getParentSize().y, isHorisontal?getSlideableOffset().x:getSlideableOffset().y);
		}
	}
	private void setSlideableOffset(Vector2f offset){
		parent.setSlideableOffset(offset);
		updatePrevSlideableOffset();
	}
	
	
	
private void snapToParent(){
		int width=size.x,height=size.y;

		if(isHorisontal)height=this.width;
		else width=this.width;
		
		if(snapToParentSize){
			Vec2i 
				m1=margin1.pocess(getParentSize()),
				m2=margin2.pocess(getParentSize()),
				newSize=getParentSize().sub(m1).sub(m2);
			if(isHorisontal){
				width=newSize.x;
				parentMargin=m1.x;
			}else{
				height=newSize.y;
				parentMargin=m1.y;
			}
		}
		
		size=new Vec2i(width, height);
	}
	
	
	
	
	
	private float toPrecent(float totalSize, float inSize, float offset){
		return MathUtil.snap(offset/(totalSize-inSize), 0, 1);
	}

	public void update(){
		colorQuadAlpha.wantedPoint=highlighted?0.4F:0.2F;
		colorLineAlpha.wantedPoint=innerBoxHighlighted?1:colorQuadAlpha.wantedPoint;
		overallAlpha.wantedPoint=UtilM.booleanToInt(getInnerSizePrecentage()<1)*0.5F;
		colorQuadAlpha.update();
		colorLineAlpha.update();
		overallAlpha.update();
	}
	private void updatePrevSlideableOffset(){
		prevSlideableOffset=getSlideableOffset();
	}
}
