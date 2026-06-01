package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.NeoTransformParameters
import net.typho.big_shot_lib.plugin.transform.ProjectRemapper
import net.typho.big_shot_lib.plugin.transform.ProjectTransformer
import net.typho.big_shot_lib.plugin.transform.TransformUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.attributes.Attribute
import org.gradle.api.internal.provider.ValueSupplier.ValueProducer.task
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool
import org.objectweb.asm.Opcodes
import java.io.ByteArrayInputStream
import java.io.File
import kotlin.jvm.java

class BigShotLibPlugin : Plugin<Project> {
    @JvmField
    val neoTweakedAttrib: Attribute<Boolean> = Attribute.of(
        "big_shot_lib:tweaked",
        Boolean::class.javaObjectType
    )
    @JvmField
    val neoReverseTweakedAttrib: Attribute<Boolean> = Attribute.of(
        "big_shot_lib:reverse_tweaked",
        Boolean::class.javaObjectType
    )

    fun applyProjectTransforms(dir: File, parameters: NeoTransformParameters) {
        val remapper = ProjectRemapper(parameters, Opcodes.ASM9)

        dir.walkTopDown().forEach { file ->
            val rel = file.relativeTo(dir)

            if (rel.extension == "class") {
                TransformUtils.transformSingleFile(
                    rel.name,
                    remapper,
                    { api, writer -> ProjectTransformer(parameters, api, writer) },
                    ByteArrayInputStream(file.readBytes())
                ) { name, consumer ->
                    if (name == rel.name) {
                        file.outputStream().use {
                            consumer(it)
                        }
                    } else {
                        file.delete()

                        val newFile = File(name)
                        newFile.parentFile.mkdirs()

                        newFile.outputStream().use {
                            consumer(it)
                        }
                    }
                }
            }
        }
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create("bigShotLib", BigShotLibPluginExtension::class.java)

        project.afterEvaluate {
            println("[Big Shot Lib] Class Renames:")
            ext.transformInfo.classRenames.get().forEach { println("\t${it.from.get()} to ${it.to.get()}") }
            println("[Big Shot Lib] Method Renames:")
            ext.transformInfo.methodRenames.get().forEach { println("\t${it.from.get().cls.get()}.${it.from.get().name.get()} ${it.from.get().desc.get()} to '${it.to.get()}'") }
            println("[Big Shot Lib] Field Renames:")
            ext.transformInfo.fieldRenames.get().forEach { println("\t${it.from.get().cls.get()}.${it.from.get().name.get()} ${it.from.get().desc.get()} to '${it.to.get()}'") }
            println("[Big Shot Lib] Interface Injections:")
            ext.transformInfo.interfaceInjections.get().forEach { println("\t${it.iface.get()} to ${it.target.get()}") }
            println("[Big Shot Lib] Static Method Injections:")
            ext.transformInfo.staticMethodInjections.get().forEach { println("\t${it.targetClass.get()}.${it.targetMethodName.get()} ${it.redirectTo.get().cls.get()}.${it.redirectTo.get().name.get()} ${it.redirectTo.get().desc.get()}") }
        }

        project.pluginManager.withPlugin("java") {
            project.dependencies.attributesSchema.attribute(neoTweakedAttrib)

            project.dependencies.artifactTypes.configureEach {
                it.attributes.attribute(neoTweakedAttrib, false)
            }

            project.configurations.getByName("compileClasspath") {
                if (it.isCanBeResolved) {
                    it.attributes.attribute(neoTweakedAttrib, true)
                }
            }

            project.dependencies.registerTransform(DependencyTransformAction::class.java) {
                it.from.attribute(neoTweakedAttrib, false)
                it.to.attribute(neoTweakedAttrib, true)
                it.parameters.set(ext)
            }

            project.tasks.getByName("classes") { task ->
                println("[Big Shot Lib] Attaching project transforms to task '${task.name}'")
                val parameters = project.provider {
                    project.objects.newInstance(NeoTransformParameters::class.java).also { it.set(ext) }
                }

                task.inputs.property("neoParameters", parameters)

                if (project.pluginManager.hasPlugin("org.jetbrains.kotlin.jvm")) {
                    task.doLast {
                        println("classes with kotlin")
                        fun scanDependencies(task: Task) {
                            println("task $task")
                            task.taskDependencies.getDependencies(task).forEach { dependency ->
                                if (dependency is KotlinCompileTool) {
                                    applyProjectTransforms(dependency.destinationDirectory.get().asFile, parameters.get())
                                }

                                scanDependencies(dependency)
                            }
                        }

                        scanDependencies(task)
                    }
                }

                task.doLast {
                    println("classes")
                    fun scanDependencies(task: Task) {
                        task.taskDependencies.getDependencies(task).forEach { dependency ->
                            if (dependency is AbstractCompile) {
                                applyProjectTransforms(dependency.destinationDirectory.get().asFile, parameters.get())
                            }

                            scanDependencies(dependency)
                        }
                    }

                    scanDependencies(task)
                }
            }
        }
    }
}