package net.typho.big_shot_lib.plugin.transform.util

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor

open class ClassStatusVisitor(
    api: Int,
    visitor: ClassVisitor? = null
) : ClassVisitor(api, visitor) {
    var status = Status.NOT_TOUCHED
        protected set

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        if (descriptor == Annotations.IS_RUNTIME_READY) {
            return object : AnnotationVisitor(api, super.visitAnnotation(descriptor, visible)) {
                override fun visit(name: String, value: Any?) {
                    if (name == "value") {
                        status = if (value as Boolean) Status.RUNTIME else Status.COMPILE
                    }

                    super.visit(name, value)
                }
            }
        }

        return super.visitAnnotation(descriptor, visible)
    }

    enum class Status {
        /**
         * Ex. `Identifier`
         */
        COMPILE,
        /**
         * Ex. `ResourceLocation`
         */
        RUNTIME,
        /**
         * Ex. `ResourceLocation` for dependencies, `Identifier` for project
         */
        NOT_TOUCHED
    }
}