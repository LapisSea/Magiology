package com.magiology.util.objs.block_bounds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import com.magiology.client.renderers.Renderer;
import com.magiology.mc_objects.BlockStates.PropertyBoolM;
import com.magiology.mc_objects.BlockStates.PropertyIntegerM;
import com.magiology.util.objs.AngularVec3;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.GeometryUtil;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;

import joptsimple.internal.Objects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiBlockBounds implements IBlockBounds{
	
	public interface BoxActivator{
		
		void prepare(IBlockState state, IBlockAccess source, BlockPos pos);
		
		boolean isActive(int id);
		
		default boolean active(MultiBlockBounds parent, int id){
			return parent.constants[id]||isActive(id);
		}
	}
	
	private static class StateData{
		
		@SideOnly(Side.CLIENT)
		private int				drawModel	=-1;
		private AxisAlignedBB	union;
		
		public StateData(AxisAlignedBB union){
			this.union=union;
		}
		
		private static class BoxLine{
			PairM<Vec3M, Vec3M> rawLine;
			Vec3i vec;
			private static final double EXPAND=0.0020000000949949026D;
			public BoxLine(Vec3M v1, Vec3M v2, Vec3i vec){
				rawLine=new PairM<>(v1, v2);
				this.vec=vec;
			}
			
			private PairM<Vec3M, Vec3M> actualLine(){
				int x=vec.getX(),y=vec.getY(),z=vec.getZ();
				return new PairM<>(
					rawLine.obj1.add(x==0?-EXPAND:EXPAND*x, y==0?-EXPAND:EXPAND*y, z==0?-EXPAND:EXPAND*z),
					rawLine.obj2.add(x==0? EXPAND:EXPAND*x, y==0? EXPAND:EXPAND*y, z==0? EXPAND:EXPAND*z)
				);
			}
			@Override
			public int hashCode(){
				return rawLine.hashCode();
			}
		}
		/**
		 * Compiles a model of block highlight with a precise outline from a set of boxes. <b>WARNING:</b> CPU expensive (model optimizing) and garbage inefficient
		 * @param boxes = bounding boxes that will be outlined
		 */
		@SideOnly(Side.CLIENT)
		void createModel(List<AxisAlignedBB> boxes){
			
			//init values
			List<BoxLine> lines=new ArrayList<>();
			final Vec3i
				EAST_UP   =new Vec3i( 1, 1, 0),
				WEST_UP   =new Vec3i(-1, 1, 0),
				EAST_DOWN =new Vec3i( 1,-1, 0),
				WEST_DOWN =new Vec3i(-1,-1, 0),
				
				UP_SOUTH  =new Vec3i( 0, 1, 1),
				UP_NORTH  =new Vec3i( 0, 1,-1),
				DOWN_SOUTH=new Vec3i( 0,-1, 1),
				DOWN_NORTH=new Vec3i( 0,-1,-1),
				
				EAST_SOUTH=new Vec3i( 1, 0, 1),
				EAST_NORTH=new Vec3i( 1, 0,-1),
				WEST_SOUTH=new Vec3i(-1, 0, 1),
				WEST_NORTH=new Vec3i(-1, 0,-1);
			
			//smart line placing from bounding boxes
			boxes.forEach(box->{
				//is a box in contact on side:
				boolean down=false,up=false,north=false,south=false,west=false,east=false;
				
				for(AxisAlignedBB test:boxes){
					//check if boxes are the same on axis
					boolean xEqual=box.minX==test.minX&&box.maxX==test.maxX;
					boolean yEqual=box.minY==test.minY&&box.maxY==test.maxY;
					boolean zEqual=box.minZ==test.minZ&&box.maxZ==test.maxZ;
					
					//contact counts only if box is the same on 2 other axis
					if(yEqual&&zEqual){
						if(box.maxX==test.minX)east =true;
						if(box.minX==test.maxX)west =true;
					}
					if(xEqual&&zEqual){
						if(box.maxY==test.minY)up   =true;
						if(box.minY==test.maxY)down =true;
					}
					if(xEqual&&yEqual){
						if(box.maxZ==test.minZ)south=true;
						if(box.minZ==test.maxZ)north=true;
					}
				}
				
				//apply lines only if detected contact is the same on both sides
				//(if side 1 is false and side 2 is true or reverse than that is a flat surface. no need for a line)
				if(east ==   up)lines.add(new BoxLine(new Vec3M(box.maxX,box.maxY,box.minZ),new Vec3M(box.maxX,box.maxY,box.maxZ),EAST_UP   ));
				if(west ==   up)lines.add(new BoxLine(new Vec3M(box.minX,box.maxY,box.minZ),new Vec3M(box.minX,box.maxY,box.maxZ),WEST_UP   ));
				if(east == down)lines.add(new BoxLine(new Vec3M(box.maxX,box.minY,box.minZ),new Vec3M(box.maxX,box.minY,box.maxZ),EAST_DOWN ));
				if(west == down)lines.add(new BoxLine(new Vec3M(box.minX,box.minY,box.minZ),new Vec3M(box.minX,box.minY,box.maxZ),WEST_DOWN ));

				if(up   ==south)lines.add(new BoxLine(new Vec3M(box.minX,box.maxY,box.maxZ),new Vec3M(box.maxX,box.maxY,box.maxZ),UP_SOUTH  ));
				if(up   ==north)lines.add(new BoxLine(new Vec3M(box.minX,box.maxY,box.minZ),new Vec3M(box.maxX,box.maxY,box.minZ),UP_NORTH  ));
				if(down ==south)lines.add(new BoxLine(new Vec3M(box.minX,box.minY,box.maxZ),new Vec3M(box.maxX,box.minY,box.maxZ),DOWN_SOUTH));
				if(down ==north)lines.add(new BoxLine(new Vec3M(box.minX,box.minY,box.minZ),new Vec3M(box.maxX,box.minY,box.minZ),DOWN_NORTH));

				if(east ==south)lines.add(new BoxLine(new Vec3M(box.maxX,box.minY,box.maxZ),new Vec3M(box.maxX,box.maxY,box.maxZ),EAST_SOUTH));
				if(east ==north)lines.add(new BoxLine(new Vec3M(box.maxX,box.minY,box.minZ),new Vec3M(box.maxX,box.maxY,box.minZ),EAST_NORTH));
				if(west ==south)lines.add(new BoxLine(new Vec3M(box.minX,box.minY,box.maxZ),new Vec3M(box.minX,box.maxY,box.maxZ),WEST_SOUTH));
				if(west ==north)lines.add(new BoxLine(new Vec3M(box.minX,box.minY,box.minZ),new Vec3M(box.minX,box.maxY,box.minZ),WEST_NORTH));
			});
			
			//try merging and removing lines in a way that it does not change the look of the outline
			List<BoxLine> optimizedOut=new ArrayList<>();
			
			lines.forEach(line->{
				if(optimizedOut.contains(line))return;
				lines.forEach(test->{
					if(line==test||optimizedOut.contains(test))return;
					
					boolean line1test2=line.rawLine.obj1.equals(test.rawLine.obj2);
					boolean line2test1=line.rawLine.obj2.equals(test.rawLine.obj1);

					
					//are they flipped aka visually equal?
					if(line1test2&&line2test1){
						//no need to have 2 same lines
						optimizedOut.add(test);
						return;
					}
					
					//are they connected?
					if(line1test2||line2test1){
						AngularVec3 ang1=new AngularVec3(line.rawLine.obj1.sub(line.rawLine.obj2));
						AngularVec3 ang2=new AngularVec3(test.rawLine.obj1.sub(test.rawLine.obj2));
						//are they in an exact same angle?
						if(ang1.getXRotation()==ang2.getXRotation()&&ang1.getYRotation()==ang2.getYRotation()){
							//than they can be merged!
							if(line2test1)line.rawLine.obj2=test.rawLine.obj2;
							else line.rawLine.obj1=test.rawLine.obj1;
							//remove line that is merged from
							optimizedOut.add(test);
							return;
						}
						
					}
				});
				
			});
			lines.removeAll(optimizedOut);
			
			//DEBUG: in case of needed refresh of model what should not occur normally
			if(drawModel!=-1)OpenGLM.glDeleteLists(drawModel, 1);
			
			//set up list building mode
			drawModel=GLAllocation.generateDisplayLists(1);
			GlStateManager.glNewList(drawModel, 4864);
			
			//finally put calculated lines to model
			Renderer.LINES.begin();
			lines.forEach(lineObj->{
				PairM<Vec3M, Vec3M> line=lineObj.actualLine();
				Renderer.LINES.addVertex(line.obj1);
				Renderer.LINES.addVertex(line.obj2);
			});
			Renderer.LINES.draw();
			
			//end list building mode
			OpenGLM.glEndList();
			
		}
		
		@Override
		protected void finalize(){
			if(drawModel!=-1) OpenGLM.glDeleteLists(drawModel, 1);
		}
	}
	
	private boolean						allDataDirty, constants[];
	private final List<AxisAlignedBB>	boxes		=new ArrayList<>();
	private final BoxActivator			activator;
	private final List<StateData>		allStates	=new ArrayList<>();
	
	public MultiBlockBounds(BoxActivator activator, int constants, AxisAlignedBB...boxes){
		setBlockBounds(constants, boxes);
		this.activator=activator;
	}
	
	public void modifyBlockBounds(int id, AxisAlignedBB box, boolean constant){
		Objects.ensureNotNull(box);
		constants[id]=constant;
		boxes.set(id, box);
		markDirty();
	}
	
	public void setBlockBounds(int constants, AxisAlignedBB...boxes){
		Objects.ensureNotNull(boxes);
		if(boxes.length==0) throw new IllegalArgumentException("Boxes are empty!");
		
		markDirty();
		
		this.boxes.clear();
		for(AxisAlignedBB box:boxes){
			Objects.ensureNotNull(box);
			this.boxes.add(box);
		}
		this.constants=new boolean[boxes.length];
		for(int i=0;i<boxes.length;i++){
			this.constants[i]=((constants>>i)&1)==1;
		}
	}
	
	private void markDirty(){
		allDataDirty=true;
	}
	
	private int getStateId(IBlockState state, IBlockAccess source, BlockPos pos){
		if(allDataDirty) compileData();
		
		activator.prepare(state, source, pos);
		int id=0;
		
		for(int i=0;i<boxes.size();i++){
			if(!constants[i]&&activator.isActive(i)) id|=1<<i;
		}
		
		return id;
	}
	
	private List<AxisAlignedBB> getActiveBoxes(IBlockState state, IBlockAccess source, BlockPos pos){
		List<AxisAlignedBB> result=new ArrayList<>();
		activator.prepare(state, source, pos);
		for(int i=0;i<boxes.size();i++){
			if(activator.active(this, i)) result.add(boxes.get(i));
		}
		return result;
	}
	
	private void compileData(){
		allDataDirty=false;
		allStates.clear();
		List<AxisAlignedBB> dynamics=new ArrayList<>(), alwaysOn=new ArrayList<>();
		
		for(int i=0;i<boxes.size();i++){
			(constants[i]?alwaysOn:dynamics).add(boxes.get(i));
		}
		
		int size=(int)Math.pow(2, dynamics.size());
		LogUtil.println(size);
		
		for(int binaryCombination=0;binaryCombination<size;binaryCombination++){
			AxisAlignedBB union=null;
			
			for(int j=0;j<dynamics.size();j++){
				if(((binaryCombination>>j)&1)==1){
					if(union==null) union=dynamics.get(j);
					else union=union.union(dynamics.get(j));
				}
			}
			for(AxisAlignedBB box:alwaysOn){
				if(union==null) union=box;
				else union=union.union(box);
			}
			
			if(union==null) union=new AxisAlignedBB(0, 0, 0, 0, 0, 0);
			allStates.add(new StateData(union));
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return allStates.get(getStateId(state, source, pos)).union;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity){
		getActiveBoxes(state, world, pos).forEach(box->addCollisionBox(pos, entityBox, collidingBoxes, box));
	}
	
	private static final List<Hit> hits=new ArrayList<>();
	
	private class Hit{
		
		RayTraceResult	h;
		double			d;
		
		public Hit(RayTraceResult h, double d){
			this.h=h;
			this.d=d;
		}
	}
	
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end){
		hits.clear();
		
		getActiveBoxes(state, world, pos).forEach(box->{
			RayTraceResult hit=rayTrace(pos, start, end, box);
			if(hit!=null) hits.add(new Hit(hit, hit.hitVec.distanceTo(start)));
		});
		
		if(hits.isEmpty()) return null;
		
		Hit hit=hits.stream().max((hit1, hit2)->hit1.d<hit2.d?-1:hit1.d==hit2.d?0:1).orElse(null);
		hits.clear();
		
		return hit.h;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBoundsOutline(IBlockState state, World world, BlockPos pos){
		StateData data=allStates.get(getStateId(state, world, pos));
		if(data.drawModel==-1) data.createModel(getActiveBoxes(state, world, pos));
		
		OpenGLM.callList(data.drawModel);
	}
	
	public static class PipeStyleBlockBounds extends MultiBlockBounds{
		
		public PipeStyleBlockBounds(BooleanSupplier[] sides, int constants, double size){
			super(new BoxActivator(){
				
				@Override
				public void prepare(IBlockState state, IBlockAccess source, BlockPos pos){}
				
				@Override
				public boolean isActive(int id){
					if(id==6) return true;
					return sides[id].getAsBoolean();
				}
			}, constants, GeometryUtil.generatePipeSyleBoxes(size));
		}
		
		public PipeStyleBlockBounds(PropertyBoolM[] sides, PropertyIntegerM straight, int constants, double size){
			super(new BoxActivator(){
				
				IBlockState state;
				
				@Override
				public void prepare(IBlockState state, IBlockAccess source, BlockPos pos){
					this.state=state;
				}
				
				@Override
				public boolean isActive(int id){
					if(id==6) return true;
					return sides[id].get(state)||straight.get(state)==id/2;
				}
			}, constants, GeometryUtil.generatePipeSyleBoxes(size));
		}
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
}
