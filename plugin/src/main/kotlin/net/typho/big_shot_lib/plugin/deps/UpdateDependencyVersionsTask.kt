package net.typho.big_shot_lib.plugin.deps

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.util.Properties
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

abstract class UpdateDependencyVersionsTask : DefaultTask() {
    @get:Input
    abstract val deps: Property<ModDependencies>

    @TaskAction
    fun run() {
        val deps = deps.get()

        val properties = Properties()

        if (deps.file.exists()) {
            deps.file.inputStream().use(properties::load)
        }

        for ((dependency, version) in properties) {
            if (deps.getVersion(dependency as String, true) == version) {
                println("[Big Shot Lib] Dependency $dependency is up to date.")
            }
        }
    }
}