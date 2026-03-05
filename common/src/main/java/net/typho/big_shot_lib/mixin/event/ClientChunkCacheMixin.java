package net.typho.big_shot_lib.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.typho.big_shot_lib.BigShotCommonEventStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ClientChunkCache.class)
public class ClientChunkCacheMixin {
    @Shadow
    @Final
    ClientLevel level;

    @Inject(
            method = "drop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;drop(ILnet/minecraft/world/level/chunk/LevelChunk;)V"
            )
    )
    private void drop(
            ChunkPos chunkPos,
            CallbackInfo ci,
            @Local LevelChunk chunk
    ) {
        BigShotCommonEventStorage.onChunkChanged.forEach(event -> event.invoke(level, chunk, null));
    }

    @Inject(
            method = "replaceWithPacketData",
            at = @At("TAIL")
    )
    private void replaceWithPacketData(
            int x,
            int z,
            FriendlyByteBuf buffer,
            CompoundTag tag,
            Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer,
            CallbackInfoReturnable<LevelChunk> cir,
            @Local LevelChunk chunk
    ) {
        BigShotCommonEventStorage.onChunkChanged.forEach(event -> event.invoke(level, chunk, chunk));
    }
}
