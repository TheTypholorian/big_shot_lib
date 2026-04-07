package net.typho.big_shot_lib.impl.client.rendering.opengl.state

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.*
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.opengl.util.*
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.math.rect.NeoRect2i
import net.typho.big_shot_lib.api.util.EnumArrayMap
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.enumArrayMapOf
import org.lwjgl.opengl.ARBImaging.GL_BLEND_COLOR
import org.lwjgl.opengl.ARBImaging.GL_BLEND_EQUATION
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL41.GL_PROGRAM_PIPELINE_BINDING
import org.lwjgl.opengl.GL41.glBindProgramPipeline
import org.lwjgl.system.MemoryStack

object NeoGlStateManagerImpl : NeoGlStateManager {
    internal var currentTarget: GlFramebuffer? = null

    override val buffers: EnumArrayMap<GlBufferTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
        GlStateStack.Impl(
            target.name,
            { GlStateManager._glBindBuffer(target.glId, it ?: 0) },
            { glGetInteger(target.bindingId) }
        )
    }
    override val program: GlStateStack<Int> = GlStateStack.Impl(
        "PROGRAM",
        { GlStateManager._glUseProgram(it ?: 0) },
        { glGetInteger(GL_CURRENT_PROGRAM) }
    )
    override val programPipeline: GlStateStack<Int> = GlStateStack.Impl(
        "PROGRAM_PIPELINE",
        { glBindProgramPipeline(it ?: 0) },
        { glGetInteger(GL_PROGRAM_PIPELINE_BINDING) }
    )
    override val vertexArray: GlStateStack<Int> = GlStateStack.Impl(
        "VERTEX_ARRAY",
        { GlStateManager._glBindVertexArray(it ?: 0) },
        { glGetInteger(GL_VERTEX_ARRAY_BINDING) }
    )
    override val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
        if (target == GlTextureTarget.TEXTURE_2D)
            GlStateStack.Impl(
                target.name,
                { GlStateManager._bindTexture(it ?: 0) },
                { GlStateManager.TEXTURES[activeTexture].binding }
            )
        else
            GlStateStack.Impl(
                target.name,
                { glBindTexture(target.glId, it ?: 0) },
                { glGetInteger(target.bindingId) }
            )
    }
    override val renderbuffer: GlStateStack<Int> = GlStateStack.Impl(
        "RENDERBUFFER",
        //? if <1.21.5 {
        { GlStateManager._glBindRenderbuffer(GL_RENDERBUFFER, it ?: 0) },
        //? } else {
        /*{ glBindRenderbuffer(GL_RENDERBUFFER, it ?: 0) },
        *///? }
        { glGetInteger(GL_RENDERBUFFER_BINDING) }
    )
    override val framebuffer: GlStateStack<Int> = GlStateStack.Impl(
        "FRAMEBUFFER",
        { GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, it ?: 0) },
        //? if <1.21.5 {
        { GlStateManager.getBoundFramebuffer() }
        //? } else {
        /*{ glGetInteger(GL_FRAMEBUFFER_BINDING) }
        *///? }
    )
    override val readFramebuffer: GlStateStack<Int> = GlStateStack.Impl(
        "READ_FRAMEBUFFER",
        { GlStateManager._glBindFramebuffer(GL_READ_FRAMEBUFFER, it ?: 0) },
        { glGetInteger(GL_READ_FRAMEBUFFER_BINDING) }
    )
    override var activeTexture: Int
        //? if <1.21.5 {
        get() = GlStateManager._getActiveTexture() - GL_TEXTURE0
        //? } else {
        /*get() = GlStateManager.activeTexture
        *///? }
        set(value) = GlStateManager._activeTexture(value + GL_TEXTURE0)

    override val blendColor: GlStateStack<NeoColor> = GlStateStack.Impl(
        "BLEND_COLOR",
        { if (it != null) glBlendColor(it.redF, it.greenF, it.blueF, it.alphaF ?: 1f) },
        { NeoColor.RGBA(glGetInteger(GL_BLEND_COLOR)) }
    )
    override val blendEquation: GlStateStack<GlBlendEquation> = GlStateStack.Impl(
        "BLEND_EQUATION",
        { if (it != null) glBlendEquation(it.glId) },
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
}