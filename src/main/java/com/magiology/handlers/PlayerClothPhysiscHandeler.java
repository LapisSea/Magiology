package com.magiology.handlers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.renderers.VertexRenderer.ShadedQuad;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.codeinsert.ObjectProcessor;
import com.magiology.util.utilobjects.vectors.Vec2i;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.real.AbstractRealPhysicsVec3F;
import com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil;
import com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil.Quad;
import com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil.Triangle;
import com.magiology.util.utilobjects.vectors.physics.real.RealPhysicsMesh;
import com.magiology.util.utilobjects.vectors.physics.real.RealPhysicsMesh.MaterialStrategy;
import com.magiology.util.utilobjects.vectors.physics.real.RealPhysicsVec3F;
import com.magiology.util.utilobjects.vectors.physics.real.entitymodel.EntityModelColider;
import com.magiology.util.utilobjects.vectors.physics.real.entitymodel.EntityPlayerModelColider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class PlayerClothPhysiscHandeler{
	
	private static List<PlayerClothPhysiscHandeler> instances=new ArrayList<>();
	
	public static void addPlayer(EntityPlayer player){
		if(!containsPlayer(player))instances.add(new PlayerClothPhysiscHandeler(player));
	}
	public static boolean containsPlayer(EntityPlayer player){
		for(PlayerClothPhysiscHandeler instance:instances){
			if(UtilM.playerEqual(instance.player,player))return true;
		}
		return false;
	}
	public static void rednerInstance(RenderPlayerEvent.Pre event){
		for(PlayerClothPhysiscHandeler instance:instances){
			if(UtilM.playerEqual(instance.player,event.getEntityPlayer())){
				instance.render(event);
				return;
			}
		}
	}
	public static void removePlayer(EntityPlayer player){
		instances.removeIf(instance->UtilM.playerEqual(instance.player,player));
	}
	public static void updateInstances(){
		for(PlayerClothPhysiscHandeler instance:instances){
			instance.update();
		}
	}
	
	
	private RealPhysicsMesh cape,handEndGloweThingys[];
	
	private EntityPlayer player;
	private EntityModelColider playerModel;
	
	private ObjectProcessor<Vec3M> 
		shouldersBack=new ObjectProcessor<Vec3M>(){
			@Override
			public Vec3M process(Vec3M object, Object... objects){
				int id=(int)objects[2];
				if(playerModel.quads==null)return object;
				
				Quad q=((List<Quad>)playerModel.quads).get(8);
				if(id==0)object=q.pos1.add(0,0,0).add(playerModel.getModelOffset());
				else object=q.pos2.add(0,0,0).add(playerModel.getModelOffset());
				return object;
				
	//			int id=(int)objects[2];
	//			object=UtilM.getEntityPos(player);
	//			float p=1F/16F;
	//			float xRot=UtilM.sin(-player.renderYawOffset+90)*p*4;
	//			float yRot=UtilM.cos(-player.renderYawOffset+90)*p*4;
	//			float xOffset=UtilM.sin(-player.renderYawOffset)*p*2;
	//			float yOffset=UtilM.cos(-player.renderYawOffset)*p*2;
	//			if(id==0){
	//				object=object.add(xRot-xOffset, p*23, yRot-yOffset);
	//			}else{
	//				object=object.add(-xRot-xOffset, p*23, -yRot-yOffset);
	//			}
	//			return object;
			}
		},rightArmEnd=new ObjectProcessor<Vec3M>(){
			@Override
			public Vec3M process(Vec3M object, Object... objects){
				int id=(int)objects[2];
				if(playerModel.quads==null)return object;
				
				switch(id){
				case 0:{
					object=((List<Quad>)playerModel.quads).get(16).pos3;
				}break;
				case 1:{
					object=((List<Quad>)playerModel.quads).get(16).pos4;
				}break;
				case 2:{
					object=((List<Quad>)playerModel.quads).get(17).pos3;
				}break;
				case 3:{
					object=((List<Quad>)playerModel.quads).get(17).pos4;
				}break;
				}
				object=object.add(UtilM.getEntityPos(player));
				return object;
			}
		},leftArmEnd=new ObjectProcessor<Vec3M>(){
			@Override
			public Vec3M process(Vec3M object, Object... objects){
				int id=(int)objects[2];
				if(playerModel.quads==null)return object;
				
				switch(id){
				case 0:{
					object=((List<Quad>)playerModel.quads).get(16+6).pos1;
				}break;
				case 1:{
					object=((List<Quad>)playerModel.quads).get(16+6).pos2;
				}break;
				case 2:{
					object=((List<Quad>)playerModel.quads).get(17+6).pos1;
				}break;
				case 3:{
					object=((List<Quad>)playerModel.quads).get(17+6).pos2;
				}break;
				}
				object=object.add(UtilM.getEntityPos(player));
				return object;
			}
		};
	
	public PlayerClothPhysiscHandeler(EntityPlayer player){
		setPlayer(player);
	}
	
	private boolean checkForTimeout(){
		if(player.isDead){
			removePlayer(player);
			return true;
		}
		return false;
	}
	private void createCape(){
		DoubleObject<List<Vec3M>, List<Vec2i>> mesh=RealPhysicsMesh.create2DPlane(3, 8, 0.15F, 0.2F);
		
		RealPhysicsVec3F example=new RealPhysicsVec3F(getWorld(), UtilM.getEntityPos(player).add(0, 26F/16F, 0));
		
		example.setMass(0.02F);
		example.setAirBorneFriction(0.9F);
		example.setSurfaceFriction(0.9F);
		example.setBounciness(0.05F);
		
		cape=new RealPhysicsMesh(getWorld(), mesh.obj1, mesh.obj2, example, MaterialStrategy.ONLY_BIGGER_SUPPRESSING);
		
		cape.addWorldHook(shouldersBack, 0,3);
	}
	private void createHandEndGloweThingys(){
		if(playerModel==null||playerModel.quads.isEmpty())return;
		Vec3M[][][] ends=new Vec3M[2][3][4];
		List<Vec3M> vert1=new ArrayList<>(),vert2=new ArrayList<>();
		List<Vec2i> ind=new ArrayList<>();
		
		for(int j=0;j<ends[0].length-1;j++){
			for(int i=0;i<4;i++)ind.add(new Vec2i(i+j*4, i+4+j*4));
			ind.add(new Vec2i(j*4+4, j*4+7));
			ind.add(new Vec2i(j*4+5, j*4+6));
			ind.add(new Vec2i(j*4+5, j*4+4));
			ind.add(new Vec2i(j*4+6, j*4+7));
			
			ind.add(new Vec2i(j*4  , j*4+7));
			ind.add(new Vec2i(j*4+4, j*4+3));
			
			ind.add(new Vec2i(j*4+1, j*4+4));
			ind.add(new Vec2i(j*4+5, j*4  ));

			ind.add(new Vec2i(j*4+1, j*4+6));
			ind.add(new Vec2i(j*4+2, j*4+5));
			
			ind.add(new Vec2i(j*4+2, j*4+7));
			ind.add(new Vec2i(j*4+3, j*4+6));
		}
		
		float lenght=2F/16F;
		try{
			for(int i=0;i<4;i++){
				ends[0][0][i]=leftArmEnd.process(new Vec3M(), null,null,i);
				ends[1][0][i]=rightArmEnd.process(new Vec3M(), null,null,i);
			}
			Vec3M normal1=GeometryUtil.getNormal(ends[0][0][2], ends[0][0][1], ends[0][0][0]).mul(lenght);
			Vec3M normal2=GeometryUtil.getNormal(ends[1][0][2], ends[1][0][1], ends[1][0][0]).mul(lenght);
			for(int i=1;i<ends[0].length;i++){
				for(int j=0;j<4;j++){
					ends[0][i][j]=ends[0][0][j].add(normal1.mul(i));
					ends[1][i][j]=ends[1][0][j].add(normal2.mul(i));
				}
			}
			
			for(int j=0;j<ends[0].length;j++){
				for(int l=0;l<ends[0][0].length;l++){
					vert1.add(ends[0][j][l]);
					vert2.add(ends[1][j][l]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		UtilM.getEntityPos(player);
		RealPhysicsVec3F example=new RealPhysicsVec3F(getWorld(), new Vec3M());
		
		example.setMass(0.03F);
		example.setAirBorneFriction(0.9F);
		example.setSurfaceFriction(0.9F);
		example.setBounciness(0.05F);
		example.setWillColideWithBlocks(false);
		handEndGloweThingys=new RealPhysicsMesh[2];
		for(int i=0;i<2;i++){
			ObjectProcessor<Vec3M> hook=i==0?leftArmEnd:rightArmEnd;
			
			handEndGloweThingys[i]=new RealPhysicsMesh(getWorld(), i==0?vert1:vert2, ind, example, MaterialStrategy.ONLY_BIGGER_SUPPRESSING);
			
			handEndGloweThingys[i].addWorldHook(hook, 0,1,2,3);
		}
	}
	
	private void drawModel(){
		VertexRenderer buff=TessUtil.getVB();
		try{
			int size=Math.min(playerModel.triangles.size(), playerModel.prevTriangles.size());
			for(int i=0;i<size;i++){
				Triangle 
					prevTriangle=(Triangle)playerModel.prevTriangles.get(i),
					triangle1=(Triangle)playerModel.triangles.get(i),
					triangle=new Triangle(
						PartialTicksUtil.calculate(prevTriangle.pos1, triangle1.pos1),
						PartialTicksUtil.calculate(prevTriangle.pos2, triangle1.pos2),
						PartialTicksUtil.calculate(prevTriangle.pos3, triangle1.pos3)
					);
				buff.addVertex(triangle.pos1.x,triangle.pos1.y,triangle.pos1.z);
				buff.addVertex(triangle.pos2.x,triangle.pos2.y,triangle.pos2.z);
				buff.addVertex(triangle.pos3.x,triangle.pos3.y,triangle.pos3.z);
				buff.addVertex(triangle.pos3.x,triangle.pos3.y,triangle.pos3.z);
				
				if(i%2==0){
					Vec3M pos=triangle.pos1.add(triangle.pos3).mul(0.5);
					TessUtil.drawLine(pos, pos.add(triangle.getNormal().mul(0.25)), 0.02F, false, buff, 0, 0);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		buff.draw();
	}
	private World getWorld(){
		return player.getEntityWorld();
	}
	private boolean isCapeFunctional(){
		return isHandelerInitialized()&&cape!=null;
	}
	private boolean isHandelerInitialized(){
		return player!=null&&playerModel!=null;
	}
	public void render(RenderPlayerEvent.Pre event){
		if(checkForTimeout())return;
		if(playerModel==null)playerModel=new EntityPlayerModelColider(event.getRenderer());
		if(!isCapeFunctional())return;
		
		
		VertexRenderer buff=TessUtil.getVB();
		OpenGLM.pushMatrix();
		OpenGLM.disableTexture2D();
		
		if(UtilM.playerEqual(player,event.getEntityPlayer())){
			
			GL11U.glTranslate(cape.originPos.mul(-1));
			boolean debudDraw=UtilM.FALSE()&&playerModel.prevTriangles!=null;
			if(debudDraw)drawModel();
			new ColorF(0, 0, 1, 1).bind();
			buff.draw();
			ColorF.WHITE.bind();
			
			List<AbstractRealPhysicsVec3F> vertices=cape.getVertices();
			
			int xCubes=3,zCubes=8;
			
			int xPoints=xCubes+1;

			for(int i=0;i<zCubes;i++){
				for(int j=0;j<xCubes;j++){
					int 
						pos1=0,        pos2=1,
						pos3=xPoints,pos4=xPoints+1;
					
					pos1+=xPoints*i+j;
					pos2+=xPoints*i+j;
					pos3+=xPoints*i+j;
					pos4+=xPoints*i+j;
					Vec3M vec1=vertices.get(pos1).getPos();
					Vec3M vec2=vertices.get(pos2).getPos();
					Vec3M vec3=vertices.get(pos3).getPos();
					Vec3M vec4=vertices.get(pos4).getPos();
					
					buff.addVertex(vec4.x,vec4.y,vec4.z);
					buff.addVertex(vec3.x,vec3.y,vec3.z);
					buff.addVertex(vec1.x,vec1.y,vec1.z);
					buff.addVertex(vec2.x,vec2.y,vec2.z);
					
					buff.addVertex(vec2.x,vec2.y,vec2.z);
					buff.addVertex(vec1.x,vec1.y,vec1.z);
					buff.addVertex(vec3.x,vec3.y,vec3.z);
					buff.addVertex(vec4.x,vec4.y,vec4.z);
				}
			}
			if(handEndGloweThingys!=null){
				for(int j=0;j<2;j++)
				for(int i=0;i<2;i++){
					List<Vec3M> vecs=new ArrayList<>();
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+0).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+1).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+5).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+4).getPos());
					
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+2).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+3).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+7).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+6).getPos());
					
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+1).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+2).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+5).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+6).getPos());
					
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+3).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+0).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+7).getPos());
					vecs.add(handEndGloweThingys[j].getVertices().get(i*4+4).getPos());
					
					for(Vec3M vec3m:vecs){
						buff.addVertexWithUV(vec3m, 0, 0);
					}
					for(Vec3M vec3m:Lists.reverse(vecs)){
						buff.addVertexWithUV(vec3m, 0, 0);
					}
				}
			}
			List<ShadedQuad> quads=buff.getTriangles();
			OpenGLM.shadeModel(GL11.GL_SMOOTH);
			Renderer.POS_UV_COLOR_NORMAL.beginQuads();
			for(ShadedQuad shadedQuad:quads){
				for(int b=0;b<4;b++){
					Vec3d pos=shadedQuad.pos4[b].vector3D;
					double pos1=(pos.xCoord+pos.yCoord+pos.zCoord)*10;
					
					Renderer.POS_UV_COLOR_NORMAL.addVertex(
							pos.xCoord,pos.yCoord,pos.zCoord, shadedQuad.pos4[b].texturePositionX, shadedQuad.pos4[b].texturePositionY,
							new ColorF(UtilC.fluctuateSmooth(120, pos1),UtilC.fluctuateSmooth(45, pos1),UtilC.fluctuateSmooth(67, pos1),1), 
							shadedQuad.normal1);
				}
			}
			Renderer.POS_UV_COLOR_NORMAL.draw();
			OpenGLM.shadeModel(GL11.GL_FLAT);
			buff.cleanUp();
		}
		OpenGLM.enableTexture2D();
		OpenGLM.popMatrix();
	}
	public void setPlayer(EntityPlayer player){
		this.player=player;
	}
	public void update(){
		if(checkForTimeout())return;
		if(cape==null||cape.isCritical()/*||player.isSneaking()&&getWorld().getTotalWorldTime()%40==0*/)createCape();
		if(!isCapeFunctional())return;
		if(cape.coliders.isEmpty()){
			cape.coliders.add(playerModel);
		}
		
		if(UtilM.playerEqual(UtilC.getThePlayer(), player))player=UtilC.getThePlayer();
		
		cape.originPos=UtilM.getEntityPos(player);
		playerModel.updateMesh(player);
		cape.update();
		if(handEndGloweThingys==null)createHandEndGloweThingys();
		else{
			if(handEndGloweThingys[0].isCritical()||handEndGloweThingys[1].isCritical())createHandEndGloweThingys();
			handEndGloweThingys[0].coliders=handEndGloweThingys[1].coliders=cape.coliders;
			handEndGloweThingys[0].originPos=handEndGloweThingys[1].originPos=cape.originPos;
			handEndGloweThingys[0].update();
			handEndGloweThingys[1].update();
		}
//		if(player==UtilM.getThePlayer()?U.getMC().gameSettings.thirdPersonView!=0:true)for(int i=32;i<35;i++){
//			AbstractRealPhysicsVec3F pos1=cape.getVertices().get(i),pos2=cape.getVertices().get(i+1);
//			Vec3M 
//				posDiff=pos2.getPos().sub(pos1.getPos()),
//				pos=pos1.getPos().add(posDiff.mul(RandUtil.RF())),
//				speed=pos1.getVelocity().add(pos2.getVelocity().sub(pos1.getVelocity()).mul(RandUtil.RF())).mul(0.3);
//			UtilM.spawnEntityFX(new EntitySmoothBubleFX(player.worldObj, 
//					pos.x, pos.y, pos.z, 
//					speed.x, speed.y, speed.z,
//					(int)(1500*posDiff.lengthVector()), 2, 0, !RandUtil.RB(0.1)?1:2, RandUtil.RF(),RandUtil.RF(),RandUtil.RF(), 0.2));
//		}
	}
}
