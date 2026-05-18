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
    val metadata: Metadata = objects.newInstance(Metadata::class.java)
    val transformInfo: TransformInfo = objects.newInstance(TransformInfo::class.java)

    fun metadata(action: Action<in Metadata>) {
        action.execute(metadata)
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

    abstract class Metadata {
        abstract val modId: Property<String>
        abstract val modName: Property<String>
        abstract val description: Property<String>
        abstract val authors: Property<Array<String>>
        abstract val homePage: Property<String>
        abstract val issuesPage: Property<String>
        abstract val sourcesPage: Property<String>
        abstract val license: Property<String>
    }

    abstract class TransformInfo @Inject constructor(
        @JvmField
        val objects: ObjectFactory
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

        abstract val classRenames: ListProperty<ClassRename>
        abstract val methodRenames: ListProperty<MethodRename>
        abstract val fieldRenames: ListProperty<FieldRename>

        init {
            classRenames.convention(listOf())
            methodRenames.convention(listOf())
            fieldRenames.convention(listOf())
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