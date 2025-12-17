package net.typho.big_shot_lib

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.AABB
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryUtil
import org.lwjgl.util.shaderc.Shaderc.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.file.Files
import java.nio.file.Paths

object BigShotLib {
    const val MOD_ID = "big_shot_lib"
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")
    val SCREEN_VBO: VertexBuffer by lazy {
        val vbo = VertexBuffer(VertexBuffer.Usage.STATIC)
        val blitBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)

        blitBuilder.quad(
            Vector3f(-1f, -1f, 0f),
            Vector3f(1f, -1f, 0f),
            Vector3f(1f, 1f, 0f),
            Vector3f(-1f, 1f, 0f),
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
            layout(location = 2) in vec3 Albedo;
            layout(location = 3) in vec2 UV;
            
            layout(location = 0) out vec3 pos;
            layout(location = 1) out vec3 albedo;
            
            void main() {
                pos = Position;
                albedo = Albedo;
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

        println(pointer.capacity())

        val bytes = ByteArray(size)
        pointer.get(bytes)
        Files.write(Paths.get("test.bin"), bytes)

        val modified = injectOutputs(pointer, 1, 3)
        val mBytes = ByteArray(modified.limit())
        modified.get(0, mBytes)
        Files.write(Paths.get("modified.bin"), mBytes)

        shaderc_result_release(result)
        shaderc_compile_options_release(options)
        shaderc_compiler_release(compiler)
    }

    fun injectOutputs(originalSpirv: ByteBuffer, inVec3Id: Int, inVec2Id: Int): ByteBuffer {
        originalSpirv.order(ByteOrder.LITTLE_ENDIAN)
        val words = originalSpirv.asIntBuffer()
        val newWords = IntArray(words.capacity() + 20) // rough extra space
        words.get(newWords, 0, words.capacity())
        var nextId = newWords.maxOrNull()!! + 1
        var insertIndex = 5 // after header (first 5 words)

        fun makeWord(opcode: Int, wordCount: Int) = (wordCount shl 16) or opcode

        // IDs for new types and variables
        val floatId = nextId++
        val vec3Id = nextId++
        val vec2Id = nextId++
        val ptrVec3Id = nextId++
        val ptrVec2Id = nextId++
        val outVec3Id = nextId++
        val outVec2Id = nextId++

        // Insert type declarations
        val typeWords = intArrayOf(
            makeWord(0x15, 3), floatId, 32,                 // OpTypeFloat %float 32
            makeWord(0x16, 4), vec3Id, floatId, 3,         // OpTypeVector %vec3 %float 3
            makeWord(0x16, 4), vec2Id, floatId, 2,         // OpTypeVector %vec2 %float 2
            makeWord(0x1e, 4), ptrVec3Id, 3, vec3Id,       // OpTypePointer Output %vec3
            makeWord(0x1e, 4), ptrVec2Id, 3, vec2Id        // OpTypePointer Output %vec2
        )
        newWords.copyInto(typeWords, 0, 0, typeWords.size)
        // shift the existing words after insertIndex
        System.arraycopy(newWords, insertIndex, newWords, insertIndex + typeWords.size, words.capacity() - insertIndex)
        System.arraycopy(typeWords, 0, newWords, insertIndex, typeWords.size)
        insertIndex += typeWords.size

        // Insert output variables
        val varWords = intArrayOf(
            makeWord(0x21, 3), ptrVec3Id, outVec3Id, 3,   // OpVariable %ptr_vec3 Output
            makeWord(0x21, 3), ptrVec2Id, outVec2Id, 3    // OpVariable %ptr_vec2 Output
        )
        System.arraycopy(newWords, insertIndex, newWords, insertIndex + varWords.size, newWords.size - insertIndex - varWords.size)
        System.arraycopy(varWords, 0, newWords, insertIndex, varWords.size)

        // Decorations
        val decorateWords = intArrayOf(
            makeWord(0x11, 4), outVec3Id, 30, 0, // OpDecorate %outVec3 Location 0
            makeWord(0x11, 4), outVec2Id, 30, 1  // OpDecorate %outVec2 Location 1
        )
        System.arraycopy(newWords, insertIndex + varWords.size, newWords, insertIndex + varWords.size + decorateWords.size, newWords.size - insertIndex - varWords.size - decorateWords.size)
        System.arraycopy(decorateWords, 0, newWords, insertIndex + varWords.size, decorateWords.size)

        // Find main function end to inject OpStore
        var i = 0
        while (i < newWords.size) {
            val word = newWords[i]
            val wordCount = word shr 16
            val opcode = word and 0xFFFF
            if (opcode == 0x11) { // OpFunctionEnd
                val storeWords = intArrayOf(
                    makeWord(0x3d, 3), outVec3Id, inVec3Id,   // OpStore %outVec3 %inVec3
                    makeWord(0x3d, 3), outVec2Id, inVec2Id    // OpStore %outVec2 %inVec2
                )
                System.arraycopy(newWords, i, newWords, i + storeWords.size, newWords.size - i - storeWords.size)
                System.arraycopy(storeWords, 0, newWords, i, storeWords.size)
                break
            }
            i += wordCount
        }

        val finalBuffer = ByteBuffer.allocateDirect(newWords.size * 4).order(ByteOrder.LITTLE_ENDIAN)
        finalBuffer.asIntBuffer().put(newWords)
        finalBuffer.flip()
        return finalBuffer
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