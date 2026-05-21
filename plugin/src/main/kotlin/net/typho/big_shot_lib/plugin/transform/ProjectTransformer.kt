package net.typho.big_shot_lib.plugin.transform

import groovyjarjarasm.asm.Opcodes
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension
import net.typho.big_shot_lib.plugin.ModLoader
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.gradle.api.model.ObjectFactory
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class ProjectTransformer(
    @JvmField
    val ext: BigShotLibPluginExtension,
    api: Int,
    visitor: ClassVisitor?
) : ClassVisitor(api, visitor) {
    @JvmField
    var desc: String? = null

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

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        if (descriptor == Annotations.ONLY_IN) {
            return object : AnnotationVisitor(api) {
                var client = false

                override fun visitEnum(name: String, descriptor: String, value: String) {
                    if (name == "value" && value == "CLIENT") {
                        client = true
                    }
                }

                override fun visitEnd() {
                    ext.loader.get().mapOnlyInAnnotation(this@ProjectTransformer, client)
                }
            }
        } else {
            return super.visitAnnotation(descriptor, visible)
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

        if (ext.transformInfo.staticMethodInjections.get().any { it.redirectTo.get().cls.get() == desc && it.redirectTo.get().name.get() == name && it.redirectTo.get().desc.get() == descriptor }) {
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
                for (injection in ext.transformInfo.staticMethodInjections.get()) {
                    if (injection.targetClass.get() == owner && injection.targetMethodName.get() == name && injection.redirectTo.get().desc.get() == descriptor) {
                        super.visitMethodInsn(opcode, injection.redirectTo.get().cls.get(), injection.redirectTo.get().name.get(), descriptor, false)
                        return
                    }
                }

                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
            }
        }
    }
}