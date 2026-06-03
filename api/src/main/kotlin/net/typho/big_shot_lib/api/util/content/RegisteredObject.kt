package net.typho.big_shot_lib.api.util.content

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import net.typho.big_shot_lib.api.util.resource.NamedResource
import java.util.function.Consumer

interface RegisteredObject<T : Any> : NamedResource {
    /**
     * @throws IllegalStateException If [isRegistered] returns false
     */
    fun get(): T

    fun isRegistered(): Boolean

    fun addListener(out: Consumer<T>)

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun <T : Any> invoke(
            location: Identifier,
            constructor: () -> T
        ): RegisteredObject<T> = PlatformUtil.INSTANCE.createRegisteredObject(location, constructor)
    }
}