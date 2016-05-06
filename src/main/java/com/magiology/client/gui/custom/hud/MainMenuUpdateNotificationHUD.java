package com.magiology.client.gui.custom.hud;

import static com.magiology.core.MReference.*;

import java.awt.Rectangle;
import java.io.File;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.magiology.client.gui.custom.DownloadingIcon;
import com.magiology.core.MReference;
import com.magiology.handlers.web.DownloadingHandler;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;
import com.magiology.util.utilobjects.vectors.physics.PhysicsVec3F;

public class MainMenuUpdateNotificationHUD extends HUD{
	
	public static MainMenuUpdateNotificationHUD instance=new MainMenuUpdateNotificationHUD();
	public  PhysicsFloat 
		backgroundBlue=new PhysicsFloat(0,0.01F),
		textTransition=new PhysicsFloat(0,0.3F,true),
		popup=new PhysicsFloat(-0.5F,0.05F),
		down=new PhysicsFloat(0,0.05F),
		button=new PhysicsFloat(0,0.3F,true);
	public ColorF backgroundColor=new ColorF(0, 0, 0, 0.2F);
	private VertexRenderer buff=TessUtil.getVB();
	public boolean isClicked,selected,isDownloading=false,downloadingInvoked=false;
	private DoubleObject<Integer, Boolean> mousePrev;
	public PhysicsVec3F rotation=new PhysicsVec3F(new Vec3M(), new Vec3M(0.2,0.2,0.2));
	private String[] text={
		"New update for: "+MReference.NAME,
		"[click for more]",
		"Click [HERE] to update the mod!",
		"WARNING: The message above will:",
		"1. turn off minecraft!",
		"2. open an automatic updater program",
		"The aplication will:",
		"1. remove the old mod file",
		"2. download a new one",
		"3. place it in your mods folder"
	};
	public int timeout,lenghts[],biggest=-1;
	public MainMenuUpdateNotificationHUD(){
		popup.friction=0.95F;
		popup.addWall("1", 0.9999F, true);
		popup.bounciness=0.4F;
		popup.wantedPoint=1;
		
		down.friction=0.95F;
		down.addWall("1", 0.9999F, true);
		down.bounciness=0.4F;
		down.wantedPoint=1;
		
		backgroundBlue.friction=0.95F;
		backgroundBlue.addWall("1", 1, true);
		backgroundBlue.addWall("0", 0, false);
		backgroundBlue.bounciness=0.4F;
		for(String a:text){
			int lenght=Font.FR().getStringWidth(a);
			biggest=Math.max(biggest, lenght);
			lenghts=ArrayUtils.add(lenghts, lenght);
		}
	}
	
	private Rectangle getBoundingBox(boolean hasGL11Transformation){
		float scale=0;
		if(hasGL11Transformation)scale=UtilC.getGuiScale();
		else scale=UtilC.getGuiScaleRaw();
		int FH=Font.FR().FONT_HEIGHT;
		return new Rectangle((int) ((4)*scale),(int) ((4)*scale),(int) ((biggest+4)*scale),(int) ((FH*2+FH*(text.length-2)*backgroundBlue.getPoint()+4)*scale));
	}
	
	private Rectangle getBoundingBoxClick(boolean hasGL11Transformation){
		float scale=0;
		if(hasGL11Transformation)scale=UtilC.getGuiScale();
		else scale=UtilC.getGuiScaleRaw();
		int FH=Font.FR().FONT_HEIGHT;
		return new Rectangle(
				(int) ((4+(biggest-lenghts[2])/2+Font.FR().getStringWidth("Click "))*scale),
				(int) ((4+FH*2)*scale),
				(int) ((Font.FR().getStringWidth("[HERE]")+3)*scale),
				(int) ((FH+2)*scale));
	}
	
	private void onClick(int x,int y){
		if(isClicked&&getBoundingBoxClick(false).contains(x, y)){
			terminateAndOpenUpdater();
		}
		isClicked=true;
		timeout=40;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void reimplementMouseEvents(){
		Rectangle boundingBox=getBoundingBox(false);
		//simulate mouse event because forge mouse event is not active in main menu :(
		int x1=Mouse.getX(),y1=Display.getHeight()-Mouse.getY();
		selected=boundingBox.contains(x1,y1);
		if(mousePrev!=null){
			if(Mouse.getEventButton()==0&&Mouse.getEventButtonState()&&!mousePrev.obj2){
				if(boundingBox.contains(x1, y1)){
					onClick(x1,y1);
				}
			}
		}
		mousePrev=new DoubleObject<Integer, Boolean>(Mouse.getEventButton(),Mouse.getEventButtonState());
	}
	@Override
	public void render(int width,int height, float partialTicks){
		float 
			x=rotation.x.getPoint(), 
			y=rotation.y.getPoint(), 
			z=rotation.z.getPoint(),
			txtTrans=textTransition.getPoint()*0.25F;
		ColorF col=new ColorF(0.75+x/80+txtTrans, 0.75+y/80+txtTrans, 0.75+z/4+txtTrans, 1);
		int FH=Font.FR().FONT_HEIGHT;
		Rectangle boundingBox=getBoundingBox(true);
		
		GL11U.protect();
		GL11U.glRotate(popup.getPoint()*90-90, -popup.getPoint()*30+30, popup.getPoint()*40-40, biggest/2, FH, 0);
		GL11U.glScale(popup.getPoint());
		GL11U.setUpOpaqueRendering(1);
		GL11U.glLighting(false);
		GL11U.glDepth(false);
		GL11U.texture(false);
		
		GL11U.glRotate(x,y,z, biggest/2, FH, 0);
		if(button.getPoint()>0){
			Rectangle boundingBoxClick=getBoundingBoxClick(true);
			OpenGLM.color(1, 0F, 0F, button.getPoint());
			drawRect(1, 1, boundingBoxClick.getMinX(), boundingBoxClick.getMinY(), 0, 0, boundingBoxClick.getWidth(), boundingBoxClick.getHeight());
		}
		OpenGLM.translate(4, 4, 0);
		backgroundColor.a=0.2F+0.3F*backgroundBlue.getPoint();
		backgroundColor.b=backgroundBlue.getPoint();
		GL11U.glColor(backgroundColor);
		//input vertices
		buff.addVertexWithUV(0,				 0,				  0, 0, 0);
		buff.addVertexWithUV(0,				 boundingBox.height, 0, 0, 0);
		buff.addVertexWithUV(boundingBox.width, boundingBox.height, 0, 0, 0);
		buff.addVertexWithUV(boundingBox.width, 0,				  0, 0, 0);
		//do not kill them
		buff.setClearing(false);
		//draw the quad
		buff.draw();
		//k now you can kill them and before you do so turn them into a wire
		buff.setClearing(true);
		buff.setDrawAsWire(true);
		//and color them
		OpenGLM.color(0, 0, 0, 0.4F+backgroundBlue.getPoint()*0.6F);
		//JUST DO IT!!
		buff.draw();
		//dam that was messed up
		buff.setDrawAsWire(false);
		OpenGLM.color(1,1,1,1);
		//reset stuff for font rendering
		GL11U.texture(true);
		//move text get out of the way. Get out of the way text get out of the way.
		
		OpenGLM.translate(2, 2, 0);
		
		//draw text
		for(int i=0;i<text.length;i++){
			if(boundingBox.contains(boundingBox.x+boundingBox.width/2, boundingBox.y+FH*i+4)){
				double maxX=boundingBox.getMaxY();
				Font.FR().drawStringWithShadow(text[i], (biggest-lenghts[i])/2, FH*i, col.set((float)((maxX-(boundingBox.y+FH*(i)+4))/FH), 'a').toCode());
			}
		}
		if(down.point>-0.1){
			OpenGLM.popMatrix();
			OpenGLM.pushMatrix();
			OpenGLM.translate(0, 110, 0);
			GL11U.glScale(0.2);
			GL11U.glRotate(-down.getPoint()*90+90, down.getPoint()*50-50, down.getPoint()*40-40, 85, 200, 0);
			GL11U.glScale(down.getPoint());
			DownloadingIcon.draw(col);
		}
		
		
		GL11U.endOpaqueRendering();
		
		//protect vanilla rendering for my stuff
		GL11U.glDepth(true);
		GL11U.endProtection();
	}
	public void terminateAndOpenUpdater(){
		if(!isDownloading)try{
			File updater=new File(UPDATER_DIR);
			if(updater.exists()){
				//TODO: make this shit!
//				String updaterFolder=UPDATER_DIR.substring(0, UPDATER_DIR.lastIndexOf("/"))+"/";
//				//bridge between the updater app and the mod
//				IOReadableMap config=new IOReadableMap(updaterFolder+"/updaterConfig");
//				config.set("modPos", "mods/"+Magiology.infoFile.getS("modPos",NAME+".jar"));
//				config.set("url", DownloadingHandler.findValue("NEWEST_VERSION_URL"));
//				config.set("appPos", updaterFolder);
//				config.set("isDev", Magiology.isDev());
//				config.writeToFile();
//				Desktop.getDesktop().open(updater);
//				UtilM.exitSoft();
			}
			else DownloadingHandler.downloadUpdater();
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	@Override
	public void update(){
		popup.update();
		rotation.update();
		backgroundBlue.update();
		textTransition.update();
		button.update();
		down.update();
		
		if(RandUtil.RB(0.1)){
			rotation.x.wantedPoint=RandUtil.CRF(20);
			rotation.y.wantedPoint=RandUtil.CRF(20);
			rotation.z.wantedPoint=RandUtil.CRF(1);
		}
		reimplementMouseEvents();
		if(selected){
			timeout++;
			rotation.x.wantedPoint=rotation.y.wantedPoint=rotation.z.wantedPoint=0;
			rotation.x.friction=rotation.y.friction=rotation.z.friction=0.3F;
		}else{
			rotation.x.friction=rotation.y.friction=rotation.z.friction=0.9F;
		}
		button.wantedPoint=U.booleanToInt(isClicked&&getBoundingBoxClick(false).contains(Mouse.getX(),Display.getHeight()-Mouse.getY()));
		
		
		if(timeout>0)timeout--;
		else isClicked=false;
		down.wantedPoint=isDownloading?1:-1;
		textTransition.wantedPoint=U.booleanToInt(selected);
		backgroundBlue.wantedPoint=U.booleanToInt(timeout>0);
		if(isClicked&&Math.abs(rotation.getPoint().x)<0.055){
			rotation.x.point=0;
			rotation.y.point=0;
			rotation.z.point=0;
		}
		if(downloadingInvoked&&!isDownloading){
			downloadingInvoked=false;
			terminateAndOpenUpdater();
		}
		
	}
}
