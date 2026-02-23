package net.typho.big_shot_lib.impl.util

import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceKey
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderBytecodeCompiler
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderBytecodeDecompiler
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixin
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.ShaderMixinFactory
import java.nio.ByteOrder
import java.util.*

class ShaderMixinManagerImpl : ShaderMixinManager {
    override val byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN
    @JvmField
    val compiler = ShaderBytecodeCompiler()

    override fun create(key: ShaderProgramKey): ShaderMixinManager.Instance {
        data class Mixin<M : ShaderMixin>(
            @JvmField
            val factory: ShaderMixin.Factory<M>?,
            @JvmField
            val instance: M
        )

        return object : ShaderMixinManager.Instance {
            val mixins = LinkedList<Mixin<*>>()

            init {
                BigShotClientEntrypoint.registerShaderMixins(object : ShaderMixinFactory {
                    override fun register(mixin: ShaderMixin) {
                        mixins.add(Mixin(null, mixin))
                    }

                    override fun register(mixin: ShaderMixin.Factory<*>) {
                        getOrCreateMixinInstance(mixin)
                    }
                })
            }

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
                factory: ShaderMixin.Factory<M>
            ): M? {
                mixins.firstOrNull { it.factory === factory }?.let { return it.instance as M }

                val mixin = factory.create(key, this) ?: return null
                mixins.add(Mixin(factory, mixin))
                return mixin
            }
        }
    }
}