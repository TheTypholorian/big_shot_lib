package net.typho.big_shot_lib.mixin.neoforge.event;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.common.NeoForge;
import net.typho.big_shot_lib.impl.event.NeoForgeBlockStateChangedEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public class LevelChunkMixin {
    @Shadow
    @Final
    private Level level;

    @Inject(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
            )
    )
    public void setBlockState(
            CallbackInfoReturnable<BlockState> cir,
            @Local(argsOnly = true) BlockState state,
            @Local(argsOnly = true) BlockPos pos,
            @Local(ordinal = 1) BlockState oldState
    ) {
        NeoForge.EVENT_BUS.post(new NeoForgeBlockStateChangedEvent(level, pos, oldState, state));
    }
}
