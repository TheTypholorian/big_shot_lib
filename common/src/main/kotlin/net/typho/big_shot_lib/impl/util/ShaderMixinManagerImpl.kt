package net.typho.big_shot_lib.impl.util

import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderBytecodeCompiler
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderBytecodeDecompiler
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixinManager
import java.nio.ByteOrder
import java.util.*

class ShaderMixinManagerImpl : ShaderMixinManager {
    override val byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN
    @JvmField
    val compiler = ShaderBytecodeCompiler()

    override fun create(key: ShaderProgramKey): ShaderMixinManager.Instance {
        data class Mixin<M : ShaderMixin>(
            @JvmField
            val factory: ShaderMixin.Factory<M>,
            @JvmField
            val instance: M
        )

        return object : ShaderMixinManager.Instance {
            val mixins = LinkedList<Mixin<*>>()

            override fun apply(
                type: ShaderSourceType,
                code: String
            ): String {
                val sourceKey = ShaderSourceKey(key, type)
                val modified = mixins.fold(code) { code, mixin -> mixin.instance.mixinPreCompile(sourceKey, code) }
                val bytecode = mixins.fold(compiler.compile(sourceKey, modified)) { buffer, mixin -> mixin.instance.mixinBytecode(sourceKey, buffer) }
                return ShaderBytecodeDecompiler().use { decompiler ->
                    mixins.fold(decompiler.decompile(sourceKey, bytecode)) { code, mixin -> mixin.instance.mixinPostCompile(sourceKey, code) }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun <M : ShaderMixin> getOrCreateMixinInstance(
                mixin: ShaderMixin.Factory<M>
            ): M? {
                return mixins.first { it.factory === mixin }.instance as? M
            }
        }
    }
}