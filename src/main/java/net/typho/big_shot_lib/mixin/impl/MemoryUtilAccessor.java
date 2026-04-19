package net.typho.big_shot_lib.mixin.impl;

import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import sun.misc.Unsafe;

@Mixin(value = MemoryUtil.class, remap = false)
public interface MemoryUtilAccessor {
    @Accessor("UNSAFE")
    static Unsafe big_shot_lib$unsafe() {
        throw new AssertionError();
    }
}
