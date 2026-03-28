package net.typho.big_shot_lib.impl.client.rendering.opengl.state

import com.mojang.blaze3d.platform.GlStateManager
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.*
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.state.*
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask
import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilOp
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
import org.lwjgl.opengl.GL31.GL_PRIMITIVE_RESTART
import org.lwjgl.opengl.GL32.*
import org.lwjgl.opengl.GL40.GL_SAMPLE_SHADING
import org.lwjgl.opengl.GL41.GL_PROGRAM_PIPELINE_BINDING
import org.lwjgl.opengl.GL41.glBindProgramPipeline
import org.lwjgl.opengl.GL43.*
import org.lwjgl.system.MemoryStack

object NeoGlStateManagerImpl : NeoGlStateManager {
    internal var currentTarget: GlFramebuffer? = null

    override val buffers: EnumArrayMap<GlBufferTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
        GlStateStack.Impl(
            { GlStateManager._glBindBuffer(target.glId, it) },
            { glGetInteger(target.bindingId) }
        )
    }
    override val program: GlStateStack<Int> = GlStateStack.Impl(
        { GlStateManager._glUseProgram(it) },
        { glGetInteger(GL_CURRENT_PROGRAM) }
    )
    override val programPipeline: GlStateStack<Int> = GlStateStack.Impl(
        { glBindProgramPipeline(it) },
        { glGetInteger(GL_PROGRAM_PIPELINE_BINDING) }
    )
    override val vertexArray: GlStateStack<Int> = GlStateStack.Impl(
        { GlStateManager._glBindVertexArray(it) },
        { glGetInteger(GL_VERTEX_ARRAY_BINDING) }
    )
    override val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
        if (target == GlTextureTarget.TEXTURE_2D)
            GlStateStack.Impl(
                { GlStateManager._bindTexture(it) },
                { GlStateManager.TEXTURES[activeTexture].binding }
            )
        else
            GlStateStack.Impl(
                { glBindTexture(target.glId, it) },
                { glGetInteger(target.bindingId) }
            )
    }
    override val renderbuffer: GlStateStack<Int> = GlStateStack.Impl(
        //? if <1.21.5 {
        { GlStateManager._glBindRenderbuffer(GL_RENDERBUFFER, it) },
        //? } else {
        /*{ glBindRenderbuffer(GL_RENDERBUFFER, it) },
        *///? }
        { glGetInteger(GL_RENDERBUFFER_BINDING) }
    )
    override val framebuffer: GlStateStack<Int> = GlStateStack.Impl(
        { GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, it) },
        //? if <1.21.5 {
        { GlStateManager.getBoundFramebuffer() }
        //? } else {
        /*{ glGetInteger(GL_FRAMEBUFFER_BINDING) }
        *///? }
    )
    override var activeTexture: Int
        //? if <1.21.5 {
        get() = GlStateManager._getActiveTexture()
        //? } else {
        /*get() = GlStateManager.activeTexture
        *///? }
        set(value) = GlStateManager._activeTexture(value)

    override val blendColor: GlStateStack<NeoColor> = GlStateStack.Impl(
        { glBlendColor(it.redF, it.greenF, it.blueF, it.alphaF ?: 1f) },
        { NeoColor.RGBA(glGetInteger(GL_BLEND_COLOR)) }
    )
    override val blendEquation: GlStateStack<GlBlendEquation> = GlStateStack.Impl(
        { glBlendEquation(it.glId) },
        { GlNamed.getEnum(glGetInteger(GL_BLEND_EQUATION)) }
    )
    override val blendFunction: GlStateStack<BlendFunction> = GlStateStack.Impl(
        { it.bind() },
        { BlendFunction.Separate(
            GlNamed.getEnum(glGetInteger(GL_BLEND_SRC_RGB)),
            GlNamed.getEnum(glGetInteger(GL_BLEND_DST_RGB)),
            GlNamed.getEnum(glGetInteger(GL_BLEND_SRC_ALPHA)),
            GlNamed.getEnum(glGetInteger(GL_BLEND_DST_ALPHA)),
        ) }
    )
    override val colorMask: GlStateStack<ColorMask> = GlStateStack.Impl(
        { glColorMask(it.red, it.green, it.blue, it.alpha) },
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
        { glCullFace(it.glId) },
        { GlNamed.getEnum(glGetInteger(GL_CULL_FACE_MODE)) }
    )
    override val depthMask: GlStateStack<Boolean> = GlStateStack.Impl(
        { glDepthMask(it) },
        { glGetBoolean(GL_DEPTH_WRITEMASK) }
    )
    override val depthFunc: GlStateStack<GlAlphaFunction> = GlStateStack.Impl(
        { glDepthFunc(it.glId) },
        { GlNamed.getEnum(glGetInteger(GL_DEPTH_FUNC)) }
    )
    override val polygonMode: GlStateStack<GlPolygonMode> = GlStateStack.Impl(
        { glPolygonMode(GL_FRONT_AND_BACK, it.glId) },
        { GlNamed.getEnum(glGetInteger(GL_POLYGON_MODE)) }
    )
    override val polygonOffset: GlStateStack<PolygonOffset> = GlStateStack.Impl(
        { glPolygonOffset(it.factor, it.units) },
        {
            PolygonOffset(
                glGetFloat(GL_POLYGON_OFFSET_FACTOR),
                glGetFloat(GL_POLYGON_OFFSET_UNITS)
            )
        }
    )
    override val scissor: GlStateStack<AbstractRect2<Int>> = GlStateStack.Impl(
        { glScissor(it.min.x, it.min.y, it.size.x, it.size.y) },
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
        { glStencilFunc(it.func.glId, it.ref, it.mask) },
        {
            StencilFunction(
                GlNamed.getEnum(glGetInteger(GL_STENCIL_FUNC)),
                glGetInteger(GL_STENCIL_REF),
                glGetInteger(GL_STENCIL_VALUE_MASK)
            )
        }
    )
    override val stencilMask: GlStateStack<Int> = GlStateStack.Impl(
        { glStencilMask(it) },
        { glGetInteger(GL_STENCIL_WRITEMASK) }
    )
    override val stencilOp: GlStateStack<StencilOp> = GlStateStack.Impl(
        { glStencilOp(it.stencilFail.glId, it.depthFail.glId, it.depthPass.glId) },
        {
            StencilOp(
                GlNamed.getEnum(glGetInteger(GL_STENCIL_FAIL)),
                GlNamed.getEnum(glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL)),
                GlNamed.getEnum(glGetInteger(GL_STENCIL_PASS_DEPTH_PASS))
            )
        }
    )
    override val viewport: GlStateStack<AbstractRect2<Int>> = GlStateStack.Impl(
        { glViewport(it.min.x, it.min.y, it.size.x, it.size.y) },
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

    override val blendEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_BLEND)
    override val colorLogicOpEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_COLOR_LOGIC_OP)
    override val cullFaceEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_CULL_FACE)
    override val debugOutputEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_DEBUG_OUTPUT)
    override val debugOutputSynchronousEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_DEBUG_OUTPUT_SYNCHRONOUS)
    override val depthClampEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_DEPTH_CLAMP)
    override val depthEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_DEPTH_TEST)
    override val ditherEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_DITHER)
    override val framebufferSRGBEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_FRAMEBUFFER_SRGB)
    override val lineSmoothEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_LINE_SMOOTH)
    override val multisampleEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_MULTISAMPLE)
    override val polygonOffsetEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_POLYGON_OFFSET_FILL)
    override val polygonSmoothEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_POLYGON_SMOOTH)
    override val primitiveRestartEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_PRIMITIVE_RESTART)
    override val primitiveRestartFixedIndexEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_PRIMITIVE_RESTART_FIXED_INDEX)
    override val rasterizerDiscardEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_RASTERIZER_DISCARD)
    override val sampleAlphaToCoverageEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_SAMPLE_ALPHA_TO_COVERAGE)
    override val sampleAlphaToOneEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_SAMPLE_ALPHA_TO_ONE)
    override val sampleCoverageEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_SAMPLE_COVERAGE)
    override val sampleShadingEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_SAMPLE_SHADING)
    override val sampleMaskEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_SAMPLE_MASK)
    override val scissorEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_SCISSOR_TEST)
    override val stencilEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_STENCIL_TEST)
    override val textureCubeMapSeamlessEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_TEXTURE_CUBE_MAP_SEAMLESS)
    override val programPointSizeEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GL_PROGRAM_POINT_SIZE)
}