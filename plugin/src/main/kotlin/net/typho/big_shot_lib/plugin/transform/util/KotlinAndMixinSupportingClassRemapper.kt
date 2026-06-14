package net.typho.big_shot_lib.plugin.transform.util

import net.typho.big_shot_lib.plugin.internal.loom.KotlinMetadataRemappingClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper

class KotlinAndMixinSupportingClassRemapper(
    api: Int,
    visitor: ClassVisitor,
    remapper: Remapper
) : ClassRemapper(api, KotlinMetadataRemappingClassVisitor(remapper, visitor), remapper) {
    private var name: String? = null
    private var target: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String?>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.name = name
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        return if (descriptor == Annotations.MIXIN) {
            MixinTargetRemapper(api, super.visitAnnotation(descriptor, visible), remapper, name!!) { target = it }
        } else {
            super.visitAnnotation(descriptor, visible)
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String?>?
    ): MethodVisitor {
        return object : MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
            override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
                if (descriptor.contains("mixin")) {
                    val target = target

                    if (target == null) {
                        System.err.println("Target for mixin class ${this@KotlinAndMixinSupportingClassRemapper.name} is null")
                    } else {
                        return MixinInjectionRemapper(api, super.visitAnnotation(descriptor, visible), remapper, target)
                    }
                }

                return super.visitAnnotation(descriptor, visible)
            }
        }
    }
}