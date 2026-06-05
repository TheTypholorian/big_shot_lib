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
    abstract val hasFletchingTable: Property<Boolean>
    val transformInfo: TransformInfo = objects.newInstance(TransformInfo::class.java, version)

    init {
        hasFletchingTable.convention(false)
    }

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
            classRenames.convention(version.map {
                val list = mutableListOf<ClassRename>()

                if (it < MCVersion.MC1_21_11) {
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/resources/ResourceLocation")
                        it.to.set("net/minecraft/resources/Identifier")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/util/ResourceLocationPattern")
                        it.to.set("net/minecraft/util/IdentifierPattern")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/ResourceLocationException")
                        it.to.set("net/minecraft/IdentifierException")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/client/resources/model/ModelResourceLocation")
                        it.to.set("net/minecraft/client/resources/model/ModelIdentifier")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/commands/arguments/ResourceLocationArgument")
                        it.to.set("net/minecraft/commands/arguments/IdentifierArgument")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/util/parsing/packrat/commands/ResourceLocationParseRule")
                        it.to.set("net/minecraft/util/parsing/packrat/commands/IdentifierParseRule")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/client/searchtree/ResourceLocationSearchTree")
                        it.to.set("net/minecraft/client/searchtree/IdentifierSearchTree")
                    })
                } else {
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/client/renderer/rendertype/LayeringTransform")
                        it.to.set("net/minecraft/client/renderer/LayeringTransform")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/client/renderer/rendertype/OutputTarget")
                        it.to.set("net/minecraft/client/renderer/OutputTarget")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/client/renderer/rendertype/RenderSetup")
                        it.to.set("net/minecraft/client/renderer/RenderSetup")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/client/renderer/rendertype/RenderType")
                        it.to.set("net/minecraft/client/renderer/RenderType")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/client/renderer/rendertype/RenderTypes")
                        it.to.set("net/minecraft/client/renderer/RenderTypes")
                    })
                    list.add(objects.newInstance(ClassRename::class.java).also {
                        it.from.set("net/minecraft/client/renderer/rendertype/TextureTransform")
                        it.to.set("net/minecraft/client/renderer/TextureTransform")
                    })
                }

                list
            })
            methodRenames.convention(listOf())
            fieldRenames.convention(listOf())
            interfaceInjections.convention(listOf())
            markAsDeprecated.convention(listOf())
            staticMethodInjections.convention(version.map {
                val list = mutableListOf<StaticMethodInjection>()

                if (it < MCVersion.MC1_21) {
                    list.add(objects.newInstance(StaticMethodInjection::class.java).also {
                        it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                            it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                            it.name.set("fromNamespaceAndPath")
                            it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                        })
                        it.targetClass.set("net/minecraft/resources/Identifier")
                        it.targetMethodName.set("fromNamespaceAndPath")
                    })
                    list.add(objects.newInstance(StaticMethodInjection::class.java).also {
                        it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                            it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                            it.name.set("fromNamespaceAndPath")
                            it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                        })
                        it.targetClass.set("net/minecraft/resources/Identifier")
                        it.targetMethodName.set("createUntrusted")
                    })
                    list.add(objects.newInstance(StaticMethodInjection::class.java).also {
                        it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                            it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                            it.name.set("parse")
                            it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                        })
                        it.targetClass.set("net/minecraft/resources/Identifier")
                        it.targetMethodName.set("parse")
                    })
                    list.add(objects.newInstance(StaticMethodInjection::class.java).also {
                        it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                            it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                            it.name.set("withDefaultNamespace")
                            it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                        })
                        it.targetClass.set("net/minecraft/resources/Identifier")
                        it.targetMethodName.set("withDefaultNamespace")
                    })
                    list.add(objects.newInstance(StaticMethodInjection::class.java).also {
                        it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                            it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                            it.name.set("bySeparator")
                            it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                        })
                        it.targetClass.set("net/minecraft/resources/Identifier")
                        it.targetMethodName.set("bySeparator")
                    })
                    list.add(objects.newInstance(StaticMethodInjection::class.java).also {
                        it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                            it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                            it.name.set("tryBySeparator")
                            it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                        })
                        it.targetClass.set("net/minecraft/resources/Identifier")
                        it.targetMethodName.set("tryBySeparator")
                    })
                }

                list
            })
            argumentOverloadConverters.convention(listOf())
            clientOnlyPackages.convention(listOf())
            serverOnlyPackages.convention(listOf())
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

        fun markAsDeprecated(cls: String, name: String, desc: String) {
            markAsDeprecated.add(objects.newInstance(MethodDesc::class.java).also {
                it.cls.set(cls)
                it.name.set(name)
                it.desc.set(desc)
            })
        }

        fun defaultDeprecatedMethods() {
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "addVertex",
                "(FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setColor",
                "(IIII)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setUv",
                "(FF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setUv1",
                "(II)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setUv2",
                "(II)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setNormal",
                "(FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setColor",
                "(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setColor",
                "(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setWhiteAlpha",
                "(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setLight",
                "(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setOverlay",
                "(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "addVertex",
                "(Lorg/joml/Vector3f;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "addVertex",
                $$"(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lorg/joml/Vector3f;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "addVertex",
                $$"(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "addVertex",
                "(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
            markAsDeprecated(
                "com/mojang/blaze3d/vertex/VertexConsumer",
                "setNormal",
                $$"(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
        }

        fun defaultInterfaceInjections() {
            injectInterface(
                "net/typho/big_shot_lib/api/client/rendering/util/NeoRenderType",
                "net/minecraft/client/renderer/RenderType"
            )
            injectInterface(
                "net/typho/big_shot_lib/api/client/rendering/util/NeoVertexConsumer",
                "com/mojang/blaze3d/vertex/VertexConsumer"
            )
            injectInterface(
                "net/typho/big_shot_lib/api/client/rendering/opengl/resource/GlUniform",
                "com/mojang/blaze3d/shaders/AbstractUniform"
            )
            injectInterface(
                "net/typho/big_shot_lib/api/client/rendering/util/NeoVertexFormat",
                "com/mojang/blaze3d/vertex/VertexFormat"
            )
            injectInterface(
                "net/typho/big_shot_lib/api/client/rendering/util/NeoGuiGraphics",
                "net/minecraft/client/gui/GuiGraphics"
            )
            injectInterface(
                "net/typho/big_shot_lib/api/client/rendering/opengl/resource/NeoRenderTarget",
                "com/mojang/blaze3d/pipeline/RenderTarget"
            )
            injectInterface(
                "net/typho/big_shot_lib/api/client/rendering/opengl/resource/GlTexture2D",
                "net/minecraft/client/renderer/texture/AbstractTexture"
            )
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

        fun shortIdentifierMethods() {
            renameMethod(
                "net/minecraft/resources/Identifier",
                "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;",
                "createUntrusted",
                "untrusted"
            )
            renameMethod(
                "net/minecraft/resources/Identifier",
                "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/Identifier;",
                "fromNamespaceAndPath",
                "of"
            )
            renameMethod(
                "net/minecraft/resources/Identifier",
                "(Ljava/lang/String;)Lnet/minecraft/resources/Identifier;",
                "withDefaultNamespace",
                "minecraft"
            )
        }
    }
}