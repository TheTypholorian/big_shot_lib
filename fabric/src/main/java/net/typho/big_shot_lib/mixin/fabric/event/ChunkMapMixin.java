package net.typho.big_shot_lib.mixin.fabric.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.LevelChunk;
import net.typho.big_shot_lib.api.client.rendering.event.ClientChunkChangedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {
    @Inject(
            method = "lambda$scheduleUnload$12",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/LevelChunk;setLoaded(Z)V"
            )
    )
    private void scheduleUnload(
            ChunkHolder chunkHolder,
            long chunkPos,
            CallbackInfo ci,
            @Local LevelChunk chunk
    ) {
        ClientChunkChangedEvent.Companion.invoke(chunk, null);
    }
}
