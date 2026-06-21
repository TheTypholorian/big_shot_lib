package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.util.FieldDesc
import net.typho.big_shot_lib.plugin.transform.util.MethodDesc
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class BigShotLibPluginExtension @Inject constructor(objects: ObjectFactory) {
    abstract val version: Property<MCVersion>
    abstract val loader: Property<ModLoader>
    val transformInfo: TransformInfo = objects.newInstance(TransformInfo::class.java, version)

    fun transformInfo(action: Action<in TransformInfo>) {
        action.execute(transformInfo)
    }

    fun version(value: String) {
        version.set(MCVersion[value])
    }

    fun version(value: MCVersion) {
        version.set(value)
    }

    fun loader(value: String) {
        loader.set(ModLoader[value])
    }

    fun loader(value: ModLoader) {
        loader.set(value)
    }

    abstract class TransformInfo @Inject constructor(
        @JvmField
        val objects: ObjectFactory,
        @JvmField
        val version: Property<MCVersion>
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

            if (version < MCVersion.MC1_21) {
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "fromNamespaceAndPath", "fromNamespaceAndPath", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "createUntrusted", "createUntrusted", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "parse", "parse", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "withDefaultNamespace", "withDefaultNamespace", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "bySeparator", "bySeparator", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                injectStaticMethod("net/minecraft/resources/Identifier", "net/typho/big_shot_lib/impl/util/OldIdentifierUtil", "tryBySeparator", "tryBySeparator", "(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
            }

            if (version < MCVersion.MC1_21_11) {
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

            if (version >= MCVersion.MC26_1) {
                renameClass("net/minecraft/client/resources/model/geometry/BakedQuad", "net/minecraft/client/renderer/block/model/BakedQuad")
            }

            if (version >= MCVersion.MC26_2) {
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

            markAsDeprecated("com/mojang/blaze3d/vertex/VertexConsumer", "(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "addVertex")
            markAsDeprecated($$"com/mojang/blaze3d/vertex/VertexFormat$Builder", "()Lcom/mojang/blaze3d/vertex/VertexFormat;", "build")

            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "createUntrusted", "untrusted")
            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "fromNamespaceAndPath", "of")
            renameMethod("net/minecraft/resources/Identifier", "(Ljava/lang/String;)Lnet/minecraft/resources/Identifier;", "withDefaultNamespace", "minecraft")

            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV0", "TEXTURE_UV")
            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV1", "OVERLAY_UV")
            renameField("com/mojang/blaze3d/vertex/VertexFormatElement", "Lcom/mojang/blaze3d/vertex/VertexFormatElement;", "UV2", "LIGHT_UV")

            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "addVertex", "vertex")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(Lorg/joml/Vector3f;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "addVertex", "vertex")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", $$"(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lorg/joml/Vector3f;)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "addVertex", "vertex")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", $$"(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "addVertex", "vertex")

            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(Lorg/joml/Matrix4fc;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "addVertex", "vertex")

            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(IIII)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setColor", "color")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setColor", "color")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setColor", "color")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setWhiteAlpha", "colorRGB")

            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(FF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setUv", "textureUV")

            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(II)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setUv1", "overlayUV")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setOverlay", "overlayUV")

            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(II)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setUv2", "lightUV")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setLight", "lightUV")

            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", "(FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setNormal", "normal")
            renameMethod("com/mojang/blaze3d/vertex/VertexConsumer", $$"(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", "setNormal", "normal")

            injectInterface("net/typho/big_shot_lib/api/client/ext/VertexConsumerExtension", "com/mojang/blaze3d/vertex/VertexConsumer")
            injectInterface("net/typho/big_shot_lib/api/client/ext/VertexFormatBuilderExtension", $$"com/mojang/blaze3d/vertex/VertexFormat$Builder")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/util/NeoRenderType", "net/minecraft/client/renderer/RenderType")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/opengl/resource/GlUniform", "com/mojang/blaze3d/shaders/AbstractUniform")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/util/NeoVertexFormat", "com/mojang/blaze3d/vertex/VertexFormat")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/util/NeoGuiGraphics", "net/minecraft/client/gui/GuiGraphics")
            injectInterface("net/typho/big_shot_lib/api/client/ext/RenderTargetExtension", "com/mojang/blaze3d/pipeline/RenderTarget")
            injectInterface("net/typho/big_shot_lib/api/client/rendering/opengl/resource/GlTexture2D", "net/minecraft/client/renderer/texture/AbstractTexture")
            injectInterface("net/typho/big_shot_lib/api/ext/DirectionExtension", "net/minecraft/core/Direction")
            injectInterface("net/typho/big_shot_lib/api/ext/Vec3iExtension", "net/minecraft/core/Vec3i")
            injectInterface("net/typho/big_shot_lib/api/ext/BlockPosExtension", "net/minecraft/core/BlockPos")
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