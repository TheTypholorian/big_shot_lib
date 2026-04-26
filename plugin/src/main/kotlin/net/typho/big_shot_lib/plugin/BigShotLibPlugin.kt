package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.task.GenerateModMetadataTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Attribute

class BigShotLibPlugin : Plugin<Project> {
    val tweaked: Attribute<Boolean> = Attribute.of("neoTweaked", Boolean::class.javaObjectType)

    override fun apply(project: Project) {
        val ext = project.extensions.create("bigShotLib", BigShotLibPluginExtension::class.java)

        val modMetadataTask = project.tasks.register("generateModMetadata", GenerateModMetadataTask::class.java) {
            it.group = "bigShotLib"
            it.metadata.set(ext.metadata)
        }
    }
}