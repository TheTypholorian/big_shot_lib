package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.transform.util.KotlinAndMixinSupportingClassRemapper
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.Remapper
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.jar.Manifest

object TransformUtils {
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
    fun transformSingleFile(
        name: String,
        remapper: Remapper,
        transformer: (api: Int, writer: ClassWriter) -> ClassVisitor,
        stream: InputStream,
        out: (name: String, stream: OutputStream.() -> Unit) -> Unit
    ) {
        if (!(name.startsWith("META-INF/") && (name.endsWith(".SF") || name.endsWith(".RSA") || name.endsWith(".DSA")))) {
            if (name.endsWith(".class") && !name.endsWith("-info.class")) {
                val className = name.removeSuffix(".class")

                val reader = ClassReader(stream)
                val writer = ClassWriter(reader, 0)
                val transformer = KotlinAndMixinSupportingClassRemapper(
                    Opcodes.ASM9,
                    transformer(Opcodes.ASM9, writer),
                    remapper
                )
                reader.accept(transformer, 0)

                val newName = remapper.map(className)

                if (className == newName) {
                    out(name) { write(writer.toByteArray()) }
                } else {
                    out("$newName.class") { write(writer.toByteArray()) }
                }
            } else if (name.endsWith(".java")) {
                val className = name.removeSuffix(".java")
                val newName = remapper.map(className)

                if (className == newName) {
                    out(name) { stream.transferTo(this) }
                } else {
                    out("$newName.java") { stream.transferTo(this) }
                }
            } else if (name.endsWith(".kt")) {
                val className = name.removeSuffix(".kt")
                val newName = remapper.map(className)

                if (className == newName) {
                    out(name) { stream.transferTo(this) }
                } else {
                    out("$newName.kt") { stream.transferTo(this) }
                }
            } else {
                out(name) { stream.transferTo(this) }
            }
        }
    }

    @JvmStatic
    fun transformJar(
        inFile: File,
        outFile: File,
        remapper: Remapper,
        transformer: (api: Int, writer: ClassWriter) -> ClassVisitor,
    ) {
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
                            transformSingleFile(entry.name, remapper, transformer, stream) { name, consumer ->
                                out.putNextEntry(JarEntry(name))
                                consumer(out)
                                out.closeEntry()
                            }
                        }
                    }
                }
            }
        }
    }
}