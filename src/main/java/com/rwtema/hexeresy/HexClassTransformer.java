package com.rwtema.hexeresy;

import com.google.common.collect.ImmutableMap;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Map;

public class HexClassTransformer implements IClassTransformer {

	private static final String constructorName = "<init>";
	private static final String constructorDesc = "(Lnet/minecraft/client/model/ModelRenderer;IIFFFIIIFZ)V";
	private static final String texturedQuadFieldDesc = "[Lnet/minecraft/client/model/TexturedQuad;";
	private static final String modelBoxClassName = "net.minecraft.client.model.ModelBox";
	private static final String methodHookDesc = "(Lnet/minecraft/client/model/ModelRenderer;IIFFFIIIFZ[Lnet/minecraft/client/model/TexturedQuad;)[Lnet/minecraft/client/model/TexturedQuad;";
	private static final String methodHookName = "assignQuadList";
	private static final Map<String, String[]> srgNames = ImmutableMap.<String, String[]>builder()
			.put("quadList", new String[]{"posX1", "posY1", "posZ1"})
			.put("field_78254_i", new String[]{"field_78252_a", "field_78250_b", "field_78251_c"})
			.build();

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (bytes == null || !transformedName.equals(modelBoxClassName)) {
			return bytes;
		}

		HexRenderingCoreMod.logger.info("Attempting to install Hexagonal Heresy rendering hook");

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		String fieldName = null;
		String[] posFieldNames = null;
		for (FieldNode field : classNode.fields) {
			if (srgNames.containsKey(field.name) && texturedQuadFieldDesc.equals(field.desc)) {
				fieldName = field.name;
				posFieldNames = srgNames.get(field.name);
				field.access = field.access & (~Opcodes.ACC_FINAL);
				break;
			}
		}

		if (fieldName == null) {
			HexRenderingCoreMod.logger.info("Unable to find quadList field.");
			return bytes;
		}

		MethodNode initMethod = null;
		for (MethodNode method : classNode.methods) {
			if (constructorName.equals(method.name) && constructorDesc.equals(method.desc)) {
				initMethod = method;
				break;
			}
		}

		if (initMethod == null) {
			HexRenderingCoreMod.logger.info("Unable to find <init> method.");
			return bytes;
		}

		InsnList instructions = initMethod.instructions;
		AbstractInsnNode lastReturn = instructions.getLast();
		while (lastReturn.getOpcode() != Opcodes.RETURN) {
			lastReturn = lastReturn.getPrevious();
		}

		String modelBoxTypeName = modelBoxClassName.replace('.', '/');

		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ALOAD, 0));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ALOAD, 1));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ILOAD, 2));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ILOAD, 3));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ALOAD, 0));
		instructions.insertBefore(lastReturn, new FieldInsnNode(Opcodes.GETFIELD, modelBoxTypeName, posFieldNames[0], "F"));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ALOAD, 0));
		instructions.insertBefore(lastReturn, new FieldInsnNode(Opcodes.GETFIELD, modelBoxTypeName, posFieldNames[1], "F"));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ALOAD, 0));
		instructions.insertBefore(lastReturn, new FieldInsnNode(Opcodes.GETFIELD, modelBoxTypeName, posFieldNames[2], "F"));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ILOAD, 7));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ILOAD, 8));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ILOAD, 9));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.FLOAD, 10));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ILOAD, 11));
		instructions.insertBefore(lastReturn, new VarInsnNode(Opcodes.ALOAD, 0));
		instructions.insertBefore(lastReturn, new FieldInsnNode(Opcodes.GETFIELD, modelBoxTypeName, fieldName, texturedQuadFieldDesc));
		instructions.insertBefore(lastReturn, new MethodInsnNode(Opcodes.INVOKESTATIC, HexRenderingHooks.class.getName().replace('.', '/'), methodHookName, methodHookDesc, false));
		instructions.insertBefore(lastReturn, new FieldInsnNode(Opcodes.PUTFIELD, modelBoxTypeName, fieldName, texturedQuadFieldDesc));

		HexRenderingCoreMod.logger.info("Installed Hexcraft rendering hook");

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}


//    ALOAD 0
//    ALOAD 1
//    ILOAD 2
//    ILOAD 3
//    ALOAD 0
//    GETFIELD net/minecraft/client/model/ModelBox.posX1 : F
//    ALOAD 0
//    GETFIELD net/minecraft/client/model/ModelBox.posY1 : F
//    ALOAD 0
//    GETFIELD net/minecraft/client/model/ModelBox.posZ1 : F
//    ILOAD 7
//    ILOAD 8
//    ILOAD 9
//    FLOAD 10
//    ILOAD 11
//    ALOAD 0
//    GETFIELD net/minecraft/client/model/ModelBox.quadList : [Lnet/minecraft/client/model/TexturedQuad;
//    INVOKESTATIC net/minecraftforge/client/HexRenderer.assignQuadList (Lnet/minecraft/client/model/ModelRenderer;IIFFFIIIFZ[Lnet/minecraft/client/model/TexturedQuad;)[Lnet/minecraft/client/model/TexturedQuad;
//    PUTFIELD net/minecraft/client/model/ModelBox.quadList : [Lnet/minecraft/client/model/TexturedQuad;
}
