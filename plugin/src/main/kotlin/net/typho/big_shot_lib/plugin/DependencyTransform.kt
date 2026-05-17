package net.typho.big_shot_lib.plugin

import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

abstract class DependencyTransform : TransformAction<DependencyTransform.Parameters> {
    @get:InputArtifact
    abstract val input: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val inFile = input.get().asFile
        val outFile = outputs.file(inFile.name.replace(".jar", "-neo-tweaked.jar"))
        println("Transforming $inFile $outFile")

        JarFile(inFile).use { jar ->
            JarOutputStream(FileOutputStream(outFile)).use { out ->
                for (entry in jar.stream()) {
                    jar.getInputStream(entry).use { stream ->
                        val newEntry = JarEntry(entry)
                        out.putNextEntry(newEntry)

                        if (entry.name.endsWith(".class")) {
                            val reader = ClassReader(stream)
                            val writer = ClassWriter(0)
                            val visitor = Visitor(writer)
                            reader.accept(visitor, 0)
                            out.write(writer.toByteArray())
                        } else {
                            stream.transferTo(out)
                        }

                        out.closeEntry()
                    }
                }
            }
        }
    }

    class Visitor(visitor: ClassVisitor) : ClassVisitor(Opcodes.ASM9, visitor) {
        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String?>?
        ): MethodVisitor? {
            return super.visitMethod(access, if (name == "accept") "test_accept" else name, descriptor, signature, exceptions)
        }
    }

    class ReverseVisitor(visitor: ClassVisitor) : ClassVisitor(Opcodes.ASM9, visitor) {
        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String?>?
        ): MethodVisitor {
            return object : MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                override fun visitMethodInsn(
                    opcode: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?,
                    isInterface: Boolean
                ) {
                    super.visitMethodInsn(opcode, owner, if (name == "test_accept") "accept" else name, descriptor, isInterface)
                }
            }
        }
    }

    interface Parameters : TransformParameters {
    }
}