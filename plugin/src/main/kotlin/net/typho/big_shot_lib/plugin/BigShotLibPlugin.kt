package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.task.GenerateModMetadataTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.internal.fingerprint.classpath.impl.ClasspathFingerprintingStrategy.runtimeClasspath
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

class BigShotLibPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("bigShotLib", BigShotLibPluginExtension::class.java)

        val modMetadataTask = project.tasks.register("generateModMetadata", GenerateModMetadataTask::class.java) {
            it.group = "bigShotLib"
            it.metadata.set(ext.metadata)
            it.destination.set(project.layout.buildDirectory.dir("generated/bigShotLib"))
        }

        project.pluginManager.withPlugin("java") {
            project.configurations.named("compileClasspath") {
                it.attributes {
                    it.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "transformed-jar")
                }
            }

            project.dependencies.registerTransform(DependencyTransform::class.java) {
                it.from.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "jar")
                it.to.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "transformed-jar")
            }

            //val outDir = project.layout.buildDirectory.dir("bigShotLib/classes")

            project.tasks.named("classes") {
                it.doLast {
                    val srcDir = project.layout.buildDirectory.dir("classes").get().asFile

                    //srcDir.copyRecursively(dstDir, overwrite = true)

                    srcDir.walkTopDown().forEach { file ->
                        if (file.extension == "class") {
                            val bytes = file.readBytes()
                            val reader = ClassReader(bytes)
                            val writer = ClassWriter(0)
                            val visitor = DependencyTransform.ReverseVisitor(writer)
                            reader.accept(visitor, 0)
                            file.writeBytes(writer.toByteArray())
                        }
                    }
                }
            }
        }

        //project.tasks.getByName("processResources") {
        //    it.dependsOn(modMetadataTask)
        //}
    }
}