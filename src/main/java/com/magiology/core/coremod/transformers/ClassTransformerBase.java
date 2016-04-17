package com.magiology.core.coremod.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Method;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.magiology.core.coremod.transformers.CodeWrapper.CodeAction;
import com.magiology.util.utilclasses.PrintUtil;

public abstract class ClassTransformerBase{
	
	protected static CodeAction 
		anyAload=new CodeAction(){
			@Override
			public boolean check(AbstractInsnNode line){
				return line.getOpcode()==ALOAD;
			}
			@Override
			public void move(LineWalker code){
				code.next();
			}
		},
		anyInstaiceof=new CodeAction(){
			@Override
			public boolean check(AbstractInsnNode line){
				return line.getOpcode()==INSTANCEOF;
			}
			@Override
			public void move(LineWalker code){
				code.next();
			}
		};
	
	
	protected static final ASMClass 
		entityLivingBaseClass=new ASMClass("pr", "net/minecraft/entity/EntityLivingBase"),
		itemStackClass=new ASMClass("zx", "net/minecraft/item/ItemStack"),
		transformTypeClass=new ASMClass("bgr$b", "net/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType"),
		renderItemClass=new ASMClass("bjh", "net/minecraft/client/renderer/entity/RenderItem"),
		itemRendererClass=new ASMClass("bfn", "net/minecraft/client/renderer/ItemRenderer"),
		iBakedModel=new ASMClass("boq", "net/minecraft/client/resources/model/IBakedModel"),
		tessellator=new ASMClass("bfx", "net/minecraft/client/renderer/Tessellator"),
		glStateManager=new ASMClass("bfl", "net/minecraft/client/renderer/GlStateManager"),
		forgeHooksClient=new ASMClass("net/minecraftforge/client/ForgeHooksClient", "net/minecraftforge/client/ForgeHooksClient");
	public static boolean isObfuscated;
	protected static final ASMMethod
		renderItemFunc=new ASMMethod("a","renderItem",new ASMFuncDesc("V",entityLivingBaseClass,itemStackClass,transformTypeClass)),
		renderItemModelForEntityFunc=new ASMMethod("a","renderItemModelForEntity",new ASMFuncDesc("V",itemStackClass,entityLivingBaseClass,transformTypeClass)),
		clientHooksM_renderItem=new ASMMethod("a","renderItemModelForEntity",new ASMFuncDesc("Z",itemStackClass,entityLivingBaseClass,transformTypeClass)),
		renderItem_renderItem=new ASMMethod("a","renderItem",new ASMFuncDesc("V",itemStackClass,iBakedModel)),
		tessellator_getInstance=new ASMMethod("a", "getInstance", new ASMFuncDesc(tessellator.get()+";")),
		tessellator_draw=new ASMMethod("b", "draw", new ASMFuncDesc("V")),
		iBakedModel_isBuiltInRenderer=new ASMMethod("d", "isBuiltInRenderer", new ASMFuncDesc("Z")),
		glStateManager_popMatrix=new ASMMethod("F", "popMatrix", new ASMFuncDesc("V")),
		handleCameraTransforms=new ASMMethod("handleCameraTransforms", "handleCameraTransforms", new ASMFuncDesc(iBakedModel.normal, iBakedModel,transformTypeClass));
	
	protected static boolean isAloadValue(AbstractInsnNode line, int value){
		return line.getOpcode()==ALOAD&&((VarInsnNode)line).var==value;
	}
	
	
	protected static boolean isInvokeInterface(AbstractInsnNode line, ASMMethod method){
		return line.getOpcode()==INVOKEINTERFACE&&method.equals(line);
	}
	protected static boolean isInvokeStatic(AbstractInsnNode line, ASMMethod method){
		return line.getOpcode()==INVOKESTATIC&&method.equals(line);
	}
	protected static boolean isInvokeVirtual(AbstractInsnNode line, ASMMethod method){
		return line.getOpcode()==INVOKEVIRTUAL&&method.equals(line);
	}
	protected MethodNode findMethod(ClassNode clazz,ASMMethod toFind){
		for(MethodNode method:clazz.methods){
			if(toFind.equals(method))return method;
		}
		return null;
	}
	protected MethodInsnNode generateStaticCall(Class clazz, String methodName){
		Method method=null;
		for(Method mhod:clazz.getDeclaredMethods()){
			if(mhod.getName().equals(methodName)){
				method=mhod;
				break;
			}
		}
		
		Class<?> returnType=method.getReturnType();
		
		String returnTypeS=Type.getInternalName(returnType);
		if(returnTypeS.equals("void"))returnTypeS="V";
		else if(returnTypeS.equals(Type.getInternalName(byte.class)))returnTypeS="B";
		else if(returnTypeS.equals(Type.getInternalName(char.class)))returnTypeS="C";
		else if(returnTypeS.equals(Type.getInternalName(double.class)))returnTypeS="D";
		else if(returnTypeS.equals(Type.getInternalName(float.class)))returnTypeS="F";
		else if(returnTypeS.equals(Type.getInternalName(int.class)))returnTypeS="I";
		else if(returnTypeS.equals(Type.getInternalName(long.class)))returnTypeS="J";
		else if(returnTypeS.equals(Type.getInternalName(short.class)))returnTypeS="S";
		else if(returnTypeS.equals(Type.getInternalName(boolean.class)))returnTypeS="Z";
		
		Class<?>[] params=method.getParameterTypes();
		ASMClass[] param=new ASMClass[params.length];
		
		for(int i=0;i<param.length;i++)param[i]=new ASMClass("", Type.getInternalName(params[i]));
		
		ASMFuncDesc desc=new ASMFuncDesc(returnTypeS, param);
		
		return new MethodInsnNode(INVOKESTATIC, Type.getInternalName(clazz), method.getName(), desc.get(),false);
	}
	public abstract String getDebudInfo();
	public abstract String[] getTransformingClasses();
	protected boolean isGetField(AbstractInsnNode line, ASMClass valueToSetOwner, String valueToSetName, ASMClass type){
		if(line.getOpcode()==GETFIELD){
			FieldInsnNode getF=(FieldInsnNode)line;
			PrintUtil.println(getF.desc.equals(type.get()+";"),getF.name.equals(valueToSetName),("L"+getF.owner).equals(valueToSetOwner.get()));
			return getF.desc.equals(type.get()+";")&&getF.name.equals(valueToSetName)&&("L"+getF.owner).equals(valueToSetOwner.get());
		}
		return false;
	}
	
	public abstract void transform(ClassNode classNode,int classID);
}
