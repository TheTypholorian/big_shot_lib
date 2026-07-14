package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.dependencies.dependencyPropertyFile
import net.typho.big_shot_lib.plugin.dependencies.getFabricLoaderVersion
import net.typho.big_shot_lib.plugin.dependencies.getModrinthProjectVersion
import net.typho.big_shot_lib.plugin.dependencies.getNeoForgeLoaderVersion
import net.typho.big_shot_lib.plugin.transform.ToCompileRemapper
import net.typho.big_shot_lib.plugin.transform.ToCompileTransformer
import net.typho.big_shot_lib.plugin.transform.NeoTransformParameters
import net.typho.big_shot_lib.plugin.transform.ToRuntimeRemapper
import net.typho.big_shot_lib.plugin.transform.ToRuntimeTransformer
import net.typho.big_shot_lib.plugin.transform.TransformUtils
import net.typho.big_shot_lib.plugin.transform.util.ClassStatusVisitor
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.attributes.Attribute
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileTool
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import java.io.File
import java.util.Properties
import kotlin.jvm.java

class BigShotLibPlugin : Plugin<Project> {
    @JvmField
    val neoTweakedAttrib: Attribute<Boolean> = Attribute.of(
        "big_shot_lib:tweaked",
        Boolean::class.javaObjectType
    )

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

        project.tasks.register("updateDependencyVersions") { task ->
            task.group = "big_shot_lib"
            task.description = "Update cached versions for all modrinth dependencies"

            task.doLast {
                val properties = Properties()
                val propertiesFile = project.dependencyPropertyFile

                if (propertiesFile.exists()) {
                    propertiesFile.inputStream().use(properties::load)
                }

                for ((dependency, version) in properties) {
                    val newVersion = when (dependency) {
                        "fabric-loader" -> project.getFabricLoaderVersion(true)
                        "neoforge-loader" -> project.getNeoForgeLoaderVersion(true)
                        else -> project.getModrinthProjectVersion(dependency as String, true)
                    }

                    if (newVersion == version) {
                        println("[Big Shot Lib] Dependency $dependency is up to date.")
                    }
                }
            }
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

            project.dependencies.registerTransform(BigShotLibTransformAction::class.java) {
                it.from.attribute(neoTweakedAttrib, false)
                it.to.attribute(neoTweakedAttrib, true)
                it.parameters.set(ext) { project.objects }
            }

            project.afterEvaluate {
                fun toCompile(dir: File, parameters: NeoTransformParameters) {
                    val remapper = ToCompileRemapper(parameters, Opcodes.ASM9)

                    TransformUtils.transformDir(
                        dir,
                        dir,
                        remapper,
                        { name, api, reader ->
                            val visitor = ClassStatusVisitor(api)
                            reader.accept(visitor, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
                            return@transformDir visitor.status == ClassStatusVisitor.Status.RUNTIME
                        },
                        { api, writer -> ToCompileTransformer(parameters, remapper, api, writer) }
                    )
                }

                fun toRuntime(dir: File, parameters: NeoTransformParameters) {
                    val remapper = ToRuntimeRemapper(parameters, Opcodes.ASM9)

                    TransformUtils.transformDir(
                        dir,
                        dir,
                        remapper,
                        { name, api, reader ->
                            val visitor = ClassStatusVisitor(api)
                            reader.accept(visitor, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
                            return@transformDir visitor.status != ClassStatusVisitor.Status.RUNTIME
                        },
                        { api, writer -> ToRuntimeTransformer(parameters, api, writer) }
                    )
                }

                project.tasks.getByName("classes") { task ->
                    println("[Big Shot Lib] Attaching project transforms to task '${task.name}'")
                    val parameters = project.provider {
                        project.objects.newInstance(NeoTransformParameters::class.java).also { it.set(ext) { project.objects } }
                    }

                    task.inputs.property("neoParameters", parameters)

                    val compileTasks = arrayListOf<Pair<Task, DirectoryProperty>>()

                    if (project.pluginManager.hasPlugin("org.jetbrains.kotlin.jvm")) {
                        val scanned = hashSetOf<Task>()

                        fun scanDependencies(task: Task) {
                            task.taskDependencies.getDependencies(task).forEach { dependency ->
                                if (scanned.add(dependency)) {
                                    if (dependency is KotlinCompileTool) {
                                        compileTasks.add(dependency to dependency.destinationDirectory)
                                    } else if (dependency is AbstractCompile) {
                                        compileTasks.add(dependency to dependency.destinationDirectory)
                                    }

                                    scanDependencies(dependency)
                                }
                            }
                        }

                        scanDependencies(task)
                    } else {
                        val scanned = hashSetOf<Task>()

                        fun scanDependencies(task: Task) {
                            task.taskDependencies.getDependencies(task).forEach { dependency ->
                                if (scanned.add(dependency)) {
                                    if (dependency is AbstractCompile) {
                                        compileTasks.add(dependency to dependency.destinationDirectory)
                                    }

                                    scanDependencies(dependency)
                                }
                            }
                        }

                        scanDependencies(task)
                    }

                    val reverseTransformsTasks = compileTasks.map { task ->
                        project.tasks.register("neoReverseTransform_${task.first.name}") {
                            it.group = "big_shot_lib"

                            val parameters = project.provider {
                                project.objects.newInstance(NeoTransformParameters::class.java).also { it.set(ext) { project.objects } }
                            }

                            it.inputs.property("neoParameters", parameters)

                            it.doFirst {
                                toCompile(task.second.get().asFile, parameters.get())
                            }
                        }
                    }.toTypedArray()

                    compileTasks.forEach { task ->
                        task.first.dependsOn(*reverseTransformsTasks)
                    }

                    task.doLast {
                        compileTasks.forEach { task ->
                            toRuntime(task.second.get().asFile, parameters.get())
                        }
                    }
                }
            }
        }
    }
}