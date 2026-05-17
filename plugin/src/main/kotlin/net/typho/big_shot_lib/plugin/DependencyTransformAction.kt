package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.DependencyRemapper
import net.typho.big_shot_lib.plugin.transform.DependencyTransformer
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.internal.file.impl.DefaultFileMetadata.file
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.ClassRemapper
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

abstract class DependencyTransformAction : TransformAction<DependencyTransformAction.Parameters> {
    @get:InputArtifact
    abstract val input: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val inFile = input.get().asFile
        val outFile = outputs.file(inFile.name.replace(".jar", "-neo-tweaked.jar"))

        val remapper = DependencyRemapper(Opcodes.ASM9)

        JarFile(inFile, false).use { jar ->
            JarOutputStream(FileOutputStream(outFile)).use { out ->
                for (entry in jar.stream()) {
                    jar.getInputStream(entry).use { stream ->
                        if (entry.name.endsWith(".class")) {
                            val reader = ClassReader(stream)
                            val writer = ClassWriter(0)
                            val transformer = DependencyTransformer(Opcodes.ASM9, ClassRemapper(writer, remapper))
                            reader.accept(transformer, 0)

                            val className = entry.name.removeSuffix(".class")
                            val newName = remapper.map(className)

                            if (className == newName) {
                                out.putNextEntry(JarEntry(entry))
                            } else {
                                out.putNextEntry(JarEntry("$newName.class"))
                            }

                            out.write(writer.toByteArray())
                        } else if (entry.name.endsWith(".java")) {
                            val className = entry.name.removeSuffix(".java")
                            val newName = remapper.map(className)

                            if (className == newName) {
                                out.putNextEntry(JarEntry(entry))
                            } else {
                                out.putNextEntry(JarEntry("$newName.java"))
                            }

                            stream.transferTo(out)
                        } else if (entry.name.endsWith(".kt")) {
                            val className = entry.name.removeSuffix(".kt")
                            val newName = remapper.map(className)

                            if (className == newName) {
                                out.putNextEntry(JarEntry(entry))
                            } else {
                                out.putNextEntry(JarEntry("$newName.kt"))
                            }

                            stream.transferTo(out)
                        } else {
                            out.putNextEntry(JarEntry(entry))
                            stream.transferTo(out)
                        }

                        out.closeEntry()
                    }
                }
            }
        }
    }

    interface Parameters : TransformParameters
}