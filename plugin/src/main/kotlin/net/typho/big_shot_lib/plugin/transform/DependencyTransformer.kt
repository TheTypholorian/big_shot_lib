package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.*
import net.typho.big_shot_lib.plugin.DependencyTransformAction
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.Remapper

class DependencyTransformer(
    @JvmField
    val info: DependencyTransformAction.Parameters,
    @JvmField
    val overloads: (owner: String, newDesc: String, oldDesc: String, argumentConverters: Array<ArgumentOverloadConverter>, returnConverter: ArgumentOverloadConverter) -> Unit,
    @JvmField
    val remapper: Remapper,
    api: Int,
    visitor: ClassVisitor
) : ClassVisitor(api, visitor) {
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>?
    ) {
        val interfaces = interfaces?.toMutableList() ?: mutableListOf()
        val oldSignature = signature
        var signature = signature
        val interfaceInjections = info.interfaceInjections.get().filter { it.target.get() == name }

        if (interfaceInjections.any { it.typeParams.get().isNotEmpty() } && signature == null) {
            signature = "L$superName;"

            for (injection in interfaceInjections) {
                signature += "L${injection.iface.get()};${interfaces.joinToString(separator = "") { "L$it;" }}"
            }
        }

        if (signature != null) {
            for (injection in interfaceInjections) {
                signature += if (injection.typeParams.get().isEmpty()) {
                    "L${injection.iface.get()};"
                } else {
                    "L${injection.iface.get()}<${injection.typeParams.get().joinToString(separator = "") { "L$it;" }}>;"
                }
            }
        }

        interfaceInjections.forEach {
            if (it.target.get() == name) {
                interfaces.add(remapper.map(it.iface.get()))
            }
        }

        if (interfaceInjections.isNotEmpty()) {
            println("[Big Shot Lib] Injected interfaces ${interfaceInjections.map { it.iface.get() }} to $name, old signature: $oldSignature, new signature: $signature")
        }

        for (injection in info.staticMethodInjections.get()) {
            if (injection.targetClass.get() == name) {
                val method = super.visitMethod(
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                    injection.targetMethodName.get(),
                    injection.redirectTo.get().desc.get(),
                    injection.signature.orNull,
                    injection.exceptions.get().toTypedArray()
                )

                val args = Type.getArgumentTypes(injection.redirectTo.get().desc.get())
                val ret = Type.getReturnType(injection.redirectTo.get().desc.get())

                var slot = 0

                for (arg in args) {
                    method.visitVarInsn(arg.getOpcode(Opcodes.ILOAD), slot)
                    slot += arg.size
                }

                method.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    injection.redirectTo.get().cls.get(),
                    injection.redirectTo.get().name.get(),
                    injection.redirectTo.get().desc.get(),
                    false
                )

                method.visitInsn(ret.getOpcode(Opcodes.IRETURN))

                method.visitMaxs(0, 0)
                method.visitEnd()
            }
        }

        super.visit(version, access, name, signature, superName, interfaces.toTypedArray())
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        if (access and Opcodes.ACC_SYNTHETIC == 0) {
            val args = Type.getArgumentTypes(descriptor)
            val ret = Type.getReturnType(descriptor)
            val all = arrayListOf(ret).let {
                it.addAll(args)
                it.toTypedArray()
            }
            val argumentConverterCache = hashMapOf<Type, List<ArgumentOverloadConverter>>()

            fun getConverter(type: Type) = argumentConverterCache.computeIfAbsent(type) { key ->
                info.argumentOverloadConverters.get().filter { it.to.get() == type.internalName }
            }

            val permutationArray = all.map { getConverter(it).size }.toIntArray()
            val temp = IntArray(permutationArray.size) { 0 }

            fun permutate() {
                if (temp.sum() > 0) {
                    val newTypes = temp.mapIndexed { index, i -> if (i == 0) all[index] else Type.getType("L${argumentConverterCache[all[index]]!![i - 1].from.get()};") }
                    val desc = Type.getMethodDescriptor(newTypes.first(), *newTypes.subList(1, newTypes.size).toTypedArray())
                    //println("Creating permutation of $name $descriptor with $desc")
                    super.visitMethod(access, name, desc, signature, exceptions).visitEnd() // TODO ?
                }
            }

            fun step(index: Int) {
                if (index == temp.size) {
                    permutate()
                } else {
                    for (num in 0..permutationArray[index]) {
                        temp[index] = num
                        step(index + 1)
                    }
                }
            }

            step(0)
        }

        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        return info.loader.get().unmapOnlyInAnnotation(this, descriptor, api) ?: super.visitAnnotation(descriptor, visible)
    }
}