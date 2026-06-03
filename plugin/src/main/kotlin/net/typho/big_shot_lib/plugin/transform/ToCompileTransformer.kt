package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.signature.SignatureReader
import org.objectweb.asm.signature.SignatureVisitor
import org.objectweb.asm.signature.SignatureWriter

class ToCompileTransformer(
    @JvmField
    val info: NeoTransformParameters,
    //@JvmField
    //val overloads: (newDesc: String, oldDesc: String, argumentConverters: List<ArgumentOverloadConverter?>) -> Unit,
    @JvmField
    val remapper: Remapper,
    api: Int,
    visitor: ClassVisitor
) : ClassVisitor(api, visitor) {
    @JvmField
    var name: String? = null
    @JvmField
    var isInterface = false

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>?
    ) {
        this.name = name
        isInterface = access and Opcodes.ACC_INTERFACE != 0

        val interfaces = interfaces?.toMutableList() ?: mutableListOf()
        val oldSignature = signature
        var signature = signature
        val interfaceInjections = info.interfaceInjections.get().filter { it.target.get() == name }

        if (signature != null) {
            val writer = SignatureWriter()
            val reader = SignatureReader(signature)

            reader.accept(object : SignatureVisitor(Opcodes.ASM9) {
                override fun visitSuperclass(): SignatureVisitor {
                    return writer.visitSuperclass()
                }

                override fun visitInterface(): SignatureVisitor {
                    return writer.visitInterface()
                }

                override fun visitFormalTypeParameter(name: String) {
                    writer.visitFormalTypeParameter(name)
                }

                override fun visitClassBound(): SignatureVisitor {
                    return writer.visitClassBound()
                }

                override fun visitInterfaceBound(): SignatureVisitor {
                    return writer.visitInterfaceBound()
                }

                override fun visitParameterType(): SignatureVisitor {
                    return writer.visitParameterType()
                }

                override fun visitReturnType(): SignatureVisitor {
                    return writer.visitReturnType()
                }

                override fun visitExceptionType(): SignatureVisitor {
                    return writer.visitExceptionType()
                }

                override fun visitBaseType(descriptor: Char) {
                    writer.visitBaseType(descriptor)
                }

                override fun visitTypeVariable(name: String) {
                    writer.visitTypeVariable(name)
                }

                override fun visitArrayType(): SignatureVisitor {
                    return writer.visitArrayType()
                }

                override fun visitClassType(name: String) {
                    writer.visitClassType(name)
                }

                override fun visitInnerClassType(name: String) {
                    writer.visitInnerClassType(name)
                }

                override fun visitTypeArgument() {
                    writer.visitTypeArgument()
                }

                override fun visitTypeArgument(wildcard: Char): SignatureVisitor {
                    return writer.visitTypeArgument(wildcard)
                }

                override fun visitEnd() {
                    writer.visitEnd()
                }
            })

            interfaceInjections.forEach { injection ->
                writer.visitInterface().apply {
                    visitClassType(remapper.map(injection.iface.get()))
                    visitEnd()
                }
            }

            signature = writer.toString()
        }

        interfaceInjections.mapTo(interfaces) { remapper.map(it.iface.get()) }

        if (interfaceInjections.isNotEmpty()) {
            println("[Big Shot Lib] Injected interfaces ${interfaceInjections.map { it.iface.get() }} to $name, old signature: $oldSignature, new signature: $signature")
        }

        for (injection in info.staticMethodInjections.get()) {
            val targetCls = injection.targetClass.get()

            if (targetCls == name) {
                val method = super.visitMethod(
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                    injection.targetMethodName.get(),
                    injection.redirectTo.get().desc.get(),
                    injection.signature.orNull?.let { remapper.mapSignature(it, false) },
                    injection.exceptions.orNull?.map { remapper.mapType(it) }?.toTypedArray()
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
                    injection.targetMethodName.get(),
                    injection.targetMethodName.get(),
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
        val visitor = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (info.markAsDeprecated.get().any { it.cls.get() == this.name && it.name.get() == name && it.desc.get() == descriptor }) {
            visitor.visitAnnotation(Annotations.DEPRECATED, true).visitEnd()
        }

        return visitor
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        if (descriptor == Annotations.IS_RUNTIME_READY) {
            return null
        }

        return info.loader.get().unmapOnlyInAnnotation(this, descriptor, api) ?: super.visitAnnotation(descriptor, visible)
    }

    override fun visitEnd() {
        for (injection in info.interfaceInjections.get()) {
            if (injection.target.get() == name) {
                for (method in injection.methods.get()) {
                    val visitor = visitMethod(
                        Opcodes.ACC_PUBLIC,
                        method.first,
                        method.second,
                        null,
                        null
                    )
                    visitor.visitTypeInsn(Opcodes.NEW, "java/lang/IllegalStateException")
                    visitor.visitInsn(Opcodes.DUP)
                    visitor.visitLdcInsn("Implemented via mixin")
                    visitor.visitMethodInsn(
                        Opcodes.INVOKESPECIAL,
                        "java/lang/IllegalStateException",
                        "<init>",
                        "(Ljava/lang/String;)V",
                        false
                    )
                    visitor.visitInsn(Opcodes.ATHROW)

                    visitor.visitMaxs(3, 1)
                    visitor.visitEnd()
                }
            }
        }

        super.visitAnnotation(Annotations.IS_RUNTIME_READY, true)?.let { anno ->
            anno.visit("value", false)
            anno.visitEnd()
        }

        super.visitEnd()
    }
}