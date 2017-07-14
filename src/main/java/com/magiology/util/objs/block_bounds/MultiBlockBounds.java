package com.magiology.util.objs.block_bounds;

import com.magiology.client.rendering.highlight.BlockHighlightRenderer;
import com.magiology.client.rendering.highlight.types.MultiHighlightRenderer;
import com.magiology.util.objs.BlockStates.PropertyBoolM;
import com.magiology.util.objs.BlockStates.PropertyIntegerM;
import com.magiology.util.objs.PairM;
import com.magiology.util.statics.CollectionConverter;
import com.magiology.util.statics.GeometryUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilM;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public class MultiBlockBounds implements IBlockBounds{
	
	public interface BoxHandler{
		
		void prepare(IBlockState state, IBlockAccess source, BlockPos pos);
		
		boolean isActive(int id);
		
		default boolean active(MultiBlockBounds parent, int id){
			return parent.constants[id]||isActive(id);
		}
		
		void boxHighlighted(RayTraceResult hit, World world);
	}
	
	public static class StateData{
		
		@SideOnly(Side.CLIENT)
		private int drawModel=-1;
		private final AxisAlignedBB       union;
		public final  List<AxisAlignedBB> boxes;
		public final  int[]               boxIds;
		
		public StateData(List<PairM<AxisAlignedBB,Integer>> boxes){
			this.boxes=CollectionConverter.convLi(boxes, AxisAlignedBB.class, p->p.obj1);
			
			boxIds=new int[boxes.size()];
			for(int i=0; i<boxes.size(); i++){
				boxIds[i]=boxes.get(i).obj2;
			}
			
			if(boxes.isEmpty()) union=new AxisAlignedBB(0, 0, 0, 0, 0, 0);
			else{
				AxisAlignedBB union=this.boxes.get(0);
				for(int i=1; i<boxes.size(); i++)
					union=union.union(this.boxes.get(i));
				this.union=union;
			}
		}
		
		@SideOnly(Side.CLIENT)
		public int getDrawModel(){
			return drawModel;
		}
		
		@SideOnly(Side.CLIENT)
		public void setDrawModel(int drawModel){
			if(this.drawModel==drawModel) return;
			if(drawModel!=-1) OpenGLM.glDeleteLists(this.drawModel, 1);
			this.drawModel=drawModel;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		protected void finalize(){
			if(drawModel!=-1) OpenGLM.glDeleteLists(drawModel, 1);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private BlockHighlightRenderer renderer=new MultiHighlightRenderer(this);
	
	private boolean allDataDirty, constants[];
	private List<AxisAlignedBB> boxes=new ArrayList<>();
	private final BoxHandler handler;
	public final List<StateData> allStates=new ArrayList<>();
	
	public MultiBlockBounds(BoxHandler activator, int constants, AxisAlignedBB... boxes){
		setBlockBounds(constants, boxes);
		handler=activator;
	}
	
	public AxisAlignedBB getBox(int id){
		return boxes.get(id);
	}
	
	public void modifyBlockBounds(int id, AxisAlignedBB box, boolean constant){
		Objects.requireNonNull(box);
		constants[id]=constant;
		boxes.set(id, box);
		markDirty();
	}
	
	public void setBlockBounds(int constants, AxisAlignedBB... boxes){
		Objects.requireNonNull(boxes);
		if(boxes.length==0) throw new IllegalArgumentException("Boxes are empty!");
		
		markDirty();
		
		this.boxes.clear();
		for(AxisAlignedBB box : boxes){
			Objects.requireNonNull(box);
			this.boxes.add(box);
		}
		this.constants=new boolean[boxes.length];
		for(int i=0; i<boxes.length; i++){
			this.constants[i]=((constants>>i)&1)==1;
		}
	}
	
	private void markDirty(){
		allDataDirty=true;
	}
	
	private int getStateId(IBlockState state, IBlockAccess source, BlockPos pos){
		if(allDataDirty) compileData(source instanceof World?((World)source).isRemote:UtilM.isRemote());
		
		handler.prepare(state, source, pos);
		int id=0;
		
		for(int i=0; i<boxes.size(); i++){
			if(!constants[i]&&handler.isActive(i)) id|=1<<i;
		}
		
		return id;
	}
	
	public StateData getStateData(IBlockState state, IBlockAccess source, BlockPos pos){
		return getStateData(getStateId(state, source, pos));
	}
	
	private StateData getStateData(int id){
		return allStates.get(id);
	}
	
	private List<AxisAlignedBB> getActiveBoxes(IBlockState state, IBlockAccess source, BlockPos pos){
		return getStateData(state, source, pos).boxes;
	}
	
	private void compileData(boolean remote){
		allDataDirty=false;
		allStates.clear();
		List<PairM<AxisAlignedBB,Integer>> dynamics=new ArrayList<>(), alwaysOn=new ArrayList<>();
		List<Integer> dynamicIds=new ArrayList<>(), alwaysOnIds=new ArrayList<>();
		
		for(int i=0; i<boxes.size(); i++){
			(constants[i]?alwaysOn:dynamics).add(new PairM<>(boxes.get(i), i));
		}
		
		int size=(int)Math.pow(2, dynamics.size());
		
		for(int binaryCombination=0; binaryCombination<size; binaryCombination++){
			List<PairM<AxisAlignedBB,Integer>> boxes=new ArrayList<>();
			
			for(int j=0; j<dynamics.size(); j++){
				if(((binaryCombination>>j)&1)==1){
					boxes.add(dynamics.get(j));
				}
			}
			boxes.addAll(alwaysOn);
			
			allStates.add(new StateData(boxes));
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return getStateData(state, source, pos).union;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity){
		getActiveBoxes(state, world, pos).forEach(box->addCollisionBox(pos, entityBox, collidingBoxes, box));
	}
	
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end){
		
		int id=getStateId(state, world, pos);
		StateData data=getStateData(id);
		
		RayTraceResult hit=GeometryUtil.rayTrace(data.boxes, start, end, pos);
		
		if(hit==null) return null;
		
		hit.subHit=data.boxIds[hit.subHit];
		
		handler.boxHighlighted(hit, world);
		return hit;
	}
	
	public static class PipeStyleBlockBounds extends MultiBlockBounds{
		
		private static final BoxHitEvent NULL_LISTENER=(hit, world)->{};
		
		public PipeStyleBlockBounds(BooleanSupplier[] sides, int constants, double size){
			this(sides, constants, size, NULL_LISTENER);
		}
		
		public PipeStyleBlockBounds(BooleanSupplier[] sides, int constants, double size, BoxHitEvent boxHighlighted){
			super(new BoxHandler(){
				
				@Override
				public void prepare(IBlockState state, IBlockAccess source, BlockPos pos){}
				
				@Override
				public boolean isActive(int id){
					return sides[id].getAsBoolean();
				}
				
				@Override
				public void boxHighlighted(RayTraceResult hit, World world){
					boxHighlighted.onEvent(hit, world);
				}
			}, constants, GeometryUtil.generatePipeSyleBoxes(size));
		}
		
		public PipeStyleBlockBounds(PropertyBoolM[] sides, PropertyIntegerM straight, int constants, double size){
			this(sides, straight, constants, size, NULL_LISTENER);
		}
		
		public interface BoxHitEvent{
			
			void onEvent(RayTraceResult hit, World world);
			
		}
		
		public PipeStyleBlockBounds(PropertyBoolM[] sides, PropertyIntegerM straight, int constants, double size, BoxHitEvent boxHighlighted){
			super(new BoxHandler(){
				
				IBlockState state;
				
				@Override
				public void prepare(IBlockState state, IBlockAccess source, BlockPos pos){
					this.state=state;
				}
				
				@Override
				public boolean isActive(int id){
					return sides[id].get(state)||straight.get(state)==id/2;
				}
				
				@Override
				public void boxHighlighted(RayTraceResult hit, World world){
					boxHighlighted.onEvent(hit, world);
				}
			}, constants, GeometryUtil.generatePipeSyleBoxes(size));
		}
		
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockHighlightRenderer getHighlightRenderer(){
		return renderer;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setHighlightRenderer(BlockHighlightRenderer renderer){
		this.renderer=renderer;
	}
	
}
