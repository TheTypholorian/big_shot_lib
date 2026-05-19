package net.typho.big_shot_lib.plugin

import org.objectweb.asm.ClassVisitor

enum class ModLoader {
    QUILT {
        override val mappedOnlyInAnnotationName = null

        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            TODO("Not yet implemented")
        }
    },
    FABRIC {
        override val mappedOnlyInAnnotationName = "Lnet/fabricmc/api/Environment;"

        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            val anno = visitor.visitAnnotation("Lnet/fabricmc/api/Environment;", true)
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
        override val mappedOnlyInAnnotationName = "Lnet/neoforged/api/distmarker/OnlyIn;"

        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            val anno = visitor.visitAnnotation("Lnet/neoforged/api/distmarker/OnlyIn;", true)
            anno.visitEnum("value", "Lnet/neoforged/api/distmarker/Dist;", if (client) "CLIENT" else "DEDICATED_SERVER")
            anno.visitEnd()
        }
    };

    abstract val mappedOnlyInAnnotationName: String?

    abstract fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean)

    companion object {
        @JvmStatic
        val CURRENT = NEOFORGE
    }
}