package net.typho.big_shot_lib.plugin.transform

import org.objectweb.asm.commons.Remapper

class ProjectRemapper(
    @JvmField
    val info: NeoTransformParameters,
    api: Int
) : Remapper(api) {
    override fun map(internalName: String): String {
        return info.classRenames.get().lastOrNull { it.to.get() == internalName }?.from?.get() ?: internalName
    }

    override fun mapMethodName(owner: String, name: String, descriptor: String?): String {
        if (name == "<init>" || name == "<clinit>") {
            return name
        }

        return info.methodRenames.get().lastOrNull { it.from.get().let { it.cls.get() == owner && (descriptor == null || it.desc.get() == descriptor) } && it.to.get() == name }?.from?.get()?.name?.get() ?: name
    }

    override fun mapFieldName(owner: String, name: String, descriptor: String): String {
        return info.fieldRenames.get().lastOrNull { it.from.get().let { it.cls.get() == owner && it.desc.get() == descriptor } && it.to.get() == name }?.from?.get()?.name?.get() ?: name
    }
}