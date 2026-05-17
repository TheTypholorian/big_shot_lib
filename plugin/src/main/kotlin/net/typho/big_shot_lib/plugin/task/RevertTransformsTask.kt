package net.typho.big_shot_lib.plugin.task

import net.typho.big_shot_lib.plugin.transform.ProjectRemapper
import net.typho.big_shot_lib.plugin.transform.ProjectTransformer
import net.typho.big_shot_lib.plugin.transform.util.AnnotationField
import net.typho.big_shot_lib.plugin.transform.util.AnnotationScanner
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.ClassRemapper
import java.util.jar.JarFile

abstract class RevertTransformsTask : DefaultTask() {
    @TaskAction
    fun run() {
        val annotations = AnnotationScanner(setOf(
            AnnotationField.NAMESPACE
        ))
        var visited = 0

        for (file in project.configurations.getByName("compileClasspath").resolve()) {
            if (file.extension == "jar") {
                JarFile(file, false).use { jar ->
                    for (entry in jar.stream()) {
                        if (entry.name.endsWith(".class")) {
                            visited++
                            ClassReader(jar.getInputStream(entry)).accept(annotations.createVisitor(), ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
                        }
                    }
                }
            }
        }

        val srcDir = project.layout.buildDirectory.dir("classes").get().asFile

        srcDir.walkTopDown().forEach { file ->
            if (file.extension == "class") {
                visited++
                ClassReader(file.readBytes()).accept(annotations.createVisitor(), ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
            }
        }

        println("Visited $visited files")

        srcDir.walkTopDown().forEach { file ->
            if (file.extension == "class") {
                val bytes = file.readBytes()
                val reader = ClassReader(bytes)
                val writer = ClassWriter(0)
                val remapper = ProjectRemapper(Opcodes.ASM9, annotations)
                val transformer = ProjectTransformer(Opcodes.ASM9, ClassRemapper(writer, remapper), annotations)
                reader.accept(transformer, 0)

                file.writeBytes(writer.toByteArray())
            }
        }
    }
}