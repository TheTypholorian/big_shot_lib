package net.typho.big_shot_lib.plugin.task

import com.google.gson.stream.JsonWriter
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.FileWriter

abstract class GenerateModMetadataTask : DefaultTask() {
    @get:Input
    abstract val metadata: Property<BigShotLibPluginExtension.Metadata>
    @get:OutputDirectory
    abstract val destination: DirectoryProperty

    @TaskAction
    fun run() {
        println(destination.get())
        val metadata = metadata.get()
        val dir = destination.asFile.get()
        dir.mkdirs()

        JsonWriter(FileWriter(dir.resolve("fabric.mod.json"))).use { out ->
            out.setIndent("\t")
            out.beginObject()

            out.name("id").value(metadata.modId.get())
            metadata.modName.orNull?.let { out.name("name").value(it) }
            metadata.description.orNull?.let { out.name("description").value(it) }
            metadata.authors.orNull?.let {
                out.name("authors").beginArray()

                for (author in it) {
                    out.value(author)
                }

                out.endArray()
            }

            out.name("contact").beginObject()
            metadata.homePage.orNull?.let { out.name("homepage").value(it) }
            metadata.issuesPage.orNull?.let { out.name("issues").value(it) }
            metadata.sourcesPage.orNull?.let { out.name("sources").value(it) }
            out.endObject()

            metadata.license.orNull?.let { out.name("license").value(it) }

            out.endObject()
        }
    }
}