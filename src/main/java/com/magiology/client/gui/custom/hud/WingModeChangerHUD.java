package com.magiology.client.gui.custom.hud;

import java.awt.Color;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.magiology.forgepowered.packets.packets.generic.GenericServerIntPacket;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler.Positions;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class WingModeChangerHUD extends HUD{
	public static WingModeChangerHUD instance=new WingModeChangerHUD();
	float[][] backgroundColor,prevBackgroundColor;
	double[][] criclePoss={MathUtil.circleXZ(0),MathUtil.circleXZ(60),MathUtil.circleXZ(120),MathUtil.circleXZ(180),MathUtil.circleXZ(240),MathUtil.circleXZ(300),MathUtil.circleXZ(0)};
	Positions curentPoss;
	private ComplexPlayerRenderingData data;
	private ExtendedPlayerData extendedData;
	FontRenderer fr;
	private boolean isExited=true;
	private int selectionId;
	private float sliderPos,prevSliderPos,sliderSpeed,sliderWantedPos,alpha,prevAlpha;
	Positions[] validPoss;
	int width=0;
	private WingModeChangerHUD(){}
	public void next(){
		if(!isExited)sliderWantedPos-=width/2+fr.FONT_HEIGHT;
	}
	private void onExit(){
		if(WingsFromTheBlackFireHandler.getIsActive(player))UtilM.sendMessage(new GenericServerIntPacket(6, validPoss[selectionId].id));
	}
	private void onOpen(){}
	public void prev(){
		if(!isExited)sliderWantedPos+=width/2+fr.FONT_HEIGHT;
	}
	@Override
	public void render(int xScreen, int yScreen, float partialTicks){
		Positions[] poss=Positions.values();
		//loading up
		if(UtilM.isNull(extendedData,data,fr,validPoss,UtilM.getTheWorld(),backgroundColor)){
			if(extendedData==null)extendedData=ExtendedPlayerData.get(player);
			if(data==null)data=ComplexPlayerRenderingData.get(player);
			fr=TessUtil.getFontRenderer();
			if(validPoss==null)for(int a=0;a<poss.length;a++)if(poss[a].wanted&&poss[a]!=Positions.FlyBackvardPos&&poss[a]!=Positions.FlyForvardPos)validPoss=ArrayUtils.add(validPoss, poss[a]);
			backgroundColor=new float[validPoss.length][3];
			prevBackgroundColor=backgroundColor.clone();
			return;
		}if(player==null)return;
		if(data.player!=player){
			extendedData=ExtendedPlayerData.get(player);
		}
		if(extendedData==null)return;
		for(Positions poz:poss)width=Math.max(width, fr.getStringWidth(poz.name()));
		//end
		player=UtilM.getThePlayer();
		float calcAlpha=PartialTicksUtil.calculatePos(prevAlpha, alpha);
		GL11U.setUpOpaqueRendering(1);
		if(WingsFromTheBlackFireHandler.getIsActive(player)){
			String poz=WingsFromTheBlackFireHandler.getPos(player)+"/"+Positions.get(WingsFromTheBlackFireHandler.getPosId(player));
			OpenGLM.pushMatrix();
			OpenGLM.translate(xScreen-fr.getStringWidth(poz)*0.7, yScreen-fr.FONT_HEIGHT*0.7,0);
			GL11U.glScale(0.7);
			Color c=new Color(255,255,255,(int)(255*MathUtil.snap(calcAlpha+0.25, 0, 1)));
			fr.drawStringWithShadow(poz, -1,-1, c.hashCode());
			OpenGLM.popMatrix();
		}
		
		if(calcAlpha<0.01){
			GL11U.endOpaqueRendering();
			return;
		}
		double slide=PartialTicksUtil.calculatePos(prevSliderPos, sliderPos);
		
		float offset=calcAlpha*10-10;
		float reducedScale=1,clipping=275-yScreen;
		if(clipping>0){
			clipping=(float)((clipping*4.5)/Math.sqrt(clipping));
			reducedScale-=clipping/200;
		}
		OpenGLM.translate(xScreen-width/2, -(validPoss.length)*(width/4), 0);
		GL11U.glRotate(offset*6,-offset*9, 0);
		OpenGLM.translate(-offset, -slide, 0);
		if(clipping>0){
			GL11U.glScale(reducedScale);
			OpenGLM.translate(0, clipping/2F, 0);
		}
		renderSlider();
		GL11U.endOpaqueRendering();
	}
	private void renderSlider(){
		float calcAlpha=PartialTicksUtil.calculatePos(prevAlpha, alpha),mainAlpha=255*calcAlpha;
		if(calcAlpha<=0.01)return;
		int id=0;
		int nextLineOffset=width/2+fr.FONT_HEIGHT/2;
		
		float[][] calcBackgroundColor=backgroundColor.clone();
		if(player.fallDistance>4)for(int a=0;a<criclePoss.length;a++)for(int b=0;b<2;b++){
			criclePoss[a][b]+=RandUtil.CRF(0.01);
		}
		for(int a=0;a<calcBackgroundColor.length;a++)for(int b=0;b<3;b++){
			calcBackgroundColor[a][b]=PartialTicksUtil.calculatePos(backgroundColor[a][b], prevBackgroundColor[a][b]);
		}
		for(int nj=0;nj<3;nj++)for(int pozId=0;pozId<validPoss.length;pozId++){
			Positions poz=validPoss[pozId];
			int var=Math.abs((id-validPoss.length)-selectionId);
			Color color=new Color(255,255,255,(int)(mainAlpha*(var==0?1:var==1?0.5:var==2?0.2:0.025)));
			if(color.getAlpha()>6){
				if(nj!=1)color=new Color(255,155,155,(int)(mainAlpha*(var==0?1:var==1?0.5:var==2?0.2:0.025)));
				if(var==0)color=new Color(125,125,255);
				
				OpenGLM.disableTexture2D();
				OpenGLM.color(
				nj==1?calcBackgroundColor[pozId][0]:1,nj==1?calcBackgroundColor[pozId][1]:0.7,nj==1?calcBackgroundColor[pozId][2]:0.7,0.4*calcAlpha*((color.getAlpha())/255F));
				Renderer.POS.begin(GL11.GL_TRIANGLES);
				for(int l=0;l<criclePoss.length-1;l++){
					Renderer.POS.addVertex(0, 0, 0);
					Renderer.POS.addVertex(criclePoss[l][0]*nextLineOffset, criclePoss[l][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
					Renderer.POS.addVertex(criclePoss[l+1][0]*nextLineOffset, criclePoss[l+1][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
				}
				Renderer.POS.draw();
				OpenGLM.color(0.8,0.8,0.8,calcAlpha*((color.getAlpha())/255F));
				OpenGLM.lineWidth(1.5F);
				Renderer.LINES.begin();
				for(int l=0;l<criclePoss.length-1;l++){
					Renderer.LINES.addVertex(criclePoss[l][0]*nextLineOffset, criclePoss[l][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
					Renderer.LINES.addVertex(criclePoss[l+1][0]*nextLineOffset, criclePoss[l+1][1]*nextLineOffset/2+fr.FONT_HEIGHT/2, 0);
				}
				Renderer.LINES.draw();
				OpenGLM.enableTexture2D();
				
				String waring=player.motionY<-0.2&&(poz==Positions.NormalPos||poz==Positions.ProtectivePos)?"!! ":"";
				OpenGLM.pushMatrix();
				OpenGLM.translate(-fr.getStringWidth(poz.name())/2-fr.getStringWidth(waring), 0, 0);
				fr.drawStringWithShadow(waring+poz.name(), 0,0, color.hashCode());
				OpenGLM.popMatrix();
			}
			OpenGLM.translate(0, nextLineOffset+2, 0);
			id++;
		}
		if(player.fallDistance>4)for(int a=0;a<criclePoss.length;a++)for(int b=0;b<2;b++){
			criclePoss=new double[][]{MathUtil.circleXZ(0),MathUtil.circleXZ(60),MathUtil.circleXZ(120),MathUtil.circleXZ(180),MathUtil.circleXZ(240),MathUtil.circleXZ(300),MathUtil.circleXZ(0)};
		}
	}
	@Override
	public void update(){
		if(UtilM.isNull(extendedData,data,player,validPoss,UtilM.getTheWorld()))return;
		int nextLineOffset=width/2+fr.FONT_HEIGHT;
		prevBackgroundColor=backgroundColor.clone();
		prevSliderPos=sliderPos;
		prevAlpha=alpha;
		sliderSpeed=UtilM.handleSpeedFolower(sliderSpeed,sliderPos,sliderWantedPos,15F);
		sliderSpeed*=0.7;
		double multiplayer=Math.abs((sliderPos-sliderWantedPos)/nextLineOffset);
		multiplayer=MathUtil.snap(multiplayer, 0, 1);
		sliderSpeed*=multiplayer;
		sliderPos+=sliderSpeed;
		int perPos=((int)(sliderWantedPos/nextLineOffset));
		if(perPos>validPoss.length-1)sliderWantedPos=0;
		if(perPos<0)sliderWantedPos=(validPoss.length-1)*nextLineOffset;
		selectionId=(int)((sliderPos+nextLineOffset/2)/nextLineOffset);
		selectionId=MathUtil.snap(selectionId, 0, 4);
		curentPoss=Positions.values()[validPoss[selectionId].id];
		alpha+=0.2F*(isExited?-1:1);
		double noise=0.05,speed=0.15;
		for(int a=0;a<backgroundColor.length;a++){
			if(a==selectionId){
				backgroundColor[a][0]=(float)UtilM.slowlyEqualize(backgroundColor[a][0], 1+RandUtil.CRF(noise),speed);
				backgroundColor[a][1]=(float)UtilM.slowlyEqualize(backgroundColor[a][1], 0.2+RandUtil.CRF(noise),speed);
				backgroundColor[a][2]=(float)UtilM.slowlyEqualize(backgroundColor[a][2], 0.2+RandUtil.CRF(noise),speed);
			}else{
				backgroundColor[a][0]=(float)UtilM.slowlyEqualize(backgroundColor[a][0], 0.2+RandUtil.CRF(noise),speed);
				backgroundColor[a][1]=(float)UtilM.slowlyEqualize(backgroundColor[a][1], 0.2+RandUtil.CRF(noise),speed);
				backgroundColor[a][2]=(float)UtilM.slowlyEqualize(backgroundColor[a][2], 0.2+RandUtil.CRF(noise),speed);
			}
			
			backgroundColor[a][0]=MathUtil.snap(backgroundColor[a][0], 0, 1);
			backgroundColor[a][1]=MathUtil.snap(backgroundColor[a][1], 0, 1);
			backgroundColor[a][2]=MathUtil.snap(backgroundColor[a][2], 0, 1);
		}
		alpha=MathUtil.snap(alpha, 0F, 1);
		boolean prevIsExited=isExited;
		isExited=!GuiScreen.isCtrlKeyDown()||!WingsFromTheBlackFireHandler.getIsActive(player);
		if(prevIsExited!=isExited){
			if(isExited)onExit();
			else onOpen();
		}
	}
}
