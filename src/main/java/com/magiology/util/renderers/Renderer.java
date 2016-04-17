package com.magiology.util.renderers;

import org.lwjgl.opengl.GL11;

import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class Renderer{
	
	public static class LineRenderer extends RendererBase{

		public void addVertex(double xPos, double yPos, double zPos){
			instance.addPos(xPos, yPos, zPos).endVertex();
		}
		public void addVertex(Vec3M pos){
			instance.addPos(pos.x, pos.y, pos.z);
		}
		public void begin(){
			begin(GL11.GL_LINES);
		}
		
		@Override
		@Deprecated
		public void begin(int type){
			super.begin(type);
		}
		
		@Deprecated
		public void beginQuads(){
			begin();
		}
		
		public VertexFormat getVertexFormat(){
			return DefaultVertexFormats.POSITION;
		}
	}
	
	public static class ParticleRenderer extends RendererBase{
		
		public void addVertex(double xPos, double yPos, double zPos, float u, float v, ColorF color, int xLight, int yLight){
			addVertex(xPos, yPos, zPos, u, v, color.r, color.g, color.b, color.a, xLight, yLight);
		}
		
		public void addVertex(double xPos, double yPos, double zPos, float u, float v, float red, float green, float blue, float alpha, int xLight, int yLight){
			instance.addVertexData(xPos, yPos, zPos, u, v).addColor(red, green, blue, alpha).lightmap(xLight, yLight).endVertex();
		}
		public void addVertex(Vec3M pos, float u, float v, ColorF color, int xLight, int yLight){
			addVertex(pos.x, pos.y, pos.z, u, v, color.r, color.g, color.b, color.a, xLight, yLight);
		}
		public void addVertex(Vec3M pos, float u, float v, float red, float green, float blue, float alpha, int xLight, int yLight){
			addVertex(pos.x, pos.y, pos.z, u, v, red, green, blue, alpha, xLight, yLight);
		}
		public VertexFormat getVertexFormat(){
			return DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP;
		}
		
	}
	public static class PosColorRenderer extends RendererBase{
		
		public void addVertex(double xPos, double yPos, double zPos, ColorF color){
			instance.addPos(xPos, yPos, zPos).addColor(color.r, color.g, color.b, color.a).endVertex();
		}
		
		public void addVertex(double xPos, double yPos, double zPos, float r, float g, float b, float a){
			instance.addPos(xPos, yPos, zPos).addColor(r, g, b, a).endVertex();
		}
		public void addVertex(Vec3M pos, ColorF color){
			instance.addPos(pos.x, pos.y, pos.z).addColor(color.r, color.g, color.b, color.a).endVertex();
		}
		public void addVertex(Vec3M pos, float r, float g, float b, float a){
			instance.addPos(pos.x, pos.y, pos.z).addColor(r, g, b, a).endVertex();
		}
		public VertexFormat getVertexFormat(){
			return DefaultVertexFormats.POSITION_TEX_COLOR;
		}
	}
	public static class PosRenderer extends RendererBase{
		
		public void addVertex(double xPos, double yPos, double zPos){
			instance.addPos(xPos, yPos, zPos).endVertex();
		}
		
		public void addVertex(Vec3M pos){
			instance.addPos(pos.x, pos.y, pos.z);
		}
		
		public VertexFormat getVertexFormat(){
			return DefaultVertexFormats.POSITION;
		}
	}
	public static class PosUVColorNormalRenderer extends RendererBase{

		public void addVertex(double xPos, double yPos, double zPos, float u, float v, ColorF color, float xNormal, float yNormal, float zNormal){
			instance.addVertexData(xPos, yPos, zPos, u, v).addColor(color.r, color.g, color.b, color.a).addNormal(xNormal, yNormal, zNormal).endVertex();
		}

		public void addVertex(double xPos, double yPos, double zPos, float u, float v, ColorF color, Vec3M normal){
			instance.addVertexData(xPos, yPos, zPos, u, v).addColor(color.r, color.g, color.b, color.a).addNormal((float)normal.x, (float)normal.y, (float)normal.z).endVertex();
		}
		public void addVertex(double xPos, double yPos, double zPos, float u, float v, float r, float g, float b, float a, float xNormal, float yNormal, float zNormal){
			instance.addVertexData(xPos, yPos, zPos, u, v).addColor(r,g,b,a).addNormal(xNormal, yNormal, zNormal).endVertex();
		}
		public void addVertex(double xPos, double yPos, double zPos, float u, float v, float r, float g, float b, float a, Vec3M normal){
			instance.addVertexData(xPos, yPos, zPos, u, v).addColor(r,g,b,a).addNormal((float)normal.x, (float)normal.y, (float)normal.z).endVertex();
		}
		public void addVertex(Vec3M pos, float u, float v, ColorF color, float xNormal, float yNormal, float zNormal){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v).addColor(color.r, color.g, color.b, color.a).addNormal(xNormal, yNormal, zNormal).endVertex();
		}
		public void addVertex(Vec3M pos, float u, float v, ColorF color, Vec3M normal){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v).addColor(color.r, color.g, color.b, color.a).addNormal((float)normal.x, (float)normal.y, (float)normal.z).endVertex();
		}
		public void addVertex(Vec3M pos, float u, float v, float r, float g, float b, float a, float xNormal, float yNormal, float zNormal){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v).addColor(r,g,b,a).addNormal(xNormal, yNormal, zNormal).endVertex();
		}
		public void addVertex(Vec3M pos, float u, float v, float r, float g, float b, float a, Vec3M normal){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v).addColor(r,g,b,a).addNormal((float)normal.x, (float)normal.y, (float)normal.z).endVertex();
		}
		public VertexFormat getVertexFormat(){
			return DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL;
		}
	}
	public static class PosUVColorRenderer extends RendererBase{
		
		public void addVertex(double xPos, double yPos, double zPos, float u, float v, ColorF color){
			instance.addVertexData(xPos, yPos, zPos, u, v).addColor(color.r, color.g, color.b, color.a).endVertex();
		}
		
		public void addVertex(double xPos, double yPos, double zPos, float u, float v, float r, float g, float b, float a){
			instance.addVertexData(xPos, yPos, zPos, u, v).addColor(r, g, b, a).endVertex();
		}
		
		public void addVertex(Vec3M pos, float u, float v, ColorF color){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v).addColor(color.r, color.g, color.b, color.a).endVertex();
		}
		public void addVertex(Vec3M pos, float u, float v, float r, float g, float b, float a){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v).addColor(r, g, b, a).endVertex();
		}
		
		public VertexFormat getVertexFormat(){
			return DefaultVertexFormats.POSITION_TEX_COLOR;
		}
	}
	public static class PosUVNormalRenderer extends RendererBase{
		
		public void addVertex(double xPos, double yPos, double zPos, float u, float v, float xNormal, float yNormal, float zNormal){
			instance.addVertexData(xPos, yPos, zPos, u, v, xNormal, yNormal, zNormal);
		}
		
		public void addVertex(double xPos, double yPos, double zPos, float u, float v, Vec3M normal){
			instance.addVertexData(xPos, yPos, zPos, u, v, (float)normal.x, (float)normal.y, (float)normal.z);
		}
		public void addVertex(Vec3M pos, float u, float v, float xNormal, float yNormal, float zNormal){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v, xNormal, yNormal, zNormal);
		}
		public void addVertex(Vec3M pos, float u, float v, Vec3M normal){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v, (float)normal.x, (float)normal.y, (float)normal.z);
		}
		
		public VertexFormat getVertexFormat(){
			return DefaultVertexFormats.POSITION_TEX_NORMAL;
		}
	}
	public static class PosUVRenderer extends RendererBase{
		
		public void addVertex(double xPos, double yPos, double zPos, float u, float v){
			instance.addVertexData(xPos, yPos, zPos, u, v).endVertex();
		}
		
		public void addVertex(Vec3M pos, float u, float v){
			instance.addVertexData(pos.x, pos.y, pos.z, u, v).endVertex();
		}
		
		public VertexFormat getVertexFormat(){
			return DefaultVertexFormats.POSITION_TEX_NORMAL;
		}
	}
	public static abstract class RendererBase{
		public static Renderer setTranslation(double x, double y, double z){
			renderer.setTranslation(x, y, z);
			return instance;
		}
		
		private RendererBase(){}
		
		
		
		public void begin(int type){
			renderer.begin(type, getVertexFormat());
		}
		public void beginQuads(){
			renderer.begin(7, getVertexFormat());
		}
		
		public final void draw(){
			tessellator.draw();
		}
		
		public abstract VertexFormat getVertexFormat();
		
	}

	
	private static final Renderer instance=new Renderer();
	public static final LineRenderer LINES=new LineRenderer();
	
	public static final ParticleRenderer PARTICLE=new ParticleRenderer();
	
	
	
	public static final PosRenderer POS=new PosRenderer();
	public static final PosColorRenderer POS_COLOR=new PosColorRenderer();
	
	
	public static final PosUVRenderer POS_UV=new PosUVRenderer();
	public static final PosUVColorRenderer POS_UV_COLOR=new PosUVColorRenderer();
	public static final PosUVColorNormalRenderer POS_UV_COLOR_NORMAL=new PosUVColorNormalRenderer();
	
	public static final PosUVNormalRenderer POS_UV_NORMAL=new PosUVNormalRenderer();
	
	private static WorldRenderer renderer=TessUtil.getWR();

	private static Tessellator tessellator=TessUtil.getT();
	
	private Renderer(){}
	
	private Renderer addColor(float r, float g, float b, float a){
		renderer.color(MathUtil.snap(r,0,1), MathUtil.snap(g,0,1), MathUtil.snap(b,0,1), MathUtil.snap(a,0,1));
		return instance;
	}
	private Renderer addNormal(float x, float y, float z){
		renderer.normal(x, y, z);
		return instance;
	}
	
	
	private Renderer addPos(double xPos,double yPos,double zPos){
		renderer.pos(xPos, yPos, zPos);
		return instance;
	}
	private Renderer addUV(double u,double v){
		renderer.tex(u, v);
		return instance;
	}
	private Renderer addVertexData(double xPos,double yPos,double zPos,double u,double v){
		addPos(xPos, yPos, zPos).addUV(u, v);
		return instance;
	}
	private void addVertexData(double xPos,double yPos,double zPos,double u,double v,float xNormal,float yNormal,float zNormal){
		addPos(xPos, yPos, zPos).addUV(u, v).addNormal(xNormal, yNormal, zNormal).endVertex();
	}
	private void endVertex(){
		renderer.endVertex();
	}
	
	
	private Renderer lightmap(int j, int k){
		renderer.lightmap(j, k);
		return instance;
	}
}
