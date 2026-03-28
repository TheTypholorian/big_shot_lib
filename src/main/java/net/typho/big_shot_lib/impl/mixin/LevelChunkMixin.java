package net.typho.big_shot_lib.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.typho.big_shot_lib.api.math.vec.NeoVec3i;
import net.typho.big_shot_lib.impl.util.BigShotCommonEvents;
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
    Level level;

    @Inject(
            method = "setBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
            )
    )
    public void setBlockState(
            BlockPos pos,
            BlockState state,
            //? if <1.21.11 {
            /*boolean isMoving,
            *///? } else {
            int updateFlags,
            //? }
            CallbackInfoReturnable<BlockState> cir,
            @Local(ordinal = 1) BlockState oldState
    ) {
        BigShotCommonEvents.INSTANCE.getBlockChanged().forEach(event -> event.invoke(level, new NeoVec3i(pos), oldState, state));
    }
}
