package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.task.GenerateModMetadataTask
import net.typho.big_shot_lib.plugin.transform.ProjectRemapper
import net.typho.big_shot_lib.plugin.transform.ProjectTransformer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.ClassRemapper

class BigShotLibPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("bigShotLib", BigShotLibPluginExtension::class.java)

        val modMetadataTask = project.tasks.register("generateModMetadata", GenerateModMetadataTask::class.java) {
            it.group = "big_shot_lib"
            it.metadata.set(ext.metadata)
            it.destination.set(project.layout.buildDirectory.dir("generated/bigShotLib"))
        }

        val revertTransformsTask = project.tasks.register("revertBigShotTransforms") {
            it.group = "big_shot_lib"
            val srcDir = project.layout.buildDirectory.dir("classes").get().asFile

            srcDir.walkTopDown().forEach { file ->
                if (file.extension == "class") {
                    val bytes = file.readBytes()
                    val reader = ClassReader(bytes)
                    val writer = ClassWriter(0)
                    val remapper = ProjectRemapper(Opcodes.ASM9)
                    val transformer = ProjectTransformer(Opcodes.ASM9, ClassRemapper(writer, remapper))
                    reader.accept(transformer, 0)

                    file.writeBytes(writer.toByteArray())
                }
            }
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
            }

            //project.tasks.getByName("processResources") {
            //    it.dependsOn(modMetadataTask)
            //}

            project.tasks.named("classes") {
                it.dependsOn(revertTransformsTask)
            }
        }
    }
}