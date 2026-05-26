package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.*
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.ArgumentOverloadConverter
import net.typho.big_shot_lib.plugin.transform.DependencyRemapper
import net.typho.big_shot_lib.plugin.transform.DependencyTransformer
import net.typho.big_shot_lib.plugin.transform.util.KotlinAndMixinSupportingClassRemapper
import net.typho.big_shot_lib.plugin.transform.util.MethodDesc
import org.gradle.api.artifacts.transform.CacheableTransform
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.jar.Manifest

@CacheableTransform
abstract class DependencyTransformAction : TransformAction<DependencyTransformAction.Parameters> {
    @get:Classpath
    @get:InputArtifact
    abstract val input: Provider<FileSystemLocation>

    companion object {
        @JvmStatic
        fun getAutomaticModuleName(name: String): String {
            var name = name.removeSuffix(".jar")

            val versionIndex = Regex("-\\d+(\\.|$)").find(name)?.range?.first

            if (versionIndex != null) {
                name = name.substring(0, versionIndex)
            }

            return name.replace(Regex("[^A-Za-z0-9]"), ".").replace(Regex("\\.+"), ".").trim('.')
        }

        @JvmStatic
        fun transformEntry(
            entry: JarEntry,
            remapper: DependencyRemapper,
            parameters: Parameters,
            stream: InputStream,
            out: (entry: JarEntry, stream: OutputStream.() -> Unit) -> Unit
        ) {
            if (!(entry.name.startsWith("META-INF/") && (entry.name.endsWith(".SF") || entry.name.endsWith(".RSA") || entry.name.endsWith(".DSA")))) {
                if (entry.name.endsWith(".class") && !entry.name.endsWith("-info.class")) {
                    val className = entry.name.removeSuffix(".class")

                    val reader = ClassReader(stream)
                    val writer = ClassWriter(reader, 0)
                    val transformer = KotlinAndMixinSupportingClassRemapper(
                        Opcodes.ASM9,
                        DependencyTransformer(
                            parameters,
                            { newDesc, oldDesc, argumentConverters -> }, // TODO
                            remapper,
                            Opcodes.ASM9,
                            writer
                        ),
                        remapper
                    )
                    reader.accept(transformer, 0)

                    val newName = remapper.map(className)

                    if (className == newName) {
                        out(entry) { write(writer.toByteArray()) }
                    } else {
                        out(JarEntry("$newName.class")) { write(writer.toByteArray()) }
                    }
                } else if (entry.name.endsWith(".java")) {
                    val className = entry.name.removeSuffix(".java")
                    val newName = remapper.map(className)

                    if (className == newName) {
                        out(entry) { stream.transferTo(this) }
                    } else {
                        out(JarEntry("$newName.java")) { stream.transferTo(this) }
                    }
                } else if (entry.name.endsWith(".kt")) {
                    val className = entry.name.removeSuffix(".kt")
                    val newName = remapper.map(className)

                    if (className == newName) {
                        out(entry) { stream.transferTo(this) }
                    } else {
                        out(JarEntry("$newName.kt")) { stream.transferTo(this) }
                    }
                } else {
                    out(entry) { stream.transferTo(this) }
                }
            }
        }

        @JvmStatic
        fun transformTo(
            inFile: File,
            outFile: File,
            parameters: Parameters
        ) {
            val remapper = DependencyRemapper(parameters, Opcodes.ASM9)

            JarFile(inFile, false).use { jar ->
                val manifest = jar.manifest ?: Manifest()

                if (manifest.mainAttributes.getValue("Automatic-Module-Name") == null) {
                    manifest.mainAttributes.putValue(
                        "Automatic-Module-Name",
                        getAutomaticModuleName(inFile.name)
                    )
                }

                JarOutputStream(FileOutputStream(outFile), manifest).use { out ->
                    jar.entries().asIterator().forEach { entry ->
                        if (entry.name != "META-INF/MANIFEST.MF") {
                            jar.getInputStream(entry).use { stream ->
                                transformEntry(entry, remapper, parameters, stream) { entry, consumer ->
                                    out.putNextEntry(entry)
                                    consumer(out)
                                    out.closeEntry()
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
        @JvmStatic
        fun transformUnary(
            file: File,
            parameters: Parameters
        ) {
            val remapper = DependencyRemapper(parameters, Opcodes.ASM9)
            val entries = hashMapOf<String, ByteArray>()

            JarFile(file, false).use { jar ->
                jar.entries().asIterator().forEach { entry ->
                    jar.getInputStream(entry).use { stream ->
                        transformEntry(entry, remapper, parameters, stream) { entry, consumer ->
                            val stream = ByteArrayOutputStream()
                            consumer(stream)
                            entries[entry.name] = stream.toByteArray()
                        }
                    }
                }
            }

            JarOutputStream(FileOutputStream(file)).use { out ->
                for (entry in entries) {
                    out.putNextEntry(JarEntry(entry.key))
                    out.write(entry.value)
                    out.closeEntry()
                }
            }
        }
         */
    }

    override fun transform(outputs: TransformOutputs) {
        val inFile = input.get().asFile
        val outFile = outputs.file("${inFile.nameWithoutExtension}-neo-tweaked${inFile.extension.let { if (it.isEmpty()) "" else ".$it" }}")
        transformTo(inFile, outFile, parameters)
    }

    interface Parameters : TransformParameters {
        @get:Input
        val classRenames: ListProperty<ClassRename>
        @get:Input
        val methodRenames: ListProperty<MethodRename>
        @get:Input
        val fieldRenames: ListProperty<FieldRename>
        @get:Input
        val markAsDeprecated: ListProperty<MethodDesc>
        @get:Input
        val interfaceInjections: ListProperty<InterfaceInjection>
        @get:Input
        val staticMethodInjections: ListProperty<StaticMethodInjection>
        @get:Input
        val argumentOverloadConverters: ListProperty<ArgumentOverloadConverter>
        @get:Input
        val version: Property<MCVersion>
        @get:Input
        val loader: Property<ModLoader>
    }
}