package net.typho.big_shot_lib.plugin.task

import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class GenerateModMetadataTask : DefaultTask() {
    @get:Input
    abstract val metadata: Property<BigShotLibPluginExtension.Metadata>

    @TaskAction
    fun run() {
        println(metadata.get().modId.get())
        println(metadata.get().modId.isPresent)
        println(metadata.get().description.isPresent)
    }
}