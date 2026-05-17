package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.DependencyTransformAction
import net.typho.big_shot_lib.plugin.MCVersion
import org.objectweb.asm.commons.Remapper

class DependencyRemapper(
    @JvmField
    val info: DependencyTransformAction.Parameters,
    api: Int
) : Remapper(api) {
    override fun map(internalName: String): String {
        if (MCVersion.CURRENT < MCVersion.MC1_21_11 && internalName == "net/minecraft/resources/ResourceLocation") {
            return "net/minecraft/resources/Identifier"
        }

        return info.classRenames.get().lastOrNull { it.from.get() == internalName }?.to?.get() ?: internalName
    }

    override fun mapMethodName(owner: String, name: String, descriptor: String): String {
        if (name == "<init>" || name == "<clinit>") {
            return name
        }

        val owner = map(owner)
        val descriptor = mapMethodDesc(descriptor)

        return info.methodRenames.get().lastOrNull { it.from.get().let { it.cls.get() == owner && it.name.get() == name && it.desc.get() == descriptor } }?.to?.get() ?: name
    }

    override fun mapFieldName(owner: String, name: String, descriptor: String): String {
        val owner = map(owner)
        val descriptor = mapDesc(descriptor)

        return info.fieldRenames.get().lastOrNull { it.from.get().let { it.cls.get() == owner && it.name.get() == name && it.desc.get() == descriptor } }?.to?.get() ?: name
    }
}