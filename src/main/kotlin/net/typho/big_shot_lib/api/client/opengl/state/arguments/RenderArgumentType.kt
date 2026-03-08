package net.typho.big_shot_lib.api.client.opengl.state.arguments

import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.level.Level
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebuffer
import net.typho.big_shot_lib.api.client.opengl.state.Camera
import net.typho.big_shot_lib.api.client.opengl.util.TextureUtil
import net.typho.big_shot_lib.api.client.util.events.RenderEventData
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlas
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.joml.FrustumIntersection
import org.joml.Matrix4f

class RenderArgumentType<T>(
    override val location: ResourceIdentifier,
    @JvmField
    val default: () -> T?,
    vararg provides: RenderArgumentValueProvider<*, T>
) : NamedResource {
    constructor(
        location: ResourceIdentifier,
        vararg provides: RenderArgumentValueProvider<*, T>
    ) : this(location, { null }, *provides)

    override fun toString() = location.toString()

    companion object {
        @JvmField
        val BUFFERS = RenderArgumentType<MultiBufferSource>(BigShotApi.id("buffers"))
        @JvmField
        val CAMERA = RenderArgumentType<Camera>(BigShotApi.id("camera"))
        @JvmField
        val LEVEL = RenderArgumentType<Level>(BigShotApi.id("level"))
        @JvmField
        val PROJ_MAT = RenderArgumentType<Matrix4f>(BigShotApi.id("proj_mat"))
        @JvmField
        val INVERSE_PROJ_MAT = RenderArgumentType<Matrix4f>(BigShotApi.id("inverse_proj_mat"))
        @JvmField
        val MODEL_MAT = RenderArgumentType<Matrix4f>(BigShotApi.id("model_view_mat"))
        @JvmField
        val INVERSE_MODEL_MAT = RenderArgumentType<Matrix4f>(BigShotApi.id("inverse_model_view_mat"))
        @JvmField
        val FRUSTUM = RenderArgumentType<FrustumIntersection>(BigShotApi.id("frustum"))
        @JvmField
        val TARGET = RenderArgumentType<GlFramebuffer>(BigShotApi.id("target"))
        @JvmField
        val RENDER_EVENT_DATA = RenderArgumentType<RenderEventData>(
            BigShotApi.id("render_event_data"),
            RenderArgumentValueProvider(BUFFERS) { it.buffers },
            RenderArgumentValueProvider(CAMERA) { it.camera },
            RenderArgumentValueProvider(LEVEL) { it.level },
            RenderArgumentValueProvider(PROJ_MAT) { it.projMat },
            RenderArgumentValueProvider(INVERSE_PROJ_MAT) { it.inverseProjMat },
            RenderArgumentValueProvider(MODEL_MAT) { it.modelViewMat },
            RenderArgumentValueProvider(INVERSE_MODEL_MAT) { it.inverseModelViewMat },
            RenderArgumentValueProvider(FRUSTUM) { it.frustum },
            RenderArgumentValueProvider(TARGET) { it.target }
        )
        @JvmField
        val BLOCK_ATLAS = RenderArgumentType<NeoAtlas>(BigShotApi.id("block_atlas"), { TextureUtil.INSTANCE.blockAtlas })
    }
}