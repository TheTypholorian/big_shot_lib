package net.typho.big_shot_lib.impl.client.rendering.opengl.state

import com.mojang.blaze3d.platform.GlStateManager
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
            Cache.buffers[target]::setValue,
            { glGetInteger(target.bindingId) }
        )
    }
    override val program: GlStateStack<Int> = GlStateStack.Impl(
        "PROGRAM",
        Cache.program::setValue,
        { glGetInteger(GL_CURRENT_PROGRAM) }
    )
    override val programPipeline: GlStateStack<Int> = GlStateStack.Impl(
        "PROGRAM_PIPELINE",
        Cache.programPipeline::setValue,
        { glGetInteger(GL_PROGRAM_PIPELINE_BINDING) }
    )
    override val vertexArray: GlStateStack<Int> = GlStateStack.Impl(
        "VERTEX_ARRAY",
        Cache.vertexArray::setValue,
        { glGetInteger(GL_VERTEX_ARRAY_BINDING) }
    )
    override val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>> = enumArrayMapOf { target ->
        if (target == GlTextureTarget.TEXTURE_2D)
            GlStateStack.Impl(
                target.name,
                Cache.textures[target]::setValue,
                { GlStateManager.TEXTURES[activeTexture].binding }
            )
        else
            GlStateStack.Impl(
                target.name,
                Cache.textures[target]::setValue,
                { glGetInteger(target.bindingId) }
            )
    }
    override val renderbuffer: GlStateStack<Int> = GlStateStack.Impl(
        "RENDERBUFFER",
        Cache.renderbuffer::setValue,
        { glGetInteger(GL_RENDERBUFFER_BINDING) }
    )
    override val framebuffer: GlStateStack<Int> = GlStateStack.Impl(
        "FRAMEBUFFER",
        Cache.framebuffer::setValue,
        //? if <1.21.5 {
        { GlStateManager.getBoundFramebuffer() }
        //? } else {
        /*{ glGetInteger(GL_FRAMEBUFFER_BINDING) }
        *///? }
    )
    override val readFramebuffer: GlStateStack<Int> = GlStateStack.Impl(
        "READ_FRAMEBUFFER",
        Cache.readFramebuffer::setValue,
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
        Cache.blendColor::setValue,
        { NeoColor.RGBA(glGetInteger(GL_BLEND_COLOR)) }
    )
    override val blendEquation: GlStateStack<GlBlendEquation> = GlStateStack.Impl(
        "BLEND_EQUATION",
        Cache.blendEquation::setValue,
        { GlNamed.getEnum(glGetInteger(GL_BLEND_EQUATION)) }
    )
    override val blendFunction: GlStateStack<BlendFunction> = GlStateStack.Impl(
        "BLEND_FUNCTION",
        Cache.blendFunction::setValue,
        { BlendFunction.Separate(
            GlNamed.getEnum(glGetInteger(GL_BLEND_SRC_RGB)),
            GlNamed.getEnum(glGetInteger(GL_BLEND_DST_RGB)),
            GlNamed.getEnum(glGetInteger(GL_BLEND_SRC_ALPHA)),
            GlNamed.getEnum(glGetInteger(GL_BLEND_DST_ALPHA)),
        ) }
    )
    override val colorMask: GlStateStack<ColorMask> = GlStateStack.Impl(
        "COLOR_MASK",
        Cache.colorMask::setValue,
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
        Cache.cullFace::setValue,
        { GlNamed.getEnum(glGetInteger(GL_CULL_FACE_MODE)) }
    )
    override val depthMask: GlStateStack<Boolean> = GlStateStack.Impl(
        "DEPTH_MASK",
        Cache.depthMask::setValue,
        { glGetBoolean(GL_DEPTH_WRITEMASK) }
    )
    override val depthFunc: GlStateStack<GlAlphaFunction> = GlStateStack.Impl(
        "DEPTH_FUNC",
        Cache.depthFunc::setValue,
        { GlNamed.getEnum(glGetInteger(GL_DEPTH_FUNC)) }
    )
    override val polygonMode: GlStateStack<GlPolygonMode> = GlStateStack.Impl(
        "POLYGON_MODE",
        Cache.polygonMode::setValue,
        { GlNamed.getEnum(glGetInteger(GL_POLYGON_MODE)) }
    )
    override val polygonOffset: GlStateStack<PolygonOffset> = GlStateStack.Impl(
        "POLYGON_OFFSET",
        Cache.polygonOffset::setValue,
        {
            PolygonOffset(
                glGetFloat(GL_POLYGON_OFFSET_FACTOR),
                glGetFloat(GL_POLYGON_OFFSET_UNITS)
            )
        }
    )
    override val scissor: GlStateStack<AbstractRect2<Int>> = GlStateStack.Impl(
        "SCISSOR",
        Cache.scissor::setValue,
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
        Cache.stencilFunction::setValue,
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
        Cache.stencilMask::setValue,
        { glGetInteger(GL_STENCIL_WRITEMASK) }
    )
    override val stencilOp: GlStateStack<StencilOp> = GlStateStack.Impl(
        "STENCIL_OP",
        Cache.stencilOp::setValue,
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
        Cache.viewport::setValue,
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
        Cache.flags[GlFlag.BLEND]::setValue,
        { glIsEnabled(GL_BLEND) }
    )
    override val colorLogicOpEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "COLOR_LOGIC_OP",
        Cache.flags[GlFlag.COLOR_LOGIC_OP]::setValue,
        { glIsEnabled(GL_COLOR_LOGIC_OP) }
    )
    override val cullFaceEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "CULL_FACE",
        Cache.flags[GlFlag.CULL_FACE]::setValue,
        { glIsEnabled(GL_CULL_FACE) }
    )
    override val debugOutputEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.DEBUG_OUTPUT, Cache.flags::get)
    override val debugOutputSynchronousEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.DEBUG_OUTPUT_SYNCHRONOUS, Cache.flags::get)
    override val depthClampEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.DEPTH_CLAMP, Cache.flags::get)
    override val depthEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "DEPTH_TEST",
        Cache.flags[GlFlag.DEPTH_TEST]::setValue,
        { glIsEnabled(GL_DEPTH_TEST) }
    )
    override val ditherEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.DITHER, Cache.flags::get)
    override val framebufferSRGBEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.FRAMEBUFFER_SRGB, Cache.flags::get)
    override val lineSmoothEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.LINE_SMOOTH, Cache.flags::get)
    override val multisampleEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.MULTISAMPLE, Cache.flags::get)
    override val polygonOffsetEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "POLYGON_OFFSET",
        Cache.flags[GlFlag.POLYGON_OFFSET]::setValue,
        { glIsEnabled(GL_POLYGON_OFFSET_FILL) }
    )
    override val polygonSmoothEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.POLYGON_SMOOTH, Cache.flags::get)
    override val primitiveRestartEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.PRIMITIVE_RESTART, Cache.flags::get)
    override val primitiveRestartFixedIndexEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.PRIMITIVE_RESTART_FIXED_INDEX, Cache.flags::get)
    override val rasterizerDiscardEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.RASTERIZER_DISCARD, Cache.flags::get)
    override val sampleAlphaToCoverageEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.SAMPLE_ALPHA_TO_COVERAGE, Cache.flags::get)
    override val sampleAlphaToOneEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.SAMPLE_ALPHA_TO_ONE, Cache.flags::get)
    override val sampleCoverageEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.SAMPLE_COVERAGE, Cache.flags::get)
    override val sampleShadingEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.SAMPLE_SHADING, Cache.flags::get)
    override val sampleMaskEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.SAMPLE_MASK, Cache.flags::get)
    override val scissorEnabled: GlStateStack<Boolean> = GlStateStack.Impl(
        "SCISSOR_TEST",
        Cache.flags[GlFlag.SCISSOR_TEST]::setValue,
        { glIsEnabled(GL_SCISSOR_TEST) }
    )
    override val stencilEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.STENCIL_TEST, Cache.flags::get)
    override val textureCubeMapSeamlessEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.TEXTURE_CUBE_MAP_SEAMLESS, Cache.flags::get)
    override val programPointSizeEnabled: GlStateStack<Boolean> = GlStateStack.DelegatedFlag(GlFlag.PROGRAM_POINT_SIZE, Cache.flags::get)

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

    internal object Cache : NeoGlStateManager.Cache {
        var cacheCounter: Int = 0

        val buffers = enumArrayMapOf<GlBufferTarget, CacheState<Int>> { target ->
            CacheState({
                GlStateManager._glBindBuffer(target.glId, it ?: 0)
            })
        }
        val program = CacheState<Int>({ GlStateManager._glUseProgram(it ?: 0) })
        val programPipeline = CacheState<Int>({ glBindProgramPipeline(it ?: 0) })
        val vertexArray = CacheState<Int>({ GlStateManager._glBindVertexArray(it ?: 0) })
        val textures = enumArrayMapOf<GlTextureTarget, CacheState<Int>> { target ->
            CacheState(
                if (target == GlTextureTarget.TEXTURE_2D) {
                    {
                        GlStateManager._bindTexture(it ?: 0)
                    }
                } else {
                    {
                        glBindTexture(target.glId, it ?: 0)
                    }
                }
            )
        }
        val renderbuffer = CacheState<Int>({
            //? if <1.21.5 {
            { GlStateManager._glBindRenderbuffer(GL_RENDERBUFFER, it ?: 0) }
            //? } else {
            /*{ glBindRenderbuffer(GL_RENDERBUFFER, it ?: 0) }
            *///? }
        })
        val framebuffer = CacheState<Int>({ GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, it ?: 0) })
        val readFramebuffer = CacheState<Int>({ GlStateManager._glBindFramebuffer(GL_READ_FRAMEBUFFER, it ?: 0) })

        val blendColor = CacheState<NeoColor>({ if (it != null) glBlendColor(it.redF, it.greenF, it.blueF, it.alphaF ?: 1f) })
        val blendEquation = CacheState<GlBlendEquation>({ if (it != null) glBlendEquation(it.glId) })
        val blendFunction = CacheState<BlendFunction>({ it?.bind() })
        val colorMask = CacheState<ColorMask>({ glColorMask(it?.red ?: true, it?.green ?: true, it?.blue ?: true, it?.alpha ?: true) })
        val cullFace = CacheState<GlCullFace>({ glCullFace((it ?: GlCullFace.BACK).glId) })
        val depthMask = CacheState<Boolean>({ glDepthMask(it ?: true) })
        val depthFunc = CacheState<GlAlphaFunction>({ glDepthFunc((it ?: GlAlphaFunction.LEQUAL).glId) })
        val polygonMode = CacheState<GlPolygonMode>({ glPolygonMode(GL_FRONT_AND_BACK, (it ?: GlPolygonMode.FILL).glId) })
        val polygonOffset = CacheState<PolygonOffset>({ glPolygonOffset(it?.factor ?: 0f, it?.units ?: 0f) })
        val scissor = CacheState<AbstractRect2<Int>>({ if (it != null) glScissor(it.min.x, it.min.y, it.size.x, it.size.y) })
        val stencilFunction = CacheState<StencilFunction>({ if (it != null) glStencilFunc(it.func.glId, it.ref, it.mask) })
        val stencilMask = CacheState<Int>({ glStencilMask(it ?: 0xFFFFFFFF.toInt()) })
        val stencilOp = CacheState<StencilOp>({ if (it != null) glStencilOp(it.stencilFail.glId, it.depthFail.glId, it.depthPass.glId) })
        val viewport = CacheState<AbstractRect2<Int>>({ if (it != null) glViewport(it.min.x, it.min.y, it.size.x, it.size.y) })

        val flags = enumArrayMapOf<GlFlag, CacheState<Boolean>> { flag ->
            when (flag) {
                GlFlag.BLEND -> CacheState({ if (it == true) GlStateManager._enableBlend() else GlStateManager._disableBlend() })
                GlFlag.COLOR_LOGIC_OP -> CacheState({ if (it == true) GlStateManager._enableColorLogicOp() else GlStateManager._disableColorLogicOp() })
                GlFlag.CULL_FACE -> CacheState({ if (it == true) GlStateManager._enableCull() else GlStateManager._disableCull() })
                GlFlag.DEPTH_TEST -> CacheState({ if (it == true) GlStateManager._enableDepthTest() else GlStateManager._disableDepthTest() })
                GlFlag.POLYGON_OFFSET -> CacheState({ if (it == true) GlStateManager._enablePolygonOffset() else GlStateManager._disablePolygonOffset() })
                GlFlag.SCISSOR_TEST -> CacheState({ if (it == true) GlStateManager._enableScissorTest() else GlStateManager._disableScissorTest() })
                else -> CacheState({ if (it == true) glEnable(flag.glId) else glDisable(flag.glId) })
            }
        }

        override fun flush() {
            buffers.forEach { (target, state) -> state.flush() }
            program.flush()
            programPipeline.flush()
            vertexArray.flush()
            textures.forEach { (target, state) -> state.flush() }
            renderbuffer.flush()
            framebuffer.flush()
            readFramebuffer.flush()
            blendColor.flush()
            blendEquation.flush()
            blendFunction.flush()
            colorMask.flush()
            cullFace.flush()
            depthMask.flush()
            depthFunc.flush()
            polygonMode.flush()
            polygonOffset.flush()
            scissor.flush()
            stencilFunction.flush()
            stencilMask.flush()
            stencilOp.flush()
            viewport.flush()
            flags.forEach { (flag, state) -> state.flush() }
        }

        override fun free() {
            cacheCounter--
        }
    }

    internal class CacheState<T>(
        val set: (value: T?) -> Unit,
        val cacheEnabled: () -> Boolean = { Cache.cacheCounter > 0 },
        var lastValue: T? = null,
        var nextValue: T? = null
    ) : (T?) -> Unit {
        override fun invoke(value: T?) {
            setValue(value)
        }

        fun setValue(value: T?) {
            if (cacheEnabled()) {
                nextValue = value
            } else {
                lastValue = value
                nextValue = value
                set(value)
            }
        }

        fun flush() {
            if (lastValue != nextValue) {
                set(nextValue)
                lastValue = nextValue
            }
        }
    }

    override fun createCache(): NeoGlStateManager.Cache {
        Cache.cacheCounter++
        return Cache
    }
}