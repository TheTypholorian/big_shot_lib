package net.typho.big_shot_lib.plugin.transform.util

import org.objectweb.asm.Type

object Annotations {
    @JvmStatic
    val ONLY_IN = "Lnet/typho/big_shot_lib/common/annotation/OnlyIn;"
    @JvmStatic
    val IS_RUNTIME_READY = "Lnet/typho/big_shot_lib/common/annotation/IsRuntimeReady;"
    @JvmStatic
    val PREFIX = "Lnet/typho/big_shot_lib/common/annotation/Prefix;"
    @JvmStatic
    val MIXIN = "Lorg/spongepowered/asm/mixin/Mixin;"
    @JvmStatic
    val DEPRECATED: String = Type.getDescriptor(Deprecated::class.java)
}