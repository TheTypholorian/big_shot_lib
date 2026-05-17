package net.typho.big_shot_lib.impl.client.rendering.opengl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import org.lwjgl.opengl.GL33.glBindSampler
import java.util.function.BiConsumer
import java.util.function.Consumer

class BoundMinecraftProgram(
    override val resource: GlProgram,
    private val setUniform: BiConsumer<String, Consumer<GlUniform>>,
    private val setTexture: BiConsumer<Int, GlTextureBinding>,
    private val setTextureArray: BiConsumer<Int, Array<out GlTextureBinding>>,
) : GlBoundProgram {
    override val handle: GlStateStack.Handle<Int> = NeoGlStateManager.MAIN.program.push(resource.glId)
    val initialTextureUnit = NeoGlStateManager.MAIN.activeTexture
    val usedUnits = hashSetOf<Int>()

    override fun setUniform(
        name: String,
        value: Consumer<GlUniform>
    ) {
        setUniform.accept(name, value)
    }

    override fun setTexture(
        index: Int,
        binding: GlTextureBinding
    ) {
        setTexture.accept(index, binding)
    }

    override fun setTextureArray(
        index: Int,
        vararg bindings: GlTextureBinding
    ) {
        setTextureArray.accept(index, bindings)
    }

    override fun unbind() {
        super.unbind()
        NeoGlStateManager.MAIN.activeTexture = initialTextureUnit
        usedUnits.forEach { glBindSampler(it, 0) }
    }
}