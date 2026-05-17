package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension
import net.typho.big_shot_lib.plugin.MCVersion
import net.typho.big_shot_lib.plugin.transform.util.AnnotationField
import net.typho.big_shot_lib.plugin.transform.util.AnnotationScanner
import org.objectweb.asm.commons.Remapper

class ProjectRemapper(
    @JvmField
    val info: BigShotLibPluginExtension.TransformInfo,
    api: Int,
    @JvmField
    val annotations: AnnotationScanner
) : Remapper(api) {
    override fun map(internalName: String): String {
        if (MCVersion.CURRENT < MCVersion.MC1_21_11 && internalName == "net/minecraft/resources/Identifier") {
            return "net/minecraft/resources/ResourceLocation"
        }

        return info.classRenames.get().lastOrNull { it.to.get() == internalName }?.from?.get() ?: internalName
    }

    override fun mapMethodName(owner: String, name: String, descriptor: String): String {
        if (name == "<init>" || name == "<clinit>") {
            return name
        }

        var name = info.methodRenames.get().lastOrNull { it.from.get().let { it.cls.get() == owner && it.desc.get() == descriptor } && it.to.get() == name }?.from?.get()?.name?.get() ?: name

        annotations.getClasses(AnnotationField.NAMESPACE) { cls, values ->
            if (cls.name == owner) {
                values[AnnotationField.NAMESPACE_VALUE]?.let {
                    if (!name.startsWith("$it$")) {
                        name = "$it$$name"
                    }
                }
            }
        }

        return name
    }

    override fun mapFieldName(owner: String, name: String, descriptor: String): String {
        var name = info.fieldRenames.get().lastOrNull { it.from.get().let { it.cls.get() == owner && it.desc.get() == descriptor } && it.to.get() == name }?.from?.get()?.name?.get() ?: name

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