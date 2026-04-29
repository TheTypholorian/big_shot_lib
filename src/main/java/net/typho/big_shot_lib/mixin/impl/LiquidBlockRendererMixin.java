package net.typho.big_shot_lib.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.typho.big_shot_lib.api.math.NeoDirection;
import net.typho.big_shot_lib.api.math.NeoDirectionKt;
import net.typho.big_shot_lib.impl.client.rendering.util.VertexConsumerWrapper;
import net.typho.big_shot_lib.impl.util.FluidQuadConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {
    @WrapOperation(
            method = "tesselate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;isFaceOccludedByNeighbor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;FLnet/minecraft/world/level/block/state/BlockState;)Z"
            )
    )
    private boolean isFaceOccludedByNeighbor(
            BlockGetter blockGetter,
            BlockPos blockPos,
            Direction direction,
            float f,
            BlockState blockState,
            Operation<Boolean> original,
            @Local(argsOnly = true) VertexConsumer consumer
    ) {
        if (consumer instanceof VertexConsumerWrapper wrapper && wrapper.inner instanceof FluidQuadConsumer fluid) {
            NeoDirection direction1 = NeoDirectionKt.getNeo(direction);
            fluid.direction = direction1;

            if (fluid.occlusionCheck.invoke(blockGetter, blockPos, direction1, blockState)) {
                return true;
            }
        }

        return original.call(blockGetter, blockPos, direction, f, blockState);
    }
}
