package com.magiology.forge_powered.events;

import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.mc_objects.particles.Particles;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TickEvents{
	
	
	public static TickEvents instance=new TickEvents();
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event){
		if(event.phase!=Phase.START){
			if(!UtilC.getMC().isGamePaused()&&UtilC.isWorldOpen()){
				Vec3M pos=new Vec3M(UtilC.getThePlayer().getPositionEyes(0));
				
				Particles.MISTY_ENERGY.spawn(pos.add(new Vec3M(RandUtil.CRF(10), RandUtil.CRF(10), RandUtil.CRF(10))), new Vec3M(), 0.1F, 0, new ColorF(0.7+RandUtil.RF(0.3),0.4+RandUtil.RF(0.4),0.2+RandUtil.RF(0.1),0.3+RandUtil.CRF(0.7)));
				//if(UtilC.getThePlayer().isSneaking())Particles.MISTY_ENERGY.compileDisplayList();
				ParticleHandler.get().updateParticles();
//				if(UtilC.getWorldTime()%60==0){
//					ShaderHandler.get().load();
//					PrintUtil.println("shaders reloaded");
//				}
			}
			return;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event){
		PartialTicksUtil.partialTicks=event.renderTickTime;
//		if(Mouse.isButtonDown(0)){
//
//			try{
//				Gson b=new Gson();
//				LinkedTreeMap lol=b.fromJson(FileUtil.getFileTxt(new File("../src/main/resources/assets/magiology/models/block/dimension_stabiliser.json")).toString(), LinkedTreeMap.class);
//				List<Vec3M> v=new ArrayList<>();
//				List<Vec2FM> vt=new ArrayList<>();
//				List<int[]> f=new ArrayList<>();
//				for(LinkedTreeMap i:((ArrayList<LinkedTreeMap>)lol.get("elements"))){
//					List<Double> from=(List<Double>)i.get("from"),to=(List<Double>)i.get("to");
//					LinkedTreeMap 
//						faces=(LinkedTreeMap)i.get("faces"),
//						north=(LinkedTreeMap)faces.get("north"),
//						east=(LinkedTreeMap)faces.get("east"),
//						south=(LinkedTreeMap)faces.get("south"),
//						west=(LinkedTreeMap)faces.get("west"),
//						up=(LinkedTreeMap)faces.get("up"),
//						down=(LinkedTreeMap)faces.get("down");
//					
//					if(north!=null){
//						f.add(new int[]{v.size(),v.size()+1,v.size()+2,v.size()+3});
//
//						List<Double> uv=(List<Double>)north.get("uv");
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(1).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(1).floatValue()));
//						
//						v.add(new Vec3M(from.get(0),from.get(1),from.get(2)));
//						v.add(new Vec3M(from.get(0),to  .get(1),from.get(2)));
//						v.add(new Vec3M(to  .get(0),to  .get(1),from.get(2)));
//						v.add(new Vec3M(to  .get(0),from.get(1),from.get(2)));
//					}
//					if(south!=null){
//						f.add(new int[]{v.size(),v.size()+1,v.size()+2,v.size()+3});
//
//						List<Double> uv=(List<Double>)south.get("uv");
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(1).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(1).floatValue()));
//						
//						v.add(new Vec3M(to  .get(0),from.get(1),to  .get(2)));
//						v.add(new Vec3M(to  .get(0),to  .get(1),to  .get(2)));
//						v.add(new Vec3M(from.get(0),to  .get(1),to  .get(2)));
//						v.add(new Vec3M(from.get(0),from.get(1),to  .get(2)));
//					}
//					if(east!=null){
//						f.add(new int[]{v.size(),v.size()+1,v.size()+2,v.size()+3});
//
//						List<Double> uv=(List<Double>)east.get("uv");
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(1).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(1).floatValue()));
//						
//						v.add(new Vec3M(to  .get(0),from.get(1),from.get(2)));
//						v.add(new Vec3M(to  .get(0),to  .get(1),from.get(2)));
//						v.add(new Vec3M(to  .get(0),to  .get(1),to  .get(2)));
//						v.add(new Vec3M(to  .get(0),from.get(1),to  .get(2)));
//					}
//					if(west!=null){
//						f.add(new int[]{v.size(),v.size()+1,v.size()+2,v.size()+3});
//
//						List<Double> uv=(List<Double>)west.get("uv");
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(1).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(1).floatValue()));
//						
//						v.add(new Vec3M(from.get(0),from.get(1),to  .get(2)));
//						v.add(new Vec3M(from.get(0),to  .get(1),to  .get(2)));
//						v.add(new Vec3M(from.get(0),to  .get(1),from.get(2)));
//						v.add(new Vec3M(from.get(0),from.get(1),from.get(2)));
//					}
//					if(down!=null){
//						f.add(new int[]{v.size(),v.size()+1,v.size()+2,v.size()+3});
//
//						List<Double> uv=(List<Double>)down.get("uv");
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(1).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(1).floatValue()));
//						
//						v.add(new Vec3M(from.get(0),from.get(1),from.get(2)));
//						v.add(new Vec3M(to  .get(0),from.get(1),from.get(2)));
//						v.add(new Vec3M(to  .get(0),from.get(1),to  .get(2)));
//						v.add(new Vec3M(from.get(0),from.get(1),to  .get(2)));
//					}
//					if(up!=null){
//						f.add(new int[]{v.size(),v.size()+1,v.size()+2,v.size()+3});
//
//						List<Double> uv=(List<Double>)up.get("uv");
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(1).floatValue()));
//						vt.add(new Vec2FM(uv.get(2).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(3).floatValue()));
//						vt.add(new Vec2FM(uv.get(0).floatValue(),uv.get(1).floatValue()));
//						
//						v.add(new Vec3M(from.get(0),to  .get(1),to  .get(2)));
//						v.add(new Vec3M(to  .get(0),to  .get(1),to  .get(2)));
//						v.add(new Vec3M(to  .get(0),to  .get(1),from.get(2)));
//						v.add(new Vec3M(from.get(0),to  .get(1),from.get(2)));
//					}
//				}
//				
//				StringBuilder obj=new StringBuilder("# Blender v2.77 (sub 0) OBJ File: ''\n# www.blender.org\n");
//
//				for(Vec3M pos:v){
//					obj.append("v ").append(pos.x/16).append(' ').append(pos.y/16).append(' ').append(pos.z/16).append('\n');
//				}
//				for(Vec2FM uv:vt){
//					obj.append("vn ").append(uv.x).append(' ').append(uv.y).append('\n');
//				}
//				obj.append("usemtl Material\ns off\n");
//				for(int i=0;i<f.size();i++){
//					int[] ids=f.get(i);
//					obj.append("f ");
//					for(int j=0;j<ids.length;j++){
//						obj.append(ids[j]).append("//").append(ids[j]);
//						if(j+1<ids.length)obj.append(' ');
//					}
//					obj.append('\n');
//				}
//				
//				FileUtil.setFileTxt(new File("../src/main/resources/assets/magiology/models/block/dimension_stabiliser.obj"), obj.toString());
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
	}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
		
	}
}
