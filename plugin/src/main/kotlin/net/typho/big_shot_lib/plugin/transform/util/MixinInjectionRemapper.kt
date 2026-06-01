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
        if (value is String && ((parentName == "method" || name == "method") || (parentName == "target" || name == "target"))) {
            var methodOwner: String? = null
            var work = value

            val index0 = work.indexOf(';')
            var index1 = work.indexOf('(')

            if (work.startsWith("L") && index0 > 0 && (index1 == -1 || index0 < index1)) {
                methodOwner = work.substring(1, index0)
                work = work.substring(index0 + 1)
            }

            index1 = work.indexOf('(')

            val methodName: String
            val methodDesc: String?

            if (index1 >= 0) {
                methodName = work.substring(0, index1)
                methodDesc = work.substring(index1)
            } else {
                methodName = work
                methodDesc = null
            }

            val builder = StringBuilder()

            if (methodOwner != null) {
                builder.append("L${remapper.map(methodOwner)};")
            }

            if (methodOwner == null) {
                methodOwner = mixinTarget
            }

            builder.append(remapper.mapMethodName(methodOwner, methodName, methodDesc))

            if (methodDesc != null) {
                builder.append(remapper.mapMethodDesc(methodDesc))
            }

            super.visit(name, builder.toString())
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