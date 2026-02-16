package net.typho.big_shot_lib.mixin.fabric.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import net.typho.big_shot_lib.api.client.rendering.event.ClientChunkChangedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkStatusTasks.class)
public class ChunkStatusTasksMixin {
    @Inject(
            method = "lambda$full$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/LevelChunk;registerAllBlockEntitiesAfterLevelLoad()V"
            )
    )
    private static void registerAllBlockEntitiesAfterLevelLoad(
            ChunkAccess chunk,
            WorldGenContext worldGenContext,
            GenerationChunkHolder generationchunkholder,
            CallbackInfoReturnable<ChunkAccess> cir,
            @Local LevelChunk levelChunk
    ) {
        ClientChunkChangedEvent.Companion.invoke(null, levelChunk);
    }
}
