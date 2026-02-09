package net.typho.big_shot_lib

import net.typho.big_shot_lib.api.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.impl.shaders.mixins.BreezeWindShaderMixin
import org.lwjgl.system.MemoryUtil
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object BigShotLib {
    fun init() {
        ShaderMixinManager.register(BreezeWindShaderMixin)
        ShaderMixinManager.register(object : ShaderMixin {
            override fun mixinBytecode(
                key: ShaderSourceKey,
                code: ShaderBytecodeBuffer
            ): ShaderBytecodeBuffer {
                println("Shader dump $key")

                try {
                    val arr = ByteArray(code.buffer.capacity() * 4)
                    MemoryUtil.memByteBuffer(code.buffer).get(0, arr, 0, arr.size)
                    val path =
                        Paths.get("shader_dump", key.fileName().replace(":".toRegex(), "_") + ".bin")

                    if (!Files.exists(path)) {
                        Files.createFile(path)
                    }

                    Files.write(path, arr)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

                return code
            }
        })
    }
}