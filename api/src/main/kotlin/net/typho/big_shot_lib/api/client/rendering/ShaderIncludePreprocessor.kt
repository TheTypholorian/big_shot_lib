package net.typho.big_shot_lib.api.client.rendering

import com.sun.org.apache.xpath.internal.operations.Bool
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

object ShaderIncludePreprocessor : ShaderPreprocessor {
    override val location: NeoIdentifier = BigShotApi.id("shader_includes")

    override fun apply(
        location: NeoIdentifier,
        code: String,
        manager: NeoResourceManager
    ): String {
        var code = code
        var index: Int = -1

        fun hasIncludes(): Boolean {
            index = code.indexOf("#include")
            return index != -1
        }

        while (hasIncludes()) {
        }
    }
}