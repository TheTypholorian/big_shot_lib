package net.typho.big_shot_lib.impl.client.rendering.opengl.state

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.renderer.ShaderInstance
import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendEquation
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlCullFace
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlPolygonMode
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask
import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilOp
import net.typho.big_shot_lib.api.math.rect.IRect2
import net.typho.big_shot_lib.api.math.rect.NeoRect2i
import net.typho.big_shot_lib.api.util.KeyedDelegate
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.mutableEnumArrayMapOf
import org.lwjgl.opengl.ARBImaging.GL_BLEND_COLOR
import org.lwjgl.opengl.ARBImaging.GL_BLEND_EQUATION
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL14.glBlendColor
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL41.GL_PROGRAM_PIPELINE_BINDING
import org.lwjgl.opengl.GL41.glBindProgramPipeline
import org.lwjgl.system.MemoryStack
import kotlin.io.use

object NeoGlStateManagerImpl : NeoGlStateManager {
    @JvmField
    val boundBuffers = mutableEnumArrayMapOf<GlBufferTarget, Int> { 0 }
    @JvmField
    var boundVertexArray: Int = 0
    @JvmField
    var boundRenderbuffer: Int = 0
    @JvmField
    var boundFramebuffer: RenderTarget? = null
    @JvmField
    var currentBlendEquation: GlBlendEquation = GlBlendEquation.ADD
    @JvmField
    var currentBlendFunction: BlendFunction = BlendFunction.NONE
    @JvmField
    var currentScissor: IRect2<Int>? = null

    override val buffers: KeyedDelegate<GlBufferTarget, Int> = KeyedDelegate.of(
        { target -> boundBuffers[target] },
        { target, glId -> GlStateManager._glBindBuffer(target.glId, glId) }
    )
    override var program: GlProgram?
        get() = RenderSystem.getShader() as GlProgram
        set(value) = RenderSystem.setShader { value as ShaderInstance }
    override var vertexArray: Int
        get() = boundVertexArray
        set(value) = GlStateManager._glBindVertexArray(value)
    override var texture: Int
        get() = GlStateManager.TEXTURES[activeTexture].binding
        set(value) = GlStateManager._bindTexture(value)
    override var renderbuffer: Int
        get() = boundRenderbuffer
        set(value) = GlStateManager._glBindRenderbuffer(GL_RENDERBUFFER, value)
    override var framebuffer: RenderTarget?
        get() = boundFramebuffer
        set(value) {
            boundFramebuffer = value
            GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, value?.glId ?: 0)
        }
    override var activeTexture: Int
        get() = GlStateManager._getActiveTexture() - GL_TEXTURE0
        set(value) = GlStateManager._activeTexture(value + GL_TEXTURE0)
    override var blendColor: NeoColor = NeoColor.FULL_ON
        set(value) {
            field = value
            glBlendColor(value.redF, value.greenF, value.blueF, value.alphaF ?: 1f)
        }
    override var blendEquation: GlBlendEquation
        get() = currentBlendEquation
        set(value) = GlStateManager._blendEquation(value.glId)
    override var blendFunction: BlendFunction
        get() = currentBlendFunction
        set(value) = when (value) {
            is BlendFunction.Basic -> GlStateManager._blendFunc(value.src.glId, value.dest.glId)
            is BlendFunction.Separate -> GlStateManager._blendFuncSeparate(value.src.glId, value.dest.glId, value.srcA.glId, value.destA.glId)
        }
    override var colorMask: ColorMask
        get() = ColorMask(
            GlStateManager.COLOR_MASK.red,
            GlStateManager.COLOR_MASK.green,
            GlStateManager.COLOR_MASK.blue,
            GlStateManager.COLOR_MASK.alpha
        )
        set(value) = GlStateManager._colorMask(value.red, value.green, value.blue, value.alpha)
    override var cullFace: GlCullFace
        get() = GlNamed.getEnum(GlStateManager.CULL.mode)
        set(value) {
            if (value.glId != GlStateManager.CULL.mode) {
                GlStateManager.CULL.mode = value.glId
                glCullFace(value.glId)
            }
        }
    override var depthMask: Boolean
        get() = GlStateManager.DEPTH.mask
        set(value) = GlStateManager._depthMask(value)
    override var depthFunc: GlAlphaFunction
        get() = GlNamed.getEnum(GlStateManager.DEPTH.func)
        set(value) = GlStateManager._depthFunc(value.glId)
    override var polygonOffset: PolygonOffset
        get() = PolygonOffset(
            GlStateManager.POLY_OFFSET.factor,
            GlStateManager.POLY_OFFSET.units
        )
        set(value) = GlStateManager._polygonOffset(value.factor, value.units)
    override var scissor: IRect2<Int>
        get() = currentScissor ?: MemoryStack.stackPush().use { stack ->
            val box = stack.mallocInt(4)
            glGetIntegerv(GL_SCISSOR_BOX, box)
            return NeoRect2i(
                box.get(0),
                box.get(1),
                box.get(0) + box.get(2),
                box.get(1) + box.get(3),
            )
        }
        set(value) = GlStateManager._scissorBox(value.min.x, value.min.y, value.size.x, value.size.y)
    override var stencilFunction: StencilFunction
        get() = StencilFunction(
            GlNamed.getEnum(GlStateManager.STENCIL.func.func),
            glGetInteger(GlStateManager.STENCIL.func.ref),
            glGetInteger(GlStateManager.STENCIL.func.mask)
        )
        set(value) = GlStateManager._stencilFunc(value.func.glId, value.ref, value.mask)
    override var stencilMask: Int
        get() = GlStateManager.STENCIL.mask
        set(value) = GlStateManager._stencilMask(value)
    override var stencilOp: StencilOp
        get() = StencilOp(
            GlNamed.getEnum(GlStateManager.STENCIL.fail),
            GlNamed.getEnum(GlStateManager.STENCIL.zfail),
            GlNamed.getEnum(GlStateManager.STENCIL.zpass)
        )
        set(value) = GlStateManager._stencilOp(value.stencilFail.glId, value.depthFail.glId, value.depthPass.glId)
    override var viewport: IRect2<Int>
        get() = NeoRect2i(
            GlStateManager.Viewport.x(),
            GlStateManager.Viewport.y(),
            GlStateManager.Viewport.x() + GlStateManager.Viewport.width(),
            GlStateManager.Viewport.y() + GlStateManager.Viewport.height()
        )
        set(value) = GlStateManager._viewport(value.min.x, value.min.y, value.size.x, value.size.y)
    override var blendEnabled: Boolean
        get() = GlStateManager.BLEND.mode.enabled
        set(value) = if (value) GlStateManager._enableBlend() else GlStateManager._disableBlend()
    override var colorLogicOpEnabled: Boolean
        get() = GlStateManager.COLOR_LOGIC.enable.enabled
        set(value) = if (value) GlStateManager._enableColorLogicOp() else GlStateManager._disableColorLogicOp()
    override var cullFaceEnabled: Boolean
        get() = GlStateManager.CULL.enable.enabled
        set(value) = if (value) GlStateManager._enableCull() else GlStateManager._disableCull()
    override var depthEnabled: Boolean
        get() = GlStateManager.DEPTH.mode.enabled
        set(value) = if (value) GlStateManager._enableDepthTest() else GlStateManager._disableDepthTest()
    override var polygonOffsetEnabled: Boolean
        get() = GlStateManager.POLY_OFFSET.fill.enabled
        set(value) = if (value) GlStateManager._enablePolygonOffset() else GlStateManager._disablePolygonOffset()
    override var scissorEnabled: Boolean
        get() = GlStateManager.SCISSOR.mode.enabled
        set(value) = if (value) GlStateManager._enableScissorTest() else GlStateManager._disableScissorTest()
    override var stencilEnabled: Boolean = false
        set(value) {
            if (field != value) {
                field = value

                if (value) {
                    glEnable(GL_STENCIL_TEST)
                } else {
                    glDisable(GL_STENCIL_TEST)
                }
            }
        }
    /*
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
    override val scissor: GlStateStack<IRect2<Int>> = GlStateStack.Impl(
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
    override val viewport: GlStateStack<IRect2<Int>> = GlStateStack.Impl(
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

    override val blendEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "BLEND",
        { if (it == true) GlStateManager._enableBlend() else GlStateManager._disableBlend() },
        { glIsEnabled(GL_BLEND) }
    )
    override val colorLogicOpEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "COLOR_LOGIC_OP",
        { if (it == true) GlStateManager._enableColorLogicOp() else GlStateManager._disableColorLogicOp() },
        { glIsEnabled(GL_COLOR_LOGIC_OP) }
    )
    override val cullFaceEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "CULL_FACE",
        { if (it == true) GlStateManager._enableCull() else GlStateManager._disableCull() },
        { glIsEnabled(GL_CULL_FACE) }
    )
    override val debugOutputEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEBUG_OUTPUT)
    override val debugOutputSynchronousEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEBUG_OUTPUT_SYNCHRONOUS)
    override val depthClampEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DEPTH_CLAMP)
    override val depthEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "DEPTH_TEST",
        { if (it == true) GlStateManager._enableDepthTest() else GlStateManager._disableDepthTest() },
        { glIsEnabled(GL_DEPTH_TEST) }
    )
    override val ditherEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.DITHER)
    override val framebufferSRGBEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.FRAMEBUFFER_SRGB)
    override val lineSmoothEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.LINE_SMOOTH)
    override val multisampleEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.MULTISAMPLE)
    override val polygonOffsetEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "POLYGON_OFFSET",
        { if (it == true) GlStateManager._enablePolygonOffset() else GlStateManager._disablePolygonOffset() },
        { glIsEnabled(GL_POLYGON_OFFSET_FILL) }
    )
    override val polygonSmoothEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.POLYGON_SMOOTH)
    override val primitiveRestartEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PRIMITIVE_RESTART)
    override val primitiveRestartFixedIndexEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PRIMITIVE_RESTART_FIXED_INDEX)
    override val rasterizerDiscardEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.RASTERIZER_DISCARD)
    override val sampleAlphaToCoverageEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_ALPHA_TO_COVERAGE)
    override val sampleAlphaToOneEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_ALPHA_TO_ONE)
    override val sampleCoverageEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_COVERAGE)
    override val sampleShadingEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_SHADING)
    override val sampleMaskEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.SAMPLE_MASK)
    override val scissorEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "SCISSOR_TEST",
        { if (it == true) GlStateManager._enableScissorTest() else GlStateManager._disableScissorTest() },
        { glIsEnabled(GL_SCISSOR_TEST) }
    )
    override val stencilEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.STENCIL_TEST)
    override val textureCubeMapSeamlessEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.TEXTURE_CUBE_MAP_SEAMLESS)
    override val programPointSizeEnabled: GlStateStack<Boolean> = GlStateStack.Flag(GlFlag.PROGRAM_POINT_SIZE)

    override fun rawBindTexture(
        target: GlTextureTarget,
        id: Int
    ) {
        if (target == GlTextureTarget.TEXTURE_2D) {
            GlStateManager._bindTexture(id)
        } else {
            glBindTexture(target.glId, id)
        }
    }
     */
}