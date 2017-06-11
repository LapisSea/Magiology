package com.magiology;

import com.magiology.client.renderers.FastNormalRenderer;
import com.magiology.client.renderers.Renderer;
import com.magiology.client.rendering.ShaderMultiTransformModel;
import com.magiology.util.objs.animation.AnimationM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.TestingAnimationM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.MathUtil;
import com.magiology.util.statics.math.MatrixUtil;
import com.magiology.util.statics.math.PartialTicksUtil;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.util.vector.Matrix4f;

/**
 *
 * this is a class where my brain makes proofs of concepts, heavy debugging and things like that... Also farts, but don't mind that.
 *
 * @author LapisSea
 *
 */
public class Development{
	
	public static ShaderMultiTransformModel model=new ShaderMultiTransformModel(){
		
		@Override
		public int[] getMatrixIds(){
			return new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,1,1,1,1,0,0,2,2,1,1,1,1,2,2,1,1,2,2,2,2,1,1,2,2,2,2};
		}
		
		@Override
		public Matrix4f[] getMatrices(){
			float tim=(float)((UtilC.getWorldTime()+(double)PartialTicksUtil.partialTicks)%3600)*2;
			Matrix4f mat1=MatrixUtil.createMatrix(new Vec3M()).rotateAt(new Vec3M(0.5, 0.5, 0.5), new Vec3M(MathUtil.sin(tim)*15, 0, 0)).finish();
			Matrix4f mat2=Matrix4f.mul(mat1, MatrixUtil.createMatrix(new Vec3M()).rotateAt(new Vec3M(0.5, 0.5, 0.5), new Vec3M(MathUtil.sin(tim)*15, 0, 0)).finish(), null);
			Matrix4f mat3=Matrix4f.mul(mat2, MatrixUtil.createMatrix(new Vec3M()).rotateAt(new Vec3M(0.5, 0.5, 0.5), new Vec3M(MathUtil.sin(tim)*15, 0, 0)).finish(), null);
			return new Matrix4f[]{mat1,mat2,mat3};
		}
		
		@Override
		public int generateModel(){
			int id=GLAllocation.generateDisplayLists(1);
			GlStateManager.glNewList(id, 4864);
			
			float p=5/16F;
			FastNormalRenderer buf=new FastNormalRenderer();
			buf.begin(true, buf.POS_UV);
			buf.add(0, p, 0, 0, 0);
			buf.add(p, p, 0, 1, 0);
			buf.add(p, 0, 0, 1, 1);
			buf.add(0, 0, 0, 0, 1);
			
			buf.add(0, 0, p, 0, 1);
			buf.add(p, 0, p, 1, 1);
			buf.add(p, p, p, 1, 0);
			buf.add(0, p, p, 0, 0);
			
			buf.add(0, 0, 0, 0, 1);
			buf.add(0, 0, p, 1, 1);
			buf.add(0, p, p, 1, 0);
			buf.add(0, p, 0, 0, 0);
			
			buf.add(p, p, 0, 0, 0);
			buf.add(p, p, p, 1, 0);
			buf.add(p, 0, p, 1, 1);
			buf.add(p, 0, 0, 0, 1);
			
			buf.add(0, 0, 0, 0, 0);
			buf.add(p, 0, 0, 1, 0);
			buf.add(p, 0, p, 1, 1);
			buf.add(0, 0, p, 0, 1);
			
			buf.add(0, p+p, 0, 0, 0);
			buf.add(p, p+p, 0, 1, 0);
			buf.add(p, 0+p, 0, 1, 1);
			buf.add(0, 0+p, 0, 0, 1);
			
			buf.add(0, 0+p, p, 0, 1);
			buf.add(p, 0+p, p, 1, 1);
			buf.add(p, p+p, p, 1, 0);
			buf.add(0, p+p, p, 0, 0);
			
			buf.add(0, 0+p, 0, 0, 1);
			buf.add(0, 0+p, p, 1, 1);
			buf.add(0, p+p, p, 1, 0);
			buf.add(0, p+p, 0, 0, 0);
			
			buf.add(p, p+p, 0, 0, 0);
			buf.add(p, p+p, p, 1, 0);
			buf.add(p, 0+p, p, 1, 1);
			buf.add(p, 0+p, 0, 0, 1);
			
			buf.add(0, p+p*2, 0, 0, 0);
			buf.add(p, p+p*2, 0, 1, 0);
			buf.add(p, 0+p*2, 0, 1, 1);
			buf.add(0, 0+p*2, 0, 0, 1);
			
			buf.add(0, 0+p*2, p, 0, 1);
			buf.add(p, 0+p*2, p, 1, 1);
			buf.add(p, p+p*2, p, 1, 0);
			buf.add(0, p+p*2, p, 0, 0);
			
			buf.add(0, 0+p*2, 0, 0, 1);
			buf.add(0, 0+p*2, p, 1, 1);
			buf.add(0, p+p*2, p, 1, 0);
			buf.add(0, p+p*2, 0, 0, 0);
			
			buf.add(p, p+p*2, 0, 0, 0);
			buf.add(p, p+p*2, p, 1, 0);
			buf.add(p, 0+p*2, p, 1, 1);
			buf.add(p, 0+p*2, 0, 0, 1);
			
			buf.add(0, p*3, p, 0, 1);
			buf.add(p, p*3, p, 1, 1);
			buf.add(p, p*3, 0, 1, 0);
			buf.add(0, p*3, 0, 0, 0);
			buf.draw();
			
			GlStateManager.glEndList();
			return id;
		}
		
		@Override
		public int getModelListId(){
			if(modelId==-1) modelId=generateModel();
			return modelId;
		}
	};
	
	public static void rendnerRandomAnimation(){
		AnimationM anim=TestingAnimationM.get();
		
		float precent=(float)(UtilC.getWorldTime()%50D+PartialTicksUtil.partialTicks)/50;
		float a=-anim.get(anim.getIdName("alpha"), precent);
		float trans=anim.get(anim.getIdName("translate"), precent);
		float rot=-anim.get(anim.getIdName("explode"), precent);
		OpenGLM.pushMatrix();
		OpenGLM.translate(100, 0, 0);
		OpenGLM.disableTexture2D();
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(0, 10, 0);
		Renderer.LINES.addVertex(0, 10-10*a, 0);
		
		Renderer.LINES.addVertex(3, 10, 0);
		Renderer.LINES.addVertex(3, 10+10*trans, 0);
		
		Renderer.LINES.addVertex(6, 10, 0);
		Renderer.LINES.addVertex(6, 10-10*rot, 0);
		Renderer.LINES.draw();
		
		OpenGLM.setUpOpaqueRendering(BlendFunc.NORMAL);
		
		OpenGLM.translate((1-trans)*50, 50, 0);
		OpenGLM.rotateZ(rot*10);
		
		ColorM.WHITE.bindWithA(MathUtil.snap(a/15-rot/2, 0, 1));
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex(-10, 10, 0);
		Renderer.POS.addVertex(10, 10, 0);
		Renderer.POS.addVertex(10, -10, 0);
		Renderer.POS.addVertex(-10, -10, 0);
		Renderer.POS.draw();
		
		ColorM.WHITE.bindWithA(a);
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(-10-rot*5, -10+rot*2, 0);
		Renderer.LINES.addVertex(-10-rot*17, 10-rot*3, 0);
		
		Renderer.LINES.addVertex(-10-rot*9, -10-rot*11, 0);
		Renderer.LINES.addVertex(10-rot*13, -10-rot*8, 0);
		
		Renderer.LINES.addVertex(10, -10, 0);
		Renderer.LINES.addVertex(10, 10, 0);
		
		Renderer.LINES.addVertex(-10, 10+rot*18, 0);
		Renderer.LINES.addVertex(10, 10+rot*11, 0);
		Renderer.LINES.draw();
		OpenGLM.popMatrix();
		
		OpenGLM.endOpaqueRendering();
		
		OpenGLM.enableTexture2D();
	}
	
	public static void startupTest(){
		//		final String script ="\n"
		//				+  "function main(){\n"
		//				+  "	var JavaHomeGetter = Java.type(\"com.magiology.handlers.scripting.bridge.JavaHomeGetter\");\n"
		//				+  "	print([JavaHomeGetter.get(),2+1.2]);\n"
		//				+  "}\n"
		//				+  "function render(){\n"
		//				+  "	print(\"rendered\");\n"
		//				+  "}\n"
		//				+  "function update(){\n"
		//				+  "	print(\"updated\");\n"
		//				+  "}\n";
		//		LogUtil.println("\n \n \n-----------------------------------------------------------------");
		//		try{
		//			RenderNUpdateScript sc=new RenderNUpdateScript(script);
		//			sc.callMain();
		//			sc.update();
		//			sc.render();
		//			for(ScriptWrapper j:sc.getLogs()){
		//				for(ScriptLogLine i:j.getLog().getAllLog())LogUtil.println(i.type,i.isError,i.msg);
		//				LogUtil.println("-----------------");
		//			}
		//		}catch(Exception e){
		//			e.printStackTrace();
		//		}
		//		LogUtil.println("-----------------------------------------------------------------\n \n \n");
		//		UtilM.exit(404);
	}
	
}
