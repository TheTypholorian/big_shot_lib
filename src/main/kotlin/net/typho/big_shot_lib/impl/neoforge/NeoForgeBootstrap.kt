package net.typho.big_shot_lib.impl.neoforge

import net.neoforged.neoforgespi.earlywindow.GraphicsBootstrapper
import net.typho.big_shot_lib.api.AgentLoader
import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.api.agent.ClassInstrumentationInfo
import net.typho.big_shot_lib.api.agent.OnlyInProcessor
import net.typho.big_shot_lib.common.annotation.Environment
import net.typho.big_shot_lib.common.annotation.OnlyIn
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

class NeoForgeBootstrap : GraphicsBootstrapper {
    override fun name() = BigShotLib.MOD_ID

    override fun bootstrap(arguments: Array<String>) {
        AgentLoader.loadAgent(javaClass, "agent.jar")

        AgentLoader.INSTRUMENTATION.addTransformer(object : ClassFileTransformer {
            override fun transform(
                loader: ClassLoader?,
                className: String,
                classBeingRedefined: Class<*>?,
                protectionDomain: ProtectionDomain?,
                classfileBuffer: ByteArray
            ): ByteArray? {
                try {
                    val node = ClassNode()
                    ClassReader(classfileBuffer).accept(node, 0)

                    val info = ClassInstrumentationInfo()

                    OnlyInProcessor.process(Environment.CLIENT, node, info)

                    if (info.changed) {
                        val writer = ClassWriter(info.writerFlags)
                        node.accept(writer)
                        return writer.toByteArray()
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }

                return null
            }
        })
        test()
    }

    fun test() {
        Test.test()
    }

    @OnlyIn(Environment.SERVER)
    object Test {
        @OnlyIn(Environment.SERVER)
        fun test() {
            println("testing failed ugh")
        }
    }
}