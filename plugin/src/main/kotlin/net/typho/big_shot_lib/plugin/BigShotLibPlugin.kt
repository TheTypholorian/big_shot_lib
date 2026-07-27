package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.deps.UpdateDependencyVersionsTask
import net.typho.big_shot_lib.plugin.transform.TransformTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Attribute
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.plugins.JavaPluginExtension
import kotlin.jvm.java

class BigShotLibPlugin : Plugin<Project> {
    @JvmField
    val neoTweakedAttrib: Attribute<Boolean> = Attribute.of(
        "big_shot_lib:tweaked",
        Boolean::class.javaObjectType
    )

    override fun apply(project: Project) {
        val ext = project.extensions.create("bigShotLib", BigShotLibPluginExtension::class.java, project)

        val modDependency = project.configurations.create("modDependency") {
            it.isCanBeResolved = true
            it.isCanBeConsumed = false
            it.description = "Adds dependencies to the mod manifest"
        }

        project.dependencies.attributesSchema.attribute(neoTweakedAttrib)

        project.dependencies.artifactTypes.configureEach {
            it.attributes.attribute(neoTweakedAttrib, false)
        }

        project.configurations.configureEach {
            if (it.isCanBeResolved) {
                it.attributes.attribute(neoTweakedAttrib, true)
            }
        }

        project.dependencies.registerTransform(BigShotLibTransformAction::class.java) {
            it.from.attribute(neoTweakedAttrib, false)
            it.to.attribute(neoTweakedAttrib, true)
            it.parameters.set(ext)
        }

        /*
        project.tasks.register("registerModDependencies", RegisterModDependenciesTask::class.java) { task ->
            task.group = "big_shot_lib"
            task.dependsOn("processResources")

            task.source(ext.loader.map { loader ->
                loader.manifestFile?.let { manifest ->
                    (project.extensions.findByName("sourceSets") as SourceSetContainer).map {
                        it.output.resourcesDir!!.resolve(manifest)
                    }
                } ?: listOf()
            })
            task.loader.set(ext.loader)
            task.artifacts.from(modDependency)
            task.minecraftVersion.set(ext.mcVersionProperty)
            task.loaderVersion.set(project.provider { ext.getLoaderVersion() })
        }.also { project.tasks.getByName("processResources").finalizedBy(it) }
         */

        project.tasks.register("updateDependencyVersions", UpdateDependencyVersionsTask::class.java) { task ->
            task.group = "big_shot_lib"
            task.description = "Update cached versions for all dependencies"
            task.deps.set(project.provider { ext.deps })
        }
    }
}