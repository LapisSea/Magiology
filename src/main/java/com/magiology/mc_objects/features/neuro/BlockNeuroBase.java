package com.magiology.mc_objects.features.neuro;

import com.magiology.util.interf.ObjectReturn;
import com.magiology.util.m_extensions.BlockContainerM;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.BlockStates;
import com.magiology.util.objs.BlockStates.IPropertyM;
import com.magiology.util.objs.BlockStates.PropertyBoolM;
import com.magiology.util.objs.BlockStates.PropertyIntegerM;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockNeuroBase<T extends TileEntityM&NeuroPart> extends BlockContainerM<T>{
	
	public static final PropertyBoolM CONNECTIONS[], HAS_CONTROLLER=BlockStates.booleanProp("has_controller");
	public static final PropertyIntegerM STRAIGHT=BlockStates.intProp("straight", 0, 3);
	
	static{
		CONNECTIONS=new PropertyBoolM[6];
		for(int i=0; i<6; i++) CONNECTIONS[i]=BlockStates.booleanProp(EnumFacing.getFront(i).toString());
	}
	
	protected BlockNeuroBase(Material material, ObjectReturn<T> tileFactory, IPropertyM[] properties){
		super(material, tileFactory, properties);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof NeuroPart){
			NeuroPart part=(NeuroPart)tile;
			TileEntityNeuroController controller=part.getController();
			if(controller!=null){
				controller.requestConnectedRefresh();
				if(!controller.isInWorld()) part.setController(null);
				else{
					if(controller.getParts().indexOf(part)==-1) part.setController(null);
				}
			}
			//			else{
			//				NeuroPart signOfCtrl=part.getConnected().stream().filter(p->{
			//
			//					NeuroPart self=p.getSelf();
			//					if(self!=null)LogUtil.println(self.getController());
			//
			//					return self!=null&&self.hasController()&&ISidedConnection.handshake(part, p);
			//
			//				}).findFirst().orElse(null);
			//
			//				if(signOfCtrl!=null){
			//					controller=signOfCtrl.getController();
			//
			//					TileEntity t=(TileEntity)signOfCtrl;
			//					world.notifyBlockOfStateChange(t.getPos(), t.getBlockType());
			//					part.setController(controller);
			//				}
			//			}
		}
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side){
		return false;
	}
}
