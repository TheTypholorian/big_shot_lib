package net.typho.big_shot_lib.impl

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModOrigin
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class BigShotLibMixinPlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String) {
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        return true
    }

    override fun acceptTargets(
        myTargets: Set<String>,
        otherTargets: Set<String>
    ) {
        Thread.currentThread().contextClassLoader.definedPackages.forEach { println("package $it") }
        println("accept targets")
        for (mod in FabricLoader.getInstance().allMods) {
            if (mod.metadata.id == "big_shot_lib" || mod.metadata.dependencies.any { it.modId == "big_shot_lib" }) {
                println("check mod ${mod.metadata.id}")
                if (mod.origin.kind == ModOrigin.Kind.PATH) {
                    for (rootPath in mod.origin.paths) {
                        println("path $rootPath")
                        try {
                            fun check(rootPath: Path, path: Path) {
                                if (path.endsWith(".jar")) {
                                    println("todo nested jars")
                                } else if (path.endsWith(".class")) {
                                }
                            }

                            Files.walk(rootPath).use { stream ->
                                stream.forEach { check(rootPath, it) }
                                /*
                                stream.forEach { classPath -> // .filter { it.endsWith(".class") }
                                    println("logging $classPath")
                                    val relPath = rootPath.relativize(classPath).toString()
                                    val namePath = relPath.substring(0, relPath.length - ".class".length)
                                        .replace(File.separatorChar, '.')
                                    val info = ClassInfo.forName(namePath)

                                    if (!info.isMixin) {
                                        ClassTinkerers.addTransformation(namePath) { node ->
                                            println("transforming ${node.name}")

                                            for (method in node.methods) {
                                                method.visibleAnnotations.replaceAll { annotation ->
                                                    println("${annotation.desc} on ${method.name}")
                                                    if (annotation.desc == "Lnet/typho/big_shot_lib/api/annotation/OnlyFor;") {
                                                        println("method ${method.name} has OnlyFor")
                                                    }

                                                    return@replaceAll annotation
                                                }
                                            }
                                        }
                                    }
                                }
                                 */
                            }
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                }
            }
        }
    }

    override fun getMixins(): List<String> {
        return listOf()
    }

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
        println("pre apply ${targetClassName} ${targetClass}")
    }

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }
}