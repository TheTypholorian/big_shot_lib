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
    val metadata: Metadata = objects.newInstance(Metadata::class.java)
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
        abstract val interfaceInjections: ListProperty<InterfaceInjection>
        abstract val staticMethodInjections: ListProperty<StaticMethodInjection>
        abstract val argumentOverloadConverters: ListProperty<ArgumentOverloadConverter>
        abstract val applyPostCompileTransforms: Property<Boolean>

        init {
            classRenames.convention(listOf(
                objects.newInstance(ClassRename::class.java).also {
                    it.from.set("net/minecraft/resources/ResourceLocation")
                    it.to.set("net/minecraft/resources/Identifier")
                }
            ))
            methodRenames.convention(listOf())
            fieldRenames.convention(listOf())
            interfaceInjections.convention(listOf())
            staticMethodInjections.convention(version.map {
                if (it < MCVersion.MC1_21) {
                    listOf(
                        objects.newInstance(StaticMethodInjection::class.java).also {
                            it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                                it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                                it.name.set("fromNamespaceAndPath")
                                it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                            })
                            it.targetClass.set("net/minecraft/resources/Identifier")
                            it.targetMethodName.set("fromNamespaceAndPath")
                        },
                        objects.newInstance(StaticMethodInjection::class.java).also {
                            it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                                it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                                it.name.set("fromNamespaceAndPath")
                                it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                            })
                            it.targetClass.set("net/minecraft/resources/Identifier")
                            it.targetMethodName.set("createUntrusted")
                        },
                        objects.newInstance(StaticMethodInjection::class.java).also {
                            it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                                it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                                it.name.set("parse")
                                it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                            })
                            it.targetClass.set("net/minecraft/resources/Identifier")
                            it.targetMethodName.set("parse")
                        },
                        objects.newInstance(StaticMethodInjection::class.java).also {
                            it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                                it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                                it.name.set("withDefaultNamespace")
                                it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                            })
                            it.targetClass.set("net/minecraft/resources/Identifier")
                            it.targetMethodName.set("withDefaultNamespace")
                        },
                        objects.newInstance(StaticMethodInjection::class.java).also {
                            it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                                it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                                it.name.set("bySeparator")
                                it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                            })
                            it.targetClass.set("net/minecraft/resources/Identifier")
                            it.targetMethodName.set("bySeparator")
                        },
                        objects.newInstance(StaticMethodInjection::class.java).also {
                            it.redirectTo.set(objects.newInstance(MethodDesc::class.java).also {
                                it.cls.set("net/typho/big_shot_lib/impl/util/OldIdentifierUtil")
                                it.name.set("tryBySeparator")
                                it.desc.set("(Ljava/lang/String;Ljava/lang/String;)L/net/minecraft/resources/Identifier;")
                            })
                            it.targetClass.set("net/minecraft/resources/Identifier")
                            it.targetMethodName.set("tryBySeparator")
                        }
                    )
                } else {
                    listOf()
                }
            })
            argumentOverloadConverters.convention(listOf())
            applyPostCompileTransforms.convention(true)
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