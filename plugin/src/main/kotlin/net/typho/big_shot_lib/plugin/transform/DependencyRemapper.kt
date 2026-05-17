package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.MCVersion
import org.objectweb.asm.commons.Remapper

class DependencyRemapper(api: Int) : Remapper(api) {
    override fun map(internalName: String?): String? {
        return if (MCVersion.CURRENT < MCVersion.MC1_21_11) {
            when (internalName) {
                "net/minecraft/resources/ResourceLocation" -> "net/minecraft/resources/Identifier"
                else -> internalName
            }
        } else {
            internalName
        }
    }
}