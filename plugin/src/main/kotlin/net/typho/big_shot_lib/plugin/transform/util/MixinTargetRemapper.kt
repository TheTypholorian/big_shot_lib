package net.typho.big_shot_lib.plugin.transform.util

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.Remapper

class MixinTargetRemapper(
    api: Int,
    visitor: AnnotationVisitor,
    @JvmField
    val remapper: Remapper,
    @JvmField
    val parent: String,
    @JvmField
    val firstTargetConsumer: (target: String) -> Unit
) : AnnotationVisitor(api, visitor) {
    private var firstTargetConsumed = false

    override fun visit(name: String?, value: Any?) {
        var value = value

        when (name) {
            "value" -> {
                val name = remapper.mapValue(value) as Type
                value = name

                if (!firstTargetConsumed) {
                    firstTargetConsumed = true
                    firstTargetConsumer(name.internalName)
                }
            }
            "targets" -> {
                val name = remapper.map(value as String)
                value = name

                if (!firstTargetConsumed) {
                    firstTargetConsumed = true
                    firstTargetConsumer(name)
                }
            }
        }

        super.visit(name, value)
    }

    override fun visitArray(name: String?): AnnotationVisitor? {
        return when (name) {
            "value" -> object : AnnotationVisitor(api, super.visitArray(name)) {
                override fun visit(name: String?, value: Any?) {
                    val value = remapper.mapValue(value) as Type

                    if (!firstTargetConsumed) {
                        firstTargetConsumed = true
                        firstTargetConsumer(value.internalName)
                    }

                    super.visit(name, value)
                }
            }
            "targets" -> object : AnnotationVisitor(api, super.visitArray(name)) {
                override fun visit(name: String?, value: Any?) {
                    val value = remapper.map(value as String)

                    if (!firstTargetConsumed) {
                        firstTargetConsumed = true
                        firstTargetConsumer(value)
                    }

                    super.visit(name, value)
                }
            }
            else -> super.visitArray(name)
        }
    }

    override fun visitEnd() {
        super.visitEnd()

        if (!firstTargetConsumed) {
            throw IllegalStateException("No mixin target in $parent")
        }
    }
}