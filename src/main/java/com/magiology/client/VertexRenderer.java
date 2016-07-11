package com.magiology.client;


import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.client.Renderer.RendererBase;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.objs.physics.real.GeometryUtil;
import com.magiology.util.statics.PrintUtil;
import com.magiology.util.statics.UtilM.U;
import com.magiology.util.statics.math.MatrixUtil;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VertexRenderer{
	
	public static class ShadedQuad{
		public Vec3M normal1=null,normal2=null;
		public PositionTextureVertex[] pos4=null;
		public ShadedQuad(PositionTextureVertex pos1,PositionTextureVertex pos2,PositionTextureVertex pos3, PositionTextureVertex pos4, Vec3M normal1, Vec3M normal2) {
			this.pos4=new PositionTextureVertex[]{pos1,pos2,pos3,pos4};
			this.normal1=normal1;
			this.normal2=normal2;
		}
		public void recalculateNormal(){
			Vec3d vec3 =this.pos4[1].vector3D.subtractReverse(this.pos4[0].vector3D);
			Vec3d vec31=this.pos4[1].vector3D.subtractReverse(this.pos4[2].vector3D);
			normal1 = Vec3M.conv(vec31.crossProduct(vec3).normalize());
		}
	}
	protected final static float NULL_UV_ID=123456789;
	protected boolean instantNormalCalculation=true,willDrawAsAWireFrame=false,willClear=true;
	private RendererBase renderer;
	
	protected Vector3f rotation=new Vector3f();
	
	protected List<Vector3f> rotationStacks=new ArrayList<>();
	
	protected List<ShadedQuad> shadedTriangles=new ArrayList<>();
	protected List<VertexRenderer> subBuffers=new ArrayList<>();
	
	protected Matrix4f transformation=new Matrix4f();
	protected List<Matrix4f> transformationStacks=new ArrayList<>();
	
	protected VertexRenderer transformedBuffer;
	protected List<PositionTextureVertex> unfinishedTriangle=new ArrayList<>();
	
	public VertexRenderer(){
		this(true);
	}
	
	public VertexRenderer(boolean hasTransformedBuffer){
		if(hasTransformedBuffer)transformedBuffer=new VertexRenderer(false);
	}
	public void addVertex(double x,double y,double z){
		addVertexWithUV(x,y,z, NULL_UV_ID, NULL_UV_ID);
	}
	public void addVertexWithUV(double x,double y,double z,double u,double v){
		addVertexWithUV((float)x, (float)y, (float)z, (float)u, (float)v);
	}
	private void addVertexWithUV(float x,float y,float z,float u,float v){
		unfinishedTriangle.add(new PositionTextureVertex(x,y,z, u, v));
		if(unfinishedTriangle.size()==4)process();
	}
	public void addVertexWithUV(Vec3M vec3m, double u, double v){
		addVertexWithUV((float)vec3m.x, (float)vec3m.y, (float)vec3m.z, (float)u, (float)v);
	}
	
	public void cleanUp(){
		shadedTriangles.clear();
		unfinishedTriangle.clear();
		if(transformedBuffer!=null)transformedBuffer.cleanUp();
	}
	public VertexRenderer createNewSubBuffer(){
		VertexRenderer result=new VertexRenderer();
		subBuffers.add(result);
		result.instantNormalCalculation=instantNormalCalculation;
		result.willDrawAsAWireFrame=willDrawAsAWireFrame;
		result.willClear=willClear;
		return result;
	}
	
	public void draw(){
		if(getRenderer()==null){
			if(willDrawAsAWireFrame){
//				GL11U.texture(false);
//				Renderer.POS.begin(GL11.GL_LINES);
//				pasteToTesselator(false);
//				renderToScreen();
//				GL11U.texture(true);
			}else{
				Renderer.POS_UV_NORMAL.begin(GL11.GL_TRIANGLES);
				pasteToTesselator(true);
				renderToScreen();
			}
		}else{
			getRenderer().beginQuads();
			pasteToTesselator(true);
			renderToScreen();
		}
	}
	

	private ShadedQuad generateShadedQuad(PositionTextureVertex pos0,PositionTextureVertex pos1,PositionTextureVertex pos2,PositionTextureVertex pos3,boolean hasNormal){
		Vec3M normal1,normal2;
		if(hasNormal&&instantNormalCalculation){
			normal1=Vec3M.conv(GeometryUtil.getNormal(pos0.vector3D, pos1.vector3D, pos2.vector3D));
			normal2=Vec3M.conv(GeometryUtil.getNormal(pos2.vector3D, pos3.vector3D, pos0.vector3D));
		}
		else {
			normal1=normal2=new Vec3M(0, 1, 0);
		}
		return new ShadedQuad(pos0,pos1,pos2,pos3, normal1,normal2);
	}

	public boolean getDrawAsWire(){
		return willDrawAsAWireFrame;
	}

	public RendererBase getRenderer(){
		return renderer;
	}
	public Vector3f getRotation(){
		return Vector3f.add(rotation, new Vector3f(), null);
	}
	
	public Matrix4f getTransformation(){
		return Matrix4f.add(transformation, Matrix4f.setZero(new Matrix4f()), null);
	}
	
	public ShadedQuad getTriangle(int id){
		return shadedTriangles.get(id);
	}
	
	public List<ShadedQuad> getTriangles(){
		return shadedTriangles;
	}
	public void pasteToTesselator(boolean type){

		for(VertexRenderer a:subBuffers){
			for(ShadedQuad b:a.shadedTriangles){
				MatrixUtil.transformVector(b.normal1, new Vector3f(),a.rotation.x,a.rotation.y,a.rotation.z,1);
				for(int b1=0;b1<4;b1++)b.pos4[b1].vector3D=MatrixUtil.transformVector(b.pos4[b1].vector3D, a.transformation);
			}
			shadedTriangles.addAll(a.shadedTriangles);
		}
		subBuffers.clear();
		if(transformedBuffer!=null)transformedBuffer.pasteToTesselator(type);
		try {
			for(ShadedQuad a:shadedTriangles){
				if(type){
					tesselateQuads(a);
				}else{
					tesselateLines(a);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void popMatrix(){
		if(transformationStacks.isEmpty()){
			PrintUtil.println("Buffer is out of stacks to pop! You need to push before popping!\nFunction aborted.",U.getStackTrace());
			return;
		}
		int pos=transformationStacks.size()-1;
		transformation=transformationStacks.get(pos);
		rotation=rotationStacks.get(pos);
		transformationStacks.remove(pos);
		rotationStacks.remove(pos);
	}
	private void process(){
		shadedTriangles.add(generateShadedQuad(unfinishedTriangle.get(0), unfinishedTriangle.get(1), unfinishedTriangle.get(2), unfinishedTriangle.get(3),true));
		unfinishedTriangle.clear();
	}
	public void pushMatrix(){
		transformationStacks.add(Matrix4f.add(transformation, Matrix4f.setZero(new Matrix4f()), null));
		rotationStacks.add(new Vector3f(rotation.x, rotation.y, rotation.z));
	}
	
	public void recalculateNormals(){
		shadedTriangles.forEach(ShadedQuad::recalculateNormal);
	}
	public void renderToScreen(){
		Renderer.POS.draw();
		if(willClear)cleanUp();
	}
	public void resetTransformation(){
		transformation=new Matrix4f();
		rotation=new Vector3f();
		
		transformationStacks=new ArrayList<>();
		rotationStacks=new ArrayList<>();
	}
	public void rotate(double x,double y,double z){
		if(x==0&&y==0&&z==0)return;
		
		Vector3f.add(rotation, new Vector3f((float)x, (float)y, (float)z), rotation);
		if(x!=0)Matrix4f.rotate((float)Math.toRadians(x), new Vector3f(1, 0, 0), transformation, transformation);
		if(y!=0)Matrix4f.rotate((float)Math.toRadians(y), new Vector3f(0, 1, 0), transformation, transformation);
		if(z!=0)Matrix4f.rotate((float)Math.toRadians(z), new Vector3f(0, 0, 1), transformation, transformation);
	}
	public void rotateAt(double x,double y,double z,double rotX,double rotY,double rotZ){
		if(rotX==0&&rotY==0&&rotZ==0)return;
		translate(x,y,z);
		rotate(rotX, rotY, rotZ);
		translate(-x,-y,-z);
	}
	public void scale(double scale){
		if(scale==1)return;
		transformation.scale(new Vector3f((float)scale, (float)scale, (float)scale));
	}
	public void setClearing(boolean enabled){
		willClear=enabled;
		if(transformedBuffer!=null)transformedBuffer.setClearing(enabled);
	}
	public void setDrawAsWire(boolean wireMode){
		willDrawAsAWireFrame=wireMode;
		if(transformedBuffer!=null)transformedBuffer.setDrawAsWire(wireMode);
	}
	public void setInstantNormalCalculation(boolean enabled){
		instantNormalCalculation=enabled;
		if(transformedBuffer!=null)transformedBuffer.setInstantNormalCalculation(enabled);
	}
	public void setRenderer(RendererBase format){
		renderer=format;
	}
	
	protected void tesselateLines(ShadedQuad triangle){
		Vec3M finalVectors[]={
				MatrixUtil.transformVector(Vec3M.conv(triangle.pos4[0].vector3D), transformation),
				MatrixUtil.transformVector(Vec3M.conv(triangle.pos4[1].vector3D), transformation),
				MatrixUtil.transformVector(Vec3M.conv(triangle.pos4[2].vector3D), transformation),
				MatrixUtil.transformVector(Vec3M.conv(triangle.pos4[3].vector3D), transformation)
		};
		Renderer.POS.addVertex(finalVectors[0].x, finalVectors[0].y, finalVectors[0].z);
		Renderer.POS.addVertex(finalVectors[1].x, finalVectors[1].y, finalVectors[1].z);

		Renderer.POS.addVertex(finalVectors[1].x, finalVectors[1].y, finalVectors[1].z);
		Renderer.POS.addVertex(finalVectors[2].x, finalVectors[2].y, finalVectors[2].z);
		
		Renderer.POS.addVertex(finalVectors[2].x, finalVectors[2].y, finalVectors[2].z);
		Renderer.POS.addVertex(finalVectors[3].x, finalVectors[3].y, finalVectors[3].z);
		
		Renderer.POS.addVertex(finalVectors[3].x, finalVectors[3].y, finalVectors[3].z);
		Renderer.POS.addVertex(finalVectors[0].x, finalVectors[0].y, finalVectors[0].z);
	}
	protected void tesselateQuads(ShadedQuad triangle){
		Vec3M finalNormal1=MatrixUtil.transformVector(triangle.normal1.add(0,0,0), new Vector3f(),rotation.x,rotation.y,rotation.z,1).normalize();
		Vec3M finalNormal2=MatrixUtil.transformVector(triangle.normal2.add(0,0,0), new Vector3f(),rotation.x,rotation.y,rotation.z,1).normalize();
		Vec3M[] pos=new Vec3M[4];
		for(int b=0;b<4;b++)pos[b]=MatrixUtil.transformVector(new Vec3M(triangle.pos4[b].vector3D.xCoord, triangle.pos4[b].vector3D.yCoord, triangle.pos4[b].vector3D.zCoord), transformation);
		Renderer.POS_UV_NORMAL.addVertex(pos[0], triangle.pos4[0].texturePositionX, triangle.pos4[0].texturePositionY,finalNormal1);
		Renderer.POS_UV_NORMAL.addVertex(pos[1], triangle.pos4[1].texturePositionX, triangle.pos4[1].texturePositionY,finalNormal1);
		Renderer.POS_UV_NORMAL.addVertex(pos[2], triangle.pos4[2].texturePositionX, triangle.pos4[2].texturePositionY,finalNormal1);
		
		Renderer.POS_UV_NORMAL.addVertex(pos[2], triangle.pos4[2].texturePositionX, triangle.pos4[2].texturePositionY,finalNormal2);
		Renderer.POS_UV_NORMAL.addVertex(pos[3], triangle.pos4[3].texturePositionX, triangle.pos4[3].texturePositionY,finalNormal2);
		Renderer.POS_UV_NORMAL.addVertex(pos[0], triangle.pos4[0].texturePositionX, triangle.pos4[0].texturePositionY,finalNormal2);
	}
	public void transform(double x,double y,double z,double rotX,double rotY,double rotZ, double scale){
		Vector3f.add(rotation, new Vector3f((float)rotX, (float)rotY, (float)rotZ), rotation);
		Matrix4f.mul(transformation, MatrixUtil.createMatrix(new Vector3f((float)x, (float)y, (float)z), (float)rotX, (float)rotY, (float)rotZ, (float)scale).finish(), transformation);
	}
	
	public void transformAndSaveTo(VertexRenderer buff){
		try{
			boolean isItself=buff==null||buff==this;
			List<ShadedQuad> buffer=isItself?new ArrayList<>():buff.shadedTriangles;
			for(ShadedQuad a:shadedTriangles){
				Vec3M finalNormal1=MatrixUtil.transformVector(a.normal1.add(0, 0, 0), new Vector3f(0, 0, 0), rotation.x, rotation.y, rotation.z, 1);
				Vec3M finalNormal2=MatrixUtil.transformVector(a.normal2.add(0, 0, 0), new Vector3f(0, 0, 0), rotation.x, rotation.y, rotation.z, 1);
				buffer.add(new ShadedQuad(
						new PositionTextureVertex(MatrixUtil.transformVector(new Vec3d(a.pos4[0].vector3D.xCoord, a.pos4[0].vector3D.yCoord, a.pos4[0].vector3D.zCoord), transformation), a.pos4[0].texturePositionX, a.pos4[0].texturePositionY),
						new PositionTextureVertex(MatrixUtil.transformVector(new Vec3d(a.pos4[1].vector3D.xCoord, a.pos4[1].vector3D.yCoord, a.pos4[1].vector3D.zCoord), transformation), a.pos4[1].texturePositionX, a.pos4[1].texturePositionY),
						new PositionTextureVertex(MatrixUtil.transformVector(new Vec3d(a.pos4[2].vector3D.xCoord, a.pos4[2].vector3D.yCoord, a.pos4[2].vector3D.zCoord), transformation), a.pos4[2].texturePositionX, a.pos4[2].texturePositionY),
						new PositionTextureVertex(MatrixUtil.transformVector(new Vec3d(a.pos4[3].vector3D.xCoord, a.pos4[3].vector3D.yCoord, a.pos4[3].vector3D.zCoord), transformation), a.pos4[3].texturePositionX, a.pos4[3].texturePositionY),
						finalNormal1,finalNormal2
				));
			}
			if(isItself){
				shadedTriangles=buffer;
			}else{
				if(willClear)shadedTriangles.clear();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void translate(double x,double y,double z){
		if(x==0&&y==0&&z==0)return;
		transformation.translate(new Vector3f((float)x, (float)y, (float)z));
	}
	public void unbindOverrideFormat(){
		renderer=null;
	}
}
