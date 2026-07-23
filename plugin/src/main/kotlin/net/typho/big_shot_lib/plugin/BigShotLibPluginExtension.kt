package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.deps.ModDependencies
import net.typho.big_shot_lib.plugin.transform.data.ClassRename
import net.typho.big_shot_lib.plugin.transform.data.FieldRename
import net.typho.big_shot_lib.plugin.transform.data.InterfaceInjection
import net.typho.big_shot_lib.plugin.transform.data.MethodRename
import net.typho.big_shot_lib.plugin.transform.data.StaticMethodInjection
import net.typho.big_shot_lib.plugin.transform.data.FieldDesc
import net.typho.big_shot_lib.plugin.transform.data.MethodDesc
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class BigShotLibPluginExtension @Inject constructor(
    objects: ObjectFactory,
    private val project: Project
) {
    abstract val mcVersionProperty: Property<String>
    abstract val loader: Property<ModLoader>
    @JvmField
    val transformInfo: TransformInfo = objects.newInstance(TransformInfo::class.java, mcVersionProperty)
    @JvmField
    val deps = objects.newInstance(ModDependencies::class.java, project).also {
        it.mcVersionProperty.set(mcVersionProperty)
        it.loader.set(loader)
    }

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

    abstract class TransformInfo @Inject constructor(
        private val objects: ObjectFactory,
        private val version: Property<String>
    ) {
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

                injectInterface("net/typho/big_shot_lib/client/api/rendering/common/GpuBuffer", "com/mojang/blaze3d/buffers/GpuBufferImpl")
                injectInterface("net/typho/big_shot_lib/client/api/rendering/common/GpuTexture", "com/mojang/blaze3d/textures/GpuTextureImpl")

                injectInterface("net/typho/big_shot_lib/client/api/ext/RenderPassExtension", "com/mojang/blaze3d/systems/RenderPassBackend")
                injectInterface("net/typho/big_shot_lib/client/api/ext/RenderPassExtension", "com/mojang/blaze3d/systems/RenderPass")
            }

            markAsDeprecated("com/mojang/blaze3d/vertex/VertexConsumer", "(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "addVertex")
            markAsDeprecated($$"com/mojang/blaze3d/vertex/VertexFormat$Builder", "()Lcom/mojang/blaze3d/vertex/VertexFormat;", "build")

            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "createUntrusted", "untrusted")
            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "fromNamespaceAndPath", "of")
            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "withDefaultNamespace", "minecraft")

            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV0", "TEXTURE_UV")
            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV1", "OVERLAY_UV")
            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV2", "LIGHT_UV")

            injectInterface("net/typho/big_shot_lib/client/api/ext/VertexConsumerExtension", "com/mojang/blaze3d/vertex/VertexConsumer")
            injectInterface("net/typho/big_shot_lib/client/api/ext/VertexFormatBuilderExtension", $$"com/mojang/blaze3d/vertex/VertexFormat$Builder")
            injectInterface("net/typho/big_shot_lib/client/api/ext/RenderTypeExtension", "net/minecraft/client/renderer/RenderType")

            injectInterface("net/typho/big_shot_lib/client/api/rendering/common/constant/GpuAlphaFunction", "com/mojang/blaze3d/platform/CompareOp")
            injectInterface("net/typho/big_shot_lib/client/api/rendering/common/constant/GpuBlendFactor", "com/mojang/blaze3d/platform/BlendFactor")
            injectInterface("net/typho/big_shot_lib/client/api/rendering/common/constant/GpuDataType", $$"com/mojang/blaze3d/GpuFormat$ComponentType")
            injectInterface("net/typho/big_shot_lib/client/api/rendering/common/constant/GpuIndexType", "com/mojang/blaze3d/IndexType")
            injectInterface("net/typho/big_shot_lib/client/api/rendering/common/constant/GpuShaderType", "com/mojang/blaze3d/shaders/ShaderType")
            injectInterface("net/typho/big_shot_lib/client/api/rendering/common/constant/GpuTextureFormat", "com/mojang/blaze3d/GpuFormat")

            injectInterface("net/typho/big_shot_lib/api/ext/DirectionExtension", "net/minecraft/core/Direction")
            injectInterface("net/typho/big_shot_lib/api/ext/Vec3iExtension", "net/minecraft/core/Vec3i")
            injectInterface("net/typho/big_shot_lib/api/ext/BlockPosExtension", "net/minecraft/core/BlockPos")
            injectInterface("net/typho/big_shot_lib/api/ext/MutableBlockPosExtension", $$"net/minecraft/core/BlockPos$MutableBlockPos")
            injectInterface("net/typho/big_shot_lib/api/ext/Vec3Extension", "net/minecraft/world/phys/Vec3")
            injectInterface("net/typho/big_shot_lib/api/ext/IdentifierExtension", "net/minecraft/resources/Identifier")
        }

        fun renameClass(from: String, to: String) {
            classRenames.add(ClassRename(from, to))
        }

        fun renameMethod(from: MethodDesc, to: String) {
            methodRenames.add(MethodRename(from, to))
        }

        fun renameMethod(cls: String, desc: String, from: String, to: String) {
            renameMethod(MethodDesc(cls, from, desc), to)
        }

        fun renameField(from: FieldDesc, to: String) {
            fieldRenames.add(FieldRename(from, to))
        }

        fun renameField(cls: String, desc: String, from: String, to: String) {
            renameField(FieldDesc(cls, from, desc), to)
        }

        fun markAsDeprecated(desc: MethodDesc) {
            markAsDeprecated.add(desc)
        }

        fun markAsDeprecated(cls: String, desc: String, name: String) {
            markAsDeprecated(MethodDesc(cls, desc, name))
        }

        @JvmOverloads
        fun injectInterface(iface: String, target: String, typeParams: List<String> = listOf()) {
            interfaceInjections.add(InterfaceInjection(iface, target, typeParams))
        }

        @JvmOverloads
        fun injectStaticMethod(fromCls: String, toCls: String, fromName: String, toName: String, methodDesc: String, signature: String? = null, exceptions: List<String> = listOf()) {
            staticMethodInjections.add(StaticMethodInjection(
                MethodDesc(fromCls, fromName, methodDesc),
                toCls,
                toName,
                signature,
                exceptions
            ))
        }

        @JvmOverloads
        fun overloadArguments(from: String, to: String, converterOwner: String, converterName: String, permutate: Boolean = false) {
            argumentOverloadConverters.add(ArgumentOverloadConverter(
                from,
                to,
                MethodDesc(converterOwner, converterName, "(L$from;)L$to;"),
                permutate
            ))
        }
    }
}