package net.typho.big_shot_lib.api

import java.lang.instrument.Instrumentation
import java.nio.file.Files
import kotlin.io.path.absolutePathString
import kotlin.io.path.outputStream

object AgentLoader {
    @JvmStatic
    @get:JvmName("getInstrumentation")
    lateinit var INSTRUMENTATION: Instrumentation

    @JvmStatic
    @JvmOverloads
    fun loadAgent(
        owner: Class<*>,
        name: String,
        extraArgs: List<String> = listOf(),
        pid: Long = ProcessHandle.current().pid(),
    ) {
        owner.getResourceAsStream("/$name")!!.use { agentStream ->
            val tempDir = Files.createTempDirectory(BigShotLib.MOD_ID)
            val agentPath = tempDir.resolve("agent.jar")

            agentPath.outputStream().use { output ->
                agentStream.transferTo(output)
            }

            val process = ProcessBuilder(
                "java",
                "-jar",
                agentPath.absolutePathString(),
                pid.toString(),
                agentPath.absolutePathString(),
            )
                .also { it.command().addAll(extraArgs) }
                .inheritIO()
                .start()
            val exitCode = process.waitFor()

            if (exitCode != 0) {
                throw AssertionError("Unable to load agent, exit code $exitCode")
            }
        }
    }
}