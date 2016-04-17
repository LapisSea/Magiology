package com.magiology.client.render.itemrender;

//public class ItemRendererPowerCounter implements IItemRenderer {
//	WorldRenderer tess=TessUtil.getWR();
//	FontRenderer fr=TessUtil.getFontRenderer();
//	private final float p= 1F/16F;
//	double anim,powerBar;
//	int maxPB,currentP;
//	String block;
//	
//	
//	@Override
//	public boolean handleRenderType(ItemStack item, ItemRenderType type){return true;}
//
//	@Override
//	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper){return true;}
//
//	@Override
//	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
//		if(item.getTagCompound()!=null){
//			NBTTagCompound PC= item.getTagCompound();
//			anim=PC.getDouble("pAnim")+(PC.getDouble("pAnim")-PC.getDouble("anim"))*RenderEvents.partialTicks;
//			powerBar=PC.getDouble("powerBar");
//			maxPB=PC.getInteger("maxEn");
//			currentP=PC.getInteger("currentEn");
//			block=PC.getString("block");
//		}
//		
//		
//		float x=0;
//		float y=0;
//		float z=0;
//		float xr=0;
//		float yr=0;
//		float zr=0;
//		float scale=1;
//		
//		if(ItemRenderType.ENTITY == type){
////			xr=90;
//			z=-0.4F;
////			y=-1.05F;
//			x=-0.4F;
//			scale=0.9F;
//		}
//		else if(ItemRenderType.EQUIPPED_FIRST_PERSON == type){
//			x=-0.8F;
//			z=0.2F;
//			xr=-5;
//			zr=-5;
//			yr=-45;
//			y=0.8F+(float)anim/2.7F;
//			xr+=5*(float)anim;
//			zr+=5*(float)anim;
//			x+=-1.07F*(float)anim;
//		}
//		else if(ItemRenderType.EQUIPPED == type){
//			scale=1.2F;
//			x=1F;
//			y=-0.2F;
//			z=1F;
//			xr=60;
//			yr=30;
//			zr=-60;
//		}
//		else if(ItemRenderType.INVENTORY == type){
//			y=-1.2F;
//			yr=180;
//			y+=(float)anim*0.6F;
//			yr+=50*(float)anim;
//			z+=1*(float)anim;
//			scale=1.4F;
//		}
//		OpenGLM.pushMatrix();
//		OpenGLM.enableLighting();
//		OpenGLM.enableCull();
//		OpenGLM.translate(x,y,z);
//		OpenGLM.rotate(-xr, 1, 0, 0);OpenGLM.rotate(-yr, 0, 1, 0);OpenGLM.rotate(-zr, 0, 0, 1);
//		OpenGLM.scale(scale, scale, scale);
//		
//		drawCore();
//		OpenGLM.enableLighting();
//		drawText();
//		OpenGLM.enableLighting();
//		OpenGLM.popMatrix();
//	}
//	public void drawText(){
//		float x=p*4-0.0001F;
//		float y=p*9;
//		float z=p*3;
//		float xr=90;
//		float yr=-90;
//		float zr=90;
//		float scale=0.0049F;
//		
//		OpenGLM.translate(x,y,z);
//		OpenGLM.rotate(-xr, 1, 0, 0);OpenGLM.rotate(-yr, 0, 1, 0);OpenGLM.rotate(-zr, 0, 0, 1);
//		OpenGLM.scale(scale, scale, scale);
//		
//		OpenGLM.enableTexture2D();
//		String pauwa=Integer.toString(maxPB)+"/"+Integer.toString(currentP);
//		double Precent=currentP!=maxPB?(powerBar*100>0?powerBar*100:0):100;
//		DecimalFormat df = new DecimalFormat("###.##");
//		String PrecentS=Precent<=0?(currentP>0?"Almost empty":"Empty"):(df.format(Precent)+"%");
//		
//		fr.drawString(pauwa, 0, 0, 11111);
//		fr.drawString(PrecentS, 0, 10, 11111);
//		fr.drawString(block, 0, 20, 11111);
//		
//		OpenGLM.rotate(xr, 1, 0, 0);OpenGLM.rotate(yr, 0, 1, 0);OpenGLM.rotate(zr, 0, 0, 1);
//		OpenGLM.translate(-x, -y, -z);
//	}
//
//	public void drawCore(){
//		TessUtil.bindTexture(Textures.PowerCounterEnergyBar);
//		double var1=powerBar;
//		double var2=p*5+p*4*var1;
//		double var3=1-var1;
//		VertixBuffer buf=TessUtil.getVB();
//		buf.cleanUp();
//		buf.addVertexWithUV(p*4-0.0001, var2, p*11, 0, var3);
//		buf.addVertexWithUV(p*4-0.0001, p*5,  p*11, 0, 1);
//		buf.addVertexWithUV(p*4-0.0001, p*5,  p*13, 1, 1);
//		buf.addVertexWithUV(p*4-0.0001, var2, p*13, 1, var3);
//		buf.draw();
//		double minx=p*4;double miny=p*4;double minz=p*2;
//		double maxx=p*10;double maxy=p*10;double maxz=p*14;
//		
//		
//		TessUtil.bindTexture(Textures.PowerCounterFront);
//		buf.addVertexWithUV(minx, maxy, minz, 0, 0);
//		buf.addVertexWithUV(minx, miny, minz, 0, 1);
//		buf.addVertexWithUV(minx, miny, maxz, 1, 1);
//		buf.addVertexWithUV(minx, maxy, maxz, 1, 0);
//		buf.draw();
//		TessUtil.bindTexture(Textures.PowerCounterSide1);
//		buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
//		buf.addVertexWithUV(maxx, miny,  maxz, 1, 0);
//		buf.addVertexWithUV(maxx, miny,  minz, 0, 0);
//		buf.addVertexWithUV(maxx, maxy, minz, 0, 1);
//		
//		buf.addVertexWithUV(minx, maxy, maxz, 0, 1);
//		buf.addVertexWithUV(minx, miny , maxz, 0, 0);
//		buf.addVertexWithUV(maxx, miny, maxz, 1, 0);
//		buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
//		
//		buf.addVertexWithUV(maxx, maxy, minz, 1, 1);
//		buf.addVertexWithUV(maxx, miny, minz, 1, 0);
//		buf.addVertexWithUV(minx, miny , minz, 0, 0);
//		buf.addVertexWithUV(minx, maxy, minz, 0, 1);
//		
//		buf.addVertexWithUV(maxx, maxy, maxz, 1, 1);
//		buf.addVertexWithUV(maxx, maxy, minz, 1, 0);
//		buf.addVertexWithUV(minx, maxy, minz, 0, 0);
//		buf.addVertexWithUV(minx, maxy, maxz, 0, 1);
//		
//		buf.addVertexWithUV(minx, miny, maxz, 0, 1);
//		buf.addVertexWithUV(minx, miny, minz, 0, 0);
//		buf.addVertexWithUV(maxx, miny, minz, 1, 0);
//		buf.addVertexWithUV(maxx, miny, maxz, 1, 1);
//		buf.draw();
//	}
//	
//}
