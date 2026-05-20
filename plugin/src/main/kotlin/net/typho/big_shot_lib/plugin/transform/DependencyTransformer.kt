package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.DependencyTransformAction
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.Remapper

class DependencyTransformer(
    @JvmField
    val info: DependencyTransformAction.Parameters,
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
                val method = visitMethod(
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                    injection.targetMethodName.get(),
                    injection.redirectTo.get().desc.get(),
                    injection.signature.orNull,
                    injection.exceptions.get().toTypedArray()
                )

                val namespaceAnno = method.visitAnnotation(Annotations.NAMESPACE, true)
                namespaceAnno.visit("value", injection.namespace.get())
                namespaceAnno.visitEnd()

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

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        return info.loader.get().unmapOnlyInAnnotation(this, descriptor, api) ?: super.visitAnnotation(descriptor, visible)
    }
}