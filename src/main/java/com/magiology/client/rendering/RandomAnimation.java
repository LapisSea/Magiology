package com.magiology.client.rendering;

import com.magiology.client.renderers.Renderer;
import com.magiology.util.objs.animation.AnimationM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.math.MathUtil;
import com.magiology.util.statics.math.PartialTicksUtil;
import com.magiology.util.statics.TestingAnimationM;
import com.magiology.util.statics.UtilC;

import jline.internal.Log;

public class RandomAnimation{
	
	public static void rendner(){
		AnimationM anim=TestingAnimationM.get();
		
		float precent=(float)(UtilC.getWorldTime()%50D+PartialTicksUtil.partialTicks)/50;
		float a=-anim.get(anim.getIdName("alpha"), precent);
		float trans=anim.get(anim.getIdName("translate"), precent);
		float rot=-anim.get(anim.getIdName("explode"), precent);
		OpenGLM.pushMatrix();
		OpenGLM.translate(100, 0, 0);
		OpenGLM.disableTexture2D();
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(0,10,0);
		Renderer.LINES.addVertex(0,10-10*a,0);
		
		Renderer.LINES.addVertex(3,10,0);
		Renderer.LINES.addVertex(3,10+10*trans,0);
		
		Renderer.LINES.addVertex(6,10,0);
		Renderer.LINES.addVertex(6,10-10*rot,0);
		Renderer.LINES.draw();
		
		OpenGLM.setUpOpaqueRendering(BlendFunc.NORMAL);
		
		OpenGLM.translate((1-trans)*50, 50, 0);
		OpenGLM.rotateZ(rot*10);
		
		ColorM.WHITE.bindWithA(MathUtil.snap(a/15-rot/2, 0, 1));
		Renderer.POS.beginQuads();
		Renderer.POS.addVertex(-10, 10,0);
		Renderer.POS.addVertex( 10, 10,0);
		Renderer.POS.addVertex( 10,-10,0);
		Renderer.POS.addVertex(-10,-10,0);
		Renderer.POS.draw();
		
		ColorM.WHITE.bindWithA(a);
		Renderer.LINES.begin();
		Renderer.LINES.addVertex(-10-rot*5,-10+rot*2,0);
		Renderer.LINES.addVertex(-10-rot*17, 10-rot*3,0);
		
		Renderer.LINES.addVertex(-10-rot*9,-10-rot*11,0);
		Renderer.LINES.addVertex(10 -rot*13,-10-rot*8,0);

		Renderer.LINES.addVertex(10,-10,0);
		Renderer.LINES.addVertex(10, 10,0);
		
		Renderer.LINES.addVertex(-10,10+rot*18,0);
		Renderer.LINES.addVertex(10, 10+rot*11,0);
		Renderer.LINES.draw();
		OpenGLM.popMatrix();
		
		OpenGLM.endOpaqueRendering();
		
		OpenGLM.enableTexture2D();
	}
	
}
