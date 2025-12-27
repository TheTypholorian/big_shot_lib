package net.typho.big_shot_lib

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.AABB
import net.typho.big_shot_lib.spirv.ShaderMixinContext
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import org.lwjgl.util.shaderc.Shaderc.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths

object BigShotLib {
    const val MOD_ID = "big_shot_lib"
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")
    val SCREEN_VBO: VertexBuffer by lazy {
        val vbo = VertexBuffer(VertexBuffer.Usage.STATIC)
        val blitBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)

        blitBuilder.quad(
            Vector3f(-1f, 1f, 0f),
            Vector3f(1f, 1f, 0f),
            Vector3f(1f, -1f, 0f),
            Vector3f(-1f, -1f, 0f),
            Vector3f(0f, 0f, -1f)
        )

        vbo.bind()
        vbo.upload(blitBuilder.buildOrThrow())
        VertexBuffer.unbind()
        return@lazy vbo
    }

    fun init() {
        val compiler = shaderc_compiler_initialize()
        require(compiler != MemoryUtil.NULL)

        val options = shaderc_compile_options_initialize()
        require(options != MemoryUtil.NULL)

        shaderc_compile_options_set_target_env(
            options,
            shaderc_target_env_opengl,
            shaderc_env_version_opengl_4_5
        )

        shaderc_compile_options_set_source_language(
            options,
            shaderc_source_language_glsl
        )

        val code = """
            #version 450 core
            
            layout(location = 0) uniform mat4 Matrix;
            
            layout(location = 0) in vec3 Position;
            layout(location = 1) in vec3 Normal;
            layout(location = 2) in vec2 UV;
            
            layout(location = 0) out vec3 pos;
            layout(location = 1) out vec2 uv;
            
            void main() {
                pos = Position;
                uv = UV;
            }
        """.trimIndent()
        val result = shaderc_compile_into_spv(
            compiler,
            code,
            shaderc_glsl_vertex_shader,
            "test_vertex.vsh",
            "main",
            options
        )

        val status = shaderc_result_get_compilation_status(result)
        if (status != shaderc_compilation_status_success) {
            error("Shader compile failed:\n${shaderc_result_get_error_message(result)?.trim()}")
        }

        val size = shaderc_result_get_length(result).toInt()
        val pointer = shaderc_result_get_bytes(result)!!
        val context = ShaderMixinContext()
        context.code.add(pointer.order(ShaderMixinContext.BYTE_ORDER))
        context.loadBound()

        val floatType = context.addFloatType(32)
        val vec3Type = context.addVectorType(floatType, 3)

        val outputId = context.addStaticVar(3, vec3Type, "BigShotNormalOutput") // Output
        val inputId = context.addStaticVar(1, vec3Type, "BigShotNormalInput") // Input
        val tempVar = context.bound++
        context.inject(
            ShaderMixinContext.AtVoidReturn("main"),
            ByteBuffer.allocate(7 * ShaderMixinContext.WORD_SIZE_BYTES)
                .order(ShaderMixinContext.BYTE_ORDER)

                .putInt(0x00_04_00_3D)
                .putInt(vec3Type)
                .putInt(tempVar)
                .putInt(inputId)

                .putInt(0x00_03_00_3E)
                .putInt(outputId)
                .putInt(tempVar)
        )
        context.putBound()

        val compiled = context.compile()
        val array = ByteArray(compiled.capacity())
        compiled.get(array)
        Files.write(Paths.get("injected.bin"), array)

        shaderc_result_release(result)
        shaderc_compile_options_release(options)
        shaderc_compiler_release(compiler)
    }

    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

    fun getViewMatrix(
        camera: Camera = Minecraft.getInstance().gameRenderer.mainCamera,
        matrix: Matrix4f = RenderSystem.getModelViewMatrix()
    ): Matrix4f {
        return matrix.translate(camera.position.toVector3f().mul(-1f))
    }

    fun VertexConsumer.cube(
        box: AABB,
    ) {
        val vertices = arrayOf(
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.minX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),
            Vector3f(box.maxX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()),
        )

        quad(vertices[0], vertices[1], vertices[2], vertices[3], Vector3f(0f, 0f, 1f))
        quad(vertices[1], vertices[5], vertices[6], vertices[2], Vector3f(-1f, 0f, 0f))
        quad(vertices[5], vertices[4], vertices[7], vertices[6], Vector3f(0f, 0f, -1f))
        quad(vertices[4], vertices[0], vertices[3], vertices[7], Vector3f(1f, 0f, 0f))
        quad(vertices[1], vertices[0], vertices[4], vertices[5], Vector3f(0f, 1f, 0f))
        quad(vertices[3], vertices[2], vertices[6], vertices[7], Vector3f(0f, -1f, 0f))
    }

    fun VertexConsumer.quad(
        v0: Vector3f,
        v1: Vector3f,
        v2: Vector3f,
        v3: Vector3f,
        normal: Vector3f
    ) {
        addVertex(v0).setUv(1f, 0f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v1).setUv(0f, 0f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v2).setUv(0f, 1f).setNormal(normal.x, normal.y, normal.z)
        addVertex(v3).setUv(1f, 1f).setNormal(normal.x, normal.y, normal.z)
    }
}