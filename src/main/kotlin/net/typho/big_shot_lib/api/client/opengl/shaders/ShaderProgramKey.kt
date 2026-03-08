package net.typho.big_shot_lib.api.client.opengl.shaders

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexFormat
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.*

@JvmRecord
data class ShaderProgramKey(
    @JvmField
    val loader: ShaderLoaderType,
    override val location: ResourceIdentifier,
    @JvmField
    val format: NeoVertexFormat,
    @JvmField
    val version: GLSLVersion,
    @JvmField
    val sources: Map<ShaderSourceType, ResourceIdentifier>,
    @JvmField
    val modules: List<ShaderModule>,
) : NamedResource {
    companion object {
        @JvmField
        val NULL = ShaderProgramKey(
            ShaderLoaderType.NULL,
            ResourceIdentifier("opengl", "null"),
            NeoVertexFormat.POSITION,
            GLSLVersion.DEFAULT,
            mapOf(),
            listOf()
        )

        @JvmStatic
        fun loadDependencies(modules: List<ShaderModule>): List<ShaderModule> {
            val list = LinkedList<ShaderModule>()

            for (module in modules) {
                for (dependency in module.type.dependencies) {
                    if (list.none { it.type === dependency.first }) {
                        dependency.second()?.let(list::add)
                    }
                }

                list.add(module)
            }

            return list
        }

        @JvmStatic
        fun codec(loader: ShaderLoaderType, location: ResourceIdentifier): MapCodec<ShaderProgramKey> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoVertexFormat.CODEC.fieldOf("format").forGetter { key -> key.format },
                GLSLVersion.CODEC.optionalFieldOf("version", GLSLVersion.DEFAULT).forGetter { key -> key.version },
                Codec.unboundedMap(NeoCodecs.enumCodec<ShaderSourceType>(), ResourceIdentifier.CODEC).fieldOf("sources").forGetter { key -> key.sources },
                ShaderModule.CODEC.codec().listOf().optionalFieldOf("modules", listOf()).forGetter { key -> key.modules }
            ).apply(it) { format, version, sources, modules -> ShaderProgramKey(loader, location, format, version, sources, loadDependencies(modules)) }
        }
    }

    override fun toString(): String {
        return location.toString()
    }
}
