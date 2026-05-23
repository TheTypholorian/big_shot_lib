package net.typho.big_shot_lib.plugin.transform.util

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.commons.Remapper

class MixinInjectionRemapper(
    api: Int,
    visitor: AnnotationVisitor,
    @JvmField
    val remapper: Remapper,
    @JvmField
    val mixinTarget: String,
    @JvmField
    val parentName: String? = null
) : AnnotationVisitor(api, visitor) {
    override fun visit(name: String?, value: Any?) {
        if (value is String && (parentName == "method" || name == "method")) {
            val index = value.indexOf('(')

            if (index == -1) {
                val n = remapper.mapMethodName(mixinTarget, value, null)
                println("remapping $value to $n")
                super.visit(name, n)
            } else {
                val desc = value.substring(index)
                val n = remapper.mapMethodName(mixinTarget, value.substring(0, index), desc) + remapper.mapMethodDesc(desc)
                println("remapping $value to $n")
                super.visit(name, n)
            }
        } else {
            super.visit(name, value)
        }
    }

    override fun visitAnnotation(
        name: String?,
        descriptor: String?
    ): AnnotationVisitor {
        return MixinInjectionRemapper(api, super.visitAnnotation(name, descriptor), remapper, mixinTarget, name)
    }

    override fun visitArray(name: String?): AnnotationVisitor {
        return MixinInjectionRemapper(api, super.visitArray(name), remapper, mixinTarget, name)
    }
}