package net.typho.big_shot_lib.plugin.transform

import net.typho.big_shot_lib.plugin.transform.util.ClassStatusVisitor
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes

abstract class TransformTask : DefaultTask() {
    @get:InputFile
    abstract val inputJar: RegularFileProperty
    @get:Input
    abstract val parameters: Property<NeoTransformParameters>

    @get:OutputFile
    abstract val outputJar: RegularFileProperty

    @TaskAction
    fun run() {
        val parameters = parameters.get()

        TransformUtils.transformJar(
            inputJar.get().asFile,
            outputJar.get().asFile,
            true,
            { markChanged -> ToProdRemapper(parameters, markChanged, Opcodes.ASM9) },
            { name, api, reader ->
                val visitor = ClassStatusVisitor(api)
                reader.accept(visitor, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
                return@transformJar visitor.status != ClassStatusVisitor.Status.RUNTIME
            },
            { api, writer, remapper, markChanged -> ToProdTransformer(parameters, markChanged, api, writer) }
        )
    }
}