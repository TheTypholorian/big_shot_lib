package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import org.lwjgl.system.NativeResource
import org.slf4j.LoggerFactory
import java.lang.ref.Cleaner
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Supplier

abstract class AntiLeakResource(
    id: Supplier<String>,
    cleanup: Boolean = true
) : NativeResource {
    @JvmField
    protected val cleanup: Runnable = if (cleanup && CHECKING) {
        val cleanup = createCleanup()
        val id = id
        val cleaned = AtomicBoolean(false)

        val cleanable = CLEANER.register(this) {
            if (cleaned.compareAndSet(false, true)) {
                LOGGER.warn("Resource '${id.get()}' was not cleaned up on time and got garbage collected")
                cleanup.run()
            }
        }
        Runnable {
            if (cleaned.compareAndSet(false, true)) {
                cleanup.run()
                cleanable.clean() // deregister the cleanable
            }
        }
    } else {
        createCleanup()
    }

    override fun free() {
        cleanup.run()
    }

    /**
     * **WARNING**: Returned lambda must not contain any reference to the original object
     */
    protected abstract fun createCleanup(): Runnable

    companion object {
        @JvmStatic
        @get:JvmName("isCheckingEnabled")
        @set:JvmName("setCheckingEnabled")
        var CHECKING = PlatformUtil.isDevEnv()

        @JvmStatic
        private val CLEANER = Cleaner.create()
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger("AntiLeakResource")
    }
}