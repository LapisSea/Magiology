package com.magiology.util.utilobjects.vectors.physics.real.entitymodel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.MatrixStack;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.real.AbstractRealPhysicsVec3F;
import com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil;
import com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil.Quad;
import com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil.Ray;
import com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil.Triangle;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public abstract class EntityModelColider<T extends EntityLivingBase> extends RenderLivingBase<T> implements Colideable{
	
	protected class ModelRendererColider extends ModelRenderer{
		
		private boolean isCompiled=false;
		private List<Quad> modelBox=new ArrayList<>();
		
		public ModelRendererColider(ModelBase model){
			super(model);
		}

		public ModelRendererColider(ModelBase model, int texOffX, int texOffY){
			super(model, texOffX, texOffY);
		}

		public ModelRendererColider(ModelBase model, String boxNameIn){
			super(model, boxNameIn);
		}
		
		private void addQuads(){
			for(Quad quad:modelBox){
				Quad q=quad.copy();
				q.transform(matrix.get());
				float height=1.41F;
				q.pos1.y+=height;
				q.pos2.y+=height;
				q.pos3.y+=height;
				q.pos4.y+=height;
				quads.add(q);
			}
		}

		private void compile(){
			if(isCompiled)return;
			isCompiled=true;
			modelBox.clear();
			this.cubeList.forEach(box->{
		    	try{
					for(TexturedQuad quad:(TexturedQuad[])quadList.get(box))modelBox.add(new Quad(quad.vertexPositions));
				}catch(Exception e){
					e.printStackTrace();
				}
		    });
		    
		}
		@Override
		public void render(float scale){
			matrix.pushMatrix();
			if(!this.isHidden){
				if(this.showModel){
					compile();
					matrix.translate(this.offsetX, this.offsetY, this.offsetZ);
					
					matrix.scale(scale);
					if(this.rotateAngleX==0.0F&&this.rotateAngleY==0.0F&&this.rotateAngleZ==0.0F){
						if(this.rotationPointX==0.0F&&this.rotationPointY==0.0F&&this.rotationPointZ==0.0F){

							addQuads();
						}else{
							matrix.translate(this.rotationPointX, this.rotationPointY, this.rotationPointZ);
							addQuads();
							matrix.translate(-this.rotationPointX, -this.rotationPointY, -this.rotationPointZ);
						}
					}else{
						matrix.pushMatrix();
						matrix.translate(this.rotationPointX, this.rotationPointY, this.rotationPointZ);

						if(this.rotateAngleZ!=0.0F){
							matrix.rotate(this.rotateAngleZ*(180F/(float)Math.PI), 0.0F, 0.0F, 1.0F);
						}

						if(this.rotateAngleY!=0.0F){
							matrix.rotate(this.rotateAngleY*(180F/(float)Math.PI), 0.0F, 1.0F, 0.0F);
						}

						if(this.rotateAngleX!=0.0F){
							matrix.rotate(this.rotateAngleX*(180F/(float)Math.PI), 1.0F, 0.0F, 0.0F);
						}

						addQuads();

						matrix.popMatrix();
					}
					
					matrix.scale(1/scale);
					matrix.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
				}
			}
			matrix.popMatrix();
		}
		@Override
		public void renderWithRotation(float scale){
			if(!this.isHidden){
				if(this.showModel){
					compile();
					matrix.pushMatrix();
					matrix.translate(this.rotationPointX*scale, this.rotationPointY*scale, this.rotationPointZ*scale);

					if(this.rotateAngleY!=0.0F)matrix.rotate(this.rotateAngleY*(180F/(float)Math.PI), 0.0F, 1.0F, 0.0F);
					if(this.rotateAngleX!=0.0F)matrix.rotate(this.rotateAngleX*(180F/(float)Math.PI), 1.0F, 0.0F, 0.0F);
					if(this.rotateAngleZ!=0.0F)matrix.rotate(this.rotateAngleZ*(180F/(float)Math.PI), 0.0F, 0.0F, 1.0F);
					
					addQuads();
					matrix.popMatrix();
				}
			}
		}
	}
	private static Field quadList;
	protected MatrixStack matrix;
	private Vec3M modelOffset=new Vec3M();
	public List<Quad> quads=new ArrayList<Quad>(),prevQuads=new ArrayList<>(),difference;
	
	public RenderLivingBase<T> src;

	public List<Triangle> triangles,prevTriangles;
	
	public EntityModelColider(RenderManager renderManager, ModelBase modelBase, float shadowSize){
		super(renderManager, modelBase, shadowSize);
		if(quadList==null){
			quadList=ReflectionHelper.findField(ModelBox.class, "quadList");
			quadList.setAccessible(true);
		}
	}
	@Override
	public void applyColideableMove(AbstractRealPhysicsVec3F vec){
		calcTriangles();
		calcPrevTriangles();
		Vec3M hitVec=GeometryUtil.checkPointToTrianleMovmentColision(vec.getPos(), prevTriangles, triangles);
		if(hitVec!=null)vec.addVelocity(hitVec);
	}
	public void calcMotion(){
		if(quads.size()!=prevQuads.size())return;
		difference=new ArrayList<>();
		Iterator<Quad> quads=this.quads.iterator(),prevQuads=this.prevQuads.iterator();
		while(quads.hasNext())difference.add(prevQuads.next().sub(quads.next()));
	}
	public void calcPrevTriangles(){
		if(prevTriangles!=null)return;
		prevTriangles=new ArrayList<>();
		
		prevQuads.forEach(q->{
			Quad q1=q.add(modelOffset);
			prevTriangles.add(q1.getTriangle1());
			prevTriangles.add(q1.getTriangle2());
		});
	}
	public void calcTriangles(){
		if(triangles!=null)return;
		triangles=new ArrayList<>();
		
		quads.forEach(q->{
			Quad q1=q.add(modelOffset);
			triangles.add(q1.getTriangle1());
			triangles.add(q1.getTriangle2());
		});
	}
	@Override
	protected ResourceLocation getEntityTexture(T entity){
		return new ResourceLocation("blank");
	}
	
	@Override
	public Vec3M getModelOffset(){
		return modelOffset;
	}
	
	public Vec3M intersectMesh(Ray ray){
		DoubleObject<Vec3M, Integer> result=GeometryUtil.intersectRayQuads(ray, quads.toArray(new Quad[0]));
		if(result==null)return null;
		return result.obj1;
	}
	
	protected abstract void mainModelGetQuads(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_);
	
	
	@Override
	public DoubleObject<Vec3M, Vec3M> rayTrace(Vec3M start, Vec3M end){
		calcTriangles();
//		DoubleObject<Vec3M, Vec3M> result=null;
		for(Triangle tri:triangles){
			Vec3M poshit=GeometryUtil.intersectRayTriangle(Ray.byPos(start, end), tri);
			if(poshit!=null&&start.distanceTo(poshit)<=start.distanceTo(end)*2){
//				if(result==null)result=new DoubleObject<Vec3M, Vec3M>(poshit, tri.getNormal());
//				else if(start.distanceTo(poshit)<start.distanceTo(result.obj1))result=new DoubleObject<Vec3M, Vec3M>(poshit, tri.getNormal());
				return new DoubleObject<Vec3M, Vec3M>(poshit, tri.getNormal());
			}
		}
		return null;
	}
	@Override
	protected void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_){

		matrix.pushMatrix();
		
		
		mainModelGetQuads(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
		
		matrix.popMatrix();
	}
	@Override
	public void setModelOffset(Vec3M modelOffset){
//		modelOffset=new Vec3M();
		this.modelOffset=modelOffset;
	}
	public void updateMesh(T entity){
		triangles=prevTriangles=null;
		prevQuads=quads;
		quads=new ArrayList<>();
		matrix=new MatrixStack();
		float partialTicks=1F;

		matrix.pushMatrix();

		getMainModel().swingProgress=getSwingProgress(entity, partialTicks);
		getMainModel().isRiding=entity.isRiding();
		getMainModel().isChild=entity.isChild();

		try{
			float f=interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float f1=interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float f2=f1-f;

			if(entity.isRiding()&&entity.getRidingEntity() instanceof EntityLivingBase){
				EntityLivingBase entitylivingbase=(EntityLivingBase)entity.getRidingEntity();
				f=interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
				f2=f1-f;
				float f3=MathHelper.wrapAngleTo180_float(f2);

				if(f3<-85.0F){
					f3=-85.0F;
				}

				if(f3>=85.0F){
					f3=85.0F;
				}

				f=f1-f3;

				if(f3*f3>2500.0F){
					f+=f3*0.2F;
				}
			}
			float f7=entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*partialTicks;
			float f8=handleRotationFloat(entity, partialTicks);

			matrix.rotate(180.0F-f, 0.0F, 1.0F, 0.0F);

			if(entity.deathTime>0){
				float f3=(entity.deathTime+partialTicks-1.0F)/20.0F*1.6F;
				f3=MathHelper.sqrt_float(f3);

				if(f3>1.0F)f3=1.0F;

				matrix.rotate(f3*this.getDeathMaxRotation(entity), 0.0F, 0.0F, 1.0F);
			}else{
				String s=ChatFormatting.stripFormatting(entity.getName());

				if(s!=null&&(s.equals("Dinnerbone")||s.equals("Grumm"))&&(!(entity instanceof EntityPlayer)||((EntityPlayer)entity).isWearing(EnumPlayerModelParts.CAPE))){
					matrix.translate(0.0F, entity.height+0.1F, 0.0F);
					matrix.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				}
			}
			matrix.scale(-1.0F, -1.0F, 1.0F);
			matrix.scale(0.9375F);
//			matrix.translate(0.0F, -1.5078125F, 0.0F);
			float f5=entity.prevLimbSwingAmount+(entity.limbSwingAmount-entity.prevLimbSwingAmount)*partialTicks;
			float f6=entity.limbSwing-entity.limbSwingAmount*(1.0F-partialTicks);

			if(entity.isChild()){
				f6*=3.0F;
			}

			if(f5>1.0F){
				f5=1.0F;
			}
			
			getMainModel().setLivingAnimations(entity, f6, f5, partialTicks);
			getMainModel().setRotationAngles(f6, f5, f8, f2, f7, 0.0625F, entity);

			renderModel(entity, f6, f5, f8, f2, f7, 0.0625F);

		}catch(Exception exception){
			exception.printStackTrace();
		}

		matrix.popMatrix();
	}

}
