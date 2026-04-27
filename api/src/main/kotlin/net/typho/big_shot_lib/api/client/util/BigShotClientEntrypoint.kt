package net.typho.big_shot_lib.api.client.util

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.ChatType
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.util.RenderLevelStage
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.util.ModEntrypoint
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import org.joml.FrustumIntersection
import org.joml.Matrix4f
import java.util.UUID

abstract class BigShotClientEntrypoint : ModEntrypoint() {
    abstract fun onInitializeClient()

    open fun addReloadListeners(
        out: (listener: NeoResourceManagerReloadListener) -> Unit
    ) {
    }

    open fun addF3Info(
        out: (location: NeoIdentifier, allowedWithReducedDebugInfo: Boolean, text: (out: (line: String) -> Unit) -> Unit) -> Unit
    ) {
    }

    open fun displayInitialScreens(
        out: (text: Component, onClose: () -> Unit) -> Unit
    ) {
    }

    open fun clientLevelChanged(
        old: ClientLevel?,
        new: ClientLevel?
    ) {
    }

    open fun renderHand(
        hand: InteractionHand,
        poseStack: PoseStack,
        buffers: MultiBufferSource,
        packedLight: Int,
        partialTick: Float,
        interpolatedPitch: Float,
        swingProgress: Float,
        equipProgress: Float,
        stack: ItemStack
    ) {
    }

    open fun renderLevel(
        stage: RenderLevelStage,
        levelRenderer: LevelRenderer,
        camera: NeoCamera,
        level: ClientLevel?,
        projMat: Matrix4f,
        modelViewMat: Matrix4f,
        frustum: FrustumIntersection,
        target: GlFramebuffer,
        renderTick: Int,
        partialTick: DeltaTracker
    ) {
    }

    open fun clientCommands(
        dispatcher: CommandDispatcher<CommandSourceStack>,
        context: CommandBuildContext
    ) {
    }

    open fun displayResized(
        windowWidth: Int,
        windowHeight: Int,
        framebufferWidth: Int,
        framebufferHeight: Int
    ) {
    }

    open fun renderTooltip(
        stack: ItemStack,
        graphics: GuiGraphics,
        x: Int,
        y: Int,
        font: Font,
        components: List<ClientTooltipComponent>
    ) {
    }

    open fun renderGui(
        graphics: GuiGraphics,
        partialTick: DeltaTracker
    ) {
    }

    open fun clientChatMessage(
        message: Component,
        type: ChatType.Bound?,
        sender: UUID
    ) {
    }

    open fun clientTick() {
    }

    companion object {
        val entrypoints by lazy { BigShotClientEntrypoint::class.loadServices() }
    }
}