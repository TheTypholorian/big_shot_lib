package net.typho.big_shot_lib.plugin

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import net.typho.big_shot_lib.plugin.transform.util.FieldDesc
import net.typho.big_shot_lib.plugin.transform.util.MethodDesc
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import java.io.File
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.Properties
import java.util.function.Function
import javax.inject.Inject

abstract class BigShotLibPluginExtension @Inject constructor(
    objects: ObjectFactory,
    private val project: Project
) {
    abstract val mcVersionProperty: Property<String>
    abstract val loader: Property<ModLoader>
    abstract val autoUpdateDependencies: Property<Boolean>
    val transformInfo: TransformInfo = objects.newInstance(TransformInfo::class.java, mcVersionProperty)
    val dependencyVersionsFile: File
        get() = project.file("dependency_versions.properties")

    val mcVersion: MCVersion
        get() = MCVersion[mcVersionProperty.get()]

    fun transformInfo(action: Action<in TransformInfo>) {
        action.execute(transformInfo)
    }

    fun version(value: String) {
        mcVersionProperty.set(value)
    }

    fun version(value: MCVersion) {
        mcVersionProperty.set(value.primaryVersion)
    }

    fun loader(value: String) {
        loader.set(ModLoader[value])
    }

    fun loader(value: ModLoader) {
        loader.set(value)
    }

    @JvmOverloads
    fun autoUpdateDependencies(value: Boolean = true) {
        autoUpdateDependencies.set(value)
    }

    fun cacheDependencyVersion(dependency: String, versionIdToName: Function<String, String>, latest: String?, update: Boolean = false): String? {
        var versionId = latest
        val properties = Properties()
        val propertiesFile = dependencyVersionsFile

        if (propertiesFile.exists()) {
            propertiesFile.inputStream().use(properties::load)
        }

        var cachedVersion = properties.getProperty(dependency)

        if (cachedVersion == "null") {
            cachedVersion = null
        }

        if (versionId != cachedVersion) {
            if (cachedVersion == null || update) {
                if (update) {
                    println("[Big Shot Lib] Updating dependency $dependency from ${versionIdToName.apply(cachedVersion)} to ${versionId?.let(versionIdToName::apply) ?: "Unknown"}.")
                }

                properties.setProperty(dependency, versionId)
                propertiesFile.outputStream().use { properties.store(it, "[Big Shot Lib] Cached dependency versions. Run the updateDependencyVersions task to update them all to latest.") }
            } else {
                println("[Big Shot Lib] Update available for dependency $dependency (${versionIdToName.apply(cachedVersion)} to ${versionId?.let(versionIdToName::apply) ?: "Unknown"}). Run the updateDependencyVersions task to update.")

                versionId = cachedVersion
            }
        }

        return versionId
    }

    @JvmOverloads
    fun getFabricLoaderVersion(update: Boolean = false): String {
        val body = ManifestCache.fabricLoaderVersions

        try {
            val json = JsonParser.parseString(body).asJsonArray[0].asJsonObject
            var versionId = json.get("version").asString

            versionId = cacheDependencyVersion("fabric-loader", { it }, versionId, update)
            println("[Big Shot Lib] Using $versionId for fabric loader")
            return versionId
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException("Error parsing json $body", e)
        }
    }

    fun getMcVersionFromNeoForge(version: String): String {
        val tokens = version.split('.')

        if (tokens[0].toInt() >= 26) {
            var mcVersion = "${tokens[0]}.${tokens[1]}"

            if (tokens[2] != "0") {
                mcVersion += ".${tokens[2]}"
            }

            val snapshot = version.split('+')

            if (snapshot.size == 2) {
                mcVersion += "-${snapshot[1]}"
            }

            return mcVersion
        }

        return "1.${tokens[0]}.${tokens[1]}"
    }

    fun getMcVersionFromForge(version: String): String {
        return version.substringBefore('-')
    }

    @JvmOverloads
    fun getNeoForgeLoaderVersion(update: Boolean = false): String? {
        val mc = MCVersion[mcVersionProperty.get()]
        val body = ManifestCache.neoForgeLoaderVersions

        try {
            val versions = JsonParser.parseString(body).asJsonObject.getAsJsonArray("versions").reversed()
            for (version in versions) {
                var versionId = version.asString

                if (getMcVersionFromNeoForge(versionId) == mc.primaryVersion) {
                    versionId = cacheDependencyVersion("neoforge-loader", { it }, versionId, update)
                    println("[Big Shot Lib] Using $versionId for neoforge loader")
                    return versionId
                }
            }
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException("Error parsing json $body", e)
        }

        return null
    }

    @JvmOverloads
    fun getForgeLoaderVersion(update: Boolean = false): String? {
        val mc = MCVersion[mcVersionProperty.get()]
        val body = ManifestCache.forgeLoaderVersions

        try {
            val versions = JsonParser.parseString(body).asJsonObject.getAsJsonArray("versions").reversed()

            for (version in versions) {
                var versionId = version.asString

                if (getMcVersionFromForge(versionId) == mc.primaryVersion) {
                    versionId = cacheDependencyVersion("forge-loader", { it }, versionId, update)
                    println("[Big Shot Lib] Using $versionId for forge loader")
                    return versionId
                }
            }
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException("Error parsing json $body", e)
        }

        return null
    }

    @JvmOverloads
    fun getLoaderVersion(update: Boolean = false): String? {
        return when (val loader = loader.get()) {
            ModLoader.FABRIC -> getFabricLoaderVersion(update)
            ModLoader.NEOFORGE -> getNeoForgeLoaderVersion(update)
            ModLoader.FORGE -> getForgeLoaderVersion(update)
            else -> throw IllegalArgumentException("No loader version for $loader")
        }
    }

    fun getParchmentVersion(): Pair<String, String>? {
        val mc = MCVersion[mcVersionProperty.get()]

        return mc.parchmentVersion
    }

    @JvmOverloads
    fun getModrinthProjectVersion(projectId: String, update: Boolean = false): String? {
        val mc = MCVersion[mcVersionProperty.get()]
        val loader = loader.get()
        val body = ManifestCache.getModrinthVersions(projectId)

        try {
            val json = JsonParser.parseString(body).asJsonArray

            val versionIdToName = mutableMapOf<String, String?>()

            var versionId: String? = null
            var mostRecentTime: Instant? = null

            for (versionElement in json) {
                val version = versionElement.asJsonObject
                val time = Instant.parse(version.get("date_published").asString)
                val id = version.get("id").asString

                if (version.getAsJsonArray("game_versions").none { a -> mc.versions.any { b -> a.asString.equals(b, ignoreCase = true) } }) {
                    continue
                }

                if (version.getAsJsonArray("loaders").none { loader.name.equals(it.asString, ignoreCase = true) }) {
                    continue
                }

                versionIdToName[id] = version.get("name")?.asString

                if (mostRecentTime == null || time.isAfter(mostRecentTime)) {
                    versionId = id
                    mostRecentTime = time
                }
            }

            versionId = cacheDependencyVersion(projectId, { versionIdToName[it] ?: it }, versionId, update)
            println("[Big Shot Lib] Using ${versionIdToName.getOrDefault(versionId, versionId)} for modrinth project $projectId on ${mc.primaryVersion} $loader")
            return versionId
        } catch (e: JsonSyntaxException) {
            throw JsonSyntaxException("Error parsing json $body", e)
        }
    }

    fun getDependencyVersion(dependency: String, update: Boolean = false): String? {
        return when (dependency) {
            "fabric-loader" -> getFabricLoaderVersion(update)
            "neoforge-loader" -> getNeoForgeLoaderVersion(update)
            "forge-loader" -> getForgeLoaderVersion(update)
            else -> getModrinthProjectVersion(dependency, update)
        }
    }

    fun modrinthDep(projectId: String, update: Boolean = false): String? {
        return getModrinthProjectVersion(projectId, update)?.let { "maven.modrinth:$projectId:$it" }
    }

    abstract class TransformInfo @Inject constructor(
        private val objects: ObjectFactory,
        private val version: Property<String>
    ) {
        interface ClassRename {
            val from: Property<String>
            val to: Property<String>
        }

        interface MethodRename {
            val from: Property<MethodDesc>
            val to: Property<String>
        }

        interface FieldRename {
            val from: Property<FieldDesc>
            val to: Property<String>
        }

        interface InterfaceInjection {
            val iface: Property<String>
            val target: Property<String>
            val typeParams: ListProperty<String>
            val methods: ListProperty<Pair<String, String>>
        }

        interface StaticMethodInjection {
            val redirectTo: Property<MethodDesc>
            val targetClass: Property<String>
            val targetMethodName: Property<String>
            val signature: Property<String>
            val exceptions: ListProperty<String>
        }

        interface ArgumentOverloadConverter {
            val from: Property<String>
            val to: Property<String>
            val converter: Property<MethodDesc>
            val permutate: Property<Boolean>
        }

        abstract val classRenames: ListProperty<ClassRename>
        abstract val methodRenames: ListProperty<MethodRename>
        abstract val fieldRenames: ListProperty<FieldRename>
        abstract val markAsDeprecated: ListProperty<MethodDesc>
        abstract val interfaceInjections: ListProperty<InterfaceInjection>
        abstract val staticMethodInjections: ListProperty<StaticMethodInjection>
        abstract val argumentOverloadConverters: ListProperty<ArgumentOverloadConverter>
        abstract val clientOnlyPackages: ListProperty<String>
        abstract val serverOnlyPackages: ListProperty<String>

        init {
            classRenames.convention(listOf())
            methodRenames.convention(listOf())
            fieldRenames.convention(listOf())
            interfaceInjections.convention(listOf())
            markAsDeprecated.convention(listOf())
            staticMethodInjections.convention(listOf())
            argumentOverloadConverters.convention(listOf())
            clientOnlyPackages.convention(listOf())
            serverOnlyPackages.convention(listOf())
        }

        fun setupDefaults() {
            val version = version.get()

            if (version < "1.21.1") {
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "fromNamespaceAndPath", "fromNamespaceAndPath", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "createUntrusted", "createUntrusted", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "parse", "parse", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "withDefaultNamespace", "withDefaultNamespace", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "bySeparator", "bySeparator", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "tryBySeparator", "tryBySeparator", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
            }

            if (version < "1.21.11") {
                renameClass("net/minecraft/resources/ResourceLocation", "net/minecraft/resources/Identifier")
                renameClass("net/minecraft/util/ResourceLocationPattern", "net/minecraft/util/IdentifierPattern")
                renameClass("net/minecraft/ResourceLocationException", "net/minecraft/IdentifierException")
                renameClass("net/minecraft/client/resources/model/ModelResourceLocation", "net/minecraft/client/resources/model/ModelIdentifier")
                renameClass("net/minecraft/commands/arguments/ResourceLocationArgument", "net/minecraft/commands/arguments/IdentifierArgument")
                renameClass("net/minecraft/util/parsing/packrat/commands/ResourceLocationParseRule", "net/minecraft/util/parsing/packrat/commands/IdentifierParseRule")
                renameClass("net/minecraft/client/searchtree/ResourceLocationSearchTree", "net/minecraft/client/searchtree/IdentifierSearchTree")

                renameMethod("net/minecraft/resources/ResourceKey", "()Lnet/minecraft/resources/Identifier;", "location", "identifier")
                renameMethod("net/minecraft/tags/TagKey", "()Lnet/minecraft/resources/Identifier;", "location", "identifier")

                renameField("net/minecraft/resources/ResourceKey", "Lnet/minecraft/resources/Identifier;", "location", "identifier")
                renameField("net/minecraft/tags/TagKey", "Lnet/minecraft/resources/Identifier;", "location", "identifier")
            } else {
                renameClass("net/minecraft/client/renderer/rendertype/LayeringTransform", "net/minecraft/client/renderer/LayeringTransform")
                renameClass("net/minecraft/client/renderer/rendertype/OutputTarget", "net/minecraft/client/renderer/OutputTarget")
                renameClass("net/minecraft/client/renderer/rendertype/RenderSetup", "net/minecraft/client/renderer/RenderSetup")
                renameClass("net/minecraft/client/renderer/rendertype/RenderType", "net/minecraft/client/renderer/RenderType")
                renameClass("net/minecraft/client/renderer/rendertype/RenderTypes", "net/minecraft/client/renderer/RenderTypes")
                renameClass("net/minecraft/client/renderer/rendertype/TextureTransform", "net/minecraft/client/renderer/TextureTransform")
            }

            if (version >= "26.1") {
                renameClass("net/minecraft/client/resources/model/geometry/BakedQuad", "net/minecraft/client/renderer/block/model/BakedQuad")
            }

            if (version >= "26.2") {
                renameClass("com/mojang/blaze3d/vulkan/VulkanBackend", "com/mojang/blaze3d/vulkan/VkBackend")
                renameClass("com/mojang/blaze3d/vulkan/VulkanBindGroupLayout", "com/mojang/blaze3d/vulkan/VkBindGroupLayout")
                renameClass("com/mojang/blaze3d/vulkan/VulkanCommandEncoder", "com/mojang/blaze3d/vulkan/VkCommandEncoder")
                renameClass("com/mojang/blaze3d/vulkan/VulkanCommandPool", "com/mojang/blaze3d/vulkan/VkCommandPool")
                renameClass("com/mojang/blaze3d/vulkan/VulkanConst", "com/mojang/blaze3d/vulkan/VkConst")
                renameClass("com/mojang/blaze3d/vulkan/VulkanDebug", "com/mojang/blaze3d/vulkan/VkDebug")
                renameClass("com/mojang/blaze3d/vulkan/VulkanDevice", "com/mojang/blaze3d/vulkan/VkDevice")
                renameClass("com/mojang/blaze3d/vulkan/VulkanGpuBuffer", "com/mojang/blaze3d/vulkan/VkBuffer")
                renameClass("com/mojang/blaze3d/vulkan/VulkanGpuSampler", "com/mojang/blaze3d/vulkan/VkSampler")
                renameClass("com/mojang/blaze3d/vulkan/VulkanGpuSurface", "com/mojang/blaze3d/vulkan/VkSurface")
                renameClass("com/mojang/blaze3d/vulkan/VulkanGpuTexture", "com/mojang/blaze3d/vulkan/VkTexture")
                renameClass("com/mojang/blaze3d/vulkan/VulkanGpuTextureView", "com/mojang/blaze3d/vulkan/VkTextureView")
                renameClass("com/mojang/blaze3d/vulkan/VulkanPhysicalDevice", "com/mojang/blaze3d/vulkan/VkPhysicalDevice")
                renameClass("com/mojang/blaze3d/vulkan/VulkanQueryPool", "com/mojang/blaze3d/vulkan/VkQueryPool")
                renameClass("com/mojang/blaze3d/vulkan/VulkanQueue", "com/mojang/blaze3d/vulkan/VkQueue")
                renameClass("com/mojang/blaze3d/vulkan/VulkanRenderPass", "com/mojang/blaze3d/vulkan/VkRenderPass")
                renameClass("com/mojang/blaze3d/vulkan/VulkanRenderPipeline", "com/mojang/blaze3d/vulkan/VkRenderPipeline")
                renameClass("com/mojang/blaze3d/vulkan/VulkanTransientMemory", "com/mojang/blaze3d/vulkan/VkTransientMemory")
                renameClass("com/mojang/blaze3d/vulkan/VulkanUtils", "com/mojang/blaze3d/vulkan/VkUtil")
            }

            if (version >= "1.21.5") {
                renameClass("com/mojang/blaze3d/buffers/GpuBuffer", "com/mojang/blaze3d/buffers/GpuBufferImpl")
                renameClass("com/mojang/blaze3d/textures/GpuSampler", "com/mojang/blaze3d/textures/GpuSamplerImpl")
                renameClass("com/mojang/blaze3d/textures/GpuTexture", "com/mojang/blaze3d/textures/GpuTextureImpl")

                injectInterface("net/typho/big_shot_lib/api/client/rendering/common/GpuBuffer", "com/mojang/blaze3d/buffers/GpuBufferImpl")
                injectInterface("net/typho/big_shot_lib/api/client/rendering/common/GpuTexture", "com/mojang/blaze3d/textures/GpuTextureImpl")

                injectInterface("net/typho/big_shot_lib/api/client/ext/RenderPassExtension", "com/mojang/blaze3d/systems/RenderPassBackend")
                injectInterface("net/typho/big_shot_lib/api/client/ext/RenderPassExtension", "com/mojang/blaze3d/systems/RenderPass")
            }

            markAsDeprecated("com/mojang/blaze3d/vertex/VertexConsumer", "(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "addVertex")
            markAsDeprecated($$"com/mojang/blaze3d/vertex/VertexFormat$Builder", "()Lcom/mojang/blaze3d/vertex/VertexFormat;", "build")

            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "createUntrusted", "untrusted")
            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "fromNamespaceAndPath", "of")
            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "withDefaultNamespace", "minecraft")

            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV0", "TEXTURE_UV")
            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV1", "OVERLAY_UV")
            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV2", "LIGHT_UV")

            injectInterface("net/typho/big_shot_lib/api/client/ext/VertexConsumerExtension", "com/mojang/blaze3d/vertex/VertexConsumer")
            injectInterface("net/typho/big_shot_lib/api/client/ext/VertexFormatBuilderExtension", $$"com/mojang/blaze3d/vertex/VertexFormat$Builder")
            injectInterface("net/typho/big_shot_lib/api/client/ext/RenderTypeExtension", "net/minecraft/client/renderer/RenderType")

            injectInterface("net/typho/big_shot_lib/api/client/rendering/common/constant/GpuAlphaFunction", "com/mojang/blaze3d/platform/CompareOp")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/common/constant/GpuBlendFactor", "com/mojang/blaze3d/platform/BlendFactor")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/common/constant/GpuDataType", $$"com/mojang/blaze3d/GpuFormat$ComponentType")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/common/constant/GpuIndexType", "com/mojang/blaze3d/IndexType")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/common/constant/GpuShaderType", "com/mojang/blaze3d/shaders/ShaderType")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/common/constant/GpuTextureFormat", "com/mojang/blaze3d/GpuFormat")

            injectInterface("net/typho/big_shot_lib/api/ext/DirectionExtension", "net/minecraft/core/Direction")
            injectInterface("net/typho/big_shot_lib/api/ext/Vec3iExtension", "net/minecraft/core/Vec3i")
            injectInterface("net/typho/big_shot_lib/api/ext/BlockPosExtension", "net/minecraft/core/BlockPos")
            injectInterface("net/typho/big_shot_lib/api/ext/MutableBlockPosExtension", $$"net/minecraft/core/BlockPos$MutableBlockPos")
            injectInterface("net/typho/big_shot_lib/api/ext/Vec3Extension", "net/minecraft/world/phys/Vec3")
            injectInterface("net/typho/big_shot_lib/api/ext/IdentifierExtension", "net/minecraft/resources/Identifier")
        }

        fun renameClass(from: String, to: String) {
            classRenames.add(objects.newInstance(ClassRename::class.java).also {
                it.from.set(from)
                it.to.set(to)
            })
        }

        fun renameMethod(from: MethodDesc, to: String) {
            methodRenames.add(objects.newInstance(MethodRename::class.java).also {
                it.from.set(from)
                it.to.set(to)
            })
        }

        fun renameMethod(cls: String, desc: String, from: String, to: String) {
            renameMethod(objects.newInstance(MethodDesc::class.java).also {
                it.cls.set(cls)
                it.name.set(from)
                it.desc.set(desc)
            }, to)
        }

        fun renameField(from: FieldDesc, to: String) {
            fieldRenames.add(objects.newInstance(FieldRename::class.java).also {
                it.from.set(from)
                it.to.set(to)
            })
        }

        fun renameField(cls: String, desc: String, from: String, to: String) {
            renameField(objects.newInstance(FieldDesc::class.java).also {
                it.cls.set(cls)
                it.name.set(from)
                it.desc.set(desc)
            }, to)
        }

        fun markAsDeprecated(cls: String, desc: String, name: String) {
            markAsDeprecated.add(objects.newInstance(MethodDesc::class.java).also {
                it.cls.set(cls)
                it.name.set(name)
                it.desc.set(desc)
            })
        }

        @JvmOverloads
        fun injectInterface(iface: String, target: String, typeParams: Array<String> = arrayOf(), vararg methods: Pair<String, String>) {
            interfaceInjections.add(objects.newInstance(InterfaceInjection::class.java).also {
                it.iface.set(iface)
                it.target.set(target)
                it.typeParams.set(typeParams.toList())
                it.methods.set(methods.toList())
            })
        }

        @JvmOverloads
        fun injectStaticMethod(fromCls: String, toCls: String, fromName: String, toName: String, methodDesc: String, signature: String? = null, exceptions: List<String>? = null) {
            staticMethodInjections.add(objects.newInstance(StaticMethodInjection::class.java).also {
                it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                    it.cls.set(fromCls)
                    it.name.set(fromName)
                    it.desc.set(methodDesc)
                })
                it.targetClass.set(toCls)
                it.targetMethodName.set(toName)
                it.exceptions.set(exceptions)
                it.signature.set(signature)
            })
        }

        @JvmOverloads
        fun overloadArguments(from: String, to: String, converterOwner: String, converterName: String, permutate: Boolean = false) {
            argumentOverloadConverters.add(objects.newInstance(ArgumentOverloadConverter::class.java).also {
                it.from.set(from)
                it.to.set(to)
                it.converter.set(objects.newInstance(MethodDesc::class.java).also {
                    it.cls.set(converterOwner)
                    it.name.set(converterName)
                    it.desc.set("(L$from;)L$to;")
                })
                it.permutate.set(permutate)
            })
        }
    }
}