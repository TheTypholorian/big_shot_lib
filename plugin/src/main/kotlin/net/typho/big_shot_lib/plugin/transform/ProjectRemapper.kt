package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.MCVersion
import net.typho.big_shot_lib.plugin.transform.util.AnnotationField
import net.typho.big_shot_lib.plugin.transform.util.AnnotationScanner
import org.objectweb.asm.commons.Remapper

class ProjectRemapper(
    api: Int,
    @JvmField
    val annotations: AnnotationScanner
) : Remapper(api) {
    override fun map(internalName: String): String? {
        return if (MCVersion.CURRENT < MCVersion.MC1_21_11) {
            when (internalName) {
                "net/minecraft/resources/Identifier" -> "net/minecraft/resources/ResourceLocation"
                else -> internalName
            }
        } else {
            internalName
        }
    }

    override fun mapMethodName(owner: String, name: String, descriptor: String): String {
        var name = name

        annotations.getClasses(AnnotationField.NAMESPACE) { cls, values ->
            if (cls.name == owner) {
                values[AnnotationField.NAMESPACE_VALUE]?.let {
                    name = "$it$$name"
                }
            }
        }

        return name
    }

    override fun mapFieldName(owner: String, name: String, descriptor: String): String {
        var name = name

        annotations.getClasses(AnnotationField.NAMESPACE) { cls, values ->
            if (cls.name == owner) {
                values[AnnotationField.NAMESPACE_VALUE]?.let {
                    name = "$it$$name"
                }
            }
        }

        return name
    }
}