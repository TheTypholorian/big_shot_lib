package net.typho.big_shot_lib.mixin.impl.client;

import net.minecraft.data.models.model.TextureSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextureSlot.class)
public interface TextureSlotAccessor {
    @Invoker("create")
    static TextureSlot eye_spy$create(String id) {
        return null;
    }
}
