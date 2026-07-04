package net.typho.big_shot_lib.api.client.rendering.common

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBlendFunction
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuDataType

interface GpuDrawSettings {
    val blend: GpuBlendFunction?
    val vertexShader: Identifier
    val fragmentShader: Identifier
    val lightmap: Boolean
    val overlay: Boolean
    val cull: Boolean
    val depth: GpuAlphaFunction?
    val writeDepth: Boolean
    val zOffset: Boolean
    val samplers: List<String>
    val uniforms: List<String>
    val storageBuffers: List<String>
    val texelBuffers: List<TexelBuffer>

    fun bind()

    fun unbind()

    data class TexelBuffer(
        @JvmField
        val name: String,
        @JvmField
        val type: GpuDataType,
        @JvmField
        val components: Int
    )

    open class Builder {
        @JvmField
        var blend: GpuBlendFunction? = null
        @JvmField
        var vertexShader: Identifier? = null
        @JvmField
        var fragmentShader: Identifier? = null
        @JvmField
        var lightmap: Boolean = false
        @JvmField
        var overlay: Boolean = false
        @JvmField
        var cull: Boolean = false
        @JvmField
        var depth: GpuAlphaFunction? = null
        @JvmField
        var writeDepth: Boolean = true
        @JvmField
        var zOffset: Boolean = false
        @JvmField
        var samplers = mutableListOf<String>()
        @JvmField
        var uniforms = mutableListOf<String>()
        @JvmField
        var storageBuffers = mutableListOf<String>()
        @JvmField
        var texelBuffers = mutableListOf<TexelBuffer>()

        constructor()

        constructor(state: GpuDrawSettings) {
            blend = state.blend
            vertexShader = state.vertexShader
            fragmentShader = state.fragmentShader
            lightmap = state.lightmap
            overlay = state.overlay
            cull = state.cull
            depth = state.depth
            writeDepth = state.writeDepth
            zOffset = state.zOffset
            samplers = state.samplers.toMutableList()
            uniforms = state.uniforms.toMutableList()
            storageBuffers = state.storageBuffers.toMutableList()
            texelBuffers = state.texelBuffers.toMutableList()
        }

        @JvmOverloads
        fun blend(blend: GpuBlendFunction? = GpuBlendFunction.TRANSLUCENT): Builder {
            this.blend = blend
            return this
        }

        @JvmOverloads
        fun shader(vertexShader: Identifier, fragmentShader: Identifier = vertexShader): Builder {
            this.vertexShader = vertexShader
            this.fragmentShader = fragmentShader
            return this
        }

        @JvmOverloads
        fun lightmap(lightmap: Boolean = true): Builder {
            this.lightmap = lightmap
            return this
        }

        @JvmOverloads
        fun overlay(overlay: Boolean = true): Builder {
            this.overlay = overlay
            return this
        }

        @JvmOverloads
        fun cull(cull: Boolean = true): Builder {
            this.cull = cull
            return this
        }

        @JvmOverloads
        fun depth(depth: GpuAlphaFunction? = GpuAlphaFunction.lequal): Builder {
            this.depth = depth
            return this
        }

        @JvmOverloads
        fun writeDepth(writeDepth: Boolean = true): Builder {
            this.writeDepth = writeDepth
            return this
        }

        @JvmOverloads
        fun zOffset(zOffset: Boolean = true): Builder {
            this.zOffset = zOffset
            return this
        }

        fun sampler(name: String): Builder {
            samplers.add(name)
            return this
        }

        fun uniform(name: String): Builder {
            uniforms.add(name)
            return this
        }

        fun storageBuffer(name: String): Builder {
            storageBuffers.add(name)
            return this
        }

        fun texelBuffer(name: String, type: GpuDataType, components: Int): Builder {
            texelBuffers.add(TexelBuffer(name, type, components))
            return this
        }
    }
}