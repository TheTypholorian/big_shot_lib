package net.typho.big_shot_lib.mixin.impl.data;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.function.Function;

@Mixin(TagLoader.class)
public interface TagLoaderAccessor<T> {
    @Accessor("idToValue")
    Function<Identifier, Optional<? extends T>> big_shot_lib$getIdToValue();
}
