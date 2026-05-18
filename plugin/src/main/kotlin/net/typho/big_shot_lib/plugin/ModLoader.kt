package net.typho.big_shot_lib.plugin

import org.objectweb.asm.ClassVisitor

enum class ModLoader {
    QUILT {
        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            TODO("Not yet implemented")
        }
    },
    FABRIC {
        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            TODO("Not yet implemented")
        }
    },
    FORGE {
        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            TODO("Not yet implemented")
        }
    },
    NEOFORGE {
        override fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean) {
            val anno = visitor.visitAnnotation("Lnet/neoforged/api/distmarker/OnlyIn;", true)
            anno.visitEnum("value", "Lnet/neoforged/api/distmarker/Dist;", if (client) "CLIENT" else "DEDICATED_SERVER")
            anno.visitEnd()
        }
    };

    abstract fun mapOnlyInAnnotation(visitor: ClassVisitor, client: Boolean)

    companion object {
        @JvmStatic
        val CURRENT = NEOFORGE
    }
}