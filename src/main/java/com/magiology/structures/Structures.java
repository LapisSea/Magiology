package com.magiology.structures;

import com.magiology.core.init.MBlocks;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Structures{
	
	public static Structure generateNewStructure(int id){
		SymmetryBoot sb=new SymmetryBoot();
		if(id==1){
			Structure ParticleLauncher=new Structure();
			sb.symmetryBoot(new BlockAt(Blocks.lapis_block, 1, 2, 1), 3, ParticleLauncher);
			sb.symmetryBoot(new BlockAt(Blocks.obsidian, 1, 1, 1), 3, ParticleLauncher);
			sb.symmetryBoot(new BlockAt(Blocks.iron_block, 1, 0, 1), 3, ParticleLauncher);
			sb.symmetryBoot(new BlockAt(Blocks.iron_block, 1, 0, 0), 3, ParticleLauncher);
			return ParticleLauncher.initializeStructure();
		}
		if(id==2){
			Structure OreStructureCore=new Structure();
			sb.symmetryBoot(new BlockAt(Blocks.obsidian,-1, 0, 0),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.obsidian,-2, 0, 0),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.obsidian,-3, 0, 0),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.obsidian,-4, 0, 0),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-4, 0, 1),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-2, 0, 1),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-3, 0, 1),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-4, 0,-2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-4, 0,-3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.obsidian,-5, 0, 1),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.obsidian,-6, 0, 2),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.iron_block,-7, 0, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.iron_block,-7, 0,-3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-5, 0, 0),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-5, 0, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-6, 0, 1),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-6, 0, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-7, 0, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-7, 0, 4),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-8, 0, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-8, 0, 4),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-7,-1, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick,-7,-2, 3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.obsidian, 1, 0, 1),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 2, 0, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3, 0, 3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.glowstone, 3, 0, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.glowstone, 2, 0, 3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3, 1, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3, 2, 3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.iron_bars, 2, 1, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.iron_bars, 3, 1, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.iron_bars, 2, 2, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.iron_bars, 3, 2, 2),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.vine, 4, 1, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.vine, 3, 1, 4),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.vine, 4, 2, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.vine, 3, 2, 4),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(MBlocks.fireLamp, 3, 3, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(MBlocks.fireLamp, 3, 4, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(MBlocks.fireLamp, 2, 4, 3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3, 3, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 2, 3, 3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 0, 4, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 1, 4, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3, 4, 1),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 2, 4, 2),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.stained_glass, 2, 4, 1),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stained_glass, 2, 4, 0),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stained_glass, 1, 4, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stained_glass, 1, 4, 0),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stained_glass, 1, 4, 1),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.iron_bars, 3, 3, 1),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.iron_bars, 2, 3, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.iron_bars, 1, 3, 3),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.air, 1, 1, 0),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.air, 2, 1, 0),3,OreStructureCore);
			OreStructureCore.BlocksAtInit.add(new BlockAt(Blocks.air, 0, 2, 0));
			OreStructureCore.BlocksAtInit.add(new BlockAt(Blocks.air, 0, 3, 0));
			
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 1, 0, 2),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 1, 0, 3),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 1, 0, 4),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 2, 0, 4),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3, 0, 4),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 2, 0, 5),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3, 0, 6),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 4, 0, 7),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 1, 0, 6),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 2, 0, 7),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3, 0, 8),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 4, 0, 8),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3,-1, 7),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.stonebrick, 3,-2, 7),3,OreStructureCore);
			
			return OreStructureCore.initializeStructure();
		}
		if(id==3){
			Structure OreStructureCoreDiamondAddon=new Structure();
			sb.symmetryBoot(new BlockAt(Blocks.gold_block,4, 0, 4),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.gold_block,5, 0, 3),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.gold_block,3, 0, 5),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.gold_block,6, 0, 4),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.gold_block,4, 0, 6),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.gold_block,6, 0, 6),3,OreStructureCoreDiamondAddon);
			
			sb.symmetryBoot(new BlockAt(Blocks.bedrock,5, 0, 5),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.diamond_block,4, 0, 5),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.diamond_block,6, 0, 5),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.diamond_block,5, 0, 4),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.diamond_block,5, 0, 6),3,OreStructureCoreDiamondAddon);
			
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick,5, 0, 7),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick,6, 0, 7),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick,7, 0, 5),3,OreStructureCoreDiamondAddon);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick,7, 0, 6),3,OreStructureCoreDiamondAddon);
			
			return OreStructureCoreDiamondAddon.initializeStructure();
		}
		if(id==4){
			Structure OreStructureCoreBedrockAddon=new Structure();
			sb.symmetryBoot(new BlockAt(Blocks.bedrock, 6, 0, 0),3,OreStructureCoreBedrockAddon);
			sb.symmetryBoot(new BlockAt(Blocks.bedrock, 7, 0, 0),3,OreStructureCoreBedrockAddon);
			sb.symmetryBoot(new BlockAt(Blocks.diamond_block, 7, 0, 1),3,OreStructureCoreBedrockAddon);
			sb.symmetryBoot(new BlockAt(Blocks.diamond_block, 1, 0, 7),3,OreStructureCoreBedrockAddon);
			sb.symmetryBoot(new BlockAt(Blocks.diamond_block, 8, 0, 0),3,OreStructureCoreBedrockAddon);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 8, 0, 1),3,OreStructureCoreBedrockAddon);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 8, 0, 2),3,OreStructureCoreBedrockAddon);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 1, 0, 8),3,OreStructureCoreBedrockAddon);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 2, 0, 8),3,OreStructureCoreBedrockAddon);
			
			return OreStructureCoreBedrockAddon.initializeStructure();
		}
		if(id==5){
			Structure OreStructureCore=new Structure();
			for(int x=0;x<4;x++)for(int y=0;y<4;y++){
				sb.symmetryBoot(new BlockAt(Blocks.iron_block,7+x, 0, 7+y),3,OreStructureCore);
			}
			sb.symmetryBoot(new BlockAt(Blocks.bedrock, 7, 1, 7),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.bedrock,10, 1, 7),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.bedrock, 7, 1, 10),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.bedrock,10, 1, 10),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.bedrock, 7, 2, 7),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.bedrock,10, 2, 7),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.bedrock, 7, 2, 10),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.bedrock,10, 2, 10),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.beacon, 8, 1, 8),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.beacon, 9, 1, 8),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.beacon, 8, 1, 9),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.beacon, 9, 1, 9),3,OreStructureCore);
			
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 5, 0, 8),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 5, 0, 9),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 6, 0, 10),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 8, 0, 5),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick, 9, 0, 5),3,OreStructureCore);
			sb.symmetryBoot(new BlockAt(Blocks.nether_brick,10, 0, 6),3,OreStructureCore);
			
			return OreStructureCore.initializeStructure();
		}
		if(id==6){
			Structure pipeCore=new Structure();
			pipeCore.BlocksAtInit.add(new BlockAt(MBlocks.firePipe, 0, 5, 0));
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,3, 5, 2),3,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,3, 5, 3),3,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,2, 5, 3),3,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,3, 4, 3),3,pipeCore);
			return pipeCore.initializeStructure();
		}
		if(id==7){
			Structure pipeCore=new Structure();
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,1, 5, 0),2,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,2, 5, 0),2,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,2, 5, 1),3,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,3, 5, 1),3,pipeCore);
			return pipeCore.initializeStructure();
		}
		if(id==8){
			Structure pipeCore=new Structure();
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,0, 5, 1),2,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,0, 5, 2),2,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,1, 5, 2),3,pipeCore);
			sb.symmetryBoot(new BlockAt(MBlocks.firePipe,1, 5, 3),3,pipeCore);
			return pipeCore.initializeStructure();
		}
		throw new IllegalStateException("WRONG ID INPUT");
	}
	public static void updateArray(Structure[] array,World world,BlockPos pos){
		if(array!=null)for(Structure s:array)if(s!=null)s.checkForNextBlock(world, pos);
	}
}
