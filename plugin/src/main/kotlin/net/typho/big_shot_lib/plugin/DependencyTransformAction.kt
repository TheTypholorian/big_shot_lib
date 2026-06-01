package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.DependencyRemapper
import net.typho.big_shot_lib.plugin.transform.DependencyTransformer
import net.typho.big_shot_lib.plugin.transform.NeoTransformParameters
import net.typho.big_shot_lib.plugin.transform.TransformUtils
import org.gradle.api.artifacts.transform.CacheableTransform
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Classpath
import org.objectweb.asm.Opcodes

@CacheableTransform
abstract class DependencyTransformAction : TransformAction<NeoTransformParameters> {
    @get:Classpath
    @get:InputArtifact
    abstract val input: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val inFile = input.get().asFile
        val outFile = outputs.file("${inFile.nameWithoutExtension}-neo-tweaked${inFile.extension.let { if (it.isEmpty()) "" else ".$it" }}")

        val remapper = DependencyRemapper(parameters, Opcodes.ASM9)
        TransformUtils.transformJar(
            inFile,
            outFile,
            remapper
        ) { api, writer ->
            DependencyTransformer(
                parameters,
                { newDesc, oldDesc, argumentConverters -> }, // TODO
                remapper,
                api,
                writer
            )
        }
    }
}