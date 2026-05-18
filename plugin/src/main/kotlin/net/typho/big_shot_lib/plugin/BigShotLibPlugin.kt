package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.task.GenerateModMetadataTask
import net.typho.big_shot_lib.plugin.transform.ProjectRemapper
import net.typho.big_shot_lib.plugin.transform.ProjectTransformer
import net.typho.big_shot_lib.plugin.transform.util.AnnotationField
import net.typho.big_shot_lib.plugin.transform.util.AnnotationScanner
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.bundling.Jar
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.ClassRemapper
import java.io.File
import java.util.jar.JarFile
import kotlin.jvm.java

class BigShotLibPlugin : Plugin<Project> {
    fun applyProjectTransforms(project: Project, inputs: FileCollection, out: File, ext: BigShotLibPluginExtension) {
        println("[Big Shot Lib] Applying project transforms")

        out.deleteRecursively()
        out.mkdirs()

        val annotations = AnnotationScanner(setOf(
            AnnotationField.NAMESPACE
        ))
        var visited = 0

        for (file in project.configurations.getByName("compileClasspath").resolve()) {
            if (file.extension == "jar") {
                JarFile(file, false).use { jar ->
                    for (entry in jar.stream()) {
                        if (entry.name.endsWith(".class") && !entry.name.endsWith("-info.class")) {
                            visited++
                            ClassReader(jar.getInputStream(entry)).accept(annotations.createVisitor(), ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
                        }
                    }
                }
            }
        }

        inputs.forEach {
            it.walkTopDown().forEach { file ->
                if (file.extension == "class" && !file.endsWith("-info.class")) {
                    visited++
                    ClassReader(file.readBytes()).accept(annotations.createVisitor(), ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
                }
            }
        }

        println("\tLoaded annotations from $visited jar files")
        visited = 0

        inputs.forEach { file ->
            if (file.extension == "class" && !file.endsWith("-info.class")) {
                val bytes = file.readBytes()
                val reader = ClassReader(bytes)
                val writer = ClassWriter(0)
                val remapper = ProjectRemapper(ext.transformInfo, Opcodes.ASM9, annotations)
                val transformer = ProjectTransformer(Opcodes.ASM9, ClassRemapper(writer, remapper))
                reader.accept(transformer, 0)

                val target = out.resolve("${transformer.desc!!.name}.class")
                target.parentFile.mkdirs()
                target.writeBytes(writer.toByteArray())
                visited++
            }
        }

        println("\tProcessed $visited class files")
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create("bigShotLib", BigShotLibPluginExtension::class.java)

        val modMetadataTask = project.tasks.register("generateModMetadata", GenerateModMetadataTask::class.java) {
            it.group = "big_shot_lib"
            it.metadata.set(ext.metadata)
            it.destination.set(project.layout.buildDirectory.dir("generated/bigShotLib"))
        }

        project.afterEvaluate {
            println("[Big Shot Lib] Class Renames:")
            ext.transformInfo.classRenames.get().forEach { println("\t${it.from.get()} to '${it.to.get()}'") }
            println("[Big Shot Lib] Method Renames:")
            ext.transformInfo.methodRenames.get().forEach { println("\t${it.from.get().cls.get()}.${it.from.get().name.get()} ${it.from.get().desc.get()} to '${it.to.get()}'") }
            println("[Big Shot Lib] Field Renames:")
            ext.transformInfo.fieldRenames.get().forEach { println("\t${it.from.get().cls.get()}.${it.from.get().name.get()} ${it.from.get().desc.get()} to '${it.to.get()}'") }
        }

        project.pluginManager.withPlugin("java") {
            project.configurations.named("compileClasspath") {
                it.attributes {
                    it.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "big-shot-jar")
                }
            }
            project.configurations.named("runtimeClasspath") {
                it.attributes {
                    it.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "big-shot-jar")
                }
            }

            project.dependencies.registerTransform(DependencyTransformAction::class.java) {
                it.from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "jar")
                it.to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "big-shot-jar")
                it.parameters.classRenames.set(ext.transformInfo.classRenames)
                it.parameters.methodRenames.set(ext.transformInfo.methodRenames)
                it.parameters.fieldRenames.set(ext.transformInfo.fieldRenames)
            }

            //project.tasks.getByName("processResources") {
            //    it.dependsOn(modMetadataTask)
            //}

            project.tasks.withType(Jar::class.java) { task ->
                println("[Big Shot Lib] Attaching project transforms to task '${task.name}'")
                val out = project.layout.buildDirectory.dir("big_shot_classes").get().asFile
                val inputs = task.inputs.files

                task.inputs.dir(out)

                task.doFirst {
                    applyProjectTransforms(project, inputs, out, ext)
                }
            }
        }
    }
}