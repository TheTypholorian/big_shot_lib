package net.typho.big_shot_lib.api.render_queue.shards

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.GlManager
import net.typho.big_shot_lib.api.render_queue.RenderSettingShard
import net.typho.big_shot_lib.api.util.IColor

open class BlendShard(
    @JvmField
    val color: IColor,
    @JvmField
    val equation: BlendEquation,
    @JvmField
    val function: BlendFunction
) : RenderSettingShard.Basic(
    BlendShard,
    listOf(
        GlFlag.BLEND.bindable,
        Bindable.ofState(GlManager::blendColor, color, IColor.BLACK),
        equation,
        function
    )
) {
    companion object : RenderSettingShard.Type<BlendShard> {
        override fun getDefault(): BlendShard {
            TODO("Not yet implemented")
        }

        override fun codec(): Codec<BlendShard> {
            TODO("Not yet implemented")
        }

        override fun location(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "blend")
    }
}