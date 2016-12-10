package com.magiology.client.rendering.highlight.types;

import java.util.ArrayList;
import java.util.List;

import com.magiology.client.renderers.Renderer;
import com.magiology.client.rendering.highlight.BlockHighlightRenderer;
import com.magiology.util.objs.AngularVec3;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.block_bounds.MultiBlockBounds;
import com.magiology.util.objs.block_bounds.MultiBlockBounds.StateData;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.OpenGLM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class MultiHighlightRenderer extends BlockHighlightRenderer<MultiBlockBounds>{
	

	private static final Vec3i
			EAST_UP	=new Vec3i(1, 1, 0),
			WEST_UP=new Vec3i(-1, 1, 0),
			EAST_DOWN=new Vec3i(1, -1, 0),
			WEST_DOWN=new Vec3i(-1, -1, 0),
			
			UP_SOUTH=new Vec3i(0, 1, 1),
			UP_NORTH=new Vec3i(0, 1, -1),
			DOWN_SOUTH=new Vec3i(0, -1, 1),
			DOWN_NORTH=new Vec3i(0, -1, -1),
			
			EAST_SOUTH=new Vec3i(1, 0, 1),
			EAST_NORTH=new Vec3i(1, 0, -1),
			WEST_SOUTH=new Vec3i(-1, 0, 1),
			WEST_NORTH=new Vec3i(-1, 0, -1);
	private static final double	EXPAND=0.0020000000949949026D;
	
	@SideOnly(Side.CLIENT)
	private static class BoxLine{
		
		PairM<Vec3M, Vec3M>	rawLine;
		Vec3i				vec;
		
		public BoxLine(Vec3M v1, Vec3M v2, Vec3i vec){
			rawLine=new PairM<>(v1, v2);
			this.vec=vec;
		}
		
		private PairM<Vec3M, Vec3M> actualLine(){
			int x=vec.getX(), y=vec.getY(), z=vec.getZ();
			return new PairM<>(
					rawLine.obj1.add(x==0?-EXPAND:EXPAND*x, y==0?-EXPAND:EXPAND*y, z==0?-EXPAND:EXPAND*z),
					rawLine.obj2.add(x==0?EXPAND:EXPAND*x, y==0?EXPAND:EXPAND*y, z==0?EXPAND:EXPAND*z));
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
	private void createModel(StateData data){
		List<AxisAlignedBB> boxes=data.boxes;
		//init values
		List<BoxLine> lines=new ArrayList<>();
		
		//smart line placing from bounding boxes
		boxes.forEach(box->{
			//is a box in contact on side:
			boolean down=false, up=false, north=false, south=false, west=false, east=false;
			
			for(AxisAlignedBB test:boxes){
				//check if boxes are the same on axis
				boolean xEqual=box.minX==test.minX&&box.maxX==test.maxX;
				boolean yEqual=box.minY==test.minY&&box.maxY==test.maxY;
				boolean zEqual=box.minZ==test.minZ&&box.maxZ==test.maxZ;
				
				//contact counts only if box is the same on 2 other axis
				if(yEqual&&zEqual){
					if(box.maxX==test.minX) east=true;
					if(box.minX==test.maxX) west=true;
				}
				if(xEqual&&zEqual){
					if(box.maxY==test.minY) up=true;
					if(box.minY==test.maxY) down=true;
				}
				if(xEqual&&yEqual){
					if(box.maxZ==test.minZ) south=true;
					if(box.minZ==test.maxZ) north=true;
				}
			}
			
			//apply lines only if detected contact is the same on both sides
			//(if side 1 is false and side 2 is true or reverse than that is a flat surface. no need for a line)
			if(east==up) lines.add(new BoxLine(new Vec3M(box.maxX, box.maxY, box.minZ), new Vec3M(box.maxX, box.maxY, box.maxZ), EAST_UP));
			if(west==up) lines.add(new BoxLine(new Vec3M(box.minX, box.maxY, box.minZ), new Vec3M(box.minX, box.maxY, box.maxZ), WEST_UP));
			if(east==down) lines.add(new BoxLine(new Vec3M(box.maxX, box.minY, box.minZ), new Vec3M(box.maxX, box.minY, box.maxZ), EAST_DOWN));
			if(west==down) lines.add(new BoxLine(new Vec3M(box.minX, box.minY, box.minZ), new Vec3M(box.minX, box.minY, box.maxZ), WEST_DOWN));
			
			if(up==south) lines.add(new BoxLine(new Vec3M(box.minX, box.maxY, box.maxZ), new Vec3M(box.maxX, box.maxY, box.maxZ), UP_SOUTH));
			if(up==north) lines.add(new BoxLine(new Vec3M(box.minX, box.maxY, box.minZ), new Vec3M(box.maxX, box.maxY, box.minZ), UP_NORTH));
			if(down==south) lines.add(new BoxLine(new Vec3M(box.minX, box.minY, box.maxZ), new Vec3M(box.maxX, box.minY, box.maxZ), DOWN_SOUTH));
			if(down==north) lines.add(new BoxLine(new Vec3M(box.minX, box.minY, box.minZ), new Vec3M(box.maxX, box.minY, box.minZ), DOWN_NORTH));
			
			if(east==south) lines.add(new BoxLine(new Vec3M(box.maxX, box.minY, box.maxZ), new Vec3M(box.maxX, box.maxY, box.maxZ), EAST_SOUTH));
			if(east==north) lines.add(new BoxLine(new Vec3M(box.maxX, box.minY, box.minZ), new Vec3M(box.maxX, box.maxY, box.minZ), EAST_NORTH));
			if(west==south) lines.add(new BoxLine(new Vec3M(box.minX, box.minY, box.maxZ), new Vec3M(box.minX, box.maxY, box.maxZ), WEST_SOUTH));
			if(west==north) lines.add(new BoxLine(new Vec3M(box.minX, box.minY, box.minZ), new Vec3M(box.minX, box.maxY, box.minZ), WEST_NORTH));
		});
		//boxes are not needed anymore.
		
		//try merging and removing lines in a way that it does not change the look of the outline
		List<BoxLine> optimizedOut=new ArrayList<>();
		
		lines.forEach(line->{
			if(optimizedOut.contains(line)) return;
			lines.forEach(test->{
				if(line==test||optimizedOut.contains(test)) return;
				
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
						if(line2test1) line.rawLine.obj2=test.rawLine.obj2;
						else line.rawLine.obj1=test.rawLine.obj1;
						//remove line that is merged from
						optimizedOut.add(test);
						return;
					}
					
				}
			});
			
		});
		lines.removeAll(optimizedOut);
		
		//set up list building mode
		data.setDrawModel(GLAllocation.generateDisplayLists(1));
		GlStateManager.glNewList(data.getDrawModel(), 4864);
		
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
	
	
	public MultiHighlightRenderer(MultiBlockBounds owner){
		super(owner);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBoundsOutline(IBlockState state, World world, BlockPos pos, RayTraceResult hit){
		StateData data=getOwner().getStateData(state, world, pos);
		if(data.getDrawModel()==-1)createModel(data);
		OpenGLM.callList(data.getDrawModel());
	}
	
	@Override
	public void markDirty(){
		for(StateData box:getOwner().allStates){
			box.setDrawModel(-1);
		}
	}
}
