package net.typho.big_shot_lib.impl.client.rendering.opengl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import org.lwjgl.opengl.GL33.glBindSampler

class BoundMinecraftProgram(
    override val resource: GlProgram,
    private val setUniform: (name: String, value: GlUniform.() -> Unit) -> Unit,
    private val setTexture: (name: String, binding: GlTextureBinding) -> Unit,
    private val setTextureArray: (name: String, bindings: Array<out GlTextureBinding>) -> Unit,
) : GlBoundProgram {
    override val handle: GlStateStack.Handle<Int> = NeoGlStateManager.MAIN.program.push(resource.glId)
    val initialTextureUnit = NeoGlStateManager.MAIN.activeTexture
    val usedUnits = hashSetOf<Int>()

    override fun setUniform(
        name: String,
        value: GlUniform.() -> Unit
    ) {
        setUniform.invoke(name, value)
    }

    override fun setTexture(
        name: String,
        binding: GlTextureBinding
    ) {
        setTexture.invoke(name, binding)
    }

    override fun setTextureArray(
        name: String,
        vararg bindings: GlTextureBinding
    ) {
        setTextureArray.invoke(name, bindings)
    }

    override fun unbind() {
        super.unbind()
        NeoGlStateManager.MAIN.activeTexture = initialTextureUnit
        usedUnits.forEach { glBindSampler(it, 0) }
    }
}