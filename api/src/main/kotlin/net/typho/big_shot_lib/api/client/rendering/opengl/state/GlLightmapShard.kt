package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

data class GlLightmapShard(
    @JvmField
    val enabled: Boolean
) : GlDrawStateShard {
    override fun bind(): BoundResource {
        if (enabled) {
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer()

            return BoundResource {
                Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer()
            }
        } else {
            return BoundResource { }
        }
    }
}