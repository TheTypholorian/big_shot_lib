package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.ProjectRemapper
import net.typho.big_shot_lib.plugin.transform.ProjectTransformer
import net.typho.big_shot_lib.plugin.transform.util.AnnotationScanner
import net.typho.big_shot_lib.plugin.transform.util.Annotations
import net.typho.big_shot_lib.plugin.transform.util.KotlinAndMixinSupportingClassRemapper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.attributes.Attribute
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.bundling.Jar
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.File
import java.util.jar.JarFile
import kotlin.jvm.java

class BigShotLibPlugin : Plugin<Project> {
    fun applyProjectTransforms(project: Project, inputs: FileCollection, out: File, ext: BigShotLibPluginExtension) {
        if (!ext.transformInfo.applyPostCompileTransforms.get()) {
            return
        }

        println("[Big Shot Lib] Applying project transforms")

        out.deleteRecursively()
        out.mkdirs()

        val annotations = AnnotationScanner(project.objects, setOf(
            Annotations.NAMESPACE,
        ))
        var visited = 0

        for (file in project.configurations.getByName("compileClasspath").resolve()) {
            if (file.extension == "jar") {
                JarFile(file, false).use { jar ->
                    jar.entries().asIterator().forEach { entry ->
                        if (entry.name.endsWith(".class") && !entry.name.endsWith("-info.class")) {
                            visited++
                            ClassReader(jar.getInputStream(entry)).accept(annotations.createVisitor(), ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
                        }
                    }
                }
            }
        }

        inputs.forEach {
            it.walkTopDown().forEach { file ->
                if (file.extension == "class" && !file.endsWith("-info.class")) {
                    visited++
                    file.inputStream().use { stream ->
                        ClassReader(stream).accept(annotations.createVisitor(), ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
                    }
                }
            }
        }

        println("\tLoaded annotations from $visited class files:")
        println("\t\t${annotations.classes.values.sumOf { it.size }} class annotations")
        println("\t\t${annotations.methods.values.sumOf { it.size }} method annotations")
        println("\t\t${annotations.fields.values.sumOf { it.size }} field annotations")
        visited = 0

        val remapper = ProjectRemapper(ext, Opcodes.ASM9, annotations)

        inputs.forEach { file ->
            if (file.extension == "class" && !file.endsWith("-info.class")) {
                file.inputStream().use { stream ->
                    val reader = ClassReader(stream)
                    val writer = ClassWriter(0)
                    val transformer = ProjectTransformer(ext, Opcodes.ASM9, KotlinAndMixinSupportingClassRemapper(Opcodes.ASM9, writer, remapper))
                    reader.accept(transformer, 0)

                    if (ext.loader.get().mappedOnlyInAnnotationName != transformer.desc!!) {
                        val target = out.resolve("${transformer.desc!!}.class")
                        target.parentFile.mkdirs()
                        target.writeBytes(writer.toByteArray())
                        visited++
                    }
                }
            }
        }

        println("\tProcessed $visited class files")
    }

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

        project.pluginManager.withPlugin("java") {
            project.dependencies.attributesSchema.attribute(neoTweakedAttrib)
            project.dependencies.artifactTypes.getByName("jar").attributes.attribute(neoTweakedAttrib, false)

            project.configurations.configureEach {
                if (it.isCanBeResolved) {
                    it.attributes.attribute(neoTweakedAttrib, true)
                }
            }

            project.dependencies.registerTransform(DependencyTransformAction::class.java) {
                it.from.attribute(neoTweakedAttrib, false)
                it.to.attribute(neoTweakedAttrib, true)

                it.parameters.classRenames.set(ext.transformInfo.classRenames)
                it.parameters.methodRenames.set(ext.transformInfo.methodRenames)
                it.parameters.fieldRenames.set(ext.transformInfo.fieldRenames)

                it.parameters.interfaceInjections.set(ext.transformInfo.interfaceInjections)
                it.parameters.staticMethodInjections.set(ext.transformInfo.staticMethodInjections)
                it.parameters.argumentOverloadConverters.set(ext.transformInfo.argumentOverloadConverters)

                it.parameters.version.set(ext.version)
                it.parameters.loader.set(ext.loader)
            }

            //project.tasks.getByName("processResources") {
            //    it.dependsOn(modMetadataTask)
            //}

            project.tasks.withType(Jar::class.java) { task ->
                println("[Big Shot Lib] Attaching project transforms to task '${task.name}'")
                val out = project.layout.buildDirectory.dir("big_shot_classes").get().asFile
                val inputs = task.inputs.files

                task.inputs.dir(out)

                task.doFirst {
                    out.mkdirs()
                    applyProjectTransforms(project, inputs, out, ext)
                }
            }
        }
    }
}