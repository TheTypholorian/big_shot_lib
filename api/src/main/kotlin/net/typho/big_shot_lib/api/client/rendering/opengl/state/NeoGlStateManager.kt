package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.*
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask
import net.typho.big_shot_lib.api.client.rendering.opengl.util.GlFlag
import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilOp
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.math.rect.NeoRect2i
import net.typho.big_shot_lib.api.util.EnumArrayMap
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.enumArrayMapOf
import org.lwjgl.opengl.ARBImaging.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL14.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.NativeResource
import kotlin.use

interface NeoGlStateManager {
    val buffers: EnumArrayMap<GlBufferTarget, GlStateStack<Int>>
    val program: GlStateStack<Int>
    val programPipeline: GlStateStack<Int>
    val vertexArray: GlStateStack<Int>
    val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>>
    val renderbuffer: GlStateStack<Int>
    val framebuffer: GlStateStack<Int>
    val readFramebuffer: GlStateStack<Int>

    var activeTexture: Int

    val blendColor: GlStateStack<NeoColor>
    val blendEquation: GlStateStack<GlBlendEquation>
    val blendFunction: GlStateStack<BlendFunction>
    val colorMask: GlStateStack<ColorMask>
    val cullFace: GlStateStack<GlCullFace>
    val depthMask: GlStateStack<Boolean>
    val depthFunc: GlStateStack<GlAlphaFunction>
    val polygonMode: GlStateStack<GlPolygonMode>
    val polygonOffset: GlStateStack<PolygonOffset>
    val scissor: GlStateStack<AbstractRect2<Int>>
    val stencilFunction: GlStateStack<StencilFunction>
    val stencilMask: GlStateStack<Int>
    val stencilOp: GlStateStack<StencilOp>
    val viewport: GlStateStack<AbstractRect2<Int>>

    val blendEnabled: GlStateStack<Boolean>
    val colorLogicOpEnabled: GlStateStack<Boolean>
    val cullFaceEnabled: GlStateStack<Boolean>
    val debugOutputEnabled: GlStateStack<Boolean>
    val debugOutputSynchronousEnabled: GlStateStack<Boolean>
    val depthClampEnabled: GlStateStack<Boolean>
    val depthEnabled: GlStateStack<Boolean>
    val ditherEnabled: GlStateStack<Boolean>
    val framebufferSRGBEnabled: GlStateStack<Boolean>
    val lineSmoothEnabled: GlStateStack<Boolean>
    val multisampleEnabled: GlStateStack<Boolean>
    val polygonOffsetEnabled: GlStateStack<Boolean>
    val polygonSmoothEnabled: GlStateStack<Boolean>
    val primitiveRestartEnabled: GlStateStack<Boolean>
    val primitiveRestartFixedIndexEnabled: GlStateStack<Boolean>
    val rasterizerDiscardEnabled: GlStateStack<Boolean>
    val sampleAlphaToCoverageEnabled: GlStateStack<Boolean>
    val sampleAlphaToOneEnabled: GlStateStack<Boolean>
    val sampleCoverageEnabled: GlStateStack<Boolean>
    val sampleShadingEnabled: GlStateStack<Boolean>
    val sampleMaskEnabled: GlStateStack<Boolean>
    val scissorEnabled: GlStateStack<Boolean>
    val stencilEnabled: GlStateStack<Boolean>
    val textureCubeMapSeamlessEnabled: GlStateStack<Boolean>
    val programPointSizeEnabled: GlStateStack<Boolean>

    fun rawBindTexture(target: GlTextureTarget, id: Int)

    fun createCache(): Cache

    interface Cache : NativeResource {
        fun flush()
    }

    class Standalone : NeoGlStateManager {
        override val buffers: EnumArrayMap<GlBufferTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
            GlStateStack.Impl(
                target.name,
                { glBindBuffer(target.glId, it ?: 0) },
                { glGetInteger(target.bindingId) }
            )
        }
        override val program: GlStateStack<Int> = GlStateStack.Impl(
            "PROGRAM",
            { glUseProgram(it ?: 0) },
            { glGetInteger(GL_CURRENT_PROGRAM) }
        )
        override val programPipeline: GlStateStack<Int> = GlStateStack.Impl(
            "PROGRAM_PIPELINE",
            { glBindProgramPipeline(it ?: 0) },
            { glGetInteger(GL_PROGRAM_PIPELINE_BINDING) }
        )
        override val vertexArray: GlStateStack<Int> = GlStateStack.Impl(
            "VERTEX_ARRAY",
            { glBindVertexArray(it ?: 0) },
            { glGetInteger(GL_VERTEX_ARRAY_BINDING) }
        )
        override val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
            GlStateStack.Impl(
                target.name,
                { glBindTexture(target.glId, it ?: 0) },
                { glGetInteger(target.bindingId) }
            )
        }
        override val renderbuffer: GlStateStack<Int> = GlStateStack.Impl(
            "RENDERBUFFER",
            //? if <1.21.5 {
            { glBindRenderbuffer(GL_RENDERBUFFER, it ?: 0) },
            //? } else {
            /*{ glBindRenderbuffer(GL_RENDERBUFFER, it ?: 0) },
            *///? }
            { glGetInteger(GL_RENDERBUFFER_BINDING) }
        )
        override val framebuffer: GlStateStack<Int> = GlStateStack.Impl(
            "FRAMEBUFFER",
            { glBindFramebuffer(GL_FRAMEBUFFER, it ?: 0) },
            { glGetInteger(GL_FRAMEBUFFER_BINDING) }
        )
        override val readFramebuffer: GlStateStack<Int> = GlStateStack.Impl(
            "READ_FRAMEBUFFER",
            { glBindFramebuffer(GL_READ_FRAMEBUFFER, it ?: 0) },
            { glGetInteger(GL_READ_FRAMEBUFFER_BINDING) }
        )
        override var activeTexture: Int
            get() = glGetInteger(GL_ACTIVE_TEXTURE) - GL_TEXTURE0
            set(value) = glActiveTexture(value + GL_TEXTURE0)

        override val blendColor: GlStateStack<NeoColor> = GlStateStack.Impl(
            "BLEND_COLOR",
            { if (it != null) GL14.glBlendColor(it.redF, it.greenF, it.blueF, it.alphaF ?: 1f) },
            { NeoColor.RGBA(glGetInteger(GL_BLEND_COLOR)) }
        )
        override val blendEquation: GlStateStack<GlBlendEquation> = GlStateStack.Impl(
            "BLEND_EQUATION",
            { if (it != null) GL14.glBlendEquation(it.glId) },
            { GlNamed.getEnum(glGetInteger(GL_BLEND_EQUATION)) }
        )
        override val blendFunction: GlStateStack<BlendFunction> = GlStateStack.Impl(
            "BLEND_FUNCTION",
            { it?.bind() },
            { BlendFunction.Separate(
                GlNamed.getEnum(glGetInteger(GL_BLEND_SRC_RGB)),
                GlNamed.getEnum(glGetInteger(GL_BLEND_DST_RGB)),
                GlNamed.getEnum(glGetInteger(GL_BLEND_SRC_ALPHA)),
                GlNamed.getEnum(glGetInteger(GL_BLEND_DST_ALPHA)),
            ) }
        )
        override val colorMask: GlStateStack<ColorMask> = GlStateStack.Impl(
            "COLOR_MASK",
            { glColorMask(it?.red ?: true, it?.green ?: true, it?.blue ?: true, it?.alpha ?: true) },
            {
                MemoryStack.stackPush().use { stack ->
                    val mask = stack.malloc(4)
                    glGetBooleanv(GL_COLOR_WRITEMASK, mask)
                    return@Impl ColorMask(
                        mask.get(0).toInt() == GL_TRUE,
                        mask.get(1).toInt() == GL_TRUE,
                        mask.get(2).toInt() == GL_TRUE,
                        mask.get(3).toInt() == GL_TRUE,
                    )
                }
            }
        )
        override val cullFace: GlStateStack<GlCullFace> = GlStateStack.Impl(
            "CULL_FACE",
            { glCullFace((it ?: GlCullFace.BACK).glId) },
            { GlNamed.getEnum(glGetInteger(GL_CULL_FACE_MODE)) }
        )
        override val depthMask: GlStateStack<Boolean> = GlStateStack.Impl(
            "DEPTH_MASK",
            { glDepthMask(it ?: true) },
            { glGetBoolean(GL_DEPTH_WRITEMASK) }
        )
        override val depthFunc: GlStateStack<GlAlphaFunction> = GlStateStack.Impl(
            "DEPTH_FUNC",
            { glDepthFunc((it ?: GlAlphaFunction.LEQUAL).glId) },
            { GlNamed.getEnum(glGetInteger(GL_DEPTH_FUNC)) }
        )
        override val polygonMode: GlStateStack<GlPolygonMode> = GlStateStack.Impl(
            "POLYGON_MODE",
            { glPolygonMode(GL_FRONT_AND_BACK, (it ?: GlPolygonMode.FILL).glId) },
            { GlNamed.getEnum(glGetInteger(GL_POLYGON_MODE)) }
        )
        override val polygonOffset: GlStateStack<PolygonOffset> = GlStateStack.Impl(
            "POLYGON_OFFSET",
            { glPolygonOffset(it?.factor ?: 0f, it?.units ?: 0f) },
            {
                PolygonOffset(
                    glGetFloat(GL_POLYGON_OFFSET_FACTOR),
                    glGetFloat(GL_POLYGON_OFFSET_UNITS)
                )
            }
        )
        override val scissor: GlStateStack<AbstractRect2<Int>> = GlStateStack.Impl(
            "SCISSOR",
            { if (it != null) glScissor(it.min.x, it.min.y, it.size.x, it.size.y) },
            {
                MemoryStack.stackPush().use { stack ->
                    val box = stack.mallocInt(4)
                    glGetIntegerv(GL_SCISSOR_BOX, box)
                    return@Impl NeoRect2i(
                        box.get(0),
                        box.get(1),
                        box.get(0) + box.get(2),
                        box.get(1) + box.get(3),
                    )
                }
            }
        )
        override val stencilFunction: GlStateStack<StencilFunction> = GlStateStack.Impl(
            "STENCIL_FUNCTION",
            { if (it != null) glStencilFunc(it.func.glId, it.ref, it.mask) },
            {
                StencilFunction(
                    GlNamed.getEnum(glGetInteger(GL_STENCIL_FUNC)),
                    glGetInteger(GL_STENCIL_REF),
                    glGetInteger(GL_STENCIL_VALUE_MASK)
                )
            }
        )
        override val stencilMask: GlStateStack<Int> = GlStateStack.Impl(
            "STENCIL_MASK",
            { glStencilMask(it ?: 0xFFFFFFFF.toInt()) },
            { glGetInteger(GL_STENCIL_WRITEMASK) }
        )
        override val stencilOp: GlStateStack<StencilOp> = GlStateStack.Impl(
            "STENCIL_OP",
            { if (it != null) glStencilOp(it.stencilFail.glId, it.depthFail.glId, it.depthPass.glId) },
            {
                StencilOp(
                    GlNamed.getEnum(glGetInteger(GL_STENCIL_FAIL)),
                    GlNamed.getEnum(glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL)),
                    GlNamed.getEnum(glGetInteger(GL_STENCIL_PASS_DEPTH_PASS))
                )
            }
        )
        override val viewport: GlStateStack<AbstractRect2<Int>> = GlStateStack.Impl(
            "VIEWPORT",
            { if (it != null) glViewport(it.min.x, it.min.y, it.size.x, it.size.y) },
            {
                MemoryStack.stackPush().use { stack ->
                    val box = stack.mallocInt(4)
                    glGetIntegerv(GL_VIEWPORT, box)
                    return@Impl NeoRect2i(
                        box.get(0),
                        box.get(1),
                        box.get(0) + box.get(2),
                        box.get(1) + box.get(3),
                    )
                }
            }
        )

        override val blendEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.BLEND)
        override val colorLogicOpEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.COLOR_LOGIC_OP)
        override val cullFaceEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.CULL_FACE)
        override val debugOutputEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEBUG_OUTPUT)
        override val debugOutputSynchronousEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEBUG_OUTPUT_SYNCHRONOUS)
        override val depthClampEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEPTH_CLAMP)
        override val depthEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEPTH_TEST)
        override val ditherEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DITHER)
        override val framebufferSRGBEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.FRAMEBUFFER_SRGB)
        override val lineSmoothEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.LINE_SMOOTH)
        override val multisampleEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.MULTISAMPLE)
        override val polygonOffsetEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.POLYGON_OFFSET)
        override val polygonSmoothEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.POLYGON_SMOOTH)
        override val primitiveRestartEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PRIMITIVE_RESTART)
        override val primitiveRestartFixedIndexEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PRIMITIVE_RESTART_FIXED_INDEX)
        override val rasterizerDiscardEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.RASTERIZER_DISCARD)
        override val sampleAlphaToCoverageEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_ALPHA_TO_COVERAGE)
        override val sampleAlphaToOneEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_ALPHA_TO_ONE)
        override val sampleCoverageEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_COVERAGE)
        override val sampleShadingEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_SHADING)
        override val sampleMaskEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_MASK)
        override val scissorEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SCISSOR_TEST)
        override val stencilEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.STENCIL_TEST)
        override val textureCubeMapSeamlessEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.TEXTURE_CUBE_MAP_SEAMLESS)
        override val programPointSizeEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PROGRAM_POINT_SIZE)

        override fun rawBindTexture(target: GlTextureTarget, id: Int) {
            glBindTexture(target.glId, id)
        }
    }

    companion object {
        val MAIN by lazy { NeoGlStateManager::class.loadService() }
        val CURRENT: NeoGlStateManager
            get() = RenderingContext.get().glManager
    }
}