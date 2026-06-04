package net.typho.big_shot_lib.mixin.impl.data.tag;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagManager;
import net.typho.big_shot_lib.api.BigShotApi;
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent;
import net.typho.big_shot_lib.impl.NeoEventBusImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.*;
import java.util.function.Function;

@Mixin(TagManager.class)
public class TagManagerMixin {
    @ModifyArgs(
            method = "lambda$createLoader$3",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/tags/TagManager$LoadResult;<init>(Lnet/minecraft/resources/ResourceKey;Ljava/util/Map;)V"
            )
    )
    @SuppressWarnings("unchecked")
    private static <T> void load(Args args, @Local(argsOnly = true) TagLoader<T> loader) {
        ResourceKey<Registry<T>> key = args.get(0);
        Map<Identifier, Collection<Holder<T>>> tags = new HashMap<>(args.get(1));
        Function<Identifier, Optional<? extends T>> func = ((TagLoaderAccessor<T>) loader).big_shot_lib$getIdToValue();
        Map<TagKey<?>, Set<Identifier>> modifiedTags = new HashMap<>();

        for (RegisterDynamicTagsEvent event : NeoEventBusImpl.DYNAMIC_TAG_EVENTS) {
            event.register(new RegisterDynamicTagsEvent.Output() {
                @Override
                public <V> void add(@NotNull ResourceKey<Registry<V>> registry, @NotNull TagKey<? extends V> tag, @NotNull ResourceKey<? extends V>... entries) {
                    if (registry.equals(key) && entries.length > 0) {
                        modifiedTags.computeIfAbsent(tag, key -> new HashSet<>()).addAll(Arrays.stream(entries).map(ResourceKey::location).toList());

                        tags.compute(tag.location(), (key, value) -> {
                            if (value == null) {
                                value = new HashSet<>();
                            } else {
                                value = new HashSet<>(value);
                            }

                            for (ResourceKey<? extends V> entry : entries) {
                                value.add((Holder<T>) func.apply(entry.location()).orElseThrow(() -> new NullPointerException("Nonexistent entry " + entry + " while injecting dynamic tags into " + registry + "." + tag)));
                            }

                            return value;
                        });
                    }
                }
            });
        }

        long numEntries = modifiedTags.values().stream().mapToInt(Set::size).count();
        long numTags = modifiedTags.size();

        if (numEntries > 0) {
            BigShotApi.LOGGER.info("Loaded {} dynamic tag entries into {} tags of registry {}", numEntries, numTags, key.location());
        }

        args.set(1, tags);
    }
}
