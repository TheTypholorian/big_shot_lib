package net.typho.big_shot_lib.mixin.neoforge.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.typho.big_shot_lib.BigShotCommonEventStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {
    @Shadow
    @Final
    ServerLevel level;

    @Inject(
            method = "lambda$scheduleUnload$12",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/LevelChunk;setLoaded(Z)V"
            )
    )
    private void scheduleUnload(
            ChunkHolder chunkHolder,
            CompletableFuture<?> completablefuture,
            long chunkPos,
            CallbackInfo ci,
            @Local LevelChunk chunk
    ) {
        BigShotCommonEventStorage.onChunkChanged.forEach(event -> event.invoke(level, chunk, null));
    }
}
