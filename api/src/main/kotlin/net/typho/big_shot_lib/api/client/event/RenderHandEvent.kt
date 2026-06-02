package net.typho.big_shot_lib.api.client.event

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack

fun interface RenderHandEvent {
    fun renderHand(
        hand: InteractionHand,
        poseStack: PoseStack,
        buffers: MultiBufferSource,
        packedLight: Int,
        partialTick: Float,
        interpolatedPitch: Float,
        swingProgress: Float,
        equipProgress: Float,
        stack: ItemStack
    )
}