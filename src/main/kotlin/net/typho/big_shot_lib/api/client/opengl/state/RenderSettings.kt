package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.state.shards.RenderSettingShard
import net.typho.big_shot_lib.api.client.opengl.util.GlAdvancedBindable
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import net.typho.big_shot_lib.api.client.opengl.util.GlLayeredBind
import net.typho.big_shot_lib.api.errors.GlBindException
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class RenderSettings(
    override val location: ResourceIdentifier,
    @JvmField
    val shards: List<RenderSettingShard>
) : NamedResource, GlAdvancedBindable {
    fun bind(args: (arguments: RenderArguments) -> Unit, pushStack: Boolean = true): GlBindResult {
        val arguments = RenderArguments()
        args(arguments)
        return bind(arguments, pushStack)
    }

    override fun bind(arguments: RenderArguments, pushStack: Boolean): GlBindResult {
        val result = GlBindResult.Multiple(shards.map { it.bind(arguments, pushStack) })

        if (result.success) {
            return result
        } else {
            throw GlBindException(result)
        }
    }

    fun layeredBind(args: (arguments: RenderArguments) -> Unit): GlLayeredBind {
        val arguments = RenderArguments()
        args(arguments)
        return GlLayeredBind(shards, arguments)
    }

    override fun unbind(popStack: Boolean) {
        shards.forEach { it.unbind(popStack) }
    }
}