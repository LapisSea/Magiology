package com.magiology.windowsgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.core.MReference;
import com.magiology.core.Magiology;
import com.magiology.io.IOReadableMap;
import com.magiology.io.ZipManager;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;

public class ModInfoGUI extends JFrame{
	public static class Line{
		public Color color;
		public Font font;
		public boolean isUserCasted;
		public String string;
		public Line(String string,Font font,Color color,boolean isUserCasted){
			this.string=string;
			this.font=font;
			this.color=color;
			this.isUserCasted=isUserCasted;
		}
	}
	private class Renderer extends JLabel{
		private final ModInfoGUI gui;
		public Renderer(ModInfoGUI gui){
			super(imageIcons[0]);
			this.gui=gui;
		}
		@Override
		public void paintComponent(Graphics g){
			
			if(g instanceof Graphics2D){
				((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			}
			
			super.paintComponent(g);
			int offset=txtSize/15;if(offset==0)offset=1;
			for(int a=0;a<lines.length;a++)if(lines[a]!=null){
				if(lines[a].font.getSize()!=gui.txtSize){
					Font font=lines[a].font;
					Font newFont=new Font(font.getName(),font.getStyle(),gui.txtSize);
					lines[a].font=newFont;
				}
				
				g.setFont(lines[a].font);
				g.translate(0, gui.offset);
				for(int k=0;k<2;k++){
					if(k==1)g.setColor(lines[a].color);
					else{
						g.setColor(Color.BLACK);
						g.translate(1, 1);
					}
					g.drawString(lines[a].string, 14, 340-g.getFont().getSize()*a);
					if(k==0)g.translate(-1,-1);
				}
				g.translate(0, -gui.offset);
			}
			g.drawImage(imageIcons[4].getImage(), 0, 0, this);
			g.setFont(new Font(Font.SANS_SERIF, Font.BOLD,12));
			for(int a=0;a<2;a++){
				if(a==1)g.setColor(Color.LIGHT_GRAY);
				else{
					g.setColor(Color.BLACK);
					g.translate(1, 1);
				}
				g.drawString(MReference.MODID+" status: "+(modStat?"Loaded!":"Loading..."), 390, 45);
				g.drawString("MC status: "+(MCStat?"Loaded!":"Loading..."), 394, 60);
				if(a==0)g.translate(-1,-1);
			}
			if(MCStat!=MCStat2){
				MCStat2=MCStat;
				SoundPlayer.playSound(MReference.MODS_SUBFOLDER_WIN_GUI+"/Loaded.wav");
			}
			if(modStat!=modStat2){
				modStat2=modStat;
				SoundPlayer.playSound(MReference.MODS_SUBFOLDER_WIN_GUI+"/Loaded.wav");
			}
		}
		
	}
	private class TheHandler1 implements ActionListener{
		ModInfoGUI gui;
		public TheHandler1(ModInfoGUI modInfoGUI){
			gui=modInfoGUI;
		}
		@Override
		public void actionPerformed(ActionEvent event){
			Object src=event.getSource();
			String command=event.getActionCommand();
			if(src==null)return;
			if(src instanceof JButton){
				if(src==button1)exit();
				else if(src==up){
					int lastLine=0;
					for(int a=0;a<lines.length;a++)if(lines[a]!=null&&!lines[a].string.isEmpty())lastLine=a;
					gui.moveTheText(lastLine*txtSize-275);
				}
				else if(src==down)gui.moveTheText(-offset);
				return;
			}
			if(src instanceof JTextField){
				if(src==item1){
					String start="I am ";
					if(command.toLowerCase().equals("lol")){
						addLine(newLine("What is so funny?", new Font(Font.SANS_SERIF, Font.ITALIC+Font.BOLD,15),Color.LIGHT_GRAY,true));
						item1.setText("");
					}else if(command.toLowerCase().startsWith(start.toLowerCase())&&command.length()>start.length()){
						char[] chars=command.toCharArray();
						String name="";
						boolean lol=false;
						if((chars[chars.length-1]+"").equals("!"))lol=true;
						for(int a=start.length();a<chars.length;a++)name+=lol&&a==chars.length-1?"":chars[a];
						if(!first){
							if(name.equals("Bevo")||name.equals("Etho")||name.equals("Nathaniel1985")||name.equals("direwolf20"))addLine(newLine("Hi "+name+"! I am LapisSea, your subscriber! ;D", new Font(Font.SANS_SERIF, Font.ITALIC,15),Color.BLUE,true));
							else addLine(newLine("Hi "+name+"! I am LapisSea! :)", new Font(Font.SANS_SERIF, Font.ITALIC,15),Color.BLUE,true));
						}else addLine(newLine("I know!",Color.WHITE,true));
						item1.setText("");
						first=true;
					}else if(dropDownID==0){clearUserInput();
					}else if(dropDownID==1){SetFontSize(command);
					}else if(dropDownID==2){setSide(command);
					}else if(dropDownID==3){setCommand(command);
					}else if(dropDownID==4){useCommand(command);
					}else if(dropDownID==5){printCommands();
					}else if(dropDownID==6){printValues();
					}else{
						addLine(newLine(dropDownID+" is an invalid dropbox command!", new Font(Font.SANS_SERIF, Font.BOLD,15), Color.RED, true));
						PrintUtil.println("what?");
					}
					Update();
				}
				return;
			}
		}
	}
	private class TheHandler2 implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent event){
			Object src=event.getSource();
			if(src instanceof JComboBox){
				if(src==dropDown){
					dropDownID=dropDown.getSelectedIndex();
					String string="Press Enter to use!";
					switch (dropDownID){
					case 1:string=txtSize+"";break;
					case 2:string="";break;
					case 3:string="c";break;
					case 4:string="";break;
					}
					item1.setText(string);
				}
			}else if(src instanceof JCheckBox){
				if(src==CB1){
					if(CB1Last!=CB1.isSelected()){
						addLine(newLine("This window will "+(CB1.isSelected()?"not ":"")+"open on the next startup!",CB1.isSelected()?Color.RED:Color.YELLOW,true));
						
						Magiology.infoFile.set("GUIOpen", !CB1.isSelected());
						Magiology.infoFile.writeToFile();
					}
					CB1Last=CB1.isSelected();
				}
			}
			Update();
		}
	}
	private class TheHandler3 implements MouseMotionListener, MouseListener, MouseWheelListener{
		int lastMousePos;
		@Override public void mouseClicked(MouseEvent e){}
		@Override
		public void mouseDragged(MouseEvent e){
			moveTheText(e.getYOnScreen()-lastMousePos);
			lastMousePos=e.getYOnScreen();
		}
		@Override public void mouseEntered(MouseEvent e){}
		@Override public void mouseExited(MouseEvent e){}
		@Override
		public void mouseMoved(MouseEvent e){
			lastMousePos=e.getYOnScreen();
		}
		@Override public void mousePressed(MouseEvent e){}
		@Override public void mouseReleased(MouseEvent e){}
		@Override
		public void mouseWheelMoved(MouseWheelEvent e){
			moveTheText(-(int)(e.getPreciseWheelRotation()*txtSize*1.5));
		}
	}
	private class TheHandler4 implements KeyListener{
		@Override public void keyPressed(KeyEvent e){}
		@Override public void keyReleased(KeyEvent e){}
		@Override public void keyTyped(KeyEvent e){}
	}
	JButton button1;
	JCheckBox CB1;
	boolean CB1Last=false;
	public String[] comands={"clearUserInput()","Set font size:","Set side:","selectCommand()","useCommand()","printCommands()","printValues()"};
	JButton down;
	JComboBox dropDown;
	int dropDownID,commandID;
	public int exitOn=0;
	TheHandler1 handler1=new TheHandler1(this);
	TheHandler2 handler2=new TheHandler2();
	ImageIcon[] imageIcons=null;
	public boolean isExited=false,first=false,autoRemove=false,autoActivate=false,isMuted=false,MCStat=false,modStat=false,MCStat2=false,modStat2=false;
	JTextField item1;
	TheHandler4 keyEvents=new TheHandler4();
	FlowLayout layout=new FlowLayout();
	
	public Line[] lines=new Line[200];
	
	
	TheHandler3 mouseEvents=new TheHandler3();
	String SExitOn="";
	
	public long systemTime=-1;
	int txtSize=18,offset=0;
	JButton up;
	Renderer visual;
	private ModInfoGUI(){
		super(MReference.NAME+" graphical interface");
		imageIcons=ZipManager.getAllImagesFromZip(MReference.MODS_SUBFOLDER_WIN_GUI+"/MagiZip");
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(imageIcons[2].getImage(), new Point(0,1), "sword"));
		setIconImage(imageIcons[1].getImage());
		//middle
		setLayout(new BorderLayout());
		visual=new Renderer(this);
		setContentPane(visual);
		//right
		layout.setAlignment(FlowLayout.RIGHT);
		setLayout(layout);
		up=new JButton("",imageIcons[3]);
		down=new JButton("",imageIcons[5]);
		//left
		layout.setAlignment(FlowLayout.LEFT);
		setLayout(layout);
		setResizable(false);
		addLine(newLine("STARTED LOADING "+MReference.MODID.toUpperCase(), new Font(Font.SANS_SERIF, Font.BOLD+Font.ITALIC,15),Color.RED));
		
		//dropDowns
		dropDown=new JComboBox(comands);
		
		//buttons
		button1=new JButton("EXIT");
		button1.setFont(new Font("Serif",Font.BOLD,10));
		up.setPreferredSize(new Dimension(20, 20));
		down.setPreferredSize(new Dimension(20, 20));
		//checkBoxes
		CB1=new JCheckBox("never open again");
		CB1.setBounds(CB1.getX(), CB1.getY(), 10, 1);
		//boxes
		item1=new JTextField("");
		item1.setPreferredSize(new Dimension(120, item1.getPreferredSize().height));
		//register
		button1.addActionListener(handler1);
		item1.addActionListener(handler1);
		CB1.addItemListener(handler2);
		dropDown.addItemListener(handler2);
		up.addActionListener(handler1);
		down.addActionListener(handler1);
		visual.addMouseListener(mouseEvents);
		visual.addMouseMotionListener(mouseEvents);
		visual.addMouseWheelListener(mouseEvents);
		addKeyListener(keyEvents);
		setFocusable(true);
		add(button1);
		add(CB1);
		add(dropDown);
		add(item1);
		add(up);
		add(down);
		SoundPlayer.playSound(MReference.MODS_SUBFOLDER_WIN_GUI+"/OpenUp.wav");
	}
	public ModInfoGUI(int x,int y,int xPos,int yPos){
		this();
		int width=548,height=384;
		x/=2;y/=2;
		x-=width/2;
		y-=height/2;
		x+=xPos;y+=yPos;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(width,height);
		setLocation(new Point(x,y));
//		setUndecorated(true);
		setVisible(true);
	}
	public void addLine(Line string){
		if(isExited)return;
		if(isMuted)return;
		if(autoRemove)clearUserInput();
		Line[] a=lines.clone();
		for(int b=0;b<lines.length;b++){
			if(b==0)lines[0]=string;
			else lines[b]=a[b-1];
		}
		moveTheText(-offset);
		Update();
	}
	public void clear(){
		for(int b=0;b<lines.length;b++)lines[b]=newLine("", Color.WHITE);
		item1.setText("");
		moveTheText(-offset);
	}
	public void clearUserInput(){
		for(int b=0;b<lines.length;b++)if(lines[b]!=null&&lines[b].isUserCasted)lines[b]=newLine("ClearThisLine!6578", Color.WHITE);
		Line[] a=lines.clone();
		Line[] cleared=null;
		for(Line b:a){
			if(!b.string.equals("ClearThisLine!6578")){
				if(cleared==null)cleared=new Line[]{b};
				else cleared=ArrayUtils.add(cleared, b);
			}
		}
		lines=cleared.clone();
		for(int c=lines.length;c<200;c++)lines=ArrayUtils.add(lines, newLine("", Color.WHITE));
		item1.setText("");
	}
	public void downloadData(IOReadableMap infoFile){
		if(infoFile.getI("GUITxtSize")==0)infoFile.set("GUITxtSize", 18);
		txtSize=infoFile.getI("GUITxtSize");
		autoRemove=infoFile.getB("GUIAR");
		autoActivate=infoFile.getB("GUIAA");
		if(infoFile.getI("GUIExitOn")==0)infoFile.set("GUIExitOn", 2);
		exitOn=infoFile.getI("GUIExitOn");
		isMuted=true;
		setCommand("c5");
		useCommand(""+exitOn);
		setCommand("c0");
		isMuted=false;
		item1.setText("Press Enter to use!");
		setSide(infoFile.getI("GUIPos")==1?"left":"right");
		
		if(txtSize==-1){
			txtSize=19;
			infoFile.set("GUITxtSize",txtSize);
			infoFile.writeToFile();
		}
		Update();
	}
	public void exit(){
		if(isExited)return;
		Frame[] window=Frame.getFrames();
		for(Frame a:window)if(tryToExit(a)){
			Magiology.modInfGUI=null;
			return;
		}
	}
	public void moveTheText(int smth){
		offset+=smth;
		int lastLine=0;
		for(int a=0;a<lines.length;a++)if(lines[a]!=null&&!lines[a].string.isEmpty())lastLine=a;
		int calculatedMax=lastLine*txtSize-275;
		if(offset>calculatedMax)offset=calculatedMax;
		if(offset<0)offset=0;
		if(smth==0)return;
		Update();
	}
	public Line newLine(String string,Color color){return new Line(string, new Font(Font.SANS_SERIF, Font.PLAIN,15),color,false);}
	
	public Line newLine(String string,Color color,boolean isUserCasted){return new Line(string, new Font(Font.SANS_SERIF, Font.PLAIN,15),color,isUserCasted);}
	public Line newLine(String string,Font font,Color color){return new Line(string, font,color,false);}
	public Line newLine(String string,Font font,Color color,boolean isUserCasted){return new Line(string, font,color,isUserCasted);}
	public void printCommands(){
		
		if(autoRemove)addLine(newLine("dummy", Color.RED, true));
		addLine(newLine("____________", Color.RED, true));
		boolean var1=autoRemove;
		autoRemove=false;
		addLine(newLine("Reading commands...", Color.YELLOW, true));
		//-------------------------------------------
		Color darkYellow=new Color(Color.YELLOW.getRed()-40, Color.YELLOW.getGreen()-40, Color.YELLOW.getBlue());
		addLine(newLine("Info: write cX to assign a new command", Color.GREEN, true));
		addLine(newLine("Info: and after that write what you want to do", Color.GREEN, true));
		addLine(newLine("c1, set if console should remove user input before", Color.YELLOW, true));
		addLine(newLine("printing new input, (true,false or 0,1)", Color.YELLOW, true));
		addLine(newLine("c2, restart the mod window, (nothing required)", darkYellow, true));
		addLine(newLine("c3, clear all lines, (nothing required)", Color.YELLOW, true));
		addLine(newLine("c4, automatically activate commands that", darkYellow, true));
		addLine(newLine("require nothing to run, (true,false)", darkYellow, true));
		addLine(newLine("c5, sets when to auto exit the mod", Color.YELLOW, true));
		addLine(newLine("window, (1=Manual (you have to close it),", Color.YELLOW, true));
		addLine(newLine("2=World open, 3=Start menu)", Color.YELLOW, true));
		addLine(newLine("c6, reloads the data file (helps if it is invalid)", darkYellow, true));
		//-------------------------------------------
		addLine(newLine("-------------------",new Font(Font.SANS_SERIF, Font.BOLD,15), Color.GREEN, true));
		autoRemove=var1;
	}
	public void printValues(){
		if(autoRemove)addLine(newLine("dummy", Color.RED, true));
		addLine(newLine("____________", Color.RED, true));
		boolean var1=autoRemove;
		autoRemove=false;
		addLine(newLine("Loading data...", Color.YELLOW, true));
		//-------------------------------------------
		addLine(newLine("Window open on startup = "+!CB1.isSelected(), Color.YELLOW, true));
		addLine(newLine("Window side = "+(Magiology.infoFile.getI("GUIPos")==1?"left":"right"), Color.YELLOW, true));
		addLine(newLine("Window text size = "+txtSize, Color.YELLOW, true));
		addLine(newLine("Auto remove user input = "+var1, Color.YELLOW, true));
		addLine(newLine("Auto invoke no input commands = "+autoActivate, Color.YELLOW, true));
		addLine(newLine("Window will exit "+(exitOn!=1?"on ":"")+SExitOn+" (id="+exitOn+")", Color.YELLOW, true));
		//-------------------------------------------
		addLine(newLine("------------------",new Font(Font.SANS_SERIF, Font.BOLD,15), Color.GREEN, true));
		autoRemove=var1;
	}
	public void restart(){
		if(isExited)return;
		ModInfoGUI a=new ModInfoGUI(4000, 1000, -680, 0);
		a.setLocation(new Point(getLocation().x,getLocation().y));
		a.downloadData(Magiology.infoFile);
		a.clear();
		a.lines=lines.clone();
		if(!a.lines[0].string.equals("THIS IS A NEW WINDOW!"))a.addLine(newLine("THIS IS A NEW WINDOW!", new Font(Font.SANS_SERIF, Font.BOLD, 1), Color.BLUE, true));
		else a.lines[0].string+="!";
		exit();
	}
	public void setCommand(String command){
		boolean isValid=false;
		if(command.startsWith("c")||command.startsWith("C")){
			char[] chars=command.toCharArray();
			if(chars.length>=2){
				if(chars[0]=='C'||chars[0]=='c'){
					String stringedNumber="";
					for(int a=1;a<chars.length;a++){
						stringedNumber+=chars[a];
					}
					if(UtilM.isInteger(stringedNumber)){
						isValid=true;
						int before=commandID;
						commandID=Integer.parseInt(stringedNumber);
						if(before==commandID)addLine(newLine("Command "+commandID+" is already selected!",new Font(Font.SANS_SERIF, Font.BOLD,15), Color.CYAN, true));
						else addLine(newLine("Command id = "+commandID,new Font(Font.SANS_SERIF, Font.BOLD,15), Color.CYAN, true));
						if(autoActivate)switch(commandID){
						case 2:useCommand("#autoInvoked");break;
						case 3:useCommand("#autoInvoked");break;
						case 6:useCommand("#autoInvoked");break;
						}
					}
				}
			}
		}
		if(!isValid)addLine(newLine("ERROR! WRONG ID FORMAT! Please enter a new one.",new Font(Font.SANS_SERIF, Font.BOLD,15), Color.RED, true));
		
		item1.setText("c");
	}
	public void SetFontSize(String command){
		if(UtilM.isInteger(command)){
			int var1=txtSize;
			txtSize=Integer.parseInt(command);
			Magiology.infoFile.set("GUITxtSize", txtSize);
			Magiology.infoFile.writeToFile();
			if(var1!=txtSize)addLine(newLine("Font size changed to: "+txtSize,Color.YELLOW,true));
		}else if(!command.isEmpty())addLine(newLine("Invalid number! Use only 0-9",Color.RED,true));
	}
	public void setSide(String command){
		int y=Toolkit.getDefaultToolkit().getScreenSize().height/2-192,left=4,right=Toolkit.getDefaultToolkit().getScreenSize().width-552;
		if(command.equals("left")){
			Magiology.infoFile.set("GUIPos", 1);
			setLocation(new Point(left,y));
		}else if(command.equals("right")){
			Magiology.infoFile.set("GUIPos", 0);
			setLocation(new Point(right,y));
		}else if(command.equals("switch")){
			if(Magiology.infoFile.getI("GUIPos")==1){
				Magiology.infoFile.set("GUIPos", 0);
				setLocation(new Point(right,y));
			}else{
				Magiology.infoFile.set("GUIPos", 1);
				setLocation(new Point(left,y));
			}
		}
		else addLine(newLine("THAT IS NOT A VALID SIDE!"+(RandUtil.RI(20)==0?"(i think)":""), Color.RED, true));
		Magiology.infoFile.writeToFile();
	}
	public boolean tryToExit(Frame frame){
		if(isExited)return false;
		if(frame==this){
			addLine(newLine("Bye Bye! o/", Color.BLUE));
			Update();
			Magiology.infoFile.writeToFile();
			SoundPlayer.playSound(MReference.MODS_SUBFOLDER_WIN_GUI+"/Close.wav");
			dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			isExited=true;
			return true;
		}return false;
	}
	public void Update(){
		if(isExited)return;
		Magiology.modInfGUI=this;
		for(int b=0;b<lines.length;b++)if(lines[b]==null)lines[b]=newLine("", Color.WHITE);
		SwingUtilities.invokeLater(new Runnable(){@Override public void run(){SwingUtilities.updateComponentTreeUI(ModInfoGUI.this);}});
		moveTheText(0);
	}
	public void useCommand(String command){
		if(command.equals("activate MLG mode!")){
			for(int a=0;a<30;a++)addLine(newLine("nope...",new Font(Font.SANS_SERIF, Font.BOLD,15), new Color(RandUtil.RI(255),RandUtil.RI(255),RandUtil.RI(255)), true));
			UtilM.exitSoft();
			return;
		}
		addLine(newLine("Command on "+commandID+(command.equals("#autoInvoked")?" automatically":"")+" invoked!",new Font(Font.SANS_SERIF, Font.BOLD,15), new Color(RandUtil.RI(255),RandUtil.RI(255),RandUtil.RI(255)), true));
		boolean isInputOk=false;
		boolean isInputInvoked=false;
		switch(commandID){
		case 1:{
			isInputInvoked=true;
			int res=-1;
			if(UtilM.isInteger(command))res=Integer.parseInt(command);
			else if(UtilM.isBoolean(command))res=UtilM.booleanToInt(Boolean.parseBoolean(command));
			if(res==0){
				autoRemove=true;
				isInputOk=true;
				addLine(newLine("Command output: User input will be removed",new Font(Font.SANS_SERIF, Font.BOLD,15), Color.GREEN, true));
			}else if(res==1){
				autoRemove=false;
				isInputOk=true;
				addLine(newLine("Command output: User input will not be removed",new Font(Font.SANS_SERIF, Font.BOLD,15), Color.RED, true));
			}
			Magiology.infoFile.set("GUIAR", autoRemove);
			Magiology.infoFile.writeToFile();
		}break;
		case 2:{
			isInputInvoked=true;
			isInputOk=true;
			restart();
		}break;
		case 3:{
			clear();
			isInputInvoked=true;
			isInputOk=true;
			if(!autoRemove)addLine(newLine("EVERYTHING CLEARED!",new Font(Font.SANS_SERIF, Font.BOLD,15), Color.RED, true));
		}break;
		case 4:{
			isInputInvoked=true;
			if(UtilM.isBoolean(command)){
				if(Boolean.parseBoolean(command)){
					isInputOk=true;
					autoActivate=true;
					addLine(newLine("No input commands will be auto activated!", Color.GREEN, true));
				}else{
					isInputOk=true;
					autoActivate=false;
					addLine(newLine("No input commands will not be auto activated!", Color.RED, true));
				}
			}
			Magiology.infoFile.set("GUIAA", autoActivate);
			Magiology.infoFile.writeToFile();
		}break;
		case 5:{
			isInputInvoked=true;
			if(UtilM.isInteger(command)){
				isInputOk=true;
				exitOn=Integer.parseInt(command);
				isInputOk=true;
				switch(exitOn){
				case 1:{SExitOn="Manual (you have to close it)";}break;
				case 2:{SExitOn="World open";}break;
				case 3:{SExitOn="Start menu";}break;
				default:isInputOk=false;break;
				}
				Magiology.infoFile.set("GUIExitOn", exitOn);
				Magiology.infoFile.writeToFile();
			}
		}break;
		case 6:{
			isInputInvoked=true;
			isInputOk=true;
			Magiology.infoFile.readFromFile();
			
		}break;
		}
		if(!isInputOk&&!isInputInvoked)addLine(newLine("NoSuchCommand exception!",new Font(Font.SANS_SERIF, Font.BOLD,15), new Color(RandUtil.RI(255),RandUtil.RI(255),RandUtil.RI(255)), true));
		else if(!isInputOk&&isInputInvoked)addLine(newLine("WrongInput exception!",new Font(Font.SANS_SERIF, Font.BOLD,15), new Color(RandUtil.RI(255),RandUtil.RI(255),RandUtil.RI(255)), true));
		item1.setText("");
		Update();
	}
}
