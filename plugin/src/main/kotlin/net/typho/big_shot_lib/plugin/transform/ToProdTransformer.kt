package net.typho.big_shot_lib.plugin.transform

import groovyjarjarasm.asm.Opcodes
import net.typho.big_shot_lib.plugin.ModLoader
import net.typho.big_shot_lib.plugin.transform.data.StaticMethodInjection
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor

class ToProdTransformer(
    @JvmField
    val staticMethodInjections: List<StaticMethodInjection>,
    @JvmField
    val clientOnlyPackages: List<String>,
    @JvmField
    val serverOnlyPackages: List<String>,
    @JvmField
    val loader: ModLoader,
    @JvmField
    val markChanged: Runnable,
    api: Int,
    visitor: ClassVisitor?
) : ClassVisitor(api, visitor) {
    @JvmField
    var desc: String? = null
    @JvmField
    var isClient: Boolean? = null

    constructor(
        parameters: NeoTransformParameters,
        markChanged: Runnable,
        api: Int,
        visitor: ClassVisitor
    ) : this(parameters.staticMethodInjections.get(), parameters.clientOnlyPackages.get(), parameters.serverOnlyPackages.get(), parameters.loader.get(), markChanged, api, visitor)

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String?>?
    ) {
        desc = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        return when (descriptor) {
            Annotations.IS_RUNTIME_READY -> null
            else -> super.visitAnnotation(descriptor, visible)
        }
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String?>?
    ): MethodVisitor {
        var access = access

        if (staticMethodInjections.any { it.redirectTo.cls == desc && it.redirectTo.name == name && it.redirectTo.desc == descriptor }) {
            markChanged.run()
            access = access and Opcodes.ACC_PUBLIC and Opcodes.ACC_PRIVATE.inv()
        }

        return object : MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
            override fun visitMethodInsn(
                opcode: Int,
                owner: String,
                name: String,
                descriptor: String,
                isInterface: Boolean
            ) {
                for (injection in staticMethodInjections) {
                    if (injection.targetClass == owner && injection.targetMethodName == name && injection.redirectTo.desc == descriptor) {
                        markChanged.run()
                        super.visitMethodInsn(opcode, injection.redirectTo.cls, injection.redirectTo.name, descriptor, false)
                        return
                    }
                }

                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
            }
        }
    }

    override fun visitEnd() {
        fun helper() {
            if (isClient == null) {
                for (pkg in clientOnlyPackages) {
                    if (desc!!.startsWith(pkg)) {
                        markChanged.run()
                        loader.mapOnlyInAnnotation(::visitAnnotation, true)
                        isClient = true
                        return
                    }
                }

                for (pkg in serverOnlyPackages) {
                    if (desc!!.startsWith(pkg)) {
                        markChanged.run()
                        loader.mapOnlyInAnnotation(::visitAnnotation, false)
                        isClient = false
                        return
                    }
                }
            }
        }

        helper()

        super.visitAnnotation(Annotations.IS_RUNTIME_READY, true)?.let { anno ->
            markChanged.run()
            anno.visit("value", true)
            anno.visitEnd()
        }

        super.visitEnd()
    }
}