package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.ModLoader
import net.typho.big_shot_lib.plugin.transform.data.InterfaceInjection
import net.typho.big_shot_lib.plugin.transform.data.MethodDesc
import net.typho.big_shot_lib.plugin.transform.data.StaticMethodInjection
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.signature.SignatureReader
import org.objectweb.asm.signature.SignatureWriter
import kotlin.collections.filter

class ToCompileTransformer(
    @JvmField
    val interfaceInjections: List<InterfaceInjection>,
    @JvmField
    val staticMethodInjections: List<StaticMethodInjection>,
    @JvmField
    val markAsDeprecated: List<MethodDesc>,
    @JvmField
    val loader: ModLoader,
    @JvmField
    val remapper: Remapper,
    @JvmField
    val markChanged: Runnable,
    api: Int,
    visitor: ClassVisitor
) : ClassVisitor(api, visitor) {
    @JvmField
    var name: String? = null
    @JvmField
    var isInterface = false

    constructor(
        parameters: NeoTransformParameters,
        remapper: Remapper,
        markChanged: Runnable,
        api: Int,
        visitor: ClassVisitor
    ) : this(parameters.interfaceInjections.get(), parameters.staticMethodInjections.get(), parameters.markAsDeprecated.get(), parameters.loader.get(), remapper, markChanged, api, visitor)

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
        var signature = signature
        val interfaceInjections = interfaceInjections.filter { it.target == name }

        if (interfaceInjections.isNotEmpty()) {
            markChanged.run()

            if (signature != null) {
                val writer = SignatureWriter()
                val reader = SignatureReader(signature)

                reader.accept(writer)

                interfaceInjections.forEach { injection ->
                    writer.visitInterface().apply {
                        visitClassType(remapper.map(injection.iface))
                        visitEnd()
                    }
                }

                signature = writer.toString()
            }

            interfaceInjections.mapTo(interfaces) { remapper.map(it.iface) }
        }

        for (injection in staticMethodInjections) {
            val targetCls = injection.targetClass

            if (targetCls == name) {
                markChanged.run()

                val method = super.visitMethod(
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                    injection.targetMethodName,
                    injection.redirectTo.desc,
                    injection.signature?.let { remapper.mapSignature(it, false) },
                    injection.exceptions.map { remapper.mapType(it) }.toTypedArray()
                )

                val args = Type.getArgumentTypes(injection.redirectTo.desc)
                val ret = Type.getReturnType(injection.redirectTo.desc)

                var slot = 0

                for (arg in args) {
                    method.visitVarInsn(arg.getOpcode(Opcodes.ILOAD), slot)
                    slot += arg.size
                }

                method.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    injection.redirectTo.cls,
                    injection.targetMethodName,
                    injection.targetMethodName,
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
    ): MethodVisitor? {
        val visitor = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (markAsDeprecated.any { it.cls == this.name && it.name == name && it.desc == descriptor }) {
            markChanged.run()
            visitor?.visitAnnotation(Annotations.DEPRECATED, true)?.visitEnd()
        }

        return visitor
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        if (descriptor == Annotations.IS_RUNTIME_READY) {
            return null
        }

        return loader.unmapOnlyInAnnotation({ descriptor, visible -> super.visitAnnotation(descriptor, visible) }, descriptor, api)?.also { markChanged.run() } ?: super.visitAnnotation(descriptor, visible)
    }

    override fun visitEnd() {
        super.visitAnnotation(Annotations.IS_RUNTIME_READY, true)?.let { anno ->
            anno.visit("value", false)
            anno.visitEnd()
        }

        super.visitEnd()
    }
}