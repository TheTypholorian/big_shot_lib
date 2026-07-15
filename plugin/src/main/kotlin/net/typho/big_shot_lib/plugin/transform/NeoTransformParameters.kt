package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.ArgumentOverloadConverter
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.ClassRename
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.FieldRename
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.InterfaceInjection
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.MethodRename
import net.typho.big_shot_lib.plugin.BigShotLibPluginExtension.TransformInfo.StaticMethodInjection
import net.typho.big_shot_lib.plugin.ModLoader
import net.typho.big_shot_lib.plugin.transform.util.MethodDesc
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface NeoTransformParameters : TransformParameters {
    @get:Input
    val classRenames: ListProperty<ClassRename>
    @get:Input
    val methodRenames: ListProperty<MethodRename>
    @get:Input
    val fieldRenames: ListProperty<FieldRename>
    @get:Input
    val markAsDeprecated: ListProperty<MethodDesc>
    @get:Input
    val interfaceInjections: ListProperty<InterfaceInjection>
    @get:Input
    val staticMethodInjections: ListProperty<StaticMethodInjection>
    @get:Input
    val argumentOverloadConverters: ListProperty<ArgumentOverloadConverter>
    @get:Input
    val version: Property<String>
    @get:Input
    val loader: Property<ModLoader>
    @get:Input
    val clientOnlyPackages: ListProperty<String>
    @get:Input
    val serverOnlyPackages: ListProperty<String>

    fun set(ext: BigShotLibPluginExtension, objects: () -> ObjectFactory) {
        classRenames.set(ext.transformInfo.classRenames)
        methodRenames.set(ext.transformInfo.methodRenames)
        fieldRenames.set(ext.transformInfo.fieldRenames)

        markAsDeprecated.set(ext.transformInfo.markAsDeprecated)

        interfaceInjections.set(ext.transformInfo.interfaceInjections)
        staticMethodInjections.set(ext.transformInfo.staticMethodInjections)
        argumentOverloadConverters.set(ext.transformInfo.argumentOverloadConverters)

        clientOnlyPackages.set(ext.transformInfo.clientOnlyPackages)
        serverOnlyPackages.set(ext.transformInfo.serverOnlyPackages)

        version.set(ext.mcVersionProperty)
        loader.set(ext.loader)
    }
}