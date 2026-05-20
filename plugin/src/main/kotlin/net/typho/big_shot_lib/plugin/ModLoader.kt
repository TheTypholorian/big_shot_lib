package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor

enum class ModLoader {
    QUILT {
        override val mappedOnlyInAnnotationName = null

        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            TODO("Not yet implemented")
        }
    },
    FABRIC {
        override val mappedOnlyInAnnotationName = "net/fabricmc/api/Environment"

        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            val anno = visitor.visitAnnotation("L$mappedOnlyInAnnotationName;", true)
            anno.visitEnum("value", "Lnet/fabricmc/api/EnvType;", if (client) "CLIENT" else "SERVER")
            anno.visitEnd()
        }
    },
    FORGE {
        override val mappedOnlyInAnnotationName = null

        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            TODO("Not yet implemented")
        }
    },
    NEOFORGE {
        override val mappedOnlyInAnnotationName = "net/neoforged/api/distmarker/OnlyIn"

        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            val anno = visitor.visitAnnotation("L$mappedOnlyInAnnotationName;", true)
            anno.visitEnum("value", "Lnet/neoforged/api/distmarker/Dist;", if (client) "CLIENT" else "DEDICATED_SERVER")
            anno.visitEnd()
        }
    };

    abstract val mappedOnlyInAnnotationName: String?

    open fun unmapOnlyInAnnotation(visitor: ClassVisitor, descriptor: String, api: Int): AnnotationVisitor? {
        return if (descriptor == "L$mappedOnlyInAnnotationName;") {
            object : AnnotationVisitor(api) {
                var client = false

                override fun visitEnum(name: String, descriptor: String, value: String) {
                    if (name == "value" && value == "CLIENT") {
                        client = true
                    }
                }

                override fun visitEnd() {
                    val anno = visitor.visitAnnotation(Annotations.ONLY_IN, true)
                    anno.visitEnum("value", "Lnet/typho/big_shot_lib/api/plugin/Environment;", if (client) "CLIENT" else "SERVER")
                    anno.visitEnd()
                }
            }
        } else {
            null
        }
    }

    abstract fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean)

    companion object {
        @JvmStatic
        val CURRENT = NEOFORGE
    }
}