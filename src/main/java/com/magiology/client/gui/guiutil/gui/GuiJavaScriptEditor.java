package com.magiology.client.gui.guiutil.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.magiology.api.lang.program.ProgramCommon;
import com.magiology.api.lang.program.ProgramUsable;
import com.magiology.client.render.font.FontRendererMClipped;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.FontEffectUtil;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.LogUtil;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec2i;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;


public class GuiJavaScriptEditor extends GuiTextEditor{
	
	protected class ErrorMarker{
		public class QuickFix{
			public ObjectProcessor<ErrorMarker> action;
			private PhysicsFloat colorAnim=new PhysicsFloat(0, 0.25F,true);
			public String description;
			private boolean highlighted;
			
			public QuickFix(String description, ObjectProcessor<ErrorMarker> action){
				this.description=description;
				this.action=action;
			}
			public void click(){
				action.pocess(ErrorMarker.this);
				timeSelected=0;
			}
			public void render(){
				float anim=colorAnim.getPoint();
				Font.FR().drawString(description, 0, 0, new ColorF(anim*0.2,anim*0.5,anim*0.8,1).toCode());
			}
			public void update(){
				colorAnim.update();
				colorAnim.wantedPoint=highlighted&&ErrorMarker.this.selected?2:0F;
			}
		}
		public final int errorLenght,physicalLenght;
		public final Vec2i errorPos,physicalPos;
		public final String message;
		public List<QuickFix> quickFix=new ArrayList<>();
		private boolean selected;
		
		
		public int timeSelected;
		
		public ErrorMarker(Vec2i pos, int lenght, String message){
			Vec2i errorPos=new Vec2i(0, (pos.y+1)*9-1);
			int errorLenght=1;
			try{
				String line=getLine(pos.y).toString();
				
				errorPos.x=FontRendererMClipped.get().getStringWidth(line.substring(0, pos.x))-1;
				errorLenght=FontRendererMClipped.get().getStringWidth(line.substring(pos.x,pos.x+lenght))+2;
			}catch(Exception e){
				errorLenght=Math.max(Font.FRB().getStringWidth(getLine(pos.y).toString())+2,10);
			}
			
			this.errorPos=errorPos.add(GuiJavaScriptEditor.this.pos);
			this.errorLenght=errorLenght;
			this.message=message;
			this.physicalLenght=lenght;
			this.physicalPos=pos;
		}
		
		public void addQuickFix(String description, ObjectProcessor<ErrorMarker> action){
			quickFix.add(new QuickFix(description, action));
		}
		
		public void click(int x, int y){
			float down=y-(getSlideableOffset().y+errorPos.y)-13;
			
			for(int i=0;i<quickFix.size();i++){
				int distance=(int)(down-i*10);
				QuickFix fix=quickFix.get(i);
				if(fix.highlighted=(distance>0&&distance<8))fix.click();
			}
		}
		
		public void draw(int x, int y){
			Vector2f 
				pos1=new Vector2f(getSlideableOffset().x+errorPos.x,getSlideableOffset().y+errorPos.y),
				pos2=new Vector2f(getSlideableOffset().x+errorPos.x+errorLenght,getSlideableOffset().y+errorPos.y);
			
			if(pos1.y<pos.y-3||pos1.y>pos.y+3+size.y)return;
			
			float minX=pos.x-3,maxX=pos.x+3+size.x;
			pos1.x=MathUtil.snap(pos1.x, minX, maxX);
			pos2.x=MathUtil.snap(pos2.x, minX, maxX);
			if(pos1.x==pos2.x)return;
			
			OpenGLM.disableTexture2D();
			ColorF.RED.bind();
			Renderer.LINES.begin();
			Renderer.LINES.addVertex(pos1.x+RandUtil.CRF(1),pos1.y+RandUtil.CRF(1),0);
			Renderer.LINES.addVertex(pos2.x+RandUtil.CRF(1),pos2.y+RandUtil.CRF(1),0);
			Renderer.LINES.draw();
			ColorF.WHITE.bind();
			pos1.y+=1;
			
			int width=Font.FRB().getStringWidth(message), height=15+Math.max(1, quickFix.size())*10;
			List<String> fixText=new ArrayList<>();
			for(QuickFix fix:quickFix){
				width=Math.max(width, Font.FRB().getStringWidth(fix.description));
				fixText.add(fix.description);
			}
			float down=y-(getSlideableOffset().y+errorPos.y)-13+10;
			for(int i=0;i<quickFix.size();i++){
				int distance=(int)(down-i*10);
				quickFix.get(i).highlighted=distance>0&&distance<8;
			}
			width+=6;
			
			
			
			
			selected=isSelected(pos1, pos2, x, y)||(timeSelected>20?new Rectangle((int)pos1.x-9,(int)pos1.y-9, width, height).contains(x, y):false);
			if(timeSelected>20){
				VertexRenderer buff=TessUtil.getVB();
				
				buff.addVertex(pos1.x,       pos1.y, 1);
				buff.addVertex(pos1.x,       pos1.y+height, 1);
				buff.addVertex(pos1.x+width, pos1.y+height, 1);
				buff.addVertex(pos1.x+width, pos1.y, 1);
				
				buff.setClearing(false);
				GL11.glColor3b((byte)100, (byte)100, (byte)100);
				buff.draw();
				GL11.glColor3b((byte)255, (byte)255, (byte)225);
				buff.setClearing(true);
				buff.setDrawAsWire(true);
				buff.draw();
				buff.setDrawAsWire(false);
				
				OpenGLM.enableTexture2D();
				GL11.glTranslatef(0, 0, 2);
				Font.FR().drawString(message, (int)pos1.x+3, (int)pos1.y+3, Color.BLACK.hashCode());
				if(fixText.size()>0){
					OpenGLM.pushMatrix();
					OpenGLM.translate(pos1.x+3, pos1.y+3, 0);
					quickFix.forEach(fix->{
						OpenGLM.translate(0, 10, 0);
						fix.render();
					});
					OpenGLM.popMatrix();
				}else{
					Font.FR().drawString("No suggested quick fixes. :(", (int)pos1.x+3, (int)pos1.y+13, Color.BLACK.hashCode());
				}
				
				GL11.glTranslatef(0, 0, -2);
			}else OpenGLM.enableTexture2D();
		}
		@Override
		public boolean equals(Object obj){
			if(!(obj instanceof ErrorMarker))return false;
			ErrorMarker obj0=(ErrorMarker)obj;
			return errorPos.equals(obj0.errorPos)&&errorLenght==obj0.errorLenght&&message.equals(obj0.message);
		}
		public boolean isSelected(int x, int y){
			return isSelected(new Vector2f(getSlideableOffset().x+errorPos.x,getSlideableOffset().y+errorPos.y), new Vector2f(getSlideableOffset().x+errorPos.x+errorLenght,getSlideableOffset().y+errorPos.y), x, y);
		}
		public boolean isSelected(Vector2f pos1,Vector2f pos2,int x, int y){
			Rectangle boundingBox=new Rectangle((int)pos1.x-9, (int)pos1.y-18,(int)(pos2.x-pos1.x),9);
			return boundingBox.contains(x, y);
		}
		@Override
		public String toString(){
			return new StringBuilder().append("ErrorMarker[errorPos = ").append(errorPos).append(", errorLenght = ").append(errorLenght).append(", message = \"").append(message).append('"').append("]").toString();
		}
		public void update(int x, int y){
			if(selected){
				if(timeSelected<30)timeSelected++;
			}else{
				if(timeSelected>0)timeSelected--;
				if(timeSelected<20)timeSelected=0;
			}
			quickFix.forEach(fix->fix.update());
		}
	}
	
	private List<DoubleObject<List<String>,List<Integer>>> coloredText;
	
	public boolean colors=false;
	protected List<ErrorMarker> errors=new ArrayList<>();
	
	//public BlockPosM runPos;
	
	private int level=0;
	
	List<CharSequence> log=new ArrayList<>();
	
	protected int noChangeCompileTime=0,comileWaitAmmount=20;
	//public Object[] runArgs={};
	protected ProgramUsable program;
	private String selectedWord="";
	
	private VertexModel selection=null;
	protected List<String> 
		strings=new ArrayList<String>(),
		statics=new ArrayList<String>(),
		classes=new ArrayList<String>(),
		nubers=new ArrayList<String>();
	protected Map<int[],String> 
		vars=new HashMap<int[],String>(),
		unfinishedVars=new HashMap<int[],String>();
	public GuiJavaScriptEditor(Vec2i pos, Vec2i size){
		super(pos, size);
	}
	public ErrorMarker addError(ErrorMarker error){
		boolean same=false;
		for(ErrorMarker error0:errors){
			if(error0.equals(error)){
				same=true;
				break;
			}
		}
		if(!same){
			errors.add(error);
			return error;
		}
		return null;
	}
	private void colorCode(FontRendererMClipped fr){
		coloredText=new ArrayList<DoubleObject<List<String>,List<Integer>>>();
		if(statics.isEmpty()){
			statics.add("runPos");
			classes.add("World");
			classes.add("Math");
			classes.add("BlockPos");
			classes.add("EnumFacing");
		}
		for(int i=0;i<textBuffer.size();i++)coloredText.add(colorLine(fr,i));
		TessUtil.getVB().cleanUp();
		highlightWords(fr);
		selection=TessUtil.getVB().exportToNormalisedVertexBufferModel();
		selection.setInstantNormalCalculation(false);
		selection.translate(-0.5, 0, 0);
		selection.setClearing(false);
		resetColorData();
		noChangeCompileTime=comileWaitAmmount;
	}
	
	private void colorKeyWord(List<String> line, List<Integer> colors,String keyWord,int color){
		int keyWordLenght=keyWord.length();
		for(int j=0;j<line.size();j++){
			String part=line.get(j);
			boolean onlyVar=line.size()==1&&line.get(0).equals(keyWord);
			
			String rawLine=UtilM.join(line.toArray());
			if(part.contains(keyWord)&&(onlyVar||!part.equals(keyWord))){
				int keyWordStart=part.indexOf(keyWord);
				String 
					beforeKeyWord=part.substring(0, keyWordStart),
					KeyWord=part.substring(keyWordStart, keyWordStart+keyWordLenght),
					aferKeyWord=part.substring(keyWordStart+keyWordLenght, part.length());
				boolean shouldColor=true;
				if(rawLine.contains("\"")){
					int 
						before=StringUtils.countMatches(beforeKeyWord, "\""),
						after=StringUtils.countMatches(aferKeyWord, "\"");
					if(before%2!=0&&after%2!=0){
						shouldColor=false;
					}
				}
				if(shouldColor){
					line.set(j, aferKeyWord);
					line.add(j, KeyWord);
					colors.add(j,color);
					line.add(j, beforeKeyWord);
					colors.add(j,0xBED6FF);
					j--;
				}
			}
		}
	}
	
	private DoubleObject<List<String>,List<Integer>> colorLine(FontRendererMClipped fr,int i){
		int cyan=0x00D0D0,red=0xED7072,yellow=0xFFFF00,blue=0x4994FF,orange=0xEFC090,purple=0xDC78A4;
		List<String> line=new ArrayList<String>();
		List<Integer> colors=new ArrayList<Integer>();
		line.add(textBuffer.get(i).toString());
		colors.add(0xBED6FF);
		String 
			raw=ProgramCommon.removeSpacesFrom(textBuffer.get(i).toString(),'{','}','(',')','=',';','*','/','+','-','%','!','>','<','@','#'),
			words[]=raw.split("((?<=\\W)|(?=\\W))");
		boolean varFound=false,functionFound=false,functionStarted=false;
		for(int k=0;k<words.length;k++){
			String word=words[k];
			word=word.replaceAll(" ", "");
			if(!word.isEmpty()){
				if(word.equals("{")){
					level++;
				}else if(word.equals("}")){
					level--;
					List<int[]> varToRemove=new ArrayList<int[]>();
					for(Entry<int[],String> var:unfinishedVars.entrySet()){
						if(var.getKey()[0]>level){
							vars.put(new int[]{var.getKey()[0],var.getKey()[1],i}, var.getValue());
							varToRemove.add(var.getKey());
						}
					}
					for(int[] key:varToRemove)unfinishedVars.remove(key);
				}else{
					if(varFound){
						varFound=false;
						if(level>0)unfinishedVars.put(new int[]{level,i}, word);
						else statics.add(word);
					}
					if(functionStarted&&!word.contains("(")){
						functionStarted=false;
						
						int level=this.level+1;
						boolean prevValid=false;
						while(k<words.length&&!words[k].contains(")")&&!words[k].contains("{")&&!words[k].contains("}")&&!words[k].contains("var")&&!words[k].contains("=")){
							if(!words[k].contains(" ")){
								boolean valid=true;
								for(char c:words[k].toCharArray()){
									if(!Character.isJavaIdentifierPart(c)){
										valid=false;
										break;
									}
								}
								if(prevValid==valid)break;
								prevValid=valid;
								if(valid)unfinishedVars.put(new int[]{level,i}, words[k]);
							}
							k++;
						}
					}
					if(functionFound){
						functionFound=false;
						functionStarted=true;
					}
					if(word.equals("var"))varFound=true;
					if(word.equals("function"))functionFound=true;
					else{
						try{
							Float.parseFloat(word);
							nubers.add(word);
						}catch(Exception e){}
					}
				}
			}
			String rawLine=textBuffer.get(i).toString();
			if(rawLine.contains("\"")&&StringUtils.countMatches(rawLine, "\"")%2==0){
				boolean in=false;
				char[] cs=rawLine.toCharArray();
				for(int j=0;j<cs.length;j++){
					if(cs[j]=='"'){
						in=!in;
						if(in){
							int start=j,end=-1;
							j++;
							while(cs[j]!='"'&&j<cs.length)j++;
							end=j;
							in=!in;
							if(end+1-start>0)strings.add(rawLine.substring(start, end+1));
						}
					}
				}
			}
			colorKeyWord(line, colors, "var", cyan);
			colorKeyWord(line, colors, "return", cyan);
			colorKeyWord(line, colors, "function", cyan);
			colorKeyWord(line, colors, "true", cyan);
			colorKeyWord(line, colors, "false", cyan);
			colorKeyWord(line, colors, "for", cyan);
			colorKeyWord(line, colors, "if", cyan);
			colorKeyWord(line, colors, "while", cyan);
			colorKeyWord(line, colors, "new", cyan);
		}
		//color words
		ArrayList<Entry<int[], String>> vars1=new ArrayList();
		vars1.addAll(vars.entrySet());
		vars1.addAll(unfinishedVars.entrySet());
		for(Entry<int[],String> word:vars1){
			String keyWord=word.getValue();
			int keyWordLenght=keyWord.length();
			for(int j=0;j<line.size();j++){
				String part=line.get(j);
				boolean onlyVar=line.size()==1&&line.get(0).equals(keyWord);
				if(part.contains(keyWord)&&(onlyVar||!part.equals(keyWord))){
					if(level>=word.getKey()[0]&&i>=word.getKey()[1]&&(word.getKey().length>2?i<=word.getKey()[2]:true)){
						int keyWordStart=part.indexOf(keyWord);
						String 
							rawLine=UtilM.join(line.toArray()),
							beforeKeyWord=part.substring(0, keyWordStart),
							KeyWord=part.substring(keyWordStart, keyWordStart+keyWordLenght),
							aferKeyWord=part.substring(keyWordStart+keyWordLenght, part.length());
						boolean shouldColor=true;
						if(rawLine.contains("\"")){
							int 
								before=StringUtils.countMatches(beforeKeyWord, "\""),
								after=StringUtils.countMatches(aferKeyWord, "\"");
							if(before%2!=0&&after%2!=0){
								shouldColor=false;
							}
						}
						if(shouldColor){
							line.set(j, aferKeyWord);
							line.add(j, KeyWord);
							colors.add(j,blue);
							line.add(j, beforeKeyWord);
							colors.add(j,0xBED6FF);
							j--;
						}
					}
				}
			}
		}
		for(String word:nubers)colorKeyWord(line, colors, word, yellow);
		for(String word:classes)colorKeyWord(line, colors, word, red);
		for(String word:statics)colorKeyWord(line, colors, word, orange);
		for(String word:strings)colorKeyWord(line, colors, word, purple);
		
		return new DoubleObject<List<String>,List<Integer>>(line, colors);
	}
	public void compile(){
		noChangeCompileTime=comileWaitAmmount;
		
		program=new ProgramUsable();
		program.init(getText());
		if(program.lastResult instanceof Exception)try{
			readExceptions((Exception)program.lastResult);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void highlightWords(FontRendererMClipped fr){
		if(!isSelected()){
			StringBuilder line=getLine(getCursorPosition().y);
			if(line.length()>0){
				Matcher 
					forward=word.matcher(line),
					backward=word.matcher(new StringBuilder(line).reverse());
				
				selectedWord=line.substring(
					backward.find(line.length()-getCursorPosition().x)?line.length()-backward.start():0, 
					forward.find(getCursorPosition().x)?forward.start():getLength(getCursorPosition().y)
				);
			}
		}else selectedWord=getSelectedText();
		
		
		char[] chars=selectedWord.toCharArray();
		boolean valid=vars.containsValue(selectedWord)||statics.contains(selectedWord);
		
		if(valid)for(char c:chars){
			if(!Character.isJavaIdentifierPart(c)){
				valid=false;
				break;
			}
		}
		if(valid){
			int selectedWordLenght=selectedWord.length();
			if(!selectedWord.isEmpty()){
				for(int i=0;i<coloredText.size();i++){
					String line=getLine(i).toString();
					if(line.contains(selectedWord)){
						int count=0;
						do{
							count++;
							int 
								start=line.indexOf(selectedWord),
								end=start+selectedWordLenght;
							if((start==0?true:!Character.isJavaIdentifierPart(line.charAt(start-1)))&&(end==line.length()?true:!Character.isJavaIdentifierPart(line.charAt(end)))){
								int
									offset=fr.getStringWidth(line.substring(0, start)),
									lenght=fr.getStringWidth(line.substring(start, end)),
									minX=pos.x+offset-1,
									minY=pos.y+i*fr.FONT_HEIGHT-1,
									maxX=minX+lenght+2,
									maxY=minY+11;
								
								TessUtil.getVB().addVertex(minX, maxY, 0);
								TessUtil.getVB().addVertex(maxX, maxY, 0);
								TessUtil.getVB().addVertex(maxX, minY, 0);
								TessUtil.getVB().addVertex(minX, minY, 0);
							}
							line=line.substring(end);
						}while(line.contains(selectedWord)&&count<100);
						if(count==100)LogUtil.error("Word highlight error!");
					}
				}
			}
		}
	}
	@Override
	public boolean keyTyped(int code, char ch){
		errors.clear();
		boolean result=super.keyTyped(code, ch);
		colorCode(FontRendererMClipped.get());
		return result;
	}
	@Override
	public void mouseClicked(int x, int y, int button){
		boolean errorHover=false;
		for(ErrorMarker errorMarker:errors){
			if(errorMarker.selected){
				if(errorMarker.timeSelected>20)errorHover=true;
				errorMarker.click(x, y);
				break;
			}
		}
		if(!errorHover)super.mouseClicked(x, y, button);
		colorCode(FontRendererMClipped.get());
	}
	
	@Override
	public void mouseClickMove(int x, int y){
		super.mouseClickMove(x, y);
		colorCode(FontRendererMClipped.get());
	}
	private void readExceptions(Exception e){
		String s=e.getMessage();
		if(s.startsWith("ReferenceError: ")){
			String invalidReference=s.substring("ReferenceError: \"".length(), s.length());
			invalidReference=invalidReference.substring(0, invalidReference.indexOf('"'));
			
			int line=Integer.parseInt(s.substring(s.indexOf("line number ")+"line number ".length()))-ProgramUsable.jsJavaBridgeLines-1;
			int colum=0;
			
			StringBuilder word=new StringBuilder();
			for(char c:getLine(line).toString().toCharArray()){
				if(Character.isJavaIdentifierPart(c))word.append(c);
				else{
					int lenght=word.length();
					if(lenght>0){
						if(word.toString().equals(invalidReference))break;
						else colum+=lenght;
						word=new StringBuilder();
					}
					colum++;
				}
			}
			
			addError(new ErrorMarker(new Vec2i(colum, line), invalidReference.length(),s));
			
			return;
		}
		
		if(s.startsWith("<eval>:")){
			s=s.substring("<eval>:".length(),s.indexOf('\n')-1);
			
			int line=Integer.parseInt(s.substring(0,s.indexOf(":")))-ProgramUsable.jsJavaBridgeLines-1;
			int colum=Integer.parseInt(s.substring(s.indexOf(":")+1,s.indexOf(" Exp")));
			String 
				found=s.substring(s.indexOf("but found ")+"but found ".length()),
				bracket1=FontEffectUtil.RED+"<"+FontEffectUtil.DARK_BLUE,
				bracket2=FontEffectUtil.RED+">"+FontEffectUtil.RESET;
			
			
			ErrorMarker error=addError(new ErrorMarker(new Vec2i(colum, line), found.length(),
					new StringBuilder()
						.append("Expected ")
						.append(bracket1)
						.append(s.substring(s.indexOf("Expe")+"Expected ".length(),s.indexOf(" but found ")))
						.append(bracket2)
						.append(" but found ")
						.append(bracket1)
						.append(found)
						.append(bracket2)
					.toString()));
			
			if(error!=null){
				error.addQuickFix("Replace with expected.", new ObjectProcessor<ErrorMarker>(){@Override public ErrorMarker pocess(ErrorMarker error, Object...objects){
					Vec2i pos=error.physicalPos;
					getLine(pos.y).replace(pos.x, pos.x+error.physicalLenght, error.message.subSequence(error.message.indexOf('<')+3, error.message.indexOf('>')-2).toString());
					return null;
				}});
				error.addQuickFix("Create local variable.", new ObjectProcessor<ErrorMarker>(){@Override public ErrorMarker pocess(ErrorMarker error, Object...objects){
					Vec2i pos=error.physicalPos;
					addLine(
						new StringBuilder()
						.append(getStartWhiteSpace(getLine(pos.y).toString()))
						.append("var ")
						.append(error.message.subSequence(error.message.lastIndexOf('<')+3, error.message.lastIndexOf('>')-2))
						.append("=\"undefined\";"),
					pos.y);
					return null;
				}});
				error.addQuickFix("Create global variable.", new ObjectProcessor<ErrorMarker>(){@Override public ErrorMarker pocess(ErrorMarker error, Object...objects){
					addLine(new StringBuilder("var ").append(error.message.subSequence(error.message.lastIndexOf('<')+3, error.message.lastIndexOf('>')-2)).append("=\"undefined\";"), 0);
					return null;
				}});
			}
			return;
		}
		if(s.startsWith("No such function")){
			errors.add(new ErrorMarker(new Vec2i(0, textBuffer.size()-1), 1,s));
			return;
		}
		PrintUtil.println(s);
	}
	protected void rednerColoredCode(FontRendererMClipped fr){
		if(selection!=null){
			GL11U.texture(false);
			selection.pushMatrix();
			selection.translate(getSlideableOffset().x, getSlideableOffset().y, 0);
			OpenGLM.color(0, 0, 0, 1);
			selection.draw();
			
			OpenGLM.color(0.2F, 0.2F, 0.2F, 1);
			selection.setDrawAsWire(true);
			selection.draw();
			selection.setDrawAsWire(false);
			
			OpenGLM.color(1, 1, 1, 1);
			selection.popMatrix();
			GL11U.texture(true);
		}
		for(int i=0;i<coloredText.size();i++){
			int offset=0;
			List<String> line=coloredText.get(i).obj1;
			List<Integer> colors=coloredText.get(i).obj2;
			for(int j=0;j<line.size();j++){
				fr.min=new Vector2f(pos.x-2,pos.y-2);
				fr.max=new Vector2f(pos.x+size.x+2,pos.y+size.y+2);
				String lin=line.get(j);
				fr.drawString(line.get(j), pos.x+offset, pos.y+i*fr.FONT_HEIGHT, colors.get(j));
				offset+=fr.getStringWidth(lin);
			}
		}
	}
	@Override
	protected void rednerText(FontRendererMClipped fr){
		if(colors){
			if(coloredText==null)colorCode(fr);
			rednerColoredCode(fr);
		}
		else super.rednerText(fr);
		errors.forEach(error->error.draw(mouse.x, mouse.y));
	}
	protected void resetColorData(){
		strings=new ArrayList<String>();
		statics=new ArrayList<String>();
		classes=new ArrayList<String>();
		nubers=new ArrayList<String>();
		vars=new HashMap<int[],String>();
		unfinishedVars=new HashMap<int[],String>();
		statics.add("runPos");
		classes.add("World");
		classes.add("Math");
		classes.add("BlockPos");
		classes.add("EnumFacing");
		level=0;
	}
	
	@Override
	public void update(){
		super.update();
		if(noChangeCompileTime>0){
			noChangeCompileTime--;
			if(noChangeCompileTime==0){
				compile();
			}
		}
		errors.forEach(err->err.update(xMouseArrow, yMouseArrow));
	}
}
