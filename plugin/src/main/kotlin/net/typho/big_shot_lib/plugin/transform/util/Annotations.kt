package net.typho.big_shot_lib.plugin.transform.util

import org.objectweb.asm.Type

object Annotations {
    @JvmStatic
    val ONLY_IN = "Lnet/typho/big_shot_lib/api/plugin/OnlyIn;"
    @JvmStatic
    val IS_RUNTIME_READY = "Lnet/typho/big_shot_lib/api/plugin/IsRuntimeReady;"
    @JvmStatic
    val MIXIN = "Lorg/spongepowered/asm/mixin/Mixin;"
    @JvmStatic
    val METADATA: String = Type.getDescriptor(Metadata::class.java)
    @JvmStatic
    val DEPRECATED: String = Type.getDescriptor(Deprecated::class.java)
}