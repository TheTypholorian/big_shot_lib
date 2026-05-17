package net.typho.big_shot_lib.plugin.transform.util

import org.gradle.api.provider.Property

interface MethodDesc {
    val cls: Property<String>
    val name: Property<String>
    val desc: Property<String>
}
