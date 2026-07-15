package net.typho.big_shot_lib.plugin

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.file.TomlFileReader
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonWriter
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.serializer
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import org.objectweb.asm.AnnotationVisitor
import java.io.File

enum class ModLoader {
    NONE {
        override val mappedOnlyInAnnotationName = null
        override val manifestFile: File? = null

        override fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean) {
        }

        override fun getModIdAndVersion(manifest: String): Pair<String, String> {
            throw UnsupportedOperationException()
        }

        override fun appendModDependencies(
            manifest: String,
            minecraftVersion: MCVersion,
            loaderVersion: String,
            dependencies: Map<String, String>
        ): String {
            throw UnsupportedOperationException()
        }
    },
    FABRIC {
        override val mappedOnlyInAnnotationName = "net/fabricmc/api/Environment"
        override val manifestFile: File = File("fabric.mod.json")

        override fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean) {
            annotation("L$mappedOnlyInAnnotationName;", true)?.let { anno ->
                anno.visitEnum("value", "Lnet/fabricmc/api/EnvType;", if (client) "CLIENT" else "SERVER")
                anno.visitEnd()
            }
        }

        override fun getModIdAndVersion(manifest: String): Pair<String, String> {
            val json = JsonParser.parseString(manifest).asJsonObject
            return json.getAsJsonPrimitive("id").asString to json.getAsJsonPrimitive("version").asString
        }

        override fun appendModDependencies(
            manifest: String,
            minecraftVersion: MCVersion,
            loaderVersion: String,
            dependencies: Map<String, String>
        ): String {
            val json = JsonParser.parseString(manifest).asJsonObject
            val depends = json.get("depends")?.asJsonObject ?: JsonObject().also { json.add("depends", it) }

            depends.addProperty("minecraft", minecraftVersion.fabricVersionRange)
            depends.addProperty("fabricloader", ">=$loaderVersion")

            dependencies.forEach { (key, value) ->
                if (!depends.has(key)) {
                    depends.addProperty(key, ">=$value")
                }
            }

            return Gson().toJson(depends)
        }
    },
    FORGE {
        override val mappedOnlyInAnnotationName = null
        override val manifestFile: File = File("META-INF/mods.toml")

        override fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean) {
            TODO("Not yet implemented")
        }

        override fun getModIdAndVersion(manifest: String): Pair<String, String> {
            TODO("Not yet implemented")
        }

        override fun appendModDependencies(
            manifest: String,
            minecraftVersion: MCVersion,
            loaderVersion: String,
            dependencies: Map<String, String>
        ): String {
            TODO("Not yet implemented")
        }
    },
    NEOFORGE {
        override val mappedOnlyInAnnotationName = "net/neoforged/api/distmarker/OnlyIn"
        override val manifestFile: File = File("META-INF/neoforge.mods.toml")

        override fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean) {
            annotation("L$mappedOnlyInAnnotationName;", true)?.let { anno ->
                anno.visitEnum("value", "Lnet/neoforged/api/distmarker/Dist;", if (client) "CLIENT" else "DEDICATED_SERVER")
                anno.visitEnd()
            }
        }

        override fun getModIdAndVersion(manifest: String): Pair<String, String> {
            @Serializable
            data class Manifest(
                @JvmField
                val modId: String,
                @JvmField
                val version: String
            )

            val manifest = Toml.partiallyDecodeFromString<Manifest>(serializer(), manifest, "mods")
            return manifest.modId to manifest.version
        }

        override fun appendModDependencies(
            manifest: String,
            minecraftVersion: MCVersion,
            loaderVersion: String,
            dependencies: Map<String, String>
        ): String {
            val modId = getModIdAndVersion(manifest).first

            @Serializable
            data class Dependency(
                @JvmField
                val modId: String,
                @JvmField
                val versionRange: String
            )

            var manifest = manifest

            fun dependency(id: String, range: String) {
                manifest += "\n[[dependencies.$modId]]\n"
                manifest += Toml.encodeToString(serializer(), Dependency(id, range))
            }

            dependency("minecraft", minecraftVersion.forgeVersionRange)
            dependency("neoforge", "[$loaderVersion,)")

            for ((mod, version) in dependencies) {
                dependency(mod, "[$version,)")
            }

            return manifest
        }
    };

    abstract val mappedOnlyInAnnotationName: String?
    abstract val manifestFile: File?

    open fun unmapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, descriptor: String, api: Int): AnnotationVisitor? {
        return if (mappedOnlyInAnnotationName != null && descriptor == "L$mappedOnlyInAnnotationName;") {
            object : AnnotationVisitor(api) {
                var client = false

                override fun visitEnum(name: String, descriptor: String, value: String) {
                    if (name == "value" && value == "CLIENT") {
                        client = true
                    }
                }

                override fun visitEnd() {
                    annotation(Annotations.ONLY_IN, true)?.let { anno ->
                        anno.visitEnum("value", "Lnet/typho/big_shot_lib/api/plugin/Environment;", if (client) "CLIENT" else "SERVER")
                        anno.visitEnd()
                    }
                }
            }
        } else {
            null
        }
    }

    abstract fun mapOnlyInAnnotation(annotation: (desc: String, visible: Boolean) -> AnnotationVisitor?, client: Boolean)

    abstract fun getModIdAndVersion(manifest: String): Pair<String, String>

    abstract fun appendModDependencies(manifest: String, minecraftVersion: MCVersion, loaderVersion: String, dependencies: Map<String, String>): String

    companion object {
        @JvmStatic
        operator fun get(key: String) = enumValueOf<ModLoader>(key.uppercase())
    }
}