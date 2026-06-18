package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor

enum class ModLoader {
    NONE {
        override val mappedOnlyInAnnotationName = null

        override fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean) {
        }
    },
    FABRIC {
        override val mappedOnlyInAnnotationName = "net/fabricmc/api/Environment"

        override fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean) {
            annotation("L$mappedOnlyInAnnotationName;", true)?.let { anno ->
                anno.visitEnum("value", "Lnet/fabricmc/api/EnvType;", if (client) "CLIENT" else "SERVER")
                anno.visitEnd()
            }
        }
    },
    FORGE {
        override val mappedOnlyInAnnotationName = null

        override fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean) {
            TODO("Not yet implemented")
        }
    },
    NEOFORGE {
        override val mappedOnlyInAnnotationName = "net/neoforged/api/distmarker/OnlyIn"

        override fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean) {
            annotation("L$mappedOnlyInAnnotationName;", true)?.let { anno ->
                anno.visitEnum("value", "Lnet/neoforged/api/distmarker/Dist;", if (client) "CLIENT" else "DEDICATED_SERVER")
                anno.visitEnd()
            }
        }
    };

    abstract val mappedOnlyInAnnotationName: String?

    open fun unmapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, descriptor: String, api: Int): AnnotationVisitor? {
        return if (mappedOnlyInAnnotationName != null && descriptor == "L$mappedOnlyInAnnotationName;") {
            object : AnnotationVisitor(api) {
                var client = false

                override fun visitEnum(name: String, descriptor: String, value: String) {
                    if (name == "value" && value == "CLIENT") {
                        client = true
                    }
                }

                override fun visitEnd() {
                    annotation(Annotations.ONLY_IN, true)?.let { anno ->
                        anno.visitEnum("value", "Lnet/typho/big_shot_lib/api/plugin/Environment;", if (client) "CLIENT" else "SERVER")
                        anno.visitEnd()
                    }
                }
            }
        } else {
            null
        }
    }

    abstract fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean)

    companion object {
        @JvmStatic
        operator fun get(key: String) = enumValueOf<ModLoader>(key.uppercase())
    }
}