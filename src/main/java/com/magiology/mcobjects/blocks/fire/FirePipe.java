package com.magiology.mcobjects.blocks.fire;

import com.magiology.mcobjects.blocks.MultiColisionProviderBlock;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FirePipe extends MultiColisionProviderBlock{
	
	public FirePipe(){
		super(Material.iron);
		float p= 1F/16F;
		setHardness(10F).setHarvestLevel("pickaxe", 1);
		setBlockBounds(p*6, p*6, p*6, p*10, p*10, p*10);
		useNeighborBrightness=true;
	}

	@Override
	public TileEntity createNewTileEntity(World var0, int var1){
		return new TileEntityFirePipe();
	}
	
//	@Override
//	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float xHit, float yHit, float zHit){
//		return super.onBlockActivated(world,pos,state,player,side,xHit,yHit,zHit);
//		TileEntity test=world.getTileEntity(pos);
//		boolean return1=false;
//		
//		if(test instanceof TileEntityFirePipe){
//			TileEntityFirePipe tile1=(TileEntityFirePipe) test;
//			
//			if(player!=null){
//				if(UtilM.isItemInStack(MItems.fireHammer, player.getHeldItem())){
//					return1=true;
//					TileEntity tile2 = null;
//					double a=0.001;
//					
//					{
//						List<ColisionBox> boxes=MultiColisionProviderHandler.getBoxesOnSide(tile1,EnumFacing.getFront(1));
//						for(ColisionBox box:boxes){z
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit))){
//								tile1.connections[1].setBanned(tile1.connections[1].getMain());
//								tile2=world.getTileEntity(pos.add(0,-1,0));
//								if(tile2 instanceof ISidedPower)((ISidedPower)tile2).setBannedSide(tile1.connections[1].getMain(), 0);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBBM> boxes=new ArrayList<AxisAlignedBBM>();
//						tile1.getExpectedBoxesOnSide(boxes, 0);
//						for(AxisAlignedBBM box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit))){
//								tile1.connections[0].setBanned(tile1.connections[0].getMain());
//								tile2=world.getTileEntity(pos.add(0,1,0));
//								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[0].getMain(), 1);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBBM> boxes=new ArrayList<AxisAlignedBBM>();
//						tile1.getExpectedBoxesOnSide(boxes, 5);
//						for(AxisAlignedBBM box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit))){
//								tile1.connections[5].setBanned(tile1.connections[5].getMain());
//								tile2=world.getTileEntity(pos.add(-1,0,0));
//								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[5].getMain(), 3);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBBM> boxes=new ArrayList<AxisAlignedBBM>();
//						tile1.getExpectedBoxesOnSide(boxes, 3);
//						for(AxisAlignedBBM box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit))){
//								tile1.connections[3].setBanned(tile1.connections[3].getMain());
//								tile2=world.getTileEntity(pos.add(1,0,0));
//								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[3].getMain(), 5);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBBM> boxes=new ArrayList<AxisAlignedBBM>();
//						tile1.getExpectedBoxesOnSide(boxes, 2);
//						for(AxisAlignedBBM box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit))){
//								tile1.connections[2].setBanned(tile1.connections[2].getMain());
//								tile2=world.getTileEntity(pos.add(0,0,-1));
//								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[2].getMain(), 4);
//								continue;
//							}
//						}
//					}
//					{
//						List<AxisAlignedBBM> boxes=new ArrayList<AxisAlignedBBM>();
//						tile1.getExpectedBoxesOnSide(boxes, 4);
//						for(AxisAlignedBBM box:boxes){
//							if(box.expand(a, a, a).isVecInside(new Vec3M(xHit, yHit, zHit).conv())){
//								tile1.connections[4].setBanned(tile1.connections[4].getMain());
//								tile2=world.getTileEntity(pos.add(0,0,-1));
//								if(tile2 instanceof TileEntityFirePipe)((TileEntityFirePipe)tile2).setBannedSide(tile1.connections[4].getMain(), 2);
//								continue;
//							}
//						}
//					}
//					UpdateablePipeHandler.updatein3by3(world, pos);
//				}
//			}
//		}
//		return return1;
//	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
}