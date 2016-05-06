package com.magiology.client.render.itemrender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.client.render.Textures;
import com.magiology.forgepowered.events.client.CustomRenderedItem.CustomRenderedItemRenderer;
import com.magiology.handlers.animationhandlers.thehand.HandData;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.handlers.animationhandlers.thehand.animation.CommonHand;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.MultiTransfromModel;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.renderers.glstates.GlState;
import com.magiology.util.renderers.glstates.GlStateCell;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MatrixUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.IndexedModel;
import com.magiology.util.utilobjects.vectors.QuadUVGenerator;
import com.magiology.util.utilobjects.vectors.Vec2FM;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.PhysicsVec3F;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRendererTheHand implements CustomRenderedItemRenderer{
	
	private static ItemRendererTheHand instance;

	public static ItemRendererTheHand get(){
		if(instance==null)instance=new ItemRendererTheHand();
		return instance;
	}
	
	private final static float p=1F/16F;
	
	private static Matrix4f worldMat;
	
	public ResourceLocation[] blank1={new ResourceLocation("noTexture")};
	
	
	private VertexModel defultModel;
	
	private static HandData lastDataRight,lastDataLeft;
	public MultiTransfromModel handModelSolidRight,handModelOpaque,handModelGlow;
	
	
	
	private Matrix4f 
		thumb1,thumb2,thumb3,
		finger1,finger2,finger3,
		finger4,finger5,finger6,
		finger7,finger8,finger9,
		finger10,finger11,finger12;
	
	
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void renderItem(ItemStack stack, TransformType position, boolean leftHand){
//		PrintUtil.printStackTrace();
		secure();
		OpenGLM.pushMatrix();
		if(defultModel==null){
			HandData data=CommonHand.naturalPosition.data.Clone();
			switch(position){
			case GROUND:{
				data.base[0]=0.20976496F;
				data.base[1]=-0.375F;
				data.base[2]=-1.1224111F;
			}break;
			case GUI:{
				data.base[0]=p*12;
				data.base[1]=-p*10;
				data.base[2]=-1.1224111F;
				data.base[3]=20;
				data.base[4]=-120;
			}break;
			}
			
			render(data,false);
			render(data,true);
		}
//		PrintUtil.println(position);
		if(position!=TransformType.FIRST_PERSON_RIGHT_HAND&&position!=TransformType.FIRST_PERSON_LEFT_HAND){
			OpenGLM.translate(p*13, p*3, 0);
//			GL11U.glRotate(0, 130, 0);
			
//			PrintUtil.println(position, leftHand);
			switch(position){
			case GROUND:{
				OpenGLM.translate(0.20976496F,-0.375F,-1.1224111F);
			}break;
			case GUI:{
				OpenGLM.translate(-p*5,p*0,-0F);
				GL11U.glRotate(40,-150,-10,p*4,p,0);
				
			}break;
			case THIRD_PERSON_RIGHT_HAND:{
				GL11U.glScale(p*6);
				GL11U.glRotate(0, -130, 0);
				GL11U.glRotate(180, 0, 180);
				OpenGLM.translate(p*10, p*7, -p*22);
			}break;
			case THIRD_PERSON_LEFT_HAND:{
				GL11U.glScale(p*6);
				GL11U.glRotate(0, -130, 0);
				GL11U.glRotate(180, 0, 180);
				OpenGLM.translate(p*17, p*7, -p*22);
				OpenGLM.scale(-1, 1, 1);
			}break;
			default:
				PrintUtil.println("HEY! "+position+" is not implemented!");
				break;
			}
			
			TessUtil.bindTexture(Textures.handTexture);
			defultModel.draw();
		}else{
			leftHand=UtilC.getThePlayer().getHeldItemOffhand()==stack;
			render(leftHand?(lastDataLeft=TheHandHandler.getRenderLeftHandData()):(lastDataRight=TheHandHandler.getRenderRightHandData()),leftHand);
		}
		OpenGLM.popMatrix();
	}
	
	private void render(HandData hand, boolean leftHand){
		worldMat=null;
		OpenGLM.pushMatrix();
		try{
			GL11U.endOpaqueRendering();
			List<Matrix4f> transformations=new ArrayList<>(),transformationsCube=new ArrayList<>();
			if(hand.cubes==null)for(int i=0;i<8;i++)transformationsCube.add(new Matrix4f());
			else for(DoubleObject<PhysicsVec3F,PhysicsVec3F> cube:hand.cubes){
				transformationsCube.add(MatrixUtil.createMatrix(cube.obj1.getPoint()).rotateAt(new Vec3M(p*(3.25F+0.375F), p*(2.5F+0.375F), p*(4.25F+0.375F)),cube.obj2.getPoint()).finish());
			}
			
			addFingerRotations(transformations, new Matrix4f[]{thumb1, thumb2, thumb3},new Vec3M(hand.thumb[0], hand.thumb[1], hand.thumb[2]),new Vector2f(hand.thumb[3], hand.thumb[4]));
			addFingerRotations(transformations, new Matrix4f[]{finger1, finger2, finger3},new Vec3M(hand.fingers[0][0], hand.fingers[0][1], 0),new Vector2f(hand.fingers[0][2], hand.fingers[0][3]));
			addFingerRotations(transformations, new Matrix4f[]{finger4, finger5, finger6},new Vec3M(hand.fingers[1][0], hand.fingers[1][1], 0),new Vector2f(hand.fingers[1][2], hand.fingers[1][3]));
			addFingerRotations(transformations, new Matrix4f[]{finger7, finger8, finger9},new Vec3M(hand.fingers[2][0], hand.fingers[2][1], 0),new Vector2f(hand.fingers[2][2], hand.fingers[2][3]));
			addFingerRotations(transformations, new Matrix4f[]{finger10, finger11, finger12},new Vec3M(hand.fingers[3][0], hand.fingers[3][1], 0),new Vector2f(hand.fingers[3][2], hand.fingers[3][3]));
			
			generateModel(hand);
			
			ColorF.WHITE.bind();
			TessUtil.bindTexture(Textures.handTexture);
			OpenGLM.translate(p*13, p*3, 0);
			GL11U.glRotate(0, 180, 0);
			OpenGLM.translate(hand.base[0]+p*3, hand.base[1]+p*4, hand.base[2]-p*1);
			if(hand.calciferiumPrecentage>0.7){
				float noise=(hand.calciferiumPrecentage-0.7F)/40;
				OpenGLM.translate(RandUtil.CRF(noise), RandUtil.CRF(noise), RandUtil.CRF(noise));
				noise*=150;
				GL11U.glRotate(RandUtil.CRF(noise), RandUtil.CRF(noise), RandUtil.CRF(noise));
			}
			
			GL11U.glRotate(hand.base[3], hand.base[4], hand.base[5],p*4,p,0);
			transformations.addAll(transformationsCube);
			if(defultModel==null)complieDefultModel(transformations,transformationsCube);
			handModelSolidRight.draw(transformations);
			handModelGlow.draw(transformationsCube);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		OpenGLM.popMatrix();
		
	}
	
	public void secure(){
		if(thumb1==null)init();
	}
	@Override
	public boolean shouldRenderSpecial(ItemStack stack, TransformType position, boolean leftHand){
		return true;
	}
	private void addFingerRotations(List<Matrix4f> transformations, Matrix4f[] mats, Vec3M rotBase, Vector2f rot_2_3){
		Matrix4f mat1=Matrix4f.load(mats[0], null);
		Matrix4f mat2=Matrix4f.load(mats[1], null);
		Matrix4f mat3=Matrix4f.load(mats[2], null);
		
		Matrix4f.mul(mat1, MatrixUtil.createMatrixZYX(rotBase.getZ(),rotBase.getY(),rotBase.getX()).finish(), mat1); 
		Matrix4f.mul(mat2, MatrixUtil.createMatrixY(rot_2_3.x).finish(), mat2);
		Matrix4f.mul(mat3, MatrixUtil.createMatrixY(rot_2_3.y).finish(), mat3);
		
		Matrix4f.mul(mat1, mat2, mat2);
		Matrix4f.mul(mat2, mat3, mat3);
		
		transformations.add(mat1);
		transformations.add(mat2);
		transformations.add(mat3);
	}
	
	private void addFingerTextureUVs(IndexedModel model,QuadUVGenerator gen,int xStart,int yStart,int height, int lenght1, int lenght2, int lenght3,boolean shouldExpand){
		int 
			totalLenght=shouldExpand?lenght1+lenght2+lenght3:0,
			bottom=totalLenght*2,
			side__=totalLenght,
			top___=0,

			bottom1=bottom,
			bottom2=bottom1+lenght1,
			bottom3=bottom2+lenght2,
			side__1=side__,
			side__2=side__1+lenght1,
			side__3=side__2+lenght2,
			top___1=top___,
			top___2=top___1+lenght1,
			top___3=top___2+lenght2,
			start__=totalLenght*3,
			end____=start__+height;
		
		model.addUVs(gen.create(xStart+top___1, yStart, lenght1, height).mirror2().toArray());
		model.addUVs(gen.create(xStart+bottom1, yStart, lenght1, height).toArray());
		model.addUVs(gen.create(xStart+side__1, yStart, lenght1, height).rotate2().toArray());
		model.addUVs(gen.create(xStart+side__1, yStart, lenght1, height).rotate2().toArray());
		model.addUVs(gen.create(xStart+start__, yStart, height,  height).toArray());
		model.addUVs(gen.create(xStart+top___2, yStart, lenght2, height).mirror2().toArray());
		model.addUVs(gen.create(xStart+bottom2, yStart, lenght2, height).toArray());
		model.addUVs(gen.create(xStart+side__2, yStart, lenght2, height).rotate2().toArray());
		model.addUVs(gen.create(xStart+side__2, yStart, lenght2, height).rotate2().toArray());
		
		model.addUVs(gen.create(xStart+top___3, yStart, lenght3, height).mirror2().toArray());
		model.addUVs(gen.create(xStart+bottom3, yStart, lenght3, height).toArray());
		model.addUVs(gen.create(xStart+side__3, yStart, lenght3, height).rotate2().toArray());
		model.addUVs(gen.create(xStart+side__3, yStart, lenght3, height).rotate2().toArray());
		model.addUVs(gen.create(xStart+end____, yStart, height,  height).rotate2().toArray());
		
		model.addUVs(gen.create(xStart+side__2-1, yStart, 2, height).rotate2().toArray());
		model.addUVs(gen.create(xStart+side__2-1, yStart, 2, height).rotate2().mirror2().toArray());
		model.addUVs(gen.create(xStart+top___2-1, yStart, 2, height).rotate1().mirror1().toArray());
		model.addUVs(gen.create(xStart+bottom2-1, yStart, 2, height).rotate1().toArray());
		
		model.addUVs(gen.create(xStart+side__3-1, yStart, 2, height).rotate2().toArray());
		model.addUVs(gen.create(xStart+side__3-1, yStart, 2, height).rotate1().toArray());
		model.addUVs(gen.create(xStart+top___3-1, yStart, 2, height).rotate1().mirror1().toArray());
		model.addUVs(gen.create(xStart+bottom3-1, yStart, 2, height).rotate1().toArray());
	}
	
	
	private void complieDefultModel(final List<Matrix4f> transformations, final List<Matrix4f> transformationsCube){
		List<Vec3M> vertices=handModelSolidRight.getTransfromed(transformations);
		List<Integer> indices=handModelSolidRight.getChild().getIndices();
		Iterator<Vec2FM> uvs=handModelSolidRight.getChild().getUVs().iterator();
		
		VertexRenderer buff=TessUtil.getVB();
		buff.cleanUp();
		indices.forEach(index->{
			Vec2FM uv=uvs.hasNext()?uvs.next():new Vec2FM();
			buff.addVertexWithUV(vertices.get(index), uv.x, uv.y);
		});
		defultModel=new VertexModel();
		buff.transformAndSaveTo(defultModel);
	}
	
	private void generateModel(HandData hand){
		QuadUVGenerator gen=new QuadUVGenerator(256, 256);
		if(handModelSolidRight==null||true){
			IndexedModel 
				modelSolid=new IndexedModel(),
				modelOpaque=new IndexedModel(),
				cubeGlow=new IndexedModel();
			handModelSolidRight=new MultiTransfromModel(modelSolid);
			handModelOpaque=new MultiTransfromModel(modelOpaque);
				
			CubeModel 
				cub=new CubeModel(p*2.75F, p*2, p*3.75F, p*5.25F, p*2.5F, p*6.25F),
				cub2=new CubeModel(0, 0, p*1.25F, p*8F, p*2.0001F, p*9.25F);
			cub.willSideRender[3]=false;
			
			cub.translate(0,0.0001F,0);
			cub.willSideRender=new boolean[6];
			cub.willSideRender[2]=true;
			cubeGlow.addCube(cub);
			
			cub2.willSideRender=cub.willSideRender;
			cubeGlow.addCube(cub2);
			cubeGlow.addUVs(gen.create(180, 20, 20, 20).toArray());
			cubeGlow.addUVs(gen.create(159, 0,  63, 63).toArray());
			
			handModelGlow=new MultiTransfromModel(cubeGlow);
			
			
			
			
			cub=new CubeModel(p*2.75F, p*2, p*3.75F, p*5.25F, p*2.5F, p*6.25F);
			cub.willSideRender[3]=false;
			
			for(int i=0;i<2;i++){
				MultiTransfromModel model=i==0?handModelSolidRight:handModelOpaque;
				IndexedModel child=model.getChild();
				child.addCube(new CubeModel(0, 0, 0, p*8, p*2, p*10));
				addFinger(model, p*2,    new Vec3M(p*3.5, p*3.4, p*2.4));
				addFinger(model, p*1.7F, new Vec3M(p*3.2, p*2.4, p*2.3));
				addFinger(model, p*1.8F, new Vec3M(p*3.6, p*3,   p*2.5));
				addFinger(model, p*1.8F, new Vec3M(p*3.2, p*2.7, p*2.9));
				addFinger(model, p*1.6F, new Vec3M(p*2.1, p*2,   p*2.1));
				child.addCube(cub);
				model=i==0?handModelSolidRight:handModelGlow;
				child=model.getChild();
				float p=this.p*0.75F;
				CubeModel cub3=new CubeModel(0,0,0, p,p,p);
				cub3.translate(this.p*3.25F, this.p*2.5F, this.p*4.25F);
				
				child.addCube(cub3);
				child.addCube(cub3);
				child.addCube(cub3);
				child.addCube(cub3);
				child.addCube(cub3);
				child.addCube(cub3);
				child.addCube(cub3);
				child.addCube(cub3);
				int start=i==1?16:136;
				model.addMatrix(UtilM.countedArray(start,(start+=8)));
				model.addMatrix(UtilM.countedArray(start,(start+=8)));
				model.addMatrix(UtilM.countedArray(start,(start+=8)));
				model.addMatrix(UtilM.countedArray(start,(start+=8)));
				model.addMatrix(UtilM.countedArray(start,(start+=8)));
				model.addMatrix(UtilM.countedArray(start,(start+=8)));
				model.addMatrix(UtilM.countedArray(start,(start+=8)));
				model.addMatrix(UtilM.countedArray(start,(start+=8)));
			}
			
			IndexedModel child=handModelSolidRight.getChild();
			//base
			child.addUVs(gen.create(128, 0, 16, 80).rotate2().toArray());
			child.addUVs(gen.create(144, 0, 16, 80).rotate2().toArray());
			child.addUVs(gen.create(0,   0, 64, 80).toArray());
			child.addUVs(gen.create(64,  0, 64, 80).toArray());
			child.addUVs(gen.create(0,  80, 64, 16).toArray());
			child.addUVs(gen.create(64, 80, 64, 16).toArray());
			
			//thumb
			addFingerTextureUVs(child, gen, 0, 96,  16,  28, 27, 19, true);
			addFingerTextureUVs(child, gen, 0, 112, 14,  25, 19, 18, true);
			addFingerTextureUVs(child, gen, 0, 126, 15,  29, 24, 15, true);
			addFingerTextureUVs(child, gen, 0, 141, 15,  26, 22, 23, true);
			addFingerTextureUVs(child, gen, 0, 156, 13,  17, 16, 17, true);
			
			Vec2FM[] sideUV=gen.create(145, 213, 4, 20).rotate1().toArray(),cubeUv=gen.create(232, 224, 8, 8).toArray();
			child.addUVs(sideUV);
			child.addUVs(sideUV);
			child.addUVs(gen.create(149, 213, 20, 20).rotate1().rotate1().toArray());
			child.addUVs(sideUV);
			child.addUVs(sideUV);
			for(int i=0;i<48;i++)child.addUVs(cubeUv);
			
			child=handModelOpaque.getChild();
			//base
			child.addUVs(gen.create(128, 170, 16, 80).rotate2().toArray());
			child.addUVs(gen.create(128, 170, 16, 80).rotate2().toArray());
			child.addUVs(gen.create(64,  170, 64, 80).toArray());
			child.addUVs(gen.create(0,   170, 64, 80).toArray());
			child.addUVs(gen.create(144, 234, 64, 16).toArray());
			child.addUVs(gen.create(144, 234, 64, 16).toArray());
			
			//thumb
			addFingerTextureUVs(child, gen, 182, 157, 15,  28, 27, 19, false);
			addFingerTextureUVs(child, gen, 194, 172, 14,  25, 19, 18, false);
			addFingerTextureUVs(child, gen, 183, 186, 15,  29, 24, 15, false);
			addFingerTextureUVs(child, gen, 185, 201, 15,  26, 22, 23, false);
			addFingerTextureUVs(child, gen, 144, 173, 13,  17, 16, 17, false);
			
			sideUV=gen.create(224, 236, 4, 20).rotate1().toArray();
			child.addUVs(sideUV);
			child.addUVs(sideUV);
			child.addUVs(gen.create(228, 236, 20, 20).rotate1().rotate1().toArray());
			child.addUVs(sideUV);
			child.addUVs(sideUV);
			
			
			cubeUv=gen.create(232, 216, 8, 8).toArray();
			for(int i=0;i<48;i++)handModelGlow.getChild().addUVs(cubeUv);
			
			
			handModelOpaque.getChild().getVertices().clear();
			
			handModelSolidRight.addChild(handModelOpaque);
			
			
		}

		handModelGlow.cell=new GlStateCell(new GlState(()->{
			GL11U.setUpOpaqueRendering(2);
			OpenGLM.color(1, 1, 1, hand.cubeGlowPrecentage);
			OpenGLM.disableLightmap();
		}), new GlState(()->{
			OpenGLM.enableLightmap();
			GL11U.endOpaqueRendering();
		}));
		handModelGlow.cell.willRender=()->lastDataRight.cubeGlowPrecentage>1F/256;
		
		handModelOpaque.cell=new GlStateCell(new GlState(()->{
			GL11U.setUpOpaqueRendering(1);
			OpenGLM.color(1, 1, 1, hand.calciferiumPrecentage);
			OpenGLM.disableLightmap();
		}), new GlState(()->{
			OpenGLM.enableLightmap();
			GL11U.endOpaqueRendering();
		}));
		handModelOpaque.cell.willRender=()->lastDataRight.calciferiumPrecentage>1F/256;
		
	}
	
	private void addFinger(MultiTransfromModel model1,float wh, Vec3M lenghts){
		IndexedModel model=model1.getChild();
		int start=model.getVertices().size();
		CubeModel 
			th1=new CubeModel(-wh/2, -wh/2, 0, wh/2, wh/2, lenghts.getX()),
			th2=new CubeModel(-wh/2, -wh/2, 0, wh/2, wh/2, lenghts.getY()),
			th3=new CubeModel(-wh/2, -wh/2, 0, wh/2, wh/2, lenghts.getZ());
		th1.willSideRender[5]=
		th2.willSideRender[4]=
		th2.willSideRender[5]=
		th3.willSideRender[4]=false;

		model.addCube(th1);
		model.addCube(th2);
		model.addCube(th3);
		
		int[] partFront={2,3,6,7},partBack={1,0,5,4};
		for(int i=0;i<4;i++)partBack[i]+=8;
		
		int[] inds={
			partBack[0],
			partFront[0],
			partFront[2],
			partBack[2],
			
			partFront[1],
			partBack[1],
			partBack[3],
			partFront[3],
			
			partBack[1],
			partFront[1],
			partFront[0],
			partBack[0],
			
			partFront[3],
			partBack[3],
			partBack[2],
			partFront[2]
		};
		
		model.addIndices(start,inds);
		model.addIndices(start+8F,inds);
		
		model1.addMatrix(UtilM.countedArray(start,start+8));
		model1.addMatrix(UtilM.countedArray(start+8,start+16));
		model1.addMatrix(UtilM.countedArray(start+16,start+24));
	}
	
	public static Vec3M getPalmMiddle(){
		return new Vec3M();
	}
	
	private void init(){
		thumb1=MatrixUtil.createMatrix(new Vector3f(p*6.5F,p*0.8F,p*1.7F)).finish();
		thumb2=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*3.5F)).finish();
		thumb3=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*3.4F)).finish();
		
		finger1=MatrixUtil.createMatrix(new Vector3f(p*7F,p,p*10F)).rotateZ(-90).finish();
		finger2=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*3.3F)).finish();
		finger3=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*2.5F)).finish();
				
		finger4=MatrixUtil.createMatrix(new Vector3f(p*5F,p,p*10F)).rotateZ(-90).finish();
		finger5=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*3.7F)).finish();
		finger6=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*3.1F)).finish();
				
		finger7=MatrixUtil.createMatrix(new Vector3f(p*3F,p,p*10F)).rotateZ(-90).finish();
		finger8=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*3.3F)).finish();
		finger9=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*2.8F)).finish();
		
		finger10=MatrixUtil.createMatrix(new Vector3f(p*1F,p,p*10F)).rotateZ(-90).finish();
		finger11=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*2.2F)).finish();
		finger12=MatrixUtil.createMatrix(new Vector3f(0,0.00001F,p*2.1F)).finish();
	}
}