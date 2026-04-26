package net.typho.big_shot_lib.plugin

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class BigShotLibPluginExtension @Inject constructor(objects: ObjectFactory) {
    val metadata: Metadata = objects.newInstance(Metadata::class.java)

    fun metadata(action: Action<in Metadata>) {
        action.execute(metadata)
    }

    abstract class Metadata {
        abstract val modId: Property<String>
        abstract val modName: Property<String>
        abstract val description: Property<String>
        abstract val authors: Property<Array<String>>
        abstract val homePage: Property<String>
        abstract val issuesPage: Property<String>
        abstract val sourcesPage: Property<String>
        abstract val license: Property<String>
    }
}