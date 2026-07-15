package net.typho.big_shot_lib.plugin.deps

import net.typho.big_shot_lib.plugin.MCVersion
import net.typho.big_shot_lib.plugin.ModLoader
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.zip.ZipFile

abstract class RegisterModDependenciesTask : SourceTask() {
    @get:Input
    abstract val artifacts: ConfigurableFileCollection
    @get:Input
    abstract val loader: Property<ModLoader>
    @get:Input
    abstract val minecraftVersion: Property<String>
    @get:Input
    abstract val loaderVersion: Property<String>

    @TaskAction
    fun run() {
        loaderVersion.orNull?.let { loaderVersion ->
            val loader = loader.get()

            loader.manifestFile?.let { manifestFile ->
                val dependencies = mutableMapOf<String, String>()

                for (file in artifacts) {
                    if (file.extension == "jar" || file.extension == "zip") {
                        ZipFile(file).use { zip ->
                            zip.entries().iterator().forEach { entry ->
                                if (File(entry.name) == manifestFile) {
                                    zip.getInputStream(entry).use { stream ->
                                        val dep = loader.getModIdAndVersion(String(stream.readAllBytes()))
                                        dependencies[dep.first] = dep.second
                                    }
                                }
                            }
                        }
                    }
                }

                source.visit {
                    it.file.writeText(loader.appendModDependencies(it.file.readText(), MCVersion[minecraftVersion.get()], loaderVersion, dependencies))
                }
            }
        }
    }
}