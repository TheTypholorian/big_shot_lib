package net.typho.big_shot_lib.impl.agent

import net.typho.big_shot_lib.api.agent.ClassWriterInfo
import net.typho.big_shot_lib.api.agent.OnlyInProcessor
import net.typho.big_shot_lib.api.event.ClassTransformEvent
import net.typho.big_shot_lib.api.util.EventGraph
import net.typho.big_shot_lib.common.annotation.Environment
import net.typho.big_shot_lib.common.loading.LoadingConstants
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.Instrumentation
import java.security.ProtectionDomain
import java.util.function.Consumer

internal object InstrumentationInit {
    @JvmStatic
    fun init(inst: Instrumentation, environment: Environment) {
        val transformGraph = EventGraph<String, ClassTransformEvent>()

        transformGraph.register(LoadingConstants.INSTRUMENTATION_STEP_ONLY_IN) { node, info, environment ->
            OnlyInProcessor.process(environment, node, info)
        }
        // TODO registration entrypoint

        val instGraph = EventGraph<String, Consumer<Instrumentation>>()

        instGraph.register(LoadingConstants.TRANSFORM_STEP_BASIC) { inst ->
            inst.addTransformer(object : ClassFileTransformer {
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

                        val info = ClassWriterInfo()

                        transformGraph.execute { it(node, info, environment) }

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
        }
        // TODO registration entrypoint

        instGraph.execute { it.accept(inst) }
    }
}