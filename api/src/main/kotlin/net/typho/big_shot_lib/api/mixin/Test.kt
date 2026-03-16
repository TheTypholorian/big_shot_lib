package net.typho.big_shot_lib.api.mixin

import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Desc
import org.spongepowered.asm.mixin.injection.Slice

annotation class Test(
    val method: Array<String> = [],
    val target: Array<Desc> = [],
    val slice: Array<Slice> = [],
    val at: At,
    val remap: Boolean = true
)
