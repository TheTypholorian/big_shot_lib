package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension
import net.typho.big_shot_lib.plugin.MCVersion
import net.typho.big_shot_lib.plugin.transform.util.AnnotationField
import net.typho.big_shot_lib.plugin.transform.util.AnnotationScanner
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.commons.Remapper

class ProjectRemapper(
    @JvmField
    val ext: BigShotLibPluginExtension,
    api: Int,
    @JvmField
    val annotations: AnnotationScanner
) : Remapper(api) {
    override fun map(internalName: String): String {
        return ext.transformInfo.classRenames.get().lastOrNull { it.to.get() == internalName }?.from?.get() ?: internalName
    }

    override fun mapMethodName(owner: String, name: String, descriptor: String): String {
        if (name == "<init>" || name == "<clinit>") {
            return name
        }

        var name = ext.transformInfo.methodRenames.get().lastOrNull { it.from.get().let { it.cls.get() == owner && it.desc.get() == descriptor } && it.to.get() == name }?.from?.get()?.name?.get() ?: name

        annotations.getClasses(Annotations.NAMESPACE) { cls, values ->
            if (cls == owner) {
                values[Annotations.NAMESPACE_VALUE]?.let {
                    if (!name.startsWith("$it$")) {
                        name = "$it$$name"
                    }
                }
            }
        }

        annotations.getMethods(Annotations.NAMESPACE) { method, values ->
            if (method.cls.get() == owner && method.name.get() == name && method.desc.get() == descriptor) {
                values[Annotations.NAMESPACE_VALUE]?.let {
                    if (!name.startsWith("$it$")) {
                        name = "$it$$name"
                    }
                }
            }
        }

        return name
    }

    override fun mapFieldName(owner: String, name: String, descriptor: String): String {
        var name = ext.transformInfo.fieldRenames.get().lastOrNull { it.from.get().let { it.cls.get() == owner && it.desc.get() == descriptor } && it.to.get() == name }?.from?.get()?.name?.get() ?: name

        annotations.getClasses(Annotations.NAMESPACE) { cls, values ->
            if (cls == owner) {
                values[Annotations.NAMESPACE_VALUE]?.let {
                    name = "$it$$name"
                }
            }
        }

        annotations.getFields(Annotations.NAMESPACE) { field, values ->
            if (field.cls.get() == owner && field.name.get() == name && field.desc.get() == descriptor) {
                values[Annotations.NAMESPACE_VALUE]?.let {
                    if (!name.startsWith("$it$")) {
                        name = "$it$$name"
                    }
                }
            }
        }

        return name
    }
}