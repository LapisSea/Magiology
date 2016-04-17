package com.magiology.client.gui.guiutil.gui;

import static com.magiology.util.utilclasses.UtilM.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.guiutil.gui.GuiSlider.SliderParent;
import com.magiology.client.render.font.FontRendererMBase;
import com.magiology.client.render.font.FontRendererMClipped;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.vectors.Vec2i;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class GuiTextEditor extends Gui implements Updateable,SliderParent{
	
	private static class UndoSave{
		public String content;
		public Vec2i cursor;
		public UndoSave(String content, Vec2i cursor){
			this.content=content;
			this.cursor=cursor;
		}
		
	}
	protected static final Pattern blankSpace=Pattern.compile("^\\s*$"),notWhitespace=Pattern.compile("[^\\s]|\\s$"),word=Pattern.compile("\\b");
	private static final int CLICK_TIME=200;
	
	private static String newline=System.getProperty("line.separator");
	public boolean active=false,visible=true,insertMode=false,viewOnly=false;
	private List<Integer> cachedWidth=new ArrayList<Integer>();
	private Vec2i cursorPosition=Vec2i.zero(),selectionStart=Vec2i.zero();
	
	private long lastClickTime=Long.MAX_VALUE;
	
	public Vec2i pos, size,mouse=Vec2i.zero(),lastMouse=Vec2i.zero(),mouseClick=Vec2i.zero();
	
	public List<StringBuilder> textBuffer=new ArrayList<StringBuilder>();
	protected Vector2f textOffset=new Vector2f(),prevTextOffset=new Vector2f();
	private int undoPos,prevXPos,clickCount=0;
	
	private List<UndoSave> undoSteps=new ArrayList<UndoSave>();
	public int width,white=Color.WHITE.hashCode(),black=new Color(16,16,32,255).hashCode(),maxWidth;
	
	
	protected int xMouseArrow, yMouseArrow;
	

	protected GuiSlider xSlider,ySlider;
	
	//TODO: user control------------------------------------------------------
	
	public GuiTextEditor(Vec2i pos,Vec2i size){
		textBuffer.add(new StringBuilder());
		cachedWidth.add(0);
		this.pos=pos;
		this.size=size;
		xSlider=new GuiSlider(this, "0 10px", 10, true, true, true);
		ySlider=new GuiSlider(this, "0 10px", 10, true, true, false);
		active=true;
	}
	
	protected void addLine(StringBuilder sb){
		textBuffer.add(sb);
		cachedWidth.add(0);
		refreshLine(textBuffer.size()-1);
		fixWidth();
	}
	
	protected void addLine(StringBuilder sb, int pos){
		textBuffer.add(pos, sb);
		cachedWidth.add(pos, 0);
		refreshLine(pos);
		fixWidth();
	}
	
	//end-------------------------------------------------------------------------------
	
	private void backspace(){
		if(isSelected()){
			deleteSelection();
			return;
		}
		if(getCursorPosition().equals(Vec2i.zero()))
			return;
		if(getCursorPosition().x==0){
			StringBuilder line=getCurrentLine();
			removeLine(getCursorPosition().y);
			Vec2i newCursorPosition=new Vec2i(getLength(getCursorPosition().y-1), getCursorPosition().y-1);
			getLine(getCursorPosition().y-1).append(line);
			refreshLine(getCursorPosition().y-1);
			setCursorPositionInternal(newCursorPosition);
			fixWidth();
		}else{
			boolean isOnEnd=getCurrentLine().length()==getCursorPosition().x;
			getCurrentLine().deleteCharAt(getCursorPosition().x-1);
			refreshLine(getCursorPosition().y);
			if(!isOnEnd)setCursorPositionInternal(new Vec2i(getCursorPosition().x-1, getCursorPosition().y));
			fixWidth();
		}
	}

	protected void check(Vec2i pos){
		if(pos.x<0||pos.y<0||pos.y>=textBuffer.size())throw new IndexOutOfBoundsException();
		if(pos.x>getLength(pos.y))throw new IndexOutOfBoundsException();
	}

	public void clearSelection(){
		selectionStart=selection().obj1;
	}
	//end-----------------------------------------------------------------------
	
	//TODO: text editing------------------------------------------------------------
	
	private void delete(){
		if(isSelected()){
			deleteSelection();
			return;
		}
		if(getCursorPosition().equals(getLastPosition()))
			return;
		if(getCursorPosition().x>=getLength(getCursorPosition().y)){
			StringBuilder line=getLine(getCursorPosition().y+1);
			removeLine(getCursorPosition().y+1);
			getLine(getCursorPosition().y).append(line);
			refreshLine(getCursorPosition().y);
		}else{
			if(GuiScreen.isCtrlKeyDown()){
				selectWord(getCursorPosition());
				delete();
			}else{
				getCurrentLine().deleteCharAt(getCursorPosition().x);
			}
			refreshLine(getCursorPosition().y);
			fixWidth();
		}
	}
	
	public void deleteSelection(){
		if(!isSelected())return;
		
		DoubleObject<Vec2i, Vec2i> selection=selection();
		Vec2i first=selection.obj1;
		Vec2i last=selection.obj2;
		
		StringBuilder firstLine=textBuffer.get(first.y);
		if(first.y==last.y){
			firstLine.delete(first.x, last.x);
		}else{
			firstLine.delete(first.x, getLength(first.y));
		}
		
		refreshLine(first.y);
		setCursorPositionInternal(first);
		
		if(first.y==last.y){
			fixWidth();
			return;
		}
		
		int numLines=last.y-first.y;
		StringBuilder lastLine=getLine(first.y+numLines);
		if(last.x<lastLine.length()-1){
			firstLine.append(lastLine.substring(last.x, lastLine.length()));
			refreshLine(first.y);
		}

		for(int i=0;i<numLines;i++){
			removeLine(first.y+1);
		}
	}

	private void doTab(DoubleObject<Vec2i, Vec2i> selection){
		boolean flip=getCursorPosition()==selection.obj1;
		selectionStart=new Vec2i(0, selection.obj1.y);
		setCursorPosition(new Vec2i(getLength(selection.obj2.y), selection.obj2.y));

		if(flip){
			Vec2i temp=selectionStart;
			selectionStart=getCursorPosition();
			setCursorPosition(temp);
		}
	}

	private void down(){
		FontRendererMBase fr=Font.FRB();
		if(getCursorPosition().y>=textBuffer.size()-1)return;
		
		int x=fr.getStringWidth(getLine(getCursorPosition().y+1).substring(0, prevXPos>getLine(getCursorPosition().y+1).length()?getLine(getCursorPosition().y+1).length():prevXPos));
		try{
			Vec2i po=findCharAtPos(pos.x+x, pos.y+(getCursorPosition().y+1)*fr.FONT_HEIGHT);
			x=po==null?getLength(getCursorPosition().y+1):po.x;
			setCursorPositionInternal(new Vec2i(x, getCursorPosition().y+1));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Vec2i findCharAtPos(int mx, int my){
		FontRendererMBase fr=Font.FRB();
		try{
			float offsetX=getSlideableOffset().x,offsetY=getSlideableOffset().y;
			mx+=offsetX;
			my+=offsetY;
			int y=my-pos.y;
			if(y<offsetY||y>=size.y+offsetY)return null;
			y/=fr.FONT_HEIGHT;
			int x=mx-pos.x;
			if(x<offsetX||x>size.x*2+offsetX)return null;
			y=Math.min(y,textBuffer.size()-1);
			x=Math.min(x,width);
			String s=fr.trimStringToWidth(textBuffer.get(y).toString(), x);
			char nextChar='';
			if(textBuffer.get(y).length()>s.length())nextChar=textBuffer.get(y).toString().charAt(s.length());
			x=s.length();
			if(nextChar!=''&&fr.getCharWidth(nextChar)<=((mx-pos.x)-fr.getStringWidth(s))*2)x++;
			return new Vec2i(x, y);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	protected void fixCursor(){
		cursorPosition.y=MathUtil.snap(cursorPosition.y, 0, Math.max(0, textBuffer.size()-1));
		cursorPosition.x=MathUtil.snap(cursorPosition.x, 0, Math.max(0, getLength(cursorPosition.y)));
	}

	private void fixSlideableOffset(){
		textOffset.x=MathUtil.snap(textOffset.x, 0, Math.max(0, maxWidth-size.x+18));
		textOffset.y=MathUtil.snap(textOffset.y, 0, Math.max(0, (textBuffer.size()+2)*Font.FRB().FONT_HEIGHT-size.y));
	}

	protected void fixWidth(){
		width=0;
		maxWidth=0;
		for(int w:cachedWidth){
			if(w>width)width=w;
			maxWidth=Math.max(maxWidth, w);
		}
	}

	protected StringBuilder getCurrentLine(){
		return getLine(getCursorPosition().y);
	}
	public Vec2i getCursorPosition(){
		fixCursor();
		return cursorPosition;
	}
	public Vec2i getLastPosition(){
		return new Vec2i(textBuffer.get(textBuffer.size()-1).length(), textBuffer.size()-1);
	}
	public int getLength(int line){
		return textBuffer.get(line).length();
	}
	
	protected StringBuilder getLine(int line){
		return textBuffer.get(line);
	}
	
	@Override
	public Vec2i getMousePos(){
		return mouse.add(getPos());
	}
	
	@Override
	public Vec2i getPos(){
		return pos;
	}
	
	public String getSelectedText(){
		if(!isSelected())return "";
		
		DoubleObject<Vec2i, Vec2i> select=selection();
		Vec2i first=select.obj1;
		Vec2i last=select.obj2;

		List<StringBuilder> selectedLines=textBuffer.subList(first.y, last.y+1);
		if(selectedLines.size()==1){
			return selectedLines.get(0).substring(first.x, last.x);
		}else{
			StringBuilder sb=new StringBuilder();
			sb.append(selectedLines.get(0).substring(first.x)).append("\r\n");
			for(int i=1;i<selectedLines.size()-1;i++){
				sb.append(selectedLines.get(i)).append("\r\n");
			}
			sb.append(selectedLines.get(selectedLines.size()-1).substring(0, last.x));
			return sb.toString();
		}
	}
	
	@Override
	public Vec2i getSize(){
		return size;
	}

	@Override
	public Vector2f getSlideableOffset(){
		fixSlideableOffset();
		return textOffset;
	}

	@Override
	public Vec2i getSlideableSize(){
		return new Vec2i(maxWidth+18, (textBuffer.size()+2)*Font.FRB().FONT_HEIGHT);
	}
	
	//TODO: selection-----------------------------------------------------------------
	
	protected StringBuilder getStartWhiteSpace(String s){
		StringBuilder spaces=new StringBuilder();
		int spaceCount=0;
		for(int j=0;j<s.length();j++){
			char ch=s.charAt(j);
			if(ch==' '){
				spaceCount++;
				if(spaceCount>=4){
					spaces.append('\t');
					spaceCount-=4;
				}
			}
			else if(ch=='\t')spaces.append(ch);
			else continue;
		}
		return spaces;
	}

	public String getText(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<textBuffer.size();i++){
			sb.append(textBuffer.get(i));
			if(i!=textBuffer.size()-1)sb.append(newline);
		}
		return sb.toString();
	}
	
	public void insertText(String text){
		insertText(text, getCursorPosition(), true);
	}
	
	public void insertText(String text, Vec2i pos){
		insertText(text, getCursorPosition(), false);
	}
	protected void insertText(String text, Vec2i pos, boolean advanceCursor){
		try{
			clearSelection();
			if(text==null)throw new NullPointerException();
			check(pos);
			
			String[] toInsert=stringNewlineSplit(text);
			if(text.equals("\n"))toInsert=new String[]{"\n"};
			StringBuilder insertLine=getLine(pos.y);
			String endOfLine=insertLine.substring(pos.x, insertLine.length());
			insertLine.delete(pos.x, insertLine.length()); 
			insertLine.append(toInsert[0]);
	 		
			if(toInsert.length>1){
				if(toInsert.length>2)for(int i=1;i<toInsert.length-1;i++){
					addLine(new StringBuilder(toInsert[i]),pos.y+i);
				}
				addLine(new StringBuilder(toInsert[toInsert.length-1]).append(endOfLine),pos.y+toInsert.length-1);
				
				if(advanceCursor){
					int newCursorY=getCursorPosition().y+toInsert.length-1;
					setCursorPositionInternal(new Vec2i(Math.min(toInsert[toInsert.length-1].length(), getLength(newCursorY)), newCursorY));
				}
			}else{
				if(advanceCursor)setCursorPositionInternal(new Vec2i(Math.min(getCursorPosition().x+toInsert[0].length(), getLength(getCursorPosition().y)), getCursorPosition().y));
				insertLine.append(endOfLine);
			}
//			int maxSpaces=Integer.MAX_VALUE;
			//next line magic
			for(int i=0;i<textBuffer.size();i++){
				final String s=textBuffer.get(i).toString();
				if(s.equals("\n")){
					setLine(new StringBuilder(""),i);
					addLine(new StringBuilder(""),i+1);
				}
				else if(s.contains("\n")){
					
					
					
					String[] lines=s.split("\n");
					setLine(new StringBuilder(lines[0]),i);
					for(int j=1;j<lines.length;j++)addLine(new StringBuilder(lines[j]),i+1);
					if(s.endsWith("{\n")){
						int braceCount=1;
						
						for(int k=getCursorPosition().y+1;k<textBuffer.size();k++){
							for(char c:getLine(k).toString().toCharArray()){
								if(c=='{'){
									braceCount++;
								}else if(c=='}'){
									braceCount--;
								}
								if(braceCount==0)break;
							}
							if(braceCount==0)break;
						}
						StringBuilder newLine=getStartWhiteSpace(s).append('\t');
						if(braceCount>0){
							addLine(new StringBuilder("}"),i+1);
							addLine(newLine,i+1);
						}else addLine(newLine,i+1);
					}
					else if(s.endsWith("\n"))addLine(new StringBuilder(getStartWhiteSpace(s)),i+1);
					getCursorPosition().y++;
					if(getCurrentLine().toString().trim().length()==0)getCursorPosition().x=getCurrentLine().length();
					else getCursorPosition().x=0;
				}
			}
			refreshLine(pos.y);
			fixWidth();
			for(int i:cachedWidth){
				maxWidth=Math.max(maxWidth, i);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean isSelected(){
		return !selectionStart.equals(getCursorPosition());
	}

	public boolean keyTyped(int code, char ch){
		if(code==Keyboard.KEY_ESCAPE?false:viewOnly)return false;
		String prevText=getText();
		try{
			if(!visible||!active)return false;
			boolean con=false;
			switch(code){
				case Keyboard.KEY_INSERT:insertMode=!insertMode;break;
				case Keyboard.KEY_DELETE:delete();break;
				case Keyboard.KEY_UP:up();break;
				case Keyboard.KEY_DOWN:down();break;
				case Keyboard.KEY_LEFT:left();break;
				case Keyboard.KEY_RIGHT:right();break;
				case Keyboard.KEY_END:{
					if(!GuiScreen.isCtrlKeyDown())getCursorPosition().x=getCurrentLine().length();
					else{
						getCursorPosition().y=Math.max(0, textBuffer.size()-1);
						if(getCursorPosition().x>getCurrentLine().length())getCursorPosition().x=getCurrentLine().length();
					}
				}break;
				case Keyboard.KEY_Z:{
					if(GuiScreen.isCtrlKeyDown()&&!undoSteps.isEmpty()){
						selectAll();
						replace(undoSteps.get(undoPos).content);
						setCursorPosition(selectionStart=undoSteps.get(undoPos).cursor);
						undoPos++;
						if(undoPos>100)undoPos=100;
					}
					if(!GuiScreen.isCtrlKeyDown())con=true;
				}break;
				case Keyboard.KEY_Y:{
					if(GuiScreen.isCtrlKeyDown()&&!undoSteps.isEmpty()&&undoPos>0){
						selectAll();
						replace(undoSteps.get(undoPos).content);
						setCursorPosition(selectionStart=undoSteps.get(undoPos).cursor);
						undoPos--;
						if(undoPos>100)undoPos=100;
					}
					if(!GuiScreen.isCtrlKeyDown())con=true;
				}break;
				default:con=true;
			}
			if(con)switch(ch){
				case 1:selectAll();break;//^A
				case 3:GuiScreen.setClipboardString(getSelectedText());break;//^C
				case 22:replace(GuiScreen.getClipboardString());break;//^V
				case 24:{
					GuiScreen.setClipboardString(getSelectedText());
					deleteSelection();
				}break;//^X
				case 8:backspace();break;//backspace
				case 27:active=false;break;//ESC
				case 13:replace("\n");break;//CR
				case '\t':{
					tab();
					String line=getCurrentLine().toString();
					if(line.startsWith("\t")){
						if(line.substring(0, getCursorPosition().x).replaceAll("\t", "").replaceAll(" ", "").isEmpty()){
							char c;
							while(getCursorPosition().x<line.length()&&((c=line.charAt(getCursorPosition().x))=='\t'||c==' '))right();
						}
					}
					
				}break;
				case ' ':{
					String line=getCurrentLine().toString();
					if(line.startsWith("\t")){
						if(line.substring(0, getCursorPosition().x).replaceAll("\t", "").replaceAll(" ", "").isEmpty()){
							char c;
							while((c=line.charAt(getCursorPosition().x))=='\t'||c==' ')right();
						}
					}
				}
				default:
					if(!Character.isISOControl(ch))replace(String.valueOf(ch));
					break;
			}
			if(!GuiScreen.isCtrlKeyDown()||code!=Keyboard.KEY_Z||code!=Keyboard.KEY_Y){
				for(int i=0;i<undoPos;i++){
					undoSteps.remove(0);
				}
				undoPos=0;
			}
			if(!(code==Keyboard.KEY_Z&&GuiScreen.isCtrlKeyDown())){
				if(!getText().equals(prevText)){
					undoSteps.add(0,new UndoSave(prevText, getCursorPosition().add(0, 0)));
					if(undoSteps.size()>200)undoSteps.remove(200);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		maxWidth=0;
		for(int i:cachedWidth){
			maxWidth=Math.max(maxWidth, i);
		}
		return true;
	}
	
	//TODO: util----------------------------------------------------------------
	
	private void left(){
		if(getCursorPosition().equals(Vec2i.zero()))return;
		prevXPos=getCursorPosition().x;
		if(getCursorPosition().x==0){
			setCursorPositionInternal(new Vec2i(getLength(getCursorPosition().y-1), getCursorPosition().y-1));
		}else setCursorPositionInternal(new Vec2i(getCursorPosition().x-1, getCursorPosition().y));
	}
	
	public void mouseClicked(int x, int y, int button){
		xMouseArrow=x;
		yMouseArrow=y;
		try{
			if(!visible||button!=0)return;
			lastMouse=mouse;
			mouse=new Vec2i(x-pos.x, y-pos.y);
			mouseClick=new Vec2i(x-pos.x, y-pos.y);
			
			
			if(mouseClick.x<0||mouseClick.y<0||mouseClick.x>size.x||mouseClick.y>size.y){
				active=false;
				return;
			}
			
			if(!xSlider.mouseClicked()&&!ySlider.mouseClicked()){
				Vec2i intersection=findCharAtPos(x, y);
				if(intersection==null){
					active=false;
					return;
				}
				prevXPos=intersection.x;
				long time=System.currentTimeMillis();
				if(time-lastClickTime>CLICK_TIME){
					clickCount=0;
				}
				lastClickTime=time;
				

				if(clickCount==0){
					setCursorPositionInternal(intersection);
				}else if(clickCount==1){
					selectWord(intersection);
				}else if(clickCount==2){
					selectLine(intersection);
					clickCount=0;
				}
			}
			clickCount++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void mouseClickMove(int x, int y){
		try{
			if(!visible||!active)return;
			lastMouse=mouse;
			mouse=new Vec2i(x-pos.x, y-pos.y);
			if(!xSlider.mouseDragged()&&!ySlider.mouseDragged()){
				Vec2i intersection=findCharAtPos(x, y);
				if(intersection!=null){
					setCursorPosition(intersection);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void rednerText(FontRendererMClipped fr){
		for(int i=0;i<textBuffer.size();i++){
			fr.min=new Vector2f(pos.x-2,pos.y-2);
			fr.max=new Vector2f(pos.x+size.x+2,pos.y+size.y+2);
			fr.drawString(textBuffer.get(i).toString(), pos.x, pos.y+i*fr.FONT_HEIGHT, white);
		}
	}
	protected void refreshLine(int line){
		cachedWidth.set(line, Font.FR().getStringWidth(textBuffer.get(line).toString()));
	}
	protected void removeLine(int line){
		textBuffer.remove(line);
		cachedWidth.remove(line);
		fixWidth();
	}
	//TODO: rendering-------------------------------------------------------------------------
	public void render(int x, int y){
		xMouseArrow=x;
		yMouseArrow=y;
		if(!visible)return;
		if(!Mouse.isButtonDown(0)){
			lastMouse=mouse;
			mouse=new Vec2i(x-pos.x, y-pos.y);
		}
		FontRendererMClipped fr=FontRendererMClipped.get();
		
		int guiScale=getGuiScaleRaw();
		GL11U.texture(false);
		OpenGLM.lineWidth(guiScale);
		
		VertexRenderer buff=TessUtil.getVB();
		buff.addVertex(pos.x-3, pos.y+size.y+3, 0);
		buff.addVertex(pos.x+size.x+3, pos.y+size.y+3, 0);
		buff.addVertex(pos.x+size.x+3, pos.y-3, 0);
		buff.addVertex(pos.x-3, pos.y-3, 0);
		
		GL11U.glColor(black);
		buff.setClearing(false);
		buff.draw();
		buff.setClearing(true);
		
		GL11U.glColor(new Color(160, 160, 160, 255).hashCode());
		buff.setDrawAsWire(true);
		buff.draw();
		buff.setDrawAsWire(false);
		
		OpenGLM.lineWidth(1);
		GL11U.texture(true);
		
		OpenGLM.pushMatrix();
		Vector2f offset=PartialTicksUtil.calculatePos(prevTextOffset, getSlideableOffset()).negate(null);
		OpenGLM.translate(offset.x, offset.y, 0);
		if(active&&isSelected()){
			renderSelection(0xFFDFB578);
		}
		
		OpenGLM.translate(-offset.x, -offset.y, 0);
		buff.pushMatrix();
		buff.translate(offset.x, offset.y, 0);
		rednerText(fr);
		buff.popMatrix();
		OpenGLM.translate(offset.x, offset.y, 0);
		
		if(active&&isSelected()){
			GL11U.blendFunc(4);
			renderSelection(0xFFFFFFFF);
			GL11U.blendFunc(1);
		}
		
		if(active){
			Rectangle box=new Rectangle(pos.x-2-(int)offset.x,pos.y-2-(int)offset.y,size.x+4, size.y+4);
			if(getWorldTime(getTheWorld())/6%2==0){
				if(getCursorPosition().x>getCurrentLine().length())getCursorPosition().x=getCurrentLine().length();
				int cursorX=pos.x+fr.getStringWidth(getCurrentLine().substring(0, getCursorPosition().x));
				int cursorY=pos.y+getCursorPosition().y*fr.FONT_HEIGHT;
				boolean vertical=getCursorPosition().x-getCurrentLine().length()<0;
				if(box.contains(cursorX, cursorY)&&(vertical?true:box.contains(cursorX+7, cursorY))){
					if(vertical)drawVerticalLine(cursorX, cursorY-2, cursorY+10, white);
					else drawRect(cursorX, cursorY+8, cursorX+5, cursorY+7, white);
				}
			}
			int 
				cpos=pos.y+getCursorPosition().y*9,
				minY=(int)MathUtil.snap(cpos-2,box.getMinY(),box.getMaxY()),
				maxY=(int)MathUtil.snap(cpos+9,box.getMinY(),box.getMaxY());
			
			if(minY!=maxY)drawRect(pos.x-2, minY, size.x+12, maxY, new ColorF(1, 1, 1, 0.1).toCode());
		}
		OpenGLM.popMatrix();
//		if(maxHeight>size.y){
//			GL11U.texture(false);
//			GL11U.setUpOpaqueRendering(2);
//			int width=8,height=(int)Math.max(((size.y-width)*((float)size.y/(float)maxHeight)),10);
//			
//			GL11U.glColor(new Color(160, 160, 160, 55).hashCode());
//			drawModalRectWithCustomSizedTexture((pos.x+size.x-width)-1, pos.y-1, 0, 0, width+2, size.y-width+2, 0, 0);
//			
//			GL11U.glColor(new Color(160, 160, 160, (int)(75*sliderYCol.getPoint())).hashCode());
//			drawModalRectWithCustomSizedTexture(pos.x+(size.x-width), (int)(pos.y+(size.y-height)*sliderY), 0, 0, width, height-width, 0, 0);
//			
//			GL11U.texture(true);
//			GL11U.endOpaqueRendering();
//		}
//		if(maxWidth>size.x){
//			GL11U.texture(false);
//			GL11U.setUpOpaqueRendering(2);
//			int height=8,width=Math.max((int)((size.x-height)*((float)size.x/(float)maxWidth)),10);
//			
//			GL11U.glColor(new Color(160, 160, 160, 55).hashCode());
//			drawModalRectWithCustomSizedTexture((pos.x)-1, pos.y+size.y-height-1, 0, 0, size.x-height+2, height+2, 0, 0);
//			
//			GL11U.glColor(new Color(160, 160, 160, (int)(75*sliderXCol.getPoint())).hashCode());
//			drawModalRectWithCustomSizedTexture((int)(pos.x+(size.x-width-height)*sliderX), pos.y+size.y-height, 0, 0, width, height, 0, 0);
//			
//			GL11U.texture(true);
//			GL11U.endOpaqueRendering();
//		}
		ySlider.render();
		xSlider.render();
	}
	private void renderSelection(int color){
		FontRendererMBase fr=Font.FRB();
		DoubleObject<Vec2i, Vec2i> selection=selection();
		Vec2i first=selection.obj1;
		Vec2i last=selection.obj2;
		
		Vector2f offset=PartialTicksUtil.calculatePos(prevTextOffset, getSlideableOffset()).negate(null);
		
		if(first.y==last.y){
			int x1=fr.getStringWidth(getLine(first.y).substring(0, first.x));
			int x2=fr.getStringWidth(getLine(first.y).substring(0, last.x));
			int min=(int)Math.max(pos.x+x1, pos.x-offset.x-2),max=(int)Math.min(pos.x+x2, pos.x+size.x-offset.x+2);
			if(min<max)drawRect(min, (int)Math.max(pos.y+first.y*fr.FONT_HEIGHT,pos.y-offset.y-2), max, pos.y+(first.y+1)*fr.FONT_HEIGHT,color);
		}else{
			int x1=fr.getStringWidth(getLine(first.y).substring(0, first.x));
			int x2=cachedWidth.get(first.y);
			{
				int 
					minX=(int)Math.max(pos.x+x1, pos.x-offset.x-2),maxX=(int)Math.min(pos.x+x2, pos.x+size.x-offset.x+2),
					minY=(int)Math.max(pos.y+first.y*fr.FONT_HEIGHT, pos.y-offset.y-2),maxY=(int)Math.min(pos.y+(first.y+1)*fr.FONT_HEIGHT, pos.y+size.y-offset.y+3);
				if(minX<maxX&&minY<maxY)drawRect(minX, minY, maxX, maxY, color);
			}
			for(int i=0;i<last.y-first.y-1;i++){
				x2=cachedWidth.get(first.y+i+1);
				int 
					minX=(int)Math.max(pos.x, pos.x-offset.x-2),maxX=(int)Math.min(pos.x+x2, pos.x+size.x-offset.x+2),
					minY=(int)Math.max(pos.y+(first.y+i+1)*fr.FONT_HEIGHT, pos.y-offset.y-2),maxY=(int)Math.min(pos.y+(first.y+i+2)*fr.FONT_HEIGHT, pos.y+size.y-offset.y+3);
				if(minX<maxX&&minY<maxY)drawRect(minX, minY, maxX, maxY, color);
			}

			x2=fr.getStringWidth(getLine(last.y).substring(0, last.x));
			
			int 
				minX=(int)Math.max(pos.x, pos.x-offset.x-2),maxX=(int)Math.min(pos.x+x2, pos.x+size.x-offset.x+2),
				minY=(int)Math.max(pos.y+last.y*fr.FONT_HEIGHT, pos.y-offset.y-2),maxY=(int)Math.min(pos.y+(last.y+1)*fr.FONT_HEIGHT, pos.y+size.y-offset.y+3);
			if(minX<maxX&&minY<maxY)drawRect(minX, minY, maxX, maxY, color);
		}
	}
	//end---------------------------------------------------------------------------
	
	
	public void replace(String text){
		deleteSelection();
		insertText(text);
	}
	
	private void right(){
		if(getCursorPosition().equals(getLastPosition()))return;
		prevXPos=getCursorPosition().x;
		if(getCursorPosition().x>=getLength(getCursorPosition().y)){
			setCursorPositionInternal(new Vec2i(0, getCursorPosition().y+1));
		}else{
			setCursorPositionInternal(new Vec2i(getCursorPosition().x+1, getCursorPosition().y));
		}
	}
	
	public void selectAll(){
		selectionStart=Vec2i.zero();
		setCursorPosition(getLastPosition());
	}

	protected DoubleObject<Vec2i, Vec2i> selection(){
		Vec2i first, last;
		if(selectionStart.y<getCursorPosition().y||(selectionStart.y==getCursorPosition().y&&selectionStart.x<getCursorPosition().x)){
			first=selectionStart;
			last=getCursorPosition();
		}else{
			first=getCursorPosition();
			last=selectionStart;
		}
		return new DoubleObject<Vec2i, Vec2i>(first, last);
	}
	
	private void selectLine(Vec2i intersection){
		selectionStart=new Vec2i(0, intersection.y);
		if(intersection.y+1>=textBuffer.size()){
			setCursorPosition(new Vec2i(getLength(intersection.y), intersection.y));
		}else{
			setCursorPosition(new Vec2i(0, intersection.y+1));
		}
	}
	private void selectWord(Vec2i intersection){
		StringBuilder line=getLine(intersection.y);

		Matcher forward=word.matcher(line);
		Matcher backward=word.matcher(new StringBuilder(line).reverse());
		
		int next=forward.find(intersection.x) ? forward.start():getLength(intersection.y);
		int prev=backward.find(line.length()-intersection.x) ? line.length()-backward.start():0;
		
		selectionStart=new Vec2i(prev, intersection.y);
		setCursorPosition(new Vec2i(next, intersection.y));
	}
	public void set(String text){
		insertText(text, getLastPosition());
	}

	public void setCursorPos(Vec2i position){
		if(position.x<0||position.y<0)
			throw new IndexOutOfBoundsException();
		if(position.y>textBuffer.size()){
			position=getLastPosition();
		}else if(position.x>getLength(position.y)){
			position=new Vec2i(getLength(position.y), position.y);
		}
		setCursorPositionInternal(position);
	}
	
	public void setCursorPosition(Vec2i cursorPosition){
		this.cursorPosition=cursorPosition;
		fixCursor();
	}

	protected void setCursorPositionInternal(Vec2i position){
		this.setCursorPosition(position);
		this.selectionStart=position;
	}

	protected void setLine(StringBuilder sb, int pos){
		textBuffer.set(pos, sb);
		cachedWidth.set(pos, 0);
		refreshLine(pos);
		fixWidth();
	}

	@Override
	public void setSlideableOffset(Vector2f offset){
		textOffset=offset;
		fixSlideableOffset();
	}

	public GuiTextEditor setText(String text){
		setCursorPositionInternal(Vec2i.zero());
		textBuffer.clear();
		cachedWidth.clear();
		addLine(new StringBuilder());
		set(text);
		setCursorPositionInternal(getLastPosition());
		return this;
	}
	//end-----------------------------------------------------------------------------

	private void tab(){
		if(getCursorPosition().y==selectionStart.y){
			replace("\t");
			
			if(blankSpace.matcher(getCurrentLine().toString().substring(0, getCursorPosition().x)).matches()){
				Matcher matcher=notWhitespace.matcher(getCurrentLine());
				if(matcher.find()){
					setCursorPositionInternal(new Vec2i(matcher.start(), getCursorPosition().y));
				}
			}
		}else{
			DoubleObject<Vec2i, Vec2i> selection=selection();
			for(int i=selection.obj1.y;i <= selection().obj2.y;i++){
				getLine(i).insert(0, "	");
				refreshLine(i);
			}
			doTab(selection);
		}
		fixWidth();
	}

	private void up(){
		FontRendererMBase fr=Font.FRB();
		if(getCursorPosition().y==0)return;
		
		int x=fr.getStringWidth(getLine(getCursorPosition().y-1).substring(0, prevXPos>getLine(getCursorPosition().y-1).length()?getLine(getCursorPosition().y-1).length():prevXPos));
		try{
			Vec2i po=findCharAtPos(pos.x+x, pos.y+(getCursorPosition().y-1)*fr.FONT_HEIGHT);
			x=po==null?getLength(getCursorPosition().y-1):po.x;
			setCursorPositionInternal(new Vec2i(x, getCursorPosition().y-1));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void update(){
		if(!visible||!active)return;
		prevTextOffset=new Vector2f((getSlideableOffset().x*3+prevTextOffset.x)/4, (getSlideableOffset().y*3+prevTextOffset.y)/4);
		
		int rool=Mouse.getDWheel()/120;
		if(rool!=0){
			int side=rool>0?1:-1;
			if(GuiScreen.isCtrlKeyDown())side*=5;
			for(int i=0;i<rool*side;i++){
				
				if(GuiScreen.isShiftKeyDown())getSlideableOffset().x-=9*side;
				else getSlideableOffset().y-=9*side;
			}
		}
		
		xSlider.update();
		ySlider.update();
	}
}