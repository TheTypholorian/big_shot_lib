package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class RenderSettings(
    override val location: ResourceIdentifier,
    @JvmField
    val shards: List<RenderSettingShard>
) : NamedResource, GlBindable {
    override fun bind(pushStack: Boolean) {
        shards.forEach { it.bind(pushStack) }
    }

    override fun unbind(popStack: Boolean) {
        shards.forEach { it.unbind(popStack) }
    }
}