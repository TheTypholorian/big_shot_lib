package net.typho.big_shot_lib.mixin.resource;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.typho.big_shot_lib.resource.ResourceRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.LinkedList;
import java.util.List;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {
    @Shadow
    @Final
    private PackType type;

    @ModifyArg(
            method = "createReload",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"
            ),
            index = 1
    )
    private List<PreparableReloadListener> createReload(List<PreparableReloadListener> listeners) {
        if (type != PackType.CLIENT_RESOURCES) {
            return listeners;
        }

        List<PreparableReloadListener> newList = new LinkedList<>(listeners);
        newList.addAll(ResourceRegistry.registries);
        return newList;
    }
}
