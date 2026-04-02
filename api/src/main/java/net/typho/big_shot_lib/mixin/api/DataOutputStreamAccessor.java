package net.typho.big_shot_lib.mixin.api;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.DataOutput;
import java.io.DataOutputStream;

@Mixin(DataOutputStream.class)
public interface DataOutputStreamAccessor {
    @Invoker("writeUTF")
    static int writeUTF(String str, DataOutput out) {
        throw new AssertionError();
    }
}
