package net.typho.big_shot_lib.plugin

import net.typho.big_shot_lib.plugin.transform.ToCompileRemapper
import net.typho.big_shot_lib.plugin.transform.ToCompileTransformer
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
abstract class BigShotLibTransformAction : TransformAction<NeoTransformParameters> {
    @get:Classpath
    @get:InputArtifact
    abstract val input: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val inFile = input.get().asFile
        println("[Big Shot Lib] Transforming $inFile")

        if (inFile.isDirectory) {
            val outFile = outputs.file("${inFile.name}-neo-tweaked")

            val remapper = ToCompileRemapper(parameters, Opcodes.ASM9)
            TransformUtils.transformDir(
                inFile,
                outFile,
                remapper,
                { name, api, reader -> true }
            ) { api, writer ->
                ToCompileTransformer(
                    parameters,
                    remapper,
                    api,
                    writer
                )
            }
        } else if (inFile.extension == "jar") {
            val outFile = outputs.file("${inFile.nameWithoutExtension}-neo-tweaked${inFile.extension.let { if (it.isEmpty()) "" else ".$it" }}")

            val remapper = ToCompileRemapper(parameters, Opcodes.ASM9)
            TransformUtils.transformJar(
                inFile,
                outFile,
                remapper,
                { name, api, reader -> true }
            ) { api, writer ->
                ToCompileTransformer(
                    parameters,
                    remapper,
                    api,
                    writer
                )
            }
        } else {
            System.err.println("[Big Shot Lib] Unsupported transform input: $inFile")
            throw IllegalArgumentException()
        }
    }
}