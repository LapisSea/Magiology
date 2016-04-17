package com.magiology.core.coremod.transformers.classtransf;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.magiology.core.coremod.corehooks.ClientHooksM;
import com.magiology.core.coremod.transformers.ClassTransformerBase;
import com.magiology.core.coremod.transformers.CodeWrapper;
import com.magiology.core.coremod.transformers.CodeWrapper.Aload;
import com.magiology.core.coremod.transformers.CodeWrapper.CodeAction;
import com.magiology.core.coremod.transformers.CodeWrapper.InvokeInterface;
import com.magiology.core.coremod.transformers.CodeWrapper.InvokeStatic;
import com.magiology.util.utilclasses.PrintUtil;

public class NativeItemRednerHook extends ClassTransformerBase{
	
	private void addItemRenderingHook(ClassNode classByte){
		PrintUtil.println("Starting to insert item rendering hook.");
		
		MethodNode renderItemMethod=findMethod(classByte, renderItem_renderItem);
		if(renderItemMethod==null)throw new IllegalStateException("Failed! Reason: Unable to find RenderItem#renderItem");
		
		InsnList ifCondition=new InsnList();
		ifCondition.add(new VarInsnNode(ALOAD, 1));
		ifCondition.add(generateStaticCall(ClientHooksM.class, "renderItem"));
		
		
		boolean sucess=CodeWrapper.ifWrapper(renderItemMethod, ifCondition, 
			new CodeAction[]{
				new Aload(2),
				new InvokeInterface(iBakedModel_isBuiltInRenderer)
			},0,
			new CodeAction[]{
				new InvokeStatic(glStateManager_popMatrix)
			},-1
		);
		if(!sucess)throw new IllegalStateException("Failed! Reason: unable to find start and end of code to wrapp! Is there any other core-mods that modify function RenderItem#renderItem?\n Start shoud be: ");
		PrintUtil.println("Rendering hook was sucessfuly inserted!");
	}
	private void addLine(StringBuilder result, String line){
		result.append(line).append("\n");
	}
	private void addTransformTypeAbsorber(ClassNode classByte){
		
		PrintUtil.println("Starting to insert transform type hook.");
		
		MethodNode func=findMethod(classByte, handleCameraTransforms);
		if(func==null)throw new IllegalStateException("Failed! Reason: Unable to find ForgeHooksClient#handleCameraTransforms");
		
		InsnList hook=new InsnList();
		hook.add(new VarInsnNode(ALOAD, 1));
		hook.add(generateStaticCall(ClientHooksM.class, "addTransformType"));
		AbstractInsnNode line=CodeWrapper.find(func, new CodeAction[]{
				anyAload,
				anyInstaiceof
			});
		if(line==null)throw new IllegalStateException("Failed! Reason: unable to find start of the function????");
		func.instructions.insertBefore(line, hook);
		
		PrintUtil.println("Rendering hook was sucessfuly inserted!");
	}
	@Override
	public String getDebudInfo(){
		StringBuilder result=new StringBuilder();
		
		addLine(result, "Wrapping:");
		addLine(result, "");
		addLine(result, "	if(model.isBuiltInRenderer())");
		addLine(result, "	{");
		addLine(result, "		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);");
		addLine(result, "		GlStateManager.translate(-0.5F, -0.5F, -0.5F);");
		addLine(result, "		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);");
		addLine(result, "		GlStateManager.enableRescaleNormal();");
		addLine(result, "		TileEntityItemStackRenderer.instance.renderByItem(stack);");
		addLine(result, "	}");
		addLine(result, "	else");
		addLine(result, "	{");
		addLine(result, "		GlStateManager.translate(-0.5F, -0.5F, -0.5F);");
		addLine(result, "		this.renderModel(model, stack);");
		addLine(result, "		");
		addLine(result, "		if(stack.hasEffect())");
		addLine(result, "		{");
		addLine(result, "			this.renderEffect(model);");
		addLine(result, "		}");
		addLine(result, "	}");
		addLine(result, "");
		addLine(result, "with:");
		addLine(result, "if(ClientHooksM.renderItem(stack)){");
		addLine(result, "...");
		addLine(result, "}");
		addLine(result, "");
		addLine(result, "And adding:");
		addLine(result, "\tClientHooksM.addTransformType(transform);");
		addLine(result, "to");
		addLine(result, "\tForgeHooksClient#handleCameraTransforms");
		
		String res=result.toString();
		return res.substring(0, res.length()-1);
	}
	@Override
	public String[] getTransformingClasses(){
		return new String[]{
				"net.minecraft.client.renderer.entity.RenderItem",
				"net.minecraftforge.client.ForgeHooksClient"
		};
	}
	@Override
	public void transform(ClassNode classByte, int classID){
		switch(classID){
		case 0:addItemRenderingHook(classByte);break;
		case 1:addTransformTypeAbsorber(classByte);break;
		}
	}
}
