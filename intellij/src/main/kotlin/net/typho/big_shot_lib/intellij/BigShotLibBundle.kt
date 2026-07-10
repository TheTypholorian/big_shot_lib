package net.typho.big_shot_lib.intellij

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

internal object BigShotLibBundle {
    private const val BUNDLE = "messages.BigShotLibBundle"
    private val instance = DynamicBundle(BigShotLibBundle::class.java, BUNDLE)

    @JvmStatic
    fun message(key: @PropertyKey(resourceBundle = BUNDLE) String, vararg params: Any?): String {
        return instance.getMessage(key, *params)
    }
}
