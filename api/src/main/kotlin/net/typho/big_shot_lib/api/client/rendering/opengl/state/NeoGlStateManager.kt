package net.typho.big_shot_lib.api.client.rendering.opengl.state

import com.mojang.blaze3d.pipeline.RenderTarget
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.*
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask
import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilOp
import net.typho.big_shot_lib.api.math.IRect2
import net.typho.big_shot_lib.api.util.KeyedDelegate
import net.typho.big_shot_lib.api.util.NeoColor

interface NeoGlStateManager {
    val buffers: KeyedDelegate<GlBufferTarget, Int>
    var program: GlProgram?
    var vertexArray: Int
    var texture: Int
    var renderbuffer: Int
    var framebuffer: RenderTarget?

    var activeTexture: Int

    var blendColor: NeoColor
    var blendEquation: GlBlendEquation
    var blendFunction: BlendFunction
    var colorMask: ColorMask
    var cullFace: GlCullFace
    var depthMask: Boolean
    var depthFunc: GlAlphaFunction
    var polygonOffset: PolygonOffset
    var scissor: IRect2<Int>
    var stencilFunction: StencilFunction
    var stencilMask: Int
    var stencilOp: StencilOp
    var viewport: IRect2<Int>

    var blendEnabled: Boolean
    var colorLogicOpEnabled: Boolean
    var cullFaceEnabled: Boolean
    var depthEnabled: Boolean
    var polygonOffsetEnabled: Boolean
    var scissorEnabled: Boolean
    var stencilEnabled: Boolean

    /*
    class Standalone : NeoGlStateManager {
        override var buffers: EnumArrayMap<GlBufferTarget, Int> = enumArrayMapOf { target ->
            GlStateStack.Impl(
                target.name,
                { glBindBuffer(target.glId, it ?: 0) },
                { glGetInteger(target.bindingId) }
            )
        }
        override var program: Int = GlStateStack.Impl(
            "PROGRAM",
            { glUseProgram(it ?: 0) },
            { glGetInteger(GL_CURRENT_PROGRAM) }
        )
        override var programPipeline: Int = GlStateStack.Impl(
            "PROGRAM_PIPELINE",
            { glBindProgramPipeline(it ?: 0) },
            { glGetInteger(GL_PROGRAM_PIPELINE_BINDING) }
        )
        override var vertexArray: Int = GlStateStack.Impl(
            "VERTEX_ARRAY",
            { glBindVertexArray(it ?: 0) },
            { glGetInteger(GL_VERTEX_ARRAY_BINDING) }
        )
        override var textures: EnumArrayMap<GlTextureTarget, Int> = enumArrayMapOf { target ->
            GlStateStack.Impl(
                target.name,
                { glBindTexture(target.glId, it ?: 0) },
                { glGetInteger(target.bindingId) }
            )
        }
        override var renderbuffer: Int = GlStateStack.Impl(
            "RENDERBUFFER",
            //? if <1.21.5 {
            { glBindRenderbuffer(GL_RENDERBUFFER, it ?: 0) },
            //? } else {
            /*{ glBindRenderbuffer(GL_RENDERBUFFER, it ?: 0) },
            *///? }
            { glGetInteger(GL_RENDERBUFFER_BINDING) }
        )
        override var framebuffer: Int = GlStateStack.Impl(
            "FRAMEBUFFER",
            { glBindFramebuffer(GL_FRAMEBUFFER, it ?: 0) },
            { glGetInteger(GL_FRAMEBUFFER_BINDING) }
        )
        override var readFramebuffer: Int = GlStateStack.Impl(
            "READ_FRAMEBUFFER",
            { glBindFramebuffer(GL_READ_FRAMEBUFFER, it ?: 0) },
            { glGetInteger(GL_READ_FRAMEBUFFER_BINDING) }
        )
        override var activeTexture: Int
            get() = glGetInteger(GL_ACTIVE_TEXTURE) - GL_TEXTURE0
            set(varue) = glActiveTexture(varue + GL_TEXTURE0)

        override var blendColor: GlStateStack<NeoColor> = GlStateStack.Impl(
            "BLEND_COLOR",
            { if (it != null) GL14.glBlendColor(it.redF, it.greenF, it.blueF, it.alphaF ?: 1f) },
            { NeoColor.RGBA(glGetInteger(GL_BLEND_COLOR)) }
        )
        override var blendEquation: GlStateStack<GlBlendEquation> = GlStateStack.Impl(
            "BLEND_EQUATION",
            { if (it != null) GL14.glBlendEquation(it.glId) },
            { GlNamed.getEnum(glGetInteger(GL_BLEND_EQUATION)) }
        )
        override var blendFunction: GlStateStack<BlendFunction> = GlStateStack.Impl(
            "BLEND_FUNCTION",
            { it?.bind() },
            { BlendFunction.Separate(
                GlNamed.getEnum(glGetInteger(GL_BLEND_SRC_RGB)),
                GlNamed.getEnum(glGetInteger(GL_BLEND_DST_RGB)),
                GlNamed.getEnum(glGetInteger(GL_BLEND_SRC_ALPHA)),
                GlNamed.getEnum(glGetInteger(GL_BLEND_DST_ALPHA)),
            ) }
        )
        override var colorMask: GlStateStack<ColorMask> = GlStateStack.Impl(
            "COLOR_MASK",
            { glColorMask(it?.red ?: true, it?.green ?: true, it?.blue ?: true, it?.alpha ?: true) },
            {
                MemoryStack.stackPush().use { stack ->
                    var mask = stack.malloc(4)
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
        override var cullFace: GlStateStack<GlCullFace> = GlStateStack.Impl(
            "CULL_FACE",
            { glCullFace((it ?: GlCullFace.BACK).glId) },
            { GlNamed.getEnum(glGetInteger(GL_CULL_FACE_MODE)) }
        )
        override var depthMask: GlStateStack<Boolean> = GlStateStack.Impl(
            "DEPTH_MASK",
            { glDepthMask(it ?: true) },
            { glGetBoolean(GL_DEPTH_WRITEMASK) }
        )
        override var depthFunc: GlStateStack<GlAlphaFunction> = GlStateStack.Impl(
            "DEPTH_FUNC",
            { glDepthFunc((it ?: GlAlphaFunction.LEQUAL).glId) },
            { GlNamed.getEnum(glGetInteger(GL_DEPTH_FUNC)) }
        )
        override var polygonMode: GlStateStack<GlPolygonMode> = GlStateStack.Impl(
            "POLYGON_MODE",
            { glPolygonMode(GL_FRONT_AND_BACK, (it ?: GlPolygonMode.FILL).glId) },
            { GlNamed.getEnum(glGetInteger(GL_POLYGON_MODE)) }
        )
        override var polygonOffset: GlStateStack<PolygonOffset> = GlStateStack.Impl(
            "POLYGON_OFFSET",
            { glPolygonOffset(it?.factor ?: 0f, it?.units ?: 0f) },
            {
                PolygonOffset(
                    glGetFloat(GL_POLYGON_OFFSET_FACTOR),
                    glGetFloat(GL_POLYGON_OFFSET_UNITS)
                )
            }
        )
        override var scissor: GlStateStack<IRect2<Int>> = GlStateStack.Impl(
            "SCISSOR",
            { if (it != null) glScissor(it.min.x, it.min.y, it.size.x, it.size.y) },
            {
                MemoryStack.stackPush().use { stack ->
                    var box = stack.mallocInt(4)
                    glGetIntegerv(GL_SCISSOR_BOX, box)
                    return@Impl IRect2(
                        box.get(0),
                        box.get(1),
                        box.get(0) + box.get(2),
                        box.get(1) + box.get(3),
                    )
                }
            }
        )
        override var stencilFunction: GlStateStack<StencilFunction> = GlStateStack.Impl(
            "STENCIL_FUNCTION",
            { if (it != null) glStencilFunc(it.func.glId, it.ref, it.mask) },
            {
                StencilFunction(
                    GlNamed.getEnum(glGetInteger(GL_STENCIL_FUNC)),
                    glGetInteger(GL_STENCIL_REF),
                    glGetInteger(GL_STENCIL_varUE_MASK)
                )
            }
        )
        override var stencilMask: Int = GlStateStack.Impl(
            "STENCIL_MASK",
            { glStencilMask(it ?: 0xFFFFFFFF.toInt()) },
            { glGetInteger(GL_STENCIL_WRITEMASK) }
        )
        override var stencilOp: GlStateStack<StencilOp> = GlStateStack.Impl(
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
        override var viewport: GlStateStack<IRect2<Int>> = GlStateStack.Impl(
            "VIEWPORT",
            { if (it != null) glViewport(it.min.x, it.min.y, it.size.x, it.size.y) },
            {
                MemoryStack.stackPush().use { stack ->
                    var box = stack.mallocInt(4)
                    glGetIntegerv(GL_VIEWPORT, box)
                    return@Impl IRect2(
                        box.get(0),
                        box.get(1),
                        box.get(0) + box.get(2),
                        box.get(1) + box.get(3),
                    )
                }
            }
        )

        override var blendEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.BLEND)
        override var colorLogicOpEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.COLOR_LOGIC_OP)
        override var cullFaceEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.CULL_FACE)
        override var debugOutputEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEBUG_OUTPUT)
        override var debugOutputSynchronousEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEBUG_OUTPUT_SYNCHRONOUS)
        override var depthClampEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEPTH_CLAMP)
        override var depthEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEPTH_TEST)
        override var ditherEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DITHER)
        override var framebufferSRGBEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.FRAMEBUFFER_SRGB)
        override var lineSmoothEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.LINE_SMOOTH)
        override var multisampleEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.MULTISAMPLE)
        override var polygonOffsetEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.POLYGON_OFFSET)
        override var polygonSmoothEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.POLYGON_SMOOTH)
        override var primitiveRestartEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PRIMITIVE_RESTART)
        override var primitiveRestartFixedIndexEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PRIMITIVE_RESTART_FIXED_INDEX)
        override var rasterizerDiscardEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.RASTERIZER_DISCARD)
        override var sampleAlphaToCoverageEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_ALPHA_TO_COVERAGE)
        override var sampleAlphaToOneEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_ALPHA_TO_ONE)
        override var sampleCoverageEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_COVERAGE)
        override var sampleShadingEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_SHADING)
        override var sampleMaskEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_MASK)
        override var scissorEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SCISSOR_TEST)
        override var stencilEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.STENCIL_TEST)
        override var textureCubeMapSeamlessEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.TEXTURE_CUBE_MAP_SEAMLESS)
        override var programPointSizeEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PROGRAM_POINT_SIZE)

        override fun rawBindTexture(target: GlTextureTarget, id: Int) {
            glBindTexture(target.glId, id)
        }
    }
     */

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val INSTANCE by lazy { NeoGlStateManager::class.loadService() }
    }
}